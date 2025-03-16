
    bits 64

    section .bss
        vyskyty resd 256
    ; variables

    section .text
        global bubble
        ;rdi = *tp_int, rsi = t_N
        ;rax = void
        global strup
        ; rdi = *t_str
        global nejcastejsi
        ; rdi = *t_str
        global prvocislo
        ; rdi = t_cislo
        ; rax = -1 or t_cislo


bubble:
    dec rsi ; length -1
    .loop:
        xor r8, r8 ; change = 0
        xor r9, r9 ; i = 0

        .for:
            cmp r9, rsi    ; i < length-1
            jge .next_loop

            movsx r10, DWORD [rdi + r9*4]   ; r10 = pole[i]
            movsx r11, DWORD [rdi + r9*4+4] ; r11 = pole[i+1]

            cmp r10, r11  ; pole[i] > pole[i+1]
            jle .next_for

            mov DWORD [rdi + r9*4],   r11d ; pole[i] = pole[i+1]
            mov DWORD [rdi + r9*4+4], r10d ; pole[i+1] = pole[i]
            mov r8,                   1

            .next_for:
                inc r9   ;                         ;i++
                jmp .for

    
    .next_loop:
        cmp r8, 1 ; change == 1
        je  .loop

    ret

strup:
    mov al, 'A'
    sub al, 'a'

    .for:
        cmp BYTE [rdi], 0
        je  .end

        cmp BYTE [rdi], 'a'
        jb  .not_lower

        cmp BYTE [rdi], 'z'
        ja  .not_lower

        add BYTE [rdi], al


        .not_lower:
            inc rdi
            jmp .for

    .end:
        ret
        
nejcastejsi:
    xor   r8, r8 ; i = 0
    .for:        ; vynulovani pole
        cmp r8, 256
        jge .loop

        mov DWORD [vyskyty + r8*4], 0
        inc r8
        jmp .for

    .loop:
        cmp BYTE [rdi], 0
        je  .end

        mov   al,  BYTE [rdi] ;znak = str[i]
        movzx rax, al

        inc DWORD [vyskyty + rax*4] ; vyskyty[znak]++


        .next:
            inc rdi
            jmp .loop

    .end:
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

        .quit:
            ret

prvocislo:
    mov r8, 2   ;i = 2
    mov r9, rdi ; temp = t_cislo

    .for:
        cmp r8, rdi ; i < t_cislo
        jge .end

        mov rax, r9 ; delenec
        xor rdx, rdx
        IDIV r8d
        ; rax obsahuje vysledek
        ; rdx obsahuje zbytek
        cmp rdx, 0
        je .found

        inc r8
        jmp .for

    .found:
        mov rax, -1     ; not prime number
        ret
    .end:
        mov rax, r9     ; prime number
        ret

