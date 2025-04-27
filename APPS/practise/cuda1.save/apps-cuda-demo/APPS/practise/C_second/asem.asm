global main
extern printf, scanf

section .data
main:
	push rbp
	mov rbp, rsp
	sub rsp, 16

	mov exp, 0
	lea rdi, "hello world"
	call printf


	add rsp, 16
	call exit




exit:
	leave
	ret


