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
    public Boolean isEqual();
    public Joe interp();
    
}

class JEmpty implements Joe
{
    public String pp(){return"▲▲▲";}
    public JEmpty(){}
    public Boolean isEqual() {return true;}
    public Joe interp()
    {
        return this;
    }
}

class JConst implements Joe
{
    public Joe left, right;
    public String pp() { return "("+this.left.pp()+" "+this.right.pp()+")";}
    public JConst(Joe left, Joe right)
    {
        this.left = left;
        this.right = right;
    }
    public Boolean isEqual(){ return false;}
    public Joe interp()
    {
        return new JConst(this.left.interp(),this.right.interp());
    }
}

class JPrim implements Joe
{
    public String s;
    public JPrim(String s){
        this.s = s;
    }
    public Boolean isEqual() { return true; }
    public String pp()
    {
        return "" + this.p;
    }
    public Joe interp(){ return this;}
}
class JBoo implements Joe 
{
    public Boolean b;
    public JBoo(Boolean b)
    {
        this.b = b;
    }
    public Boolean isEqual() {return true;}
    public String pp() {return "" + this.b;}
    public Joe interp(){return this;}
}

class Jif implements Joe
{
    public Joe cond, taction, faction;
    public Jif(Joe cond, Joe taction, Joe faction)
    {
        this.cond = cond;
        this.taction = taction;
        this.faction = faction;    
    }
    public Boolean isEqual() {return false;}
    public String pp()
    {
        return "(if "+this.cond.pp()+" "+this.taction.pp()+" "+this.faction.pp()+")";
    }
    public Joe interp()
    {
        Joe condv = this.cond.interp();
        if(condv instanceof JBoo &&((JBoo)condv).b==false)
        {
            return this.faction.interp();
        }
        else
    }
}
class JNumber implements Joe 
{
    public int n;
    public String pp()
    {
        return Integer.toString(n);
    }
    public Joe interp()
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
    
    public Joe interp()
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
    
    public Joe interp()
    {
        return this.left.interp()*this.right.interp();
    }
    
    JMult(JNumber left, JNumber right)
    {
        this.left = left;
        this.right = right;
    }
    
}

class COMP3010HLProgram
{
    static Joe JA(Joe left, Joe right)
    {
        return new JPlus((JNumber)left,(JNumber)right);
    }
    static Joe desugar(Sxpr se)
    {
        
        //desugar n to n
        if(se instanceof SENum)
        {
            return new JNumber(((SENum)se).n);
        }
        //desugar + to 0
        if(se instanceof SConst
                && ((SConst)se).left instanceof SStr
                && ((SStr)((SConst)se).left).s.equals("+")
                && ((SConst)se).right instanceof SEmpty)
        {
            return new JNumber(0);
        }
        //desugar + left right ... to + desugar(left) desugar(+ right...)
        if(se instanceof SConst
                && ((SConst)se).left instanceof SStr
                && ((SStr)((SConst)se).left).s.equals("+")
                && ((SConst)se).right instanceof SConst)
        {
            return new JPlus((JNumber)desugar(((SConst)((SConst)se).right).left),
                    (JNumber)desugar(((SConst)((SConst)se).right).right));
        }
        //desugar * to 1
        if(se instanceof SConst
                && ((SConst)se).left instanceof SStr
                && ((SStr)((SConst)se).left).s.equals("*")
                && ((SConst)se).right instanceof SEmpty)
        {
            return new JNumber(1);
        }
        //desugar * left right to *desugar(left) desugar(right)
        if(se instanceof SConst
                && ((SConst)se).left instanceof SStr
                && ((SStr)((SConst)se).left).s.equals("*")
                && ((SConst)se).right instanceof SConst)
        {
            return new JMult((JNumber)desugar(((SConst)((SConst)se).right).left),
                    (JNumber)desugar(((SConst)((SConst)se).right).right));
        }
        return new JNumber(666);
    }
    public static void main(String[] args) {
       Sxpr groupa = new SConst(new SENum(8),new SENum(3));
       Sxpr groupb = new SConst(new SStr("+"), groupa);
       Sxpr groupc = new SENum(838);
       Sxpr groupd = new SConst(new SStr("*"), new SConst(new SENum(6),new SENum(7)));
       JNumber test1 = new JNumber(6);
       JNumber test2 = new JNumber(7);
       JNumber test3 = new JNumber(3);
       JNumber t1 = new JNumber(8);
       JNumber t2 = new JNumber(3);
       JNumber t3 = new JNumber(8);
       JMult convert1 = (JMult)desugar(groupd);
       JNumber convert2 = (JNumber)desugar(groupc);
       JPlus convert3 = (JPlus)desugar(groupb);
       
       System.out.println("SExpr groupa outputs: "+groupb.pp()+" and when converted to JExpr outputs: "+convert3.pp()+" should be "+convert3.interp());
       System.out.println("SExpr groupd outputs: "+groupd.pp()+" and when converted to JExpr outputs: "+convert1.pp()+" should be "+convert1.interp());
       System.out.println("SExpr groupc outputs: "+groupc.pp()+" and when converted to JExpr outputs: "+convert2.pp()+" should be "+convert2.interp());
    }
}
