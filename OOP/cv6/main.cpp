#include "header.h"
#include "ExtendedArray.h"
/*
Trida jako objekt:
  Trida bude mit vlastni stav a vlastni chovani sama o sobe

  static
    metoda patri tride
    muzeme pouzivat aniz bychom museli vytvorit objekt

  muzeme vyuzit misto konstanty pro jednotlive projekty, jako spolecnou promennou pro vsechny objekty
    napriklad PI pro vsechny objekty Matematika...
    kdyz nam trida poskytuje metody, ktere nesouvisi s vnitrnim stavem objektu, tak nam staci globalni... (kdyz obaluji vypocty navenek...)


  counter instanci tridy...

  pro vytvoreni pole 
    staticka metoda pro vytvoreni pole o danem rozsahu
    naplneni pole danymi hodnotami (1..n, nebo, 0..0, nebo 1..1, atd.);
*/



int main () {
  cout << Math::add(5,4.2) << "\t" << Math::PI << endl;

  ExtendedArray ea = ExtendedArray::zeros(5);

  
  std::cout << ExtendedArray::objectCounter << endl;

  Math m;
  m.PI;


  return 0;
}