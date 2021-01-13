@.str1 = private unnamed_addr constant [13 x i8] c"gt 41e     \0A\00", align 1
@.str2 = private unnamed_addr constant [13 x i8] c"!gt 41e    \0A\00", align 1
@.str3 = private unnamed_addr constant [13 x i8] c"!lt c1e    \0A\00", align 1
@.str4 = private unnamed_addr constant [13 x i8] c"lt c1e     \0A\00", align 1
declare i32 @printf(i8*,...)

define i32 @exact.example.Example.main.s32.0(i8* %thr) {
B0:
  %L0 = sitofp i32 2147483646 to float

  ; %L1 = fcmp oge float %L0, 0x41dfffffffc00000
  %L1 = fcmp oge float %L0, 0x41e0000000000000

  %L3 = fptosi double 0x41dfffffffc00000 to i32
  ; %L3 = fptosi float 0x41e0000000000000 to i32
  ; %L3 = add i32 2147483647, 0
  %L4 = fptosi float %L0 to i32
  %L5 = fcmp olt float %L0, 0xc1e0000000000000
  %L6 = fptosi float 0xc1e0000000000000 to i32
  br i1 %L1, label %B1, label %B2
B1:
  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([13 x i8], [13 x i8]* @.str1, i64 0, i64 0))
  br label %B3
B3:
  %L2 = phi i32 [ %L3, %B1 ], [ %L4, %B2 ], [ %L6, %B4 ]
  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([13 x i8], [13 x i8]* @.str3, i64 0, i64 0))
  %L7 = sub i32 2147483646, %L2
  %L8 = icmp ne i32 %L7, -1
  br i1 %L8, label %B5, label %B6
B2:
  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([13 x i8], [13 x i8]* @.str2, i64 0, i64 0))
  br i1 %L5, label %B4, label %B3
B4:
  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([13 x i8], [13 x i8]* @.str4, i64 0, i64 0))
  br label %B3
B5:
  br label %B7
B7:
  %L9 = phi i32 [ 49, %B6 ], [ 48, %B5 ]
  %LA = call i32 (i32) @putchar(i32 %L9)
  ret i32 0
B6:
  br label %B7
}

define i32 @main() {
B0:
  %L0 = call i32 (i8*) @exact.example.Example.main.s32.0(i8* null)
  ret i32 %L0
}

declare i32 @putchar(i32)
define void @exact.example.Example.main.v.1.ref.ref_array.ref.class.java-lang-String(i8* %thr, i8* %p0) {
B0:
  ret void
}

