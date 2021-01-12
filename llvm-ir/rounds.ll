target triple = "x86_64-pc-linux-gnu"

@pfmtf = constant [4 x i8] c"%a\0A\00"
@pfmti = constant [4 x i8] c"%d\0A\00"
declare i32 @printf(i8*,...)

declare i32 @llvm.flt.rounds()
declare i32 @llvm.lround.i32.f32(float)
declare dso_local i32 @fesetround(i32) local_unnamed_addr #1

define i32 @main() {
B0:
  call i32 @showrounds() ; 1  - to nearest, ties to even
  call i32 @roundhigh()

  call i32 @fesetround(i32 1024)
  call i32 @showrounds() ; 3  - toward negative infinity
  call i32 @roundhigh()

  call i32 @fesetround(i32 3072)
  call i32 @showrounds(); 0  - toward zero
  call i32 @roundhigh()

  call i32 @fesetround(i32 2048)
  call i32 @showrounds(); 2  - toward positive infinity
  call i32 @roundhigh()

  ret i32 0
}

define i32 @showrounds() {
  %L0 = call i32 @llvm.flt.rounds()
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmti, i32 0, i32 0), i32 %L0)
  ret i32 0
}

define i32 @roundhigh() {
  %L0 = sitofp i32 2147483646 to float

  %L1 = fpext float %L0 to double
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtf, i32 0, i32 0), double %L1)

  %L2 = call i32 @llvm.lround.i32.f32(float %L0)
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmti, i32 0, i32 0), i32 %L2)

  ret i32 0
}