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


/*
J0 with data structures for Values, Addition, and Multiplication
Interface class contains pretty-printer, big step interpreter
boolean comparison, and small step interpreter functions
*/
interface Joe 
{
    public String pp();
    public Boolean isEqual();
    public Joe interp();
    public Joe step();
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
    public Joe step() { return this; }
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
    public Joe step() {return this;}
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
    public Joe step()
    {
        return this.left.step();
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
    public Joe step(){ return this;}
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
    public Joe step(){return this;}
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
    
    public Joe step()
    {
        if(cond instanceof JBoo)
        {
            if(((JBoo)cond).b == true)
            {
                return taction;
            }
            return faction;
        }
        
        cond = cond.step();
        return this.cond;
        
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
    
    public Joe step()
    {
        if(!((((JConst)args).left).isEqual()))
        {
            return ((JConst)args).left.step();
        }
        
        if(!(((JConst)((JConst)args).right).left.isEqual()))
        {
            return ((JConst)((JConst)args).right).left.step();
        }
        
        
        String s = ((JPrim)oper).s;
        int left = ((JNumber)((JConst)args).left).n;
        int right = ((JNumber)((JConst)((JConst)args).right).left).n;
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

//Context contains plug function for filling holes in expressions
interface Context
{
    Joe plug(Joe r);
}

class hole implements Context
{
    public Joe plug(Joe fill)
    {
        return fill;
    }
}


class cap implements Context
{
      public Context hole;
      public Joe oper, left, right;
      
      public cap(Context c, Joe oper, Joe left, Joe right)
      {
          hole = c;
          this.oper = oper;
          this.left = left;
          this.right = right;
          
      }
      
      public Joe plug(Joe fill)
      {
          if(left instanceof JEmpty)
          {
              return new JApp(oper, new JConst(fill, new JConst(right, new JEmpty())));
          }
          return new JApp(oper, new JConst(left, new JConst(fill, new JEmpty())));
      }
}

//Context if for when the hole is in place of the condition case
class Cif1 implements Context
{
    public Joe taction, faction;
    public Context hole;
    
    public Cif1(Context hole, Joe left, Joe right)
    {
        this.hole = hole;
        taction = left;
        faction = right;
    }
    
    public Joe plug(Joe fill)
    {
        return new Jif(hole.plug(fill),taction, faction);
    }
    
}

//Context if for when the hole is in place of the true case
class Cif2 implements Context
{
    public Joe cond, faction;
    public Context hole;
    
    public Cif2(Joe left, Context hole, Joe right)
    {
        this.hole = hole;
        cond = left;
        faction = right;
    }
    
    public Joe plug(Joe fill)
    {
        return new Jif(cond, hole.plug(fill), faction);
    }
    
}

//Context if for when the hole is in place of the false case
class Cif3 implements Context
{
    public Joe taction, cond;
    public Context hole;
    
    public Cif3(Joe left, Joe right, Context hole)
    {
        this.hole = hole;
        taction = right;
        cond = left;
    }
    
    public Joe plug(Joe fill)
    {
        return new Jif(cond, taction, hole.plug(fill));
    }
    
}

class COMP3010HLProgram
{
    //Defining sorter function names for quicker test case creation
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
    
    //Find redex function
    static Joe findRed(Context c, Joe e)
    {
        
        //If no redex is found
        if(e.isEqual())
        {
            return e;
        }
        
        //If redex is a Jif
        if(e instanceof Jif)
        {
            if(((Jif)e).cond.isEqual())
            {
                return e;
            }
            
            Joe red = findRed(c, ((Jif)e).cond);
            return red;
            
        }
        
        //If redex is a JApp
        if(e instanceof JApp)
        {
            Joe carry = ((JApp)e).args;
            
            //Loop goes through arguments until at the end of the term list or if a redex is found
            while(!carry.isEqual())
            {
                if(carry instanceof JConst)
                {
                    if(!((JConst)carry).left.isEqual())
                    {
                        Joe red = findRed(c, ((JConst)carry).left);
                        return red;
                    }
                }
                carry = ((JConst)carry).right;
            }
        }
        
        //Failure case
        return e;
    }
    
    static Joe terp (Joe e)
    {
        Joe ee = e.step();
        
        //No more steps
        if(ee == e)
        {
            return e;
        }
        
        //More steps to be had
        return terp(ee);
    }
    
    //Test function compares Sxpr to their Joe counterpart along with what the expected value should be
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
    
    //Main runs all tests
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