package jtcb.rtc;

import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.common.TeamRepositoryException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Handle the connection to a repository
 * @author MAYANJE
 */
public class RepositoryConnection {    
    /**
     * Perform the logging to a repository
     * @param repositoryURI The repository URI
     * @param user the user name
     * @param password the password
     * @param monitor The progression monitor
     * @return The Team Repository interface
     * @throws TeamRepositoryException 
     */
    public static ITeamRepository login(String repositoryURI, final String user, final String password, IProgressMonitor monitor) throws TeamRepositoryException {    	        
        ITeamRepository repository = TeamPlatform.getTeamRepositoryService().getTeamRepository(repositoryURI);
        repository.registerLoginHandler(new ITeamRepository.ILoginHandler() {
            @Override
            public ILoginInfo challenge(ITeamRepository repository) {
                return new ILoginInfo() {
                    @Override
                    public String getUserId() {
                        return user;
                    }
                    @Override
                    public String getPassword() {
                        return password;                        
                    }
                };
            }
        });
        if (monitor != null)
            monitor.subTask("Contacting " + repository.getRepositoryURI() + "...");
        repository.login(monitor);
        if (monitor != null)
            monitor.subTask("Connected");
        return repository;
    }    
}
