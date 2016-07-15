package jtcb.util;

/**
 * A Pair is really a class which is missing in Java
 * @author MAYANJE
 */
public class Pair<F,S> {
    public F fst;
    public S snd;
    
    public Pair(F fst, S snd)
    {
        this.fst = fst;
        this.snd = snd;
    }
    
    @Override
    public int hashCode()
    {        
        return (fst != null ? fst.hashCode() : 0) ^ (snd != null ? snd.hashCode() : 0);
    }
    @Override
    public String toString()
    {
        return "(" + (fst == null  ? "" : fst.toString()) + "," + (snd == null ? "" : snd.toString()) + ")";
    }
}
