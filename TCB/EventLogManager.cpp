#include "stdafx.h"
#include "EventLogManager.h"

void EventLogManager::OnEntryWritten(System::Object ^source, EntryWrittenEventArgs ^e)
{
    if (e->Entry->Source == sSourceLog)
    {
		  try {
			 EventLogObserver(source, e);
		  }
		  catch(System::NullReferenceException^) {
		  }
        //Console.WriteLine("written entry: " + e.Entry.Message);
    }
}

bool EventLogManager::CheckEventSource()
{
    //String TestPath = System.IO.Directory.GetCurrentDirectory();
    try
    {
        if (!EventLog::SourceExists((System::String ^)sSourceLog, (System::String ^)sLog))
        {
            try
            {
                EventLog::CreateEventSource((System::String ^)sSourceLog, (System::String ^)sLog);
            }
            catch (System::ArgumentException ^)
            {
                return false;
            }
            catch (System::InvalidOperationException ^)
            {
                return false;
            }
        }
    }
    catch (System::IO::IOException ^ex)
    {
        String ^msg = ex->ToString();
        System::Diagnostics::Debug::WriteLine(msg);
    }
    return true;
}

void EventLogManager::WriteEntry(System::String ^message, EventLogEntryType type, int eventID, Category category)
{
    if (LogOn)
    {
        if (CheckEventSource())
        {
            try
            {
                EventLog::WriteEntry((System::String ^)sSourceLog, message, type, eventID, (short)category);
            }
            catch (Exception ^)
            {
            }
        }
    }
}

void EventLogManager::WriteEntry(System::String ^message, EventLogEntryType type, Category category)
{
	WriteEntry(message, type, 0, category);
}

void EventLogManager::Clear()
{
	Singleton->Clear();
}

