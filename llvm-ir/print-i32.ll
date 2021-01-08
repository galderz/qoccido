target triple = "x86_64-pc-linux-gnu"

@pfmti32 = constant [4 x i8] c"%d\0A\00"
declare i32 @printf(i8*,...)

define i32 @main() {
B0:
  %L0 = add i32 1, 1
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmti32, i32 0, i32 0), i32 %L0)
  ret i32 0
}
