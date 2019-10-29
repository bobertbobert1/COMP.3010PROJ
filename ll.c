/*
Nicholas Sweeney LL Code
*/
#include <stdio.h>
#include <string.h>

typedef enum{plus, sub, mult, div, less, lessE, equal, greatE, great}prim;
enum{Dif, Dnum, Dapp, Dprim, Dboo, DKif, DKApp, DKRet, DKCheck, DKUncheck, DBool}determinant;
typedef struct {enum determinant d;}expr;

struct
{
	expr d;
	int n;
}JNum;
	
struct
{
	expr h;
	expr v;
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

expr* delta(expr* oper, expr* check)
{
    KCheck* c = (KCheck*)check;
    KCheck* n = (KCheck*)(c->next);
    JPrim* primoper = (prim*)oper;
    char* sign = primoper->p;
    JNum* left = c->data;
    JNum* right = n->data;
    
    if (!strcmp(p, "+")) 
    {
        return CKNum(left->n + right->n); 
    }
    if (!strcmp(p, "-")) 
    { 
        return CKNum(left->n - right->n); 
    }
    if (!strcmp(p, "*")) 
    { 
        return CKNum(left->n * right->n); 
    }
    if (!strcmp(p, "/")) 
    { 
        return CKNum(left->n / right->n); 
    }
    if (!strcmp(p, "<")) 
    { 
        return CKBool(left->n < right->n); 
    }
    if (!strcmp(p, "<=")) 
    { 
        return CKBool(left->n <= right->n); 
    }
    if (!strcmp(p, "==")) 
    { 
        return CKBool(left->n == right->n); 
    }
    if (!strcmp(p, ">")) 
    { 
        return CKBool(left->n > right->n); 
    }
    if (!strcmp(p, ">=")) 
    { 
        return CKBool(left->n >= right->n); 
    }
    if (!strcmp(p, "!=")) 
    { 
        return CKBool(left->n != right->n); 
    }

    return CKNum(666);
}

int booq(expr* e)
{
    switch(e->d)
    {
    case DBoo:
    {
        return e->v;
    }
    case DNum:
    {
        return e->n;
    }
    default:
    {
        return 0;
    }
    }
}
void eval(expr* e)
{
    expr *continue = CKRet();

    while(1)
    {
        switch(e->d)
        {
        case Dapp:
        {
            continue = CKApp(NULL, NULL, CKUncheck(e->left, CKUncheck(e->right, NULL)), continue);
            break;
        }
        case Dif:
        {
            continue = CKif(e->cond, e->taction, e->faction, continue);
            break;
        }
        case Dnum:
        case DBoo:
        case Dprim:
        }
            switch(continue->d)
            {
            case DKRet:
            {
                return;
            }
            case DKApp:
            {
                expr* checked = continue->check;
                expr* oper = continue->oper;

                if(!oper)
                {
                    oper = e;
                }
                if(oper!=e)
                {
                    CKCheck(e, checked);
                }
                if(continue->uncheck == NULL)
                {
                    e = delta(continue->oper, continue->check);
                    continue = continue->k;
                    break;
                }
                if(continue->uncheck != NULL)
                {
                    KUncheck* unch = continue->unchecked;
                    e = unch->data;
                    unch = unch->next;
                    break;
                }
                break;
            }
            case DKif:
            {
                e = boostate(e) ? continue->taction : continue->faction;
                continue = continue->k;
                break;
            }
            
            }    
        }
    }
}