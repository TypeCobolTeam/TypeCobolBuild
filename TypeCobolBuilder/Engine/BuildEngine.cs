using Mono.Options;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using TypeCobol.Compiler.CodeModel;
using TypeCobol.Server;

namespace TypeCobolBuilder.Engine
{
    /// <summary>
    /// This the TypeCobolBuilder Engine
    /// </summary>
    public class BuildEngine
    {
        static BuildEngine()
        {
            //For debugging it can be usefull to track where a message is printed

            //Console.SetOut(new StandardOutput(Console.Out));
            //Console.SetError(new StandardOutput(Console.Error));
        }

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="tcbInterop">The tcb instance use for interoperability</param>
        public BuildEngine(tcb.TCB tcbInterop)
        {
            TcbInterop = tcbInterop;
            InitTcbInterop();
        }

        /// <summary>
        /// Init Tcb Interoperability
        /// </summary>
        private void InitTcbInterop()
        {
            if (TcbInterop != null)
            {   //Important set the main compilation function
                TcbInterop.MainCompileCmd = Main;
                //Function Login function
                TcbInterop.LogError = LogError;
                TcbInterop.LogInfo = LogInfo;
                TcbInterop.LogWarning = LogWarning;
            }
        }

        /// <summary>
        /// The External File Provider instance if any.
        /// </summary>
        public tcb.TCB TcbInterop
        {
            get;
            set;
        }

        /// <summary>
        /// Taken from TypeCobol CLI
        /// </summary>
        class Config
        {
            public TypeCobol.Compiler.DocumentFormat Format = TypeCobol.Compiler.DocumentFormat.RDZReferenceFormat;
            public bool Codegen = false;
            public List<string> InputFiles = new List<string>();
            public List<string> OutputFiles = new List<string>();
            public string ErrorFile = null;
            public bool IsErrorXML
            {
                get { return ErrorFile != null && ErrorFile.ToLower().EndsWith(".xml"); }
            }
            public List<string> Copies = new List<string>();
        }

        /// <summary>
        /// Log an error message
        /// </summary>
        /// <param name="msg"></param>
        /// <returns>null</returns>
        public object LogError(String msg)
        {
            Logger.GetLogger().Error(msg);
            return null;
        }

        /// <summary>
        /// Log an info message
        /// </summary>
        /// <param name="msg"></param>
        /// <returns>null</returns>
        public object LogInfo(String msg)
        {
            Logger.GetLogger().Info(msg);
            return null;
        }

        /// <summary>
        /// Log a warning message
        /// </summary>
        /// <param name="msg">The message</param>
        /// <returns>null</returns>
        public object LogWarning(String msg)
        {
            Logger.GetLogger().Warn(msg);
            return null;
        }

        /// <summary>
        /// Taken from TypeCobol CLI
        /// <returns>0 if OK, an different value if not OK</returns>
        /// </summary>
        public int Main(string[] argv)
        {
            bool help = false;
            bool version = false;
            bool once = false;
            var config = new Config();
            var pipename = "TypeCobol.Server";

            var p = new OptionSet() {
				"USAGE",
				"  "+PROGNAME+" [OPTIONS]... [PIPENAME]",
				"",
				"VERSION:",
				"  "+PROGVERSION,
				"",
				"DESCRIPTION:",
				"  Run the TypeCobol parser server.",
				{ "1|once",  "Parse one set of files and exit. If present, this option does NOT launch the server.", v => once = (v!=null) },
				{ "i|input=", "{PATH} to an input file to parse. This option can be specified more than once.", (string v) => config.InputFiles.Add(v) },
				{ "o|output=","{PATH} to an ouput file where to generate code. This option can be specified more than once.", (string v) => config.OutputFiles.Add(v) },
				{ "g|generate",  "If present, this option generates code corresponding to each input file parsed.", v => config.Codegen = (v!=null) },
				{ "d|diagnostics=", "{PATH} to the error diagnostics file.", (string v) => config.ErrorFile = v },
//				{ "p|pipename=",  "{NAME} of the communication pipe to use. Default: "+pipename+".", (string v) => pipename = v },
				{ "e|encoding=", "{ENCODING} of the file(s) to parse. It can be one of \"rdz\"(this is the default), \"zos\", or \"utf8\". "
								+"If this option is not present, the parser will attempt to guess the {ENCODING} automatically.",
								(string v) => config.Format = CreateFormat(v)
				},
				{ "y|copy=", "{PATH} to a copy to load. This option can be specified more than once.", (string v) => config.Copies.Add(v) },
				{ "h|help",  "Output a usage message and exit.", v => help = (v!=null) },
				{ "V|version",  "Output the version number of "+PROGNAME+" and exit.", v => version = (v!=null) },
			};
            System.Collections.Generic.List<string> args;
            try { args = p.Parse(argv); }
            catch (OptionException ex) 
            { 
                return exit(1, ex.Message); 
            }

            if (help)
            {
                p.WriteOptionDescriptions(System.Console.Out);
                return 0;
            }
            if (version)
            {
                System.Console.WriteLine(PROGVERSION);
                return 0;
            }
            if (config.OutputFiles.Count > 0 && config.InputFiles.Count != config.OutputFiles.Count)
                return exit(2, "The number of output files must be equal to the number of input files.");
            if (config.OutputFiles.Count == 0 && config.Codegen)
                foreach (var path in config.InputFiles) config.OutputFiles.Add(path + ".cee");

            if (args.Count > 0) pipename = args[0];

            if (once)
            {
                return -runOnce(config);
            }
            else
            {
                //TODO: JCM -- Not supportted runServer
                //runServer(pipename);
            }

            return 0;
        }

        /// <summary>
        /// Taken from TypeCobol CLI
        /// </summary>
        private TypeCobol.Compiler.DocumentFormat CreateFormat(string encoding)
        {
            if (encoding == null) return null;
            if (encoding.ToLower().Equals("zos")) return TypeCobol.Compiler.DocumentFormat.ZOsReferenceFormat;
            if (encoding.ToLower().Equals("utf8")) return TypeCobol.Compiler.DocumentFormat.FreeUTF8Format;
            /*if (encoding.ToLower().Equals("rdz"))*/
            return TypeCobol.Compiler.DocumentFormat.RDZReferenceFormat;
        }

        /// <summary>
        /// Taken from TypeCobol CLI
        /// </summary>
        private int runOnce(Config config)
        {
            TextWriter w;
            if (config.ErrorFile == null) w = System.Console.Error;
            else w = File.CreateText(config.ErrorFile);
            AbstractErrorWriter writer;
            if (config.IsErrorXML) writer = new XMLWriter(w);
            else writer = new ConsoleWriter(w);
            writer.Outputs = config.OutputFiles;

            var parser = new BuildParser(this, "TypeCobol.Server");
            parser.CustomSymbols = loadCopies(config.Copies);

            int iHasErrors = 0;
            for (int c = 0; c < config.InputFiles.Count; c++)
            {
                string path = config.InputFiles[c];
                try { parser.Init(path, config.Format); }
                catch (System.IO.IOException ex)
                {
                    AddError(writer, ex.Message, path);
                    continue;
                }
                parser.Parse(path);
                if (parser.Results.CodeElementsDocumentSnapshot == null)
                {
                    AddError(writer, "File \"" + path + "\" has syntactic error(s) preventing codegen (CodeElements).", path);
                    continue;
                }

                writer.AddErrors(path, parser.Converter.AsDiagnostics(parser.Results.CodeElementsDocumentSnapshot.ParserDiagnostics));
                // no need to add errors from parser.CodeElementsSnapshot.CodeElements
                // as they are on parser.CodeElementsSnapshot.CodeElements which are added below

                if (parser.Results.ProgramClassDocumentSnapshot == null)
                {
                    AddError(writer, "File \"" + path + "\" has semantic error(s) preventing codegen (ProgramClass).", path);
                    continue;
                }
                iHasErrors += parser.Results.ProgramClassDocumentSnapshot.Diagnostics.Count;
                writer.AddErrors(path, parser.Converter.AsDiagnostics(parser.Results.ProgramClassDocumentSnapshot.Diagnostics));
                foreach (var e in parser.Results.CodeElementsDocumentSnapshot.CodeElements)
                {
                    if (e.Diagnostics.Count < 1) continue;
                    writer.AddErrors(path, parser.Converter.GetDiagnostics(e));
                }

                if (config.Codegen)
                {
                    var codegen = new TypeCobol.Compiler.Generator.TypeCobolGenerator(parser.Source, config.Format, parser.Results.ProgramClassDocumentSnapshot);
                    if (codegen.IsValid)
                    {
                        var stream = new StreamWriter(config.OutputFiles[c]);
                        codegen.WriteCobol(stream);
                        System.Console.WriteLine("Code generated to file \"" + config.OutputFiles[c] + "\".");
                    }
                    else
                    {
                        // might be a problem regarding the input file format
                        AddError(writer, "Codegen failed for \"" + path + "\" (no Program). Check file format/encoding?", path);
                    }
                }
            }
            writer.Write();
            writer.Flush();
            return iHasErrors;
        }

        /// <summary>
        /// Taken from TypeCobol CLI
        /// </summary>
        private void AddError(AbstractErrorWriter writer, string message, string path)
        {
            var error = new TypeCobol.Tools.Diagnostic();
            error.Message = message;
            error.Code = "codegen";
            try 
            { 
                //TODO: JCM -- access Inputs error code               
                //error.Source = writer.Inputs[path]; 
                error.Source = writer.Count.ToString(); 
            }
            catch (KeyNotFoundException /*ex*/) 
            { 
                error.Source = writer.Count.ToString(); 
            }
            var list = new List<TypeCobol.Tools.Diagnostic>();
            list.Add(error);
            writer.AddErrors(path, list);
            System.Console.WriteLine(error.Message);
        }

        /// <summary>
        /// Taken from TypeCobol CLI
        /// </summary>
        private TypeCobol.Compiler.CodeModel.SymbolTable loadCopies(List<string> copies)
        {
            var parser = new BuildParser(this, "TypeCobol.Server.loading");
            var table = new SymbolTable(null, SymbolTable.Scope.Intrinsic);
            foreach (string path in copies)
            {
                parser.Init(path);
                parser.Parse(path);

                if (parser.Results.ProgramClassDocumentSnapshot == null) continue;
                if (parser.Results.ProgramClassDocumentSnapshot.Program == null)
                {
                    Console.WriteLine("Error: Your Intrisic types are not included into a program.");
                    continue;
                }

                foreach (var type in parser.Results.ProgramClassDocumentSnapshot.Program.SymbolTable.CustomTypes)
                    table.RegisterCustomType(type);//TODO check if already there
            }
            return table;
        }

        /// <summary>
        /// Resolvfe a Copy
        /// </summary>
        /// <param name="copyName">The Copy Name</param>
        /// <param name="outputDir">The outputDir to save the copy</param>
        /// <returns>True if the copy is resolved, false otherwise.</returns>
        public bool ResolveCopy(String copyName, String outputDir, String encoding)
        {
            //TODO : JCM Handle the encoding
            if (TcbInterop != null)
            {
                return TcbInterop.LoadExternalFile(copyName, outputDir);
            }
            return false;
        }


        private string PROGNAME = System.AppDomain.CurrentDomain.FriendlyName;
        private string PROGVERSION = GetVersion();

        /// <summary>
        /// Taken from TypeCobol CLI
        /// </summary>
        private static string GetVersion()
        {
            System.Reflection.Assembly assembly = System.Reflection.Assembly.GetExecutingAssembly();
            var info = System.Diagnostics.FileVersionInfo.GetVersionInfo(assembly.Location);
            return info.FileVersion;
        }

        /// <summary>
        /// Taken from TypeCobol CLI
        /// </summary>
        int exit(int code, string message)
        {
            string errmsg = PROGNAME + ": " + message + "\n";
            errmsg += "Try " + PROGNAME + " --help for usage information.";
            System.Console.WriteLine(errmsg);
            return code;
        }
    }
}
