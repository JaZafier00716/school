package lab.regexp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser {
    private static Collection<String> names;

    public static void main(String[] args) {
        String url="https://www.fei.vsb.cz/460/cs/kontakt/lide/";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()))){
            String fileContent = in.lines().collect(Collectors.joining("\n"));
            String tables = extractTables(fileContent);
            Collection<String> titlenames = extractNamesWithTitles(fileContent);
            names = extractNames(titlenames);
        } catch (IOException | URISyntaxException e) {
            //TODO do something
            e.printStackTrace();
        }
    }

    public static String extractTables(String html) {
        String tablePattern = "(?s)(<table.*?>.*?</table>)";
        Pattern pattern = Pattern.compile(tablePattern);
        Matcher matcher = pattern.matcher(html);
        StringBuilder tables = new StringBuilder();
        while (matcher.find()) {
            tables.append(matcher.group(1).strip()).append("\n");
        }
        return tables.toString();
    }

    public static Collection<String> extractNamesWithTitles(String html) {
        String namePattern = "(?s)(<a class='name'.*?>.*?</a>)";
        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(html);
        Set<String> names = new HashSet<>();
        while (matcher.find()) {
            String name =
                matcher.group(1)
                    .strip()
                    .split("[><]")[2]
                    .toLowerCase();
            names.add(name);
        }
        return names;
    }

    public static Collection<String> extractNames(Collection<String> titleNames) {
        Set<String> names = new HashSet<>();
        for (String titledName : titleNames) {
            String name = Arrays.stream(titledName.split(" "))
                .filter(part -> !part.endsWith("."))
                .collect(Collectors.joining(" "))
                .split(",")[0];
            names.add(name);
        }
        return names;
    }

    public static Collection<String> getNames() {
        return names;
    }
}
