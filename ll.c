/*
Nicholas Sweeney LL Code
*/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>


enum determinant{Dif, Dnum, Dapp, Dprim, Dboo, DKif, DKApp, DKRet, DKCheck, DKUncheck, DBool};
typedef struct {enum determinant d;}expr;

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

expr* CKApp(expr* oper, expr* check, expr* uncheck, expr* k)
{
	printf("Made KApp\n");
	KApp* e = malloc(sizeof(KApp));
	e->d.d = DKApp;
	e->oper = oper;
	e->k = k;
	e->check = check;
	e->uncheck = uncheck;
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
    default:
    {
        return 0;
    }
    }
}

//LL Interpreter
void eval(expr* e)
{
	
	expr* end = CKRet();
	
    while(1)
    {
        switch(e->d)
        {
        case Dapp:
		{
			JApp* tmp = (JApp*)e;
			end = CKApp(NULL, NULL, CKUncheck(tmp->left, CKUncheck(tmp->right, NULL)), end);
			break;
		}
        case Dif:
		{
			Jif* tmp = (Jif*)e;
			end = CKif(tmp->cond, tmp->taction, tmp->faction, end);
			break;
		}
        case Dnum:
        case Dboo:
        case Dprim:
		{
			switch(end->d)
			{
				
			case DKRet:
			{
				return;
			}
			case DKApp:
			{
				KApp* tmp2 = (KApp*)end;
				if(!tmp2->oper)
				{
					tmp2->oper = e;
				}
				else
				{
					expr* checker = CKCheck(e, checker);
				}
				if(tmp2->uncheck == NULL)
				{
					e = delta(tmp2->oper,  tmp2->uncheck);
					end = tmp2->k;
					break;
				}
				else
				{
					KUncheck* unchecker = (KUncheck*)tmp2->uncheck;
					e = unchecker->curr;
					unchecker = (KUncheck*)unchecker->next;
					break;
				}
				break;
			}
			case DKif:
			{
				Kif* tmp2 = (Kif*)end;
				e = booq(e) ? tmp2->taction : tmp2->faction;
				end = tmp2->k;
				break;
			}
			}
        }
		}
	}
}