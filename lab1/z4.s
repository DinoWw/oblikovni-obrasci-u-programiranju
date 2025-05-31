	.file	"z4.c++"
# GNU C++17 (GCC) version 14.2.1 20250207 (x86_64-pc-linux-gnu)
#	compiled by GNU C version 14.2.1 20250207, GMP version 6.3.0, MPFR version 4.2.1, MPC version 1.3.1, isl version isl-0.27-GMP

# GGC heuristics: --param ggc-min-expand=100 --param ggc-min-heapsize=131072
# options passed: -masm=att -mtune=generic -march=x86-64 -O0 -fno-asynchronous-unwind-tables -fno-exceptions -fno-rtti
	.text
	.section	.text._ZN13PlainOldClass3setEi,"axG",@progbits,_ZN13PlainOldClass3setEi,comdat
	.align 2
	.weak	_ZN13PlainOldClass3setEi
	.type	_ZN13PlainOldClass3setEi, @function
_ZN13PlainOldClass3setEi:
	pushq	%rbp	#
	movq	%rsp, %rbp	#,
	movq	%rdi, -8(%rbp)	# this, this
	movl	%esi, -12(%rbp)	# x, x
# z4.c++:5:   void set(int x){x_=x;};
	movq	-8(%rbp), %rax	# this, tmp98
	movl	-12(%rbp), %edx	# x, tmp99
	movl	%edx, (%rax)	# tmp99, this_2(D)->x_
# z4.c++:5:   void set(int x){x_=x;};
	nop	
	popq	%rbp	#
	ret	
	.size	_ZN13PlainOldClass3setEi, .-_ZN13PlainOldClass3setEi
	.section	.text._ZN9CoolClass3setEi,"axG",@progbits,_ZN9CoolClass3setEi,comdat
	.align 2
	.weak	_ZN9CoolClass3setEi
	.type	_ZN9CoolClass3setEi, @function
_ZN9CoolClass3setEi:
	pushq	%rbp	#
	movq	%rsp, %rbp	#,
	movq	%rdi, -8(%rbp)	# this, this
	movl	%esi, -12(%rbp)	# x, x
# z4.c++:20:   virtual void set(int x){x_=x;};
	movq	-8(%rbp), %rax	# this, tmp98
	movl	-12(%rbp), %edx	# x, tmp99
	movl	%edx, 8(%rax)	# tmp99, this_2(D)->x_
# z4.c++:20:   virtual void set(int x){x_=x;};
	nop	
	popq	%rbp	#
	ret	
	.size	_ZN9CoolClass3setEi, .-_ZN9CoolClass3setEi
	.section	.text._ZN9CoolClass3getEv,"axG",@progbits,_ZN9CoolClass3getEv,comdat
	.align 2
	.weak	_ZN9CoolClass3getEv
	.type	_ZN9CoolClass3getEv, @function
_ZN9CoolClass3getEv:
	pushq	%rbp	#
	movq	%rsp, %rbp	#,
	movq	%rdi, -8(%rbp)	# this, this
# z4.c++:21:   virtual int get(){return x_;};
	movq	-8(%rbp), %rax	# this, tmp100
	movl	8(%rax), %eax	# this_2(D)->x_, _3
# z4.c++:21:   virtual int get(){return x_;};
	popq	%rbp	#
	ret	
	.size	_ZN9CoolClass3getEv, .-_ZN9CoolClass3getEv
	.section	.text._ZN4BaseC2Ev,"axG",@progbits,_ZN4BaseC5Ev,comdat
	.align 2
	.weak	_ZN4BaseC2Ev
	.type	_ZN4BaseC2Ev, @function
_ZN4BaseC2Ev:
	pushq	%rbp	#
	movq	%rsp, %rbp	#,
	movq	%rdi, -8(%rbp)	# this, this
# z4.c++:11: class Base{
	leaq	16+_ZTV4Base(%rip), %rdx	#, _1
	movq	-8(%rbp), %rax	# this, tmp99
	movq	%rdx, (%rax)	# _1, this_3(D)->_vptr.Base
	nop	
	popq	%rbp	#
	ret	
	.size	_ZN4BaseC2Ev, .-_ZN4BaseC2Ev
	.weak	_ZN4BaseC1Ev
	.set	_ZN4BaseC1Ev,_ZN4BaseC2Ev
	.section	.text._ZN9CoolClassC2Ev,"axG",@progbits,_ZN9CoolClassC5Ev,comdat
	.align 2
	.weak	_ZN9CoolClassC2Ev
	.type	_ZN9CoolClassC2Ev, @function
_ZN9CoolClassC2Ev:
	pushq	%rbp	#
	movq	%rsp, %rbp	#,
	subq	$16, %rsp	#,
	movq	%rdi, -8(%rbp)	# this, this
# z4.c++:18: class CoolClass: public Base{
	movq	-8(%rbp), %rax	# this, _1
	movq	%rax, %rdi	# _1,
	call	_ZN4BaseC2Ev	#
# z4.c++:18: class CoolClass: public Base{
	leaq	16+_ZTV9CoolClass(%rip), %rdx	#, _2
	movq	-8(%rbp), %rax	# this, tmp100
	movq	%rdx, (%rax)	# _2, this_3(D)->D.3413._vptr.Base
# z4.c++:18: class CoolClass: public Base{
	nop	
	leave	
	ret	
	.size	_ZN9CoolClassC2Ev, .-_ZN9CoolClassC2Ev
	.weak	_ZN9CoolClassC1Ev
	.set	_ZN9CoolClassC1Ev,_ZN9CoolClassC2Ev
	.text
	.globl	main
	.type	main, @function
main:
	pushq	%rbp	#
	movq	%rsp, %rbp	#,
	pushq	%rbx	#
	subq	$40, %rsp	#,
# z4.c++:26: int main(){
	movq	%fs:40, %rax	# MEM[(<address-space-1> long unsigned int *)40B], tmp103
	movq	%rax, -24(%rbp)	# tmp103, D.3537
	xorl	%eax, %eax	# tmp103
# z4.c++:28:   Base* pb=new CoolClass;
	movl	$16, %edi	#,
	call	_Znwm@PLT	#
	movq	%rax, %rbx	# tmp104, _5
# z4.c++:28:   Base* pb=new CoolClass;
	movq	%rbx, %rdi	# _5,
	call	_ZN9CoolClassC1Ev	#
# z4.c++:28:   Base* pb=new CoolClass;
	movq	%rbx, -32(%rbp)	# _5, pb
# z4.c++:29:   poc.set(42);
	leaq	-36(%rbp), %rax	#, tmp105
	movl	$42, %esi	#,
	movq	%rax, %rdi	# tmp105,
	call	_ZN13PlainOldClass3setEi	#
# z4.c++:30:   pb->set(42);
	movq	-32(%rbp), %rax	# pb, tmp106
	movq	(%rax), %rax	# pb_7->_vptr.Base, _1
	movq	(%rax), %rdx	# *_1, _2
	movq	-32(%rbp), %rax	# pb, tmp107
	movl	$42, %esi	#,
	movq	%rax, %rdi	# tmp107,
	call	*%rdx	# _2
# z4.c++:31: }  
	movl	$0, %eax	#, _11
	movq	-24(%rbp), %rdx	# D.3537, tmp109
	subq	%fs:40, %rdx	# MEM[(<address-space-1> long unsigned int *)40B], tmp109
	je	.L9	#,
	call	__stack_chk_fail@PLT	#
.L9:
	movq	-8(%rbp), %rbx	#,
	leave	
	ret	
	.size	main, .-main
	.weak	_ZTV9CoolClass
	.section	.data.rel.ro.local._ZTV9CoolClass,"awG",@progbits,_ZTV9CoolClass,comdat
	.align 8
	.type	_ZTV9CoolClass, @object
	.size	_ZTV9CoolClass, 32
_ZTV9CoolClass:
	.quad	0
	.quad	0
	.quad	_ZN9CoolClass3setEi
	.quad	_ZN9CoolClass3getEv
	.weak	_ZTV4Base
	.section	.data.rel.ro._ZTV4Base,"awG",@progbits,_ZTV4Base,comdat
	.align 8
	.type	_ZTV4Base, @object
	.size	_ZTV4Base, 32
_ZTV4Base:
	.quad	0
	.quad	0
	.quad	__cxa_pure_virtual
	.quad	__cxa_pure_virtual
	.weak	__cxa_pure_virtual
	.ident	"GCC: (GNU) 14.2.1 20250207"
	.section	.note.GNU-stack,"",@progbits
