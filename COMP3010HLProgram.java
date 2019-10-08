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

class JNumber implements Joe
{
    public int n;
    public JNumber(int n)
    {
        this.n = n;
    }
    public Boolean isEqual(){ return true;}
    public String pp() { return ""+this.n;}
    public Joe interp() { return this; }
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
        return "" + this.s;
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
        {
            return this.taction.interp();
        }
    }
}

class Context implements Joe
{
    public Joe hole, left, right;
    public Context(Joe hole, Joe left, Joe right)
    {
        this.hole = hole;
        this.left = left;
        this.right = right;
        
    }
    public Boolean isEqual()
    {
        return false;
    }
    
    public String pp()
    {
        return "(C "+this.left.pp()+" "+this.hole.pp()+" "+this.right.pp()+")";
    }
    
    public Joe interp()
    {
        Joe which_hole = this.hole.interp();
        Joe which_left = this.left.interp();
        Joe which_right = this.right.interp();
        return new JConst(which_left, new JConst(which_hole, which_right));
    }
    
}
class JApp implements Joe
{
    public Joe oper, args;
    public JApp(Joe oper, Joe args)
    {
        this.oper=oper;
        this.args=args;
    }
    public Boolean isEqual()
    {
        return false;
    }
    public String pp()
    {
        return "(@ "+this.oper.pp()+" "+this.args.pp()+")";
    }
    public Joe interp()
    {
        Joe which_oper = this.oper.interp();
        Joe arg_vals = this.args.interp();
        
        String s = ((JPrim)which_oper).s;
        int left = ((JNumber)((JConst)arg_vals).left).n;
        int right = ((JNumber)((JConst)((JConst)arg_vals).right).left).n;
        if(s.equals("+")) {return new JNumber(left+right);}
        if(s.equals("*")) {return new JNumber(left*right);}
        if(s.equals("-")) {return new JNumber(left-right);}
        if(s.equals("/")) {return new JNumber(left/right);}
        if(s.equals("<")) {return new JBoo(left<right);}
        if(s.equals("<=")) {return new JBoo(left<=right);}
        if(s.equals("==")) {return new JBoo(left==right);}
        if(s.equals(">")) {return new JBoo(left>right);}
        if(s.equals(">=")) {return new JBoo(left>=right);}
        if(s.equals("!=")) {return new JBoo(left!=right);}
        
        return new JNumber(666);
        }
}

class COMP3010HLProgram
{
    static Joe JNum(int n)
    {
        return new JNumber(n);
    }
    static Joe JA(Joe left, Joe right)
    {
        return new JApp(new JPrim("+"), new JConst(left, new JConst(right, new JEmpty())));
    }
    static Joe JM(Joe left, Joe right)
    {
        return new JApp(new JPrim("*"), new JConst(left, new JConst(right,new JEmpty())));
    }
    
    static Sxpr SApp(String oper ,Sxpr left, Sxpr right)
    {
        return new SConst(new SStr(oper),new SConst(left, new  SConst(right, new SEmpty())));
    }
    
    static Sxpr SN(int n)
    {
        return new SENum(n);
    }
    static Sxpr SA(Sxpr left, Sxpr right)
    {
        return SApp("+",left, right);
    }
    static Sxpr SM(Sxpr left, Sxpr right)
    {
        return SApp("*",left, right);
    }
    static Sxpr SIf(Sxpr cond, Sxpr left, Sxpr right)
    {
        return new SConst(new SStr("if"), new SConst(cond, new SConst(left, new SConst(right, new SEmpty()))));
    }
    
    static Joe desugar(Sxpr se)
    {
        
        //desugar n to n
        if(se instanceof SENum)
        {
            return JNum(((SENum) se).n);
        }
        
        
        //desugar + SENum SEmpty to desugar(SENum)
        if((se instanceof SConst
                && ((SConst)se).left instanceof SStr
                && ((SStr)((SConst)se).left).s.equals("+")
                && ((SConst)se).right instanceof SConst
                && ((SConst)((SConst)se).right).left instanceof SENum
                && ((SConst)((SConst)se).right).right instanceof SEmpty))
        {
            return desugar(((SConst)((SConst)se).right).left);
        }
        
        //desugar + SEmpty to 0
        if(se instanceof SConst
                && ((SConst)se).left instanceof SStr
                && ((SStr)((SConst)se).left).s.equals("+")
                && ((SConst)se).right instanceof SEmpty)
        {
            return JNum(0);
        }
        
        //desugar + left right ... to + desugar(left) desugar(+ right...)
        if(se instanceof SConst
                && ((SConst)se).left instanceof SStr
                && ((SStr)((SConst)se).left).s.equals("+")
                && ((SConst)se).right instanceof SConst)
        {
            return JA(desugar(((SConst)((SConst)se).right).left),
                    desugar(new SConst(((SConst)se).left, ((SConst)((SConst)se).right).right)));
        }
        
        //desugar * SENum SEmpty to desugar(SENum)
        if((se instanceof SConst
                && ((SConst)se).left instanceof SStr
                && ((SStr)((SConst)se).left).s.equals("*")
                && ((SConst)se).right instanceof SConst
                && ((SConst)((SConst)se).right).left instanceof SENum
                && ((SConst)((SConst)se).right).right instanceof SEmpty))
        {
            return desugar(((SConst)((SConst)se).right).left);
        }
        
        //desugar * SEmpty to 1
        if(se instanceof SConst
                && ((SConst)se).left instanceof SStr
                && ((SStr)((SConst)se).left).s.equals("*")
                && ((SConst)se).right instanceof SEmpty)
        {
            return JNum(1);
        }
        
        //desugar * left right to *desugar(left) desugar(right)
        if(se instanceof SConst
                && ((SConst)se).left instanceof SStr
                && ((SStr)((SConst)se).left).s.equals("*")
                && ((SConst)se).right instanceof SConst)
        {
            return JM(desugar(((SConst)((SConst)se).right).left),
                    desugar(new SConst(((SConst)se).left, ((SConst)((SConst)se).right).right)));
        }
        
        //desugar - left right to - desugar(left) desugar(right)
        if(se instanceof SConst
         && ((SConst)se).left instanceof SStr
         && ((SStr)((SConst)se).left).s.equals("-")
         && ((SConst)se).right instanceof SConst
         && ((SConst)((SConst)se).right).right instanceof SEmpty) 
        {
            return JM(JNum(-1), desugar(((SConst)((SConst)se).right).left) ); 
        }
        //desugar none operational primitives
        if ( se instanceof SConst
         && ((SConst)se).left instanceof SStr
         && ((SConst)se).right instanceof SConst
         && ((SConst)((SConst)se).right).right instanceof SConst
         && ((SConst)((SConst)((SConst)se).right).right).right instanceof SEmpty) 
        {
             return new JApp( new JPrim(((SStr)((SConst)se).left).s),
                    new JConst(desugar(((SConst)((SConst)se).right).left),
                    new JConst(desugar(((SConst)((SConst)((SConst)se).right).right).left), new JEmpty()))); }
        //desugar If statements
        if ( se instanceof SConst
         && ((SConst)se).left instanceof SStr
         && ((SStr)((SConst)se).left).s.equals("if")
         && ((SConst)se).right instanceof SConst
         && ((SConst)((SConst)se).right).right instanceof SConst
         && ((SConst)((SConst)((SConst)se).right).right).right instanceof SConst
         && ((SConst)((SConst)((SConst)((SConst)se).right).right).right).right instanceof SEmpty ) 
        {
            return new Jif( desugar(((SConst)((SConst)se).right).left),
                   desugar(((SConst)((SConst)((SConst)se).right).right).left),
                   desugar(((SConst)((SConst)((SConst)((SConst)se).right).right).right).left) ); 
        }
        
        return JNum(666);
    }
    
    static int tests_passed = 0;
    static void test(Sxpr se, Joe expect)
    {
        Joe e = desugar(se);
        //Output the sxpr desugared to joe
        Joe answer = e.interp();
        if(!answer.pp().equals(expect.pp()))
        {
            System.out.println(e.pp()+" = "+answer.pp()+" but should = "+expect.pp());
        }
        else
        {
            tests_passed++;
        }
    }
    
    static void test_num(Sxpr se, int n)
    {
        test(se, JNum(n));
    }
    public static void main(String[] args) 
    {
        test_num(SN(42), 42);
        test_num(SN(7), 7);
        test_num(SA(SN(42),SN(0)), 42);
        test_num(SM(SN(42),SN(0)), 0);
        test_num(SA(SM(SN(42),SN(0)),SN(0)), 0);
        test_num(SA(SM(SN(42),SN(0)),SA(SM(SN(42),SN(0)),SN(0))), 0);

        test_num(SA(SN(42),SN(1)), 43);
        test_num(SM(SN(42),SN(1)), 42);
        test_num(SA(SM(SN(42),SN(1)),SN(1)), 43);
        test_num(SA(SM(SN(42),SN(1)),SA(SM(SN(42),SN(1)),SN(1))), 85);

        test_num(new SConst(new SStr("+"), new SEmpty()), 0);
        test_num(new SConst(new SStr("*"), new SEmpty()), 1);
        Sxpr three_things =
          new SConst(new SENum(1),
                      new SConst(new SENum(2),
                                  new SConst(new SENum(4),
                                              new SEmpty())));
        test_num(new SConst(new SStr("+"), three_things), 7);
        test_num(new SConst(new SStr("*"), three_things), 8);

        test_num(new SConst(new SStr("-"), new SConst(new SENum(4), new SEmpty())), -4);
        test_num(new SConst(new SStr("-"), new SConst(new SENum(4), new SConst(new SENum(2), new SEmpty()))), 2);

        test(new SConst(new SStr("=="), new SConst(new SENum(4), new SConst(new SENum(2), new SEmpty()))), new JBoo(false));
        test(new SConst(new SStr("=="), new SConst(new SENum(4), new SConst(new SENum(4), new SEmpty()))), new JBoo(true));

        test(SApp("==", new SENum(4), new SENum(4)), new JBoo(true));
        test(SIf(SApp("==", new SENum(4), new SENum(4)),
                 new SENum(5),
                 new SENum(6)), JNum(5));
        test(SIf(SApp("==", new SENum(4), new SENum(2)),
                 new SENum(5),
                 new SENum(6)), JNum(6));

        System.out.println(tests_passed + " tests passed!");
       
    }
}
