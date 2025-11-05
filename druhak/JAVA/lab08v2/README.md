# Java 1 - 8. cvičení v2

## Projekt
Z vašeho projektu z minulého cvičení nebo z projektu s řešením minulého
cvičení (<https://gitlab.vsb.cz/jez04-vyuka/java1/labs/lab08v2.git>)


## Skóre
Vytvořte v novém balíku `lab.score` třídu `lab.score.Score` s instanční proměnnou `nickName` typu `String` a instanční proměnnou `score` typu `int`.

- Přidejte konstruktor se dvěmi parametry.
- Vytvořte statickou metodu `public static Score generate()`, která za použití pomocné třídy `Utilities`
  vytvoří néhodné skóre.

## Table
Použijte již vytvořený soubor `menu.fxml` a pro něj vytvořte třídu `lab.MenuController`.
Nezapomeňte přidat potřebné instanční proměnné a metody (viz ukázkový skeleton ve scene buildru).

Třídě také přidejte instanční proměnnou `private App app;` a její setter `public void setApp(App app)`.

Generické instanční proměnné:

- pro `TableView` použijte generický typ `Score`
- pro `TableColumn` použijte generické typy `<Score, String>` a `<Score, Integer>`.

V metodě `MenuController.initialize` nastavte co mají zobrazovat sloupce:
````java
columnNickName.setCellValueFactory(new PropertyValueFactory<>("nickName"));
columnScore.setCellValueFactory(new PropertyValueFactory<>("score"));
````

## Přepínání scén
Ve třídě `App`:

- přidejte instanční proměnnou `private GameController gameController;`
- přidejte instanční proměnnou `private Stage primaryStage;`
- v metodě `start` načtěte font `Font.loadFont(this.getClass().getResourceAsStream("/TRON.TTF"), 20);`
- uložte odkaz na okno (stage) `this.primaryStage = primaryStage;`
- místo načtení scény volejte metodu `switchToMenu();`
- Přidejte metody:
  ````java
    public void switchToGame(String name, int numberOfMonsters) throws IOException {
        // Construct a main window with a canvas.
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/lab/gameWindow.fxml"));
        Parent root = gameLoader.load();
        gameController = gameLoader.getController();
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
        gameController.startGame(name, numberOfMonsters);
    }

    private void switchToMenu() throws IOException {
        // Construct a main window with a canvas.
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = menuLoader.load();
        MenuController menuController = menuLoader.getController();
        menuController.setApp(this);
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
    }
  ````

## Úprava GameController
Funkcionalitu vytvoření a spuštení `DrawingThread` přesuňte do metody:
````java
    public void startGame(String name, int numberOfMonsters) {
  //        playerName.setText(name);
  //        level = new Level(canvas.getWidth(), canvas.getHeight(), numberOfMonsters);
  timer = new DrawingThread(canvas, world);
  timer.start();
}

````

## Generování
V medodě `MenuController.onBtnGenerate` vygenerujte několik náhodných skóre a vložte je do tabulky
pomocí `scoreTable.getItems().add`

Odzkoušejte - ve třídě `Score` bude něco chybět.

## Ukládání do souboru
Vytvořte novou pomocnou třídu `lab.score.ScoreRepository`, která bude sloužit k načítání a
ukládání skóre.

Ve třídě `ScoreRepository` vytvořte metodu `public static void save(List<Score> scores)`.

- pomocí `BufferedWriter` uložte skóre ve formátu CSV.
- Pokud dojde k jakékoliv chybě vyhoďte novou vyjímku `ScoreException` (vytvořte ji v balíku `lab.score`).
- Metoda save bude vyhazovot vyjímku `ScoreException`
- V `MenuController.onBtnSave` uložte data z tabuky do CSV souboru, pokud nasatane vyjímka
  zobrazte uživateli alert:
  ````java
  Alert alert = new Alert(Alert.AlertType.WARNING);
  alert.setHeaderText("Storing problem");
  alert.getDialogPane().setContentText(e.getMessage());
  alert.showAndWait();
  ````

## Načítání ze souboru
Ve třídě `ScoreRepository` vytvořte metodu `public static List<Score> load()`.

- pomocí `BufferedReader` načtěte skóre ze souboru (formát CSV).
- Pokud dojde k jakékoliv chybě vyhoďte vyjímku `ScoreException`.
  Přidejte do vyjímky informaci o čísle řádku a to vložte to `message`.
- Metoda load bude vyhazovot vyjímku `ScoreException`
- V `MenuController.onBtnLoad` vložte načtená data do tabuky, pokud nasatane vyjímka
  zobrazte uživateli alert:
  ````java
  Alert alert = new Alert(Alert.AlertType.WARNING);
  alert.setHeaderText("Loading problem");
  alert.getDialogPane().setContentText(e.getMessage());
  alert.showAndWait();
  ````

## Spuštení hry
V metodě `MenuController.onBtnPlay` spusťte původní "hru" pomocí `app.switchToGame("Name", 10);`

## Způsob odevzdání
Projekt odevzdejte do systému Kelvin (právě jste zde). Nahrajte zde celý adresář **src** a soubor **pom.xml**
dle níže zobrazeného odkazu na video.

Následně bude projekt zkompilován a provedou se Unit Testy. Vzhledem k povaze projektu a prozatímnímu
testovacímu využití systému Kelvin v předmětu Java 1 v případě selhání nezoufejte.

Jedná se o pomocný test, vše bude ještě hodnoceno ručně. Důležité je nahrát soubory aby bylo možno vše vyhodnotit
a provést analýzu na plagiáty. Věřím, že je to je formalita a všichni z Vás tvoří vlastní kód.

[Podrobný popis odevzdání do Kelvinu](https://swi.cs.vsb.cz/jezek/student-information/Java1/kelvin-submision.html)
