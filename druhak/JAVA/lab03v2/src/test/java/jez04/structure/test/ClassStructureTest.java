package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.ClassExist;
import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.HasProperty;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import java.util.Random;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class ClassStructureTest {
    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

    @Test
    void monsterClassEexistanceTest() {
        assertThat(ClassStructureTest.class, new ClassExist("Monster"));
    }

    @Test
    void monsterClassPropertiesTest() throws ClassNotFoundException {
        Class<?> monster = helper.getClass("Monster");
        Class<?> level = helper.getClass("Level", false);
        assertThat(monster, new HasProperty(".*", level, false));
    }

    @Test
    void monsterClassProperties2Test() throws ClassNotFoundException {
        Class<?> monster = helper.getClass("Monster");
        assertThat(monster, new HasProperty(".*", Random.class, false));
    }

    @Test
    void monsterClassMethodsTest() throws ClassNotFoundException {
        Class<?> monster = helper.getClass("Monster");
        assertThat(monster, new HasMethod("draw", void.class, GraphicsContext.class));
        assertThat(monster, new HasMethod("simulate", void.class, double.class));
    }


    @Test
    void monsterClassMethods2Test() throws ClassNotFoundException {
        Class<?> monster = helper.getClass("Monster");
        assertThat(monster, new HasMethod("getBoundingBox", Rectangle2D.class));
    }

    @Test
    void monsterClassMethods3Test() throws ClassNotFoundException {
        Class<?> monster = helper.getClass("Monster");
        assertThat(monster, new HasMethod("changeDirection", void.class));
    }

    @Test
    void levelClassPropertyTest() throws ClassNotFoundException {
        Class<?> level = helper.getClass("Level", false);
        Class<?> monster = helper.getClass("Monster");
        assertThat(level, new HasProperty(".*", monster.arrayType(), false));
    }

    @Test
    void levelClassProperty2Test() throws ClassNotFoundException {
        Class<?> level = helper.getClass("Level", false);
        Class<?> obstacle = helper.getClass("lab.Obstacle");
        assertThat(level, new HasProperty(".*", obstacle.arrayType(), false));
    }

    @Test
    void obstacleClassPropertiesTest() throws ClassNotFoundException {
        Class<?> obstacle = helper.getClass("lab.Obstacle");
        assertThat(obstacle, new HasProperty(".*", Random.class, false));
    }

    @Test
    void playeClassMethodTest() throws ClassNotFoundException {
        Class<?> player = helper.getClass("Player", false);
        assertThat(player, new HasMethod("randomBounce", void.class));
    }

}
