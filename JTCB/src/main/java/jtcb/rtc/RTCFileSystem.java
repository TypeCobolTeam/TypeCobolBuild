package jtcb.rtc;

import com.ibm.team.filesystem.client.FileSystemCore;
import com.ibm.team.filesystem.client.IFileContentManager;
import com.ibm.team.filesystem.client.workitems.IFileSystemWorkItemManager;
import com.ibm.team.filesystem.common.FileLineDelimiter;
import com.ibm.team.filesystem.common.IFileContent;
import com.ibm.team.filesystem.common.IFileItem;
import com.ibm.team.filesystem.common.internal.FileItem;
import com.ibm.team.filesystem.common.workitems.ILinkConstants;
import com.ibm.team.links.common.ILink;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.util.IClientLibraryContext;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.scm.client.IChangeHistory;
import com.ibm.team.scm.client.IConfiguration;
import com.ibm.team.scm.client.IWorkspaceConnection;
import com.ibm.team.scm.client.IWorkspaceManager;
import com.ibm.team.scm.client.IWorkspaceManager.IVersionableLockOperation;
import com.ibm.team.scm.client.IWorkspaceManager.IVersionableLockOperationFactory;
import com.ibm.team.scm.client.SCMPlatform;
import com.ibm.team.scm.client.content.util.VersionedContentManagerByteArrayInputStreamPovider;
import com.ibm.team.scm.common.IBaselineHandle;
import com.ibm.team.scm.common.IChangeHistoryEntryChange;
import com.ibm.team.scm.common.IChangeSetHandle;
import com.ibm.team.scm.common.IComponentHandle;
import com.ibm.team.scm.common.IFolderHandle;
import com.ibm.team.scm.common.IScmService;
import com.ibm.team.scm.common.IVersionable;
import com.ibm.team.scm.common.IVersionableHandle;
import com.ibm.team.scm.common.IWorkspaceHandle;
import com.ibm.team.scm.common.WorkspaceComparisonFlags;
import com.ibm.team.scm.common.dto.IChangeHistorySyncReport;
import com.ibm.team.scm.common.internal.dto.ICommitParameter;
import com.ibm.team.scm.common.links.ChangeSetLinks;
import com.ibm.team.scm.common.providers.ProviderFactory;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import jtcb.util.Pair;
import org.apache.commons.logging.Log;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * This the RTC File System as it is view and used by the Java part of the 
 * Type Cobol Builder
 * @author MAYANJE
 */
public class RTCFileSystem {
    /**
     * The RTC context.
     */
    RTCContext m_context;
    /**
     * Constructor
     * @param context The RTC context.
     */
    public RTCFileSystem(RTCContext context)
    {
        m_context = context;
    }
    
    /**
     * Get the RTC Context
     * @return RTC Context
     */
    public RTCContext getContext()
    {
        return m_context;
    }
        
    /**
     * Get a file content 
     * @param fileName The file name to get the content
     * @return the file content
     * @throws TeamRepositoryException
     * @throws IOException 
     */
    public String readFileContent(String fileName)
        throws TeamRepositoryException, IOException {        
        return readFileContent(m_context.getWorkspaceStream(), 
                m_context.getComponent(), m_context.getTeamRepository(), 
                fileName,
                m_context.getProgressMonitor(), m_context.getLogger());
    }
    
    /**
     * Get handles corresponding to the given file
     * @param fileName The file name to look for handle
     * @return file handles: Folder handle containing the file, IVersionableHandle of the file
     * @throws TeamRepositoryException
     * @throws IOException 
     */    
    public Pair<IFolderHandle, IVersionableHandle> getFileHandles(String fileName) throws TeamRepositoryException, IOException
    {
        return getFileHandles(
            m_context.getWorkspaceStream(),
            m_context.getComponent(), m_context.getTeamRepository(), 
            fileName,
            m_context.getProgressMonitor(), m_context.getLogger());        
    }
    
    /**
     * Output the content of a file to the given Output Stream.
     * @param fileName The file name ro output
     * @param outputStream the Output stream
     * @throws TeamRepositoryException
     * @throws IOException 
     */
    public void ouputFileContent(
        String fileName, 
        OutputStream outputStream)
        throws TeamRepositoryException, IOException {
        ouputFileContent(
            m_context.getWorkspaceStream(),
            m_context.getComponent(), m_context.getTeamRepository(), 
            fileName,
            outputStream,
            m_context.getProgressMonitor(), m_context.getLogger());        
    }
    
    /**
     * Get the Active Change Set of the current component.
     * @return a List of IchangeSetHandle if aany, null otherwise.
     */
    public List<IChangeSetHandle> getComponentActiveChangeSet() throws TeamRepositoryException
    {
        IWorkspaceConnection workspaceConnection = 
                SCMPlatform.getWorkspaceManager(m_context.getTeamRepository())
                        .getWorkspaceConnection(m_context.getWorkspaceStream(), 
                                m_context.getProgressMonitor());
        
         List<IChangeSetHandle> listChangeSets =  workspaceConnection.activeChangeSets(m_context.getComponent()); 
         return listChangeSets;
    }

    /**
     * Get the Active Change Set of the current component.
     * @return a List of IchangeSetHandle if aany, null otherwise.
     */
    public List<IChangeSetHandle> getSuspendedChangeSet() throws TeamRepositoryException
    {
        IWorkspaceConnection workspaceConnection = 
                SCMPlatform.getWorkspaceManager(m_context.getTeamRepository())
                        .getWorkspaceConnection(m_context.getWorkspaceStream(), 
                                m_context.getProgressMonitor());
        
         List<IChangeSetHandle> listChangeSets =  workspaceConnection.suspendedChangeSets(m_context.getComponent()); 
         return listChangeSets;
    }
    
    /**
     * Get the first WorkItem handle associate dto a ChangeSet handle if any.
     * @param changeSetHandle The Change Set handle to get the assoicated Work Item handle.
     * @return The associated Work Item handle if any, null otherwise.
     * @throws TeamRepositoryException 
     */
    public IWorkItemHandle getChangeSetWorkItem(IChangeSetHandle changeSetHandle) throws TeamRepositoryException
    {
        return getChangeSetWorkItem(m_context.getTeamRepository(), changeSetHandle, m_context.getProgressMonitor());
    }
    
    /**
     * This method creates or update a file in a workspace and the target component in the target root forlder.
     * @param rootFolder the target root folder
     * @param versionable The version of the file, should be set to null if this is a new file
     * @param filename the file name
     * @param content the content to update or create.
     * @param encoding the encoding to use can be: IFileContent.ENCODING_US_ASCII = "us-ascii", ENCODING_UTF_8 = "UTF-8"; IFileContent.ENCODING_UTF_16BE = "UTF-16BE"; IFileContent.ENCODING_UTF_16LE = "UTF-16LE";
     * @param comment The Comment to associate to a new ChangeSet
     * @param workItemHandle any WorkItem to associated to ChangeSet, null otherwise.
     * @param closeChangeSet true if the active Change set must be closed so that it will no longer be the active one, false to leave it the active one.
     * @throws TeamRepositoryException 
     */
    public void CreateOrUpdateFile(
            IWorkspaceConnection streamConnection, 
            IFolderHandle rootFolder,
            IVersionable versionable, 
            String filename,  
            String content, String encoding,
            String comment, 
            IWorkItemHandle workItemHandle, 
            boolean closeChangeSet) throws TeamRepositoryException, UnsupportedEncodingException  {
        
        IWorkspaceConnection workspaceConnection = 
                SCMPlatform.getWorkspaceManager(m_context.getTeamRepository())
                        .getWorkspaceConnection(m_context.getWorkspaceStream(), m_context.getProgressMonitor());
        
        CreateOrUpdateFile(
            m_context.getTeamRepository(),
            streamConnection,
            m_context.getWorkspaceStream(),
            workspaceConnection,
            m_context.getComponent(),
            rootFolder,
            versionable, 
            filename, 
            content, encoding,
            comment, 
            workItemHandle, 
            closeChangeSet, m_context.getProgressMonitor());
    }

    /**
     * Get the last ChangeSet for the given IVersionableHandle object
     * @param versionableHandle The Versionable instance to get the last ChangeSet.
     * @return The last IChangeSetHandle instance if any, null otherwise.
     */
    public IChangeSetHandle getLastChangeSet(IVersionableHandle versionableHandle) throws TeamRepositoryException
    {    
        return getLastChangeSet(m_context.getTeamRepository(), m_context.getWorkspaceStream(), 
                m_context.getComponent(), versionableHandle, m_context.getProgressMonitor());
    }
    
    /**
     * Get the last ChangeSet for the given IVersionableHandle object
     * @param teamRepository The Source Team Repository
     * @param workspaceStreamHandle The Source workspace
     * @param componentHandle The source component
     * @param versionableHandle The Versionable instance to get the last ChangeSet.
     * @param monitor The Progress monitor to use.
     * @return the IChangeSetHandle instance if any, null otherwise.
     * @throws TeamRepositoryException 
     */
    public static IChangeSetHandle getLastChangeSet(ITeamRepository teamRepository,
        IWorkspaceHandle workspaceStreamHandle,
        IComponentHandle componentHandle, 
        IVersionableHandle versionableHandle,
        IProgressMonitor monitor) throws TeamRepositoryException
    {
        IWorkspaceConnection workspaceConnection = 
                SCMPlatform.getWorkspaceManager(teamRepository).getWorkspaceConnection(workspaceStreamHandle, monitor);
        
        IChangeHistory changeHistory = workspaceConnection.changeHistory(componentHandle);
        List changes = changeHistory.getHistoryFor(versionableHandle, 1, monitor);
        if (!changes.isEmpty())
        {
            IChangeHistoryEntryChange ichec = (IChangeHistoryEntryChange)changes.get(0);
            return ichec.changeSet();
        }        
        return null;
    }
    
    /**
     * Get handles corresponding to the given file
     * @param workspaceStreamHandle the source Workspace Stream handle
     * @param componentHandle The source component handle
     * @param teamRepository the source Team Repository
     * @param fileName The file name to look for handle
     * @param monitor The progression monitor
     * @param logger The logger
     * @return file handles: Folder handle containing the file, IVersionableHandle of the file
     * @throws TeamRepositoryException
     * @throws IOException 
     */
    public static Pair<IFolderHandle, IVersionableHandle> getFileHandles(
        IWorkspaceHandle workspaceStreamHandle,
        IComponentHandle componentHandle, ITeamRepository teamRepository,
        String fileName, IProgressMonitor monitor, Logger logger)
        throws TeamRepositoryException, IOException {
        
        IWorkspaceConnection workspaceConnection = 
                SCMPlatform.getWorkspaceManager(teamRepository).getWorkspaceConnection(workspaceStreamHandle, monitor);

        // Find file handle
        IConfiguration iconfig = (IConfiguration) workspaceConnection.configuration(componentHandle);
        IFolderHandle root = iconfig.rootFolderHandle(monitor);
        Pair<IFolderHandle, IVersionableHandle> fileHandles = findFile(root, iconfig, fileName, monitor, logger);
        return fileHandles;
    }
    
    /**
     * Finding the file.
     * The following method searches recursively in a given component and start folder for a filename and 
     * returns it in a form of IVersionableHandle.
     * @param root
     * @param iconfig
     * @param fileName
     * @param monitor
     * @param logger
     * @return The file version handle
     * @throws TeamRepositoryException 
     */
    public static Pair<IFolderHandle, IVersionableHandle> findFile(IFolderHandle root,
        IConfiguration iconfig, String fileName, IProgressMonitor monitor, Logger logger) throws TeamRepositoryException {

        String fileNamePath[] = { fileName };
        IVersionableHandle filePathHandle = null;

        // Check if file at this folder level
        filePathHandle = iconfig.resolvePath(root, fileNamePath, monitor);
        if (filePathHandle != null) {
            return new Pair<IFolderHandle, IVersionableHandle>(root, filePathHandle);
        }

        logger.info("Searching for file " + fileName);
        // Check this file sub folders
        @SuppressWarnings("unchecked")
        Map<String, IVersionableHandle> childEntries = iconfig.childEntries(root, monitor);
        for (Map.Entry<String, IVersionableHandle> next : childEntries.entrySet()) {
            IVersionableHandle nextVersionable = next.getValue();
            if (nextVersionable instanceof IFolderHandle) {
                Pair<IFolderHandle, IVersionableHandle> fileHandles = findFile((IFolderHandle) nextVersionable,
                iconfig, fileName, monitor, logger);
                filePathHandle = fileHandles.snd;
                if (filePathHandle != null) {
                    logger.info("Found file " + fileName);
                    return fileHandles;
                }
            }
        }
        return new Pair<IFolderHandle, IVersionableHandle>(root, filePathHandle);
    }
    
    /**
     * Using the method findFile, from the returned IVersionablehandle we retrieve the full IFileItem and make use of the content Manager to retrieve its Content. 
     * There are different ways to access the content of a file in the repository we chose the way of using a ByteArrayOutputStream 
     * because it offers me the simple toString() method returning me the filecontent as a String.
     * 
     * @param streamHandle
     * @param componentHandle
     * @param repo
     * @param fileName
     * @param monitor
     * @param logger
     * @return The file content
     * @throws TeamRepositoryException
     * @throws IOException 
     */
    public static String readFileContent(IWorkspaceHandle streamHandle,
        IComponentHandle componentHandle, ITeamRepository repo,
        String fileName, IProgressMonitor monitor, Logger logger)
        throws TeamRepositoryException, IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ouputFileContent(streamHandle, componentHandle, repo,
            fileName, outputStream,
            monitor, logger);
               
        String readContent = null;
        readContent = outputStream.toString("UTF-8");
        return readContent;
    }    

    /**
     * Using the method findFile, from the returned IVersionablehandle we retrieve the full IFileItem and make use of the content Manager to retrieve its Content. 
     * The content is ouput in the given OutputStream object.
     * 
     * @param streamHandle The source Stream handle
     * @param componentHandle The Source component handle
     * @param repo the source Team Repository
     * @param fileName the file name
     * @param outputStream The output stream
     * @param monitor The Progress monitor
     * @param logger The logger
     * @throws TeamRepositoryException
     * @throws IOException 
     */
    public static void ouputFileContent(IWorkspaceHandle streamHandle,
        IComponentHandle componentHandle, ITeamRepository repo,
        String fileName, 
        OutputStream outputStream,
        IProgressMonitor monitor, Logger logger)
        throws TeamRepositoryException, IOException {

        IFileItem fileItem = null;
        IFileContentManager contentManager = FileSystemCore.getContentManager(repo);

        IWorkspaceConnection workspaceConnection = 
                SCMPlatform.getWorkspaceManager(repo).getWorkspaceConnection(streamHandle, monitor);

        // Find file handle
        IConfiguration iconfig = (IConfiguration) workspaceConnection.configuration(componentHandle);
        IFolderHandle root = iconfig.rootFolderHandle(monitor);
        Pair<IFolderHandle, IVersionableHandle> fileHandles = findFile(root, iconfig, fileName, monitor, logger);
        IVersionableHandle filePathHandle = fileHandles.snd;

        // Check if file found
        if (filePathHandle == null) {
            throw new FileNotFoundException("File not found.");
        } else {
            logger.info("Found file: " + fileName);
        }

        // Fetch file complete item
        IVersionable filePath = (IVersionable) iconfig.fetchCompleteItem(
        filePathHandle, monitor);
        if (filePath.hasFullState()) {
            fileItem = (IFileItem) filePath.getFullState();
        } else {
            throw new TeamRepositoryException("Could not find file item");
        }
        // Get file content
        IFileContent content = fileItem.getContent();        
        contentManager.retrieveContent(fileItem, content, outputStream, monitor);
    }    

    /**
     * Output the content of the given fileHandles in the given OutputStream object.
     * 
     * @param streamHandle The source Stream handle
     * @param componentHandle The Source component handle
     * @param repo the source Team Repository
     * @param fileHandles the file name
     * @param outputStream The output stream
     * @param monitor The Progress monitor
     * @param logger The logger
     * @throws TeamRepositoryException
     * @throws IOException 
     */    
    public static void ouputFileContent(IWorkspaceHandle streamHandle,
        IComponentHandle componentHandle, ITeamRepository repo,
        Pair<IFolderHandle, IVersionableHandle> fileHandles, 
        OutputStream outputStream,
        IProgressMonitor monitor, Logger logger)
        throws TeamRepositoryException, IOException {

        IFileItem fileItem = null;
        IFileContentManager contentManager = FileSystemCore.getContentManager(repo);

        IWorkspaceConnection workspaceConnection = 
                SCMPlatform.getWorkspaceManager(repo).getWorkspaceConnection(streamHandle, monitor);

        // Find file handle
        IConfiguration iconfig = (IConfiguration) workspaceConnection.configuration(componentHandle);
        IVersionableHandle filePathHandle = fileHandles.snd;

        // Fetch file complete item
        IVersionable filePath = (IVersionable) iconfig.fetchCompleteItem(
        filePathHandle, monitor);
        if (filePath.hasFullState()) {
            fileItem = (IFileItem) filePath.getFullState();
        } else {
            throw new TeamRepositoryException("Could not find file item");
        }
        // Get file content
        IFileContent content = fileItem.getContent();        
        contentManager.retrieveContent(fileItem, content, outputStream, monitor);
    }    
    
    /**
     * Get the WorkItem associated to a ChangeSet
     * @param teamRepository The Team repository Handle
     * @param changeSetHandle The change set to lookfor the associated WorkItem
     * @param monitor The pRogress monitor to use.
     * @return The first associated WorkItem handle if any, null otherwise.
     * @throws com.ibm.team.repository.common.TeamRepositoryException
     */
    public static IWorkItemHandle getChangeSetWorkItem(ITeamRepository teamRepository, IChangeSetHandle changeSetHandle, IProgressMonitor monitor) throws TeamRepositoryException
    {
        List<ILink> links;
        links = ChangeSetLinks.findLinks((ProviderFactory) teamRepository.getClientLibrary(ProviderFactory.class), changeSetHandle, 
                new String[] { ILinkConstants.CHANGESET_WORKITEM_LINKTYPE_ID }, monitor); 

        for (final ILink link : links) { 
            final Object resolved = link.getTargetRef().resolve(); 
            if (resolved instanceof IWorkItemHandle) { 
                // do something with the linked work item 
                return (IWorkItemHandle)resolved;
            }        
        }    
        return null;
    }
    
    /**
     * Determines if the given GhangeSet handle is associated to the Given WorkItem Handle.
     * @param teamRepository
     * @param changeSetHandle
     * @param workItem
     * @param monitor
     * @return
     * @throws TeamRepositoryException 
     */
    public static boolean isChangeSetAssociatedToWorkItem(ITeamRepository teamRepository, 
            IChangeSetHandle changeSetHandle, IWorkItemHandle workItem,
            IProgressMonitor monitor) throws TeamRepositoryException {
        if (workItem == null)
            return false;
        List<ILink> links;
        links = ChangeSetLinks.findLinks((ProviderFactory) teamRepository.getClientLibrary(ProviderFactory.class), changeSetHandle, 
                new String[] { ILinkConstants.CHANGESET_WORKITEM_LINKTYPE_ID }, monitor); 

        for (final ILink link : links) { 
            final Object resolved = link.getTargetRef().resolve(); 
            if (resolved instanceof IWorkItemHandle) { 
                // do something with the linked work item 
                IWorkItemHandle iwh = (IWorkItemHandle)resolved;
                if (iwh.getItemId() == workItem.getItemId())
                    return true;
            }        
        }    
        return false;
    }

    /**
     * Associate a ChangeSet with a WorkItem handle
     * @param teamRepository The Team Repository
     * @param changeSetHandle The Change set handle to associate with a Work Item
     * @param workItemHandlde Work Item Handle to asociate with.
     * @param monitor The Progress Monitor
     * @throws TeamRepositoryException 
     */
    public static void associateChangeSetWithWorkItem(
            ITeamRepository teamRepository, 
            IWorkspaceHandle workspaceHandle,
            IChangeSetHandle changeSetHandle, IWorkItemHandle workItemHandlde,
            IProgressMonitor monitor) throws TeamRepositoryException {
        
/*      // Thi way of doing does not work.
        //WorkItemWorkingCopy                
        IWorkItemWorkingCopyManager workingCopyManager = ((IWorkItemClient) teamRepository.getClientLibrary(IWorkItemClient.class)).getWorkItemWorkingCopyManager();
        workingCopyManager.connect(workItemHandlde, IWorkItem.FULL_PROFILE, monitor);
        try
        {
            WorkItemWorkingCopy  workItemCopy = workingCopyManager.getWorkingCopy(workItemHandlde);
            IItemType iit = changeSetHandle.getItemType(); 
            IItemHandle iih = iit.createItemHandle(changeSetHandle.getItemId(), changeSetHandle.getStateId()); 
            IItemReference reference = IReferenceFactory.INSTANCE.createReferenceToItem(iih); 
            IEndPointDescriptor endpoint = LinkTypeRegistry.INSTANCE.getLinkType(WorkItemLinkTypes.CHANGE_SET).getTargetEndPointDescriptor(); 
            workItemCopy.getReferences().add(endpoint, reference);             
        }
        finally
        {
            workingCopyManager.disconnect(workItemHandlde);
        }
*/
        IFileSystemWorkItemManager fswim = (IFileSystemWorkItemManager)teamRepository.getClientLibrary(IFileSystemWorkItemManager.class);
        fswim.createLink(workspaceHandle, changeSetHandle, new IWorkItemHandle[]{workItemHandlde}, monitor);
        //fswim.deliverAndResolve(null, null, null, null, null, null, true, null, true, null, null, null, null, monitor)
        
    }
    /**
     * This method creates or update a file in a workspace and the target component in the target root forlder.
     * @param teamRepository the Teal Repository object
     * @param streamConnection The target stream connection to which the deliver is performed if null, the delivery will be performed on
     * the workspace connection and they will appers in the outgoing of the epnding change.
     * @param workspaceHandle the target repository workspace
     * @param workspace the target repository workspace connection
     * @param component the target component
     * @param rootFolder the target root folder
     * @param versionable The version of the file, should be set to null if this is a new file
     * @param filename the file name
     * @param content the content to update or create.
     * @param encoding the encoding to use can be: IFileContent.ENCODING_US_ASCII = "us-ascii", ENCODING_UTF_8 = "UTF-8"; IFileContent.ENCODING_UTF_16BE = "UTF-16BE"; IFileContent.ENCODING_UTF_16LE = "UTF-16LE";
     * @param comment The Comment to associate to a new ChangeSet
     * @param workItemHandle any WorkItem to associated to ChangeSet, null otherwise.
     * @param closeChangeSet true if the active Change set must be closed so that it will no longer be the active one, false to leave it the active one.
     * @param monitor The progress monitor to use
     * @throws TeamRepositoryException 
     * @throws java.io.UnsupportedEncodingException 
     */
    public static void CreateOrUpdateFile(ITeamRepository teamRepository, 
            IWorkspaceConnection streamConnection, 
            IWorkspaceHandle workspaceHandle,
            IWorkspaceConnection workspace, 
            IComponentHandle component, 
            IFolderHandle rootFolder,
            IVersionable versionable, 
            String filename,  
            String content, String encoding,
            String comment, 
            IWorkItemHandle workItemHandle, 
            boolean closeChangeSet,
            IProgressMonitor monitor) throws TeamRepositoryException, UnsupportedEncodingException 
    {
        if (encoding == null)
            throw new UnsupportedEncodingException("encoding parameter is null");
        if (!(encoding.equals(IFileContent.ENCODING_US_ASCII) ||
                encoding.equals(IFileContent.ENCODING_UTF_8) ||
                encoding.equals(IFileContent.ENCODING_UTF_16LE) ||
                encoding.equals(IFileContent.ENCODING_UTF_16BE)
                ))
            throw new UnsupportedEncodingException("invalid encoding encoding : " + encoding);
        // Get the currently active change set if it exists or create a new one 
         List<IChangeSetHandle> listChangeSets =  workspace.activeChangeSets(component); 
         IChangeSetHandle changeSetHandle = null; 
         if (listChangeSets != null) { 
           if (listChangeSets.size() > 0) { 
            Object[] changeSetObjects =  listChangeSets.toArray(); 
            changeSetHandle = (IChangeSetHandle) changeSetObjects[0]; 
           } 
         } 
         if (changeSetHandle == null) {
             //Create a new ChangeSet as the new active one.
            changeSetHandle = workspace.createChangeSet(component, comment, true, monitor);             
            if (workItemHandle != null)
            { //Associate the chaneg set to the WorkItem if any
                associateChangeSetWithWorkItem(teamRepository, workspaceHandle, changeSetHandle, workItemHandle, monitor);
            }
         }
         else if (workItemHandle != null) {
            //Tries to see if the ChangeSet is already associated to the change set.
            if (!isChangeSetAssociatedToWorkItem(teamRepository, 
                        changeSetHandle, workItemHandle,
                        monitor)) {
                associateChangeSetWithWorkItem(teamRepository, workspaceHandle, changeSetHandle, workItemHandle, monitor);
            }
         }

        // find or create a file 
         IFileItem file = null; 
         if (versionable instanceof FileItem) { 
           FileItem rtcitem = (FileItem) versionable; 
           file = (IFileItem) rtcitem.getWorkingCopy();    
         } else { 
           file = (IFileItem) IFileItem.ITEM_TYPE.createItem(); 
           file.setName(filename); file.setParent(rootFolder); 
         } 

         // update the file content 
         IFileContentManager contentManager = FileSystemCore.getContentManager(teamRepository); 
         IFileContent storedContent = contentManager.storeContent( 
            encoding, 
            FileLineDelimiter.LINE_DELIMITER_PLATFORM, 
              new VersionedContentManagerByteArrayInputStreamPovider(content.getBytes(encoding)), 
             null, monitor); 

         file.setContentType(IFileItem.CONTENT_TYPE_TEXT);
         file.setContent(storedContent);
         file.setFileTimestamp(new Date());
         
         // commit the change to workspace
         workspace.commit(changeSetHandle, Collections.singletonList(workspace.configurationOpFactory().save(file)), monitor);
         
         // deliver the changes to the stream
         IChangeHistorySyncReport sync = 
         workspace.compareTo(streamConnection == null ? workspace : streamConnection, WorkspaceComparisonFlags.CHANGE_SET_COMPARISON_ONLY, Collections.EMPTY_LIST, monitor); 
                  
         workspace.deliver(streamConnection == null ? workspace : streamConnection, sync, Collections.EMPTY_LIST, sync.outgoingChangeSets(component), monitor); 
        if (closeChangeSet && streamConnection == null)
            workspace.closeChangeSets(Collections.singletonList(changeSetHandle), monitor);                  
    }    
       
    /**
     * Update the content of a file
     * @param fileItem The file item to update the content.
     * @param repo the Team repository
     * @param newContent the new content of the update
     * @param monitor the Progress monitor
     * @param logger the logger
     * @return The working copy File Item
     * @throws TeamRepositoryException 
     */
    public static IFileItem uploadContentToFile(IFileItem fileItem,
        ITeamRepository repo, String newContent, IProgressMonitor monitor, Logger logger) throws TeamRepositoryException {

        //It is important to retrieve a working copy of the fileitem to which the content is linked.
        IFileItem fileWorkingCopy = (IFileItem) fileItem.getWorkingCopy();

        IFileContentManager contentManager = FileSystemCore
        .getContentManager(repo);

        VersionedContentManagerByteArrayInputStreamPovider inputStream = new VersionedContentManagerByteArrayInputStreamPovider(
        newContent.getBytes());

        IFileContent uploadedcontent = null;
        uploadedcontent = contentManager.storeContent(
            IFileContent.ENCODING_UTF_8,
            FileLineDelimiter.LINE_DELIMITER_PLATFORM, inputStream, null,
            monitor);

        fileWorkingCopy.setContent(uploadedcontent);
        logger.info("Loaded new content into file.");
        return fileWorkingCopy;
    }    
    
    /**
     * Before creating the changeSet the repository file has to be locked.
     * @param repo
     * @param workspaceConnection
     * @param componentHandle
     * @param fileItem
     * @param monitor
     * @throws TeamRepositoryException 
     */
    public static void applyLock(ITeamRepository repo,
        IWorkspaceConnection workspaceConnection,
        IComponentHandle componentHandle, IFileItem fileItem,
        IProgressMonitor monitor) throws TeamRepositoryException {

        IWorkspaceManager wsManager = (IWorkspaceManager) repo.getClientLibrary(IWorkspaceManager.class);
        IVersionableLockOperationFactory lockFactory = wsManager.lockOperationFactory();
        ArrayList<IVersionableLockOperation> lockOperations = new ArrayList<IVersionableLockOperation>();
        lockOperations.add(lockFactory.acquire(fileItem, workspaceConnection, componentHandle));
        wsManager.applyLockOperations(lockOperations, monitor);
    }
    
    /**
     * This function remove a lock previously set.
     * @param repo
     * @param workspaceConnection
     * @param componentHandle
     * @param fileItem
     * @param monitor
     * @throws TeamRepositoryException 
     */
    public static void removeLock(ITeamRepository repo,
        IWorkspaceConnection workspaceConnection,
        IComponentHandle componentHandle, IFileItem fileItem,
        IProgressMonitor monitor) throws TeamRepositoryException {

        IWorkspaceManager wsManager = (IWorkspaceManager) repo.getClientLibrary(IWorkspaceManager.class);
        IVersionableLockOperationFactory lockFactory = wsManager.lockOperationFactory();
        ArrayList<IVersionableLockOperation> lockOperations = new ArrayList<IVersionableLockOperation>();
        lockOperations.add(lockFactory.release(fileItem, workspaceConnection,componentHandle, true));
        wsManager.applyLockOperations(lockOperations, monitor);
    }
    
    /**
     * Upload the content of a file item
     * @param fileItem the fikle item to upload
     * @param streamHandle
     * @param componentHandle
     * @param repo the source repository
     * @param newContent the new content
     * @param changeSetComment
     * @param monitor
     * @return The working copy File Item
     * @throws TeamRepositoryException 
     * @deprecated This Method is not tested.
     */
    public static IFileItem uploadFile(ITeamRepository repo,
        IWorkspaceHandle streamHandle,
        IComponentHandle componentHandle, IFileItem fileItem, 
        String newContent, String changeSetComment,
            IProgressMonitor monitor, Logger logger) 
            throws TeamRepositoryException {    

        IWorkspaceConnection workspaceConnection = 
                SCMPlatform.getWorkspaceManager(repo).getWorkspaceConnection(streamHandle, monitor);

        //I return the working copy to put it in a changeset. 
        IFileItem fileWorkingCopy = uploadContentToFile(fileItem, repo, newContent, monitor, logger);        
                
        //To create a changeset directly on a stream we need special parameters to set the commit mode and the operations to be carried out:
        // Set up parameter for changeset
        ICommitParameter param = ICommitParameter.FACTORY.create();
        param.addItemToSave(fileWorkingCopy);
        
        //Before creating the changeSet the repository file has to be locked.
        applyLock(repo, workspaceConnection, componentHandle, fileItem, monitor) ;
        
        //Now that the file is locked I create a changeset directly on a stream and provide the commit parameters to it:
        // Create Changeset
        IScmService scmService = (IScmService) ((IClientLibraryContext) repo)
        .getServiceInterface(IScmService.class);
        IChangeSetHandle csHandle = null;
 
        try {
            csHandle = scmService.createChangeSetForStream(streamHandle,
                componentHandle, changeSetComment, param,
                IScmService.DELTA_PER_INVOCATION, /*repoMonitor*/ null); 
        } catch (TeamRepositoryException e) {
            e.printStackTrace();
        }
        // After creating the changeset I release the lock and deliver the changeset, 
        removeLock(repo, workspaceConnection, componentHandle, fileItem, monitor);
        
        //prodiving null as the originating workspace: Deliver changeset
        IChangeSetHandle[] csHandles = new IChangeSetHandle[1];
        csHandles[0] = csHandle;
        scmService.deliverCombined(null, null, streamHandle,
            new IBaselineHandle[0], csHandles, null, null, null, /*repoMonitor*/ null);        
        return fileWorkingCopy;
    }    
}
