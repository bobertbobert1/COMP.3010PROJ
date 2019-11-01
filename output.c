#include <stdio.h>
#include "ll.c"
 int main(int argc, char* argv[]) 
{
	expr* test3 = CJApp(CJPrim("+"), CJNum(1), CJNum(1));
	eval(&test3);
	JNum* result = (JNum*)test3;
	printf("Result of JApp is %d", result->n);
	return 0;
}