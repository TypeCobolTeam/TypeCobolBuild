using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using log4net;
using log4net.Appender;
using log4net.Layout;
using log4net.Core;
using log4net.Repository.Hierarchy;


namespace TypeCobolBuilder.Engine
{
	public class Logger
	{
		static Logger()
		{
            String cur_dir = System.IO.Directory.GetCurrentDirectory();
            Hierarchy hierarchy = (Hierarchy )LogManager.GetRepository();

            PatternLayout patternLayout = new PatternLayout();
            patternLayout.ConversionPattern = "%date [%thread] %-5level %logger - %message%newline";
            patternLayout.ActivateOptions();

            RollingFileAppender roller = new RollingFileAppender();
            roller.AppendToFile = false;
            roller.File = System.IO.Path.Combine(cur_dir, "TypeCobolBuilder.log");
            roller.Layout = patternLayout;
            roller.MaxSizeRollBackups = 5;
            roller.MaximumFileSize = "1GB";
            roller.RollingStyle = RollingFileAppender.RollingMode.Size;
            roller.StaticLogFileName = true;            
            roller.ActivateOptions();
            hierarchy.Root.AddAppender(roller);

            MemoryAppender memory = new MemoryAppender();
            memory.ActivateOptions();
            hierarchy.Root.AddAppender(memory);

            hierarchy.Root.Level = Level.Info;
            hierarchy.Configured = true;
		}
		/**
			\brief Get the logger instance.
		*/
		public static ILog GetLogger()
		{
			return _log;
		}

		private static ILog _log =  LogManager.GetLogger(typeof(Logger));

	};
}
