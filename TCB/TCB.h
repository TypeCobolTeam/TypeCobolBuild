// TCB.h

#pragma once

using namespace System;
#include "IExternalFileProvider.h"

//Forward declarion
class JTCBEngine;

namespace tcb {
	/**
		The Main C++/CLI Type Cobol builder class..
	*/
	public ref class TCB : public IExternalFileProvider
	{
	private:
		/**
			\brief The JTCBEngine instance.
		*/
		JTCBEngine *m_jtcbEngibne;
		/**
			\brief The target Builder engine used by reflection.
		*/
		System::Object ^m_builderEngine;
		/**
			\brief the Build Engine Main compilation commande function.
		*/
		Func< array<System::String ^> ^, int> ^ m_MainCompileCmd;

		/**
			\brief Logging error function
		*/
		Func< System::String ^, System::Object ^> ^ m_LogError;

		/**
			\brief Logging info function
		*/
		Func< System::String ^, System::Object ^> ^ m_LogInfo;

		/**
			\brief Logging warning function
		*/
		Func< System::String ^, System::Object ^> ^ m_LogWarning;
	public:
		/**
			\brief empty TCB constructor.
		*/
		TCB(JTCBEngine *jtcbEngibne);

		/**
			\brief the target BuildEngine
		*/
		property System::Object ^BuilderEngine
		{
			System::Object ^get()
			{
				return m_builderEngine;
			}
		}

		/**
			\brief the Build Engine Main compilation command function, this function must be set.
		*/
		property Func< array<System::String ^> ^, int> ^ MainCompileCmd
		{
			Func< array<System::String ^> ^, int> ^ get()
			{
				return m_MainCompileCmd;
			}
			void set(Func< array<System::String ^> ^, int> ^ value)
			{
				m_MainCompileCmd = value;
			}
		}

		/**
			\brief Logg Error function
		*/
		property Func< System::String ^, System::Object ^> ^ LogError
		{
			Func< System::String ^, System::Object ^> ^ get()
			{
				return m_LogError;
			}
			void set(Func< System::String ^, System::Object ^> ^ value)
			{
				m_LogError = value;
			}
		}

		/**
			\brief Logg Info function
		*/
		property Func< System::String ^, System::Object ^> ^ LogInfo
		{
			Func< System::String ^, System::Object ^> ^ get()
			{
				return m_LogInfo;
			}
			void set(Func< System::String ^, System::Object ^> ^ value)
			{
				m_LogInfo = value;
			}
		}

		/**
			\brief Logg Warning function
		*/
		property Func< System::String ^, System::Object ^> ^ LogWarning
		{
			Func< System::String ^, System::Object ^> ^ get()
			{
				return m_LogWarning;
			}
			void set(Func< System::String ^, System::Object ^> ^ value)
			{
				m_LogWarning = value;
			}
		}

		/**
			\brief Get the JAVA/JNI Interoperability object.
		*/
		property JTCBEngine * JTCB_Engine
		{
			JTCBEngine * get()
			{
				return m_jtcbEngibne;
			}
		}

		/**
			\brief Compile a file
			\param inputFile input file path to compile
			\param outputFile the output file to generate.
			\return true if OK, false otherwise.
		*/
		bool CompileFile(System::String ^ inputFile, System::String ^outputFile);

		/**
			\brief Implementation of the interface IExternalFileProvider.
		*/
		virtual bool LoadExternalFile(System::String^ externalFilePath, System::String^ outputDirectory);
		/**
			\brief Implementation of the interface IExternalFileProvider.
		*/
		virtual System::String^ LoadExternalFileContent(System::String^ externalFilePath);

	private:
		/**
			\brief Init the interoperability with TypeCobolBuilder.dll by reflection
		*/
		void initReflectionIntroperability();
	};
}
