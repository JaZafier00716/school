#include <stdio.h>
#include <stdlib.h>

void allocate_2d(char ***array, unsigned long rows, unsigned long cols);
void deallocate_2d(char ***array, unsigned long rows);

typedef struct
{
    unsigned long turtle_row, turtle_col;
    char sym;
    int turtle_direction; // 0 = right, 1 = bottom, 2 = left, 3 = top
} Turtle;

int main()
{
    unsigned long cols, rows;
    char **arr, action;
    Turtle *t;
    unsigned long turtle_number = 1;

    t = (Turtle *)malloc(sizeof(Turtle) * turtle_number);

    // Turtle init
    t[turtle_number - 1].sym = 'z';
    t[turtle_number - 1].turtle_direction = 0;
    t[turtle_number - 1].turtle_row = 0;
    t[turtle_number - 1].turtle_col = 0;

    scanf("%lu %lu", &rows, &cols);

    allocate_2d(&arr, rows, cols);

    arr[t[0].turtle_row][t[0].turtle_col] = t[0].sym;

    do
    {
        scanf(" %c", &action); // Notice the space before %c to ignore whitespace

        switch (action)
        {
        case 'x':
            // Print the canvas
            for (unsigned long i = 0; i < rows; i++)
            {
                for (unsigned long j = 0; j < cols; j++)
                {
                    printf("%c", arr[i][j]);
                }
                printf("\n");
            }
            break;
        case 'r':
            // Turn all turtles to the right
            for (unsigned long i = 0; i < turtle_number; i++)
            {
                t[i].turtle_direction = (t[i].turtle_direction + 1) % 4;
            }
            break;
        case 'l':
            // Turn all turtles to the left
            for (unsigned long i = 0; i < turtle_number; i++)
            {
                t[i].turtle_direction = (t[i].turtle_direction - 1 + 4) % 4;
            }
            break;
        case 'o':
            // Draw or erase on the canvas
            for (unsigned long i = 0; i < turtle_number; i++)
            {
                if (arr[t[i].turtle_row][t[i].turtle_col] == 'o')
                {
                    arr[t[i].turtle_row][t[i].turtle_col] = '.';
                }
                else
                {
                    arr[t[i].turtle_row][t[i].turtle_col] = 'o';
                }
            }
            break;
        case 'f':
            // Add a new turtle if there are fewer than 3 turtles
            if (turtle_number < 3)
            {
                turtle_number++;
                t = (Turtle *)realloc(t, sizeof(Turtle) * turtle_number); // Use realloc to keep old turtles

                t[turtle_number - 1].sym = 'z';
                t[turtle_number - 1].turtle_direction = 0;
                t[turtle_number - 1].turtle_row = 0;
                t[turtle_number - 1].turtle_col = 0;
            }
            break;
        case 'm':
            // Move all turtles in their current direction
            for (unsigned long i = 0; i < turtle_number; i++)
            {
                // Erase the turtle from the current position
                arr[t[i].turtle_row][t[i].turtle_col] = '.';

                // Move the turtle in its direction
                switch (t[i].turtle_direction)
                {
                case 0: // Right
                    t[i].turtle_col = (t[i].turtle_col + 1) % cols;
                    break;
                case 1: // Down
                    t[i].turtle_row = (t[i].turtle_row + 1) % rows;
                    break;
                case 2: // Left
                    t[i].turtle_col = (t[i].turtle_col - 1 + cols) % cols;
                    break;
                case 3: // Up
                    t[i].turtle_row = (t[i].turtle_row - 1 + rows) % rows;
                    break;
                }

                // Place the turtle in the new position
                arr[t[i].turtle_row][t[i].turtle_col] = t[i].sym;
            }
            break;
        default:
            break;
        }
    } while (action != 'x');

    deallocate_2d(&arr, rows);
    free(t);

    return 0;
}

void allocate_2d(char ***array, unsigned long rows, unsigned long cols)
{
    *array = (char **)malloc(sizeof(char *) * rows);

    for (unsigned long i = 0; i < rows; i++)
    {
        (*array)[i] = (char *)malloc(sizeof(char) * cols);

        for (unsigned long j = 0; j < cols; j++)
        {
            (*array)[i][j] = '.';
        }
    }
}

void deallocate_2d(char ***array, unsigned long rows)
{
    for (unsigned long i = 0; i < rows; i++)
    {
        free((*array)[i]);
    }
    free(*array);
    *array = NULL;
}
