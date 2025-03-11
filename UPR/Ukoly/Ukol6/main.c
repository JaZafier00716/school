// gcc -g -fsanitize=address main.c -o main && ./main < ./tests/stdin.stdin
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAX 50

typedef struct
{
    int lower_num, upper_num, space_num;
} Tstr;

// void print_row(char row[])
// {
//     for (int i = 0; i < MAX; i++)
//     {
//         if (row[i] == '\n')
//         {
//             printf("/n");
//             continue;
//         }
//         if (row[i] == '\0')
//         {
//             printf("/0");
//             continue;
//         }
//         if (row[i] == ' ')
//         {
//             printf("_");
//             continue;
//         }
//         printf("%c", row[i]);
//     }
// }

void print_data(char row[], Tstr before, Tstr after)
{
    printf("%s\n", row);
    printf("lowercase: %d -> %d\n", before.lower_num, after.lower_num);
    printf("uppercase: %d -> %d\n", before.upper_num, after.upper_num);
    printf("spaces: %d -> %d\n", before.space_num, after.space_num);
}

int length(char row[])
{
    int i;
    for (i = 0; row[i] != '\0'; i++)
        ;
    return i;
}

int numLower(char row[])
{
    int num = 0;
    for (int i = 0; i < length(row); i++)
    {
        if (row[i] >= 'a' && row[i] <= 'z')
        {
            num++;
        }
    }
    return num;
}

int numUpper(char row[])
{
    int num = 0;
    for (int i = 0; i < length(row); i++)
    {
        if (row[i] >= 'A' && row[i] <= 'Z')
        {
            num++;
        }
    }
    return num;
}
int numSpaces(char row[])
{
    int num = 0;
    for (int i = 0; i < length(row); i++)
    {
        if (row[i] == ' ')
        {
            num++;
        }
    }
    return num;
}

void remove_spaces(char row[])
{
    int new_length = length(row);
    for (int i = 0; row[0] == ' '; i++) // remove spaces from start
    {
        for (int j = 0; j < new_length; j++)
        {
            row[j] = row[j + 1];
        }
        new_length--;
    }
    for (int i = new_length; i > 0 && (row[i] == ' ' || row[i] == '\n' || row[i] == '\0'); i--) // removes spaces from end
    {
        row[i] = '\0';
        new_length--;
    }
}

void remove_duplicates(char row[])
{
    int new_length = length(row);
    for (int i = 0; i < new_length; i++)
    {
        if (row[i] == row[i + 1])
        {
            for (int j = i; j < new_length; j++)
            {
                row[j] = row[j + 1];
            }
            row[new_length] = '\0';
            new_length--;
            i--;
        }
    }
}

void normalize(char row[])
{
    char new_row[MAX];
    char tmp[MAX];
    char *token = strtok(row, " ");

    memset(tmp, '\0', MAX);
    memset(new_row, '\0', MAX);

    while (token)
    {
        if (numUpper(token) == 0)
        {
            for (int i = 0; i < length(token); i++)
            {
                // is all in lowercase
                if (token[i] >= 'a' && token[i] <= 'z')
                {
                    tmp[i] = token[i] - 32;
                    continue;
                }
                if (token[i] != '\n')
                {
                    tmp[i] = token[i];
                }
            }
        }
        else
        {
            for (int i = 0; i < length(token); i++)
            {
                if (token[i] >= 'A' && token[i] <= 'Z' && i > 0)
                {
                    tmp[i] = token[i] + 32;
                    continue;
                }
                if (token[i] >= 'a' && token[i] <= 'z' && i == 0)
                {
                    tmp[i] = token[i] - 32;
                    continue;
                }
                tmp[i] = token[i];
            }
        }
        strcat(new_row,
  // for (int i = 0; i < MAX; i++)
  // {
  //   if (new_row[i] == '\0')
  //   {
  //     printf("/0");
  //     continue;
  //   }
  //   if (new_row[i] == '\n')
  //   {
  //     printf("/n");
  //     continue;
  //   }
  //   printf("%c", new_row[i]);
  // }
 tmp);
        token = strtok(NULL, " ");
        if (token)
        {
            new_row[length(new_row)] = ' ';
            memset(tmp, '\0', MAX);
        }
    }
    strcpy(row, new_row);
}

int main()
{
    char row[MAX];
    int n;
    Tstr prev, post;

    scanf("%d", &n);
    memset(row, '\0', MAX);

    while (fgets(row, sizeof(row), stdin) && n > 0)
    {
        if (row[0] == '\n')
        {
            continue;
        }
        prev.lower_num = numLower(row);
        prev.upper_num = numUpper(row);
        prev.space_num = numSpaces(row);
        remove_spaces(row);
        normalize(row);
        remove_duplicates(row);
        post.lower_num = numLower(row);
        post.upper_num = numUpper(row);
        post.space_num = numSpaces(row);
        print_data(row, prev, post);
        if (n > 1)
        {
            printf("\n");
        }

        n--;
        memset(row, '\0', MAX);
    }
    if (n > 0)
    {
        // print empty row
        post.lower_num = 0;
        post.space_num = 0;
        post.upper_num = 0;
        print_data("", post, post);
    }

    return 0;
}