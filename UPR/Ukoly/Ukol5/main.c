#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define MAX_T_NUM 3

/* =========================================== */
/* |   For '#' Bonus uncomment line 30       | */
/* |   For Animation search for 'animation'  | */
/* |   and uncomment/comment these lines     | */
/* =========================================== */

typedef struct
{
  int r, s;
  int dir;
  char prev_sym;
} Turtle;

void allocate_2d(char **a, int rows, int cols)
{
  int hash = 0; // init for tests

  *a = (char *)malloc(sizeof(char ) * (size_t)(rows * cols));

  for (int i = 0; i < rows; i++)
  {
    for (int j = 0; j < cols; j++)
    {
      // hash = rand() % 5; // <---- Uncomment this - init for bonus with #

      if (hash < 4) // 80 % chance for '.' and 20% chance for '#'
      {
        (*a)[i*cols + j] = '.';
        // (*a)[i][j] = ' '; // <--- Uncomment this for animation
      }
      else
      {
        (*a)[i*cols + j] = '#';
      }
    }
  }
}

void dealloc_2d(char **a)
{
  free((*a));
}

int create_turtle(Turtle t[], int t_num)
{
  t[t_num].dir = 0; // 0 = Right, 1 = Bottom, 2 = Left, 3 = Top
  t[t_num].prev_sym = '.';
  // t[t_num].prev_sym = ' '; // <--- Uncomment this for animation
  t[t_num].r = 0;
  t[t_num].s = 0;

  return t_num + 1;
}

void move_turtle(Turtle *t, int rows, int cols)
{
  switch (t->dir)
  {
  case 0:
    t->s = (t->s + 1) % cols;
    break;
  case 1:
    t->r = (t->r + 1) % rows;
    break;
  case 2:
    t->s = (t->s - 1 + cols) % cols;
    break;
  case 3:
    t->r = (t->r - 1 + rows) % rows;
    break;
  }
}

int main()
{
  srand((unsigned int)time(NULL));
  // char anim_string[] = {"ormomomomlmomomlmomomomormmormomomomollmmmmrmomrmomrmormmrmmmormomomomollmmmmrmomrmomrmolmlmomrmolmmmolmmomomox"}; // <--- Uncomment this for animation
  int rows, cols;
  char *playing_field;
  Turtle ts[MAX_T_NUM];
  int t_num = 0;

  char action;

  scanf("%d %d", &rows, &cols); // <--- Comment this for animation

  // rows = 5; // <--- Uncomment this for animation
  // cols = 16;// <--- Uncomment this for animation

  allocate_2d(&playing_field, rows, cols);

  t_num = create_turtle(ts, t_num);

  int z = 0;
  while (1)
  {
    scanf("%c", &action); // <--- Comment this for animation
    // action = anim_string[z]; // <--- Uncomment this for animation
    z++;
    if (action == 'x')
    {
      break;
    }

    switch (action)
    {
    case 'r':
    {
      for (int i = 0; i < t_num; i++)
      {
        if (ts[i].dir == 3)
        {
          ts[i].dir = 0;
        }
        else
        {
          ts[i].dir++;
        }
      }

      break;
    }
    case 'l':
    {
      for (int i = 0; i < t_num; i++)
      {
        if (ts[i].dir == 0)
        {
          ts[i].dir = 3;
        }
        else
        {
          ts[i].dir--;
        }
      }
      break;
    }
    case 'm':
    {
      for (int i = 0; i < t_num; i++)
      {
        int prev_r = ts[i].r;
        int prev_s = ts[i].s;

        playing_field[ts[i].r * cols + ts[i].s] = ts[i].prev_sym;
        move_turtle(&ts[i], rows, cols);
        if (playing_field[ts[i].r *cols + ts[i].s] == '#')
        { // if the next position is # return to the previous position
          ts[i].r = prev_r;
          ts[i].s = prev_s;
        }
        ts[i].prev_sym = playing_field[ts[i].r * cols + ts[i].s];
        playing_field[ts[i].r*cols + ts[i].s] = 'z';
      }
      break;
    }
    case 'o':
    {
      for (int i = 0; i < t_num; i++)
      {
        if (ts[i].prev_sym == '.' || ts[i].prev_sym == ' ')
        {
          ts[i].prev_sym = 'o';
        }
        else
        {
          ts[i].prev_sym = '.';
          // ts[i].prev_sym = ' '; // <--- Uncomment this for animation
        }
        playing_field[ts[i].r*cols + ts[i].s] = ts[i].prev_sym;
      }

      break;
    }
    case 'f':
    {
      if (t_num < MAX_T_NUM)
      {
        t_num = create_turtle(ts, t_num);
      }
      break;
    }
    }
    // printf("\x1b[2J\x1b[1;1F"); // <--- Uncomment this for animation
    // for (int i = 0; i < rows; i++) // <--- Unocomment this for animation
    // {
    //   for (int j = 0; j < cols; j++)
    //   {
    //     printf("%c", playing_field[i][j]);
    //   }
    //   printf("\n");
    // }
    // for (int i = 0, j =0; i < 16000000; i++) // <--- Uncomment this for animation -delay
    // {
    //   j++;
    // }
  }

  // Comment this for loop for animation
  for (int i = 0; i < rows; i++)
  {
    for (int j = 0; j < cols; j++)
    {
      printf("%c", playing_field[i*cols + j]);
    }
    printf("\n");
  }

  dealloc_2d(&playing_field);
  return 0;
}