/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jtcb.rtc;

import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.scm.common.IChangeSet;
import com.ibm.team.scm.common.IChangeSetHandle;
import com.ibm.team.scm.common.IFolderHandle;
import com.ibm.team.scm.common.IVersionableHandle;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.IWorkItemWorkingCopyManager;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import java.io.File;
import java.io.IOException;
import static jtcb.rtc.TestRTCFileSystem.m_session;
import jtcb.util.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Set of tests related to RTC ChangeSet.
 * Note all this test use RTC.SI in reading mode.
 * DON'T USE THESE TESTS FOR WRITTING SOMETHING IN THE RTC_SI.
 * @author MAYANJE
 */
public class TestChangeSet {
    
    public TestChangeSet() {
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
     * This Test uses RTC_SI IN !!!READ MODE ONLY!!!
     * To Display the comment associated with the last ChangeSet that
     * contains the file TestChangeSet.java
     */
    public void testLastChangeSet() throws TeamRepositoryException, IOException {
        RTCContext context = new RTCContext(m_session);
        context.setStreamSource("TCOB_DEV", "TCOB_TCBUILDENGINE");
        RTCFileSystem fs = new RTCFileSystem(context);
        Pair<IFolderHandle, IVersionableHandle> handles = fs.getFileHandles("TestChangeSet.java");
        assertNotNull(handles);
        assertNotNull(handles.snd);
        IChangeSetHandle changeSetHandle = fs.getLastChangeSet(handles.snd);
        assertNotNull(changeSetHandle);
        if (changeSetHandle != null)
        {
            IItemManager itemManager = context.getTeamRepository().itemManager();
            final IChangeSet changeSet = 
                            (IChangeSet) itemManager.fetchCompleteItem(
                                    changeSetHandle,
                                    IItemManager.REFRESH,
                                    context.getProgressMonitor());
            System.out.println(changeSetHandle.toString());
            System.out.println("[*******]"+changeSet.getComment()+"[*******]");
        }
    }    
    
    @Test
    /**
     * This Test uses RTC_SI IN !!!READ MODE ONLY!!!
     * To Display the description of associated with the WorkItem of the last ChangeSet that
     * contains the file TestChangeSet.java
     */    
    public void testLastChangeSetWithWorkItem() throws TeamRepositoryException, IOException {
        RTCContext context = new RTCContext(m_session);
        context.setStreamSource("TCOB_DEV", "TCOB_TCBUILDENGINE");
        RTCFileSystem fs = new RTCFileSystem(context);
        Pair<IFolderHandle, IVersionableHandle> handles = fs.getFileHandles("TestChangeSet.java");
        assertNotNull(handles);
        assertNotNull(handles.snd);
        IChangeSetHandle changeSetHandle = fs.getLastChangeSet(handles.snd);
        assertNotNull(changeSetHandle);
        if (changeSetHandle != null)
        {
            IWorkItemHandle workItemHandlde = fs.getChangeSetWorkItem(changeSetHandle);
            assertNotNull(workItemHandlde);
            IWorkItemWorkingCopyManager workingCopyManager = 
                    ((IWorkItemClient) context.getTeamRepository().getClientLibrary(IWorkItemClient.class)).getWorkItemWorkingCopyManager();
            workingCopyManager.connect(workItemHandlde, IWorkItem.FULL_PROFILE, context.getProgressMonitor());
            try
            {                
                WorkItemWorkingCopy  workItemCopy = workingCopyManager.getWorkingCopy(workItemHandlde);
                System.out.println(workItemCopy.getWorkItem().getHTMLDescription().getPlainText());
            }
            finally
            {
                workingCopyManager.disconnect(workItemHandlde);
            }            
        }
    }    
}
