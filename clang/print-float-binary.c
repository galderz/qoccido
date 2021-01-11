#include "stdio.h"
#include "assert.h"

// cc -lm print-float-binary.c && ./a.out

void print_raw_double_binary(double d)
{
 unsigned long long *double_as_int = (unsigned long long *)&d;
 int i;

 for (i=0; i<=63; i++)
   {
    if (i==1)
      printf(" "); // Space after sign field
    if (i==12)
      printf(" "); // Space after exponent field

    if ((*double_as_int >> (63-i)) & 1)
      printf("1");
    else
      printf("0");
   }
 printf("\n");
}

void parse_double(double d,
                  unsigned char *sign_field,
                  unsigned short *exponent_field,
                  unsigned long long *fraction_field)
{
 unsigned long long *double_as_int = (unsigned long long *)&d;

 *sign_field = (unsigned char)(*double_as_int >> 63);
 *exponent_field = (unsigned short)(*double_as_int >> 52 & 0x7FF);
 *fraction_field = *double_as_int & 0x000FFFFFFFFFFFFFULL;
}

void print_raw_double_hex(double d)
{
 unsigned char sign_field;
 unsigned short exponent_field;
 unsigned long long fraction_field;

 parse_double(d,&sign_field,&exponent_field,&fraction_field);

 printf("%X %X %llX\n",sign_field,exponent_field,fraction_field);
}

int main(int argc, char *argv[])
{
 double d;
 int i;

 /* Make sure unsigned long long is 8 bytes */
 assert (sizeof(unsigned long long) == sizeof(double));

 float f = 2147483648.000000;
 printf("(float)2147483648.000000:\n");
 print_raw_double_binary(f);
 print_raw_double_hex(f);
 printf("\n");

 f = 2147483647.000000;
 printf("(float)2147483647.000000:\n");
 print_raw_double_binary(f);
 print_raw_double_hex(f);
 printf("\n");

 f = 2147483646.000000;
 printf("(float)2147483646.000000:\n");
 print_raw_double_binary(f);
 print_raw_double_hex(f);
 printf("\n");

// d = 0.5;
// printf("0.5:\n");
// print_raw_double_binary(d);
// print_raw_double_hex(d);
// printf("\n");
//
// d = 0.1;
// printf("0.1:\n");
// print_raw_double_binary(d);
// print_raw_double_hex(d);
// printf("\n");
//
// /* d = NaN */
// printf("NaN:\n");
// d = 0;
// d/=d;
// print_raw_double_binary(d);
// print_raw_double_hex(d);
// printf("\n");
//
// /* d = +infinity */
// printf("+infinity:\n");
// d = 1e300;
// d*=d;
// print_raw_double_binary(d);
// print_raw_double_hex(d);
// printf("\n");
//
// /* d = 2^-1074 */
// printf("2^-1074:\n");
// d = 1;
// for (i=1; i<=1074; i++)
//   d/=2;
// print_raw_double_binary(d);
// print_raw_double_hex(d);

 return (0);
}
