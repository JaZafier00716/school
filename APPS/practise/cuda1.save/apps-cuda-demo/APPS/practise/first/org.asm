  global _start

  section .text
_start:
  ;sys_write
  mov rax, 1
  mov rdi, 1
  lea rsi, [hello_world]      ; load effective address
  mov rdx, 14                 ; length of string
  syscall

  ;sys_exit
  mov rax, 60
  mov rdi, 69
  syscall

  section .data
hello_world:
  db "Hello, World!", 10
