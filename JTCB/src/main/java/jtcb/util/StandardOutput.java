/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jtcb.util;

import java.io.PrintStream;

/**
 *
 * @author MAYANJE
 */
public class StandardOutput extends PrintStream {

    public StandardOutput() {
        super(System.out);
        System.setOut(this);
        System.setErr(this);        
    }
    
    public void println(String x) {
        super.println(x);
    }    
}
