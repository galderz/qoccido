target triple = "x86_64-pc-linux-gnu"

@pfmtf = constant [4 x i8] c"%f\0A\00"
declare i32 @printf(i8*,...)

define i32 @main() {
B0:
  %L0 = sitofp i32 1 to float
  %L1 = fpext float %L0 to double
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtf, i32 0, i32 0), double %L1)
  ret i32 0
}
