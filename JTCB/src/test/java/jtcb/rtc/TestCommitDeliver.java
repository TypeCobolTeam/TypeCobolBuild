package jtcb.rtc;

import com.ibm.team.filesystem.common.IFileContent;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.scm.client.IConfiguration;
import com.ibm.team.scm.client.IWorkspaceConnection;
import com.ibm.team.scm.common.IChangeSetHandle;
import com.ibm.team.scm.common.IFolderHandle;
import com.ibm.team.scm.common.IVersionable;
import com.ibm.team.scm.common.IVersionableHandle;
import com.ibm.team.scm.common.IWorkspaceHandle;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import jtcb.util.Pair;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author MAYANJE
 */
public class TestCommitDeliver {
    static RTCSession m_session;    
    
    public TestCommitDeliver() {
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
     * This Test simulate the commit and deliver of a file that is a new or an update
     * from a file that exits in the workspace, without association the
     * commit/deliver to a WorkItem.
     */
    public void testCommitDeliverOnly() throws TeamRepositoryException, IOException 
    {
        RTCContext context = new RTCContext(m_session);
        //context.setWorkspaceSource("BUILD_DEVZ_DEV_RWZ", "TEST_TYPECOBOL");
        context.setWorkspaceSource("MAYANJE_DEVZ_DEV_RW", "TEST_TYPECOBOL");
        
        //1)Read the source file
        String content = RTCFileSystem.readFileContent(context.getWorkspaceStream(), 
                context.getComponent(), 
                context.getTeamRepository(), "DVZF0OSM.TCBL", context.getProgressMonitor(), context.getLogger());  
        assertNotNull(content);
        
        //2) Create or Update a file DVZF0OSMBis.TCBL
        content = content + "\r\n" + "      *    Generate from JTCB Test : " + new java.util.Date().toString();
        RTCFileSystem fs = new RTCFileSystem(context);
        //Get the root folder handle and the verison of the source file
        Pair<IFolderHandle, IVersionableHandle> handles = fs.getFileHandles("DVZF0OSM.TCBL");
        //No WorkItem associated
        IWorkItemHandle workItemHandle = null;
        //Try to see if the file that we want to add exits if so this is an update       
        String fileName = "DVZF0OSMBis.TCBL";
        Pair<IFolderHandle, IVersionableHandle> handles_bis = fs.getFileHandles(fileName);
        IVersionable versionable = null;
        if (handles_bis.snd != null)
        {
            //fetch a versionable from IConfiguration   
            IWorkspaceConnection workspaceConnection = context.getWorkspaceConnection();
            IConfiguration configuration = workspaceConnection.configuration(context.getComponent());
            versionable = configuration.fetchCompleteItem(handles_bis.snd, context.getProgressMonitor());
        }        
        fs.CreateOrUpdateFile(null, handles.fst, versionable, fileName, content, IFileContent.ENCODING_UTF_8, "Test commit from JTCB : " + new Date().toString(), workItemHandle, true);
    }

    @Test
    /**
     * This Test simulate the commit and deliver of a file that is a new or an update
     * from a file that exits in the workspace, with association the
     * commit/deliver to a WorkItem.
     */    
    public void testCommitDeliverWithWorkItem() throws TeamRepositoryException, IOException 
    {
        RTCContext context = new RTCContext(m_session);
        //context.setWorkspaceSource("BUILD_DEVZ_DEV_RWZ", "TEST_TYPECOBOL");
        context.setWorkspaceSource("MAYANJE_DEVZ_DEV_RW", "TEST_TYPECOBOL");
        
        //1)Read the source file
        String content = RTCFileSystem.readFileContent(context.getWorkspaceStream(), 
                context.getComponent(), 
                context.getTeamRepository(), "DVZF0OSM.TCBL", context.getProgressMonitor(), context.getLogger());  
        assertNotNull(content);
        
        //2) Create or Update a file DVZF0OSMTiers.TCBL
        content = content + "\r\n" + "      *    Generate from JTCB Test : " + new java.util.Date().toString();
        RTCFileSystem fs = new RTCFileSystem(context);
        //Get the root folder handle and the verison of the source file
        Pair<IFolderHandle, IVersionableHandle> handles = fs.getFileHandles("DVZF0OSM.TCBL");
        //Get the last change set of the source file.
        IChangeSetHandle changeSetHandle = fs.getLastChangeSet(handles.snd);        
        //Get any WorkItem associated with the last change set
        IWorkItemHandle workItemHandle = fs.getChangeSetWorkItem(changeSetHandle);
        //Try to see if the file that we want to add exits if so this is an update    
        String fileName = "DVZF0OSMTiers.TCBL";
        Pair<IFolderHandle, IVersionableHandle> handles_bis = fs.getFileHandles(fileName);
        IVersionable versionable = null;
        if (handles_bis.snd != null)
        {
            //fetch a versionable from IConfiguration   
            IWorkspaceConnection workspaceConnection = context.getWorkspaceConnection();
            IConfiguration configuration = workspaceConnection.configuration(context.getComponent());
            versionable = configuration.fetchCompleteItem(handles_bis.snd, context.getProgressMonitor());
        }        
        fs.CreateOrUpdateFile(null, handles.fst, versionable, fileName, content, IFileContent.ENCODING_UTF_8, "Test commit from JTCB : " + new Date().toString(), workItemHandle, true);
    }    
}
