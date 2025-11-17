package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.ClassExist;
import cz.vsb.fei.kelvin.unittest.HasConstructor;
import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.HasProperty;
import cz.vsb.fei.kelvin.unittest.OutputContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import lab.base.BaseStructure;
import lab.cars.CarTasks;
import lab.regexp.Parser;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class ClassStructureTest {

    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

    @Test
    void varArgMethodTest() {
        MatcherAssert.assertThat(BaseStructure.class, new HasMethod("calcSum", String.class, int.class.arrayType()));
    }

    @Test
    void varArgMethod2Test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method sum = helper.getMethod(BaseStructure.class, "calcSum", String.class, int.class.arrayType());
        String result = (String) sum.invoke(new BaseStructure(), new int[]{1, 2, -8, 10});
        MatcherAssert.assertThat(result,
            Matchers.matchesPattern("1\\s*\\+\\s*2\\s*\\+\\s*\\(\\s*-8\\s*\\)\\s*\\+\\s*10\\s*=\\s*5"));
    }

    @Test
    void varArgMethod3Test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MatcherAssert.assertThat(() -> new BaseStructure().varArgTask(), new OutputContains("\\+.*=\\s\\d"));
    }

    @Test
    void parseWordTest() {
        Parser parser = new Parser();
        MatcherAssert.assertThat(parser.splitToWorlds("""
                Toto je zkušební text.
                Něco jako   tab a	tab.
                Další(ano) část.

                ()
                jako jako ano,ano
                """),
            Matchers.containsInAnyOrder("toto", "a", "ano", "tab", "další", "zkušební", "část", "jako", "je", "text",
                "něco"));
    }

    @Test
    void parseWord2Test() {
        Parser parser = new Parser();
        MatcherAssert.assertThat(parser.splitToWorlds("""
            Toto je zkušební text
            """), Matchers.containsInAnyOrder("toto", "zkušební", "je", "text"));
    }

    @Test
    void parseNumbersTest() {
        Parser parser = new Parser();
        MatcherAssert.assertThat(parser.findNumbers("""
            Toto je zkušební text.
            7 9.5
            700 -987.012
            Něco jako   tab a	ta11b.
            Další(ano) čá23.8st.

            ()
            jako jako ano,ano
            """), Matchers.containsInAnyOrder(7.0f, 9.5f, 700.0f, -987.012f, 11.0f, 23.8f));
    }

    @Test
    void parseNumbers2Test() {
        Parser parser = new Parser();
        MatcherAssert.assertThat(parser.findNumbers("""
            7 9.5
            """), Matchers.containsInAnyOrder(7.0f, 9.5f));
    }

    @Test
    void printAllCarProducerTest() {
        MatcherAssert.assertThat(() -> new CarTasks().printAllCarProducers(), new OutputContains("Porsche"));
        MatcherAssert.assertThat(() -> new CarTasks().printAllCarProducers(), new OutputContains("BMW"));
        MatcherAssert.assertThat(() -> new CarTasks().printAllCarProducers(), new OutputContains("Škoda"));
        MatcherAssert.assertThat(() -> new CarTasks().printAllCarProducers(),
            new OutputContains("Sheer Driving Pleasure"));
        MatcherAssert.assertThat(() -> new CarTasks().printAllCarProducers(), new OutputContains("Driven by Dreams"));
        MatcherAssert.assertThat(() -> new CarTasks().printAllCarProducers(), new OutputContains("Let´s Explore"));
    }

    @Test
    void CarProducerTest() {
        MatcherAssert.assertThat(ClassStructureTest.class, new ClassExist("CarProducer"));
    }

    @Test
    void CarProducerEnumTest() throws ClassNotFoundException {
        Class<?> carProducer = helper.getClass("CarProducer");
        MatcherAssert.assertThat("CarProducer have to be enum.", carProducer.isEnum(), Matchers.is(true));
    }

    @Test
    void CarProducerConstructorTest() throws ClassNotFoundException {
        Class<?> carProducer = helper.getClass("CarProducer");
        MatcherAssert.assertThat(carProducer, new HasConstructor(String.class, int.class, String.class, String.class));
    }

    @Test
    void CarProducerMethodTest() throws ClassNotFoundException {
        Class<?> carProducer = helper.getClass("CarProducer");
        MatcherAssert.assertThat(carProducer, new HasMethod("getName", String.class));
        MatcherAssert.assertThat(carProducer, new HasMethod("getSlogan", String.class));
        MatcherAssert.assertThat(carProducer, new HasMethod("toString", String.class));
    }

    @Test
    void CarTest() {
        MatcherAssert.assertThat(ClassStructureTest.class, new ClassExist("Car"));
    }

    @Test
    void CarParamsTest() throws ClassNotFoundException {
        Class<?> car = helper.getClass("Car");
        Class<?> carProducer = helper.getClass("CarProducer");
        MatcherAssert.assertThat(car, new HasProperty(".*", carProducer));
        MatcherAssert.assertThat(car, new HasProperty(".*", String.class));
        MatcherAssert.assertThat(car, new HasProperty(".*", float.class));
    }

    @Test
    void CarConstructorTest() throws ClassNotFoundException {
        Class<?> car = helper.getClass("Car");
        Class<?> carProducer = helper.getClass("CarProducer");
        MatcherAssert.assertThat(car, new HasConstructor(carProducer, String.class, float.class));
    }

    @Test
    void CarMethodTest() throws ClassNotFoundException {
        Class<?> car = helper.getClass("Car");
        MatcherAssert.assertThat(car, new HasMethod("toString", String.class));
    }

    @Test
    void generateTwoCarsTest() throws ClassNotFoundException {
        CarTasks carTasks = new CarTasks();
        Class<?> car = helper.getClass("Car");
        List<?> result = carTasks.generateTwoCars();
        MatcherAssert.assertThat(result, Matchers.hasSize(2));
        MatcherAssert.assertThat(result, Matchers.everyItem(Matchers.instanceOf(car)));
    }

    @Test
    void storeTest() throws ClassNotFoundException {
        CarTasks carTasks = new CarTasks();
        boolean result = carTasks.store("test-car-store.bin");
        MatcherAssert.assertThat("Method store should return true", result, Matchers.is(true));
        MatcherAssert.assertThat("File test-car.bin have to be created", new File("test-car-store.bin").exists(),
            Matchers.is(true));
        MatcherAssert.assertThat("File test-car.bin have to be bigger then 200B",
            new File("test-car-store.bin").length(), Matchers.greaterThan(200L));
    }

    @Test
    void loadTest() throws ClassNotFoundException {
        CarTasks carTasks = new CarTasks();
        carTasks.store("test-car-load.bin");
        boolean result = carTasks.load("test-car-load.bin");
        MatcherAssert.assertThat("Method load should return true", () -> carTasks.load("test-car-load.bin"),
            new OutputContains("km/h").count(2));
        MatcherAssert.assertThat("Method load should return true", result, Matchers.is(true));
    }

}
