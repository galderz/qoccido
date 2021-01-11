#include <stdio.h>
#include <stdint.h>
#include <inttypes.h>
#include <math.h>

// The LLVM bitcode produced by this code can be checked via:
// clang -g -O0 -c -emit-llvm -pipe -x c -o - print-float-bits.c | llvm-dis | less
int main (void) {

    float pi = (float)M_PI;
    union {
        float f;
        uint32_t u;
    } f2u = { .f = pi };

    printf ("pi : %f\n   : 0x%" PRIx32 "\n", pi, f2u.u);
    printf ("pi : %f\n   : %a" PRIx32 "\n", pi, pi);

    return 0;
}