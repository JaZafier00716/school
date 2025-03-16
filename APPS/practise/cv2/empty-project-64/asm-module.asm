;***************************************************************************
;
; Program for education in subject "Assembly Languages" and "APPS"
; petr.olivka@vsb.cz, Department of Computer Science, VSB-TUO
;
; Empty project
;
;***************************************************************************

    bits 64

    section .data
        extern g_int_pole
        extern SIZE

        extern g_int_N
        extern g_int_pocet
        extern g_long_pole

        extern g_char_str
        extern g_int_mezer
    ; variables

    ;global g_some_asm_var
    ;extern g_some_c_var

;g_some_asm_var dd ?

;***************************************************************************

    section .text
        global sum_bytes
        global count_pow
        global get_max_spaces
    ; functions

    ;global some_asm_function
    ;extern some_c_function

;some_asm_function:
    ;ret

sum_bytes:

    xor edi, edi    ; i = 0
.loop:
    xor esi, esi    ; sum = 0

    mov al, [g_int_pole + edi*4 + 0]  ;pole + i*4B + j
    movzx eax, al       ; extend to 4B
    add esi, eax    

    mov al, [g_int_pole + edi*4 + 1]  ;pole + i*4[B] + j[B]
    movzx eax, al       ; extend to 4B
    add esi, eax

    mov al, [g_int_pole + edi*4 + 2]  ;pole + i*4B + j
    movzx eax, al       ; extend to 4B
    add esi, eax

    mov al, [g_int_pole + edi*4 + 3]  ;pole + i*4B + j
    movzx eax, al       ; extend to 4B
    add esi, eax


    mov DWORD [g_int_pole + edi*4], esi

    inc edi
    mov r8d, DWORD [SIZE]
    cmp edi, r8d
    jl .loop

    ret

count_pow:

    mov cl, BYTE [g_int_N]          ; N

    mov ebx, DWORD 0xFFFF_FFFF                    ; mask = 1111...1111
    shl ebx, cl                     ; mask = 1111...1000...0000 - number of 0 = N
    not ebx                         ; mask = 0000...0111...1111

    xor edi, edi    ;i = 0
    xor ecx, ecx    ;count = 0

.loop:
    cmp edi, DWORD [SIZE]
    jge .done

    mov eax, [g_long_pole + edi*4]  ; reg = pole [i]

    test eax, ebx                    ; if pole[i] & mask == 0  -> inc
    jnz .next

    inc ecx                         ; count++

.next:
    inc edi
    jmp .loop

.done:
    mov [g_int_pocet], ecx

    ret

get_max_spaces:
    xor r8d, r8d    ; count = 0
    xor r9d, r9d    ; max_count = 0
    xor rdi, rdi    ; i = 0

.loop1:
    mov al, BYTE [g_char_str + rdi]

    cmp al, 0
    je .end     ; pole[i] = \0

    cmp al, " "
    jne .not_space

    inc r8d     ;count++;

    inc rdi     ; i++
    jmp .loop1
.not_space:
    cmp r8d, r9d    ; if count > max_count
    jle .not_greater

    mov r9d, r8d    ; max_count = count

.not_greater:
    xor r8d, r8d    ;count = 0
    inc rdi         ; i++
    jmp .loop1

.end:
    mov [g_int_mezer], r9d
    ret