# Java 2 - 11. cvičení v2 - Reflection, Localization

<div class="announce" markdown="1">

### Témata cvičení

* Reflection
* Localization

### Studijní materiály:

- [Informace k předmětu a studijní materiály](https://swi.cs.vsb.cz/jezek/student-information/java2.html)
</div>


Vycházejte z projektu <https://gitlab.vsb.cz/jez04-vyuka/java2/labs/java2lab11v2>

## Reflection

Upravte třídu `lab.gui.EditController` tak aby bylo možno editovat vlastnosti (properties)
jakékoliv třídy s využitím Java Reflection.

- V metodě `setObjectToEdit` použijte `Introspector` k získání `BeanInfo` pro detekci java properties
  (kombinace getter a setter metod) a přidejte dialogový řádek pro každou property
- V metodě `btnOkAction` přeneste data z textových polí zpět do java properties objektu
- Vytvořte anotaci `@MyEdit` pro možnost nastavení atributů pouze pro čtení (`readOnly`) a viditelnosti
  (`visible`) každé java bean property
- Použijte anotaci `MyEdit` k zobrazení pouze těch vlastností, které mají stejné jméno jako instanční
  proměnná s anotací `@MyEdit` a použijte nastavení z anotace `@MyEdit` ke skrytí vlastnosti nebo k
  jejímu nastavení pouze pro čtení.
- Nastavte vlastnost `id` jako skrytou.
- Nastavte vlastnost `name` pouze pro čtení

## Lokalizace

- Použijte java lokalizaci (`ResourceBundle`, `Locale`) pro překlad názvu vlastností v dialogu.
  Vytvořte "resource bundle" jménem **msg** pro několik jazyků.

Použijte např. parametry `-Duser.language=cs -Duser.country=CZ`


## Correct Settings
The correct settings for `*.properties` files and Czech or other special characters are as follows:

### Newer Version

`UTF-8` encoding

- IDEA settings:
    - Settings -> Editor -> File Encodings -> Default encoding for properties files: UTF-8
    - Settings -> Editor -> File Encodings -> Transparent native-to-ascii conversion: false

- Eclipse with the [ResourceBundle editor](https://marketplace.eclipse.org/content/resourcebundle-editor) plugin:
    - < right-click > on file -> Properties -> Text file encoding: UTF-8
    - Window -> Preferences -> ResourceBundle editor -> Convert \uxxxx .... : false


### Older method - Required for Java versions prior to 8

`ISO-8859-1` encoding and UTF characters are escaped in the format \uxxxx (e.g., `\u017Dlu\u0165ou\u010Dk\u00FD k\u016F\ u0148 sk\u00E1\u010De p\u0159es kalu\u017Ee`)
- IDEA settings:
    - Settings -> Editor -> File Encodings -> Default encoding for properties files: ISO-8859-1
    - Settings -> Editor -> File Encodings -> Transparent native-to-ASCII conversion: true
- Eclipse - no configuration required
