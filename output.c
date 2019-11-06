#include <stdio.h>
#include "ll.c"
 int main(int argc, char* argv[]) 
{
	expr* test3 = CJApp(CJPrim("+"), CJNum(1), CJNum(1));
	eval(&test3);
	expr* test1 = CJif(CJPrim("<"), CJNum(1), CJNum(2));
	eval(&test1);
	expr* test2 = CJApp(CJPrim("*"), CJNum(4),CJNum(3));
	expr* test4 = CJif(CJPrim(">"), CJApp(CJPrim("*"), CJNum(4),CJNum(3)), CJApp(CJPrim("+"), CJNum(1), CJNum(1)));
	expr* test5 = CJApp(CJPrim("/"), CJNum(8), CJNum(2));
	expr* test6 = CJif(CJPrim(">="), CJNum(3), CJNum(6));
	expr* test7 = CJApp(CJPrim("-"), CJNum(333), CJNum(1));
	expr* test8 = CJif(CJPrim("<="), CJNum(3), CJNum(3));
	expr* test9 = CJApp(CJPrim("+"),CJNum(4), CJNum(69));
	expr* test10 = test3;
	expr* test11 = test10;
	expr* test12 = CJApp(CJPrim("+"), CJNum(303), CJNum(30));
	eval(&test2);
	eval(&test4);
	eval(&test5);
	eval(&test6);
	eval(&test7);
	eval(&test8);
	eval(&test9);
	eval(&test11);
	eval(&test12);
	JNum* result1 = (JNum*)test1;
	JNum* result2 = (JNum*)test2;
	JNum* result3 = (JNum*)test3;
	JNum* result4 = (JNum*)test4;
	JNum* result5 = (JNum*)test5;
	JNum* result6 = (JNum*)test6;
	JNum* result7 = (JNum*)test7;
	JNum* result8 = (JNum*)test8;
	JNum* result9 = (JNum*)test9;
	JNum* result10 = (JNum*)test10;
	JNum* result11 = (JNum*)test11;
	JNum* result12 = (JNum*)test12;
	printf("Result 1 is %d", result1->n);
	printf("Result 2 is %d", result2->n);
	printf("Result 3 is %d", result3->n);
	printf("Result 4 is %d", result4->n);
	printf("Result 5 is %d", result5->n);
	printf("Result 6 is %d", result6->n);
	printf("Result 7 is %d", result7->n);
	printf("Result 8 is %d", result8->n);
	printf("Result 9 is %d", result9->n);
	printf("Result 10 is %d", result10->n);
	printf("Result 11 is %d", result11->n);
	printf("Result 12 is %d", result12->n);
	return 0;
}