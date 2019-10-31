#include <stdio.h>
#include "ll.c"
 int main(int argc, char* argv[]) 
{
	expr* test3 = CJApp(CJPrim("+"), CJNum(1), CJNum(1));
	eval(test3);
	return 0;
}