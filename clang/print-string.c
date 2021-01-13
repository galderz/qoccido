#include <stdio.h>

// The LLVM bitcode produced by this code can be checked via:
// clang -g -O0 -c -emit-llvm -pipe -x c -o - print-string.c | llvm-dis | less
int main(int argc, char *argv[])
{
    printf("hello world\n");
    return 0;
}
