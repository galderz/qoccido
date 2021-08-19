# Escape Analysis Samples

To verify:

```bash
$ make verify
...
(qbicc compiler thread xx/128) [ea-opt] New class(org/example/ea/samples/EASample_01_Basic$A) on org/example/ea/samples.EASample_01_Basic.sample(I)I does not escape, so stack allocate it
(qbicc compiler thread xx/128) [ea-opt] New class(org/example/ea/samples/EASample_03_ParameterEscape$A) on org/example/ea/samples.EASample_03_ParameterEscape.sample()I does not escape, so stack allocate it
```