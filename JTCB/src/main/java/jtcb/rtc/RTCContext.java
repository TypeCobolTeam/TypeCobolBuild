package jtcb.rtc;

import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.common.query.IItemQueryPage;
import com.ibm.team.scm.client.IWorkspaceConnection;
import com.ibm.team.scm.client.IWorkspaceManager;
import com.ibm.team.scm.client.SCMPlatform;
import com.ibm.team.scm.common.IComponent;
import com.ibm.team.scm.common.IComponentHandle;
import com.ibm.team.scm.common.IWorkspaceHandle;
import com.ibm.team.scm.common.dto.IComponentSearchCriteria;
import com.ibm.team.scm.common.dto.IWorkspaceSearchCriteria;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import jtcb.util.LogOutProgressMonitor;
import jtcb.util.Pair;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * A RTC Context as it is used by the Java part of the Type Cobol Builder
 * is tiplet (Repository, Workspace or Stream, Component).
 * @author MAYANJE
 */
public class RTCContext {
    private ITeamRepository m_teamRepository;
    private IWorkspaceHandle m_deliverWorkspaceStreamHandle;
    private IWorkspaceHandle m_workspaceStreamHandle;
    private IComponentHandle m_componentHandle;
    private IComponent m_component;
    private List< Pair<String,String> > m_streamComponents;
    // Current workspace connection
    private IWorkspaceConnection m_deliverWorkspaceConnection;    
    // Current workspace connection
    private IWorkspaceConnection m_workspaceConnection;

    /**
     * The Progress monitor used by this session
     */
    private IProgressMonitor m_monitor;
    
    /**
     * Logging mechanism.
     */
    private Logger m_log = Logger.getLogger(RTCContext.class.getName());
    
    /**
     * Constructor
     * @param teamRepository The RTC Team Repositoty
     */
    public RTCContext(ITeamRepository teamRepository)
    {
        this(teamRepository, null, null);
    }

    /**
     * Constructor
     * @param teamRepository The RTC Team Repositoty
     */
    public RTCContext(RTCSession session)
    {
        m_teamRepository = session.getTeamRepository();
        m_log = session.getLogger();
        m_monitor = session.getProgressMonitor();                
    }
    
    /**
     * Constructor
     * @param teamRepository The RTC Team Repositoty
     * @param workspaceStreamHandle The source RTC Workspace handle
     * @param componentHandle The Source RTC Component Handle
     */
    public RTCContext(ITeamRepository teamRepository, IWorkspaceHandle workspaceStreamHandle, IComponentHandle componentHandle)
    {        
        m_teamRepository = teamRepository;
        m_workspaceStreamHandle = workspaceStreamHandle;
        m_componentHandle = componentHandle;
        m_monitor = new LogOutProgressMonitor();
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
     * Get the Team Repository instance
     * @return Team Repository
     */
    public ITeamRepository getTeamRepository()
    {
        return m_teamRepository;
    }
    
    /**
     * Set the Team Repository
     * @param teamRepository The Team Repository
     */
    public void setTeamRepository(ITeamRepository teamRepository)
    {
        m_teamRepository = teamRepository;
    }
    
    /**
     * get the source Workspace or Stream.
     * @return 
     */
    public IWorkspaceHandle getWorkspaceStream()
    {
        return m_workspaceStreamHandle;
    }
    
    /**
     * Set the Workspace stream handle
     * @param streamHandle The Workspace Stream handle.
     */
    public void setWorkspaceStream(IWorkspaceHandle streamHandle)
    {
        m_workspaceStreamHandle = streamHandle;
    }
    
    /**
     * Get the component
     * @return The component.
     */
    public IComponentHandle getComponent()
    {
        return m_componentHandle;
    }
    
    /**
     * Set the workspace component handle.
     * @param component 
     */
    public void setComponent(IComponentHandle component)
    {
        m_componentHandle = component;
    }
    
    /**
     * get the source Workspace or Stream used to delivery.
     * @return 
     */
    public IWorkspaceHandle getDeliverWorkspaceStream()
    {
        return m_deliverWorkspaceStreamHandle;
    }
    
    /**
     * Set the Workspace stream handle used to delivery
     * @param streamHandle The Workspace Stream handle.
     */
    public void setDeliverWorkspaceStream(IWorkspaceHandle streamHandle)
    {
        m_deliverWorkspaceStreamHandle = streamHandle;
    }
    
    /**
     * The MList of Streams and Components specified for lppkoing for copies.
     * @return the list of Streams and components for looking for copies.
     */
    public List< Pair<String,String> > getStreamAndComponentIdentification()
    {
        return m_streamComponents;
    }
    
    /**
     * Sets the Stream and Components identifications
     * @param streamComponents 
     */
    public void setStreamAndComponentIdentification(List< Pair<String,String> > streamComponents)
    {
        m_streamComponents = streamComponents;
    }
    
    /**
     * Set the Source context based on a workspace and a component
     * @param workspaceName The source workspace name
     * @param component The source component
     * @throws com.ibm.team.repository.common.TeamRepositoryException
     */
    public void setWorkspaceSource(String workspaceName, String component) throws TeamRepositoryException
    {        
        m_workspaceStreamHandle = findWorkspace(m_teamRepository, workspaceName, m_monitor);
        Pair<IComponentHandle,IComponent> comps = findWorkspaceStreamComponent(m_teamRepository, m_workspaceStreamHandle, component, m_monitor);
        this.m_componentHandle = comps.fst;
        this.m_component = comps.snd;
    }

    /**
     * Set the Source context based on a stream and a coponent
     * @param streamName The source workspace name
     * @param component The source component
     * @throws com.ibm.team.repository.common.TeamRepositoryException
     */
    public void setStreamSource(String streamName, String component) throws TeamRepositoryException
    {        
        m_workspaceStreamHandle = findStream(this.m_teamRepository, streamName, m_monitor);
        Pair<IComponentHandle,IComponent> comps = findWorkspaceStreamComponent(m_teamRepository, m_workspaceStreamHandle, component, m_monitor);
        this.m_componentHandle = comps.fst;
        this.m_component = comps.snd;        
    }

    /**
     * Set the Source context based on a stream and a coponent
     * @param deliveryStreamOrWorkspace The Workspace or Stream used to deliver
     * @param sourceStreamOrWorkspaceName The Workspace or Stream as source
     * @param component The source component
     * @throws com.ibm.team.repository.common.TeamRepositoryException
     */
    public void setDeliverStreamWorkspaceSource(String deliveryStreamOrWorkspace, String sourceStreamOrWorkspaceName, String component) throws TeamRepositoryException
    {   
        m_deliverWorkspaceStreamHandle = findWorkspaceOrStream(m_teamRepository, deliveryStreamOrWorkspace, m_monitor);
        m_workspaceStreamHandle = findWorkspaceOrStream(this.m_teamRepository, sourceStreamOrWorkspaceName, m_monitor);
        Pair<IComponentHandle,IComponent> comps = findWorkspaceStreamComponent(m_teamRepository, m_workspaceStreamHandle, component, m_monitor);
        this.m_componentHandle = comps.fst;
        this.m_component = comps.snd;        
    }
    
    /**
     * Get the workspace connection associated to the current workspace or stream.
     * @return The Workspace connection if any, null otherwise.
     * @throws TeamRepositoryException 
     */
    public IWorkspaceConnection getWorkspaceConnection() throws TeamRepositoryException
    {
        if (m_workspaceConnection == null)
        {
            m_workspaceConnection = 
                SCMPlatform.getWorkspaceManager(getTeamRepository())
                        .getWorkspaceConnection(getWorkspaceStream(), getProgressMonitor());
        }
        return m_workspaceConnection;
    }
    
    /**
     * Get the workspace connection associated to the current workspace or stream used for the delivery.
     * @return The Workspace connection if any, null otherwise.
     * @throws TeamRepositoryException 
     */
    public IWorkspaceConnection getDeliverWorkspaceConnection() throws TeamRepositoryException
    {
        if (m_deliverWorkspaceConnection == null)
        {
            m_deliverWorkspaceConnection = 
                SCMPlatform.getWorkspaceManager(getTeamRepository())
                        .getWorkspaceConnection(getDeliverWorkspaceStream(), getProgressMonitor());
        }
        return m_deliverWorkspaceConnection;
    }
    
    /**
     * Given a TeamRepository instance and a Workspace Stream name, this function tries to find
     * the associated Workspace stream handle.
     * @param teamRepository The Team Repository instance
     * @param workspaceName The Workspace stream nale
     * @param monitor The progression monitor object
     * @return The Workspace stream handle if any.
     * @throws TeamRepositoryException 
     */
    @Deprecated
    public static IWorkspaceHandle findWorkspaceByName(ITeamRepository teamRepository, String workspaceName, IProgressMonitor monitor) throws TeamRepositoryException
    {
        IWorkspaceManager wm = SCMPlatform.getWorkspaceManager(teamRepository);
        IItemQueryPage itemQueryPage = wm.findWorkspacesByName(workspaceName, true , false , false , 1, monitor);
        List<IWorkspaceHandle> handles = itemQueryPage.getItemHandles();
        return handles.get(0);        
    }
    
    /**
     * Given a Workspace name, this function tries to find
     * the associated Workspace stream handle.
     * @param teamRepository The Team Repository instance
     * @param workspaceName The Workspace stream name
     * @param monitor The progression monitor object
     * @return The Workspace stream handle if any.
     * @throws TeamRepositoryException 
     */    
    public static IWorkspaceHandle findWorkspace(ITeamRepository teamRepository, String workspaceName, IProgressMonitor monitor) throws TeamRepositoryException
    {
        // Create search Criteria and get Baselines 
        IWorkspaceSearchCriteria crit1 = IWorkspaceSearchCriteria.FACTORY.newInstance();         
        // Get snapshot stream 
        crit1.setExactName(workspaceName);        
        crit1.setKind(IWorkspaceSearchCriteria.WORKSPACES); 

        IWorkspaceManager wm = SCMPlatform.getWorkspaceManager(teamRepository);
        List<IWorkspaceHandle> tmp_list = wm.findWorkspaces(crit1, 1, monitor); 
        if (tmp_list.isEmpty()) { 
            throw new TeamRepositoryException("Workspace " + workspaceName + " does not exists"); 
        } 
        return tmp_list.iterator().next();
    }    

    /**
     * Given a Stream name, this function tries to find the associated Workspace stream handle.
     * the associated Workspace stream handle.
     * @param teamRepository The Team Repository instance
     * @param streamName The stream name
     * @param monitor The progression monitor object
     * @return The Workspace stream handle if any.
     * @throws TeamRepositoryException 
     */    
    public static IWorkspaceHandle findStream(ITeamRepository teamRepository, String streamName, IProgressMonitor monitor) throws TeamRepositoryException
    {        
        // Create search Criteria and get Baselines 
        IWorkspaceSearchCriteria crit1 = IWorkspaceSearchCriteria.FACTORY.newInstance();         
        // Get snapshot stream 
        crit1.setExactName(streamName);        
        crit1.setKind(IWorkspaceSearchCriteria.STREAMS); 

        IWorkspaceManager wm = SCMPlatform.getWorkspaceManager(teamRepository);
        List<IWorkspaceHandle> tmp_list = wm.findWorkspaces(crit1, 1, monitor); 
        if (tmp_list.isEmpty()) { 
            throw new TeamRepositoryException("Stream " + streamName + " does not exists"); 
        } 
        return tmp_list.iterator().next();
    }    
    
    /**
     * Given a Stream or a Workspace name, this function tries to find
     * @param teamRepository
     * @param workspaceOrStreamName
     * @param monitor
     * @return The Workspace handle
     * @throws TeamRepositoryException 
     */
    public static IWorkspaceHandle findWorkspaceOrStream(ITeamRepository teamRepository, String workspaceOrStreamName, IProgressMonitor monitor) throws TeamRepositoryException
    {
        IWorkspaceHandle workspaceHandle = null;        
        try
        { //First try for a Workspace
            workspaceHandle = findWorkspace(teamRepository, workspaceOrStreamName, monitor);
        }
        catch(TeamRepositoryException tre)
        {
            //Second try Stream
            workspaceHandle = findStream(teamRepository, workspaceOrStreamName, monitor);
        }
        return workspaceHandle;
    }
    
    /**
     * Find component in a Workspace Stream
     * @param teamRepository  The Team Repository instance
     * @param workspaceStreamHandle the Workspace or Stream
     * @param componentName The Component to find
     * @param monitor The progression monitor object
     * @return A pair Component handles if any null otherwise.
     * @throws TeamRepositoryException 
     */
    public static Pair<IComponentHandle,IComponent> findWorkspaceStreamComponent(ITeamRepository teamRepository, IWorkspaceHandle workspaceStreamHandle, String componentName, IProgressMonitor monitor) throws TeamRepositoryException    
    {
        IWorkspaceManager wm = SCMPlatform.getWorkspaceManager(teamRepository);
        IComponentSearchCriteria criteria = IComponentSearchCriteria.FACTORY.newInstance();
        criteria.setExactName(componentName);
                IWorkspaceConnection stream = wm.getWorkspaceConnection(workspaceStreamHandle, null); 

        // Get component Handle 
        List components = stream.getComponents(); 
        for (Iterator a = components.iterator(); a.hasNext();) { 
            IComponentHandle compHandle = (IComponentHandle) a.next(); 
            IComponent comp = (IComponent)teamRepository.itemManager().fetchCompleteItem(compHandle, IItemManager.DEFAULT, monitor); 
            if (comp.getName().equals(componentName))
                return new Pair<IComponentHandle,IComponent>(compHandle, comp);
        }         
        throw new TeamRepositoryException("Component " + componentName + " does not exists"); 
        //return null;
    }    
}
