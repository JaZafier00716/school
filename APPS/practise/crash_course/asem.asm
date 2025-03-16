global main

section .data		; define constant variables
; strings, magic numberrs, terminating strings

section .bss
; reserving space in memory for future data


section .text
; _start:
; main:

; nasm -f elf64 -g file.asm -> file.o
; gcc file.o -o file -m64 -no-pie

; registers: hardware implemented variables
; you have to move data into registers from variables before performing operations


; mov dest, src ; move data from src to dest
; movzx eax, byte ptr [ebx]	; grab 1 byte from address stored in ebx, store it in eax and fill the remaining space in eax with zeros (because eax has 32b/4B and we are storing in it 8b/1B)
; movsx dest, src		; if we take a  number that is smaller than the dest size, it extends it with sign value
; and, or, xor, neg, ...	; it can do all bitwise operations
; test eax, eax			; checks weather eax = 0

; add, sub, inc, dec		; it can do basic math operations
; mul, dev			; They are using rax and rbx as inputs and the result is stored in rax and in case of oveflow in rdx

;flag registers
; CF = carry (1 = yes, 0 = no)
; OF = overflow flag
; ZF = zero flag
; SF = 1 = negative, 0 = positive
; PF = parity (even, odd)


; jump operations
; jmp label
; je label 	; if the operation before resulted in equal
; jne label	; jump if not equal
; jz label	; jump if zero

; call
; call label	; uses stack

; cmp label, label2	;label1 - label2 - does not store it anywhere, you have to jump right after this operation, otherwise you would jump based on the operation in between
; shr eax, 4		; shift eax by 4 bits to the right
; shl eax, 4		; shift eax by 4 bits to the left
; sar			; shifts right, but keeps the sign bit
; sal			; sign extends after shift into left

; ror, rol		; rotates bits msb -> lsb or lsb -> msb based on direction and number of bits shifted

; system calls
; mov eax, 1	; sys_exit - based on number stored in eax, kernel knows what you want to do
; int 80h	; interupt - invokes kernel and figures out what you want to do based on data stored in registers
	; func(a, b, c)	; eax = func number in table, a = rdi, b = rsi, c = rdx

; Conditions:
; int x = 3;
; int a = 4;
; int b = 5;
; int c = 0;
;
; if (x == a) {
;	c = c + a;
; }
; else if (x == b) {
;	c = b;
; }
; else {
; 	c = 1;
; }

; First we must decide which register we will use for which variable:
;eax x
;ebx a
;ecx b
;edx c
;
; Conditions in assembly:
;
; main:
; 	cmp eax, ebx
; 	jne elseif
;	add edx, ebx
;
; elseif:
; 	cmp eax, ecx
; 	jne else
;	mov edx, ecx
;
; else:
;	mov edx, 1


; For loops:
; for (int i=0; i < 5; i++) {
; 	printf("%d", i);
; }

;section .text
;main:
;	; we know that rax, rdi, rsi and rdx are used for system calls so lets use other registers
;	; let r8d = i
;	xor r8d, r8d	; we can also mov 0, edi
;
;loop1:
;	add r8d, '0'
;
;	xor rax, 1	; sys_write
;	mov rdi, 1	; unsigned int fd
;	mov rsi, r8d	; const char *buf
;	mov rdx, 1	; size_t count
;	int 0x80
;
;	sub r8d, '0'
;	inc r8d
;	
;	cmp r8d, 5
;	jl loop1


; Printf in assembly:
; int printf (const char *format, ...);
; 
; printf("this is a test %d %d %d\n", a, b, c);

; extern printf

; section .data
; 	msg db "This is a test %d %d %d", 10, 0
; 	a db 3
; 	b db 5
; 	c db 8

; section .text

; main:
; 	mov eax, [c]
; 	push eax
; 	push [b]
; 	push [a]
; 	push msg
; 	; It has to be in reverse order, because it will be poped in reversed order (bottom up)
; 	call printf

; 	add esp, 16	;cleaning stack, because each stack address is 4B in size. So 16, because we are pushing 4 variables (each has 4B address)
; printf:
; 	push eax
; 	push ebx
; 	push ecx
; 	push edx

; 	push ebp
; 	mov ebp, esp
	
; 	mov eax, esp + 28 (we have to move down the stack by 4 addresses in order to access our data to be printed
; 	; [printing]

; 	pop ebp
; 	pop edx
; 	pop ecx
; 	pop ebx
; 	pop eax

	






