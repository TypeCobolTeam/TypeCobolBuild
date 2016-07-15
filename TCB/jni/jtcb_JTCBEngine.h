/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class jtcb_JTCBEngine */

#ifndef _Included_jtcb_JTCBEngine
#define _Included_jtcb_JTCBEngine
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     jtcb_JTCBEngine
 * Method:    compileFile
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_jtcb_JTCBEngine_compileFile
  (JNIEnv *, jobject, jstring, jstring);

/*
 * Class:     jtcb_JTCBEngine
 * Method:    log_clear
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jtcb_JTCBEngine_log_1clear
  (JNIEnv *, jclass);

/*
 * Class:     jtcb_JTCBEngine
 * Method:    log_info
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jtcb_JTCBEngine_log_1info
  (JNIEnv *, jclass, jstring);

/*
 * Class:     jtcb_JTCBEngine
 * Method:    log_warning
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jtcb_JTCBEngine_log_1warning
  (JNIEnv *, jclass, jstring);

/*
 * Class:     jtcb_JTCBEngine
 * Method:    log_error
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jtcb_JTCBEngine_log_1error
  (JNIEnv *, jclass, jstring);

/*
 * Class:     jtcb_JTCBEngine
 * Method:    finalize
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jtcb_JTCBEngine_finalize
  (JNIEnv *, jobject);

/*
 * Class:     jtcb_JTCBEngine
 * Method:    allocateNativeObject
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jtcb_JTCBEngine_allocateNativeObject
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
