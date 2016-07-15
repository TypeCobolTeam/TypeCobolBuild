package jtcb.rtc;

import com.ibm.team.repository.client.internal.TeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.scm.common.IComponent;
import com.ibm.team.scm.common.IComponentHandle;
import com.ibm.team.scm.common.IWorkspaceHandle;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import jtcb.util.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Set of test related to RTC Context : Repository, Worksace, Stream, Component
 * @author MAYANJE
 */
public class TestRTCContext {
    static RTCSession m_session;    
    
    public TestRTCContext() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        String dir = System.getProperty("user.dir");
        String dll = new File(dir).getParent() + "\\Debug\\TCB.dll";
        System.load(dll);            
        m_session = new RTCSession();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {        
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    /**
     * This test tries to find a Repository Workspace using a RTC deprecated method that still works.
     */
    public void testFindWorkspaceDeprecated() 
    {        
        RTCContext context = new RTCContext(m_session.getTeamRepository());
        IWorkspaceHandle workspaceHandle = null;
        try {
            //This Workspace is take from Caroline BuildEngine test.
            workspaceHandle = RTCContext.findWorkspaceByName((TeamRepository)context.getTeamRepository(), "BUILD_DEVZ_DEV_RWZ", m_session.getProgressMonitor());            
        } catch (TeamRepositoryException ex) {
            Logger.getLogger(TestRTCContext.class.getName()).log(Level.SEVERE, null, ex);            
        }
        assertNotNull(workspaceHandle);
    }
    
    @Test
    /**
     * This test tries to find a Repository Workspace using a RTC the official method.
     */    
    public void testFindWorkspaceNotDeprecated() 
    {        
        RTCContext context = new RTCContext(m_session.getTeamRepository());
        IWorkspaceHandle workspaceHandle = null;
        try {
            //This Workspace is take from Caroline BuildEngine test.
            workspaceHandle = RTCContext.findWorkspace((TeamRepository)context.getTeamRepository(), "BUILD_DEVZ_DEV_RWZ", m_session.getProgressMonitor());            
        } catch (TeamRepositoryException ex) {
            Logger.getLogger(TestRTCContext.class.getName()).log(Level.SEVERE, null, ex);            
        }
        assertNotNull(workspaceHandle);
    }    
    
    @Test
    /**
     * This test tries to find a Repository Workspace using a RTC the official method.
     */    
    public void testFindStream() 
    {        
        RTCContext context = new RTCContext(m_session.getTeamRepository());
        IWorkspaceHandle workspaceHandle = null;
        try {
            //This Workspace is take from Caroline BuildEngine test.
            workspaceHandle = RTCContext.findStream((TeamRepository)context.getTeamRepository(), "DEVZ_DEV", m_session.getProgressMonitor());            
        } catch (TeamRepositoryException ex) {
            Logger.getLogger(TestRTCContext.class.getName()).log(Level.SEVERE, null, ex);            
        }
        assertNotNull(workspaceHandle);
    }     
    
    @Test
    /**
     * This test tries to find a Component from a Repository and a Workspace
     */
    public void testFindWorkspaceComponent()
    {
        RTCContext context = new RTCContext(m_session.getTeamRepository());
        IWorkspaceHandle workspaceHandle = null;        
        Pair<IComponentHandle,IComponent> component = null;
        try {
            //This Workspace is take from Caroline BuildEngine test.
            workspaceHandle = RTCContext.findWorkspace(context.getTeamRepository(), "BUILD_DEVZ_DEV_RWZ", m_session.getProgressMonitor());            
            component = RTCContext.findWorkspaceStreamComponent(context.getTeamRepository(), workspaceHandle, "TEST_TYPECOBOL", m_session.getProgressMonitor());
        } catch (TeamRepositoryException ex) {
            Logger.getLogger(TestRTCContext.class.getName()).log(Level.SEVERE, null, ex);            
        }   
        assertNotNull(component);
    }
    
    @Test
    /**
     * This test tries to find a Component from a Repository and a Stream
     */
    public void testFindStreamComponent()
    {
        RTCContext context = new RTCContext(m_session.getTeamRepository());
        IWorkspaceHandle workspaceHandle = null;        
        Pair<IComponentHandle,IComponent> component = null;
        try {
            //This Workspace is take from Caroline BuildEngine test.
            workspaceHandle = RTCContext.findStream(context.getTeamRepository(), "DEVZ_DEV", m_session.getProgressMonitor());            
            component = RTCContext.findWorkspaceStreamComponent(context.getTeamRepository(), workspaceHandle, "TEST_TYPECOBOL", m_session.getProgressMonitor());
        } catch (TeamRepositoryException ex) {
            Logger.getLogger(TestRTCContext.class.getName()).log(Level.SEVERE, null, ex);            
        }   
        assertNotNull(component);
    }    
}
