#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <ctype.h>

typedef unsigned char byte;

typedef struct
{
  byte id_length;
  byte color_map_type;
  byte image_type;
  byte color_map[5];
  byte x_origin[2];
  byte y_origin[2];
  byte width[2];
  byte height[2];
  byte depth;
  byte descriptor;
} TGA_HEADER;

typedef struct
{
  unsigned char r, g, b;
} PIXEL;

typedef struct
{
  PIXEL *image;
  int w, h;
} LETTER;

PIXEL *load_pixels(TGA_HEADER header, FILE *file)
{
  int width = 0;
  int height = 0;

  memcpy(&width, header.width, 2);
  memcpy(&height, header.height, 2);
  if (height == 0 || width == 0)
  {
    printf("invalid image size\n");
    return NULL;
  }

  PIXEL *pixels = (PIXEL *)malloc(sizeof(PIXEL) * width * height);
  if (!pixels)
  {
    printf("Could not allocate enough memory\n");
    return NULL;
  }
  assert(fread(pixels, sizeof(PIXEL) * width * height, 1, file) == 1);
  return pixels;
}

int get_row_width(LETTER letters[], char *row)
{
  int letter_index;
  int row_width = 0;
  for (int i = 0; i < (int)strlen(row); i++)
  {
    if (toupper(row[i]) >= 'A' && toupper(row[i]) <= 'Z')
    {
      letter_index = toupper(row[i]) - 'A';
      row_width += letters[letter_index].w;
    }
    else
    {
      row_width += letters[0].w;
    }
  }
  return row_width;
}

void draw_text(int width, LETTER letters[], char *row, int starting_height, PIXEL **image)
{
  int row_width = get_row_width(letters, row);
  int letter_index = -1;
  int starting_index = (width - row_width) / 2;

  // Get Starting index
  for (int i = 0; i < (int)strlen(row); i++) // For each letter in row
  {
    if (toupper(row[i]) >= 'A' && toupper(row[i]) <= 'Z') // Convert to uppercase
    {
      letter_index = toupper(row[i]) - 'A';
    }
    else // If it is space
    {
      letter_index = -1;
    }
    if (letter_index >=0 && letter_index < 26)
    {
      for (int k = starting_height, ii = 0; k < starting_height + letters[letter_index].h; k++, ii++)
      {
        for (int j = starting_index, jj = 0; j < starting_index + letters[letter_index].w; j++, jj++)
        {
          if (letters[letter_index].image[ii * letters[letter_index].w + jj].r != 0 || letters[letter_index].image[ii * letters[letter_index].w + jj].g != 0 || letters[letter_index].image[ii * letters[letter_index].w + jj].b != 0)
          { // If the pixel is black, dont put it into image
            (*image)[k * width + j] = letters[letter_index].image[ii * letters[letter_index].w + jj];
          }
        }
      }
      starting_index += letters[letter_index].w; // add width of letter to starting position
    } else {
      starting_index += letters[0].w;
    }
  }
}

void default_letter_init(LETTER letters[]) {
  for (int i = 0; i < 26; i++)
  {
    letters[i].h =0;
    letters[i].w =0;
    letters[i].image=NULL;
  }
}

int main(int argc, char const *argv[])
{
  if (argc != 4)
  {
    printf("Wrong parameters\n");
    return 1;
  }
  PIXEL *image = NULL;
  LETTER letters[26];
  default_letter_init(letters);
  TGA_HEADER header;
  TGA_HEADER letter_header;
  const char *input = argv[1];
  const char *output = argv[2];
  const char *font = argv[3];
  char row[100];
  int top = 0, bottom = 0;
  int width = 0, height = 0;
  FILE *f_input = fopen(input, "rb");

  // Load image
  if (!f_input)
  {
    printf("Could not load image\n");
    return 1;
  }
  assert(fread(&header, sizeof(TGA_HEADER), 1, f_input) == 1);

  memcpy(&width, header.width, 2);
  memcpy(&height, header.height, 2);
  if (height == 0 || width == 0)
  {
    printf("invalid image size\n");
    return 1;
  }
  image = load_pixels(header, f_input);
  if (!image)
  {
    return 1;
  }
  fclose(f_input);

  // Load Font letters
  for (int i = 0; i < 26; i++)
  {
    char c = (char)('A' + i);
    int size = 8 + (int)strlen(font);
    char *name = (char *)malloc(size);
    if (!name)
    {
      printf("Memory allocation failed\n");
      free(image);
      image = NULL;
      for (int ii = 0; ii < 26; ii++)
      {
        if (letters[ii].image)
        {
          free(letters[ii].image);
          letters[ii].image = NULL;
        }
      }
      return 1;
    }

    snprintf(name, size, "%s/%c.tga", font, c);
    FILE *f = fopen(name, "rb");
    if (!f)
    {
      printf("Could not load image\n");
      free(name);
      name = NULL;
      free(image);
      image = NULL;
      for (int ii = 0; ii < 26; ii++)
      {
        if (letters[ii].image)
        {
          free(letters[ii].image);
          letters[ii].image = NULL;
        }
      }
      return 1;
    }
    assert(fread(&letter_header, sizeof(TGA_HEADER), 1, f) == 1);

    memcpy(&letters[i].w, letter_header.width, 2);
    memcpy(&letters[i].h, letter_header.height, 2);
    if (letters[i].w == 0 || letters[i].h == 0)
    {
      fclose(f);
      printf("invalid image size\n");
      free(name);
      name = NULL;
      free(image);
      image = NULL;
      for (int ii = 0; ii < 26; ii++)
      {
        if (letters[ii].image)
        {
          free(letters[ii].image);
          letters[ii].image = NULL;
        }
      }
      return 1;
    }

    letters[i].image = load_pixels(letter_header, f);
    if (!letters[i].image)
    {
      fclose(f);
      free(name);
      name = NULL;
      free(image);
      image = NULL;
      for (int ii = 0; ii < 26; ii++)
      {
        if (letters[ii].image)
        {
          free(letters[ii].image);
          letters[ii].image = NULL;
        }
      }
      return 1;
    }
    fclose(f);
    free(name);
  }

  // Get number of lines at top and bottom
  scanf("%d %d", &top, &bottom);

  int starting_height = letters[0].h;
  // Load top Lines
  for (int i = 0; i < top; i++)
  {
    if (!fgets(row, sizeof(row), stdin))
    {
      printf("Could not reed input\n");
      return 1;
    }
    if (row[0] == '\n' || row[0] == '\0')
    {
      i--;
      continue;
    }

    draw_text(width, letters, row, starting_height, &image);
    starting_height += letters[0].h + 10; // 10px for spacing between lines
  }

  starting_height = height - bottom * (letters[0].h + 10) - (int)0.5*letters[0].h;
  // Get Bottom lines
  for (int i = 0; i < bottom; i++)
  {
    fgets(row, sizeof(row), stdin);
    if (row[0] == '\n')
    {
      i--;
      continue;
    }
    draw_text(width, letters, row, starting_height, &image);
    starting_height += letters[0].h + 10; // 10px for spacing between lines
  }

  FILE *f_out = fopen(output, "wb");
  if (!f_out)
  {
    printf("Could not load image\n");
    free(image);
    image = NULL;
    for (int ii = 0; ii < 26; ii++)
    {
      if (letters[ii].image)
      {
        free(letters[ii].image);
        letters[ii].image = NULL;
      }
    }
    return 1;
  }
  fwrite(&header, sizeof(header), 1, f_out);
  fwrite(image, sizeof(PIXEL), width * height, f_out);

  fclose(f_out);
  free(image);
  image = NULL;
  for (int ii = 0; ii < 26; ii++)
  {
    if (letters[ii].image)
    {
      free(letters[ii].image);
      letters[ii].image = NULL;
    }
  }

  return 0;
}
