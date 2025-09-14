#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAX 100

typedef struct
{
  char *nazev_akcie;
  int index;
  float start_hodnota;
  float end_hodnota;
  int pocet_obchodu;
  char *underscore_pocet_obchodu;
} TAkcie;

int num_length(int number)
{
  int i = 0;
  while (number != 0)
  {
    number /= 10;

    i++;
  }

  return i;
}

void format_number(int number, char **underscore)
{
  int length = num_length(number) + 1;
  int underscore_length = length + (int)(length / 3) + 1;
  char *str = (char *)malloc(sizeof(char) * length);
  *underscore = (char *)malloc(sizeof(char) * underscore_length);

  memset(*underscore, '\0', underscore_length);

  sprintf(str, "%d", number);

  if (length > 3)
  {
    for (int i = length - 1, j = underscore_length - 1; i >= 0; i--, j--)
    {
      (*underscore)[j] = str[i];
      if ((length - 1 - i) % 3 == 0 && i < length - 1)
      {
        j--;
        (*underscore)[j] = '_';
      }
    }
    for (int i = 0; i < underscore_length; i++)
    {
      if ((*underscore)[0] == '\0')
      {
        for (int j = i; j < underscore_length; j++)
        {
          if (j >= underscore_length - 1)
          {
            (*underscore)[j] = '\0';
          }
          else
          {
            (*underscore)[j] = (*underscore)[j + 1];
          }
        }
        i--;
      }
    }
  }
  else
  {
    strcpy(*underscore, str);
  }
  free(str);
}

int main(int argc, char const *argv[])
{
  if (argc != 3)
  {
    printf("Wrong parameters\n");
    return 1;
  }
  const char *t = argv[1];
  int n = atoi(argv[2]);
  char row[MAX];
  char *nazev;
  int i;
  TAkcie *akcie = (TAkcie *)malloc(sizeof(TAkcie) * n);
  TAkcie max;
  max.nazev_akcie = "";

  for (i = 0; fgets(row, sizeof(row), stdin) && i < n; i++)
  {
    akcie[i].index = atoi(strtok(row, ","));
    nazev = strtok(NULL, ",");
    akcie[i].nazev_akcie = (char *)malloc(sizeof(char) * ((int)(strlen(nazev) + 1)));
    strcpy(akcie[i].nazev_akcie, nazev);
    akcie[i].start_hodnota = (float)atof(strtok(NULL, ","));
    akcie[i].end_hodnota = (float)atof(strtok(NULL, ","));
    akcie[i].pocet_obchodu = atoi(strtok(NULL, "\n"));

    format_number(akcie[i].pocet_obchodu, &(akcie[i].underscore_pocet_obchodu));

    if (strcmp(akcie[i].nazev_akcie, t) == 0)
    {
      if (strcmp(max.nazev_akcie, "") == 0)
      {
        max = akcie[i];
      }
      else
      {
        // max is set => compare
        if (max.pocet_obchodu < akcie[i].pocet_obchodu)
        {
          max = akcie[i];
        }
      }
    }
  }

  printf("<html>\n");
  printf("<body>\n");
  /* ==================    max value start    ================== */
  printf("<div>\n");
  if (strcmp(max.nazev_akcie, "") == 0)
  {
    printf("Ticker %s was not found\n", t);
  }
  else
  {
    printf("<h1>%s: highest volume</h1>\n", t);
    printf("<div>Day: %d</div>\n", max.index);
    printf("<div>Start price: %.2f</div>\n", max.start_hodnota);
    printf("<div>End price: %.2f</div>\n", max.end_hodnota);
    printf("<div>Volume: %s</div>\n", max.underscore_pocet_obchodu);
  }
  printf("</div>\n");
  /* ==================    max value end    ================== */
  printf("<table>\n");
  printf("<thead>\n");
  printf("<tr><th>Day</th><th>Ticker</th><th>Start</th><th>End</th><th>Diff</th><th>Volume</th></tr>\n");
  printf("</thead>\n");
  printf("<tbody>\n");
  for (int i = n - 1; i >= 0; i--)
  {
    printf("<tr>\n");
    if (strcmp(akcie[i].nazev_akcie, t) == 0)
    {

      printf("\t<td><b>%d</b></td>\n", akcie[i].index);
      printf("\t<td><b>%s</b></td>\n", akcie[i].nazev_akcie);
      printf("\t<td><b>%.2f</b></td>\n", akcie[i].start_hodnota);
      printf("\t<td><b>%.2f</b></td>\n", akcie[i].end_hodnota);
      printf("\t<td><b>%.2f</b></td>\n", akcie[i].end_hodnota - akcie[i].start_hodnota);
      printf("\t<td><b>%s</b></td>\n", akcie[i].underscore_pocet_obchodu);
    }
    else
    {
      printf("\t<td>%d</td>\n", akcie[i].index);
      printf("\t<td>%s</td>\n", akcie[i].nazev_akcie);
      printf("\t<td>%.2f</td>\n", akcie[i].start_hodnota);
      printf("\t<td>%.2f</td>\n", akcie[i].end_hodnota);
      printf("\t<td>%.2f</td>\n", akcie[i].end_hodnota - akcie[i].start_hodnota);
      printf("\t<td>%s</td>\n", akcie[i].underscore_pocet_obchodu);
    }
    printf("</tr>\n");
  }

  printf("</tbody>\n");
  printf("</table>\n");
  printf("</body>\n");
  printf("</html>\n");

  for (int i = 0; i < n; i++)
  {
    free(akcie[i].nazev_akcie);
    akcie[i].nazev_akcie = NULL;
    free(akcie[i].underscore_pocet_obchodu);
    akcie[i].underscore_pocet_obchodu = NULL;
  }

  free(akcie);
  akcie = NULL;
  return 0;
}
