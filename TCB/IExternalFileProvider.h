#ifndef __tcb_IExternalFileProvider_h
#define __tcb_IExternalFileProvider_h

using namespace System;

namespace tcb
{
    /// <summary>
    /// An Interface for provinding external file access.
    /// </summary>
    public interface class IExternalFileProvider
    {
	public:
        /// <summary>
        /// Load an external file
        /// </summary>
        /// <param name="externalFilePath">The path of the external file</param>
        /// <param name="outputDirectory">The ouput directory where to load the external file</param>
        /// <returns>trur if thr have have be resolved and loaded, false if the file cannot be located</returns>
        /// <exception cref="Exception">Any exception</exception>
        bool LoadExternalFile(System::String ^externalFilePath, System::String ^outputDirectory);
        /// <summary>
        /// Load an external file content
        /// </summary>
        /// <param name="externalFilePath">The path of the external file</param>
        /// <returns>The contect of the file if found, null otherwise.</returns>
        /// <exception cref="Exception">Any exception</exception>
        System::String ^LoadExternalFileContent(String ^externalFilePath);
    };
}

#endif
