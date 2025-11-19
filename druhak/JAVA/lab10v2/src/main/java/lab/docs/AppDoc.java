package lab.docs;

import java.util.Collection;
import java.util.List;

public class AppDoc {

	/**
	 * Jako samostatné třídy/rozhraní v tomto balíku vytvořte:
	 *
	 * Vytvořte rozhraní Document s metodou getContent, která vrací String
	 *
	 *
	 * Vytvořte třídu Email, která implementuje rozhraní Document a má jako
	 * instanční proměnnou content typu String. Má kostruktor s jedním parametrem a
	 * implementuje potřebné metody.
	 *
	 *
	 * Vytvořte abstraktní třídu SigneDocument (podepsaný dokument), která
	 * implementuje rozhraní Docuemnt. Navíc má:
	 *
	 * abstraktní metodu getNotSignedContent - vrací String (nepodepsaný obsah)
	 *
	 * metodu getSignature - vrací String, který obsahuje text "Signature:" a
	 * následuje délka nepodepsaného obsahu
	 *
	 * překrývá metodu getContent - ta vrací nepodepsaný obsah spojený s podpisem,
	 * ideálně je podpis na novém řádku
	 *
	 *
	 * Vytvořte třídu Pdf, která rozšiřuje třídu SigneDocument a má instanční
	 * proměnnou jménem text (String).
	 *
	 * Má kostruktor s jedním parametrem typu String.
	 *
	 * Metoda getNotSignedContent vrací obsah proměnné text obalený řetězcem
	 * "--PDF--"
	 *
	 *
	 * Vytvořte třídu Encrypted, která rozšiřuje třídu SigneDocument a má instanční
	 * proměnnou jménem text (String).
	 *
	 * Má kostruktor s jedním parametrem typu String.
	 *
	 * Metoda getNotSignedContent vrací zašifrovaný (převedený na velká písmena)
	 * obsah proměnné text
	 *
	 */
	public static void main(String[] args) {
		createDocs("abcdef");
		printDocs(createDocs("abcdef"));
	}

	/**
	 * Vytvořte kolekci (vázaný seznam) objektů implementujících rozhraní Document.
	 *
     * Do kolekce vložte jeden dokument od každého typu. Kolekci vraťte jako návratovou
	 * hodnotu.
	 *
	 * @return - kolekce dokuemntů (Docuemnt). Neměňte typ návratové hodnoty.
	 */
	public static List<?> createDocs(String text) {
        Collection<Document> docs = List.of(
            new Email(text),
            new Pdf(text),
            new Encrypted(text)
        );
        return (List<?>) docs;
	}

	/**
	 * Metoda všechny objekty typu Document vypíše do konzole.
	 *
	 * Projděte kolekci a pokud je prvek kolekce typu Document vypište jeho obsah do
	 * konzole.
	 *
	 * Neměňte typ parametru, projděte kolekci a použijte přetypování.
	 *
	 * @param docs - koelkce dokumentů
	 */
	public static void printDocs(List<?> docs) {
        for (Object obj : docs) {
            if (obj instanceof Document doc) {
                System.out.println(doc.getContent());
            }
        }
	}
}
