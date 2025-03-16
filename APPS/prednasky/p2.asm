bits 64


section .data


section .text
  global sum_int
  ; rdi rsi rdx rcx r8 r9
  ;  a   b
  global sum_long
  global asm_strlen
  ; *str
  global asm_otoc_znamenko
  global asm_max_long


; sum_int:
;   mov eax, edi    ; ret = a
;   add eax, esi    ; ret += b
  
;   ret

sum_long:
sum_int:
  mov rax, rdi    ; ret = a
  add rax, rsi    ; ret += b
  
  ret

asm_strlen:
  enter 0,0

  mov rax, 0      ; len = 0

.zpet:
  ; cmp [rdi], byte 0 ; while(*str != '\0')  ; porovnavani adresy daneho bytu (znaku)
  cmp [rdi+rax], byte 0 ; while(str[len] != '\0');
  je .hotovo
  ; inc rdi
  inc rax
  jmp .zpet

.hotovo:
  ; ret = eax
  leave
  ret
  

asm_otoc_znamenko:
  movsx rsi, esi  ; i = extend(N)
  mov rcx, 0
.back:
  cmp rcx, rsi    ; i < N
  jge .done

  neg dword [rdi + rcx * 4]   ; pole[i] = -pole[i];

  inc rcx               ; i++;
  jmp .back

.done:
  
  ret

asm_max_long:

asm_max_long:
  movsx rsi, esi  ; i = extend(N)
  mov rax, [ rdi + 0 ]  ; max = pole[0];
  mov rcx, 0
.back:
  cmp rcx, rsi
  jge .done

  cmp rax, [rdi + rcx * 8]    ; max ? pole[i]
  jg .nothing
  mov rax, [rdi + rcx * 8]    ; max = pole[i]
.nothing 
  inc rcx
  jmp .back




  inc rcx               ; i++;
  jmp .back

.done:
  
  ret