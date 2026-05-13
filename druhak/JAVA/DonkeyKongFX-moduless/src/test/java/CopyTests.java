import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CopyTests {
  static {
    System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s: %5$s%6$s%n");
  }

  private static Logger log = java.util.logging.Logger.getLogger(CopyTests.class.getName());
  public static final List<String> skipNames = List.of("target", ".settings", ".project", ".classpath");
  public static final String TAR_GZ_PATTERN = ".*\\.tar\\.gz";

  public static void main(String[] args) {
    try {
      if (args.length == 0) {
        printUsage();
      }
      String destProjectDir = null;
      String templateSourceDir = null;
      for (String arg : args) {
        if (destProjectDir == null) {
          destProjectDir = arg;
        } else if (templateSourceDir == null) {
          templateSourceDir = arg;
        } else {
          log.log(Level.WARNING, () -> String.format("Uknown argument '%s'.", arg));
        }
      }
      if (destProjectDir == null) {
        destProjectDir = ".";
      }
      if (templateSourceDir == null) {
        templateSourceDir = "/template/java-tests/";
      }
      if(isPomParentPom(Path.of(destProjectDir)) && allTestDefineChild(Paths.get(templateSourceDir))){
        copyTestsToModules(Paths.get(templateSourceDir), Paths.get(destProjectDir));
      } else {
        copyTests(Paths.get(templateSourceDir), Paths.get(destProjectDir));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void printUsage() {
    log.info("Usage:\n  java [--source 21 --enable-preview] CopyTest.java [<destProjectDir> [<sourceDir>]]");
  }

  private static boolean isPomParentPom(Path destProjectDir) {
    try {
      String content = Files.readString(destProjectDir.resolve("pom.xml"));
      return content.contains("<modules>");
    } catch (IOException e) {
      log.log(Level.SEVERE, e, () -> "Error reading pom.xml content");
      return false;
    }
  }

  private static boolean allTestDefineChild(Path templateDir) {
    List<File> testFiles = List.of(templateDir.toFile().listFiles());
    return testFiles.stream().filter(file -> file.getName().endsWith(".java")).allMatch(file -> {
      try {
        List<String> content = Files.readAllLines(file.toPath());
        return content.getFirst().matches("\\w*//child:.*");
      } catch (IOException e) {
        log.log(Level.SEVERE, e, () -> "Error reading pom.xml content for: " + file.toPath().toAbsolutePath());
        return false;
      }
    });
  }

  private static void copyTestsToModules(Path sourceDir, Path descProjectDir) {
    List<File> javaTestFiles = Arrays.asList(sourceDir.toFile().listFiles()).stream()
        .filter(f -> f.getName().matches(".*\\.java")).toList();
    List<File> moduleDirs = List.of(descProjectDir.toFile().listFiles(filePath -> filePath.isDirectory()));
    
    for (File file : javaTestFiles) {
      try {
        List<String> content = Files.readAllLines(file.toPath());
        String firstLine = content.getFirst();
        String parts[] = firstLine.split("\\w*//child:");
        String regExpForModuleName = parts[1];
        File moduleDir = moduleDirs.stream().filter(module -> module.getName().matches(regExpForModuleName)).findFirst()
            .orElseThrow();
        copyTest(file, moduleDir.toPath());
      } catch (IOException e) {
        log.log(Level.SEVERE, e, () -> "Error reading pom.xml content for: " + file.toPath().toAbsolutePath());
      }

    }
  }

  private static void copyTest(File file, Path descProjectDir) {
    Path javaTestDir = descProjectDir.resolve("src").resolve("test").resolve("java");
    try (Stream<String> lines = Files.lines(file.toPath())) {
      String packagePath = lines.filter(line -> !line.isBlank()).filter(line -> line.matches("\\s*package .*"))
          .findFirst().map(line -> line.replaceAll("\\s*package ", "").replace(";", ""))
          .map(packageStr -> packageStr.replace('.', '/')).orElse("");
      Path target = javaTestDir.resolve(packagePath);
      target.toFile().mkdirs();
      Files.copy(file.toPath(), target.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      log.log(Level.SEVERE, "Cannot copy test file.", e);
    }

  }

  private static void copyTests(Path sourceDir, Path descProjectDir) {
    System.out.println("Test source dir: " + sourceDir.toAbsolutePath());
    System.out.println("Test target dir: " + descProjectDir.toAbsolutePath());
    List<File> javaTestFiles = Arrays.asList(sourceDir.toFile().listFiles()).stream()
        .filter(f -> f.getName().matches(".*\\.java")).toList();
    for (File file : javaTestFiles) {
      copyTest(file, descProjectDir);
    }

  }

  private static void download(String urlString, Path destPath, boolean unzip, int cutoffParrent) {
    try (InputStream inputStream = new URI(urlString).toURL().openStream()) {
      if (unzip) {
        destPath.toFile().mkdirs();
        extractZipStream(inputStream, destPath, cutoffParrent);
      } else {
        Path parent = destPath.getParent();
        if (parent != null) {
          parent.toFile().mkdirs();
        }
        Files.copy(inputStream, destPath, StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (MalformedURLException ex) {
      log.log(Level.SEVERE, "Cannot connect to git", ex);
    } catch (IOException ex) {
      log.log(Level.SEVERE, "Cannot douwnload and extract multi project evaluator", ex);
    } catch (URISyntaxException ex) {
      log.log(Level.SEVERE, "Cannot parse URL", ex);
    }
  }

  private static final int BUFFER_SIZE = 4096;

  /**
   * Extracts a zip file specified by the zipFilePath to a directory specified by
   * destDirectory (will be created if does not exists)
   *
   * @param zipFilePath
   * @throws IOException
   */
  public static void unzip(File zipFilePath, Path destDir) throws IOException {
    if (!destDir.toFile().exists()) {
      destDir.toFile().mkdir();
    }
    try (InputStream in = new FileInputStream(zipFilePath)) {
      extractZipStream(in, destDir);
    }
  }

  private static void extractZipStream(InputStream zipInputStream, Path destDir) throws IOException {
    extractZipStream(zipInputStream, destDir, 0);
  }

  private static void extractZipStream(InputStream zipInputStream, Path destDir, int cutoffParent) throws
      FileNotFoundException, IOException {
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
   * @param zipIn
   * @param filePath
   * @throws IOException
   */
  private static void extractFile(ZipInputStream zipIn, Path filePath) throws IOException {
    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath.toFile()))) {
      byte[] bytesIn = new byte[BUFFER_SIZE];
      int read = 0;
      while ((read = zipIn.read(bytesIn)) != -1) {
        bos.write(bytesIn, 0, read);
      }
    }
  }

}
