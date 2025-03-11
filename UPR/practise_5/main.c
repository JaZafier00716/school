#include <stdio.h>
#include <stdlib.h>
#define M 2 // R1
#define N 3 // C1
#define R 3 // R2
#define S 1 // C2

void allocate_2D(int ***a, int rows, int cols)
{
    *a = (int **)malloc(sizeof(int *) * rows);
    for (int i = 0; i < rows; i++)
    {
        (*a)[i] = (int *)malloc(sizeof(int) * cols);

        printf("&(a[%d]):\t%p\n", i, &(a[i]));
        printf("(a[%d]):\t\t%p\n\n", i, (a[i]));
    }
    printf("%p", a);
}

void deallocate_2d(int ***a, int rows)
{
    for (int i = 0; i < rows; i++)
    {
        free((*a)[i]);
    }
    free(*a);
    *a = NULL;
}

int nasobeni_matic(int **m1, int **m2, int **m)
{
    for (int i = 0; i < M; i++) // Radek
    {
        for (int j = 0; j < S; j++) // Sloupec
        {
            for (int k = 0; k < N; k++)
            {
                m[i][j] += m1[i][k] * m2[k][j];
            }
        }
    }
}

int main()
{

    int **m1, **m2, **m = { 0 };

    allocate_2D(&m1, 20, 10);
    // allocate_2D(&m2, R, S);
    // allocate_2D(&m, M, S);

    // for (int i = 0; i < M; i++)
    // {
    //     for (int j = 0; j < N; j++)
    //     {
    //         printf("Zadejte M1 [%d][%d]", i, j);
    //         scanf("%d", &m1[i][j]);
    //     }
    // }
    // for (int i = 0; i < R; i++)
    // {
    //     for (int j = 0; j < S; j++)
    //     {
    //         printf("Zadejte M2 [%d][%d]", i, j);
    //         scanf("%d", &m2[i][j]);
    //     }
    // }
    

    // nasobeni_matic(m1, m2, m);

    // for (int i = 0; i < M; i++)
    // {
    //     for (int j = 0; j < S; j++)
    //     {
    //         printf("%d  ", m[i][j]);
    //     }
    //     printf("\n");
    // }
    

    deallocate_2d(&m1, M);
    // deallocate_2d(&m2, R);
    // deallocate_2d(&m, M);
    return 0;
}