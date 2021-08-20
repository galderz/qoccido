# Escape Analysis Samples

To verify:

```bash
$ make verify
...
======== Stack allocations for  org/example/ea/samples.EASample_01_Basic.sample(I)I
org/example/ea/samples/EASample_01_Basic$A

======== Stack allocations for  org/example/ea/samples.EASample_03_ParameterEscape.sample()I
org/example/ea/samples/EASample_03_ParameterEscape$A
```