/*
Nicholas Sweeney LL Code
*/

#include <stdio.h>


typedef enum(FALSE, TRUE)bool;
typedef enum{plus, sub, mult, div, less, lessE, equal, greatE, great}prim;

struct
{
	int type;
	int n;
	bool b;
	prim p;
		
}v;
	
struct
{
	e* eee;
	
}JApp;
	
struct
{
	bool cond;
	e taction;
	e faction;
}Jif;
	
struct
{
	int type;
	JApp a;
	Jif i;
}e;
