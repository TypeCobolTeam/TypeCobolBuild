using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TypeCobol.Compiler.Directives;
using TypeCobol.Compiler.File;
using TypeCobol.Compiler.Preprocessor;
using TypeCobol.Compiler.Text;

namespace TypeCobolBuilder.Engine
{
    /// <summary>
    /// This the specialization of the compilation project used by the Builder.
    /// Create a new Cobol compilation project in a local directory.
    /// </summary>
    public class BuildProject : TypeCobol.Compiler.CompilationProject
    {
        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="buildEngine"></param>
        /// <param name="projectName"></param>
        /// <param name="rootDirectory"></param>
        /// <param name="fileExtensions"></param>
        /// <param name="encoding"></param>
        /// <param name="endOfLineDelimiter"></param>
        /// <param name="fixedLineLength"></param>
        /// <param name="columnsLayout"></param>
        /// <param name="compilationOptions"></param>
        public BuildProject(BuildEngine buildEngine, string projectName, string rootDirectory, string[] fileExtensions, Encoding encoding, EndOfLineDelimiter endOfLineDelimiter, int fixedLineLength, ColumnsLayout columnsLayout, TypeCobolOptions compilationOptions)
            : base(projectName, rootDirectory, fileExtensions, encoding, endOfLineDelimiter, fixedLineLength, columnsLayout, compilationOptions)
        {
            BuilderEngine = buildEngine;
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
        /// Get a Preprocessed document
        /// </summary>
        /// <param name="libraryName"></param>
        /// <param name="textName"></param>
        /// <returns></returns>
        public override ProcessedTokensDocument GetProcessedTokensDocument(string libraryName, string textName)
        {
            try
            {
                return base.GetProcessedTokensDocument(libraryName, textName);
            }
            catch (Exception e)
            {
                // Text name refenced by COPY directive was not found

                //TODO : JCM Handle the encoding ==> the null paramater
                // => Try an external Stream resolution  
                if (BuilderEngine.ResolveCopy(textName, RootDirectory, null))
                {                    
                    //Redo the base processing
                    return base.GetProcessedTokensDocument(libraryName, textName);
                }
                else
                {
                    // => register a preprocessor error on this line                            
                    throw e;                
                }
            }
        }
    }
}
