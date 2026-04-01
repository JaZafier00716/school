package lab.garden;

import java.util.List;
import java.util.stream.Stream;

import static lab.garden.Fruit.generate;

public class Gardenner {

	public static void main(String[] args) {
		new Gardenner().run();
	}

	public void run() {
		List<Fruit> fruits = generateFruits();
		printFruits(fruits);
		List<Equipment> equipments = generateEquipments();
		printEquipments(equipments);
		testEquals();
	}

	/**
	 * Vygenerujte seznam 10 ovocí pomocí metody generate
	 * 
	 * @return seznam 10 ovocí
	 */
	public List<Fruit> generateFruits() {
		return Stream.generate(Fruit::generate).limit(10).toList();
	}

	/**
	 * Vygenerujte seznam obsahující 5 nářadí (Equipment), 5 zahradních konví (WateringCan) a 5 elektrických nářadí (ElectricTool)
	 * pomocí metody generate
	 * 
	 * @return seznam 15 nářadí a jeho potomků
	 */
	public List<Equipment> generateEquipments() {
        return Stream.concat(
                Stream.concat(
                        Stream.generate(Equipment::generate).limit(5),      // 5x Equipment
                        Stream.generate(WateringCan::generate).limit(5)     // 5x WateringCan
                ),
                Stream.generate(ElectricTool::generate).limit(5)        // 5x ElectricTool
        ).toList();
	}

	public void printFruits(List<Fruit> fruits) {
		for (Fruit fruit : fruits) {
			System.out.println(fruit);
		}
	}

	public void printEquipments(List<Equipment> equipments) {
		for (Equipment equipment : equipments) {
			System.out.println(equipment);
		}
	}

	/**
	 * Možno upravit pro testování
	 */
	public void testEquals() {
		testEquals(new Equipment("a", 10), new Equipment("a", 10));
	}

	private void testEquals(Equipment t1, Equipment t2) {
		System.out.printf("Tickets:%n\t%s%n\t%s%n  are equal:%b%n", t1, t2, t1.equals(t2));
	}

}
