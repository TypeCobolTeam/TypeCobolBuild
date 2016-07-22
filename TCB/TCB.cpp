// This is the main DLL file.

#include "stdafx.h"

#include "TCB.h"
#include "jni\JTCBEngine.h"

using namespace tcb;

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

