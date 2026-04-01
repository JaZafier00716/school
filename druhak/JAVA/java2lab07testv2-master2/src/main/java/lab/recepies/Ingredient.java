package lab.recepies;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lab.Tools;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import lombok.*;

/**
 * Vytvořte ze třídy platnou entitu (entitní třídu) za použití anotací a knihovny lombok
 * Třída bude mít
 * - konstruktor bez parametrů
 * - gettry settry pro všechny instanční proměnné
 * - metodu toString
 * - zajistěte aby recept nebyl součásti výpisu pomocí toString 
 * - enum type se bude do DB ukládat jako řetězec
 * - nezapomeňte na ID
 * - nezapomeňte na vazbu na recept
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)       // enum se uloží jako řetězec
    private IngredientType type;

    private String unit;
    private double quantity;

    @ManyToOne                         // vazba N:1 na recept
    @JoinColumn(name = "cooking_recipe_id")
    @ToString.Exclude                  // recept nebude součástí výpisu (zabraňuje cyklickému výpisu)
    private CookingRecipe cookingRecipe;

    public static Ingredient generate(CookingRecipe cookingRecipe) {
        Ingredient ingredient = new Ingredient();
        ingredient.unit = Tools.randomUnits();
        ingredient.quantity = Tools.randomQuantity();
        ingredient.type = Tools.randomElement(IngredientType.values());
        ingredient.cookingRecipe = cookingRecipe;
        return ingredient;
    }
	
	public enum IngredientType {
	    // ZÁKLADNÍ SUROVINY
	    FLOUR,          // Mouka
	    SUGAR,          // Cukr
	    SALT,           // Sůl
	    BUTTER,         // Máslo
	    MILK,           // Mléko
	    EGGS,           // Vejce
	    WATER,          // Voda
	    BAKING_POWDER,  // Prášek do pečiva
	    YEAST,          // Droždí
	    OLIVE_OIL,      // Olivový olej
	    SUNFLOWER_OIL,  // Slunečnicový olej
	    HONEY,          // Med
	    VANILLA,        // Vanilka

	    // KOŘENÍ A BYLINKY
	    CINNAMON,       // Skořice
	    NUTMEG,         // Muškátový oříšek
	    GINGER,         // Zázvor
	    GARLIC,         // Česnek
	    ONION,          // Cibule
	    PEPPER,         // Pepř
	    PAPRIKA,        // Paprika (koření)
	    CHILI,          // Chilli
	    OREGANO,        // Oregano
	    BASIL,          // Bazalka
	    ROSEMARY,       // Rozmarýn
	    THYME,          // Tymián
	    PARSLEY,        // Petržel
	    DILL,           // Kopr
	    BAY_LEAF,       // Bobkový list

	    // MASO A RYBY
	    CHICKEN,        // Kuřecí maso
	    BEEF,           // Hovězí maso
	    PORK,           // Vepřové maso
	    LAMB,           // Jehněčí maso
	    DUCK,           // Kachní maso
	    TURKEY,         // Krůtí maso
	    FISH,           // Ryba
	    SHRIMP,         // Krevety
	    SALMON,         // Losos
	    TUNA,           // Tuňák

	    // MLÉČNÉ PRODUKTY
	    CHEESE,         // Sýr
	    CREAM,          // Smetana
	    YOGURT,         // Jogurt
	    COTTAGE_CHEESE, // Tvaroh

	    // OVOCE
	    APPLE,          // Jablko
	    BANANA,         // Banán
	    ORANGE,         // Pomeranč
	    LEMON,          // Citron
	    LIME,           // Limetka
	    STRAWBERRY,     // Jahoda
	    BLUEBERRY,      // Borůvka
	    RASPBERRY,      // Malina
	    GRAPE,          // Hroznové víno
	    PINEAPPLE,      // Ananas
	    PEAR,           // Hruška
	    MANGO,          // Mango
	    CHERRY,         // Třešeň
	    PEACH,          // Broskev
	    WATERMELON,     // Meloun

	    // ZELENINA
	    TOMATO,         // Rajče
	    CUCUMBER,       // Okurka
	    CARROT,         // Mrkev
	    POTATO,         // Brambora
	    SWEET_POTATO,   // Batáty
	    LETTUCE,        // Salát
	    CABBAGE,        // Zelí
	    SPINACH,        // Špenát
	    BROCCOLI,       // Brokolice
	    CAULIFLOWER,    // Květák
	    ZUCCHINI,       // Cuketa
	    BELL_PEPPER,    // Paprika
	    EGGPLANT,       // Lilek
	    MUSHROOM,       // Houby
	    AVOCADO,        // Avokádo
	    PEAS,           // Hrášek
	    CORN,           // Kukuřice
	    ASPARAGUS,      // Chřest

	    // LUŠTĚNINY A OBILOVINY
	    RICE,           // Rýže
	    PASTA,          // Těstoviny
	    LENTILS,        // Čočka
	    CHICKPEAS,      // Cizrna
	    BEANS,          // Fazole
	    OATS,           // Ovesné vločky
	    QUINOA,         // Quinoa

	    // OŘECHY A SEMÍNKA
	    WALNUTS,        // Vlašské ořechy
	    ALMONDS,        // Mandle
	    PEANUTS,        // Arašídy
	    HAZELNUTS,      // Lískové ořechy
	    CASHEWS,        // Kešu
	    PISTACHIOS,     // Pistácie
	    SUNFLOWER_SEEDS,// Slunečnicová semínka
	    PUMPKIN_SEEDS,  // Dýňová semínka
	    CHIA_SEEDS,     // Chia semínka
	    FLAXSEEDS,      // Lněná semínka

	    // OSTATNÍ
	    CHOCOLATE,      // Čokoláda
	    COCONUT,        // Kokos
	    TOFU,           // Tofu
	    SOY_SAUCE,      // Sójová omáčka
	    MUSTARD,        // Hořčice
	    KETCHUP,        // Kečup
	    MAYONNAISE      // Majonéza
	}
}
