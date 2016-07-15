#ifndef __JNI_ACCESS_H
#define __JNI_ACCESS_H

#include <assert.h>
#include "jni.h"

/**
	Helper static method to access JNI fields.
*/
class Jni_Access
{
public:
	static jchar get_CharacterField(JNIEnv *env, jobject jobj, char *szField);
	static bool set_CharacterField(JNIEnv *env, jobject jobj, char *szField, jchar v);
	static jboolean get_BooleanField(JNIEnv *env, jobject jobj, char *szField);
	static bool set_BooleanField(JNIEnv *env, jobject jobj, char *szField, jboolean v);
	static jbyte get_ByteField(JNIEnv *env, jobject jobj, char *szField);
	static bool set_ByteField(JNIEnv *env, jobject jobj, char *szField, jbyte v);
	static jshort get_ShortField(JNIEnv *env, jobject jobj, char *szField);
	static bool set_ShortField(JNIEnv *env, jobject jobj, char *szField, jshort v);
	static jint get_IntField(JNIEnv *env, jobject jobj, char *szField);
	static bool set_IntField(JNIEnv *env, jobject jobj, char *szField, jint v);
	static jlong get_LongField(JNIEnv *env, jobject jobj, char *szField);
	static bool set_LongField(JNIEnv *env, jobject jobj, char *szField, jlong v);
	static jfloat get_FloatField(JNIEnv *env, jobject jobj, char *szField);
	static bool set_FloatField(JNIEnv *env, jobject jobj, char *szField, jfloat v);
	static jdouble get_DoubleField(JNIEnv *env, jobject jobj, char *szField);
	static bool set_DoubleField(JNIEnv *env, jobject jobj, char *szField, jdouble v);
	static jobject get_ObjectField(JNIEnv *env, jobject jobj, char *type, char *szField);
	static bool set_ObjectField(JNIEnv *env, jobject jobj, char *type, char *szField, jobject v);


	static jchar get_StaticCharacterField(JNIEnv *env, jclass jclazz, char *szField);
	static bool set_StaticCharacterField(JNIEnv *env, jclass jclazz, char *szField, jchar v);
	static jboolean get_StaticBooleanField(JNIEnv *env, jclass jclazz, char *szField);
	static bool set_StaticBooleanField(JNIEnv *env, jclass jclazz, char *szField, jboolean v);
	static jbyte get_StaticByteField(JNIEnv *env, jclass jclazz, char *szField);
	static bool set_StaticByteField(JNIEnv *env, jclass jclazz, char *szField, jbyte v);
	static jshort get_StaticShortField(JNIEnv *env, jclass jclazz, char *szField);
	static bool set_StaticShortField(JNIEnv *env, jclass jclazz, char *szField, jshort v);
	static jint get_StaticIntField(JNIEnv *env, jclass jclazz, char *szField);
	static bool set_StaticIntField(JNIEnv *env, jclass jclazz, char *szField, jint v);
	static jlong get_StaticLongField(JNIEnv *env, jclass jclazz, char *szField);
	static bool set_StaticLongField(JNIEnv *env, jclass jclazz, char *szField, jlong v);
	static jfloat get_StaticFloatField(JNIEnv *env, jclass jclazz, char *szField);
	static bool set_StaticFloatField(JNIEnv *env, jclass jclazz, char *szField, jfloat v);
	static jdouble get_StaticDoubleField(JNIEnv *env, jclass jclazz, char *szField);
	static bool set_StaticDoubleField(JNIEnv *env, jclass jclazz, char *szField, jdouble v);
	static jobject get_StaticObjectField(JNIEnv *env, jclass jclazz, char *type, char *szField);
	static bool set_StaticObjectField(JNIEnv *env, jclass jclazz, char *type, char *szField, jobject v);

	static jobject allocateObject(JNIEnv *env, char *clazz);

	static jboolean isInstanceOf(JNIEnv *env, jobject jobj, char *clazz);
	static void JNU_ThrowByName(JNIEnv *env, const char *name, const char *msg);
	static void ThrowNullPointerException(JNIEnv *env, const char *msg="");
	static void ThrowOutOfMemoryError(JNIEnv *env, const char *msg="");
	static void ThrowIllegalArgumentException(JNIEnv *env, const char *msg="");
	static void ThrowClassNotFoundException(JNIEnv *env, const char *msg="");
	static void ThrowClassCastException(JNIEnv *env, const char *msg="");
	static void ThrowIOException(JNIEnv *env, const char *msg="");
};


#endif //__JNI_ACCESS_H

