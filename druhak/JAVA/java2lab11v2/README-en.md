# Java 2 - Exercise 11 v2 - Reflection, Localization

<div class="announce" markdown="1">

### Exercise Topics

* Reflection
* Localization

### Study Materials:

- [Course Information and Study Materials](https://swi.cs.vsb.cz/jezek/student-information/java2.html)
</div>


Use the project <https://gitlab.vsb.cz/jez04-vyuka/java2/labs/java2lab11v2> as a starting point

## Reflection

Modify the `lab.gui.EditController` class so that the properties
of any class can be edited using Java Reflection.

- In the `setObjectToEdit` method, use `Introspector` to obtain `BeanInfo` for detecting Java properties
  (a combination of getter and setter methods) and add a dialog line for each property
- In the `btnOkAction` method, transfer data from the text fields back to the Java properties object
- Create the `@MyEdit` annotation to set read-only (`readOnly`) and visibility
  (`visible`) attributes for each Java bean property
- Use the `MyEdit` annotation to display only those properties that have the same name as an instance
  variable with the `@MyEdit` annotation, and use the settings from the `@MyEdit` annotation to hide the property or
  set it to read-only.
- Set the `id` property as hidden.
- Set the `nickName` property as read-only

## Localization

- Use Java localization (`ResourceBundle`, `Locale`) to translate property names in the dialog.
  Create a "resource bundle" named **msg** for multiple languages.

Use parameters such as `-Duser.language=cs -Duser.country=CZ`

## Correct Settings
The correct settings for `*.properties` files and Czech or other special characters are as follows:

### Newer Java Versions

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
