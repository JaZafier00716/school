package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.SrcContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import cz.vsb.fei.kelvin.unittest.TextFileContains;
import java.net.URISyntaxException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;

class ClassStructureTest {

    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

    // @formatter:off
    @ParameterizedTest
    @CsvSource({
        "msg(_.*)?\\.properties,nickName,2"
    })
    // @formatter:on
    void propertiesFileTest(String file, String annotation, int count) throws URISyntaxException {
        assertThat(TextFileContains.getProjectRoot(getClass()), new TextFileContains(file, annotation).count(count).useRegExpForName(true));
    }

    // @formatter:off
    @ParameterizedTest
    @CsvSource({
        "Score,@MyEdit,2",
        "EditController,Introspector.getBeanInfo,1",
        "EditController,BeanInfo,1",
        "EditController,PropertyDescriptor,1",
        "EditController,getDeclaredField,1",
        "EditController,getAnnotation,1",
        "EditController,\\.readOnly\\(\\),1",
        "EditController,\\.visible\\(\\),1",
        "MyEdit,@interface,1",
        "MyEdit,@Retention,1",
        "MyEdit,RetentionPolicy\\.RUNTIME,1",
        "MyEdit,readOnly\\(\\),1",
        "MyEdit,visible\\(\\),1",
    })
    // @formatter:on
    void usageTest(String file, String annotation, int count) throws ClassNotFoundException {
        Class<?> clazz = helper.getClass(file);
        new SrcContains(annotation).count(count);
        assertThat(clazz, new SrcContains(annotation).count(count));
    }


}
