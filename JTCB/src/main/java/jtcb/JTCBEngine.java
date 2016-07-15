/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jtcb;

import com.ibm.team.filesystem.common.IFileContent;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.scm.client.IConfiguration;
import com.ibm.team.scm.client.IWorkspaceConnection;
import com.ibm.team.scm.common.IChangeSetHandle;
import com.ibm.team.scm.common.IComponent;
import com.ibm.team.scm.common.IComponentHandle;
import com.ibm.team.scm.common.IFolderHandle;
import com.ibm.team.scm.common.IVersionable;
import com.ibm.team.scm.common.IVersionableHandle;
import com.ibm.team.scm.common.IWorkspaceHandle;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import jtcb.rtc.RTCContext;
import jtcb.rtc.RTCFileSystem;
import jtcb.rtc.RTCSession;
import jtcb.util.Pair;

/**
 * This is the Java Type Cobol Builder Engine implementation. With native access
 * to the .Net corresponding functionalities.
 * 
 * The corresponding JNI/C++ header must be generated using the following command:
 * 
 * Thi generates a jtcb_JTCBEngine.h file
 * @author MAYANJE
 */
public class JTCBEngine {
    private String m_targetStream;
    private String m_sourceStream;
    private String m_component;
    private String m_inputFile;
    private String m_outputFile;
    
    /**
     * The RTC session
     */
    private final RTCSession m_session;
    /**
     * The RTC context corresponding to settings
     */
    private final RTCContext m_context;    
    
    /**
     * Constructor
     * @param session The RTC session
     * @param context The RTC context.
     */
    public JTCBEngine(RTCSession session, RTCContext context)
    {    
        m_session = session;
        m_context = context;
        /*
        Allocate the corresponding native C++ Object.
        */
        allocateNativeObject();        
    }

    /**
     * Get the current RTC sessiont
     * @return the RTC context.
     */
    public RTCSession getSession()
    {
        return m_session;
    }
    
    /**
     * Get the current RTC contect
     * @return the RTC context.
     */
    public RTCContext getContext()
    {
        return m_context;
    }
    /**
     * Get the target workspace repository or stream
     * @return the target workspace repository or stream.
     */
    public String getTargetStream()
    {
        return m_targetStream;
    }
    
    /**
     * Set the target workspace repository or stream
     * @param targetStream the target workspace repository or stream.
     */
    public void setTargetStream(String targetStream)
    {
        m_targetStream = targetStream;
    }    
    
    /**
     * Get the target workspace repository or stream
     * @return the source workspace repository or stream.
     */
    public String getSourceStream()
    {
        return m_sourceStream;
    }
    
    /**
     * Set the source workspace repository or stream
     * @param sourceStream the source workspace repository or stream.
     */
    public void setSourceStream(String sourceStream)
    {
        m_sourceStream = sourceStream;
    }
    
    /**
     * Get the sibling component
     * @return the compoent.
     */
    public String getComponent()
    {
        return m_component;
    }
    
    /**
     * Set the sibling component.
     * @param component the component 
     */
    public void setComponent(String component)
    {
        m_component = component;
    }

    /**
     * Get the current input file
     * @return the input file
     */
    public String getInputFile()
    {
        return m_inputFile;
    }
    
    /**
     * Set the current input file
     * @param inputFile the input file
     */
    public void setInputFile(String inputFile)
    {
        m_inputFile = inputFile;
    }
    
    /**
     * Get the current output file
     * @return the input file
     */
    public String getOutputFile()
    {
        return m_outputFile;
    }
    
    /**
     * Set the current output file
     * @param outputFile the input file
     */
    public void setOutputFile(String outputFile)
    {
        m_outputFile = outputFile;
    }    
    
    /**
     * Native compilation of a file    
     * @param inputFile the input fiel to compile
     * @param outputFile the ouput file
     * @return true if compilation is successful, fals eotherwise
     */
    public native boolean compileFile(String inputFile, String outputFile);
    
    /**
     * Resolve a copy
     * @param copyName The Fle opy name
     * @param outputDir The output directory
     * @return true if the copy has been resolved, fals eotherwise.
     */
    public boolean resolveCopy(String copyName, String outputDir)
    {
        try
        {
            File fin = new File(copyName);
            String name = fin.getName();
            //Check if we have an extension
            int idot = name.lastIndexOf('.');
            //All file names to check
            String[] names = null;
            if(idot < 0)
            {            
                names = new String[]{name, name + ".cbl", name + ".tcbl"};
            }
            else
            {
                names = new String[]{name};
            }
            
            RTCFileSystem fs = new RTCFileSystem(m_context);            
            Pair<IFolderHandle, IVersionableHandle> handles = null;
            IWorkspaceHandle s_wh = null;
            Pair<IComponentHandle,IComponent> c_ch = null;
            String final_name = null;//The final COPY name found
            found: for(Pair<String,String> sc : m_context.getStreamAndComponentIdentification())
            {                
                String s = sc.fst;
                String c = sc.snd;
                s_wh = RTCContext.findWorkspaceOrStream(m_context.getTeamRepository(), s, m_context.getProgressMonitor());
                c_ch = RTCContext.findWorkspaceStreamComponent(m_context.getTeamRepository(), s_wh, c, m_context.getProgressMonitor());
                for (String cur_name : names)
                {
                    try
                    {
                        handles = RTCFileSystem.getFileHandles(s_wh, 
                                c_ch.fst, 
                                m_context.getTeamRepository(), 
                                cur_name, 
                                m_context.getProgressMonitor(), 
                                m_context.getLogger());   
                        if (handles.fst != null && handles.snd != null)
                        {
                            final_name = cur_name;
                            break found;
                        }
                    }
                    catch(Exception e)
                    {              
                        m_session.getLogger().info(e.getMessage());
                    }
                }
            }
            //I have look in all (Stream,Component) and the copy is not found.
            if (handles == null)
            {
                m_session.getLogger().info("fail to resolve COPY : " + copyName);
                return false;
            }                
                    
            File fout = new File(outputDir, final_name);
            FileOutputStream fos = new FileOutputStream(fout);

            RTCFileSystem.ouputFileContent(
                    s_wh, 
                    c_ch.fst, 
                    m_context.getTeamRepository(), 
                    handles,
                    fos, 
                    m_context.getProgressMonitor(), 
                    m_context.getLogger()
            );
            fos.flush();
            fos.close();
            m_session.getLogger().info("Succeed to resolve COPY : " + copyName);
            System.out.println("Resolving COPY : " + copyName);
            return true;
        }
        catch(TeamRepositoryException | IOException tre)
        {
            m_session.getLogger().info("fail to resolve COPY : " + copyName);
            m_session.getLogger().severe(tre.getMessage());
        }                
        return false;
    }
    
    /**
     * Commit and deliver the output file.
     * @param inputFile The input file from the source Workspace stream.
     * @param outputFile The output file to commit and deliver
     * @return true if OK, false otherwise
     * @throws java.io.IOException
     * @throws com.ibm.team.repository.common.TeamRepositoryException
     */
    public boolean checkinAndDeliverOutputFile(String inputFile, String outputFile) throws IOException, TeamRepositoryException
    {
        RTCContext context = m_context;
        
        //1)Read the source file        
        String content = new String(Files.readAllBytes(Paths.get(outputFile)));
        
        //2) Create or Update a file DVZF0OSMTiers.TCBL
        RTCFileSystem fs = new RTCFileSystem(context);
        //Get the root folder handle and the verison of the source file
        Pair<IFolderHandle, IVersionableHandle> handles = fs.getFileHandles(new File(inputFile).getName());
        //Get the last change set of the source file.
        IChangeSetHandle changeSetHandle = fs.getLastChangeSet(handles.snd);        
        //Get any WorkItem associated with the last change set
        IWorkItemHandle workItemHandle = fs.getChangeSetWorkItem(changeSetHandle);
        //Try to see if the file that we want to add exists if so this is an update    
        String fileName = new File(outputFile).getName();
        Pair<IFolderHandle, IVersionableHandle> handles_bis = fs.getFileHandles(fileName);
        IVersionable versionable = null;
        if (handles_bis.snd != null)
        {
            //fetch a versionable from IConfiguration   
            IWorkspaceConnection workspaceConnection = context.getWorkspaceConnection();
            IConfiguration configuration = workspaceConnection.configuration(context.getComponent());
            versionable = configuration.fetchCompleteItem(handles_bis.snd, context.getProgressMonitor());
        }                        
        IWorkspaceConnection streamConnection = context.getDeliverWorkspaceConnection();                
        fs.CreateOrUpdateFile(streamConnection, handles.fst, versionable, fileName, content, IFileContent.ENCODING_UTF_8, "Test commit from JTCB no outgoing: " + new Date().toString(), workItemHandle, true);
        return true;
    }
    
    /**
     * Clear all logs
     */
    public synchronized static native void log_clear();
    /**
     * Log an information message
     * @param message The message
     */
    public synchronized static native void log_info(String message);
    /**
     * Log a warning message
     * @param message The message
     */    
    public synchronized static native void log_warning(String message);
    /**
     * Log a error message
     * @param message The message
     */        
    public synchronized static native void log_error(String message);
    
    /**
     * The finalize method to destroy the native object
     */
    @Override
    protected native void finalize();

    /**
     * Native method to allocate the target native Info object.
     */
    private native void allocateNativeObject();
        
    
    /* 
     * This a private long value which is a pointer to the native C++ object.
     * It is safe to do that because in Java the size in bit of long type is the double
     * than the size of an int type or a pointer type.
     * This field is private so that it cannot be inherited.
     */                
    private long pObject;    
}
