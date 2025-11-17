package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.ClassExist;
import cz.vsb.fei.kelvin.unittest.ContainsInnerClasses;
import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.HasProperty;
import cz.vsb.fei.kelvin.unittest.SrcContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import java.util.Collection;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class ClassStructureTest {
    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

    @Test
    void testMonsterProperty() throws ClassNotFoundException {
        Class<?> monster = helper.getClass("Monster");
        assertThat(monster, new HasProperty(".*", List.class));
    }

    @Test
    void testMonsterMethod() throws ClassNotFoundException {
        Class<?> monster = helper.getClass("Monster");
        Class<?> deadListener = helper.getClass("DeadListener");
        assertThat(monster, new HasMethod("addDeadListener", boolean.class, deadListener));
        assertThat(monster, new HasMethod("removeDeadListener", boolean.class, deadListener));
    }

    @Test
    void testMonsterMethod2() throws ClassNotFoundException {
        Class<?> monster = helper.getClass("Monster");
        assertThat(monster, new HasMethod("fire.*", void.class).useRegExp(true));
    }

    @Test
    void testGameControllerProperty3() throws ClassNotFoundException {
        Class<?> gameController = helper.getClass("GameController");
        assertThat(gameController, new HasProperty(".*", Label.class).annotation(FXML.class));
    }

    @Test
    void testGameControllerProperty4() throws ClassNotFoundException {
        Class<?> gameController = helper.getClass("GameController");
        assertThat(gameController, new HasProperty(".*", int.class));
    }

    @Test
    void testGameControllerMethod() throws ClassNotFoundException {
        Class<?> gameController = helper.getClass("GameController");
        assertThat(gameController, new HasMethod(".*coun.*", void.class).useRegExp(true).caseSensitive(false));
    }

    @Test
    void testGameControllerLambdaAndMethodReference() throws ClassNotFoundException {
        Class<?> gameController = helper.getClass("GameController");
        assertThat(gameController, new ContainsInnerClasses(2));
    }

    @Test
    void testDeadListenerMethod() throws ClassNotFoundException {
        Class<?> deadListener = helper.getClass("DeadListener");
        assertThat(deadListener, new HasMethod("monsterDead", void.class));
    }

    @Test
    void testDeadListenerExistence() {
        assertThat(ClassStructureTest.class, new ClassExist("DeadListener"));
    }

    @Test
    void testWorldProperties() throws ClassNotFoundException {
        Class<?> level = helper.getClass("Level");
        assertThat(level, new HasProperty(".*", List.class));
    }
    @Test
    void testLevelProperties2() throws ClassNotFoundException {
        Class<?> level = helper.getClass("Level");
        assertThat(level, new HasProperty(".*", Collection.class).count(2));
    }

    @Test
    void testLevelMethods() throws ClassNotFoundException {
        Class<?> level = helper.getClass("Level");
        Class<?> drawableSimulable = helper.getClass("DrawableSimulable");
        assertThat(level, new HasMethod("add.*", void.class, drawableSimulable).useRegExp(true).caseSensitive(false));
        assertThat(level, new HasMethod("remove.*", void.class, drawableSimulable).useRegExp(true).caseSensitive(false));
    }

    @Test
    void testLevelUseCollections() throws ClassNotFoundException {
        Class<?> level = helper.getClass("Level");
        assertThat(level, new SrcContains("\\.addAll\\("));
        assertThat(level, new SrcContains("\\.clear\\(\\)"));
    }

    @Test
    void testLevelUseCollections2() throws ClassNotFoundException {
        Class<?> level = helper.getClass("Level");
        assertThat(level, new SrcContains("\\.removeAll\\("));
        assertThat(level, new SrcContains("\\.clear\\(\\)"));
    }


}
