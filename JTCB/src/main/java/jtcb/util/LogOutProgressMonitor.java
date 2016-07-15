package jtcb.util;
import java.util.logging.Logger;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * A Log Progress Monitor.
 * @author MAYANJE
 */
public class LogOutProgressMonitor implements IProgressMonitor {

    /**
     * The logger
     */
    Logger m_log;    
    
    /**
     * Empty constructor.
     */
    public LogOutProgressMonitor()
    {
        m_log = Logger.getGlobal();
    }
    
    /**
     * Constructor.
     * @param log 
     */
    public LogOutProgressMonitor(Logger log)
    {
        m_log = log;
    }
    @Override
    public void beginTask(String name, int totalWork) {
        print(name);
    }

    @Override
    public void done() {
    }

    @Override
    public void internalWorked(double work) {
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void setCanceled(boolean value) {
    }

    @Override
    public void setTaskName(String name) {
        print(name);
    }

    @Override
    public void subTask(String name) {
        print(name);
    }

    @Override
    public void worked(int work) {
    }
    
    private void print(String message) {
        if(message != null && ! "".equals(message))
        {
            System.out.println(message);
            if (m_log != null)
                m_log.info(message);
            //Log it to TCB
            //JTCBEngine.log_info(message);
        }
    }
}
