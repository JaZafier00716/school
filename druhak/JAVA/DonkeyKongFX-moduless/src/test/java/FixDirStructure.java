import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class FixDirStructure {
  private static final String JUNIT_VERSION = "6.0.3";
  private static final String ORG_JUNIT_JUPITER = "org.junit.jupiter";

  static {
    System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s: %5$s%6$s%n");
  }

  private static Logger log = java.util.logging.Logger.getLogger(FixDirStructure.class.getName());
  public static final List<String> skipNames = List.of("target", ".settings", ".project", ".classpath", ".kelvin-utils",
      "result.html");
  public static final String TAR_GZ_PATTERN = ".*\\.tar\\.gz";

  private static final Pattern mainPatter = Pattern.compile(
      "^\\s*public\\s+static\\s+void\\s+main\\s*\\(\\s*String\\s*\\[\\]\\s*args\\s*\\)", Pattern.MULTILINE);

  public static void main(String[] args) {
    try {
      if (args.length < 1) {
        log.info("Usage:");
        log.info("java FixDirStructure <pathToDir> <pathToTemplate>");
      } else {
        log.info(() -> "Fixing dir maven project dir structure for: " + args[0]);
        String tempDir = "/template";
        if (args.length >= 2) {
          tempDir = args[1];
        }
        log.info("Template dir: " + tempDir);
        fix(args[0], tempDir);
      }
    } catch (Exception e) {
      log.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public static void fix(String pathToDir, String pathToTemplateDir) {
    Path dir = Paths.get(pathToDir).toAbsolutePath();
    log.info(() -> "Absolute path: " + dir);
    if (!dir.toFile().exists()) {
      log.info("Path not exist!");
      log.info("");
      return;
    }
    if (!dir.toFile().isDirectory()) {
      log.info("Path is not directory!");
      log.info("");
      return;
    }
    removeOneDirPath(dir);
    List<File> files = getFileListInProjectRoot(dir);
    boolean pomExist = files.stream().anyMatch(f -> f.getName().equals("pom.xml"));
    long multiPomCount = findFiles(dir, "pom.xml").size();
    boolean pomInRootDir = files.stream().anyMatch(f -> !f.isDirectory() && f.getName().equalsIgnoreCase("pom.xml"));
    long multiPomCountInRootDir = files.stream()
        .filter(f -> !f.isDirectory() && f.getName().matches("pom(\\s*\\((.*)\\))?\\.xml")).count();
    boolean onlyJava = files.stream().filter(f -> !f.getName().equals("pom.xml"))
        .allMatch(f -> f.getName().endsWith(".java"));
    boolean pomAndSrcZip = pomExist && files.size() == 2 &&
        files.stream().filter(f -> !f.getName().equals("pom.xml")).allMatch(f -> !f.isDirectory()) &&
        files.stream().filter(f -> !f.getName().equals("pom.xml"))
            .allMatch(f -> f.getName().toLowerCase().matches("src|src.zip"));
    boolean onlyTarGz = files.size() == 1 && files.stream().allMatch(f -> f.getName().matches(TAR_GZ_PATTERN));
    boolean parentPom = findFiles(dir, "pom.xml").stream().anyMatch(pomPath -> {
      try {
        String content = Files.readString(pomPath);
        return content.contains("<modules>");
      } catch (IOException e) {
        log.log(Level.SEVERE, e, () -> "Error reading pom.xml content for: " + pomPath.toAbsolutePath());
        return false;
      }
    });
    List<File> testFiles = List.of(Paths.get(pathToTemplateDir, "java-tests").toFile().listFiles());
    boolean parentReadyTask = testFiles.stream().filter(file -> file.getName().endsWith(".java")).allMatch(file -> {
      try {
        List<String> content = Files.readAllLines(file.toPath());
        return content.getFirst().matches("\\w*//child:.*");
      } catch (IOException e) {
        log.log(Level.SEVERE, e, () -> "Error reading pom.xml content for: " + file.toPath().toAbsolutePath());
        return false;
      }
    });
    log.info(() -> "multi Pom Count :" + multiPomCount);
    log.info(() -> "multi Pom Count in root :" + multiPomCountInRootDir);
    log.info(() -> "pom Exist      : " + pomExist);
    log.info(() -> "only Java      : " + onlyJava);
    log.info(() -> "pom And Src Zip: " + pomAndSrcZip);
    log.info(() -> "only Tar Gz    : " + onlyTarGz);
    log.info(() -> "Parent pom     : " + parentPom);
    log.info(() -> "Parent ready   : " + parentReadyTask);
    log.info(() -> "Pom.xml in root: " + pomInRootDir);
    log.info("");
    log.info("");
    if (onlyTarGz) {
      expandTarGz(dir);
    } else if (multiPomCountInRootDir > 1) {
      fixMultiplepPoms(dir);
      downloadAndExpandMultiProjectEvaluator(dir);
    } else if (multiPomCount > 1) {
      boolean downloadMultiproject = true;
      if (pomInRootDir) {
        if (parentPom) {
          if (parentReadyTask) {
            downloadMultiproject = false;
          }
        } else {
          preserveRootProject(dir);
        }
      }
      if (downloadMultiproject) {
        downloadAndExpandMultiProjectEvaluator(dir);
      }
    } else if (pomExist && onlyJava) {
      makeMavenProjectStructure(dir);
    } else if (pomAndSrcZip) {
      expandSrcZip(dir);
    }
    if(parentReadyTask){
      List<File> moduleDirs = List.of(dir.toFile().listFiles(filePath -> filePath.isDirectory()));
      for(File moduleDir : moduleDirs){
        extendPom(moduleDir.toPath());
      }
    }else {
      extendPom(dir);
    }
    findMain(dir);
  }

  private static boolean isOneDir(Path dir) {
    List<File> files = getFileListInProjectRoot(dir);
    return files.stream().allMatch(File::isDirectory) && files.size() == 1;
  }

  private static List<File> getFileListInProjectRoot(Path dir) {
    return Arrays.asList(dir.toFile().listFiles()).stream().filter(f -> !skipNames.contains(f.getName())).toList();
  }

  private static void preserveRootProject(Path dir) {
    Path newProjectPath = dir.resolve("preserved-root-project");
    List<File> files = getFileListInProjectRoot(dir).stream()
        .filter(f -> "pom.xml".equals(f.getName()) || "src".equals(f.getName())).toList();
    moveFilesToNewDir(newProjectPath, files);

  }

  private static void preserveParentRootProject(Path dir) {
    Path newProjectPath = dir.resolve("parent-project");
    List<File> files = getFileListInProjectRoot(dir);
    moveFilesToNewDir(newProjectPath, files);

  }

  private static void moveFilesToNewDir(Path newProjectPath, List<File> files) {
    try {
      Files.createDirectory(newProjectPath);
      for (File file : files) {
        Files.move(file.toPath(), newProjectPath.resolve(file.getName()));
      }
    } catch (IOException e) {
      log.log(Level.SEVERE, e, () -> "Cannot move file to new folder.");
    }
  }

  private static void fixMultiplepPoms(Path dir) {
    List<File> files = getFileListInProjectRoot(dir);
    List<File> poms = files.stream().filter(f -> !f.isDirectory() && f.getName().matches("pom(\\s*\\((.*)\\))?\\.xml"))
        .toList();
    Pattern pomExp = Pattern.compile("pom(\\s*\\((.*)\\))?\\.xml");
    Map<String, List<File>> projectMap = new HashMap<>();
    for (File pom : poms) {
      Matcher matcher = pomExp.matcher(pom.getName());
      matcher.find();
      String index = matcher.group(1);
      projectMap.computeIfAbsent(index, key -> new ArrayList<>()).add(pom);
    }
    List<File> srcs = files.stream().filter(f -> f.isDirectory() && f.getName().matches("src(\\s*\\((.*)\\))?"))
        .toList();
    Pattern srcExp = Pattern.compile("src(\\s*\\((.*)\\))?");
    for (File src : srcs) {
      Matcher matcher = srcExp.matcher(src.getName());
      matcher.find();
      String index = matcher.group(1);
      projectMap.computeIfAbsent(index, key -> new ArrayList<>()).add(src);
    }
    int projectIndex = 1;
    for (Entry<String, List<File>> entry : projectMap.entrySet()) {
      Path projectPath = dir.resolve("project-" + projectIndex);
      try {
        Files.createDirectory(projectPath);
        for (File file : entry.getValue()) {
          Files.move(file.toPath(), projectPath.resolve(file.getName().contains("src") ? "src" : "pom.xml"));
        }
      } catch (IOException e) {
        log.log(Level.SEVERE, e, () -> "Cannot move file to new folder.");
      }
      projectIndex++;
    }
  }

  private static void downloadAndExpandMultiProjectEvaluator(Path path) {
    try (InputStream inputStream = new URI(
        "https://gitlab.vsb.cz/jez04-vyuka/eval-multiproject/-/archive/release/eval-multiproject-main.zip").toURL()
        .openStream()) {
      extractZipStream(inputStream, path, 1);
    } catch (URISyntaxException ex) {
      log.log(Level.SEVERE, "Cannot parse URL", ex);
    } catch (MalformedURLException ex) {
      log.log(Level.SEVERE, "Cannot connect to git", ex);
    } catch (IOException ex) {
      log.log(Level.SEVERE, "Cannot douwnload and extract multi project evaluator", ex);
    }
  }

  private static void expandSrcZip(Path dir) {
    File zip = Arrays.asList(dir.toFile().listFiles()).stream().filter(f -> !f.getName().equals("pom.xml")).findFirst()
        .orElse(null);
    if (zip == null) {
      log.warning("No ZIP file found.");
      return;
    }
    File tempZip = zip.toPath().getParent().resolve("temp.zip").toFile();
    if (!zip.renameTo(tempZip)) {
      log.warning(() -> "Cannot rename file: " + zip);
    }
    try {
      unzip(tempZip, tempZip.toPath().getParent());
    } catch (IOException e) {
      log.log(Level.WARNING, e, () -> "Cannot unzip file: " + zip.getAbsolutePath());
    }
  }

  private static void removeOneDirPath(Path dir) {
    while (isOneDir(dir)) {
      List<File> files = getFileListInProjectRoot(dir);
      log.info(() -> "item in dirs count: " + files.size());
      log.info(() -> files.stream().map(File::toString).collect(Collectors.joining(", ")));
      Path insideProject = files.stream().filter(File::isDirectory).map(File::toPath).findFirst().orElse(null);
      if (insideProject == null) {
        log.info("No singel dir found!!!");
        return;
      }
      log.info(() -> "Only one dir for: " + insideProject);
      File[] projectFiles = insideProject.toFile().listFiles();
      if (projectFiles == null) {
        log.warning(() -> "Is not folder: " + insideProject.toAbsolutePath().toString());
        return;
      }
      for (File file : projectFiles) {
        try {
          Files.move(file.toPath(), file.toPath().getParent().getParent().resolve(file.getName()));
        } catch (IOException e) {
          log.log(Level.SEVERE, e,
              () -> String.format("Cannot move %s to %s%n", file.toPath(), file.toPath().getParent().getParent()));
        }
      }
      try {
        Files.delete(insideProject);
      } catch (IOException e) {
        log.log(Level.SEVERE, e, () -> "Cannot delete " + insideProject);
      }
    }
  }

  private static void makeMavenProjectStructure(Path dir) {
    List<File> files = Arrays.asList(dir.toFile().listFiles());
    files.stream().filter(f -> !f.getName().equals("pom.xml")).forEach(file -> {
      try {
        String packageName = Files.lines(file.toPath()).filter(line -> line.startsWith("package "))
            .map(line -> line.substring("package ".length(), line.length() - 1)).findFirst().orElse("").trim();
        Path newDir = dir.resolve(Paths.get("src", "main", "java").resolve(Paths.get("", packageName.split("\\."))));
        if (!newDir.toFile().mkdirs()) {
          log.info(() -> "Directory exists: " + newDir.toAbsolutePath());
        }
        Files.move(file.toPath(), newDir.resolve(file.getName()));
      } catch (IOException e) {
        log.log(Level.WARNING, e, () -> "cannot read file: " + file);
      }
    });

  }

  private static void expandTarGz(Path dir) {
    Path tarGzFile = Arrays.stream(dir.toFile().listFiles()).filter(f -> f.getName().matches(TAR_GZ_PATTERN))
        .map(File::toPath).findFirst().orElse(null);
    if (tarGzFile == null) {
      log.warning("Cannot find tar.gz file.");
      return;
    }
    try {
      Files.move(tarGzFile, tarGzFile.getParent().resolve("to-expand.tar.gz"), StandardCopyOption.ATOMIC_MOVE);
    } catch (IOException e) {
      log.info("Cannot rename file: " + tarGzFile);
    }
  }

  private static final int BUFFER_SIZE = 4096;

  /**
   * Extracts a zip file specified by the zipFilePath to a directory specified by
   * destDirectory (will be created if does not exists)
   *
   * @param zipFilePath zip fiel path
   * @throws IOException if cannot be unzziped
   */
  public static void unzip(File zipFilePath, Path destDir) throws IOException {
    if (!destDir.toFile().exists() && !destDir.toFile().mkdir()) {
      log.info(() -> "Directory exists: " + destDir.toAbsolutePath());
    }
    try (InputStream in = new FileInputStream(zipFilePath)) {
      extractZipStream(in, destDir);
    }
  }

  private static void extractZipStream(InputStream zipInputStream, Path destDir) throws IOException {
    extractZipStream(zipInputStream, destDir, 0);
  }

  private static void extractZipStream(InputStream zipInputStream, Path destDir, int cutoffParent) throws IOException {
    ZipInputStream zipIn = new ZipInputStream(zipInputStream);
    ZipEntry entry = zipIn.getNextEntry();
    // iterates over entries in the zip file
    while (entry != null) {
      Path entryPath = Paths.get(entry.getName());
      if (entry.isDirectory() && entryPath.getNameCount() <= cutoffParent) {
        entryPath = Paths.get("");
      } else if (!entry.isDirectory() && entryPath.getNameCount() <= cutoffParent) {
        entryPath = entryPath.getFileName();
      } else {
        entryPath = entryPath.subpath(cutoffParent, entryPath.getNameCount());
      }
      Path filePath = destDir.resolve(entryPath);
      if (!entry.isDirectory()) {
        // if the entry is a file, extracts it
        extractFile(zipIn, filePath);
      } else {
        // if the entry is a directory, make the directory
        File dir = filePath.toFile();
        dir.mkdirs();
      }
      zipIn.closeEntry();
      entry = zipIn.getNextEntry();
    }
    zipIn.close();
  }

  /**
   * Extracts a zip entry (file entry)
   *
   * @param zipIn    zip in
   * @param filePath file path
   * @throws IOException an exception
   */
  private static void extractFile(ZipInputStream zipIn, Path filePath) throws IOException {
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath.toFile()));
    byte[] bytesIn = new byte[BUFFER_SIZE];
    int read = 0;
    while ((read = zipIn.read(bytesIn)) != -1) {
      bos.write(bytesIn, 0, read);
    }
    bos.close();
  }

  private static void extendPom(Path dir) {
    Path pom = dir.resolve("pom.xml");
    if (pom.toFile().exists()) {
      try {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(pom.toFile());
        if (!exist(doc, "/project/dependencies/dependency/artifactId[text() = 'spring-boot-starter-test']")) {
          ifNotExistAddLib(doc, "junit-jupiter-api", ORG_JUNIT_JUPITER, JUNIT_VERSION, "test", true);
          ifNotExistAddLib(doc, "junit-jupiter-engine", ORG_JUNIT_JUPITER, JUNIT_VERSION, "test", true);
          ifNotExistAddLib(doc, "junit-jupiter-params", ORG_JUNIT_JUPITER, JUNIT_VERSION, "test", true);
        }
        ifNotExistAddLib(doc, "kelvin-java-unittest-support", "cz.vsb.fei", "[3.15.9,)", "test", true);
        ifNotExistAddLib(doc, "kelvin-java-unittest-support", "cz.vsb.fei", "[3.15.9,)", "test", true);
        ifNotExistAddRep(doc, "vsb-education-release", "https://artifactory.cs.vsb.cz/repository/education-releases/");
        ifNotExistAddRep(doc, "vsb-education-snapshot", "https://artifactory.cs.vsb.cz/repository/education-snapshot/");
        removeIfExist(doc, "/project/build/plugins/plugin/configuration/failOnError", false);
        DOMSource source = new DOMSource(doc);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        FileWriter writer = new FileWriter(pom.toFile());
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
      } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException |
               TransformerException e) {
        log.log(Level.SEVERE, "ERR: Cannot pares pom!", e);
      }
    }
  }

  private static void ifNotExistAddLib(Document doc, String artifactId, String groupId, String version, String scope,
                                       boolean force) throws XPathExpressionException {
    if (force) {
      removeIfExist(doc, "/project/dependencies/dependency/artifactId[text() = '" + artifactId + "']", true);
    }
    XPath xPath = XPathFactory.newInstance().newXPath();
    Node dependencies = (Node) xPath.compile("/project/dependencies").evaluate(doc, XPathConstants.NODE);
    Node libNode = (Node) xPath.compile("/project/dependencies/dependency/artifactIdy")
        .evaluate(doc, XPathConstants.NODE);
    if (libNode == null) {
      log.log(Level.INFO, () -> String.format("Inserting lib '%s' into pom.xml", artifactId));
      Node dependency = doc.createElement("dependency");
      dependency.appendChild(doc.createElement("groupId").appendChild(doc.createTextNode(groupId)).getParentNode());
      dependency.appendChild(
          doc.createElement("artifactId").appendChild(doc.createTextNode(artifactId)).getParentNode());
      dependency.appendChild(doc.createElement("version").appendChild(doc.createTextNode(version)).getParentNode());
      dependency.appendChild(doc.createElement("scope").appendChild(doc.createTextNode(scope)).getParentNode());
      dependencies.appendChild(dependency);
    }
  }

  private static boolean exist(Document doc, String xpathQuery) throws XPathExpressionException {
    XPath xPath = XPathFactory.newInstance().newXPath();
    Node result = (Node) xPath.compile(xpathQuery).evaluate(doc, XPathConstants.NODE);
    return result != null;
  }

  private static void removeIfExist(Document doc, String xpathQuery, boolean parent) throws XPathExpressionException {
    XPath xPath = XPathFactory.newInstance().newXPath();
    Node result = (Node) xPath.compile(xpathQuery).evaluate(doc, XPathConstants.NODE);
    if (result != null && parent) {
      result = result.getParentNode();
    }
    if (result != null) {
      result.getParentNode().removeChild(result);
    }
  }

  private static void ifNotExistAddRep(Document doc, String id, String url) throws XPathExpressionException {
    XPath xPath = XPathFactory.newInstance().newXPath();
    Node libNode = (Node) xPath.compile("/project/repositories/repository/id[text() = '" + id + "']")
        .evaluate(doc, XPathConstants.NODE);
    if (libNode == null) {
      Node repositories = findOrAdd(doc, "/project", "repositories");
      Node repository = doc.createElement("repository");
      repository.appendChild(doc.createElement("id").appendChild(doc.createTextNode(id)).getParentNode());
      repository.appendChild(doc.createElement("url")).appendChild(doc.createTextNode(url));
      repositories.appendChild(repository);
    }
  }

  private static Node findOrAdd(Document doc, String xpathQuery, String name) throws XPathExpressionException {
    XPath xPath = XPathFactory.newInstance().newXPath();
    Node result = (Node) xPath.compile(xpathQuery + "/" + name).evaluate(doc, XPathConstants.NODE);
    if (result == null) {
      Node parent = (Node) xPath.compile(xpathQuery).evaluate(doc, XPathConstants.NODE);
      result = doc.createElement(name);
      parent.appendChild(result);
    }
    return result;
  }

  private static final List<String> IGNORE_FILE_NAMES = List.of("AppTest.java");

  private static void findMain(Path rootDir) {
    List<Path> files = getJavaSrcFiles(rootDir);
    log.info(() -> rootDir + ": " + files.size());
    List<Path> filesWithMain = findFileWithMains(files);
    log.info(() -> String.format("Found %d main functions.", filesWithMain.size()));
    List<String> classWithRenamedMain = renameMainDefinition(filesWithMain);
    renameMainUsage(files, classWithRenamedMain);
  }

  private static void renameMainUsage(List<Path> javaFiles, List<String> classWithRenamedMain) {
    for (Path javaFile : javaFiles) {
      try {
        String content = Files.readString(javaFile);
        boolean needSave = false;
        for (String className : classWithRenamedMain) {
          if (content.contains(className + ".main(")) {
            content = content.replace(className + ".main(", className + ".main2(");
            needSave = true;
          }
        }
        if (needSave) {
          Files.writeString(javaFile, content, StandardOpenOption.TRUNCATE_EXISTING);
        }
      } catch (IOException e) {
        log.log(Level.SEVERE, "Cannot rename main method usage in srcs", e);
      }
    }
  }

  private static List<String> renameMainDefinition(List<Path> filesWithMain) {
    List<String> result = new ArrayList<>();
    if (filesWithMain.size() > 1) {
      filesWithMain.sort(Comparator.comparing(p -> p.toAbsolutePath().toString().length()));
      for (Path file : filesWithMain) {
        try {
          String content = Files.readString(file);
          content = mainPatter.matcher(content).replaceAll("public static void main2(String[] args)");
          Files.writeString(file, content, StandardOpenOption.TRUNCATE_EXISTING);
          result.add(file.getFileName().toString().substring(0, file.getFileName().toString().lastIndexOf('.')));
        } catch (IOException e) {
          log.log(Level.SEVERE, "Cannot rename main method definition in srcs", e);
        }
      }
    }
    return result;
  }

  private static List<Path> findFileWithMains(List<Path> files) {
    List<Path> result = new ArrayList<>();
    for (Path javaFile : files) {
      try {
        if (mainPatter.matcher(Files.readString(javaFile)).find()) {
          result.add(javaFile);
        }
      } catch (IOException e) {
        log.log(Level.SEVERE, e, () -> "Cannot read file: " + javaFile);
      }
    }
    return result;
  }

  private static List<Path> getJavaSrcFiles(Path rootDir) {
    List<Path> files = findFiles(rootDir, ".*\\.java").stream()
        .filter(path -> !IGNORE_FILE_NAMES.contains(path.getFileName().toString())).toList();
    return files;
  }

  public static List<Path> findFiles(Path dir, String regexp) {
    try {
      Pattern p = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
      List<Path> result = new LinkedList<>();
      Files.walkFileTree(dir, new FileVisitor<Path>() {

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
          if (skipNames.contains(dir.getFileName().toString())) {
            return FileVisitResult.SKIP_SUBTREE;
          }
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          if (p.matcher(file.getFileName().toString()).matches()) {
            result.add(file);
          }
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
          return FileVisitResult.CONTINUE;
        }
      });
      return result;
    } catch (IOException e) {
      log.log(Level.SEVERE, e, () -> "Cannot inspect dir tree: " + dir);
      return Collections.emptyList();
    }
  }

}
