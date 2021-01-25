define i32 @exact.example.Example.main.s32.0(i64 %thr) {
B0:
  call void (i64) @exact.example.Example.errors.v.0(i64 %thr)
  ret i32 0
}

define i32 @main() {
B0:
  %L0 = call i32 (i64) @exact.example.Example.main.s32.0(i64 zeroinitializer)
  ret i32 %L0
}

declare i32 @putchar(i32)
define void @exact.example.Example.errors.v.0(i64 %thr) {
B0:
  %L0 = fsub float 0xfff0000000000000, 0xfff0000000000000
  %L1 = fcmp ult float %L0, %L0
  %L2 = fcmp ugt float %L0, %L0
  ; %L1 = fcmp olt float %L0, %L0
  ; %L2 = fcmp ogt float %L0, %L0
  %L3 = select i1 %L2, i32 1, i32 0
  %L4 = select i1 %L1, i32 -1, i32 %L3
  %L5 = icmp eq i32 %L4, 0
  br i1 %L5, label %B1, label %B2
B1:
  br label %B3
B3:
  %L6 = phi i32 [ 46, %B2 ], [ 70, %B1 ]
  %L7 = call i32 (i32) @putchar(i32 %L6)
  ret void
B2:
  br label %B3
}

define void @exact.example.Example.main.v.1.ref.ref_array.ref.class.java-lang-String(i64 %thr, i64 %p0) {
B0:
  ret void
}