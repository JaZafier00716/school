; Compile & run:  nasm -f elf64 asem.asm && gcc -m64 -no-pie asem.o -o asem && ./asem
; return value:   echo $?

global main
extern printf, scanf

section .data
  print_test: db "Banknote: %d index: %d", 10, 0
  entered: db "You have entered: %d", 10, 0
  get_num: db "Enter your amount: ", 0
  print_banknote: db "Banknote %d: %dx", 10, 0
  format_int: db "%d", 0
  bank_notes dd 5000, 2000, 1000, 500, 200, 100
  arr_num dd 6

section .bss
  number resd 1

section .text
main:
  push rbp
  mov rbp, rsp
  sub rsp, 16

  ; Print message for user
  mov eax, 0
  lea rdi, [get_num]
  call printf

  ; Get number
  mov eax, 0
  lea rdi, [format_int]
  lea rsi, [number]
  call scanf

  mov r14d, [number]  ; Move number to register
  
  ; Print entered number
  mov eax, 0
  lea rdi, [entered]
  mov esi, r14d
  call printf

  mov r12d, 0  ; Index in an array

loop:
  mov r15d, 0  ; Banknote counter
  mov r13d, [bank_notes + r12d*4]  ; Current element

  cmp r14d, r13d  ; if number < current_element
  jl next_element  ; skip this banknote

  ; Call count_banknotes
  mov edi, r14d
  mov esi, r13d
  call count_banknotes
  mov r15d, eax  ; Store result in r15d
  mov r14d, edi

next_element:
  ; Call print_result
  mov edi, r15d
  mov esi, r13d
  call print_result

  inc r12d
  mov ecx, [arr_num]
  cmp r12d, ecx
  jl loop

  add rsp, 16
  call exit

; Function: count_banknotes
; Parameters:
;   edi = amount
;   esi = banknote value
; Returns:
;   eax = count of banknotes
count_banknotes:
  push rbp
  mov rbp, rsp

  mov eax, 0  ; Initialize banknote count

count_loop:
  cmp edi, esi
  jl exit  ; If amount < banknote, return

  sub edi, esi
  inc eax
  jmp count_loop


; Function: print_result
; Parameters:
;   edi = banknote value
;   esi = count of banknotes
print_result:
  push rbp
  mov rbp, rsp

  mov edx, edi

  mov eax, 0
  lea rdi, [print_banknote]
  call printf

  call exit

exit:
  leave
  ret