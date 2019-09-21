/*
Nicholas Sweeney COMP3010 Project
 */
package comp3010hlprogram;

//SExpression for use of data structures that are Strings, empty, or constants with more SExpressions
//Interface class contains pretty printer function
interface Sxpr
{
    public String pp();
}

class SStr implements Sxpr
{
    public String s;
    public String pp() {return s;}
    public SStr(String s){this.s = s;}
}

class SEmpty implements Sxpr
{
    public String pp(){return "▲▲▲";}
    public SEmpty( ){ }
}

class SConst implements Sxpr
{
    public Sxpr left, right;
    public String pp()
    {
        return "("+this.left.pp()+" "+this.right.pp()+")";
    }
    public SConst(Sxpr left, Sxpr right)
    {
        this.left = left;
        this.right = right;
    }
}

//J0 with data structures for Values, Addition, and Multiplication
//Interface class contains pretty-printer and big step interpreter functions
interface Joe 
{
    public String pp();
    public int interp();
    
}

class JNumber implements Joe 
{
    public int n;
    public String pp()
    {
        return Integer.toString(n);
    }
    public int interp()
    {
        return this.n;
    }
    JNumber(int n){this.n = n;}
}

class JPlus implements Joe
{
    public Joe left, right;
    public String pp()
    {
         return this.left.pp()+" + "+this.right.pp();
    }
    
    public int interp()
    {
        return this.left.interp()+this.right.interp();
    }
    
    JPlus(JNumber left, JNumber right)
    {
        this.left = left;
        this.right = right;
    }
}

class JMult implements Joe
{
    public Joe left,right;
    public String pp()
    {
        return this.left.pp()+" * "+this.right.pp();
    }
    
    public int interp()
    {
        return this.left.interp()*this.right.interp();
    }
    
    JMult(JNumber left, JNumber right)
    {
        this.left = left;
        this.right = right;
    }
    
}
public class COMP3010HLProgram {

    public static void main(String[] args) {
       JNumber test1 = new JNumber(5);
       JNumber test2 = new JNumber(10);
       JNumber test3 = new JNumber(7);
       JNumber t1 = new JNumber(1);
       JNumber t2 = new JNumber(2);
       JNumber t3 = new JNumber(3);
       JMult m1 = new JMult(test1, test2);
       JMult m2 = new JMult(test1, test3);
       JMult m3 = new JMult(t2, t3);
       JPlus a1 = new JPlus(test1, t1);
       JPlus a2 = new JPlus(test1, t2);
       JPlus a3 = new JPlus(test1, t3);
       System.out.println("The following numbers: "+test1.pp()+" "+test2.pp()+" "+test3.pp()+" "+t1.pp()+" "+t2.pp()+" "+t3.pp()+" should be "+test1.n+" "+test2.n+" "+test3.n+" "+t1.n+" "+t2.n+" "+t3.n);
       System.out.println(a1.pp()+" should be "+(a1.interp()));
       System.out.println(a2.pp()+" should be "+(a2.interp()));
       System.out.println(a3.pp()+" should be "+(a3.interp()));
       System.out.println(m1.pp()+" should be "+(m1.interp()));
       System.out.println(m2.pp()+" should be "+(m2.interp()));
       System.out.println(m3.pp()+" should be "+(m3.interp()));
       
    }
    
}