[5.1.2. Widening Primitive Conversion](https://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html)

> A widening conversion of an `int` or a `long` value to `float`, 
> or of a `long` value to `double`, 
> may result in loss of precision - that is, 
> the result may lose some of the least significant bits of the value. 
> In this case, the resulting floating-point value will be
> a correctly rounded version of the integer value, 
> using IEEE 754 round-to-nearest mode (§4.2.4).
  
Floats in Java

See [here](https://dzone.com/articles/java-hexadecimal-floating-point-literal).

Floating points: sign, mantissa (integer, fractional), exponent

Exponent: 
Unsigned 8 bits, number of bitshifts calculated substracting 127 to get a signed numnber.
It specifies how many bits the mantissa should virtually be shifted to the left or right.

32 bits:

0 10000000 10010010000111111011010

0 = sign (positive)

10000000 128 = 128 - 127 = 1 (shift mantissa left by 1, e.g. multiply by two)

10010010000111111011010 = 0x490FDA = 4788186

Hexadecimal: `0x0.C90FDAP2f`

Mantissa: `0xC9aFDA` ~ `0x490FDA`.
`C` shown instead of `4`.
First bit is always 1 and not represented in the binary.
`C` is `1100` while the original `4` is `0100`

