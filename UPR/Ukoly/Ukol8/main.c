#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#define MAX 100

void free_all(char *input, char *output, char *needle, char *lowercase_needle)
{
  if (input != NULL)
  {
    free(input);
    input = NULL;
  }
  if (output != NULL)
  {
    free(output);
    output = NULL;
  }
  if (needle != NULL)
  {
    free(needle);
    needle = NULL;
  }
  if (lowercase_needle != NULL)
  {
    free(lowercase_needle);
    lowercase_needle = NULL;
  }
}

int main(int argc, char const *argv[])
{
  char row[MAX];
  char lowercase_row[MAX];       // used with -i parameter
  char *lowercase_needle = NULL; // used with -i parameter
  char *input_path = NULL;
  char *needle = NULL;
  char *output_file = NULL;
  int ignore_casing = 0;

  /* === Start Checking parameter correctness === */
  for (int i = 1; i < argc; i++)
  {
    if (strcmp(argv[i], "-i") == 0)
    {
      if (ignore_casing)
      { // If input has already been set
        printf("Parameter -i provided multiple times\n");
        free_all(input_path, output_file, needle, lowercase_needle);
        return 1;
      }
      ignore_casing = 1;
      continue;
    }
    if (strcmp(argv[i], "-o") == 0)
    {
      i++;
      if (output_file != NULL)
      { // If output has already been set
        printf("Parameter -o provided multiple times\n");
        free_all(input_path, output_file, needle, lowercase_needle);
        return 1;
      }
      if (i >= argc)
      { // if the output path is missing
        printf("Missing output path\n");
        free_all(input_path, output_file, needle, lowercase_needle);
        return 1;
      }
      output_file = strdup(argv[i]);
      continue;
    }
    if (input_path == NULL)
    {
      input_path = strdup(argv[i]);
      continue;
    }
    if (needle == NULL)
    {
      needle = strdup(argv[i]);
      continue;
    }
    printf("Too many parameters provided\n");
    free_all(input_path, output_file, needle, lowercase_needle);
    return 1;
  }
  if (input_path == NULL)
  {
    printf("Input path not provided\n");
    free_all(input_path, output_file, needle, lowercase_needle);
    return 1;
  }
  if (needle == NULL)
  {
    printf("Needle not provided\n");
    free_all(input_path, output_file, needle, lowercase_needle);
    return 1;
  }
  /* === End Checking parameter correctness === */

  if (ignore_casing)
  {
    lowercase_needle = strdup(needle);
    for (int i = 0; i < strlen(lowercase_needle); i++)
    {
      lowercase_needle[i] = (char)tolower(lowercase_needle[i]);
    }
  }

  FILE *f = fopen(input_path, "r");
  FILE *o;
  if (output_file)
  {
    o = fopen(output_file, "w");
  }
  while (fgets(row, sizeof(row), f))
  {
    if (ignore_casing)
    {
      for (int i = 0; i < (int)strlen(row); i++)
      {
        lowercase_row[i] = (char)tolower(row[i]);
      }
      lowercase_row[strlen(row)] = '\0';

      if (strstr(lowercase_row, lowercase_needle))
      {
        if (output_file == NULL)
        {
          printf("%s", row);
          if (!strstr(row, "\n"))
          {
            printf("\n");
          }
        }
        else
        {
          fprintf(o, "%s", row);
          if (!strstr(row, "\n"))
          {
            fprintf(o, "\n");
          }
        }
        continue;
      }
    }
    if (strstr(row, needle))
    {
      if (output_file == NULL)
      {
        printf("%s", row);
        if (!strstr(row, "\n"))
        {
          printf("\n");
        }
      }
      else
      {
        fprintf(o, "%s", row);
        if (!strstr(row, "\n"))
        {
          fprintf(o, "\n");
        }
      }
      continue;
    }
  }

  if (output_file)
  {
    fclose(o);
  }

  fclose(f);
  free_all(input_path, output_file, needle, lowercase_needle);
  return 0;
}
