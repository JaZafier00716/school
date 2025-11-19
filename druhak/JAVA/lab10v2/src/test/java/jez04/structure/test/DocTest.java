package jez04.structure.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cz.vsb.fei.kelvin.unittest.ClassExist;
import cz.vsb.fei.kelvin.unittest.HasConstructor;
import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.HasProperty;
import cz.vsb.fei.kelvin.unittest.IsDescendatOf;
import cz.vsb.fei.kelvin.unittest.IsInterface;
import cz.vsb.fei.kelvin.unittest.OutputContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lab.docs.AppDoc;
import static org.hamcrest.MatcherAssert.assertThat;
class DocTest {

	private static final String SIGNED_DOC = "SigneDocument";
	private static final String DOC = "Document";

	static StructureHelper helper = StructureHelper.getInstance(AppDoc.class);

	@Test
	void documentExistenceTest() throws ClassNotFoundException {
        assertThat(DocTest.class, new ClassExist(DOC));
		Class<?> c = helper.getClass(DOC);
		assertTrue(c.isInterface(), c.getSimpleName() + " have to be interface.");
        assertThat(c, new IsInterface());
        assertThat(c, new HasMethod("getContent", String.class));
	}

	@Test
	void signeDocumentExistenceTest() throws ClassNotFoundException, NoSuchMethodException {
        assertThat(DocTest.class, new ClassExist(SIGNED_DOC));
		Class<?> c = helper.getClass(SIGNED_DOC);

		assertTrue(Modifier.isAbstract(c.getModifiers()), c.getSimpleName() + " have to be interface.");
        assertThat(c, new IsDescendatOf(DOC));
        assertThat(c, new HasMethod("getSignature", String.class));
        assertThat(c, new HasMethod("getContent", String.class));
        assertThat(c, new HasMethod("getNotSignedContent", String.class).abstractTag(true));
	}

	@Test
	void emailExistenceTest() throws ClassNotFoundException {
        assertThat(DocTest.class, new ClassExist("Email"));
		Class<?> c = helper.getClass("Email");
        assertThat(c, new IsDescendatOf(DOC));
        assertThat(c, new HasProperty("content", String.class));
        assertThat(c, new HasConstructor(String.class));
	}

	@Test
	void pdfExistenceTest() throws ClassNotFoundException {
        assertThat(DocTest.class, new ClassExist("Pdf"));
		Class<?> c = helper.getClass("Pdf");
        assertThat(c, new IsDescendatOf(SIGNED_DOC));
        assertThat(c, new HasProperty("text", String.class));
        assertThat(c, new HasConstructor(String.class));
	}

	@Test
	void encryptedExistenceTest() throws ClassNotFoundException {
        assertThat(DocTest.class, new ClassExist("Encrypted"));
		Class<?> c = helper.getClass("Encrypted");
        assertThat(c, new IsDescendatOf(SIGNED_DOC));
        assertThat(c, new HasProperty("text", String.class));
        assertThat(c, new HasConstructor(String.class));
	}

	@Test
	void createBodiesTest() throws ClassNotFoundException {
		Class<?> email = helper.getClass("Email");
		Class<?> pdf = helper.getClass("Pdf");
		Class<?> encrypted = helper.getClass("Encrypted");
		List<?> docs = AppDoc.createDocs("abcdef");
		assertTrue(docs.stream().anyMatch(b -> email.isAssignableFrom(b.getClass())),
				"Some element of collection hacve to be type Email");
		assertTrue(docs.stream().anyMatch(b -> pdf.isAssignableFrom(b.getClass())),
				"Some element of collection hacve to be type Pdf");
		assertTrue(docs.stream().anyMatch(b -> encrypted.isAssignableFrom(b.getClass())),
				"Some element of collection hacve to be type Encrypted");
	}

	@Test
	void printDocsTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
        InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
		List<Object> docs = new ArrayList<>();
		Class<?> email = helper.getClass("Email");
		Class<?> pdf = helper.getClass("Pdf");
		Class<?> encrypted = helper.getClass("Encrypted");
		docs.add(helper.getConstructor(email, String.class).newInstance("email"));
		docs.add(helper.getConstructor(pdf, String.class).newInstance("pdfdoc"));
		docs.add(helper.getConstructor(encrypted, String.class).newInstance("abcdef"));

        assertThat(() -> AppDoc.printDocs(docs), new OutputContains("email"));
        assertThat(() -> AppDoc.printDocs(docs), new OutputContains("pdfdoc"));
        assertThat(() -> AppDoc.printDocs(docs), new OutputContains("--PDF--"));
        assertThat(() -> AppDoc.printDocs(docs), new OutputContains("Signature:"));
        assertThat(() -> AppDoc.printDocs(docs), new OutputContains("ABCDEF"));
	}
}
