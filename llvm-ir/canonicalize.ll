target triple = "x86_64-pc-linux-gnu"

declare i32 @putchar(i32)
declare double @llvm.canonicalize.f64(double)

define i32 @main() {
B0:
  %L0 = call double @llvm.canonicalize.f64(double 0x3ffca3d70a3d70a4)
  %L1 = bitcast double %L0 to i64
  %L2 = icmp slt i64 4610740262505640100, %L1
  %L3 = icmp sgt i64 4610740262505640100, %L1
  %L4 = select i1 %L3, i32 1, i32 0
  %L5 = select i1 %L2, i32 -1, i32 %L4
  %L6 = icmp ne i32 %L5, 0
  br i1 %L6, label %B1, label %B2
B1:
  br label %B3
B3:
  %L7 = phi i32 [ 70, %B1 ], [ 46, %B2 ]
  %L8 = call i32 (i32) @putchar(i32 %L7)
  ret i32 0
B2:
  br label %B3
  ret i32 0
}

