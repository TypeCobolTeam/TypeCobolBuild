#include "Stdafx.h"
#include "Jni_Access.h"

jchar Jni_Access::get_CharacterField(JNIEnv *env, jobject jobj, char *szField)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "C");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetCharField(jobj, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_CharacterField(JNIEnv *env, jobject jobj, char *szField, jchar v)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "C");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetCharField(jobj, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}
jboolean Jni_Access::get_BooleanField(JNIEnv *env, jobject jobj, char *szField)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "Z");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetBooleanField(jobj, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_BooleanField(JNIEnv *env, jobject jobj, char *szField, jboolean v)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "Z");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetBooleanField(jobj, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}
jbyte Jni_Access::get_ByteField(JNIEnv *env, jobject jobj, char *szField)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "B");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetByteField(jobj, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_ByteField(JNIEnv *env, jobject jobj, char *szField, jbyte v)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "Z");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetByteField(jobj, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}
jshort Jni_Access::get_ShortField(JNIEnv *env, jobject jobj, char *szField)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "S");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetShortField(jobj, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_ShortField(JNIEnv *env, jobject jobj, char *szField, jshort v)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "S");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetShortField(jobj, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}
jint Jni_Access::get_IntField(JNIEnv *env, jobject jobj, char *szField)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "I");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetIntField(jobj, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_IntField(JNIEnv *env, jobject jobj, char *szField, jint v)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "I");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetIntField(jobj, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}
jlong Jni_Access::get_LongField(JNIEnv *env, jobject jobj, char *szField)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "J");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetLongField(jobj, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_LongField(JNIEnv *env, jobject jobj, char *szField, jlong v)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "J");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetLongField(jobj, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}
jfloat Jni_Access::get_FloatField(JNIEnv *env, jobject jobj, char *szField)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "F");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetFloatField(jobj, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_FloatField(JNIEnv *env, jobject jobj, char *szField, jfloat v)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "F");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetFloatField(jobj, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}
jdouble Jni_Access::get_DoubleField(JNIEnv *env, jobject jobj, char *szField)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "D");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetDoubleField(jobj, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_DoubleField(JNIEnv *env, jobject jobj, char *szField, jdouble v)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, "F");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetDoubleField(jobj, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}


jobject Jni_Access::get_ObjectField(JNIEnv *env, jobject jobj, char *type, char *szField)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, type);
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetObjectField(jobj, fobj);
	}
	else
	{
		return 0;
	}
}

bool Jni_Access::set_ObjectField(JNIEnv *env, jobject jobj, char *type, char *szField, jobject v)
{
	jclass classe = env->GetObjectClass(jobj);
	jfieldID fobj = env->GetFieldID(classe, szField, type);
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetObjectField(jobj, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}

jchar Jni_Access::get_StaticCharacterField(JNIEnv *env, jclass jclazz, char *szField)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "C");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetStaticCharField(jclazz, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_StaticCharacterField(JNIEnv *env, jclass jclazz, char *szField, jchar v)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "C");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetStaticCharField(jclazz, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}
jboolean Jni_Access::get_StaticBooleanField(JNIEnv *env, jclass jclazz, char *szField)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "Z");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetStaticBooleanField(jclazz, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_StaticBooleanField(JNIEnv *env, jclass jclazz, char *szField, jboolean v)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "Z");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetStaticBooleanField(jclazz, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}
jbyte Jni_Access::get_StaticByteField(JNIEnv *env, jclass jclazz, char *szField)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "B");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetStaticByteField(jclazz, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_StaticByteField(JNIEnv *env, jclass jclazz, char *szField, jbyte v)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "Z");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetStaticByteField(jclazz, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}
jshort Jni_Access::get_StaticShortField(JNIEnv *env, jclass jclazz, char *szField)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "S");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetStaticShortField(jclazz, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_StaticShortField(JNIEnv *env, jclass jclazz, char *szField, jshort v)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "S");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetStaticShortField(jclazz, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}
jint Jni_Access::get_StaticIntField(JNIEnv *env, jclass jclazz, char *szField)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "I");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetStaticIntField(jclazz, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_StaticIntField(JNIEnv *env, jclass jclazz, char *szField, jint v)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "I");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetStaticIntField(jclazz, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}
jlong Jni_Access::get_StaticLongField(JNIEnv *env, jclass jclazz, char *szField)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "J");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetStaticLongField(jclazz, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_StaticLongField(JNIEnv *env, jclass jclazz, char *szField, jlong v)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "J");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetStaticLongField(jclazz, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}
jfloat Jni_Access::get_StaticFloatField(JNIEnv *env, jclass jclazz, char *szField)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "F");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetStaticFloatField(jclazz, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_StaticFloatField(JNIEnv *env, jclass jclazz, char *szField, jfloat v)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "F");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetStaticFloatField(jclazz, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}
jdouble Jni_Access::get_StaticDoubleField(JNIEnv *env, jclass jclazz, char *szField)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "D");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetStaticDoubleField(jclazz, fobj);
	}
	else
	{
		return 0;
	}
}
bool Jni_Access::set_StaticDoubleField(JNIEnv *env, jclass jclazz, char *szField, jdouble v)
{

	jfieldID fobj = env->GetFieldID(jclazz, szField, "F");
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetStaticDoubleField(jclazz, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}


jobject Jni_Access::get_StaticObjectField(JNIEnv *env, jclass jclazz, char *type, char *szField)
{
	
	jfieldID fobj = env->GetStaticFieldID(jclazz, szField, type);
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		return env->GetStaticObjectField(jclazz, fobj);
	}
	else
	{
		return 0;
	}
}

bool Jni_Access::set_StaticObjectField(JNIEnv *env, jclass jclazz, char *type, char *szField, jobject v)
{

	jfieldID fobj = env->GetStaticFieldID(jclazz, szField, type);
	assert(fobj != NULL);
	if (fobj != NULL)
	{
		env->SetStaticObjectField(jclazz, fobj, v);
		return true;
	}
	else
	{
		return false;
	}
}

/**
	\brief allocate a Java object and attach it to its corresponding native object.
	\param en is the Java/Jni environment in which to alocate
	\param the Java class name to allocate : ex "gekernel/RealGas"
	\param pNative pointer to the native C++ object to attach.
	\param bCopy is true if the pNative object must be copied in the target object or assigne
	\return the Java/Jni object allocated if success, NULL otherwise.
*/
jobject Jni_Access::allocateObject(JNIEnv *env, char *clazz)
{
	jclass clazzT = env->FindClass(clazz);
	assert(clazzT != NULL);
	if (clazzT != NULL)
	{	/* Now we must found the constructor method */
		jmethodID cid = env->GetMethodID(clazzT, "<init>", "()V");
		assert(cid != NULL);
		if (cid == NULL) {
			env->DeleteLocalRef(clazzT);
			return NULL;
		}
		/* Construct the Java RealGas Object */
		jobject jt = env->NewObject(clazzT, cid);
		assert(jt != NULL);
		if (jt == NULL) {
			env->DeleteLocalRef(clazzT);
			return NULL;
		}
		env->DeleteLocalRef(clazzT);
		return jt;
	}
	else
	{
		return NULL;
	}
}

/**
	\brief Check if an object is an instanceof a given class.
	\param en is the Java/Jni environment in which to alocate
	\param jobj the object to check
	\param clazz is the class name that the object must be an instance.
	\return true if the objetc is instanceof the class, false otherwise.
*/
jboolean Jni_Access::isInstanceOf(JNIEnv *env, jobject jobj, char *clazz)
{
	jclass clazzOf = env->FindClass(clazz);
	if (clazzOf != NULL)
	{	/* we have a real gas object instantiate a real gaz object */
		jboolean isA = env->IsInstanceOf(jobj, clazzOf);		
		env->DeleteLocalRef(clazzOf);
		return isA;
	}
	return false;
}

/**
	\brief throws a java exception.
	\param name is the java exception class name.
	\param msg is the message associated to the Java exception.
*/
void Jni_Access::JNU_ThrowByName(JNIEnv *env, const char *name, const char *msg)
{
	jclass cls = env->FindClass(name);
	/* if cls is NULL, an exception has already been thrown */
	if (cls != NULL) {			
		env->ThrowNew(cls, msg);
	}
	/* free the local ref */
	env->DeleteLocalRef(cls);
};

/**
	\brief throws a java NullPointerException exception.
	\param name is the java exception class name.
	\param msg is the message associated to the Java exception.
*/
void Jni_Access::ThrowNullPointerException(JNIEnv *env, const char *msg)
{
	JNU_ThrowByName(env, "java/lang/NullPointerException", msg);
}

/**
	\brief throws a java OutOfMemoryError exception.
	\param name is the java exception class name.
	\param msg is the message associated to the Java exception.
*/
void Jni_Access::ThrowOutOfMemoryError(JNIEnv *env, const char *msg)
{
	JNU_ThrowByName(env, "java/lang/OutOfMemoryError", msg);
}

/**
	\brief throws a java IllegalArgumentException exception.
	\param name is the java exception class name.
	\param msg is the message associated to the Java exception.
*/
void Jni_Access::ThrowIllegalArgumentException(JNIEnv *env, const char *msg)
{
	JNU_ThrowByName(env, "java/lang/IllegalArgumentException", msg);
}

/**
	\brief throws a java ClassNotFoundException exception.
	\param name is the java exception class name.
	\param msg is the message associated to the Java exception.
*/
void Jni_Access::ThrowClassNotFoundException(JNIEnv *env, const char *msg)
{
	JNU_ThrowByName(env, "java/lang/ClassNotFoundException", msg);
}

/**
	\brief throws a java ClassCastException exception.
	\param name is the java exception class name.
	\param msg is the message associated to the Java exception.
*/
void Jni_Access::ThrowClassCastException(JNIEnv *env, const char *msg)
{
	JNU_ThrowByName(env, "java/lang/ClassCastException", msg);
}

/**
	\brief throws a java IOException exception.
	\param name is the java exception class name.
	\param msg is the message associated to the Java exception.
*/
void Jni_Access::ThrowIOException(JNIEnv *env, const char *msg)
{
	JNU_ThrowByName(env, "java/io/IOException", msg);
}
