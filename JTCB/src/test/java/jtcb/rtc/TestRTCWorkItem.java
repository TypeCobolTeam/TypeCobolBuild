/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jtcb.rtc;

import java.io.File;
import static jtcb.rtc.TestRTCFileSystem.m_session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Set of test related to RTC WorkItem functionalities.
 * @author MAYANJE
 */
public class TestRTCWorkItem {
    
    public TestRTCWorkItem() {
        String dir = System.getProperty("user.dir");
        String dll = new File(dir).getParent() + "\\Debug\\TCB.dll";
        System.load(dll);            
        m_session = new RTCSession();                
    }
    
    @BeforeClass
    public static void setUpClass() {
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
    // @Test
    // public void hello() {}
}
