global main
extern printf, scanf

section .data
  ok:
    db "ok", 10, 0
  print_get_num:
    db "Enter init value: ", 0
  print_entered:
    db "You have entered: %d", 10, 0
  print_banknote:
    db "Banknote %d: %dx", 10, 0
  int_format:
    db "%d", 0
  banknote_values dd 5000, 2000, 1000, 500, 200, 100
  banknote_num db 6


section .bss
  number resd 1


section .text
main:
  push rbp
  mov rbp, rsp
  sub rsp, 16

  ; Print get number message
  mov eax, 0
  lea rdi, [print_get_num]
  call printf

  ; get number
  mov eax, 0
  lea rdi, [int_format]
  lea rsi, [number]
  call scanf

  ; move number into register
  mov r12d, [number]

  ; print entered number
  mov eax, 0
  lea rdi, [print_entered]
  mov esi, r12d
  call printf

  ; set index register
  mov r13d, 0

loop_banknotes:
  mov r15d, [banknote_values + r13d*4]    ; Get current banknote
  mov r8d, 0    ; Set init banknote count to zero

  mov edi, r15d   ; Move banknote value into function params
  mov esi, r12d   ; Move init value into function params
  call count_banknote
  mov r12d, esi   ; Move updated init value to its register
  mov r8d, eax    ; Move banknote count into register 

next_banknote:
  mov esi, r15d
  mov edi, r8d
  call print_result

  ; Move to next banknote
  inc r13d

  ; Move number of banknotes into register
  mov r14d, [banknote_num]

  cmp r13d, r14d
  jl loop_banknotes

  add rsp, 16
  call exit


; Function: banknote
; Params:
;   edi - banknote value
;   esi - init value
; Returns:
;   eax - banknote count
;   esi - new value

count_banknote:
  push rbp
  mov rbp, rsp

  mov eax, 0    ; Set starting count

  cmp esi, edi  ; Compare init value with banknote value
  jl exit       ; if it is smaller, return

count_loop:
  sub esi, edi
  inc eax     ; increase count
  cmp esi, edi  ; If value is smaller than banknote, return
  jge count_loop

  call exit

; Function: print_result
; Params:
;   esi - banknote value
;   edi - banknote count
print_result:
  push rbp
  mov rbp, rsp

  mov eax, 0
  mov edx, edi    ; banknote count
  lea rdi, [print_banknote]
  call printf

  call exit

exit:
  leave
  ret

