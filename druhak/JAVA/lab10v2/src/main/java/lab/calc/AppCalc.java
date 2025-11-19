package lab.calc;

public class AppCalc {

	/**
	 * Jako samostatné třídy/výčtové typy v tomto balíku vytvořte:
	 *
	 * Vytvořte výčtový typ (enumeration) MathOperation s konstantami: PLUS, MINUS,
	 * MULTIPLY, DIVIDE
	 *
	 * Každá konstanda bude mít proměnnou name (string - český název) a sign
     * (char - znaménko operace + - * /) a také metody getName a print.
	 *
	 * Metoda print bude mít dva parametry typu int a bude vracet řetězec, který
	 * bude obsahovat předané parametry a mezi nimi znaménko.
	 *
	 * Dále vytvořte třídu CalcRow (řádek kalkulačky), která bude mít proměnné:
	 *
	 * a - typu int
	 *
	 * b - typu int
	 *
	 * operation - typu MathOperation
	 *
	 * Třída CalcRow bude mít také:
	 *
	 * překrytou metodu toString
	 *
     * Konstruktor se třemi parametry a, MathOperation, b (dodržte pořadí argumentů)
	 *
	 * překrytou metodu toString, která vrátí výsledek metody print ze třídy
	 * MathOperation.
	 *
	 * vytvořte třídu SimpleCalc, která bude mít:
	 *
	 * proměnou rows typu list řádků (CalcRow)
	 *
	 * konstruktor s proměnným počtem argumentů typu CalcRow, pro vytvoření
	 * SimpleCalc s počátečním seznamem rádků.
	 *
	 * metudu add s jedním parametrem CalcRow, pro přidání nového řádku
	 *
	 * metodu print bez parametrů pro výpis všech rádků do konzole.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Object simpleCalc = createCalculator();
		addRowIntoCalculator(simpleCalc);
		calculateCircleArea(10);
	}

	/**
	 * Metoda vytvoří a vrátí objekt třídy SimpleCalc, který obsahuje alesppoň dva
	 * řádky (CalcRow)
	 *
	 * @return nový objekt typu SimpleCalc. Neměňte typ návratové hodnoty.
	 */
	public static Object createCalculator() {
        return new SimpleCalc(
            new CalcRow(5, MathOperation.PLUS, 3),
            new CalcRow(10, MathOperation.DIVIDE, 2)
        );
	}

	/**
	 * Neměňte typ parametru.
	 *
	 * Použíjte přetypování na typ SimpleCalc a přidejde do něj další libovolný
	 * řádek.
	 *
	 * @param simpleCalc
	 */
	public static void addRowIntoCalculator(Object simpleCalc) {
        if (simpleCalc instanceof SimpleCalc calc) {
            calc.add(new CalcRow(7, MathOperation.MULTIPLY, 6));
        }
	}

	/**
	 * Metoda spočítá poloměr kruhu π*r²
	 *
	 * Použijte konstantu π definovanou v jazyce Java
	 *
	 * @param r - poloměr kruhu.
	 * @return obsah kruhu s daným poloměrem
	 */
	public static double calculateCircleArea(double r) {
		return Math.PI * r * r;
	}
}
