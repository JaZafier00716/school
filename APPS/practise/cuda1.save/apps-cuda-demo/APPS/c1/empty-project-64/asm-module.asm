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
        extern g_pole
        extern g_char_ahoj
        extern g_int_1, g_int_2
        extern g_heslo
        extern g_char_heslo
    ; variables

    ;global g_some_asm_var
    ;extern g_some_c_var

;g_some_asm_var dd ?

;***************************************************************************

    section .text
        global switch_dot
        global switch_first_second
        global cut
        global heslo_to_char
switch_dot:
    mov BYTE [g_char_ahoj +4], '!' 

    ret

switch_first_second:
    mov rdi, [g_pole + 0*8]
    mov rsi, [g_pole + 1*8]
    mov [g_pole+0*8], rsi
    mov [g_pole+1*8], rdi

    ret

cut:
    mov edi, DWORD[g_pole+2*8+4]
    mov DWORD [g_int_1], edi


    mov edi, DWORD[g_pole+2*8]
    mov DWORD [g_int_2], edi

    ret

heslo_to_char:
    mov rdi, QWORD[g_heslo]
    mov QWORD[g_char_heslo], rdi
    mov [g_char_heslo+8], BYTE 0
    ret