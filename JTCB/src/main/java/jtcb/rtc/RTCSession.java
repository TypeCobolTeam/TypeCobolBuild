package jtcb.rtc;

import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.common.TeamRepositoryException;
import java.util.logging.Logger;
import jtcb.AuthentificationDialog;
import jtcb.util.LogOutProgressMonitor;
import org.apache.commons.logging.Log;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Object that representa RTC Session. When a RTC session instance is created it asks for
 * a connection to a RTC repository if no connection information are supplied.
 * @author MAYANJE
 */
public class RTCSession {
    private ITeamRepository m_teamRepository;
    private String m_repositoryURI;
    private String m_user;
    private String m_password;
    /**
     * The Progress monitor used by this session
     */
    private IProgressMonitor m_monitor;
    
    /**
     * Logging mechanism.
     */
    private Logger m_log = Logger.getGlobal();//Logger.getLogger(RTCSession.class.getName());
    
    /**
     * Indicator if a session has started
     */
    private boolean m_sessionStarted;
    
    /**
     * Empty constructor
     */
    public RTCSession()
    {
        m_monitor = new LogOutProgressMonitor(m_log);
        loginSession(null);
        openUserSession();
    }

    /**
     * Constructor with only the repository URI given
     * @param repositoryURI 
     */
    public RTCSession(String repositoryURI)
    {
        m_monitor = new LogOutProgressMonitor();
        loginSession(repositoryURI);
        openUserSession();
    }
    
    /**
     * Get this session Team Repository.
     * @return The Team Repository instance
     */
    public ITeamRepository getTeamRepository()
    {
        return m_teamRepository;
    }
    
    /**
     * Get this session Repository URI
     * @return the Repository URI
     */
    public String getRepositoriURI()
    {
        return m_repositoryURI;
    }
    
    /**
     * Get the User name of this session
     * @return The User name
     */
    public String getUser()
    {
        return m_user;
    }
    
    /**
     * Get the password of this session
     * @return The User password.
     */
    public String getPassword()
    {
        return m_password;
    }
    
    /**
     * Get the logger for this session
     * @return The logger
     */
    public Logger getLogger()
    {
        return m_log;
    }
    
    /**
     * Get the Progress monitor for this session.
     * @return The progress monitor
     */
    public IProgressMonitor getProgressMonitor()
    {
        return m_monitor;
    }
    
    /**
     * Parameterize constructor
     * @param repositoryURI The repository URI
     * @param user  The user name
     * @param password The user passoword
     */
    public RTCSession(String repositoryURI, String user, String password)
    {
        m_repositoryURI = repositoryURI;
        m_user = user;
        m_password = password;
        m_monitor = new LogOutProgressMonitor();
        openUserSession();
    }   
    
    /**
     * Initiate a Repository login session.
     */
    private void loginSession(String repositoryURI)
    {
        AuthentificationDialog dialog = new AuthentificationDialog(null, true);
        if (repositoryURI != null)
            dialog.m_RepositoryTextField.setText(repositoryURI);        
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
       if (dialog.getReturnStatus() == AuthentificationDialog.RET_CANCEL)
       {
           System.err.println("User cancel");
           m_log.info("Authentification canceled by the user!");
           System.exit(1);
       }
       m_user = dialog.m_UserNameField.getText().trim();
       m_password = dialog.m_PasswordField.getText().trim();
       m_repositoryURI = dialog.m_RepositoryTextField.getText().trim();        
    }

    /**
     * Open a user session
     */
    private void openUserSession()
    {
        TeamPlatform.startup();
        m_sessionStarted = true;
        try {     
            m_teamRepository = RepositoryConnection.login(m_repositoryURI , m_user, m_password, m_monitor);
        } catch (TeamRepositoryException e) {
            String msg = "Unable to login: " + e.getMessage();
            System.err.println(msg);
            m_log.info(e.getMessage());
            TeamPlatform.shutdown();
            System.exit(1);
        } finally {
        }      
    }    
    
    /**
     * Terminate a session
     * @throws java.lang.Throwable
     */
    @Override
    protected void finalize() throws Throwable
    {
        try {
            if (m_teamRepository != null)
                m_teamRepository.logout();
            
            if (m_monitor != null)
                m_monitor.subTask("Disconnected");
            
            if (m_sessionStarted)
                TeamPlatform.shutdown();
        } finally {
            super.finalize();
        }
    }
}
