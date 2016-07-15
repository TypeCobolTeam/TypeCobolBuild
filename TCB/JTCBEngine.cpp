#include "Stdafx.h"
#include "jni/JTCBEngine.h"
using namespace System;

JTCBEngine::JTCBEngine(JNIEnv *env, jobject jobj, char *szObject) : Jni_Object<JTCBEngine>(env, jobj, szObject)
{
	m_fResolveCopyID = NULL;
	//Auto wrapping  to ourself
	wrap(this);
	//Create the TCB Managed object	
}

bool JTCBEngine::compileFile(JNIEnv *env, jobject jtcb, const wchar_t *inputFile, const wchar_t *outputFile)
{	
	m_TCB = gcnew tcb::TCB(this);
	m_env = env;
	m_jobj = jtcb;
	wprintf(L"Compiling file %s > %s!\n", inputFile, outputFile);

	System::String ^sInputFile = gcnew System::String(inputFile);
	System::String ^sOutputFile = gcnew System::String(outputFile);

	bool bResult = m_TCB->CompileFile(sInputFile, sOutputFile);
	
	return bResult;	
}


bool JTCBEngine::resolveCopy(const wchar_t *copyName, const wchar_t *outputDir)
{	
	if (m_fResolveCopyID == NULL)
	{
		//Get resolveCopyMethod
		jclass clazz = m_env->GetObjectClass(m_jobj);
		m_fResolveCopyID = m_env->GetMethodID(clazz,"resolveCopy","(Ljava/lang/String;Ljava/lang/String;)Z");
		if (m_fResolveCopyID == NULL)
			return false;
	}
	jstring jcopyName = m_env->NewString((const jchar *)copyName, wcslen(copyName));
	if (jcopyName == NULL)
		ThrowOutOfMemoryError(m_env);

	jstring jOutputDir = m_env->NewString((const jchar *)outputDir, wcslen(outputDir));
	if (jOutputDir == NULL)
		ThrowOutOfMemoryError(m_env);

	jboolean  bResult = m_env->CallBooleanMethod(m_jobj, m_fResolveCopyID, jcopyName, jOutputDir);
	return bResult != 0;
}

