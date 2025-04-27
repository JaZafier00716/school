#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <SDL2/SDL.h>

char key_pressed(SDL_KeyboardEvent key)
{
  if (key.keysym.scancode == SDL_SCANCODE_L)
  {
    return 'l';
  }
  if(key.keysym.scancode == SDL_SCANCODE_K) {
    return 'k';
  }
}

int main()
{
  SDL_Init(SDL_INIT_VIDEO | SDL_INIT_AUDIO);

  SDL_Window *window = SDL_CreateWindow(
      "SuperGalakticka hra",
      SDL_WINDOWPOS_CENTERED,
      SDL_WINDOWPOS_CENTERED,
      500, 500,
      SDL_WINDOW_SHOWN | SDL_WINDOW_RESIZABLE);

  SDL_Renderer *renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_PRESENTVSYNC);

  int running = 1;
  SDL_Event e;
  int w, h;
  int steps = 0;
  char key_down = 0;

  while (running)
  {
    SDL_GetWindowSize(window, &w, &h);
    while (SDL_PollEvent(&e))
    {
      switch (e.type)
      {
      case SDL_QUIT:
        running = 0;
        break;
      case SDL_KEYDOWN:
        key_down = key_pressed(e.key);
        break;

      default:
        break;
      }
    }

    if (key_down == 'l')
    {
      steps++;
      key_down = 0;
    }
    if(key_down == 'k') {
      steps--;
      key_down = 0;
    }

    SDL_SetRenderDrawColor(renderer, 135, 135, 135, SDL_ALPHA_OPAQUE);
    SDL_RenderClear(renderer);

    SDL_SetRenderDrawColor(renderer, 0, 255, 255, SDL_ALPHA_OPAQUE);
    for (int i = 0; i < steps; i++)
    {
      if (i * 10 < h-10)
      {
        SDL_RenderDrawLine(renderer, 10, (i+1) * 10, w - 10, (i+1) * 10);
      }
    }
    SDL_RenderPresent(renderer);
    SDL_Delay(200);
  }

  SDL_Quit();
  return 0;
}