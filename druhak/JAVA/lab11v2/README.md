# Java 1 - 11. cvičení v2

## Projekt
Z vašeho projektu z minulého cvičení nebo z projektu s řešením minulého
cvičení (<https://gitlab.vsb.cz/jez04-vyuka/java1/labs/lab11v2.git>)

## Přístup k Databázi

### Driver
Do `pom.xml` přidejte závislost:
````xml
<!-- https://mvnrepository.com/artifact/com.h2database/h2 -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.4.240</version>
</dependency>
````

## Připojení na DB
V metodě `ScoreRepository.getConnection` vytvořte pomocí techniky LazyLoad připojení na DB.
- Connection string: `jdbc:h2:file:./score-db`

## Tabluka
V metodě `ScoreRepository.init` získejte connection na databázi a vytvořte `Statement` a proveďte SQL pro vytvoření databáze:
````sql
CREATE TABLE Scores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    points integer
);
````
Pokud nastane vyjímka může to být i tím, že tabulka již exisatuje.

### CRUD
Doplňte metody:
- `void save(Score score)`
- `void save(List<Score> scores)` - použijte prepare statement
- `List<Score> load()`
- `void delete(Score score)`

## Způsob odevzdání
Projekt odevzdejte do systému Kelvin (právě jste zde). Nahrajte zde celý adresář **src** a soubor **pom.xml**
dle níže zobrazeného odkazu na video.

Následně bude projekt zkompilován a provedou se Unit Testy. Vzhledem k povaze projektu a prozatímnímu
testovacímu využití systému Kelvin v předmětu Java 1 v případě selhání nezoufejte.

Jedná se o pomocný test, vše bude ještě hodnoceno ručně. Důležité je nahrát soubory aby bylo možno vše vyhodnotit
a provést analýzu na plagiáty. Věřím, že je to je formalita a všichni z Vás tvoří vlastní kód.

[Podrobný popis odevzdání do Kelvinu](https://swi.cs.vsb.cz/jezek/student-information/Java1/kelvin-submision.html)
