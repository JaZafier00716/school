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

int main()
{
  srand((unsigned int)time(NULL));
  IMG_Init(IMG_INIT_PNG);
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
          if (snowflake_num == 1)
          {
            snowflakes = malloc(sizeof(TSnowflake));
            if (!snowflakes)
            {
              perror("unable to allocate memory\n");
              free(snowflakes);
              snowflakes = NULL;
              SDL_DestroyTexture(snowflake);
              sdl_context_free(&context);
              IMG_Quit();
              SDL_Quit();
              return 1;
            }
          }
          else
          {
            TSnowflake *tmp = realloc(snowflakes, sizeof(TSnowflake) * snowflake_num);
            if (!tmp)
            {
              perror("unable to allocate memory\n");
              free(snowflakes);
              snowflakes = NULL;
              SDL_DestroyTexture(snowflake);
              sdl_context_free(&context);
              IMG_Quit();
              SDL_Quit();
              return 1;
            }
            else
            {
              snowflakes = tmp;
            }
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
      snowflakes[i].dstrect.y += snowflakes[i].speed;
      if (snowflakes[i].dstrect.y > HEIGHT)
      {
        snowflake_num--;
        for (int j = i; j < snowflake_num; j++)
        {
          snowflakes[j] = snowflakes[j + 1];
        }
        if (snowflake_num > 0)
        {
          TSnowflake *tmp = realloc(snowflakes, sizeof(TSnowflake) * snowflake_num);
          if (!tmp)
          {
            perror("could not reallocate memory\n");
            free(snowflakes);
            snowflakes = NULL;
            SDL_DestroyTexture(snowflake);
            sdl_context_free(&context);
            IMG_Quit();
            SDL_Quit();
            return 1;
          }
          snowflakes = tmp;
        }
        else
        {
          free(snowflakes);
          snowflakes = NULL;
        }

        printf("%d\n", snowflake_num);
        i--;
      }
      if (snowflake_num > 0)
      {
        SDL_RenderCopyEx(context.renderer, snowflake, NULL, &(snowflakes[i].dstrect), snowflakes[i].rotation, &(snowflakes[i].center), SDL_FLIP_NONE);
      }
    }

    SDL_RenderPresent(context.renderer);
  }
  sdl_context_free(&context);
  SDL_DestroyTexture(snowflake);
  free(snowflakes);
  snowflakes = NULL;
  IMG_Quit();
  SDL_Quit();
  return 0;
}