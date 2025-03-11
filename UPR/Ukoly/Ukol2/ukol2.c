#include <stdio.h>

void shape(int obrazec, int a, int b);

int main()
{
    int obrazec = 0;
    int a = 0;
    int b = 0;

    scanf("%d%d%d", &obrazec, &a, &b);

    shape(obrazec, a, b);

    return 0;
}

void shape(int obrazec, int a, int b)
{
    int x = 0;

    switch (obrazec) {
    case 0:
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
        break;
    case 1:
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
        break;
    case 2:
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
        break;
    case 3:
        for (int i = 0; i < a; i++)
        {
            for (int j = i; j > 0; j--)
            {
                printf(" ");
            }
            printf("x");
            if(i < a-1) {
                printf("\n");
            }
        }
        break;
    case 4:
        for (int i = a-1; i >= 0; i--)
        {
            for (int j = i; j > 0; j--)
            {
                printf(" ");
            }
            printf("x");
            if(i > 0) {
                printf("\n");
            }
        }
        break;
    case 5:
        x=1;
        for (int i = a-1; i > 0; i--)
        {
            for (int j = i; j > 0; j--)
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
            printf("\n");
        }
        for(int i = 0; i < 2*a-1; i++) {
            printf("x");
        }    
        break;
    case 6:
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
        break;
    case 7:
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
        break;
    case 9:
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
        break;
    default:
        printf("Neznamy obrazec");
        break;
    }
    printf("\n");
}