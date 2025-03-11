#include <stdio.h>
#include <stdlib.h>

void full_rectangle(int a, int b);
void empty_rectangle(int a, int b);
void num_row_rectangle(int a, int b);
void backslash(int a, int b);
void slash(int a, int b);
void triangle(int a, int b);
void T(int a, int b);
void H(int a, int b);
void num_col_rectangle(int a, int b);

void neznamyObrazec(char text[]);


#include <stdio.h>

int main() {
    int obrazec = 0;
    int a = 0;
    int b = 0;
    void (*funciton_pointer[])(int, int) = {&full_rectangle, &empty_rectangle, &num_row_rectangle, &backslash, &slash, &triangle, &T, &H, &num_col_rectangle};

    scanf("%d%d%d", &obrazec, &a, &b);

    if(obrazec < 0 || obrazec > 9 || obrazec == 8) {
        neznamyObrazec("Neznamy obrazec\n");
        return 0;
    }
    if(obrazec == 9) {
        obrazec = 8;
    }

    (*funciton_pointer[obrazec])(a, b);
    printf("\n");
    return 0;
}

void full_rectangle(int a, int b) {
    for (int i = 0; i < b; i++)
        {
            for (int j = 0; j < a; j++)
            {
                printf("x");
            }
            if(i < b-1) {
                printf("\n");
            }
        }
}

void empty_rectangle(int a, int b) {
    for (int i = 0; i < a; i++)
        {
            printf("x");
        }
    printf("\n");
    for (int i = 0; i < b - 2; i++)
    {
        printf("x");
        for (int j = 0; j < a - 2; j++)
        {
            printf(" ");
        }
        printf("x\n");
    }
    for (int i = 0; i < a; i++)
    {
        printf("x");
    }
}

void num_row_rectangle(int a, int b) {
    int x = 0;
    for (int i = 0; i < a; i++)
    {
        printf("x");
    }
    printf("\n");
    for (int i = 0; i < b - 2; i++)
    {
        printf("x");
        for (int j = 0; j < a - 2; j++)
        {
            printf("%d", x);
            x++;
            if (x >= 10)
            {
                x = 0;
            }
        }
        printf("x\n");
    }  
    for (int i = 0; i < a; i++)
    {
        printf("x");
    }
}
void backslash(int a, int b) {
    for (int i = 0; i < a; i++)
    {
        for (b = i; b > 0; b--)
        {
            printf(" ");
        }
        printf("x");
        if(i < a-1) {
            printf("\n");
        }
    }
}

void slash(int a, int b) {
    for (int i = a-1; i >= 0; i--)
    {
        for (b = i; b > 0; b--)
        {
            printf(" ");
        }
        printf("x");
        for(int j = i; j < a-1; j++) {
            printf(" ");
        }
        if(i > 0) {
            printf("\n");
        }
    }
}

void triangle(int a, int b) {
    int x = 1;
    for (int i = a-1; i > 0; i--)
    {
        for (b = i; b > 0; b--)
        {
            printf(" ");
        }
        printf("x");
        
        if(i != a-1) {
            for (int j = 0; j < x; j++)
            {
                printf(" ");
            }
            x+=2;
            printf("x");         
        } 
        for (b = i; b > 0; b--)
        {
            printf(" "); 
        }
        printf("\n");
    }
    for(int i = 0; i < 2*a-1; i++) {
        printf("x");
    } 
}
void T(int a, int b) {
for (int i = 0; i < a; i++)
    {
        printf("x");
    }
    printf("\n");
    for (int i = 0; i < b-1; i++)
    {
        for (int i = 0; i < (a-1)/2 ; i++)
        {
            printf(" ");
        }
        printf("x");
        if(i < b-2) {
        printf("\n");
        }
    }  
}

void H(int a, int b) {
for (int i = 0; i < (b-1)/2; i++)
    {
        printf("x");
        for (int j = 0; j < a-2; j++)
        {
            printf(" ");
        }
        printf("x\n");
    }
    for (int i = 0; i < a; i++)
    {
        printf("x");
    }
    printf("\n");
    for (int i = 0; i < (b-1)/2; i++)
    {
        printf("x");
        for (int j = 0; j < a-2; j++)
        {
            printf(" ");
        }
        printf("x");
        if(i < (b-1)/2 -1) {
            printf("\n");
        }
    }
}


void num_col_rectangle(int a, int b) {
    int x = 0;
    for (int i = 0; i < a; i++)
    {
        printf("x");
    }
    printf("\n");
    for (int i = 0; i < b-2; i++)
    {
        printf("x");
        for (int j = 0; j < a-2; j++)
        {
            if(x + j*(b - 2) >= 10) {
                printf("%d", (x + j*(b-2)) % 10);
            } else {
                printf("%d", x + j*(b-2));
            }
        }
        x++;      
        printf("x\n");
    }
    for (int i = 0; i < a; i++)
    {
        printf("x");
    }
}

void neznamyObrazec(char text[]) {
    printf("%s", text);
}