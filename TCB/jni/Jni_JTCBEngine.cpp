#include "Stdafx.h"
#include "JTCBEngine.h"
#include "Jni_Object.h"
#include "..\EventLogManager.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jboolean JNICALL Java_jtcb_JTCBEngine_compileFile
  (JNIEnv *env, jobject jtcb, jstring jInputFile, jstring jOutputFile)
{
	Jni_Object<JTCBEngine> jni_obj(env, jtcb);
	if (jInputFile == NULL)
		jni_obj.ThrowNullPointerException(env, "Input file parameter");

	if (jOutputFile == NULL)
		jni_obj.ThrowNullPointerException(env, "Output file parameter");

	JTCBEngine *pjTCB = jni_obj;
	assert(pjTCB != NULL);
	if (pjTCB != NULL)
	{
		const jchar *cInputFile = (jInputFile == 0) ? 0 : env->GetStringChars(jInputFile, 0);
		if (cInputFile == NULL)
			jni_obj.ThrowOutOfMemoryError(env);

		const jchar *cOutputFile = (jOutputFile == 0) ? 0 : env->GetStringChars(jOutputFile, 0);
		if (cOutputFile == NULL)
		{
			env->ReleaseStringChars(jInputFile, cInputFile);
			jni_obj.ThrowOutOfMemoryError(env);
		}

		pjTCB->compileFile(env, jtcb, (wchar_t *)cInputFile, (wchar_t *)cOutputFile);		

		env->ReleaseStringChars(jInputFile, cInputFile);
		env->ReleaseStringChars(jOutputFile, cOutputFile);
		return true;
	}
	else return false;	
}

/*
 * Class:     jtcb_JTCBEngine
 * Method:    finalize
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jtcb_JTCBEngine_finalize
  (JNIEnv *env, jobject jobj)
{
	Jni_Object<JTCBEngine> jni_obj(env, jobj);	
	//Simply delete it here
	delete (JTCBEngine *)jni_obj;
}

/*
 * Class:     jtcb_JTCBEngine
 * Method:    allocateNativeObject
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jtcb_JTCBEngine_allocateNativeObject
  (JNIEnv *env, jobject jobj)
{
	// Create an auto wrapper instance.
	JTCBEngine *pJTCBEngine = new JTCBEngine(env, jobj);
	return;
}

/*
 * Class:     jtcb_JTCBEngine
 * Method:    log_clear
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jtcb_JTCBEngine_log_1clear
  (JNIEnv *env, jclass jclazz)
{
	EventLogManager::Clear();
}

/*
 * Class:     jtcb_JTCBEngine
 * Method:    log_info
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jtcb_JTCBEngine_log_1info
  (JNIEnv *env, jclass jclazz, jstring jmsg)
{
	if (jmsg == NULL)
		return;		

	const jchar *cMsg = (jmsg == 0) ? 0 : env->GetStringChars(jmsg, 0);
	if (cMsg == NULL) {
		EventLogManager::WriteEntry("JNI:OutOfMemoryException", System::Diagnostics::EventLogEntryType::Error, EventLogManager::Category::JNI);
		return;
	}	

	System::String ^sMsg = gcnew System::String((wchar_t *)cMsg);
	EventLogManager::WriteEntry(sMsg, System::Diagnostics::EventLogEntryType::Information, EventLogManager::Category::JTCB);

	env->ReleaseStringChars(jmsg, cMsg);
}

/*
 * Class:     jtcb_JTCBEngine
 * Method:    log_warning
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jtcb_JTCBEngine_log_1warning
  (JNIEnv *env, jclass jclazz, jstring jmsg)
{
	if (jmsg == NULL)
		return;		

	const jchar *cMsg = (jmsg == 0) ? 0 : env->GetStringChars(jmsg, 0);
	if (cMsg == NULL) {
		EventLogManager::WriteEntry("JNI:OutOfMemoryException", System::Diagnostics::EventLogEntryType::Error, EventLogManager::Category::JNI);
		return;
	}	

	System::String ^sMsg = gcnew System::String((wchar_t *)cMsg);
	EventLogManager::WriteEntry(sMsg, System::Diagnostics::EventLogEntryType::Warning, EventLogManager::Category::JTCB);

	env->ReleaseStringChars(jmsg, cMsg);
}

/*
 * Class:     jtcb_JTCBEngine
 * Method:    log_error
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jtcb_JTCBEngine_log_1error
  (JNIEnv *env, jclass jclazz, jstring jmsg)
{
	if (jmsg == NULL)
		return;		

	const jchar *cMsg = (jmsg == 0) ? 0 : env->GetStringChars(jmsg, 0);
	if (cMsg == NULL) {
		EventLogManager::WriteEntry("JNI:OutOfMemoryException", System::Diagnostics::EventLogEntryType::Error, EventLogManager::Category::JNI);
		return;
	}	

	System::String ^sMsg = gcnew System::String((wchar_t *)cMsg);
	EventLogManager::WriteEntry(sMsg, System::Diagnostics::EventLogEntryType::Error, EventLogManager::Category::JTCB);

	env->ReleaseStringChars(jmsg, cMsg);
}


#ifdef __cplusplus
}
#endif
