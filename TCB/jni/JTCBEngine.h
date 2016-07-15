#ifndef __JTCB_H
#define __JTCB_H
#include "jtcb_JTCBEngine.h"
#include "Jni_Object.h"
#include <string>
#include <vcclr.h>
#include "..\TCB.h"

/**
	This the C++ peer object that implements the Java Native Interface of the class JTCBEngine
*/
class JTCBEngine : Jni_Object<JTCBEngine>
{
public:
	/** 
		Empty Constructor.
	*/
	JTCBEngine() : Jni_Object<JTCBEngine>(NULL, NULL, NULL)
	{
		m_fResolveCopyID = NULL;
	}

	/**
		Wrapper constructor
	*/
	JTCBEngine(JNIEnv *env, jobject jobj, char *szObject = "pObject");

	/**
		\brief Set the Current Input File
	*/
	void setInputFile(wchar_t *inputFile)
	{
		m_inputFileName = inputFile;
	}
	/**
		\brief Set the current Output File
	*/
	void setOutputFile(wchar_t *outputFile)
	{
		m_outputFileName = outputFile;
	}
	/**
		\brief Compile the given input file.
		\param inputFile the Input file
		\param outputFile the Output Fime.
		\return true if OK, false otherwise.
	*/
	bool compileFile(JNIEnv *env, jobject jtcb, const wchar_t *inputFile, const wchar_t *outputFile);
	/**
		\brief Tries to resolve the given COPY Name
		\param copyName the COPY's name
		\param outputDir the output directory where to store the COPY file.
		\return true if OK, false otherwise.
	*/
	bool resolveCopy(const wchar_t *copyName, const wchar_t *outputDir);
private:
	// The Managed reference to the TCB instance.
	gcroot<tcb::TCB ^> m_TCB;

private:
	std::wstring m_inputFileName;
	std::wstring m_outputFileName;
	/**
		Java/JNI ID to the Resolve Copy method
	*/
	jmethodID m_fResolveCopyID;
};

#endif
