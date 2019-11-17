#include <stdio.h>
#include "ll.c"
 int main(int argc, char* argv[]) 
{
	expr* test = CJOper("OperTest", CKCheck(CJNum(15), CKCheck(CJOper("(x4)", CKCheck(CJNum(4),NULL)), NULL)));
	eval(&test);
	return 0;
}