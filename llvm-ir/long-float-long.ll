@.str1 = private unnamed_addr constant [13 x i8] c"ge 43e     \0A\00", align 1
@.str2 = private unnamed_addr constant [13 x i8] c"!ge 43e    \0A\00", align 1
@.str3 = private unnamed_addr constant [13 x i8] c"!lt c3e    \0A\00", align 1
@.str4 = private unnamed_addr constant [13 x i8] c"lt c3e     \0A\00", align 1
declare i32 @printf(i8*,...)

define i32 @exact.example.Example.main.s32.0(i64 %thr) {
B0:
  %L0 = sitofp i64 9223372036854775806 to float
  %L1 = fcmp oge float %L0, 0x43e0000000000000
  %L3 = fptosi double 0x43e0000000000000 to i64
  %L4 = fptosi float 0xc3e0000000000000 to i64
  %L5 = fptosi float %L0 to i64
  %L6 = fcmp olt float %L0, 0xc3e0000000000000
  br i1 %L1, label %B1, label %B2
B1:
  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([13 x i8], [13 x i8]* @.str1, i64 0, i64 0))
  br label %B3
B3:
  %L2 = phi i64 [ %L3, %B1 ], [ %L4, %B4 ], [ %L5, %B2 ]
  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([13 x i8], [13 x i8]* @.str3, i64 0, i64 0))
  %L7 = sub i64 9223372036854775806, %L2
  %L8 = icmp slt i64 %L7, -1
  %L9 = icmp sgt i64 %L7, -1
  %LA = select i1 %L9, i32 1, i32 0
  %LB = select i1 %L8, i32 -1, i32 %LA
  %LC = icmp ne i32 %LB, 0
  br i1 %LC, label %B5, label %B6
B4:
  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([13 x i8], [13 x i8]* @.str4, i64 0, i64 0))
  br label %B3
B2:
  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([13 x i8], [13 x i8]* @.str2, i64 0, i64 0))
  br i1 %L6, label %B4, label %B3
B5:
  br label %B7
B7:
  %LD = phi i32 [ 46, %B6 ], [ 70, %B5 ]
  %LE = call i32 (i32) @putchar(i32 %LD)
  %LF = call i32 (i32) @putchar(i32 10)
  ret i32 0
B6:
  br label %B7
}

define i32 @main() {
B0:
  %L0 = call i32 (i64) @exact.example.Example.main.s32.0(i64 zeroinitializer)
  ret i32 %L0
}

declare i32 @putchar(i32)
define void @exact.example.Example.main.v.1.ref.ref_array.ref.class.java-lang-String(i64 %thr, i64 %p0) {
B0:
  ret void
}
