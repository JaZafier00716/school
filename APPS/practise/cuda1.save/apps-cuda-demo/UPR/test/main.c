#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#define ROW_MAX 256

typedef struct
{
  char jmeno[100];
  char mesto[100];
  int zlobivost;
  int pocet_darku;
} TDite;

void allocate_dite(TDite **dite, int pocet)
{
  (*dite) = malloc(sizeof(TDite) * (pocet + 1));
}

int main(int argc, char const **argv)
{
  TDite *dite;
  char row[ROW_MAX];
  char *names;
  int k = atoi(argv[1]); // Pocet deti
  int q = atoi(argv[2]); // Pocet dotazu

  allocate_dite(&dite, k);
  names = (char *)malloc(q);

  for (int i = 0; i < k; i++)
  {
    fgets(row, sizeof(row), stdin);
    row[strcspn(row, "\n")] = '\0';
    strcpy(dite[i].jmeno, strtok(row, ","));
    strcpy(dite[i].mesto, strtok(NULL, ","));
    dite[i].zlobivost = atoi(strtok(NULL, ","));
    dite[i].pocet_darku = atoi(strtok(NULL, "\0"));
  }
  for (int i = 0; i < q; i++)
  {
    fgets(row, sizeof(row), stdin);
    strtok(row, ",");
    names[i] = strtok(NULL, "\n")[0];
  }

  TDite max;
  int found = 0;

  // Nastaveni default maxu
  for (int i = 0; i < k; i++) // prochazeni decek
  {
    for (int j = 0; j < q; j++) // prochazeni znaku
    {
      if (strchr(dite[i].jmeno, names[j]))
      {
        max = dite[i];
        found = 1;
        break;
      }
    }
    if (found)
    {
      break;
    }
  }
  if (found)
  {
    for (int i = 0; i < k; i++)
    {
      for (int j = 0; j < q; j++)
      {
        if (strchr(dite[i].jmeno, names[j]))
        {
          if (max.zlobivost < dite[i].zlobivost)
          {
            max = dite[i];
          }
        }
      }
    }
    printf("%s,%d\n", max.jmeno, max.zlobivost);
  } else {
    printf("<not-found>\n");
  }

  free(dite);
  free(names);
  return 0;
}

/* Prikladovy vstup:
jana,brno,50,2
petr,ostrava,25,0
pavel,praha,15,1
karel,chomutov,75,2
patrik,teplice,25,1
naughtiest-named,o
naughtiest-named,p
*/