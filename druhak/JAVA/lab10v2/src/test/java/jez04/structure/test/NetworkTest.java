package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.ClassExist;
import cz.vsb.fei.kelvin.unittest.ContainsInnerClasses;
import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.HasProperty;
import cz.vsb.fei.kelvin.unittest.OutputContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import lab.socnet.AppSocialNetwork;
import lab.socnet.Post;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class NetworkTest {

    static StructureHelper helper = StructureHelper.getInstance(AppSocialNetwork.class);
    private static String objectMixerName = "ObjectMixer";
    private static String socnetName = "AppSocialNetwork";

    List<Post> posts = new ArrayList<Post>(List.of(
        // @formatter:off
			new Post("a", "#1", "text1"),
			new Post("b", "#1", "text12"),
			new Post("c", "#2", "text12345"),
			new Post("d", "#3", "text1234"),
			new Post("a", "#7", "text123456"),
			new Post("c", "#6", "text12345678"),
			new Post("e", "#1", "text1234567"),
			new Post("a", "#2", "text123456789"))
		);
	// @formatter:on

    @Test
    void generatePostsTest() {
        List<Post> posts = AppSocialNetwork.generatePosts(10);
        assertNotNull(posts, "Collection should not be null");
        assertEquals(10, posts.size(), "Collection has not requested size");
        List<Field> randoms = List.of(AppSocialNetwork.class.getDeclaredFields()).stream()
            .filter(field -> Random.class.equals(field.getType())).toList();
        assertFalse(randoms.isEmpty(), "No field for random generator found.");
        assertTrue(randoms.stream().anyMatch(field -> Modifier.isStatic(field.getModifiers())),
            "Field for random generator is not static");
    }

    @Test
    void sortByHashtagTest() {
        AppSocialNetwork.sortByHashtag(posts);
        for (int i = 0; i < posts.size() - 1; i++) {
            assertTrue(posts.get(i).getHashtag().compareTo(posts.get(i + 1).getHashtag()) <= 0,
                "Collection is not sorted properly");
        }
        assertThat("Use lambda or method reference for sorting no inner classes.", AppSocialNetwork.class,
            new ContainsInnerClasses().countLambdaExpressions(true).countMethodReferences(true)
                .countInnerClasses(false));
    }

    @Test
    void sortByHashtagMethodReferenceTest() throws URISyntaxException, IOException {
        AppSocialNetwork.sortByHashtag(posts);
        for (int i = 0; i < posts.size() - 1; i++) {
            assertTrue(posts.get(i).getHashtag().compareTo(posts.get(i + 1).getHashtag()) <= 0,
                "Collection is not sorted properly");
        }
        assertThat("Use lambda or method reference for sorting no inner classes.", AppSocialNetwork.class,
            new ContainsInnerClasses().countLambdaExpressions(true).countMethodReferences(true)
                .countInnerClasses(false));
    }

    @Test
    void sortByAuthorAndLengthTest() {
        AppSocialNetwork.sortByAuthorAndLength(posts);
        for (int i = 0; i < posts.size() - 1; i++) {
            int comp = posts.get(i).getAuthor().compareTo(posts.get(i + 1).getAuthor());
            if (comp == 0) {
                comp = Integer.compare(posts.get(i).getText().length(), posts.get(i + 1).getText().length());
            }
            assertTrue(comp <= 0, "Collection is not sorted properly");
        }
        long innerClassCount = helper.countClassesRegexp(socnetName + "\\$.*");
        assertEquals(innerClassCount, 0, "Do not use inner class for sorting use lambda or method reference");
    }

    @Test
    void disablePostForAuthorTest() throws IOException {
        assertThat(NetworkTest.class, new ClassExist(socnetName + "$DisableAuthor"));
        Files.writeString(Paths.get("testBanAuthor.txt"), "Jane Austen\nCharles Dickens",
            StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        assertThat(() -> AppSocialNetwork.disablePostForAuthor("testBanAuthor.txt"),
            Matchers.not(new OutputContains("Jane Austen|Charles Dickens")));
    }

    @Test
    void disableAuthorsNonExistentFileTest() {
        assertThat(NetworkTest.class, new ClassExist(socnetName + "$DisableAuthor"));
        assertFalse(AppSocialNetwork.disablePostForAuthor("not-existent-file.txt"),
            "Method disablePostForAuthor do not return false for not existent file.");
    }

    @Test
    void logPostWithLifeTest() {
        assertThat("Use anonymus inner class .", AppSocialNetwork.class,
            new ContainsInnerClasses().countLambdaExpressions(false).countMethodReferences(false)
                .countInnerClassesOnlyAnonymous(true));
        assertThat(AppSocialNetwork::logPostWithLife, new OutputContains("life").caseSensitive(false).count(2));
    }

    @Test
    void logPostWithLifeTest2() {
        assertThat("Use anonymus inner class .", AppSocialNetwork.class,
            new ContainsInnerClasses().countLambdaExpressions(false).countMethodReferences(false)
                .countInnerClassesOnlyAnonymous(true));
        assertThat(AppSocialNetwork::logPostWithLife, new OutputContains("life").caseSensitive(false).count(3));
    }

    @Test
    void objectMixerExistenceTest() throws ClassNotFoundException {
        assertThat(NetworkTest.class, new ClassExist("ObjectMixer"));
        Class<?> c = helper.getClass("ObjectMixer");
        assertNotEquals(0, c.getTypeParameters().length, "No generic parameter found");
        assertThat(c, new HasProperty(".*", List.class));
        assertThat(c, new HasMethod("add", void.class, Object.class));
        assertThat(c, new HasMethod("add", void.class, List.class));
        assertThat(c, new HasMethod("getFirst", Object.class));
    }

    @Test
    void objectMixerStringTest() throws IllegalAccessException, InvocationTargetException, ClassNotFoundException,
        NoSuchMethodException {
        assertThat(NetworkTest.class, new ClassExist("ObjectMixer"));
        Class<?> c = helper.getClass("ObjectMixer");
        Field listField = List.of(c.getDeclaredFields()).stream().filter(f -> f.getType().equals(List.class))
            .findFirst().orElse(null);
        if (listField == null) {
            fail("No list found in object mixer.");
        }
        listField.setAccessible(true);
        String[] data = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        Object o = AppSocialNetwork.mixerForStrings(data);
        List<?> list = (List<?>) listField.get(o);
        assertEquals(8, list.size(), "List in mixer should have 8 elements");
        boolean result = false;
        for (int i = 0; i < data.length; i++) {
            result |= !Objects.equals(data[i], list.get(i));
        }
        assertTrue(result, "Lisat is not mixed");
        Method getFirst = helper.getMethod(c, "getFirst", Object.class);
        getFirst.invoke(o);
        assertEquals(7, list.size(), "List in mixer should have 7 elements after getFirst");
    }

    @Test
    void uniqueHeapIntTest() throws IllegalAccessException, InvocationTargetException, ClassNotFoundException,
        NoSuchMethodException {
        assertThat(NetworkTest.class, new ClassExist("ObjectMixer"));
        Class<?> c = helper.getClass("ObjectMixer");
        Field listField = List.of(c.getDeclaredFields()).stream().filter(f -> f.getType().equals(List.class))
            .findFirst().orElse(null);
        if (listField == null) {
            fail("No list found in object mixer.");
        }
        listField.setAccessible(true);
        int[] data = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        Object o = AppSocialNetwork.mixerForInt(data);
        List<?> list = (List<?>) listField.get(o);
        assertEquals(9, list.size(), "List in mixer should have 9 elements");
        boolean result = false;
        for (int i = 0; i < data.length; i++) {
            result |= !Objects.equals(data[i], list.get(i));
        }
        assertTrue(result, "Lisat is not mixed");
        Method getFirst = helper.getMethod(c, "getFirst", Object.class);
        getFirst.invoke(o);
        assertEquals(8, list.size(), "List in mixer should have 8 elements after getFirst");
    }
}
