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

class SENum implements Sxpr
{
    public int n;
    public String pp(){return ""+n;}
    public SENum(int n){this.n=n;}    
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
       
       Sxpr groupa = new SConst(new SENum(8),new SConst(new SENum(3),new SConst(new SENum(8), new SEmpty())));
       Sxpr groupb = new SConst(new SStr("+"), groupa);
       Sxpr groupc = new SConst(new SStr("a"), new SConst(new SStr("b"), new SConst(new SStr("c"),new SEmpty())));
       Sxpr groupd = new SConst(new SStr("+"), new SConst(new SENum(1),new SConst(new SEmpty(),new SConst(new SStr("+"), new SConst(new SENum(2), new SConst(new SENum(3), new SEmpty()))))));
       
       
       
       System.out.println("SExpr groupa outputs: "+groupa.pp()+" should be 838 ");
       System.out.println("SExpr groupb outputs: "+groupb.pp()+" should be (+838)");
       System.out.println("SExpr groupc outputs: "+groupc.pp()+" should be (abc)");
       System.out.println("SExpr groupd outputs: "+groupd.pp()+" should be (+1(+23))");
    }
    
}