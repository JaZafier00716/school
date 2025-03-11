#include <stdio.h>
#include <string.h>
#define STR_SIZE 50

void remove_duplicates(char str[])
{
  int new_size = (int)strlen(str);

  for (int i = 0; i < new_size; i++) // Removes multiple spaces from in between words
  {
    if ((str[i] == str[i + 1]))
    {
      for (int j = i; j < new_size; j++)
      {
        str[j] = str[j + 1];
      }
      new_size--;
      i--;
    }
  }
}

void remove_spaces(char str[])
{ // returns new size
  int new_size = (int)strlen(str);

  while (str[0] == ' ') // Removes all spaces from start
  {
    for (int j = 0; j < new_size; j++)
    {
      str[j] = str[j + 1];
    }
    new_size--;
  }

  for (int i = 0; i < new_size; i++) // Removes multiple spaces from in between words
  {
    if ((str[i] == ' ' && str[i + 1] == ' '))
    {
      for (int j = i; j < new_size; j++)
      {
        str[j] = str[j + 1];
      }
      new_size--;
      i--;
    }
  }
  for (int i = new_size; str[i] == ' ' || str[i] == '\n' || str[i] == '\0'; i--) // Removes all spaces from end
  {
    str[i] = '\0';
    new_size--;
  }
  str[new_size + 1] = '\n';
}

int space_num(char str[])
{
  int spaces = 0;
  for (int i = 0; i < (int)strlen(str); i++)
  {
    if (str[i] == ' ')
    {
      spaces++;
    }
  }

  return spaces;
}

int lower_num(char str[])
{
  int lowers = 0;
  for (int i = 0; i < (int)strlen(str); i++)
  {
    if (str[i] >= 'a' && str[i] <= 'z')
    {
      lowers++;
    }
  }

  return lowers;
}

int upper_num(char str[])
{
  int uppers = 0;
  for (int i = 0; i < (int)strlen(str); i++)
  {
    if (str[i] >= 'A' && str[i] <= 'Z')
    {
      uppers++;
    }
  }

  return uppers;
}

void normalize_str(char str[])
{
  char new_string[STR_SIZE];

  char *substr = strtok(str, " ");
  int length = 0;

  memset(new_string, '\0', STR_SIZE);

  // printf("\nSTR_LENGTH: %d\n\n", strlen(str));

  while (substr)
  {

    if (0 == upper_num(substr))
    {
      for (int i = 0; i < (int)strlen(substr); i++) // make all uppercase
      {
        if (substr[i] == '\n')
        {
          new_string[i + length] = substr[i];
        }
        else
        {
          new_string[i + length] = substr[i] - 32;
        }
      }
    }
    else
    {
      for (int i = 0; i < (int)strlen(substr); i++)
      {
        if (substr[i] == '\n')
        {
          new_string[i + length] = substr[i];
          break;
        }
        if (substr[i] >= 'A' && substr[i] <= 'Z' && i > 0) // change letter to lowercase
        {
          new_string[i + length] = substr[i] + 32;
          continue;
        }
        if (substr[i] >= 'a' && substr[i] <= 'z' && i == 0) // if the first letter is lowercase, make it uppercase
        {
          new_string[i + length] = substr[i] - 32;
          continue;
        }
        new_string[i + length] = substr[i];
      }
    }

    length += (int)strlen(substr);

    substr = strtok(NULL, " ");
    if (substr)
    {
      new_string[length] = ' ';
      length++;
    }
    else
    {

      new_string[length] = '\0';
      length++;
    }
  }
  strcpy(str, new_string);
}

int main()
{
  int n;
  char str[STR_SIZE], n_str[STR_SIZE];

  scanf("%d", &n);

  while (getchar() != '\n')
    ;
  for (int i = 0; i < n; i++)
  {
    fflush(stdin);
    fflush(stdout);
    memset(str, '\0', STR_SIZE);
    memset(n_str, '\0', STR_SIZE);
    if (fgets(str, sizeof(str), stdin) == NULL)
    {
      printf("%s", n_str);
      if (strstr(n_str, "\n") == NULL)
      {
        printf("\n");
      }
      printf("lowercase: %d -> %d\n", lower_num(str), lower_num(n_str));
      printf("uppercase: %d -> %d\n", upper_num(str), upper_num(n_str));
      printf("spaces: %d -> %d\n", space_num(str), space_num(n_str));
      return 0;
    }

    // printf("length: %d\n", strlen(str));
    if (strlen(str) <= 1)
    {
      i--;
      continue;
    }

    strcpy(n_str, str);

    remove_spaces(n_str);

    normalize_str(n_str);
    remove_duplicates(n_str);

    printf("%s", n_str);
    if (strstr(n_str, "\n") == NULL)
    {
      printf("\n");
    }
    printf("lowercase: %d -> %d\n", lower_num(str), lower_num(n_str));
    printf("uppercase: %d -> %d\n", upper_num(str), upper_num(n_str));
    printf("spaces: %d -> %d\n", space_num(str), space_num(n_str));
    if(i+1 < n) {
      printf("\n");
    }
  }

  return 0;
}