package jtcb.rtc;

import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.common.TeamRepositoryException;
import java.text.MessageFormat;
import java.util.logging.Logger;
import jtcb.AuthentificationDialog;
import jtcb.util.LogOutProgressMonitor;
import jtcb.util.Resources;
import org.eclipse.core.runtime.IProgressMonitor;
import com.ibm.team.build.internal.PasswordHelper;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;

/**
 * Object that representa RTC Session. When a RTC session instance is created it asks for
 * a connection to a RTC repository if no connection information are supplied.
 * @author MAYANJE
 */
public class RTCSession {
    private static String jtcb_passwordFile = "jtcb_{0}.login";
    private ITeamRepository m_teamRepository;
    private String m_repositoryURI;
    private String m_user;
    private String m_password;
    private boolean m_loginFromPasswordFile = false; //True if the login has been performed using the Password file.
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
     * Get the password File
     * @return The password file
     */
    private static File getPasswordFile()
    {
        String user = System.getProperty("user.name");
        String user_home = System.getProperty("user.home");

        //Asks the user if he wants to create a log file.
        String log_file = MessageFormat.format(jtcb_passwordFile, user.toLowerCase());
        File file = new File(user_home, log_file);
        return file;
    }
    /**
     * Tries to get the password from a Password file.
     * @return the Password if any, null otherwise.
     */
    private static String getPasswordFromFile()
    {
        File file = getPasswordFile();
        if (!file.exists())
            return null;
        try {
            String password = PasswordHelper.getPassword(file);
            return password;
        } catch (Exception ex) {
            Logger.getLogger(RTCSession.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Create the usr password file. After asking if he wants to create one.
     * @param user The user
     * @param password The password
     * @param bPrompt Prompt the user if true
     */
    private static void createPasswordFile(String user, String password, boolean bPrompt)
    {
        String user_home = System.getProperty("user.home");

        //Asks the user if he wants to create a log file.
        File log_file = getPasswordFile();
        int nResult = bPrompt ? javax.swing.JOptionPane.showConfirmDialog(null,
                MessageFormat.format(Resources.getMessage("create_ps_file_question"), log_file),
                Resources.getMessage("create_ps_file_title"),
                javax.swing.JOptionPane.YES_NO_OPTION) : javax.swing.JOptionPane.YES_OPTION;
        if (nResult == javax.swing.JOptionPane.NO_OPTION)
            return;
        try {
            com.ibm.team.build.internal.PasswordHelper.createPasswordFile(log_file, password);
        } catch (IOException | GeneralSecurityException ex) {
            Logger.getLogger(RTCSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initiate a Repository login session.
     */
    private void loginSession(String repositoryURI)
    {
        String user = System.getProperty("user.name");
        String password = !m_loginFromPasswordFile ? getPasswordFromFile() : null;
        if (password == null || repositoryURI.isEmpty())
        {
            AuthentificationDialog dialog = new AuthentificationDialog(null, true);
            if (repositoryURI != null)
                dialog.m_RepositoryTextField.setText(repositoryURI);

            dialog.m_UserNameField.setText(user);
            if (password != null)
                dialog.m_PasswordField.setText(password);

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
            createPasswordFile(m_user, m_password, !dialog.m_createPassFileCheck.isSelected());
            m_loginFromPasswordFile = false;
        }
        else
        {
            m_user = user;
            m_password = password;
            m_repositoryURI = repositoryURI;
            m_loginFromPasswordFile = true;
        }
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
            if (m_loginFromPasswordFile)
            {//If the login was from the File try a manual login by opening the dialog box.
                loginSession(m_repositoryURI);
                try {
                    m_teamRepository = RepositoryConnection.login(m_repositoryURI , m_user, m_password, m_monitor);
                    return;
                } catch (TeamRepositoryException ex) {
                }
            }
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
