package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.ClassExist;
import cz.vsb.fei.kelvin.unittest.HasExtends;
import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.HasProperty;
import cz.vsb.fei.kelvin.unittest.SrcContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import java.lang.reflect.Modifier;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class ClassStructureTest {
    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

    @Test
    void drawableSimulableExistanceTest() {
        assertThat(ClassStructureTest.class, new ClassExist("DrawableSimulable"));
    }

    @Test
    void drawableSimulableMethodsTest() throws ClassNotFoundException {
        Class<?> drawableSimulable = helper.getClass("DrawableSimulable");
        assertThat(drawableSimulable, new HasMethod("draw", void.class, GraphicsContext.class));
        assertThat(drawableSimulable, Matchers.anyOf(new HasMethod("simulate", void.class, double.class),
            new HasMethod("simulate", void.class, Double.class)));
    }

    @Test
    void levelClassPropertyArrayTest() throws ClassNotFoundException {
        Class<?> level = helper.getClass("Level", false);
        Class<?> drawableSimulable = helper.getClass("DrawableSimulable");
        assertThat(level, new HasProperty(".*", drawableSimulable, true));
    }

    @Test
    void collisionableEexistanceTest() {
        assertThat(ClassStructureTest.class, new ClassExist("Collisionable"));
    }

    @Test
    void collisionableMethodsTest() throws ClassNotFoundException {
        Class<?> collisionable = helper.getClass("Collisionable");
        assertThat(collisionable, new HasMethod("getBoundingBox", Rectangle2D.class));
        assertThat(collisionable, new HasMethod("intersect", boolean.class, Rectangle2D.class));
        assertThat(collisionable, new HasMethod("hitBy", void.class, collisionable));
    }

    @Test
    void worldEntityTest() throws ClassNotFoundException {
        assertThat(ClassStructureTest.class, new ClassExist("WorldEntity"));
        Class<?> level = helper.getClass("Level");
        assertThat(level, new SrcContains("instanceof Collisionable"));
    }

    @Test
    void worldEntity2Test() throws ClassNotFoundException {
        Class<?> worldEntity = helper.getClass("WorldEntity");
        Assertions.assertTrue(Modifier.isAbstract(worldEntity.getModifiers()), "WorldEntity should be abstract");
    }

    @Test
    void worldEntityPropertiesTest() throws ClassNotFoundException {
        Class<?> worldEntity = helper.getClass("WorldEntity");
        Class<?> level = helper.getClass("Level");
        assertThat(worldEntity, new HasProperty(".*", Point2D.class, false));
        assertThat(worldEntity, new HasProperty(".*", level, false));

    }

    @Test
    void worldEntityMethodsTest() throws ClassNotFoundException {
        Class<?> worldEntity = helper.getClass("WorldEntity");
        assertThat(worldEntity, new HasMethod("draw", void.class, GraphicsContext.class).finalTag(true));
        assertThat(worldEntity, new HasMethod("drawInternal", void.class, GraphicsContext.class).abstractTag(true));
        assertThat(worldEntity, new HasMethod("getPosition", Point2D.class));
    }

    @Test
    void nicerObstacleExtendsTest() throws ClassNotFoundException {
        Class<?> nicerObstacle = helper.getClass("NicerObstacle");
        assertThat(nicerObstacle, new HasExtends("lab.Obstacle"));
    }

    @Test
    void playerMethodsTest() throws ClassNotFoundException {
        Class<?> player = helper.getClass("Player");
        assertThat(player, new HasMethod("setAngle", void.class, double.class));
        assertThat(player, new HasMethod("respawn", void.class));
    }
}
