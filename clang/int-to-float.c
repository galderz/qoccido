#include <stdio.h>

int main(int argc, char *argv[])
{
    int i = 2147483646;
    float f = (float) 2147483646;
    int other = (int) f;
    int result = i - other;
    printf("%d\n", result);
    return 0;
}
