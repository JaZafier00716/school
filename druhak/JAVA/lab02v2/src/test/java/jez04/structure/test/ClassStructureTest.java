package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.ClassExist;
import cz.vsb.fei.kelvin.unittest.HasConstructor;
import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class ClassStructureTest {
    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

    @Test
    void testPlayerClassExists() {
        assertThat(ClassStructureTest.class, new ClassExist("Player"));
    }
    @Test
    void testPlayerMethods() throws ClassNotFoundException {
        Class<?> cannon = helper.getClass("Player");
        assertThat(cannon, new HasMethod("draw", void.class, GraphicsContext.class));
        assertThat(cannon, new HasMethod("simulate", void.class, double.class));
    }

    @Test
    void testLevelClassExists() {
        assertThat(ClassStructureTest.class, new ClassExist("Level"));
    }

    @Test
    void testLevelMethods() throws ClassNotFoundException {
        Class<?> level = helper.getClass("Level");
        assertThat(level, new HasMethod("draw", void.class, GraphicsContext.class));
        assertThat(level, new HasMethod("simulate", void.class, double.class));
    }

    @Test
    void testObstacleClassExists() {
        assertThat(ClassStructureTest.class, new ClassExist("lab.Obstacle"));
    }

    @Test
    void testObstacleConstructors() throws ClassNotFoundException {
        Class<?> obstacle = helper.getClass("lab.Obstacle");
        Class<?> level = helper.getClass("Level");
        assertThat(obstacle, new HasConstructor(level, Point2D.class, Dimension2D.class));
        assertThat(obstacle, new HasConstructor(level));
    }

    @Test
    void testObstacleMethods() throws ClassNotFoundException {
        Class<?> level = helper.getClass("lab.Obstacle");
        assertThat(level, new HasMethod("draw", void.class, GraphicsContext.class));
        assertThat(level, new HasMethod("simulate", void.class, double.class));
    }

    @Test
    void testNicerObstacleClassExists() {
        assertThat(ClassStructureTest.class, new ClassExist("NicerObstacle"));
    }

    @Test
    void testNicerObstacleConstructors() throws ClassNotFoundException {
        Class<?> obstacle = helper.getClass("NicerObstacle");
        Class<?> level = helper.getClass("Level");
        assertThat(obstacle, new HasConstructor(level, Point2D.class));
    }

    @Test
    void testNicerObstacleMethods() throws ClassNotFoundException {
        Class<?> level = helper.getClass("NicerObstacle");
        assertThat(level, new HasMethod("draw", void.class, GraphicsContext.class));
        assertThat(level, new HasMethod("simulate", void.class, double.class));
    }

}
