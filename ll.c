/*
Nicholas Sweeney LL Code
*/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>


enum determinant{Dif, Dnum, Dapp, Dprim, Dboo, DKif, DKApp, DKRet, DKCheck, DKUncheck, DBool, Doper, Dvar, Djdef};
typedef struct {enum determinant d;}expr;
JDef** cmap = NULL;

typedef struct
{
	expr d;
	int n;
}JNum;
	
typedef struct
{
	expr d;
	int v;
}JBoo;

typedef struct
{
	expr d;
	expr* oper;
	expr* left;
	expr* right;
}JApp;
	
typedef struct
{
	expr d;
	expr* cond;
	expr* taction;
	expr* faction;
}Jif;

typedef struct
{
	expr d;
	char* p;
}JPrim;

typedef struct
{
	expr d;
	expr* args;
	char* s;
}JOper;

typedef struct
{
	expr d;
	char* s;
}JVar;

typedef struct
{
	expr d;
	expr* oper;
	expr* e;
}
typedef struct
{
	expr d;
	expr* oper;
	expr* e;
}JDefine;

typedef struct
{
	expr d;
}KRet;

typedef struct
{
	expr d;
	expr* cond;
	expr* taction;
	expr* faction;
	expr* k;
}Kif;

typedef struct
{
	expr d;
	expr* oper;
	expr* check;
	expr* env;
	expr* uncheck;
	expr* k;
}KApp;

typedef struct
{
	expr d;
	expr* curr;
	expr* next;
	
}KCheck;

typedef struct
{
	expr d;
	expr* curr;
	expr* next;
	
}KUncheck;


expr* CJApp(expr* oper, expr* left, expr* right)
{
	printf("Made JApp\n");
	JApp* e = malloc(sizeof(JApp));
	e->d.d=Dapp;
	e->oper = oper;
	e->left = left;
	e->right = right;
	return (expr*)e;
	
}

expr* CJif(expr* cond, expr* taction, expr* faction)
{
	printf("Made Jif\n");
	Jif* e = malloc(sizeof(Jif));
	e->d.d=Dif;
	e->cond = cond;
	e->taction = taction;
	e->faction = faction;
	return (expr*)e;
}

expr* CJNum(int n)
{
	printf("Made JNum(%d)\n",n);
	JNum* e = malloc(sizeof(JNum));
	e->d.d=Dnum;
	e->n = n;
	return (expr*)e;
}

expr* CJBoo(int b)
{
	if(b==0)
	{
		printf("Made JBoo(FALSE)\n");
	}
	else
	{
		printf("Made JBoo(TRUE)\n");
	}
	JBoo* e = malloc(sizeof(JBoo));
	e->d.d=Dboo;
	e->v = b;
	return (expr*)e;
}

expr* CJPrim(char* p)
{
	printf("Made JPrim(%s)\n",p);
	JPrim* e = malloc(sizeof(JPrim));
	e->d.d=Dprim;
	e->p = p;
	return (expr*)e;
}

expr* CJOper(char* s, expr* args)
{
	printf("Made Oper\n");
	JOper* e = malloc(sizeof(JOper));
	e->d.d = Doper;
	e->args = args;
	e->s = s;
	return (expr*)e;
}

expr* CJVar(char* s)
{
	printf("Made Var\n");
	JVar* e = malloc(sizeof(JVar));
	e->d.d = Dvar;
	e->s = s;
	return (expr*)e;
}

expr* CJDefine(expr* oper, expr* e)
{
	printf("Made JDefine\n");
	if(checkMap(oper))
	{
		printf("Map Error\n");
		return 0;
	}
	JDefine* ee = malloc(sizeof(JDefine));
	ee->d.d = Djdef;
	ee->oper = oper;
	ee->e = e;
	pushMap(ee);
	return (expr*)ee;
	
}

expr* CKRet()
{
	printf("Made KRet\n");
	KRet* e = malloc(sizeof(KRet));
	e->d.d = DKRet;
	return (expr*)e;
}

expr* CKif(expr* cond, expr* taction, expr* faction, expr* k)
{
	printf("Made Kif\n");
	Kif* e = malloc(sizeof(Kif));
	e->d.d = DKif;
	e->cond = cond;
	e->taction = taction;
	e->faction = faction;
	e->k = k;
	return (expr*)e;
}

expr* CKApp(expr* oper, expr* check, expr* env, expr* uncheck, expr* k)
{
	printf("Made KApp\n");
	KApp* e = malloc(sizeof(KApp));
	e->d.d = DKApp;
	e->oper = oper;
	e->check = check;
	e->env = env;
	e->uncheck = uncheck;
	e->k = k;
	return (expr*)e;
}

expr* CKCheck(expr* curr, expr* next)
{
	printf("Made KCheck\n");
	KCheck* e = malloc(sizeof(KCheck));
	e->d.d = DKCheck;
	e->curr = curr;
	e->next = next;
	return (expr*)e;
}

expr* CKUncheck(expr* curr, expr* next)
{
	printf("Made KUncheck\n");
	KUncheck* e = malloc(sizeof(KUncheck));
	e->d.d = DKUncheck;
	e->curr = curr;
	e->next = next;
	return (expr*)e;
}

expr* delta(expr* oper, expr* check)
{
    KCheck* c = (KCheck*)check;
    KCheck* n = (KCheck*)(c->next);
    JPrim* primoper = (JPrim*)oper;
    char* sign = primoper->p;
    JNum* left = (JNum*)c->curr;
    JNum* right = (JNum*)n->next;
	int l = left->n;
	int r = right->n;
    
    if (!strcmp(sign, "+")) 
    {
        return CJNum(l + r); 
    }
    if (!strcmp(sign, "-")) 
    { 
        return CJNum(l - r); 
    }
    if (!strcmp(sign, "*")) 
    { 
        return CJNum(l * r); 
    }
    if (!strcmp(sign, "/")) 
    { 
        return CJNum(l / r); 
    }
    if (!strcmp(sign, "<")) 
    { 
        return CJBoo(l < r); 
    }
    if (!strcmp(sign, "<=")) 
    { 
        return CJBoo(l <= r); 
    }
    if (!strcmp(sign, "==")) 
    { 
        return CJBoo(l == r); 
    }
    if (!strcmp(sign, ">")) 
    { 
        return CJBoo(l > r); 
    }
    if (!strcmp(sign, ">=")) 
    { 
        return CJBoo(l >= r); 
    }
    if (!strcmp(sign, "!=")) 
    { 
        return CJBoo(l != r); 
    }

    return CJNum(666);
}

int booq(expr* e)
{
    switch(e->d)
    {
    case Dboo:
    {
		JBoo* tempo = (JBoo*)e;
        return tempo->v;
    }
    case Dnum:
    {
		JNum* tempo = (JNum*)e;
        return tempo->n;
    }
	case Dprim:
    default:
    {
        return 0;
    }
    }
}

int checkMap(expr* e)
{
	return 0;
}

void pushMap(expr* jd)
{
	
}

//LL Interpreter
void eval(expr** e)
{
	
	expr *end = CKRet();
	expr *env = NULL;
	
    while(1)
    {
        switch((*e)->d)
        {
        case Dapp:
		{
			JApp* tmp = (JApp*)(*e);
			(*e) = tmp->oper;
			end = CKApp(NULL, NULL, env, CKUncheck(tmp->left, CKUncheck(tmp->right, NULL)), end);
			break;
		}
        case Dif:
		{
			Jif* tmp = (Jif*)e;
			(*e) = CKif(env, tmp->taction, tmp->faction, end);
			break;
		}
        case Dnum:
        case Dboo:
        case Dprim:
		case Doper:
		{
			switch(end->d)
			{
				
			case DKRet:
			{
				printf("e = %d\n",((JNum*)(*e))->n);
				return;
			}
			case DKApp:
			{
				KApp* tmp2 = (KApp*)end;
				expr* poper = tmp2->oper;
				expr* checker = tmp2->check;
				if(!poper)
				{
					poper = (*e);
					tmp2->oper = poper;
				}
				else
				{
					checker = CKCheck((*e), checker);
					tmp2->check = checker;
				}
				if(tmp2->uncheck == NULL)
				{
					(*e) = delta(tmp2->oper,  tmp2->check);
					env = tmp2->env;
					end = tmp2->k;
					break;
				}
				else
				{
					KUncheck* unchecker = (KUncheck*)tmp2->uncheck;
					(*e) = unchecker->curr;
					unchecker = (KUncheck*)unchecker->next;
					tmp2->uncheck = unchecker;
					end = (expr*)tmp2;
					break;
				}
				break;
			}
			case DKif:
			{
				Kif* tmp2 = (Kif*)end;
				(*e) = booq((*e)) ? tmp2->taction : tmp2->faction;
				env = tmp2->cond;
				end = tmp2->k;
				break;
			}
			}
        }
		}
	}
}