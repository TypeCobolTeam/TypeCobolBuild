#ifndef __JNI_OBJECT_H
#define __JNI_OBJECT_H

#include <assert.h>
#include "jni.h"
#include "Jni_Access.h"

/**
	\brief Wrapper class to a Jni Object.
*/
template<class T>
class Jni_Object : public Jni_Access
{
protected:
	JNIEnv *m_env;
	jobject m_jobj;
	jfieldID m_fobj;
	T * m_pObject;

public:
	/**
		\brief Constructor
		\param env JNI env
		\param jobj the Java/Jni object to wrap
		\param szObject the name of the field in the Java Class which hold the pointer to the Native C++ pointer.
	*/
	Jni_Object(JNIEnv *env, jobject jobj, char *szObject = "pObject")
	{
		assert(env != NULL && jobj != NULL);
		if (env != NULL && jobj != NULL)
		{
			m_jobj = jobj;
			m_env = env;
			jclass classe = env->GetObjectClass(jobj);
			m_fobj = env->GetFieldID(classe,szObject,"J");
			assert(m_fobj != NULL);
			if (m_fobj != NULL)
			{
				m_pObject = (T *)env->GetLongField(jobj, m_fobj);
			}
			else
			{
				m_pObject = NULL;
			}
		}
		else
		{
			m_env = NULL;
			m_jobj = NULL;
			m_fobj = NULL;
			m_pObject = NULL;
		}
	}
	/**
		\brief coercion (cast) operator to the natice C++ object.
	*/
	operator T *()
	{
		return m_pObject;
	}
	/**
		\brief assign operator with the natice C++ object.
	*/
	T * operator =(T *pObject)
	{
		if (m_jobj != NULL && m_fobj != NULL && m_env != NULL)
		{
			m_env->SetLongField(m_jobj, m_fobj, (jlong)pObject);
			m_pObject = pObject;
			return pObject;
		}
		else
		{
			return NULL;
		}
	}
	/**
		\brief allocate the native object, and attach it to the Java/JNI object.
	*/
	void allocate()
	{
		if (m_jobj != NULL && m_fobj != NULL && m_env != NULL)
		{
			assert(m_pObject == NULL);
			if(m_pObject == NULL)
			{
				m_pObject = new T();
				m_env->SetLongField(m_jobj, m_fobj, (jlong)m_pObject);
			}
		}
	}

	/**
		\brief wrap the native object, and attach it to the Java/JNI object.
	*/
	void wrap(T *pObject)
	{
		if (m_jobj != NULL && m_fobj != NULL && m_env != NULL)
		{
			assert(m_pObject == NULL);
			if(m_pObject == NULL)
			{
				m_pObject = pObject;
				m_env->SetLongField(m_jobj, m_fobj, (jlong)m_pObject);
			}
		}
	}

	/**
		\brief finalize the object
	*/
	void finalize()
	{
		if (m_jobj != NULL && m_fobj != NULL && m_env != NULL)
		{
			if(m_pObject != 0)
			{/* Take the attached C++ object */
				T *p = (T *)m_pObject;
				/* delete it */
				delete p;
				/* Set the Java/Jni reference to the C++ object to NULL */
				m_env->SetLongField(m_jobj, m_fobj, 0);
				/* Set the pointer to the C++ object to zero */
				m_pObject = 0;
			}
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
	static jobject allocateObject(JNIEnv *env, char *clazz, T *pNative, bool bCopy)
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

			Jni_Object<T> jni_obj(env, jt);
			T *pObject = jni_obj;
			assert(pObject != NULL);
			if (pObject != NULL)
			{	/* Copy the object */
				if (bCopy)
				{
					if (pNative != NULL)
					{
						*pObject = *pNative;
					}
				}
				else
				{
					jni_obj = pNative;
					/* Delete the previous object */
					delete pObject;
				}
			}
			else
			{
				env->DeleteLocalRef(jt);			
				return NULL;
			}
			return jt;
		}
		else
		{
			return NULL;
		}
	}
};

/*
	This macro is used to get the value of a member of a class using a Jni_Object.
	It throws an exception if there is no native C++ object.
	C is the class
	t is the type of the value
	v is tne name of the value.
*/
#define GET_JNI_OBJ_FIELD(C,t,v)\
	Jni_Object<C> jni_obj(env, jobj);\
	C *pC = jni_obj;\
	assert(pC != NULL);\
	if (pC != NULL)\
	{\
		return pC->v;\
	}\
	else\
		Jni_Access::ThrowNullPointerException(env, "No Native C++ object allocated");\
	return (t)0

/*
	This macro is used to set the value of a member of a class using a Jni_Object.
	It throws an exception if there is no native C++ object.
	C is the class
	t is the type of the value
	v is tne name of the value.
	return true if ok, false otherwise.
*/
#define SET_JNI_OBJ_FIELD(C,t,v)\
	Jni_Object<C> jni_obj(env, jobj);\
	C *pC = jni_obj;\
	assert(pC != NULL);\
	if (pC != NULL)\
	{\
		pC->v = v;\
		return true;\
	}\
	else Jni_Access::ThrowNullPointerException(env, "No Native C++ object allocated");\
	return false

#endif
