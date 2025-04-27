  bits 64

  section .data
    extern g_int_a, g_int_b
    extern g_int_rgb, g_char_r, g_char_g, g_char_b
    extern g_int_max
    extern g_int_sum, g_int_pole

  section .text
    global asm_funkce
    global asm_funkce2
    global asm_funkce3

asm_funkce3:
  ; for (int i=0; i < 10; i++)
  ;   suma += pole[i]

  xor eax, eax

; for (int i=0; i < 10; i++)
  mov rcx, 0  ; i = 0
.zpet:
  cmp rcx, 10 ; i < 10
  jge .hotovo

  ; for - body

  add eax, [g_int_pole + rcx * 4] ; suma += pole[i]


  inc rcx
  jmp .zpet

.hotovo
  mov [g_int_sum], eax
  ret

asm_funkce2:
  mov eax, [g_int_a]
  mov ebx, [g_int_b]
  mov [g_int_max], eax
  cmp ebx, eax
  jg .preskoc  ; a < b goto preskoc
  mov ebx, [g_int_a]
  mov [g_int_max], ebx
.preskoc: ; s . jsou lokalni navesti, bez tecky jsou globalni
  mov [g_int_max], eax




  ret

asm_funkce:
  ;mov [g_int_a], DWORD 3333

  ;mov eax,       6666
  ;mov [g_int_b], eax

  ; Switching variables:
  ; mov eax, [g_int_a]
  ; mov ebx, [g_int_b]
  ; mov [g_int_a], ebx
  ; mov [g_int_b], eax

  ; Null first byte in g_int_a
  xor al, al
  mov [g_int_a +3], al

  ; Switch first and last byte in g_int_b
  mov al, [g_int_b + 0]
  mov cl, [g_int_b + 3]
  mov [g_int_b + 0], cl
  mov [g_int_b + 3], al
  
  ; Saving bytes from g_int_rgb into individual byte variables
  mov al, [g_int_rgb +2]
  mov [g_char_r], al

  mov al, [g_int_rgb +1]
  mov [g_char_g], al

  mov al, [g_int_rgb +0]
  mov [g_char_b], al

  ret