#ifndef __tcb_EventLogManager_h
#define __tcb_EventLogManager_h

using namespace System;
using namespace System::Diagnostics;
using namespace System::Runtime::CompilerServices;

/// <summary>
/// A class for writting to an event log.
/// Event logging provides a standard, centralized way for your applications to record important 
/// software and hardware events. Windows supplies a standard user interface for viewing the logs, 
/// the Event Viewer. By using the common language's run-time EventLog component, 
/// you can connect to existing event logs easily, on both local and remote computers, 
/// and write entries to these logs. 
/// You can also read entries from existing logs and create your own custom event logs.
/// http://support.microsoft.com/kb/307024/en-us
/// On Windows 7
/// 1. Open the Start Menu, type eventvwr.msc, and press Enter.
/// 2. If you are logged in as an administrator and prompted by UAC, then click on Yes.
/// 3. In the left pane, click on an event log to see it's events listed at the top of the middle pane.
/// 4. If you like, you could also right click on an event log in the left pane for more options to use. For example, to Filter Current Log or Find a specific event ID number listed in the middle pane.
/// FOR TypeCobolBuilder
/// Journaux Windows/Application/
/// </summary>
public ref class EventLogManager
{
	public:
		/// <summary>
		/// Log Source name
		/// </summary>
		static const System::String ^ sSourceLog = "TypeCobolBuilder";
		/// <summary>
		/// The Journal's name
		/// </summary>
		static const System::String ^sLog = "Application";
        /// <summary>
        /// The event log observer.
        /// </summary>
        static event EntryWrittenEventHandler ^EventLogObserver;

	private:
		static EventLog ^Singleton;
		static Boolean m_bLogOn = true;
	public:
        /// <summary>
        /// Static constructor.
        /// </summary>
		static EventLogManager()
		{
            Singleton = gcnew EventLog();
            Singleton->Log = (System::String ^)sLog;
            Singleton->EntryWritten += gcnew EntryWrittenEventHandler(OnEntryWritten);
            // EnableRaisingEvents gets or sets a value indicating whether the
            // EventLog instance receives EntryWritten event notifications.
            Singleton->EnableRaisingEvents = true;
		}

        /// <summary>
        /// Entry point of an event
        /// </summary>
        /// <param name="source">Source of the event</param>
        /// <param name="e">Event arguments</param>
		[MethodImpl(MethodImplOptions::Synchronized)]
        static void OnEntryWritten(System::Object ^source, EntryWrittenEventArgs ^e);

        /// <summary>
        /// Various Log Categories
        /// </summary>
        enum class Category
        {
            JTCB = 100,
            TCB = 110,
            TypeCobolBuilder = 120,
			JNI = 130
        };

        /// <summary>
        /// Enable Log or not
        /// </summary>		
        property static bool LogOn
        {
			[MethodImpl(MethodImplOptions::Synchronized)]
            bool get()
            {
                return m_bLogOn;
            }
			[MethodImpl(MethodImplOptions::Synchronized)]
            void set(bool value)
            {
                m_bLogOn = value;
            }
        }

        /// <summary>
		/// Clear event logs
        /// <see cref="EventLog.Clear"/>
        /// </summary>
		[MethodImpl(MethodImplOptions::Synchronized)]
        static void Clear();

        /// <summary>
        /// <see cref="EventLog.WriteEntry"/>
        /// </summary>
        /// <param name="message">The message to Log</param>
		/// <param name="type">The message log entry type</param>
		/// <param name="eventID">The message event id</param>
		/// <param name="category">The message category</param>
		[MethodImpl(MethodImplOptions::Synchronized)]
        static void WriteEntry(System::String ^message, EventLogEntryType type, int eventID, Category category);

        /// <summary>
        /// <see cref="EventLog.WriteEntry"/>
        /// </summary>
        /// <param name="message">The message to Log</param>
		/// <param name="type">The message log entry type</param>
		/// <param name="category">The message category</param>
		[MethodImpl(MethodImplOptions::Synchronized)]
        static void WriteEntry(System::String ^message, EventLogEntryType type, Category category);

	private:
        /// <summary>
        /// Check that the Log Event Source Log exists
        /// </summary>
        /// <returns>true if the Log Event Source Log exists, fals eotherwise</returns>
        static bool CheckEventSource();
};

#endif
