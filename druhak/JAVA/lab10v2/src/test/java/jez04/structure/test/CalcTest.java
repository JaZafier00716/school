package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.ClassExist;
import cz.vsb.fei.kelvin.unittest.HasConstructor;
import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.HasProperty;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import lab.calc.AppCalc;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalcTest {

    static StructureHelper helper = StructureHelper.getInstance(AppCalc.class);
    private static String mathOperationName = "MathOperation";
    private static String simpleCalcName = "SimpleCalc";
    private static String calcRowName = "CalcRow";

    @Test
    void mathOperationExistTest() {
        assertThat(CalcTest.class, new ClassExist(mathOperationName));
    }

    @Test
    void operationIsEnumerationTest() throws ClassNotFoundException {
        mathOperationExistTest();
        Class<?> c = helper.getClass(mathOperationName);
        assertTrue(c.isEnum(), mathOperationName + " have to be enumaration.");
    }

    @Test
    void operationPropertyExistenceTest() throws ClassNotFoundException {
        Class<?> c = helper.getClass(mathOperationName);
        assertThat(c, new HasProperty("name", String.class));
        assertThat(c, new HasProperty("sign", char.class));
    }

    @Test
    void operationMethodExistenceTest() throws ClassNotFoundException {
        Class<?> c = helper.getClass(mathOperationName);
        assertThat(c, new HasMethod("getName", String.class));
        assertThat(c, new HasMethod("print", String.class, int.class, int.class));
        assertThat(c, new HasConstructor(String.class, int.class, String.class, char.class));
    }

    @Test
    void mathOperationHasElementsTest() throws ClassNotFoundException {
        mathOperationExistTest();
        Class<?> c = helper.getClass(mathOperationName);
        assertTrue(c.getEnumConstants().length > 0, "operation has not some enumeration constants");
    }

    @Test
    void calcRowExistenceTest() {
        assertThat(CalcTest.class, new ClassExist(calcRowName));
    }

    @Test
    void calcRowPropertyTest() throws ClassNotFoundException {
        assertThat(CalcTest.class, new ClassExist(calcRowName));
        Class<?> c = helper.getClass(calcRowName);
        Class<?> mo = helper.getClass(mathOperationName);
        assertThat(c, new HasProperty("a", int.class));
        assertThat(c, new HasProperty("b", int.class));
        assertThat(c, new HasProperty("operation", mo));
    }

    @Test
    void calcRowMethodsTest() throws ClassNotFoundException {
        Class<?> c = helper.getClass(calcRowName);
        Class<?> mo = helper.getClass(mathOperationName);
        assertThat(c, new HasMethod("toString", String.class));
        assertThat(c, new HasConstructor(int.class, mo, int.class));
    }

    @Test
    void simpleCalcExistenceTest() {
        assertThat(CalcTest.class, new ClassExist(simpleCalcName));
    }


    @Test
    void simpleCalcPropertyExistenceTest() throws ClassNotFoundException {
        assertThat(CalcTest.class, new ClassExist(simpleCalcName));
        Class<?> c = helper.getClass(simpleCalcName);
        assertThat(c, new HasProperty("rows", List.class));
    }

    @Test
    void simpleCalcMethodExistenceTest() throws ClassNotFoundException {
        assertThat(CalcTest.class, new ClassExist(simpleCalcName));
        Class<?> c = helper.getClass(simpleCalcName);
        Class<?> cr = helper.getClass(calcRowName);
        assertThat(c, new HasMethod("add", void.class, cr));
        assertThat(c, new HasMethod("print", void.class));
    }

    @Test
    void calcRowConstructorExistenceTest() throws ClassNotFoundException {
        assertThat(CalcTest.class, new ClassExist(simpleCalcName));
        Class<?> c = helper.getClass(simpleCalcName);
        Class<?> cr = helper.getClass(calcRowName);
        assertThat(c, new HasConstructor(cr.arrayType()));
    }

    @Test
    void creatCalculatorTest() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
        SecurityException, ClassNotFoundException {
        Object o = AppCalc.createCalculator();
        Class<?> simpleCalcClass = helper.getClass(simpleCalcName);
        assertEquals(simpleCalcClass, o.getClass(), "Returned object should be type SimpleCalc");
        Field rows = simpleCalcClass.getDeclaredField("rows");
        rows.setAccessible(true);
        assertTrue(((List<?>) rows.get(o)).size() >= 2, "SimpleCalc should contains at least 2 rows");
    }

    @Test
    void addRowIntoCalculator() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
        SecurityException, InstantiationException, InvocationTargetException, ClassNotFoundException,
        NoSuchMethodException {
        Class<?> simpleCalcClass = helper.getClass(simpleCalcName);
        Class<?> cr = helper.getClass(calcRowName);
        Object o = helper.getConstructor(simpleCalcClass, cr.arrayType()).newInstance(Array.newInstance(cr, 0));
        AppCalc.addRowIntoCalculator(o);
        Field rows = simpleCalcClass.getDeclaredField("rows");
        rows.setAccessible(true);
        assertTrue(((List<?>) rows.get(o)).size() == 1, "SimpleCalc should contains 1 row");
    }


    @Test
    void calculateCircleAreaTest() {
        assertEquals(3.141592653589793, AppCalc.calculateCircleArea(1), 0.0000000000000001, "Use proper value of π");
    }
}
