package lab.socnet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AppSocialNetwork {
    static java.util.Random RANDOM = new java.util.Random();

	public static void main(String[] args) {
		/* Metoda slouží pro testovací účely, můžete si jí měnit */
		List<Post> posts = generatePosts(10);
		sortByHashtag(posts);
		Post.print(posts);
		sortByAuthorAndLength(posts);
		Post.print(posts);
		disablePostForAuthor("src/main/resources/bannedAuthors.txt");
		logPostWithLife();
		mixerForStrings(new String[] { "1♥", "2♥", "3♥", "4♥", "5♥", "6♥", "7♥", "8♥", "9♥", "J♥", "Q♥", "K♥", "A♥" });
		mixerForInt(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });

	}

	/**
	 * Metoda vytvoří kolekci a do ní vygeneruje zadaný počet příspěvků (Post),
	 * využijte kolekce authors, hashtags, samplePosts ze třídy SocialNetwork a
	 * vybírejte z nich náhodné položky.
	 *
	 * Vytvořte statickou třídní proměnnou v této třítě (AppSocialNetwork) ve které,
	 * bude uložena instance náhodného generátoru čísel.
	 *
	 * @param count - počet příspěvků, které se mají vygenerovat do kolekce
	 * @return vytvořená kolekce příspěvků.
	 */
	public static List<Post> generatePosts(int count) {
        List<Post> posts = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            String author = SocialNetwork.authors.get(RANDOM.nextInt(SocialNetwork.authors.size()));
            String hashtag = SocialNetwork.hashtags.get(RANDOM.nextInt(SocialNetwork.hashtags.size()));
            String content = SocialNetwork.samplePosts.get(RANDOM.nextInt(SocialNetwork.samplePosts.size()));
            posts.add(new Post(author, hashtag, content));
        }
        return posts;
	}

	/**
	 * Metoda setřídí kolekci příspěvků podle hashtagu (abecedně od A po Z).
	 * Využijte k setřídění lambda výraz nebo refrenci na metodu (method reference)
	 *
	 * @param posts - kolekce příspěvků k setřídění
	 */
	public static void sortByHashtag(List<Post> posts) {
        posts.sort((p1, p2) -> p1.getHashtag().compareToIgnoreCase(p2.getHashtag()));
    }

	/**
	 * Metoda setřídí kolekci příspěvků podle autora (abecedně od A po Z) a pokud je
	 * autor stejný, tak podle délky textu příspěvku od nejkratšího po nejvyšší.
	 * Využijte k setřídění lambda výraz nebo refrenci na metodu (method reference)
	 *
	 * @param posts - kolekce příspěvků k setřídění
	 */
	public static void sortByAuthorAndLength(List<Post> posts) {
        posts.sort((p1, p2) -> {
            int authorCompare = p1.getAuthor().compareToIgnoreCase(p2.getAuthor());
            if(authorCompare != 0) {
                return authorCompare;
            } else {
                return Integer.compare(p1.getText().length(), p2.getText().length());
            }
        });
	}

    /**
     * Ve třídě AppSocialNetwork vytvořte vnořenou pojmenovanou třídu DisableAuthor,
     * která implementuje rozhraní SocialNetwork.PostFilter. Metoda apply vybírá
     * příspěvky, které se budou zpracovávat v metodě process. Třída DisableAuthor
     * bude mít konstruktor se jménem autora, jehož příspěvky mají být zakázány.
     *
     * Metoda process se zavolá jen pro příspěvky, pro které metoda apply vrátí
     * true.
     *
     * Načtěte ze souboru (předaného jako parametr fileName - jméno souboru) jména autorů
     * příspěvků. a Vytvořte instanci třídy SocialNetwork.
     *
     * Pro tyto autory proveďte:
     *
     * Vytvořte instanci třídy DisableAuthor a předejte ji jméno autora.
     * DisableAuthor pak zakáže (disable) všechny příspěvky daného autora.
     *
     * Až se pro sí´t zakáží všichni autoři ze souboru, vypíší se všechny příspěvky
     * pomocí metody printAll().
     *
     * Pokud metoda proběhne úspěšně vrací TRUE, pokud dojde při čtení zesouboru k
     * výjimce, metoda vypíše messge vyjímky do konzole a vrátí FALSE.
     */

    public class DisableAuthor implements SocialNetwork.PostFilter {
        private String bannedAuthor;

        public DisableAuthor(String bannedAuthor) {
            this.bannedAuthor = bannedAuthor;
        }

        @Override
        public boolean apply(Post post) {
            return post.getAuthor().equalsIgnoreCase(bannedAuthor);
        }

        @Override
        public void process(Post post) {
            if(apply(post)) {
                post.disable();
            }
        }
    }

	public static boolean disablePostForAuthor(String fileName) {
		try(java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(fileName))) {
            SocialNetwork socialNetwork = new SocialNetwork();
            String line;
            while((line = br.readLine()) != null) {
                DisableAuthor disableAuthor = new AppSocialNetwork().new DisableAuthor(line.trim());
                socialNetwork.processPosts(disableAuthor);
            }
            socialNetwork.printAll();
            return true;
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }

        return false;
	}

	/**
	 * Vytvořte instanci třídy SocialNetwork a pomocí její metody processPosts a
	 * vnořené anonymní třídy, která implementuje rozhraní SocialNetwork.PostFilter,
	 * vypište do konzole všechny příspěvky obsahující slovo "life"
     * (velké a malé písmenka nehrají roli, takže například i variantu Life).
     *
	 * Metoda process se zavolá jen pro příspěvky, pro které metoda apply vrátí
	 * true.
	 */
	public static void logPostWithLife() {
        SocialNetwork socialNetwork = new SocialNetwork();
        socialNetwork.processPosts(new SocialNetwork.PostFilter() {
            @Override
            public boolean apply(Post post) {
                return post.getText().toLowerCase().contains("life");
            }

            @Override
            public void process(Post post) {
                if(apply(post)) {
                    System.out.println(post);
                }
            }
        });
	}

    /**
     * Vytvořte generickou třídu ObjectMixer, která budemít instanční proměnnou typu
     * list.
     *
     * Třída bude mít:
     *
     * metodu add, pro přidání jednoho elementu do seznamu. Metoda po vložení
     * promíchá (shuffle) všechny prvky v seznamu.
     *
     * metodu add, pro přidání listu elementů do seznamu. Metoda po vložení
     * promíchá (shuffle) všechny prvky v seznamu.
     *
     * metodu getFirst, která odebere a vrátí první prvek z listu.
     *
     * @param cards- jména karet pro vložení do ObjectMixer
     * @return objekt typu ObjectMixer
     */
	public static Object mixerForStrings(String[] cards) {
        ObjectMixer<String> mixer = new ObjectMixer<>();
        for(String card : cards) {
            mixer.add(card);
        }
		return mixer;
	}

	/**
	 * Stejnou generickou třídu ObjectMixer, popsanou u předchozí metody, použijte
	 * pro uložeí celých čísel.
	 *
	 * @param numbers - čísla pro vložení do ObjectMixer
	 * @return objekt typu ObjectMixer
	 */
	public static Object mixerForInt(int[] numbers) {
        ObjectMixer<Integer> mixer = new ObjectMixer<>();
        for(int number : numbers) {
            mixer.add(number);
        }
        return mixer;
	}

}
