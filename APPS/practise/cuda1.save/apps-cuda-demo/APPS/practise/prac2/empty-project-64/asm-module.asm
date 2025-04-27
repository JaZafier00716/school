bits 64

section .data
    
section .text
    global uprav_pole
    ; rdi = *t_pole, rsi = t_N
    ; rax = void (return value)
    global hledej_max_msb
    ; rdi = *t_pole, rsi = t_N
    ; rax = max value
    global pocet_cislic
    ; rdi = char *t_str
    global nahrada_mezer
    ; rdi = char *t_str, rsi = char t_nahradni_znak


uprav_pole:
    xor r10d, r10d  ; i = 0

    .loop:
        mov eax, DWORD [rdi + r10 * 4]   ; eax = pole[i]
        test eax, eax
        je .zaporne

        ; 0x111111110
        and eax, 0xFF_FF_FF_FE
        mov DWORD [rdi + r10*4], eax
        jmp .next

    .zaporne:
        or eax, 0x00_00_00_01
        mov DWORD [rdi + r10*4], eax

    .next:
        inc r10d
        cmp r10d, esi   ; i < t_n -> jmp
        jl .loop
    .end:

        ret

hledej_max_msb:
    xor r10, r10         ; i = 0
    xor r11, r11         ; max_index = 0

    mov rax, QWORD [rdi] ; Load pole[0]
    shr rax, 56          ; MSB -> LSB
    mov bl, al           ; max = pole[0]

    .loop:
        inc r10
        cmp r10, rsi
        jge .end             ; If i >= size, exit

        mov rax, QWORD [rdi + r10 * 8]  ; Load pole[i]
        shr rax, 56                      ; Extract MSB
        cmp bl, al
        jge .not_greater

        mov bl, al          ; Update max MSB value
        mov r11, r10         ; Update max_index

    .not_greater:
        jmp .loop

    .end:
        mov rax, r11         ; Return max_index
        ret

pocet_cislic:
    xor r8, r8    ; i = 0
    xor eax, eax    ;ret = 0

    .loop:
        mov bl, BYTE [rdi + r8]
        cmp bl, 0
        je .end

        cmp bl, '0'
        jl .not_number

        cmp bl, '9'
        jg .not_number

        ; is number
        inc eax

    .not_number:

        inc r8
        jmp .loop

    .end:
        ret

nahrada_mezer:
    xor r8, r8  ; i = 0

    .loop:
        mov bl, BYTE [rdi + r8]
        cmp bl, 0
        je .end

        cmp bl, ' '
        jne .not_space

        mov rax, rsi

        mov BYTE [rdi + r8], al
    .not_space:
        inc r8
        jmp .loop

    .end:
        ret