# Escape Analysis Samples

## Overview

Here's a summary of the escape analysis samples.
Allocations that do not escape are stack allocated.

* `EASample_01_Basic`:
An allocation within a method that does not escape (e.g. not assigned to a static variable) is stack allocated.
* `EASample_02_StaticAssignment`:
An allocation within a method that is assigned to a static variable is considered to escape globally.
* `EASample_03_ParameterEscape`:
Allocations that are assigned to field of arguments are considered to escape as arguments.
* `EASample_04_ReturnEscape`:
Allocations that are returned from method invocations are considered to escape as arguments.
* `EASample_05_Throw`:
Allocations for exceptions thrown are considered to escape as arguments.
* `EASample_06_ParameterToStatic`:
Allocations that get assigned to static variables are considered to escape globally.
* `EASample_07_Runnable`:
`Runnable` allocations are considered to escape globally, regardless of the situation.
* `EASample_09_Interfaces`:
Escape state for parameters sent to interface methods is updated with the escape information of all possible method targets.
* `EASample_10_Subclasses`:
Escape state for parameters sent to abstract methods is updated with the escape information of all possible method targets.
* `EASample_11_Loops`:
Allocations within loops are never stack allocated.

## Verification 

```bash
$ VERIFY=true make
...
Test allocations:
Success

Compilation completed with 311 warning(s)
..........................
```
