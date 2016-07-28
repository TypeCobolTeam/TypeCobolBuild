package jtcb;

import com.ibm.team.repository.common.TeamRepositoryException;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import jtcb.rtc.RTCContext;
import jtcb.rtc.RTCSession;
import jtcb.util.Pair;
import jtcb.util.StandardOutput;
import org.kohsuke.args4j.Argument; 
import org.kohsuke.args4j.CmdLineException; 
import org.kohsuke.args4j.CmdLineParser; 
import org.kohsuke.args4j.Option; 
import static org.kohsuke.args4j.OptionHandlerFilter.ALL;
  
/**
 * The Main entry class JTCB.
 * 
 * @author MAYANJE
 */
public class JTCB {
    @Option(name="-r", usage="The RTC repository")
    private String repositoryURI;
    
    @Option(name="-t", usage="The target Workspace area or stream")
    private String targetStream;
    
    @Option(name="-s", usage="The Source Workspace area or stream")
    private String sourceStream;

    @Option(name="-C", usage="The sibling component")
    private String component;

    @Option(name="-i", usage="The Type Cobol Input File")
    private String inputFile;

    @Option(name="-o", usage="The Cobol Output File")
    private String outputFile;
    
    /**
     * Option for specifying a component.
     * @param s 
     */
    @Option(name="-sc",usage="specifies a stream,component") 
    private void setStreamComponent(String s) { 
        String[] sc = s.split(","); 
        if (sc.length == 2)
        {
            streamComponents.add(new Pair<String,String>(sc[0], sc[1])); 
        }
    } 
    private List< Pair<String,String> > streamComponents = new ArrayList< Pair<String,String> >(); 
    
    // receives other command line parameters than options 
    @Argument 
    private List<String> arguments = new ArrayList<>();
        
    /**
     * Statically configure login parameters.
     */
    static
    {
        try {
            /*
            //Specify our target Logging engine ==> Jdk14Logger
            System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
            // The following creates two handlers
            System.setProperty("handlers","java.util.logging.ConsoleHandler, java.util.logging.FileHandler");
            // Set the default logging level for the root logger
            System.setProperty(".level", "SEVERE");
            // log level for the "com.example" package
            System.setProperty("sample.logging.level", "FINE");
            // Set the default logging level
            System.setProperty("java.util.logging.ConsoleHandler.level","ALL");
            System.setProperty("java.util.logging.FileHandler.level","FINE");
            // Set the default formatter
            System.setProperty("java.util.logging.ConsoleHandler.formatter", "java.util.logging.SimpleFormatter");
            System.setProperty("java.util.logging.FileHandler.formatter","java.util.logging.SimpleFormatter");
            // Specify the location and name of the log file
            String logFile = new java.io.File(System.getProperty("user.dir"), "JTCB.log").getPath();
            System.setProperty("java.util.logging.FileHandler.pattern", logFile);
            */
            // Specify the location and name of the log file
            String logFile = new java.io.File(System.getProperty("user.dir"), "JTCB.log").getPath();            
            Logger.getGlobal().addHandler(new FileHandler(logFile));
        } catch (IOException ex) {
            Logger.getLogger(JTCB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(JTCB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Load the TCB dll
     * @param subDir The subirectory relative to the "user.dir"
     */
    static void loadTCB(String subDir)
    {
        String[] all_dlls = {//All libraries to load ourselfs
            "TCB.dll",
        };
        String dir = new File(System.getProperty("user.dir"), subDir).getPath();
        System.out.println(dir);
        for (String all_dll : all_dlls) {
            String dll = new File(dir, all_dll).getPath();
            System.out.println(dll);
            System.load(dll);       
        }
    }
    static
    {   //We must load the Dll        
        //The table of all subdirs to look for the TCB dll.
        String[] sub_dirs =
        {
            "",
            "TCB",
            "TCB\\Debug",
            "TCB\\Debug_exe",
            "TCB\\Release",     
            "TCB\\Release_exe",     
            "TCB\\x64\\Debug",            
            "TCB\\x64\\Debug_exe",            
            "TCB\\x64\\Release",            
            "TCB\\x64\\Release_exe",            
            "..\\Debug",
            "..\\Debug_exe",
            "..\\Release",            
            "..\\Release_exe",            
            "..\\x64\\Debug",                        
            "..\\x64\\Debug_exe",                        
            "..\\x64\\Release",            
            "..\\x64\\Release_exe",            
            "..\\TCB\\Debug",
            "..\\TCB\\Debug_exe",
            "..\\TCB\\Release",     
            "..\\TCB\\Release_exe",     
            "..\\TCB\\x64\\Debug",            
            "..\\TCB\\x64\\Debug_exe",            
            "..\\TCB\\x64\\Release",            
            "..\\TCB\\x64\\Release_exe",                        
        };
        for (int i = 0; i < sub_dirs.length; i++)
        {
            try
            {
                loadTCB(sub_dirs[i]);
                break;
            }
            catch(UnsatisfiedLinkError ufe)
            {
                if (i == (sub_dirs.length - 1))
                    throw ufe;
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new StandardOutput();
        new JTCB().doMain(args);
    }
    
    public void doMain(String[] args) {                
        CmdLineParser parser = new CmdLineParser(this);
        
        // if you have a wider console, you could increase the value; 
         // here 80 is also the default 
         parser.setUsageWidth(80); 
 
         try { 
            // parse the arguments. 
            parser.parseArgument(args); 

            // you can parse additional arguments if you want. 
            // parser.parseArgument("more","args"); 

            // after parsing arguments, you should check 
            // if enough arguments are given. 
            if( !arguments.isEmpty() ) 
            {
                
            }
            run();
            System.exit(0);              
         } catch( CmdLineException e ) { 
             // if there's a problem in the command line, 
             // you'll get this exception. this will report 
             // an error message. 
             System.err.println(e.getMessage()); 
             System.err.println("JTCB [options...] arguments..."); 
             // print the list of available options 
             parser.printUsage(System.err); 
             System.err.println(); 
 
             // print option sample. This is useful some time 
             System.err.println("  Example: JTCB "+parser.printExample(ALL));  
             System.exit(1); 
         }         
    }    
    
    /**
     * Run the gneration
     */
    public void run()
    {
        try
        {
            // Allocate a session
            RTCSession session = new RTCSession(repositoryURI);
            // Allocate a RTC context
            RTCContext context = new RTCContext(session);
            //Set repository locations
            context.setDeliverStreamWorkspaceSource(targetStream, sourceStream, component);
            //Set the Stream,Component used to locate copies.
            context.setStreamAndComponentIdentification(streamComponents);
            //Allocate our JTCBEngine
            JTCBEngine jtcb_engine = new JTCBEngine(session, context);
            jtcb_engine.setTargetStream(targetStream);
            jtcb_engine.setSourceStream(sourceStream);
            jtcb_engine.setComponent(component);
            //Compile the file
            if (!jtcb_engine.compileFile(inputFile, outputFile))
            {
                session.getLogger().severe(MessageFormat.format("Failed to generate Cobol file {0} from TypeCocol file {1}!", outputFile, inputFile));
                System.exit(1);
            }
            else
            {
                //Now Checkin commit and deliver the outfile.
                jtcb_engine.checkinAndDeliverOutputFile(inputFile, outputFile);
                session.getLogger().info(MessageFormat.format("Succeed to generate Cobol file {0} from TypeCocol file {1}!", outputFile, inputFile));            
            }
        }
        catch(TeamRepositoryException | IOException e)
        {        
        }
    }
}
