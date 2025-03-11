#include <stdio.h>
#define N 10

void getInputs(char *orientation, int *n, int *m);
void histogram(int n, int m, int pole[]);
void printHorizontal(int m, int pole[]);
void printVertical(int m, int pole[]);
int numLength(int num);

int main()
{
    int n, m;
    char orientation;
    int pole[N] = {0};

    getInputs(&orientation, &n, &m);

    switch (orientation)
    {
    case 'h':
        histogram(n, m, pole);
        printHorizontal(m, pole);
        break;
    case 'v':
        histogram(n, m, pole);
        printVertical(m, pole);
        break;
    default:
        printf("Neplatny mod vykresleni\n");
        return 1;
    }

    return 0;
}

void histogram(int n, int m, int pole[])
{
    int num;
    for (int i = 0; i < n; i++)
    {
        scanf("%d", &num);

        if (num < m || num > m + 8)
        {
            pole[9]++;
        }
        else
        {
            pole[num - m]++;
        }
    }
}

int numLength(int num)
{
    int i;

    if (num == 0)
    {
        return 1;
    }
    for (i = 0; num != 0; i++)
    {
        num /= 10;
    }

    return i;
}

void printHorizontal(int m, int pole[])
{
    for (int i = 0; i < N; i++)
    {
        if (numLength(m + i) < numLength(m + 8))
        {
            printf(" ");
        }
        if (i >= N - 1)
        {
            if (pole[N - 1] > 0)
            {
                printf("invalid:");
            }
            else
            {
                continue;
            }
        }
        else
        {
            printf("%d", m + i);
        }

        if (pole[i] > 0)
        {
            printf(" ");
        }

        for (int j = 0; j < pole[i]; j++)
        {
            printf("#");
        }
        printf("\n");
    }
}

void printVertical(int m, int pole[])
{
    int max = pole[0];
    for (int i = 0; i < N; i++)
    {
        if (pole[i] > max)
        {
            max = pole[i];
        }
    }

    for (int i = 0; i < max; i++)
    {
        for (int j = 0; j < N; j++)
        {
            if (j == 0)
            {
                if (pole[N] >= max - i)
                {
                    printf("#");
                }
            }
            else
            {
                if (pole[j - 1] >= max - i)
                {
                    printf("#");
                }
                else
                {
                    printf(" ");
                }
            }
        }
        printf("\n");
    }
    for (int i = 0; i < N; i++)
    {
        if (i == 0)
        {
            printf("i");
        }
        else
        {
            printf("%d", m + i - 1);
        }
    }
    printf("\n");
}

void getInputs(char *orientation, int *n, int *m)
{
    scanf("%c", orientation);

    if (*orientation == 'h' || *orientation == 'v')
    {
        scanf("%d %d", n, m);
    }
}