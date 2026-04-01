package lab.recepies;

import java.util.List;
import java.util.stream.Stream;

import lab.Tools;

public class Chef {

	public static void main(String[] args) {
		new Chef().run();
	}

	public void run() {
		JpaConnector connector = new JpaConnector();
		doDisies(connector);
		createCookingRecipies(connector);
	}

	public void doDisies(JpaConnector connector) {

		for (Cookware cookware : generateCookwares()) {
			connector.save(cookware);
		}
		for (Cookware cookware : connector.getAllCookwares()) {
			System.out.println(cookware);
		}

		connector.delete(Tools.randomElementFrom(connector.getAllCookwares()));

		System.out.println("All Orange andf white cookwares:");
		for (Cookware cookware : connector
				.executeCriteriaQuery(connector.createCriteriaQueryForColoredCookware("Orange", "White"))) {
			System.out.println("  " + cookware);
		}


	}

	public List<Cookware> generateCookwares() {
		return Stream.generate(Cookware::generate).limit(30).toList();
	}

	public void createCookingRecipies(JpaConnector connector) {
		for (CookingRecipe cookingRecipe : generateCookingRecipies()) {
			connector.save(cookingRecipe);
		}

		for (CookingRecipe cookingRecipe : connector.getAllCookingRecipies()) {
			for (Ingredient ingredient : generateIngredients(cookingRecipe)) {
				connector.save(ingredient);
			}
		}

		connector.getEntityManager().clear();

		for (CookingRecipe cookingRecipe : connector.getAllCookingRecipies()) {
			System.out.println("CookingRecipe: " + cookingRecipe);
			//TODO: print out all Ingredients
		}

	}

	public List<CookingRecipe> generateCookingRecipies() {
		return Stream.generate(CookingRecipe::generate).limit(5).toList();
	}

	public List<Ingredient> generateIngredients(CookingRecipe cookingRecipe) {
		int count = Tools.RANDOM.nextInt(3, 10);
		return Stream.generate(() -> Ingredient.generate(cookingRecipe)).limit(count).toList();
	}

}
