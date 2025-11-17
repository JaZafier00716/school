package lab.regexp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public static void main(String[] args) {
        System.out.println("Starting Java programing test - part Parser.");
        Parser parser = new Parser();
        String fileContent = """
            Toto je zkušební text.
            Něco jako   tab a	tab.
            Další(ano) část.

            ()
            jako jako ano,ano
            """;
        for (String word : parser.splitToWorlds(fileContent)) {
            System.out.println("'" + word + "'");
        }
        String numbers = """
            Toto je zkušební text.
            7 9.5
            700 -987.012
            Něco jako   tab a	ta11b.
            Další(ano) čá23.8st.

            ()
            jako jako ano,ano
            """;
        for (Float f : parser.findNumbers(numbers)) {
            System.out.println(f);
        }
    }

    /**
     * Metoda rozdělí content na jednotlivá slova podle:
     * mezer, tabulátorů, závorek, teček a konců řádků
     * Slova se převedou na malé znaky a vrátí se v kolekci bez duplicit.
     */
    public Collection<String> splitToWorlds(String content) {
        //TODO
        String[] wordArray = content.split("[ ,.()\t\n\r]+"); // + znamena 1-n opakovani predhoziho
        Set<String> wordSet = new HashSet<>();
        for (String word : wordArray) {
            wordSet.add(word.toLowerCase().trim()); // trim oseka bile znaky okolo slova
        }
        return wordSet; //nutno změnit
    }

    /**
     * Metoda najde v textu desetiná čísla a vrátí je jako kolekci.
     */
    public List<Float> findNumbers(String text) {
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*"); // vyhleda integery \\. pro hledani '.', ? pro optional
        Matcher matcher = pattern.matcher(text);
        List<Float> numbers = new ArrayList<>();
        while(matcher.find()) {
            numbers.add(Float.parseFloat(matcher.group()));
        }
        return numbers; //nutno změnit
    }
}
