/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp3010hlprogram;

/**
 *
 * @author Nick
 */
interface Joe {    }

class JNumber implements Joe 
{
    int n;
    JNumber(int _n){ n=_n;};
}

class JPlus implements Joe
{
    Joe left, right;
    int JPlus(JNumber left, JNumber right)
    {
        return left.n+right.n;
    }
}

class JMult implements Joe
{
    Joe left,right;
    int JMult(JNumber left, JNumber right)
    {
        return left.n*right.n;
    }
    
}
public class COMP3010HLProgram {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
