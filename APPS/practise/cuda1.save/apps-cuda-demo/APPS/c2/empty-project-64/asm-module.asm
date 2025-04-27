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
        extern g_int_N
        extern g_int_mezer
    ; variables

    ;global g_some_asm_var
    ;extern g_some_c_var


    section .text
        global byte_sum
        ; rdi = *pole, rsi = N
        global count_mod
        ; rdi = *pole, rsi = N
        global find_max_spaces
        ; rdi = *str

byte_sum:
    mov rcx, 0      ; i = 0
.back:
    cmp rcx, rsi    ; i >= N
    jge .done

    mov r11d, 0      ; ret = 0
    mov al, [rdi + rcx*4 + 0];
    movzx r10d, al
    add r11d, r10d
    mov al, [rdi + rcx*4 + 1];
    movzx r10d, al
    add r11d, r10d
    mov al, [rdi + rcx*4 + 2];
    movzx r10d, al
    add r11d, r10d
    mov al, [rdi + rcx*4 + 3];
    movzx r10d, al
    add r11d, r10d

    mov [rdi + rcx * 4], r11d


    inc rcx
    jmp .back  
.done:
    ret

count_mod:
    mov eax, 0      ; ret = 0       - counts numbers divisible by 2^N
    mov r10, QWORD 0xFFFF_FFFF_FFFF_FFFF    ; 1111...1111
    mov cl, BYTE [g_int_N]

    shl r10, cl   ; 1111...1111 << g_int_N
    not r10        ; 1000...0000 -> 0111...1111
    mov rcx, 0      ; i = 0
.back:
    cmp rcx, rsi    ; i >= N
    jge .done 

    mov r11, QWORD [rdi + rcx*8]
    and r11, r10      ; 0111...1111 & pole[i]
    jnz .next                   ; if 0111...1111 & pole[i] == 0 -> is power
    inc eax     ;ret ++;


.next:
    inc rcx
    jmp .back

.done:
    ret


find_max_spaces:
    mov rcx, 0  ; i=0
    mov r11, 0  ; space count 
    mov r10, 0  ; max count 

.loop:
    mov al, [rdi + rcx]  ; Load character
    cmp al, 0            ; \0
    je .done

    cmp al, ' '          
    jne .not_space

    inc r11              ; Increase space count
    jmp .next_char

.not_space:
    cmp r11, r10        
    jle .reset_count
    mov r10, r11

.reset_count:
    mov r11, 0          

.next_char:
    inc rcx
    jmp .loop

.done:
    cmp r11, r10         
    jle .store_result
    mov r10, r11

.store_result:
    mov [g_int_mezer], r10  ; Store result
    ret
