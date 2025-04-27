global main
extern printf, scanf

section .text
main:
  ; Function prologue
  push rbp        ; Keep the stack aligned
  mov rbp, rsp
  sub rsp, 16     ; reserving memory for local variables. Must be 16 byte increments to maintain "clean" memory

  ; Function body

  ; printf(msg);
  mov eax, 0  ; Zeroing eax eax = 0
  lea rdi, [msg] ;first parameter
  call printf

  ; scanf(format, &num);
  mov eax, 0  ; clear AL (zero FP args in XMM registers)
  lea rdi, [format]
  lea rsi, [number]
  call scanf

  ; i=0;
  mov DWORD [rbp - 4], 0

loop:
  ; printf(msg2, i, num);
  mov edx, [number] ; third parameter
  mov rsi, [rbp-4]  ; second parameter
  lea rdi [msg2]    ; first parameter
  xor eax, eax
  call printf

  ; i++;
  mov ecx, DWORD [number] ; move variable (number) into register (ecx), for comparing with i ([rbp - 4])
  add DWORD [rbp - 4], 1  ; increment register (increment i)

  cmp [rbp - 4], ecx  ; compare i ([rbp - 4]) with number (ecx)
  jle loop  ; jump if i < number

  ; Function epilogue
  add rsp, 16
  leave           ; mov rsp, rbp, pop rbp
  ret

section .data
  msg: 
    db "Enter a number: ", 0  ; 0 for null
  msg2:
    db "Looping %d of %d", 10, 0  ; 10 for new line
  format:
    db "%d", 0

section .bss
    number resd 1 ; Reserve 4 bytes