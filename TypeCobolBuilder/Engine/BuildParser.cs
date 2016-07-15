using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TypeCobol.Compiler;
using TypeCobol.Compiler.Directives;

namespace TypeCobolBuilder.Engine
{
    /// <summary>
    /// This the Parser use by the Builder.
    /// </summary>
    public class BuildParser : TypeCobol.Server.Parser
    {        
        /// <summary>
        /// All projects being parsed.
        /// </summary>
        private Dictionary<string, BuildProject> m_Projects;
        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="name">The Build Engine'</param>
        /// <param name="name">This Build Parser name</param>
        public BuildParser(BuildEngine buildEngine, string name)
            : base(name)
        {
            BuilderEngine = buildEngine;
            m_Projects = new Dictionary<string, BuildProject>();
        }

        BuildEngine m_BuildEngine;
        /// <summary>
        /// The Build Engine
        /// </summary>
        public BuildEngine BuilderEngine
        {
            get
            {
                return m_BuildEngine;
            }
            private set
            {
                m_BuildEngine = value;
            }
        }

        /// <summary>
        /// New implementation of the Init function, to allocate a BuildProject instance rather than a 
        /// CompilationProject.
        /// </summary>
        /// <param name="path">The path of the file to parser</param>
        /// <param name="format">The resulting document format</param>
        /// <returns>The BuildProject instance created</returns>
        public new BuildProject Init(string path, DocumentFormat format = null)
        {
            FileCompiler compiler;
            if (Compilers.TryGetValue(path, out compiler))
                return m_Projects[path];
            string directory = Directory.GetParent(path).FullName;
            string filename = Path.GetFileName(path);
            DirectoryInfo root = new DirectoryInfo(directory);
            if (format == null) format = GetFormat(path);
            TypeCobolOptions options = new TypeCobolOptions();
            BuildProject project = new BuildProject(BuilderEngine, path, root.FullName, new string[] { "*.cbl", "*.cpy" },
                format.Encoding, format.EndOfLineDelimiter, format.FixedLineLength, format.ColumnsLayout, options);
            m_Projects[path] = project;
            compiler = new FileCompiler(null, filename, project.SourceFileProvider, project, format.ColumnsLayout, options, CustomSymbols, false);
            Compilers.Add(path, compiler);
            Inits.Add(path, false);
            return project;
        }
    }
}
