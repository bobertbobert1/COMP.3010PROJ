/*
Nicholas Sweeney COMP3010 Project
 */
package comp3010hlprogram;


import java.io.*;

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
class Cif implements Context
{
    public Joe taction, faction;
    public Context hole;
    
    public Cif(Context hole, Joe left, Joe right)
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

//CC0 Machine
class CC0
{
    class state
    {
        Joe e;
        Context E;
        public state(Joe e, Context E)
        {
            this.e = e;
            this.E = E;
        }
    }
    
    public state inject(Joe e)
    {
        return new state(e, new hole());
    }
    
    public Joe extract(state st)
    {
        return st.E.plug(st.e);
    }
    
    public Joe interp(Joe e)
    {
        state st = inject(e);
        while(!(st.e instanceof JNumber) && !(st.E instanceof hole))
        {
            st = step(st);
            st.e = st.e.step();
        }
        return extract(st);
    }
    
    public Joe delta(Joe e)
    {
        Joe oper = ((JApp)e).oper;
        Joe args = ((JApp)e).args;
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
    public state step(state st)
    {
        if(st.e instanceof Jif)
        {
            return new state(((Jif)st.e).cond, new Cif(new hole(), ((Jif)st.e).taction, ((Jif)st.e).faction));
        }
        if(st.e instanceof JBoo && ((JBoo)st.e).b == true && st.E instanceof Cif)
        {
            return new state(((Cif)st.E).taction, new hole());
        }
        if(st.e instanceof JBoo && ((JBoo)st.e).b == false && st.E instanceof Cif)
        {
            return new state(((Cif)st.E).faction, new hole());
        }
        if(st.e instanceof JApp)
        {
            return new state(((JConst)((JApp)st.e).args).left, new cap(new hole(), ((JApp)st.e).oper, new JEmpty(), ((JConst)((JConst)((JApp)st.e).args).right).left));
        }
        if(st.e instanceof JNumber && st.E instanceof cap && ((cap)st.E).left instanceof JEmpty)
        {
            return new state(((cap)st.E).right, new cap(new hole(), ((JApp)st.e).oper, st.e, new JEmpty()));
        }
        if(st.e instanceof JNumber && st.E instanceof cap && ((cap)st.E).right instanceof JEmpty)
        {
            return new state(delta(st.E.plug(st.e)), new hole());
        }
        
        return new state(new JNumber(666), new hole());
    }
    
}

class COMP3010HLProgram
{
    //Defining sorter function names for quicker test case creation
    static Joe JNum(int n)
    {        return new JNumber(n);   }
    static Joe JA(Joe left, Joe right)
    {     return new JApp(new JPrim("+"), new JConst(left, new JConst(right, new JEmpty())));  }
    static Joe JM(Joe left, Joe right)
    {    return new JApp(new JPrim("*"), new JConst(left, new JConst(right,new JEmpty())));  }
     static Sxpr SApp(String oper ,Sxpr left, Sxpr right)
    {     return new SConst(new SStr(oper),new SConst(left, new  SConst(right, new SEmpty()))); }  
    static Sxpr SN(int n)
    { return new SENum(n);}
    static Sxpr SA(Sxpr left, Sxpr right)
    {    return SApp("+",left, right);}
    static Sxpr SM(Sxpr left, Sxpr right)
    {    return SApp("*",left, right);}
    static Sxpr SIf(Sxpr cond, Sxpr left, Sxpr right)
    {    return new SConst(new SStr("if"), new SConst(cond, new SConst(left, new SConst(right, new SEmpty()))));    }
    
    static Joe desugar(Sxpr se)
    {
        
        //desugar n to n
        if(se instanceof SENum)
        {    return JNum(((SENum) se).n);    }
        
        
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
        Context c = new hole();
        Joe rede = findRed(c, e);
        Joe redee = rede.step();
        Joe finale = c.plug(redee);
        
        
        //No more steps
        if(finale == e)
        {
            return e;
        }
        
        //More steps to be had
        return terp(finale);
    }
    
    static void emit(Joe e) throws IOException
    {
        FileWriter fw = new FileWriter("output.c");

        fw.write("#include <stdio.h>;\n");
        fw.write("#include "+'"'+"ll.c"+'"'+";\n ");
        fw.write("int main(int argc, char* argv[]) {\n");
        
        if(e instanceof JNumber)
        {
            fw.write("\tExpression = "+printJNumber(e)+";\n");
        }
        if(e instanceof JBoo)
        {
            fw.write("\tExpression = "+printJBoo(e)+";\n");
        }
        if(e instanceof JApp)
        {
             fw.write("\tExpression = "+printJApp(e)+";\n");
        }
        if(e instanceof Jif)
        {
             fw.write("\tExpression = "+printJif(e)+";\n");
        }
        fw.write("return 0;\n}");
        fw.close();
    }
    static String printJNumber(Joe e)
    {
        return "CJNum("+((JNumber)e).n+")";
    }
    static String printJBoo(Joe e)
    {
        return "CJBoo("+((JBoo)e).b+")";
    }
    static String printJApp(Joe e)
    {
        String output = "CJApp(";
        
        //Operator
        output+="CJPrim("+((JPrim)((JApp)e).oper).s+"))";
        
        //Left
        if(((JConst)((JApp)e).args).left instanceof JApp)
        {
            output+=", "+printJApp(((JConst)((JApp)e).args).left);
        }
        if(((JConst)((JApp)e).args).left instanceof Jif)
        {
            output+=", "+printJif(((JConst)((JApp)e).args).left);
        }
        if(((JConst)((JApp)e).args).left instanceof JNumber)
        {
            output+=", "+printJNumber(((JConst)((JApp)e).args).left);
        }
        if(((JConst)((JApp)e).args).left instanceof JBoo)
        {
            output+=", "+printJBoo(((JConst)((JApp)e).args).left);
        }
        
        //Right
        
        if(((JConst)((JConst)((JApp)e).args).right).left instanceof JApp)
        {
            output+=", "+printJApp(((JConst)((JConst)((JApp)e).args).right).left);
        }
        if(((JConst)((JConst)((JApp)e).args).right).left instanceof Jif)
        {
            output+=", "+printJif(((JConst)((JConst)((JApp)e).args).right).left);
        }
        if(((JConst)((JConst)((JApp)e).args).right).left instanceof JNumber)
        {
            output+=", "+printJNumber(((JConst)((JConst)((JApp)e).args).right).left);
        }
        if(((JConst)((JConst)((JApp)e).args).right).left instanceof JBoo)
        {
            output+=", "+printJBoo(((JConst)((JConst)((JApp)e).args).right).left);
        }
        
        output+=")";
        return output;
    }
    static String printJif(Joe e)
    {
        String output = "CJIf(";
        
        //cond
        if(((Jif)e).cond instanceof Jif)
        {
            output+=", "+printJif(((Jif)e).cond);
        }
        
        if(((Jif)e).cond instanceof JApp)
        {
            output+=", "+printJApp(((Jif)e).cond);
        }
        
        if(((Jif)e).cond instanceof JNumber)
        {
            output+=", "+printJNumber(((Jif)e).cond);
        }
        
        if(((Jif)e).cond instanceof JBoo)
        {
            output+=", "+printJBoo(((Jif)e).cond);
        }
        
        //taction
        if(((Jif)e).taction instanceof Jif)
        {
            output+=", "+printJif(((Jif)e).taction);
        }
        
        if(((Jif)e).taction instanceof JApp)
        {
            output+=", "+printJApp(((Jif)e).taction);
        }
        
        if(((Jif)e).taction instanceof JNumber)
        {
            output+=", "+printJNumber(((Jif)e).taction);
        }
        
        if(((Jif)e).taction instanceof JBoo)
        {
            output+=", "+printJBoo(((Jif)e).taction);
        }
        
        //faction
        if(((Jif)e).faction instanceof Jif)
        {
            output+=", "+printJif(((Jif)e).faction);
        }
        
        if(((Jif)e).faction instanceof JApp)
        {
            output+=", "+printJApp(((Jif)e).faction);
        }
        
        if(((Jif)e).faction instanceof JNumber)
        {
            output+=", "+printJNumber(((Jif)e).faction);
        }
        
        if(((Jif)e).faction instanceof JBoo)
        {
            output+=", "+printJBoo(((Jif)e).faction);
        }
        return output;
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
    public static void main(String[] args) throws IOException 
    {
        Joe e = JA(JNum(1), JNum(1));
        emit(e);
    }
}