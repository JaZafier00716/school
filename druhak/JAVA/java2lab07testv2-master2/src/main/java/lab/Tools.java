package lab;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Tools {

	public static final Random RANDOM = new Random();

	public static final List<String> FIRST_NAMES = List.of("Petr", "Marie", "Jan", "Jana", "Tomáš", "Kateřina", "Lukáš",
			"Tereza", "Martin", "Veronika", "David", "Eva", "Jakub", "Lucie", "Michal", "Anna", "Adam", "Monika", "Tom",
			"Klára", "Robert", "Kristýna", "Marek", "Simona", "Filip", "Petra", "Ondřej", "Lenka", "Matěj", "Markéta",
			"Pavel", "Hana", "Jakub", "Adéla", "Daniel", "Barbora", "Lukáš", "Eliška", "Josef");

	public static final List<String> LAST_NAMES = List.of("Novák", "Svoboda", "Novotný", "Dvořák", "Černý",
			"Procházková", "Kučera", "Veselý", "Horák", "Němec", "Pokorný", "Mareš", "Pospíšilová", "Hájek", "Jelínek",
			"Král", "Růžička", "Beneš", "Fiala", "Sedláček", "Kříž", "Němcová", "Vlček", "Kolář", "Bartoš", "Bílý",
			"Veselá", "Kovařík", "Havelka", "Malý", "Urban", "Kopecký", "Vlach", "Šimek", "Konečný", "Doležal",
			"Šťastný", "Kopečná", "Holub", "Pospíchal");

	public static final List<String> STREETS = List.of("Panská", "Hlavní", "Ulice Nová", "Náměstí Svobody", "Hřbitovní",
			"Revoluční", "Sadová", "Jiráskova", "Školní", "Vinohradská", "Komenského", "Mírová", "Křížová",
			"Masarykova", "Třída Svobody", "Karlova", "Dlouhá", "Žižkova", "Purkyňova", "Rybářská", "Sokolská",
			"Rybníček", "Vrchlického", "Štefánikova", "Veselá", "Lipová", "Na Hrázi", "Výšinná", "Nová Cesta", "Česká");

	public static final List<String> CITIES = Collections.unmodifiableList(
			Arrays.asList("Praha", "Brno", "Ostrava", "Plzeň", "Liberec", "Olomouc", "České Budějovice",
					"Hradec Králové", "Ústí nad Labem", "Pardubice", "Zlín", "Karlovy Vary", "Jihlava", "Tábor",
					"Havířov", "Český Těšín", "Mladá Boleslav", "Trutnov", "Přerov", "Kladno", "Opava", "Frýdek-Místek",
					"Děčín", "Karviná", "Jablonec nad Nisou", "Třebíč", "Žďár nad Sázavou", "Kolín", "Uherské Hradiště",
					"Znojmo", "Prostějov", "Litoměřice", "Kroměříž", "Chomutov", "Příbram", "Příbram", "Cheb",
					"Teplice", "Uherský Brod", "Sokolov", "Břeclav", "Litvínov", "Klatovy", "Vsetín", "Nový Jičín"));

	public static final List<String> WORLD_CITIES = List.of("New York", "London", "Tokyo", "Paris", "Los Angeles",
			"Shanghai", "Hong Kong", "Dubai", "Singapore", "Rome", "Berlin", "Sydney", "Barcelona", "Toronto",
			"Chicago", "San Francisco", "Madrid", "Moscow", "Bangkok", "Beijing", "Seoul", "Istanbul", "Mumbai",
			"Mexico City", "São Paulo", "Buenos Aires", "Jakarta", "Lagos", "Cairo", "Rio de Janeiro", "Kuala Lumpur",
			"Johannesburg", "Stockholm", "Helsinki", "Vienna", "Oslo", "Copenhagen", "Brussels", "Amsterdam", "Lisbon",
			"Athens", "Dublin", "Warsaw", "Prague", "Budapest", "Zurich", "Geneva", "Munich", "Milan", "Venice",
			"Seville", "Valencia", "Mexico City", "Havana", "Santiago", "Bogotá", "Lima", "Caracas", "Montevideo",
			"Brasilia", "Manila", "Hanoi", "Taipei", "Kolkata", "Chennai", "Bangalore", "Karachi", "Riyadh", "Tehran",
			"Baghdad", "Damascus", "Doha", "Kuwait City", "Muscat", "Ankara");

	public static final List<String> BUILDINGS = List.of("Eiffel Tower", "Empire State Building", "Burj Khalifa",
			"Big Ben", "Colosseum", "Sydney Opera House", "Statue of Liberty", "Leaning Tower of Pisa",
			"Shanghai Tower", "One World Trade Center", "Willis Tower", "Taipei 101", "Louvre Museum",
			"Chrysler Building", "Petronas Towers", "Shard London Bridge", "CN Tower", "Berlin TV Tower",
			"Tokyo Skytree", "Guggenheim Museum", "Buckingham Palace", "Vatican City St. Peter's Basilica",
			"Notre-Dame Cathedral", "Hagia Sophia", "Neuschwanstein Castle", "Forbidden City", "Red Square Kremlin",
			"Christ the Redeemer", "Pantheon Rome", "Alhambra Spain", "Sagrada Familia", "Westminster Abbey",
			"White House", "Lincoln Memorial", "Golden Gate Bridge", "Hollywood Sign", "Mount Rushmore",
			"St. Basil's Cathedral", "Tower Bridge", "Edinburgh Castle", "Brandenburg Gate", "Palace of Versailles",
			"Angkor Wat", "Great Wall of China", "Taj Mahal", "Machu Picchu", "Pyramids of Giza", "Stonehenge",
			"Parthenon Athens", "Petra Jordan");

	public static final List<String> NATO_COUNTRIES = List.of("United States", "Canada", "United Kingdom", "France",
			"Germany", "Italy", "Spain", "Turkey", "Poland", "Netherlands", "Belgium", "Norway", "Denmark", "Portugal",
			"Greece", "Czech Republic", "Hungary", "Slovakia", "Romania", "Bulgaria", "Estonia", "Latvia", "Lithuania",
			"Slovenia", "Croatia", "Albania", "Montenegro", "North Macedonia");

	public static final List<String> POST_NUMBER = List.of("10000", "11000", "12000", "13000", "14000", "15000",
			"16000", "17000", "18000", "19000", "20000", "21000", "22000", "23000", "24000", "25000", "26000", "27000",
			"28000", "29000", "30000", "31000", "32000", "33000", "34000", "35000", "36000", "37000", "38000", "39000");

	public static final List<String> COMPANY_NAMES = List.of("Inova", "Sprint", "Polaris", "Forte", "Nexus", "Vertex",
			"Synergie", "Horizon", "Astra", "Axiom", "Optima", "Integra", "Evolve", "Apex", "Dynasty", "Eclipse",
			"Paragon", "Fusion", "Oasis", "Equinox", "Voyage", "Genesis", "Zenith", "Elite", "Harmony", "Stratos",
			"Aurora", "Quantum", "Spectrum");

	public static final List<String> BIRD_NAMES = List.of("Vlaštovka", "Sýkorka", "Krkavec", "Sokol", "Káně",
			"Střízlík", "Sojka", "Datel", "Drozd", "Rorýs", "Husa", "Labuť", "Krahujec", "Sova", "Výr", "Husice",
			"Havran", "Špaček", "Ledňáček", "Sup");

	public static final List<String> NICKNAMES = List.of("CyberSurfer", "PixelPioneer", "SocialSavvy", "DigitalDynamo",
			"ByteBuddy", "InstaGuru", "TikTokTornado", "SnapMaster", "TweetTrendsetter", "ChatChampion", "HashtagHero",
			"EmojiEnthusiast", "StoryStylist", "SelfieStar", "FilterFanatic", "VlogVirtuoso", "Memelord",
			"InfluencerInsider", "StreamSupreme", "GeekyGizmo", "CodeCommander", "JavaJuggernaut", "ByteNinja",
			"SyntaxSamurai", "ClassyCoder", "ObjectOmnipotent", "LoopLegend", "VariableVirtuoso", "DebugDemon",
			"CompilerCrusader", "PixelProdigy", "VirtualVoyager", "AlgorithmAce", "DataDynamo", "ExceptionExpert",
			"BugBuster", "SyntaxSorcerer", "CodeCrusader", "JavaJester");

	public static final List<String> PLAYER_NICKS = List.of("CyberSurfer", "PixelPioneer", "SocialSavvy",
			"DigitalDynamo", "ByteBuddy", "InstaGuru", "TikTokTornado", "SnapMaster", "TweetTrendsetter",
			"ChatChampion", "HashtagHero", "EmojiEnthusiast", "StoryStylist", "SelfieStar", "FilterFanatic",
			"VlogVirtuoso", "Memelord", "InfluencerInsider", "StreamSupreme", "GeekyGizmo", "CodeCommander",
			"JavaJuggernaut", "ByteNinja", "SyntaxSamurai", "ClassyCoder", "ObjectOmnipotent", "LoopLegend",
			"VariableVirtuoso", "DebugDemon", "CompilerCrusader", "PixelProdigy", "VirtualVoyager", "AlgorithmAce",
			"DataDynamo", "ExceptionExpert", "BugBuster", "SyntaxSorcerer", "CodeCrusader", "JavaJester",
			"NerdyNavigator", "CryptoCaptain", "SocialButterfly", "AppArchitect", "WebWizard", "FunctionFreak",
			"PixelArtist", "CyberPhantom", "HackHero", "CacheChampion", "ScreenSage", "WebWeaver", "LogicLover",
			"BitBlazer", "NetworkNomad", "ProtocolPioneer", "BinaryBoss", "StackSultan", "SocialScribe", "RenderRuler",
			"ScriptSorcerer", "HTMLHero", "PixelProwler", "FrameFreak", "DataDreamer", "BotBuilder", "ByteBishop",
			"KeyboardKnight", "DesignDaredevil", "JavaJuggler", "SyntaxStrategist", "TechTactician", "ProgramProdigy",
			"BinaryBard", "PixelPoet", "GigabyteGuru", "TechTrekker", "NetworkNinja", "DataDetective", "MatrixMaster",
			"CodeConductor", "AppAlchemist", "ServerSage", "ClusterChampion", "ScriptSensei", "KeyboardKicker",
			"CacheCrafter", "SocialSpark", "BinaryBeast", "CodeConnoisseur", "BitBrain", "VirtualVanguard",
			"SystemSculptor", "RenderRogue", "CryptoConqueror", "MachineMonarch", "PixelPal", "CompilerCaptain",
			"BitBuilder", "TechTitan", "CloudConqueror", "EchoExplorer", "FunctionFanatic", "RobotRanger");

	public static final List<String> NAME_OF_GAMES = List.of("Call of Duty: Warzone", "Counter-Strike 2",
			"Battlefield 2042", "Overwatch 2", "Team Fortress 2", "Halo Infinite", "Apex Legends", "Rainbow Six Siege",
			"Valorant", "Fortnite", "PUBG: Battlegrounds", "Destiny 2", "Titanfall 2", "Splatoon 3", "Gears of War 5",
			"Left 4 Dead 2", "Dota 2", "League of Legends", "Minecraft (Bedrock/Java - multiplayer)", "Rust",
			"Escape from Tarkov", "DayZ", "Hell Let Loose", "Hunt: Showdown", "Chivalry 2", "Payday 3", "War Thunder",
			"Enlisted", "World of Tanks", "Sea of Thieves");

	public static final List<String> EMAIL_PREFIX = List.of("cool.guy", "sunny.day", "dark.knight", "happy.cat",
			"speed.runner", "storm.rider", "night.owl", "fire.fox", "ice.queen", "shadow.fox", "blue.sky", "lucky.star",
			"red.dragon", "silver.wolf", "golden.eagle", "mystic.raven", "thunder.bolt", "frost.bite", "cosmic.wave",
			"silent.ninja", "hidden.tiger", "burning.phoenix", "wild.hawk", "deep.ocean", "swift.fox", "brave.lion",
			"electric.zebra", "rapid.cobra", "gentle.giant", "crazy.horse");

	public static final List<String> PASSWORDS = List.of("Dr@g0n123!", "Thund3r#456", "Sh@d0w789$", "E@gl3!234",
			"St0rm#567", "T1g3r$890", "Ph03n1x@123", "R@v3n#456", "Kn1ght!789", "0c3an@234", "F1r3#567", "Fr0st$890",
			"L1ghtn!ng@123", "C0br@#456", "H@wk!789", "F0x$234", "W0lf#567", "L10n!890", "B!zzard@123", "D3str0y3r#456",
			"R3ck0n!789", "Gh0st$234", "D@rkN!ght#567", "L3gend!890", "M@st3r@123", "N1ghtm@r3#456", "S@v@g3!789",
			"T3rm!n@t0r$234", "Fur10us#567", "B@ttleL!on!890");

	public static final List<String> EMAIL_DOMAIN = List.of("gmail.com", "seznam.cz", "hotmail.com", "outlook.com");

	public static final List<String> COURSE_NAMES = List.of("Java 1", "Java 2", "C# 1", "C# 2", "Python", "Databáze I",
			"Databáze II", "Funkcionální programování", "OOP", "PS", "APPS", "SWI");

	public static final List<String> RECIPES = List.of(
	        "Spaghetti Carbonara",
	        "Chicken Alfredo",
	        "Beef Stroganoff",
	        "Vegetable Stir-Fry",
	        "Tomato Basil Soup",
	        "Classic Caesar Salad",
	        "Grilled Salmon with Lemon Butter",
	        "Homemade Pizza Margherita",
	        "Chocolate Chip Cookies",
	        "Blueberry Pancakes",
	        "Creamy Mushroom Risotto",
	        "Garlic Butter Shrimp",
	        "Lentil and Spinach Curry",
	        "Stuffed Bell Peppers",
	        "Avocado Toast with Poached Egg",
	        "Banana Bread",
	        "Shepherd’s Pie",
	        "Mango Smoothie",
	        "Tiramisu",
	        "French Onion Soup"
	    );

	public static final List<String> COOKWARES = List.of(
	        "Mixing Bowl",
	        "Cutting Board",
	        "Measuring Cup",
	        "Measuring Spoon",
	        "Frying Pan",
	        "Saucepan",
	        "Baking Dish",
	        "Whisk",
	        "Spatula",
	        "Ladle",
	        "Rolling Pin",
	        "Colander",
	        "Blender",
	        "Grater",
	        "Mortar and Pestle",
	        "Chef's Knife",
	        "Tongs",
	        "Oven Mitts",
	        "Muffin Tin",
	        "Salad Spinner"
	    );
	
	public static final List<String> UNIT_LIST = List.of("g", // Gram
			"kg", // Kilogram
			"ml", // Mililitr
			"l", // Litr
			"tsp", // Čajová lžička
			"tbsp", // Polévková lžíce
			"cup", // Hrnek
			"pinch", // Špetka
			"piece", // Kus
			"slice", // Plátek
			"dash", // Troška
			"oz", // Unce
			"lb", // Libra
			"fl oz" // Tekutá unce
	);
    public static final List<String> COLORS = List.of(
            "Red",
            "Blue",
            "Green",
            "Yellow",
            "Orange",
            "Purple",
            "Pink",
            "Brown",
            "Black",
            "White",
            "Gray",
            "Cyan",
            "Magenta",
            "Lime",
            "Olive",
            "Maroon",
            "Navy",
            "Teal",
            "Turquoise",
            "Beige"
        );
    
    public static final List<String> FRUITS = List.of(
            "Apple", "Pear", "Banana", "Orange", "Mandarin",
            "Lemon", "Lime", "Peach", "Nectarine", "Plum",
            "Apricot", "Cherry", "Sour Cherry", "Strawberry", "Raspberry",
            "Blackberry", "Blueberry", "Currant", "Gooseberry", "Melon",
            "Pineapple", "Mango", "Papaya", "Kiwi", "Fig",
            "Pomegranate", "Lychee", "Quince", "Cornelian Cherry", "Persimmon"
        );
    
    public static final List<String> GARDEN_TOOLS = List.of(
            "Shovel", "Rake", "Hoe", "Trowel", "Pruning Shears",
            "Wheelbarrow", "Garden Fork", "Hedge Trimmer", "Lawn Mower",
            "Garden Hose", "Sprinkler", "Cultivator", "Weeder", "Spade",
            "Edger", "Hand Fork", "Lopper", "Pickaxe", "Mattock",
            "Secateurs", "Chainsaw", "Leaf Blower", "Garden Scissors", "Soil Knife"
        );
    public static final List<String> WATERING_CANS = List.of(
            "Plastic Watering Can", "Metal Watering Can", "Galvanized Watering Can", "Copper Watering Can", "Stainless Steel Watering Can",
            "Ceramic Watering Can", "Glass Watering Can", "Vintage Watering Can", "Indoor Watering Can", "Outdoor Watering Can",
            "Long Spout Watering Can", "Short Spout Watering Can", "Automatic Watering Can", "Decorative Watering Can", "Mini Watering Can"
        );
    public static final List<String> ELECTRIC_GARDEN_TOOLS = List.of(
            "Electric Lawn Mower", "Electric Hedge Trimmer", "Electric Chainsaw", "Electric Leaf Blower", "Electric Grass Trimmer",
            "Electric Garden Shredder", "Electric Cultivator", "Electric Pressure Washer", "Electric Pruning Shears", "Electric Edger",
            "Electric Tiller", "Cordless Garden Sprayer", "Electric Pole Saw", "Electric Weed Burner", "Electric Water Pump"
        );
	// Možné hodnoty pro kusové jednotky
	private static final List<Integer> PIECE_AMOUNTS = List.of(1, 2, 3, 4, 5);

	// Možné hodnoty pro objemové a hmotnostní jednotky
	private static final List<Double> FRACTIONAL_AMOUNTS = List.of(0.25, 0.5, 1.0, 2.0);
	private static final List<Integer> COMMON_AMOUNTS = List.of(100, 250, 500, 750, 1000);

	public static String randomUnits() {
		return randomElementFrom(UNIT_LIST);
	}
	
	public static String randomWateringCans() {
		return randomElementFrom(WATERING_CANS);
	}

	public static String randomElectricGardenTool() {
		return randomElementFrom(ELECTRIC_GARDEN_TOOLS);
	}

	public static String randomRecipe() {
		return randomElementFrom(RECIPES);
	}

	public static String randomDische() {
		return randomElementFrom(COOKWARES);
	}

	public static String randomColor() {
		return randomElementFrom(COLORS);
	}

	public static String randomFruit() {
		return randomElementFrom(FRUITS);
	}

	public static String randomGardenTool() {
		return randomElementFrom(GARDEN_TOOLS);
	}
	public static double randomQuantity() {
		if (RANDOM.nextBoolean()) {
			// Kusové jednotky
			return PIECE_AMOUNTS.get(RANDOM.nextInt(PIECE_AMOUNTS.size()));
		} else {
			// Objemové jednotky
			return randomAmount();
		}
	}

	private static double randomAmount() {
		if (RANDOM.nextBoolean()) {
			return FRACTIONAL_AMOUNTS.get(RANDOM.nextInt(FRACTIONAL_AMOUNTS.size()));
		} else {
			return COMMON_AMOUNTS.get(RANDOM.nextInt(COMMON_AMOUNTS.size()));
		}
	}

	public static String randomPassword() {
		return randomElementFrom(PASSWORDS);
	}

	public static String randomGameName() {
		return randomElementFrom(NAME_OF_GAMES);
	}

	public static String randomNick() {
		return randomElementFrom(PLAYER_NICKS);
	}

	public static String randomFistName() {
		return randomElementFrom(FIRST_NAMES);
	}

	public static String randomFullName() {
		return String.format("%s %s", randomElementFrom(FIRST_NAMES), randomElementFrom(LAST_NAMES));
	}

	public static String randomEmail() {
		return String.format("%s@%s", randomElementFrom(EMAIL_PREFIX), randomElementFrom(EMAIL_DOMAIN));
	}

	public static String randomLastName() {
		return randomElementFrom(LAST_NAMES);
	}

	public static String randomCountry() {
		return randomElementFrom(NATO_COUNTRIES);
	}

	public static String randomCity() {
		return randomElementFrom(CITIES);
	}

	public static String randomBuilding() {
		return randomElementFrom(BUILDINGS);
	}

	public static String randomCompanyName() {
		StringBuilder name = new StringBuilder();
		int numberOfWords = RANDOM.nextInt(2) + 1;
		for (int j = 0; j < numberOfWords; j++) {
			name.append(randomElementFrom(COMPANY_NAMES));
			if (j < numberOfWords - 1) {
				name.append(" ");
			}
		}
		return name.toString();
	}

	public static String randomAddress() {
		return String.format("%s %d, %s, %s", randomElementFrom(STREETS), RANDOM.nextInt(99) + 1,
				randomElementFrom(CITIES), randomElementFrom(POST_NUMBER));
	}

	public static <T> T randomElementFrom(List<T> list) {
		return randomElementFrom(list, RANDOM);
	}

	public static <T> T randomElement(T[] array) {
		return array[RANDOM.nextInt(array.length)];
	}

	public static <T> T randomElementFrom(List<T> list, Random r) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(r.nextInt(list.size()));
	}

	public static LocalDate randomPreviousDate() {
		return LocalDate.of(LocalDate.now().getYear() - RANDOM.nextInt(80), RANDOM.nextInt(12) + 1, 1)
				.plusDays(RANDOM.nextInt(31));
	}

	public static LocalDate randomDateInFuture() {
		return LocalDate.now().plusDays(RANDOM.nextInt(400));
	}

	public static LocalDateTime randomPreviousDateTime() {
		return LocalDateTime.now().minusMinutes(RANDOM.nextInt(100000));
	}

	public static LocalDateTime randomDateTimeInFuture() {
		return LocalDateTime.now().plusMinutes(RANDOM.nextInt(100000));
	}

	private Tools() {
		/* hide public one - nothing to do */
	}
}
