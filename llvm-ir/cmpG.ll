target triple = "x86_64-pc-linux-gnu"

declare i32 @putchar(i32)
declare double @llvm.canonicalize.f64(double)

define i32 @main() {
B0:
  %L0 = fcmp ugt double 0x44d83cebd0c4e4b0, 0x402b851eb851eb85, !dbg !14353
  %L1 = fcmp nnan ult double 0x44d83cebd0c4e4b0, 0x402b851eb851eb85, !dbg !14353
  %L2 = select i1 %L1, i32 -1, i32 0, !dbg !14353
  %L3 = select i1 %L0, i32 1, i32 %L2, !dbg !14353
  %L4 = fcmp oge double %L3, 0, !dbg !14352
  ret i32 0
}

