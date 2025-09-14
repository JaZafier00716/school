    bits 64

    section .bss
        vyskyty resd 256 ;reserve DWORD 256x

;***************************************************************************

    section .text
        extern bubble
        ; rdi = int *tp_int,
        ; rsi = int t_N 
        extern strup
        ; rdi = char *t_str
        extern nejcastejsi
        ; rdi = char *t_str
        extern prvocislo
        ; long t_cislo


bubble:
    dec rsi ; length -1
    .loop:
        xor r8, r8 ; change = 0
        xor r9, r9 ; i = 0

        .for:
            cmp r9, rsi    ; i < length -1
            jge .next_loop

            movsx r10, DWORD [rdi + r9 * 4]     ; pole[i]
            movsx r11, DWORD [rdi + r9 * 4 + 4] ; pole[i+1]

            cmp r10, r11  ; pole[i] > pole[i+1]
            jle .next_for

            mov r8,                       1    ; change = 1
            mov DWORD [rdi + r9 * 4],     r11d ; pole[i] = pole[i+1]
            mov DWORD [rdi + r9 * 4 + 4], r10d ; pole[i+1] = pole[i]       r11d = 32b, r11 = 64b

        .next_for:
            inc r9
            jmp .for

    .next_loop:
        cmp r8, 1 ; if change == 1
        je  .loop


    ret

strup:
    mov al, 'A'
    sub al, 'a'
    ;al = difference to uppercase

    .for:
        cmp BYTE [rdi], 0 ; if pole[i] == '\0'
        je  .end

        ; if pole[i] < 'a'
        cmp BYTE [rdi], 'a'
        jb  .next_for

        ; if pole[i] > 'z'
        cmp BYTE [rdi], 'z'
        ja  .next_for

        add BYTE [rdi], al


    .next_for:
        inc rdi
        jmp .for

    .end:
    ret

nejcastejsi:
    xor r8, r8 ; i = 0
    .nullify:
        cmp r8, 256
        jge .end_nullifying

        mov DWORD [vyskyty + r8*4], 0
        inc r8
        jmp .nullify
    .end_nullifying:

    .for:
        cmp BYTE [rdi], 0 ; if pole[i] == '\0'
        je  .end_counting

        mov al, BYTE [rdi]
        movzx rax, al

        inc DWORD [vyskyty + rax * 4]


    .next_for:
        inc rdi
        jmp .for


    .end_counting:
        xor r8, r8  ; i = 0
        xor rax, rax ; ret = 0

        .for2:
            cmp r8, 256
            jge .end

            mov r9d, DWORD [vyskyty + rax * 4]      ; max_index

            cmp r9d, DWORD [vyskyty + r8 * 4]       ; if pole[i] > max_index
            CMOVl rax, r8

            inc r8
            jmp .for2
    .end:
        xor r8, r8

    ret

prvocislo:
    mov r8, 2       ; i = 2

    .for:
        cmp r8, rdi     ; i < rdi
        jge .end

        mov rax, rdi
        xor rdx, rdx    ; nullify remainder
        ; rax / r8d = rax (remainder: rdx)
        IDIV r8d        ; sizeof r8d must be 2x smaller than size of our number

        cmp rdx, 0      ; if remainder == 0
        je .found


        inc r8
        jmp .for

        .found:
            mov rax, -1
            ret

    .end:
        mov rax, rdi
        ret

