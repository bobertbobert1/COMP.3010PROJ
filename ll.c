/*
Nicholas Sweeney LL Code
*/

#include <stdio.h>


typedef enum(FALSE, TRUE)bool;
typedef enum{plus, sub, mult, div, less, lessE, equal, greatE, great}prim;
enum{Dif, Dnum, Dapp, Dprim, Dboo}determinant;
typedef struct {enum determinant d;}expr;

struct
{
	expr d;
	int n;
}JNum;
	
struct
{
	expr h;
	bool v;
}JBoo;

struct
{
	expr d;
	prim* oper;
	expr* left;
	expr* right;
}JApp;
	
struct
{
	expr d;
	expr* cond;
	expr* taction;
	expr* faction;
}Jif;

struct
{
	expr d;
	prim p;
}JPrim;


	

expr* CJApp(expr* oper, expr* left, expr* right)
{
	JApp* e = malloc(sizeof(JApp));
	
}

expr* CJif(expr* cond, expr* taction, expr* faction)
{
	Jif* e = malloc(sizeof(Jif));
}

expr* CJNum(expr* n)
{
	JNum* e malloc(sizeof(JNum));
}

expr* CJBoo(expr* b)
{
	JBoo* e malloc(sizeof(JBoo));
	
}

expr* CJPrim(prim* p)
{
	JPrim* e malloc(sizeof(JPrim));
}










