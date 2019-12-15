/*
Nicholas Sweeney LL Code
*/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>


enum determinant{Dif, Dnum, Dapp, Dprim, Dboo, DKif, DKApp, DKRet, DKCheck, DKUncheck, DBool, Dlam, Dvar, Djdef, Denv, Dclo, Dcase, Dkcase, Dunit, Dpair, Dinl, Dinr};
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
	expr* args;
	char* s;
}Lam;

typedef struct
{
	expr d;
	char* s;
}JVar;

typedef struct
{
	expr d;
	JOper* oper;
	expr* e;
}JDefine;

typedef struct
{
	expr d;
	expr* v;
	expr* val;
	expr* next;
}JEnv;

typedef struct
{
	expr d;
	expr* delt;
	expr* env;
}JClo;

typedef struct
{
	expr d;
}KRet;

typedef struct
{
	expr d;
	expr* cond;
	expr* env;
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

typedef struct
{
	expr d;
	expr* e;
	expr* left;
	expr* lefte;
	expr* right;
	expr* righte;
	
}JCase;

typedef struct
{
	expr d;
	expr* e;
	exor* left;
	expr* right;
	expr* lefte;
	expr* righte;
	expr* env;
	expr* k;
}KCase;

typedef struct
{
	expr d;
}JUnit;

typedef struct
{
	expr d;
	expr* left;
	expr* right;
}JPair;

typedef struct
{
	expr d;
	expr* e;

}JInl;

typedef struct
{
	expr d;
	expr* e;
}JInr;


JDefine** cmap = NULL;

//Checks to see if the expression is already in the map
int checkMap(JOper* e)
{
	JOper* o = (JOper*)e;
	if(cmap==NULL)
	{
		cmap = malloc(sizeof(JDefine*));
	}
	for(int i=0; i<(sizeof(cmap)/sizeof(JDefine)); ++i)
	{
		if(strcmp(o->s, (((JOper*)cmap[i])->s)))
		{
			return i;
		}
	}
	return 0;
}

//Updates Map with new JDef
JDefine* pushMap(JDefine* jd)
{
	JDefine** tmp = malloc(sizeof(cmap)+sizeof(JDefine*));
	int i;
	for(i=0;i< (sizeof(cmap)/sizeof(JDefine*));++i)
	{
		tmp[i] = cmap[i];
	}
	tmp[i] = jd;
	free(cmap);
	cmap = tmp;
}

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

expr* CLam(char* s, expr* args)
{
	printf("Made Oper\n");
	Lam* e = malloc(sizeof(Lam));
	e->d.d = Dlam;
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

expr* CJDefine(Lam* oper, expr* e)
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
	ee = pushMap(ee);
	return (expr*)ee;
	
}

expr* CJEnv(expr* v, expr* val, expr* next)
{
	printf("Made JEnv\n");
	JEnv* ee = malloc(sizeof(JEnv));
	ee->d.d=Denv;
	ee->v = v;
	ee->val = val;
	ee->next = next;
	return (expr*)ee;
}

expr* CJClo(expr* delt, expr* env)
{
	printf("Made JClo\n");
	JClo* ee = malloc(sizeof(JClo));
	ee->d.d = Dclo;
	ee->delt = delt;
	ee->env = env;
	return (expr*)ee;
}

expr* CKRet()
{
	printf("Made KRet\n");
	KRet* e = malloc(sizeof(KRet));
	e->d.d = DKRet;
	return (expr*)e;
}

expr* CKif(expr* cond, expr* env, expr* taction, expr* faction, expr* k)
{
	printf("Made Kif\n");
	Kif* e = malloc(sizeof(Kif));
	e->d.d = DKif;
	e->cond = cond;
	e->env = env;
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
	e->check = check;
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

expr* CJCase(expr* e, expr* left, expr* lefte, expr* right, expr* righte)
{
	printf("Made JCase\n");
	JCase* e = malloc(sizeof(JCase));
	e->d.d = Dcase;
	e->e = e;
	e->left = left;
	e->right = right;
	e->lefte = lefte;
	e->righte = righte;
	return e;	
}

expr* CKCase(expr* e, expr* left, expr* lefte, expr* right, expr* righte, expr* env, expr* k)
{
	printf("Made KCase\n");
	KCase* e = malloc(sizeof(KCase));
	e->d.d = Dkcase;
	e->e e;
	e->left = left;
	e->right = right;
	e->lefte = lefte;
	e->righte = righte;
	e->env = env;
	e->k = k;
	return e;
}

expr* CJUnit()
{
	printf("Made JUnit\n");
	JUnit* e = malloc(sizeof(JUnit));
	e->d.d = Dunit;
	return e;
}

expr* CJPair(expr* left, expr* right)
{
	printf("Made JPair\n");
	JPair* e = malloc(sizeof(JPair));
	e->d.d = Dpair;
	e->left = left;
	e->right = right;
	return e;	
}

expr* CJInl(expr* e)
{
	printf("Made JInl\n");
	JInl* e = malloc(sizeof(JInl));
	e->d.d = Dinl;
	e->e = e;
	return e;
}

expr* CJInr(expr* e)
{
	printf("Made JInr\n");
	JInr* e = malloc(sizeof(JInr));
	e->d.d = Dinr;
	e->e = e;
	return e;
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
    
	if(c->curr->d == Dnum && n->curr->d == Dnum)
	{
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
	}
	
	else
	{
		if(!strcmp(sign, "JPair"))
		{
			return CJPair(c->curr,n->curr);
		}
		
		if(!strcmp(sign, "Jinl"))
		{
			return CJInl(c->curr);
		}
		
		if(!strcmp(sign, "Jinr"))
		{
			return CJInr(n->curr);
		}
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

expr* subst(expr* e, expr* x, expr* v)
{
	switch(e->d)
	{
		case Dapp:
		{
			JApp* tmp = (JApp*)e;
			return CJApp(subst(tmp->oper, x, v), subst(tmp->left, x, v), subst(tmp->right, x, v));
			break;
		}
		case Dif:
		{
			Jif* tmp = (Jif*)e;
			return CJif(subst(tmp->cond, x, v), subst(tmp->taction, x, v), subst(tmp->faction, x, v));
			break;
		}
		case Dclo:
		{
			JClo* tmp = (JClo*)e;
			return CJClo(subst(tmp->delt, x, v),subst(tmp->env,x ,v));
			break;
		}
		case Dvar:
		{
			if(e==x)
			{
				return v;
			}
			else
			{
				return e;
			}
			break;
			
		}
		case Dnum:
		case Dboo:
		case Dprim:
		case Dlam:
			return e;
	}
}

//LL Interpreter
void eval(expr** e)
{
	
	expr *end = CKRet();
	JEnv *env = NULL;
	
    while(1)
    {
        switch((*e)->d)
        {
        case Dapp:
		{
			JApp* tmp = (JApp*)(*e);
			(*e) = tmp->oper;
			end = CKApp(CJClo(delta((*e) ,tmp->check),  env)), NULL, CKUncheck(tmp->left, CKUncheck(tmp->right, NULL)), end);
			break;
		}
        case Dif:
		{
			Jif* tmp = (Jif*)e;
			(*e) = CKif(env, tmp->cond, tmp->taction, tmp->faction, end);
			break;
		}
		case Dlam:
		{
			printf("LAM\n");
			env = CJEnv(((Lam*)e)->s, NULL, env);
			e = CJClo(e, env);
			env->val = e;
			env = NULL;
			break;
		}
		case Dvar:
		{
			printf("VAR\n");
			JVar* tmp = (JVar*)(*e);
			JEnv* tmpenv = (JEnv*)env;
			
			while(tmpenv!=NULL)
			{
				printf("ENV: %s\n",tmpenv->v);
				printf("VAR: %s\n", (JVar*)tmp->s);
				tmpenv = (JEnv*)tmpenv->next;
			}
			break;
			
		}
		
		case Dcase:
		{
			printf("CASE\n");
			JCase* tmp = (JCase*)(*e);
			end = CKCase(tmp->e, tmp->left, tmp->lefte, tmp->right, tmp->righte, env, end);
			(*e) = tmp->e;
			break;
		}
		case Dinl:
		case Dinr:
        case Dnum:
        case Dboo:
        case Dprim:
		case Dclo:
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
					(*e) = JClo(delta(tmp2->oper,  tmp2->check), tmp2->env);
					env = NULL;
					end = tmp2->k;
					break;
				}
				else
				{
					KUncheck* unchecker = (KUncheck*)tmp2->uncheck;
					(*e) = unchecker->curr;
					unchecker = (KUncheck*)unchecker->next;
					tmp2->uncheck = (expr*)unchecker;
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
			case Dkcase:
			{
				KCase* tmp2 = (KCase*)end;
				if((*e)->d == Dinl)
				{
					(*e) = tmp2->lefte;
				}
				if((*e)->d ==Dinr)
				{
					(*e) = tmp2->righte;
				}
				
				env = tmp2->env;
				end = tmp2->k;
				break;
			}
			}
        }
		}
	}
}