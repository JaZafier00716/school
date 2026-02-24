#include <iostream>
#include <print>

/*
  By default:
    Struct - vsechny atributy jsou public
    Class - vsechny atributy jsou private

*/

enum struct Enum {
  ABCD = 1,
  EFGH = 2
};


enum class Enum2 {
  ABCD = 5
};

struct Bitfields {
  int a : 4;
  int b : 4;
};

/*
  union v cpp nepouzivat, protoze neni bezpecny, a muze vest k nepredvidatelne chovani programu, pokud se pouzije nespravne.
  nemame jak zjistit, ktery z clenu unie je aktualne pouzivan, a pokud se pouzije nespravne, muze to vest k nepredvidatelne chovani programu.
  V cpp pouzivame std::variant, ktery je bezpecny a umoznuje nam zjistit, ktery z clenu varianty je aktualne pouzivan.
*/

// union Union {
//   std::string str;
//   int num;
// };

/*
  Alignment - zarovnani dat v pameti, aby bylo rychlejsi pristupovat k nim.
  V cpp mame alignas, ktere nam umoznuje zarovnat data na urcity pocet bajtu, a alignof, ktere nam umoznuje zjistit, jak je dane typ zarovnan.
*/

struct S {
  int a;
  int b;
  int c;
}


class C {
  int a;
  int b;
  int c;
}

/*

  Inversion of control - princip, ktery nam umoznuje oddelit logiku programu od implementace, a umoznuje nam tak lepe testovat a udrzovat nas kod.
  V cpp mame std::function, ktere nam umoznuje definovat funkce jako objekty, a umoznuje nam tak lepe testovat a udrzovat nas kod.

  Nejvetsi vyhoda OOP
  napr. filter, map funkce


*/


/*
  explicitni konstruktor - konstruktor, ktery nelze pouzit pro implicitni konverzi, a musi byt volan explicitne.
  V cpp mame explicit, ktere nam umoznuje definovat explicitni konstruktor, a umoznuje nam tak lepe kontrolovat, kdy se ma konstruktor volat, a kdy ne.
*/

/*===================================================================
!!!  V C++ neexistuje "metoda", existuje pouze "clenska funkce", ktera je funkcni, ale je spojena s objektem, a muze pristupovat k jeho atributum a metodam.
=================================================================== */

class A {}


// public - vsichni vidi, ze B dedi z A, mohou volat clenske funkce A na objektech B, a mohou pristupovat k atributum A na objektech B
// protected - na venek nikdo nevidi, ze B dedi z A, nemuze ho precastit na A, ale B a jeho potomci mohou volat clenske funkce A na objektech B, a mohou pristupovat k atributum A na objektech B
// private - umoznuje skladat tridy z jinych trid, umoznuje schovat implementacni detaily, pouzivane hlavne v standardni knihovne, ale v praxi se moc nepouziva, protoze je neprakticke, a muze vest k nepredvidatelne chovani programu, pokud se pouzije nespravne.
class B : public A { 
  mutable int a{}; // mutable je vhodny v pripade, kdy pouzivame knihovny treti strany, ktere maji spatny navrh, a my nemame moznost to zmenit, ale chceme to pouzivat, protoze nam umoznuje lepe testovat a udrzovat nas kod.
  // Dalsi dobre vyuziti je u statistik - chceme spocitat kolikrat byla dana clenska funkce volana, ale nechceme, aby to ovlivnovalo chovani programu, protoze to je jenom statistika, a nechceme, aby to ovlivnovalo chovani programu.
  // abychom vedeli ktere data tam chodi nejcasteji, a mohli jsme je optimalizovat, ale nechceme, aby to ovlivnovalo chovani programu, protoze to je jenom statistika, a nechceme, aby to ovlivnovalo chovani programu.

  void fn() const {
    a = 5; // mutable umoznuje menit atributy objektu, na kterem je volana clenska funkce, i kdyz je tento objekt konstantni
    // Nepouzivat!!!, protoze to muze vest k nepredvidatelne chovani programu, pokud se pouzije nespravne.
  }
};


/*
  friend muze pristupovat k private a protected atributum a metodam tridy, ktera ho deklaruje jako friend, a muze volat clenske funkce tridy, ktera ho deklaruje jako friend, na objektech tridy, ktera ho deklaruje jako friend.
  Pouzivame ho, kdyz chceme, aby nejaka funkce nebo trida mela pristup k private nebo protected atributum nebo metodam jine tridy, ale nechceme, aby to bylo verejne pristupne pro vsechny ostatni tridy.

  muzou to byt funkce, tridy, struktury, unie, nebo dokonce i jine friend deklarace. Friend deklarace muze byt umistena kdekoli v tele tridy, a muze se vyskytovat vicekrat, ale kazda friend deklarace musi byt unikatni - nemuze se vyskytovat vicekrat pro stejnou funkci nebo tridu.
*/


/*
  const - pointer 'this' bude konstantni a nebude moct menit atributy objektu, na kterem je volana clenska funkce, a nebude moct volat jine clenske funkce, ktere nejsou konstantni.
  chceme ho pouzivat co nejcasteji, protoze nam umoznuje lepe kontrolovat, kdy se ma konstruktor volat, a kdy ne, a umoznuje nam tak lepe testovat a udrzovat nas kod.
*/


/*
  copy constructor - konstruktor, ktery se pouziva pro kopirovani objektu, a vytvari novy objekt, ktery je kopii puvodniho objektu.
*/

/*
  RAII - Resource Acquisition Is Initialization - princip, ktery nam umoznuje spravovat zdroje (napr. pamet, soubory, sockety) pomocí objektu, ktery je zodpovedny za jejich spravu, a umoznuje nam tak lepe testovat a udrzovat nas kod.

*/

/*
  optional - bud tam je nebo neni hodnota, kontroluje, jestli je tam hodnota, a umoznuje nam tak lepe kontrolovat, kdy se ma konstruktor volat, a kdy ne, a umoznuje nam tak lepe testovat a udrzovat nas kod.
*/

class IntWrapper {
  int number;

public:
  // IntWrapper(int a) : number{a} { }
  explicit IntWrapper(int a) : number{a} { }

  IntWrapper(const IntWrapper& other) : number{other.number} { }

  // copy assignment operator - konstruktor a operator, ktery se pouziva pro kopirovani objektu, a vytvari novy objekt, ktery je kopii puvodniho objektu, nebo pro prirazeni jednoho objektu do druheho, a vytvari novy objekt, ktery je kopii puvodniho objektu.
  IntWrapper operator=(const IntWrapper& other) { 
    // Inline implementace - implementace se automaticky nakopiruje do vsech mist, kde je tento operator volan, a muze to vest k rychlejsimu behu programu, protoze se nemusí volat funkce, ale muze to vest k zvetseni velikosti programu, protoze se implementace nakopiruje do vsech mist, kde je tento operator volan.
    // Hure se cachuje, protoze se implementace nakopiruje do vsech mist, kde je tento operator volan, a muze to vest k pomalejsimu behu programu, protoze se musi volat funkce, ale muze to vest k zmenseni velikosti programu, protoze se implementace nenakopiruje do vsech mist, kde je tento operator volan.
    number = other.number;
    return *this;
  }
};

void fn(IntWrapper w) {

}

struct HttpRequest { };

struct HttpHandler {
  virtual void handle(HttpRequest& req) = 0;
}

struct HttpServer {
  // HttpRequest getNext();

  // bool isRunning() {
  //   return true;
  // }

  HttpHandler& handler;

  // Handler definuje, jak se bude zpracovavat jeden dany request, a HttpServer se stara o to, aby se tyto requesty dostaly k handleru, ktery je zpracuje.

  // U konstruktoru pokud mozno inicializovat za ':' ne v tele konstruktoru, protoze to je rychlejsi, a navic to umoznuje inicializovat const atributy, ktere nelze inicializovat v tele konstruktoru.

  // Slozene zavorky pouzivame jakozto nejobjecnejsi inicializaci - pokud existuje konstruktor, tak se zavola, pokud ne, tak se pouziva jako '='
  
  
  HttpServer(HttpHandler& handler) : handler{handler} { }

  void listen();
}

struct Handler : HttpHandler {
  void handle(HttpRequest& req) override;
};

int main()
{
  // fn(25); // implicitni konverze z int do IntWrapper, protoze konstruktor neni explicitni
  fn(IntWrapper{25}); // explicitni konverze z int do IntWrapper, protoze konstruktor je explicitni


  // HttpServer server { };
  Handler handler;
  HttpServer server { handler };
  server.listen();

  // while (server.isRunning()) {
  //   // Zbytecne slozite, uzivatel musi znat az moc podrobne implementaci serveru
  //   auto req: HttpRequest = server.getNext();
  // }

  std::println("Hello, world!");

  Enum e = Enum::ABCD;

  std::variant<std::string, int, float> var {"abcd"};

  var = 25;
  // var.emplace<int>(123);
  
  std::get<int>(var);

//RAII:

  // Konstruktor se zavola presne na radku zavolani
  std::vector<int> v {1, 2, 3, 4, 5};
  // Destruktor se zavola pri opusteni scopu funkce main

  if(1 == 2) {
    // Konstruktor se zavola presne na radku zavolani
    std::vector<int> v2 {1, 2, 3, 4, 5};
    // Destruktor se zavola pri opusteni scopu if
    // Pokud funkce fn vyhodi vyjimku, tak RAII zajisti, ze se destruktor zavola, a uvolni se tak pamet, kterou vektor zabira, a nedojde tak k memory leak.

    std::println("something is being done");
    fn(IntWrapper{25});
    std::println("something was done");
  }


  return 0;
}