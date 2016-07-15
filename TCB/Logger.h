#ifdef TCB_EXE_VERSION //ONLY an executable version can load the log4net DLL.

using namespace log4net;
using namespace log4net::Appender;
using namespace log4net::Layout;
using namespace log4net::Core;
using namespace log4net::Repository::Hierarchy;

namespace tcb {
	/**
		\brief Logging configuration class.
	*/

	ref class Logger
	{
	public:
		static Logger()
		{
            Hierarchy ^hierarchy = (Hierarchy ^)LogManager::GetRepository();

            PatternLayout ^patternLayout = gcnew PatternLayout();
            patternLayout->ConversionPattern = "%date [%thread] %-5level %logger - %message%newline";
            patternLayout->ActivateOptions();

            RollingFileAppender ^roller = gcnew RollingFileAppender();
            roller->AppendToFile = false;
            roller->File = "TCB.log";
            roller->Layout = patternLayout;
            roller->MaxSizeRollBackups = 5;
            roller->MaximumFileSize = "1GB";
            roller->RollingStyle = RollingFileAppender::RollingMode::Size;
            roller->StaticLogFileName = true;            
            roller->ActivateOptions();
            hierarchy->Root->AddAppender(roller);

            MemoryAppender ^memory = gcnew MemoryAppender();
            memory->ActivateOptions();
            hierarchy->Root->AddAppender(memory);

            hierarchy->Root->Level = Level::Info;
            hierarchy->Configured = true;
		}
	public:
		/**
			\brief Get the logger instance.
		*/
		static ILog ^GetLogger()
		{
			return _log;
		}
	private:
		static ILog ^_log =  LogManager::GetLogger(tcb::Logger::typeid);

	};
}

#endif
