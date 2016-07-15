package jtcb.jni;

import java.io.File;
import jtcb.JTCBEngine;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author MAYANJE
 */
public class JNICallTest {
    
    public JNICallTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        String dir = System.getProperty("user.dir");
        String dll = new File(dir).getParent() + "\\Debug\\TCB.dll";
        System.load(dll);
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testCallCompileFile() 
    {
        String dir = System.getProperty("user.dir");
        JTCBEngine jtcbe = new JTCBEngine(null, null);
        boolean result = jtcbe.compileFile("toto", "tata");
        assertTrue(result);
        
    }
}
