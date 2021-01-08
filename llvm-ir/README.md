[`llvm.flt.rounds()`](https://llvm.org/docs/LangRef.html#llvm-flt-rounds-intrinsic)
```bash
lli rounds.ll
1
```
```
0  - toward zero
1  - to nearest, ties to even
2  - toward positive infinity
3  - toward negative infinity
4  - to nearest, ties away from zero
```

The default returned by `rounds()` matches the 
[LLVM documentation](https://llvm.org/docs/LangRef.html#constrained-floating-point-intrinsics):
> By default, LLVM optimization passes assume that the rounding mode is round-to-nearest
> and that floating-point exceptions will not be monitored.
