target triple = "x86_64-pc-linux-gnu"

@pfmtf = constant [4 x i8] c"%a\0A\00"
@pfmti = constant [4 x i8] c"%d\0A\00"
declare i32 @printf(i8*,...)

declare float @llvm.experimental.constrained.sitofp.f32.i32(i32, metadata, metadata)

define i32 @main() {
B0:
  %L0 = sitofp i32 2147483646 to float
  %L01 = fpext float %L0 to double
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtf, i32 0, i32 0), double %L01)
  %L00 = fptosi float %L0 to i32
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmti, i32 0, i32 0), i32 %L00)

  %L1 = call float @llvm.experimental.constrained.sitofp.f32.i32(i32 2147483646, metadata !"round.tonearest", metadata !"fpexcept.strict")
  %L11 = fpext float %L1 to double
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtf, i32 0, i32 0), double %L11)
  %L10 = fptosi float %L1 to i32
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmti, i32 0, i32 0), i32 %L10)

  %L2 = call float @llvm.experimental.constrained.sitofp.f32.i32(i32 2147483646, metadata !"round.dynamic", metadata !"fpexcept.strict")
  %L21 = fpext float %L2 to double
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtf, i32 0, i32 0), double %L21)
  %L20 = fptosi float %L2 to i32
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmti, i32 0, i32 0), i32 %L20)

  %L3 = call float @llvm.experimental.constrained.sitofp.f32.i32(i32 2147483646, metadata !"round.downward", metadata !"fpexcept.strict")
  %L31 = fpext float %L3 to double
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtf, i32 0, i32 0), double %L31)
  %L30 = fptosi float %L3 to i32
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmti, i32 0, i32 0), i32 %L30)

  %L4 = call float @llvm.experimental.constrained.sitofp.f32.i32(i32 2147483646, metadata !"round.upward", metadata !"fpexcept.strict")
  %L41 = fpext float %L4 to double
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtf, i32 0, i32 0), double %L41)
  %L40 = fptosi float %L4 to i32
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmti, i32 0, i32 0), i32 %L40)

  %L5 = call float @llvm.experimental.constrained.sitofp.f32.i32(i32 2147483646, metadata !"round.towardzero", metadata !"fpexcept.strict")
  %L51 = fpext float %L5 to double
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtf, i32 0, i32 0), double %L51)
  %L50 = fptosi float %L5 to i32
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmti, i32 0, i32 0), i32 %L50)

  %L6 = call float @llvm.experimental.constrained.sitofp.f32.i32(i32 2147483646, metadata !"round.tonearestaway", metadata !"fpexcept.strict")
  %L61 = fpext float %L5 to double
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmtf, i32 0, i32 0), double %L61)
  %L60 = fptosi float %L6 to i32
  call i32(i8*,...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @pfmti, i32 0, i32 0), i32 %L60)

  ret i32 0
}
