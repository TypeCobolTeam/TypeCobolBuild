// This is the main DLL file.

#include "stdafx.h"

#include "TCB.h"
#include "jni\JTCBEngine.h"

#ifdef TCB_EXE_VERSION
#include "Logger.h"
#endif

using namespace tcb;

#ifdef TCB_EXE_VERSION
/**
	This if the application is an executable.
*/
int main(array<System::String ^> ^args)
{
	TCB ^tcv = gcnew TCB(nullptr);
    Console::WriteLine(L"Hello World");	
	tcb::Logger::GetLogger()->Info("TCB ias an executable");
    return 0;
}
#endif

TCB::TCB(JTCBEngine *jtcbEngibne)
{
	m_jtcbEngibne = jtcbEngibne;
	initReflectionIntroperability();
}

void TCB::initReflectionIntroperability()
{
	//Locate the TypeCobolBuilder Dll
	System::Reflection::Assembly ^ c = System::Reflection::Assembly::GetExecutingAssembly();
	System::String ^location = c->Location;
	System::IO::FileInfo ^fi = gcnew System::IO::FileInfo(location);
	System::IO::DirectoryInfo ^ dir = fi->Directory;
	System::String ^dll_path = nullptr;
	
	dll_path = System::IO::Path::Combine(dir->FullName, "TypeCobolBuilder.dll");
	System::Reflection::Assembly ^tcb_assembly = System::Reflection::Assembly::LoadFrom(dll_path);
	if (tcb_assembly == nullptr)
	{//TODO : JCM Log that we fail to load TypeCobolBuilder assembler
		LogError("Fail to load TypeCobolBuilder assembler");
		return;
	}

	//Get BuildEngine type
	System::Type ^tcb_be = tcb_assembly->GetType("TypeCobolBuilder.Engine.BuildEngine");
	if(tcb_be == nullptr)
	{//TODO : JCM Log that we fail to get the type of the TypeCobolBuilder.Engine.BuildEngine class
		LogError("Fail to get the type of the TypeCobolBuilder.Engine.BuildEngine class");
		return;
	}	

	//Create a BuildEngine instance
	System::Type ^tcb_typeid = TCB::typeid;
	System::Reflection::ConstructorInfo ^ctor = tcb_be->GetConstructor(gcnew array<System::Type ^>{tcb_typeid});
	if (ctor == nullptr)
	{//TODO : JCM Log that we fail to Get BuildEngine constructor
		LogError("Fail to get BuildEngine constructor");
	}
	m_builderEngine = ctor->Invoke(gcnew array<System::Object ^>{this});

	if (MainCompileCmd == nullptr)
	{//TODO : JCM Log that we fail to Get BuildEngine constructor
		LogError("Fail to get BuildEngine constructor");
	}
}

bool TCB::CompileFile(System::String ^ inputFile, System::String ^outputFile)
{
	if (MainCompileCmd != nullptr)
	{
		//Create arguments
		array<System::String ^> ^args = {"-1", "-i", inputFile, "-g", "-o", outputFile, };
		LogInfo(String::Format("Compiling : {0} {1} {2} {3} {4} {5}", args));
		int result = MainCompileCmd(args);
		return result == 0;
	}
	else
	{//TODO : JCM Log that the compilation function was not set.
		return false;
	}
}

/**
	\brief Implementation of the interface IExternalFileProvider.
*/
bool TCB::LoadExternalFile(System::String^ externalFilePath, System::String^ outputDirectory)
{
	pin_ptr<const wchar_t> wExternalFilePath = PtrToStringChars( externalFilePath );	
	pin_ptr<const wchar_t> wOutputDirectory = PtrToStringChars( outputDirectory );

	return JTCB_Engine->resolveCopy(wExternalFilePath, wOutputDirectory);
}

/**
	\brief Implementation of the interface IExternalFileProvider.
*/
System::String^ TCB::LoadExternalFileContent(System::String^ externalFilePath)
{
	return nullptr;
}


#if 0 //SAMPLE CODE
#include <jni.h>       /* where everything is defined */
    ...
    JavaVM *jvm;       /* denotes a Java VM */
    JNIEnv *env;       /* pointer to native method interface */
    JavaVMInitArgs vm_args; /* JDK/JRE 6 VM initialization arguments */
    JavaVMOption* options = new JavaVMOption[1];
    options[0].optionString = "-Djava.class.path=/usr/lib/java";
    vm_args.version = JNI_VERSION_1_6;
    vm_args.nOptions = 1;
    vm_args.options = options;
    vm_args.ignoreUnrecognized = false;
    /* load and initialize a Java VM, return a JNI interface
     * pointer in env */
    JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);
    delete options;
    /* invoke the Main.test method using the JNI */
    jclass cls = env->FindClass("Main");
    jmethodID mid = env->GetStaticMethodID(cls, "test", "(I)V");
    env->CallStaticVoidMethod(cls, mid, 100);
    /* We are done. */
    jvm->DestroyJavaVM();
#endif

#if 0
	JavaVMInitArgs vm_args;
	JavaVMOption options[4];

	options[0].optionString = "-Djava.compiler=NONE";           /* disable JIT */
	options[1].optionString = "-Djava.class.path=c:\myclasses"; /* user classes */
	options[2].optionString = "-Djava.library.path=c:\mylibs";  /* set native library path */
	options[3].optionString = "-verbose:jni";                   /* print JNI-related messages */

	vm_args.version = JNI_VERSION_1_2;
	vm_args.options = options;
	vm_args.nOptions = 4;
	vm_args.ignoreUnrecognized = TRUE;

	/* Note that in the JDK/JRE, there is no longer any need to call
	 * JNI_GetDefaultJavaVMInitArgs.
	 */
	res = JNI_CreateJavaVM(&vm, (void **)&env, &vm_args);
	if (res < 0) ...
#endif
