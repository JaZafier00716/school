#include "sdl.h"
#include "dynamic_array.h"
#include <SDL2/SDL_image.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#define MAX_SNOWFLAKE_SIZE 200
#define MIN_SNOWFLAKE_SIZE 10
#define WIDTH 800
#define HEIGHT 600

typedef struct
{
  double rotation;
  SDL_Rect dstrect;
  SDL_Point center;
  int speed;
} TSnowflake;

int allocate_snowflake(TSnowflake **snowflakes, int pocet)
{
  if (pocet == 1)
  {
    *snowflakes = (TSnowflake *)malloc(sizeof(TSnowflake));
  }
  else
  {
    TSnowflake *tmp = realloc(*snowflakes, sizeof(TSnowflake) * pocet);
    if (!tmp)
    {
      return 0;
    } else {
      *snowflakes = tmp;
    }
  }
  return 1;
}

void free_all(TSnowflake **snowflakes, SDL_Context *context, SDL_Texture *snowflake)
{
  SDL_DestroyTexture(snowflake);
  sdl_context_free(context);
  free(*snowflakes);
  snowflakes = NULL;
  IMG_Quit();
  SDL_Quit();
}

int remove_snowflake(TSnowflake *snowflakes, int pocet, int snowflake_id)
{
  if (pocet > 1)
  {
    for (int i = snowflake_id; i < pocet - 1; i++)
    {
      snowflakes[i] = snowflakes[i + 1];
    }
    if (!allocate_snowflake(&snowflakes, pocet - 1))
    {
      return -1;
    }
  }
  else
  {
    free(snowflakes);
    snowflakes = NULL;
  }
  return pocet - 1;
}

int main()
{
  srand((unsigned int)time(NULL));
  IMG_Init(IMG_INIT_PNG);
  SDL_Init(SDL_INIT_VIDEO);

  int snowflake_num = 0;
  TSnowflake *snowflakes;
  SDL_Context context = sdl_context_init("snezeni", WIDTH, HEIGHT);
  SDL_Texture *snowflake = IMG_LoadTexture(context.renderer, "snowflake.svg");

  int running = 1;
  SDL_Event e;

  while (running)
  {

    while (SDL_PollEvent(&e) && running)
    {
      switch (e.type)
      {
      case SDL_QUIT:
        running = 0;
        break;
      case SDL_MOUSEBUTTONDOWN:
        if (e.button.button == SDL_BUTTON_LEFT)
        {
          snowflake_num++;
          if (!allocate_snowflake(&snowflakes, snowflake_num))
          {
            perror("unable to allocate memory\n");
            free_all(&snowflakes, &context, snowflake);
            return 1;
          }
          int size = rand() % (MAX_SNOWFLAKE_SIZE + 1 - MIN_SNOWFLAKE_SIZE) + MIN_SNOWFLAKE_SIZE;
          int rotation = rand() % 360;
          int speed = (rand() % 10) + 1;
          SDL_Rect dstrect = {
              .w = size,
              .h = size,
              .x = (int)(e.button.x - 0.5 * (size)),
              .y = (int)(e.button.y - 0.5 * (size))};
          SDL_Point center = {
              .x = size / 2,
              .y = size / 2};
          snowflakes[snowflake_num - 1].center = center;
          snowflakes[snowflake_num - 1].dstrect = dstrect;
          snowflakes[snowflake_num - 1].rotation = rotation;
          snowflakes[snowflake_num - 1].speed = speed;
          printf("%d\n", snowflake_num);
        }
        break;
      }
    }
    SDL_SetRenderDrawColor(context.renderer, 0, 0, 0, 1);
    SDL_RenderClear(context.renderer);
    for (int i = 0; i < snowflake_num; i++)
    {
      snowflakes[i].rotation += snowflakes[i].speed;
      snowflakes[i].dstrect.y += snowflakes[i].speed;
      if (snowflakes[i].dstrect.y > HEIGHT)
      {
        snowflake_num = remove_snowflake(snowflakes, snowflake_num, i);
        if (-1 == snowflake_num)
        {
          free_all(&snowflakes, &context, snowflake);
          return 1;
        }
        i--;
      }
      else
      {
        SDL_RenderCopyEx(context.renderer, snowflake, NULL, &(snowflakes[i].dstrect), snowflakes[i].rotation, &(snowflakes[i].center), SDL_FLIP_NONE);
      }
    }
    SDL_RenderPresent(context.renderer);
  }
  free_all(&snowflakes, &context, snowflake);
  return 0;
}