/*
Nicholas Sweeney LL Code
*/
#include <stdio.h>


typedef enum(FALSE, TRUE)bool;
typedef enum{plus, sub, mult, div, less, lessE, equal, greatE, great}prim;
enum{Dif, Dnum, Dapp, Dprim, Dboo, DKif, DKApp, DKRet, DKCheck, DKUncheck}determinant;
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

struct
{
	expr d;
}KRet;

struct
{
	expr d;
	expr* cond;
	expr* taction;
	expr* faction;
	expr* k;
}Kif;

struct
{
	expr d;
	expr* oper;
	expr* env;
	expr* check;
	expr* uncheck;
	expr* k;
}KApp;

struct
{
	expr d;
	expr* curr;
	expr* next;
	
}KCheck;

struct
{
	expr d;
	expr* curr;
	expr* next;
	
}KUncheck;


expr* CJApp(expr* oper, expr* left, expr* right)
{
	JApp* e = malloc(sizeof(JApp));
	e->d.d=Dapp;
	e->oper = oper;
	e->left = left;
	e->right = right;
	return e;
	
}

expr* CJif(expr* cond, expr* taction, expr* faction)
{
	Jif* e = malloc(sizeof(Jif));
	e->d.d=Dif;
	e->cond = cond;
	e->taction = taction;
	e->faction = faction;
	return e;
}

expr* CJNum(expr* n)
{
	JNum* e malloc(sizeof(JNum));
	e->d.d=Dnum;
	e->n = n;
	return e;
}

expr* CJBoo(expr* b)
{
	JBoo* e malloc(sizeof(JBoo));
	e->d.d=Dboo;
	e->v = b;
}

expr* CJPrim(prim* p)
{
	JPrim* e malloc(sizeof(JPrim));
	e->d.d=Dprim;
	e->p = p;
	return e;
}

expr* CKRet()
{
	KRet* e = malloc(sizeof(KRet));
	o->d.d = DKRet;
	return e;
}

expr* CKif(expr* cond, expr* taction, expr* faction, expr* k)
{
	Kif* e = malloc(sizeof(Kif));
	e->d.d = DKif;
	e->cond = cond;
	e->taction = taction;
	e->faction = faction;
	e->k = k;
	return e;
}

expr* CKApp(expr* oper, expr* env, expr* check, expr* uncheck)
{
	KApp* e = malloc(sizeof(KApp));
	e->d.d = DKApp;
	e->oper = oper;
	e->env = env;
	e->check = check;
	e->uncheck = uncheck;
	return e;
}

expr* CKCheck(expr* curr, expr* next)
{
	KCheck* e = malloc(sizeof(KCheck));
	e->d.d = DKCheck;
	e->curr = curr;
	e->next = next;
	return e;
}

expr* CKUncheck(expr* args)
{
	KUncheck* e = malloc(sizeof(KUncheck));
	e->d.d = DKUncheck;
	e->curr = curr;
	e->next = next;
	return e;
}