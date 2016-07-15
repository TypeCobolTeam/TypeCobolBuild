/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jtcb.rtc;

import com.ibm.team.repository.common.TeamRepositoryException;
import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Set of test related to RTC FileSystem Management
 * @author MAYANJE
 */
public class TestRTCFileSystem {
    static RTCSession m_session;    
    
    public TestRTCFileSystem() {
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
    /**
     * This test read the content of a file directly in the source Workspace and component.
     * @throws TeamRepositoryException
     * @throws IOException 
     */
    @Test
    public void testReadWorkspaceFileContent() throws TeamRepositoryException, IOException 
    {
        RTCContext context = new RTCContext(m_session);
        context.setWorkspaceSource("BUILD_DEVZ_DEV_RWZ", "TEST_TYPECOBOL");
        //DVZF0OSM
        String content = RTCFileSystem.readFileContent(context.getWorkspaceStream(), context.getComponent(), context.getTeamRepository(), "DVZF0OSM.TCBL", context.getProgressMonitor(), context.getLogger());
        System.out.println(content);
        assertNotNull(content);
    }
    
    @Test
    /**
     * This test read the content of a file directly in the source Stream and component.
     * @throws TeamRepositoryException
     * @throws IOException 
     */    
    public void testReadStreamFileContent() throws TeamRepositoryException, IOException 
    {
        RTCContext context = new RTCContext(m_session);
        context.setStreamSource("DEVZ_DEV", "TEST_TYPECOBOL");
        String content = RTCFileSystem.readFileContent(context.getWorkspaceStream(), context.getComponent(), context.getTeamRepository(), "DVZF0OSM.TCBL", context.getProgressMonitor(), context.getLogger());
        System.out.println(content);
        assertNotNull(content);
    }        
}
