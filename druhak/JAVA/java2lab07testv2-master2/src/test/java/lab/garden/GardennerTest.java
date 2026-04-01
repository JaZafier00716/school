package lab.garden;

import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URISyntaxException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.TextFileContains;
import lab.garden.ElectricTool;
import lab.garden.Fruit;
import lab.garden.WateringCan;
import lab.garden.ElectricTool.PowerType;

class GardennerTest {

	// @formatter:off
	@ParameterizedTest
	@CsvSource({
		"Fruit.java,@ToString", 
		"Fruit.java,@NoArgsConstructor", 
		"Fruit.java,@AllArgsConstructor",
		"Fruit.java,@Builder",
		"Fruit.java,@Getter",
		"Fruit.java,@Setter",
		"Fruit.java,@ToString\\.Exclude",
		"Equipment.java,@Getter",
		"Equipment.java,@Setter", 
		"Equipment.java,@ToString",
		"Equipment.java,@EqualsAndHashCode",
		"WateringCan.java,@Getter",
		"WateringCan.java,@Setter",
		"WateringCan.java,@ToString\\(callSuper\\s* =\\s*true\\)",
		"WateringCan.java,@EqualsAndHashCode\\(callSuper\\s*=\\s*true\\)",
		"ElectricTool.java,@Getter",
		"ElectricTool.java,@Setter",
		"ElectricTool.java,@ToString\\(callSuper\\s* =\\s*true\\)",
		"ElectricTool.java,@EqualsAndHashCode\\(callSuper\\s*=\\s*true\\)"
	})
	// @formatter:on
	void anotaceTest(String file, String annotation) throws URISyntaxException {
		assertThat(TextFileContains.getProjectRoot(getClass()), new TextFileContains(file, annotation));
	}

	@Test
	void fruitToStringNoPoisonedTest() {
		assertThat(Fruit.generate().toString(), Matchers.containsString("name"));
		assertThat(Fruit.generate().toString(), Matchers.not(Matchers.containsString("poisoned")));
	}

	@Test
	void fruitSetterColorTest() {
		assertThat(Fruit.class, new HasMethod("setColor", void.class, String.class));
	}

	@Test
	void testElectricToolToString() {
		assertThat(ElectricTool.generate().toString(), Matchers.containsString("name"));
	}

	@Test
	void testWateringCanToString() {
		assertThat(WateringCan.generate().toString(), Matchers.containsString("name"));
	}

	@Test
	void testElectricToolEquals() {
		assertThat(new ElectricTool("b", 10, 1, PowerType.BATTERY),
				Matchers.equalTo(new ElectricTool("b", 10, 1, PowerType.BATTERY)));
		assertThat(new ElectricTool("b", 10, 1, PowerType.BATTERY),
				Matchers.not(Matchers.equalTo(new ElectricTool("a", 10, 1, PowerType.BATTERY))));
	}

	@Test
	void testWateringCanEquals() {
		assertThat(new WateringCan("a", 10, 4, true),
				Matchers.equalTo(new WateringCan("a", 10, 4, true)));
		assertThat(new WateringCan("b", 10, 4, true),
				Matchers.not(Matchers.equalTo(new WateringCan("a", 10, 4, true))));
	}
}
