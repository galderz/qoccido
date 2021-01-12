target triple = "x86_64-pc-linux-gnu"

@pfmta = constant [4 x i8] c"%a\0A\00"
@pfmtf = constant [4 x i8] c"%f\0A\00"
@pfmti = constant [4 x i8] c"%x\0A\00"
@pfmtd = constant [4 x i8] c"%d\0A\00"
declare i32 @printf(i8*,...)

declare float @llvm.experimental.constrained.sitofp.f32.i32(i32, metadata, metadata)

define i32 @main() {
B0:
  call i32 @veryhigh()
  call i32 @high()
  ret i32 0
}

define i32 @high() {
  %L0 = sitofp i32 1234567890 to float
  %L01 = fpext float %L0 to double
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtf, i32 0, i32 0), double %L01)
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmta, i32 0, i32 0), double %L01)
  %L1 = fptosi float %L0 to i32
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmti, i32 0, i32 0), i32 %L1)
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtd, i32 0, i32 0), i32 %L1)
  %L2 = sub i32 1234567890, %L1
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmti, i32 0, i32 0), i32 %L2)
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtd, i32 0, i32 0), i32 %L2)

  ret i32 0
}

define i32 @veryhigh() {
  %L0 = sitofp i32 2147483646 to float
  %L01 = fpext float %L0 to double
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtf, i32 0, i32 0), double %L01)
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmta, i32 0, i32 0), double %L01)
  %L1 = fptosi float %L0 to i32
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmti, i32 0, i32 0), i32 %L1)
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtd, i32 0, i32 0), i32 %L1)
  %L2 = sub i32 2147483646, %L1
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmti, i32 0, i32 0), i32 %L2)
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtd, i32 0, i32 0), i32 %L2)

  ret i32 0
}