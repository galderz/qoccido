@.str = private unnamed_addr constant [13 x i8] c"hello world\0A\00", align 1
declare i32 @printf(i8*,...)

define i32 @main() {
B0:
  %L0 = sitofp i32 1 to float
  %L1 = fpext float %L0 to double
  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([13 x i8], [13 x i8]* @.str, i64 0, i64 0))
  ret i32 0
}
