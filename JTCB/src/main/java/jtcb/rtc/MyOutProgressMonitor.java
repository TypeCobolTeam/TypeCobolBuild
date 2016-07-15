package jtcb.rtc;
import org.eclipse.core.runtime.IProgressMonitor;

public class MyOutProgressMonitor implements IProgressMonitor {

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
    
    private void print(String name) {
        if(name != null && ! "".equals(name))
            System.out.println(name);
    }
}
