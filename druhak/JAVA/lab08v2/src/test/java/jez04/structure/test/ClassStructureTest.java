package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.ClassExist;
import cz.vsb.fei.kelvin.unittest.HasConstructor;
import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.HasProperty;
import cz.vsb.fei.kelvin.unittest.IsDescendatOf;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class ClassStructureTest {
    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

    @Test
    void testScoreExists() {
        assertThat(ClassStructureTest.class, new ClassExist("Score"));
    }

    @Test
    void testScoreProperties() throws ClassNotFoundException {
        Class<?> score = helper.getClass("Score");
        assertThat(score, new HasProperty(".*", String.class));
    }

    @Test
    void testScoreProperties2() throws ClassNotFoundException {
        Class<?> score = helper.getClass("Score");
        assertThat(score, new HasProperty(".*", int.class));
    }

    @Test
    void testScoreGetter1() throws ClassNotFoundException {
        Class<?> score = helper.getClass("Score");
        assertThat(score, new HasMethod("get.*", int.class).useRegExp(true));
    }
    @Test
    void testScoreGette2() throws ClassNotFoundException {
        Class<?> score = helper.getClass("Score");
        assertThat(score, new HasMethod("get.*", String.class).useRegExp(true));
    }

    @Test
    void testScoreConstructor() throws ClassNotFoundException {
        Class<?> score = helper.getClass("Score");
        assertThat(score, new HasConstructor(String.class, int.class));
    }

    @Test
    void testScoreMethod() throws ClassNotFoundException {
        Class<?> score = helper.getClass("Score");
        assertThat(score, new HasMethod("gene.*", score).staticTag(true).useRegExp(true));
    }


    @Test
    void testMenuControllerExists() {
        assertThat(ClassStructureTest.class, new ClassExist("MenuController"));
    }

    @Test
    void testMenuControllerColumn() throws ClassNotFoundException {
        Class<?> score = helper.getClass("MenuController");
        assertThat(score, new HasProperty(".*", TableColumn.class).annotation(FXML.class).count(2));
    }

    @Test
    void testMenuControllerTable() throws ClassNotFoundException {
        Class<?> score = helper.getClass("MenuController");
        assertThat(score, new HasProperty(".*", TableView.class).annotation(FXML.class));
    }

    @Test
    void testMenuControllerButtonHandlers() throws ClassNotFoundException {
        Class<?> score = helper.getClass("MenuController");
        assertThat(score, new HasMethod(".*", void.class, ActionEvent.class).annotation(FXML.class).count(3).useRegExp(true));
    }

    @Test
    void testAppMethod() throws ClassNotFoundException {
        Class<?> score = helper.getClass("App");
        assertThat(score, new HasMethod("switchToMenu", void.class).anyParam(true));
    }

    @Test
    void testAppMethod2() throws ClassNotFoundException {
        Class<?> score = helper.getClass("App");
        assertThat(score, new HasMethod("switchToGame", void.class).anyParam(true));
    }

    @Test
    void testScoreExceptionExists() {
        assertThat(ClassStructureTest.class, new ClassExist("ScoreException"));
    }

    @Test
    void testScoreExceptionExtends() throws ClassNotFoundException {
        Class<?> scoreException = helper.getClass("ScoreException");
        assertThat(scoreException, new IsDescendatOf(Exception.class));
    }

    @Test
    void testScoreRepositoryExists() {
        assertThat(ClassStructureTest.class, new ClassExist("ScoreRepository"));
    }

    @Test
    void testScoreRepositoryMethod() throws ClassNotFoundException {
        Class<?> scoreRepository = helper.getClass("ScoreRepository");
        assertThat(scoreRepository, new HasMethod(".*", void.class, List.class).useRegExp(true));
    }

    @Test
    void testScoreRepositoryMethod2() throws ClassNotFoundException {
        Class<?> scoreRepository = helper.getClass("ScoreRepository");
        Class<?> scoreException = helper.getClass("ScoreException");
        assertThat(scoreRepository, new HasMethod(".*", void.class, List.class).useRegExp(true).throwsEx(scoreException));
    }

    @Test
    void testScoreRepositoryMethod3() throws ClassNotFoundException {
        Class<?> scoreRepository = helper.getClass("ScoreRepository");
        assertThat(scoreRepository, new HasMethod(".*", List.class).useRegExp(true));
    }

    @Test
    void testScoreRepositoryMethod4() throws ClassNotFoundException {
        Class<?> scoreRepository = helper.getClass("ScoreRepository");
        Class<?> scoreException = helper.getClass("ScoreException");
        assertThat(scoreRepository, new HasMethod(".*", List.class).useRegExp(true).throwsEx(scoreException));
    }

    @Test
    void testScoreRepositorySave() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
        IllegalAccessException {
        Class<?> scoreRepository = helper.getClass("ScoreRepository");
        Method save = helper.getMethod(scoreRepository, ".*", void.class, List.class);
        Class<?> score = helper.getClass("Score");
        Method generate = helper.getMethod(score, "gene.*", score);
        List<Object> scores = new ArrayList<>();
        scores.add(generate.invoke(null ));
        deleteAllCsvFiles();
        save.invoke(null, scores);
        printAllFiles();
        int csvCounts = List.of(Paths.get("").toAbsolutePath().toFile()
            .listFiles(file -> file.getName().toLowerCase().endsWith(".csv"))).size();
        assertThat("Saved csv files count have to be grater then 1.", csvCounts, Matchers.greaterThan(0));
    }

    @Test
    void testScoreRepositoryLoad() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
        IllegalAccessException {
        Class<?> scoreRepository = helper.getClass("ScoreRepository");
        Method save = helper.getMethod(scoreRepository, ".*", void.class, List.class);
        Method load = helper.getMethod(scoreRepository, ".*", List.class);
        Class<?> score = helper.getClass("Score");
        Method generate = helper.getMethod(score, "gene.*", score);
        List<Object> scores = new ArrayList<>();
        scores.add(generate.invoke(null));
        deleteAllCsvFiles();
        save.invoke(null, scores);

        Object result = load.invoke(null);
        if(result instanceof List<?> resultList){
            assertThat("Loaded List of score should have size 1.", resultList, Matchers.hasSize(1));
        } else {
            Assertions.fail("Load method do not return List");
        }
    }

    private static void printAllFiles() {
        List.of(Paths.get("").toAbsolutePath().toFile().list()).forEach(System.out::println);
    }

    private static void deleteAllCsvFiles() {
        List.of(Paths.get("").toAbsolutePath().toFile()
                .listFiles(file -> file.getName().toLowerCase().endsWith(".csv")))
            .forEach(File::delete);
    }
    
}

