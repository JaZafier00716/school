#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct
{
  unsigned char idlength;
  unsigned char colourmaptype;
  unsigned char datatypecode;
  unsigned char colourmaporigin[2];
  unsigned char colourmaplength[2];
  unsigned char colourmapdepth;
  unsigned char x_origin[2];
  unsigned char y_origin[2];
  unsigned char width[2];
  unsigned char height[2];
  unsigned char bitsperpixel;
  unsigned char imagedescriptor;
} TGA_HEADER;

typedef struct
{
  unsigned char b;
  unsigned char g;
  unsigned char r;
} PIXEL;

int main(void)
{
  int width = 1920; // 1920 / 256, 1920 % 256
  int height = 1080;
  TGA_HEADER header = {
      0,
      0,
      2,
      {0},
      {0},
      0,
      {0},
      {0},
      // {width%256, width/256}, // nejprve spodni Byte potom horni
      // {height%256, height/256},
      {0}, // Defaultni sirka a vyska do 0
      {0},
      24,
      32};

  memcpy(header.width, &width, 2);
  memcpy(header.height, &height, 2);

  PIXEL *pixel = malloc(sizeof(PIXEL) * width * height);
  int r = 18, g = 18, b = 18;
  for (int i = 0, k = 0; i < height; i++, k++)
  {
    for (int j = 0; j < width; j++)
    {
      if (k > 5)
      {

        r = j * i + i + rand() % 15;
        g = j + i + rand() % 15;
        b = i * j + rand() % 15;
        k = 0;
      }
      if (r > 255)
      {
        r = i * j + rand() % 128;
      }
      if (g > 255)
      {
        g = j * i + i + rand() % 128;
      }
      if (b > 255)
      {
        b = j + i + rand() % 128;
      }
      unsigned char color = rand() % 5;
      pixel[i * width + j].b = r;
      pixel[i * width + j].g = g;
      pixel[i * width + j].r = b;
    }
  }

  FILE *image = fopen("image.tga", "wb");

  fwrite(&header, sizeof(header), 1, image);
  fwrite(pixel, sizeof(pixel), width * height, image);

  fclose(image);

  printf("%d %d", header.width[0], header.width[1]);
  printf("ld\n", sizeof(TGA_HEADER));

  unsigned short w;
  memcpy(&w, header.width, 2);

  printf("%d", w);
  free (pixel);
  pixel = NULL;

  return 0;
}