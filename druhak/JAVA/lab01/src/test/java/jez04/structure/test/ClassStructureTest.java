package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.ClassExist;
import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.HasProperty;
import cz.vsb.fei.kelvin.unittest.OutputContains;
import cz.vsb.fei.kelvin.unittest.SrcContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import lab.DrawingThread;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;

class ClassStructureTest {
    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

    @Test
    void testGreetingClassExists() {
        assertThat(ClassStructureTest.class, new ClassExist("Greeting"));
    }

    @Test
    void testGreetingRun() {
        assertThat(() -> {
            try {
                Class<?> greeting = helper.getClass("Greeting");
                Method main = helper.getMethod(greeting, "main", void.class, String[].class);
                main.invoke(null, new String[1]);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }, new OutputContains("world").caseSensitive(false));
    }

    @Test
    void testPrimesClassExists() {
        assertThat(ClassStructureTest.class, new ClassExist("Greeting"));
    }

    @Test
    void testPrimesRun() {
        assertThat(() -> {
            try {
                Class<?> primes = helper.getClass("Primes");
                Method main = helper.getMethod(primes, "main", void.class, String[].class);
                main.invoke(null, new String[1]);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }, new OutputContains("97").caseSensitive(false));
    }

    @Test
    void testDrawing() {
        assertThat(DrawingThread.class, new SrcContains("gc.").count(4));
    }

    @Test
    void testMethodDrawPicture() {
        assertThat(DrawingThread.class, new HasMethod("drawPicture", void.class, double.class, double.class));
    }

    @Test
    void testInstanceVariables() {
        assertThat(DrawingThread.class, new HasProperty(".*", double.class, false).count(2));
    }
}
