# Escape Analysis Samples

To verify:

```bash
$ VERIFY=true make
...
======== Stack allocations

Method                                                          Type

org/example/ea/samples.EASample_01_Basic.sample(I)I             org/example/ea/samples/EASample_01_Basic$A
org/example/ea/samples.EASample_03_ParameterEscape.sample()I    org/example/ea/samples/EASample_03_ParameterEscape$A
```