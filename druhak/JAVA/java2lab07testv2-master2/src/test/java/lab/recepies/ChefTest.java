package lab.recepies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import cz.vsb.fei.kelvin.unittest.StructureHelper;
import cz.vsb.fei.kelvin.unittest.TextFileContains;

class ChefTest {

	StructureHelper helper = StructureHelper.getInstance(ChefTest.class);

	//@formatter:off
	@ParameterizedTest
	@CsvSource({
		"Cookware.java,@Entity",
		"Cookware.java,@NoArgsConstructor",
		"Cookware.java,@Getter",
		"Cookware.java,@Setter",
		"Cookware.java,@ToString",
		"Cookware.java,@Id",
		"Cookware.java,@GeneratedValue",
		"Ingredient.java,@Entity",
		"Ingredient.java,@NoArgsConstructor",
		"Ingredient.java,@Getter",
		"Ingredient.java,@Setter", 
		"Ingredient.java,@ToString",
		"Ingredient.java,@Id",
		"Ingredient.java,@GeneratedValue",
		"Ingredient.java,@ManyToOne",
		"Ingredient.java,@ToString\\.Exclude",
		"CookingRecipe.java,@Entity",
		"CookingRecipe.java,@NoArgsConstructor",
		"CookingRecipe.java,@Getter",
		"CookingRecipe.java,@Setter",
		"CookingRecipe.java,@ToString",
		"CookingRecipe.java,@Id", 
		"CookingRecipe.java,@GeneratedValue",
		"CookingRecipe.java,@OneToMany\\(mappedBy"
	})
	//@formatter:on
	void anotaceTest(String file, String annotation) throws URISyntaxException {
		assertThat(TextFileContains.getProjectRoot(getClass()), new TextFileContains(file, annotation));
	}

	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	class JpaConnectorTests {

		private static JpaConnector connector;

		@BeforeAll
		static void initAll() {
			try {
				connector = new JpaConnector();
			} catch (Exception e) {
				e.printStackTrace();
				fail("Failed to initialize JpaConnector: " + e.getMessage(), e);
			}
		}

		@BeforeEach
		void init() {
			if (connector == null) {
				fail("JpaConnector was not properly initialized in @BeforeAll");
			}
			connector.getEntityManager().clear();
			if (connector.getEntityManager().getTransaction().isActive()) {
				connector.getEntityManager().getTransaction().rollback();
			}
		}

		@AfterAll
		static void cleanUpAll() {
			if (connector != null) {
				connector.stop();
			}
		}

		private boolean same(Cookware e1, Cookware e2) throws IllegalAccessException, InvocationTargetException {
			// @formatter:off
			return Objects.equals(helper.executeGetter(e1, "getId"), helper.executeGetter(e2, "getId"))
					&& Objects.equals(helper.executeGetter(e1, "getName"), helper.executeGetter(e2, "getName"));
			// @formatter:on
		}

		private boolean same(Ingredient e1, Ingredient e2) throws IllegalAccessException, InvocationTargetException {
			// @formatter:off
			return Objects.equals(helper.executeGetter(e1, "getId"), helper.executeGetter(e2, "getId")) 
					&& Objects.equals(helper.executeGetter(e1, "getUnit"), helper.executeGetter(e2, "getUnit"))
					&& Objects.equals(helper.executeGetter(e1, "getQuantity"), helper.executeGetter(e2, "getQuantity"))
					&& Objects.equals(helper.executeGetter(e1, "getType"), helper.executeGetter(e2, "getType"));
			// @formatter:on
		}

		private boolean same(CookingRecipe e1, CookingRecipe e2)
				throws IllegalAccessException, InvocationTargetException {
			// @formatter:off
			return Objects.equals(helper.executeGetter(e1, "getId"), helper.executeGetter(e2, "getId")) 
					&& Objects.equals(helper.executeGetter(e1, "getTitle"), helper.executeGetter(e2, "getTitle"))
					&& Objects.equals(helper.executeGetter(e1, "getDuration"), helper.executeGetter(e2, "getDuration"));
			// @formatter:on
		}

		private boolean same(List<?> l1, List<?> l2) {
			if (l1 == null) {
				l1 = Collections.emptyList();
			}
			if (l2 == null) {
				l2 = Collections.emptyList();
			}
			return Objects.equals(l1, l2);
		}

		@Test
		@Order(100)
		void jpaCountryInsertTest() throws IllegalAccessException, InvocationTargetException {
			Cookware entity = Cookware.generate();
			Cookware saved = connector.save(entity);
			assertTrue(same(entity, saved), "Countries are not same after save.");
		}

		@Test
		@Order(120)
		void jpaCookingRecipeInsertTest() throws IllegalAccessException, InvocationTargetException {
			CookingRecipe entity = CookingRecipe.generate();
			CookingRecipe saved = connector.save(entity);
			assertTrue(same(entity, saved), "CookingRecipies are not same after save.");
		}

		@Test
		@Order(200)
		void jpaCookwareReadTest() {
			List<Cookware> savedEntities = connector.getAllCookwares().stream().toList();
			assertThat(savedEntities, Matchers.not(Matchers.hasSize(0)));
		}

		@Test
		@Order(210)
		void jpaCookingRecipeReadTest() {
			List<CookingRecipe> savedEntities = connector.getAllCookingRecipies().stream().toList();
			assertThat(savedEntities, Matchers.not(Matchers.hasSize(0)));
		}

		@Test
		@Order(220)
		void jpaIngredientInsertTest() throws IllegalAccessException, InvocationTargetException {
			Ingredient entity = Ingredient.generate(connector.getAllCookingRecipies().getFirst());
			Ingredient saved = connector.save(entity);
			assertTrue(same(entity, saved), "CookingRecipies are not same after save.");
		}

		@Test
		@Order(230)
		void jpaIngredientReadTest() {
			List<Ingredient> savedEntities = connector.getAllIngredients().stream().toList();
			assertThat(savedEntities, Matchers.not(Matchers.hasSize(0)));
		}

		@Test
		@Order(250)
		void jpaIngredientOfReciepeContainInIngredientsTest() throws IllegalAccessException, InvocationTargetException {
			connector.getEntityManager().clear();
			List<Ingredient> savedEntities = connector.getAllIngredients().stream().toList();
			for (Ingredient entity : savedEntities) {
				assertTrue(((List<Ingredient>)helper.executeGetter(helper.executeGetter(entity, "getCookingRecipe"), "getIngredients")).stream().anyMatch(b -> {
					try {
						return this.same(b, entity);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
						return false;
					}
				}), "Ingredient is not in list of ingredients of ingredient.cookingRecipe.");
			}
		}

		@Test
		@Order(270)
		void jpaCookingRecipeToStringTest() throws IllegalAccessException, InvocationTargetException {
			connector.getEntityManager().clear();
			List<CookingRecipe> savedEntities = connector.getAllCookingRecipies();
			for (CookingRecipe cookingRecipe : savedEntities) {
				assertThat(cookingRecipe.toString(), Matchers.notNullValue());
				if (!((List<Ingredient>)helper.executeGetter(cookingRecipe, "getIngredients")).isEmpty()) {
					assertTrue(cookingRecipe.toString().contains("Ingredient"),
							"ToString of CookingRecepe do not contains Ingredient.");
				}
			}
			assertFalse( ((List<Ingredient>)helper.executeGetter(savedEntities.getFirst(), "getIngredients")).getFirst().toString().contains("CookingRecipe"),
					"ToString of ingredient should not contains CookingRecipe.");
		}

		@Test
		@Order(300)
		void jpaCookwareDeleteTest() {
			List<Cookware> savedEntities = connector.getAllCookwares();
			assertThat(savedEntities, Matchers.not(Matchers.hasSize(0)));
			for (Cookware entity : savedEntities) {
				connector.delete(entity);
			}
			List<Cookware> modifiedEntities = connector.getAllCookwares();
			assertThat(modifiedEntities, Matchers.hasSize(0));
		}

		@Test
		@Order(400)
		void jpaFindByCookwareByColorsTest() throws IllegalAccessException, InvocationTargetException {
			connector.save(CookingRecipe.generate());
			List<CookingRecipe> cookingRecipes = connector.getAllCookingRecipies();
			for (Ingredient ingredient : Stream.generate(() -> Ingredient.generate(cookingRecipes.getFirst()))
					.limit(100).toList()) {
				connector.save(ingredient);
			}

			List<Cookware> cookwares = connector
					.executeCriteriaQuery(connector.createCriteriaQueryForColoredCookware("Orange", "White"));
			for (Cookware cookware : cookwares) {
				assertThat(helper.executeGetter(cookwares, "getColor").toString(), Matchers.in(List.of("Orange", "White")));
			}
		}

	}

}
