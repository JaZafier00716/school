Tady je celý tahák převedený do surového zdrojového kódu Markdownu (takže si ho můžeš snadno zkopírovat do poznámek) a doplněný o konkrétní příklady u všech zmíněných funkcí. 

Pro zkopírování stačí kliknout na tlačítko "Copy" v pravém horním rohu bloku.

```markdown
# 🛠️ 1. Základní nastavení & Prerekvizity

* **Zobrazení výstupu v konzoli:** Bez tohoto příkazu neuvidíš výsledky `dbms_output.put_line`!
  ```sql
  SET SERVEROUTPUT ON;
  ```
* **Nastavení formátu data v relaci:**
  ```sql
  ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS'; 
  ```

---

# 💡 2. Zkouškové "Killer" Funkce

Běžné funkce, které se neustále objevují v zadáních.

### Ošetření NULL hodnot (Zásadní!)
* **`NVL(hodnota, nahrada)`**: Nahradí NULL zadanou hodnotou.
  * *Příklad:* `v_celkem := v_zaklad + NVL(v_bonus, 0);` (Kdyby byl bonus NULL, počítá se jako 0).
* **`COALESCE(v_val1, v_val2, v_val3)`**: Vrátí první hodnotu v seznamu, která není NULL.
  * *Příklad:* `v_kontakt := COALESCE(v_telefon, v_email, 'Nezadáno');`

### Matematika a Logika
* **`GREATEST(v_val, 0)`**: Mnohem lepší než složité IFy, pokud potřebuješ zajistit, aby číslo nebylo záporné.
  * *Příklad:* `v_odmena := GREATEST(v_vypocitana_odmena, 0);` (Vrátí buď odměnu, nebo 0, pokud by odměna vyšla záporně).
* **`MOD(cislo, delitel)`**: Zbytek po dělení.
  * *Příklad:* `IF MOD(v_id, 2) = 0 THEN dbms_output.put_line('Sudé ID'); END IF;`
* **`TRUNC(cislo_nebo_datum, pocet_mist)`**: Odsekne desetinná místa bez zaokrouhlování, nebo odsekne čas z data.
  * *Příklad (číslo):* `v_cislo := TRUNC(3.99);` (Výsledek je 3).
  * *Příklad (datum):* `v_dnes_pulnoc := TRUNC(SYSDATE);` (Vrátí dnešní datum s časem 00:00:00).
* **`CEIL(cislo)`**: Zaokrouhlí vždy nahoru.
  * *Příklad:* `v_strany := CEIL(10.1);` (Výsledek je 11).
* **`ROUND(cislo, pocet_mist)`**: Klasické zaokrouhlení.
  * *Příklad:* `v_cena := ROUND(15.567, 2);` (Výsledek je 15.57).

### Práce s Datem
* **`SYSDATE`**: Aktuální čas a datum na serveru.
  * *Příklad:* `INSERT INTO logy (cas) VALUES (SYSDATE);`
* **`EXTRACT(cast FROM datum)`**: Nejlepší na získání konkrétní části data (YEAR, MONTH, DAY...).
  * *Příklad:* `v_rok := EXTRACT(YEAR FROM SYSDATE);`
* **`MONTHS_BETWEEN(datum1, datum2)`**: Výpočet věku/rozdílu v měsících.
  * *Příklad:* `v_vek_roky := TRUNC(MONTHS_BETWEEN(SYSDATE, v_narozeni) / 12);`
* **`ADD_MONTHS(datum, pocet)`**: Bezpečné přičítání/odčítání měsíců.
  * *Příklad:* `v_pristi_mesic := ADD_MONTHS(SYSDATE, 1);`

### Agregace Textu
* **`LISTAGG(sloupec, 'oddelovac') WITHIN GROUP (ORDER BY sloupec)`**: Spojí více řádků do jednoho textu.
  * *Příklad:* ```sql
    SELECT LISTAGG(name, ', ') WITHIN GROUP (ORDER BY name) INTO v_seznam_autoru 
    FROM z_author;
    ```

---

# 🔍 3. Práce se SELECTy a Proměnnými

* **SELECT INTO do více proměnných:** Počet a pořadí sloupců musí přesně odpovídat proměnným.
  ```sql
  SELECT article_author_count, article_institution_count 
  INTO v_au_count, v_i_count
  FROM WORK_Z_ARTICLE 
  WHERE aid = p_aid;
  ```
* **Omezení na 1 řádek:** Na konec dotazu dej `FETCH FIRST 1 ROW ONLY;`.
* **Počet dotčených řádků:**
  * `SQL%ROWCOUNT` vrací počet řádků ovlivněných posledním DML příkazem (INSERT/UPDATE/DELETE).
  * *Příklad:* `v_smazano := SQL%ROWCOUNT;`

---

# 🔄 4. Kurzory a Cykly

Používej **Cursor FOR Loop** všude, kde to jde. Je bezpečný, sám se otevírá i zavírá a nemusíš explicitně deklarovat proměnnou řádku.

```sql
DECLARE
    CURSOR c_data IS SELECT name, year FROM z_article;
BEGIN
    FOR v_row IN c_data LOOP
        dbms_output.put_line(v_row.name || ' ' || v_row.year);
    END LOOP;
END;
```

---

# ⚡ 5. Dynamické SQL (`EXECUTE IMMEDIATE`)

* **Kdy použít:** Když dynamicky měníš jména tabulek/sloupců. Názvy nelze předat jako bind proměnné, musí se zřetězit.
* **Zkoušková past č. 1 (Středník):** Uvnitř dynamického stringu **NESMÍ** být na konci středník `;`!
* **Zkoušková past č. 2 (Bind proměnné v DDL):** Vázané proměnné (`USING`) lze použít **pouze** v DML (INSERT, UPDATE, DELETE). V DDL (CREATE, DROP, ALTER) nejsou povoleny!

```sql
-- DDL (Zřetězení, bez USING, bez středníku na konci)
EXECUTE IMMEDIATE ('CREATE TABLE ' || v_table_name || ' AS SELECT * FROM x');

-- DML (Bezpečné s USING)
EXECUTE IMMEDIATE ('UPDATE tabulka SET hodnota = :1 WHERE id = :2') USING v_hodnota, v_id;
```

---

# 🛡️ 6. Triggery a past ORA-04091

* **Mutating Table Error (ORA-04091):** Nastane, když se řádkový trigger (`FOR EACH ROW`) snaží přes `UPDATE/INSERT/DELETE/SELECT` sahat do té samé tabulky, nad kterou zrovna běží.
* **Jak to vyřešit:** Použij `BEFORE INSERT OR UPDATE` trigger a místo příkazu `UPDATE` přímo uprav hodnoty v paměti pomocí pseudozáznamu `:NEW`.

```sql
CREATE OR REPLACE TRIGGER t_moje_tabulka
BEFORE INSERT OR UPDATE ON moje_tabulka
FOR EACH ROW
BEGIN
    -- Neupdatuj tabulku přes SQL příkaz! 
    -- Jen podstrč hodnoty do :NEW a databáze je sama uloží.
    :NEW.pocet_clanku := v_vypocitany_pocet;
    :NEW.stamp := SYSDATE;
END;
```

---

# ⚠️ 7. Správa Výjimek (Exceptions)

Každý blok `SELECT INTO` by měl mít vlastní ošetření, pokud hrozí vícero chyb za sebou.

```sql
BEGIN
    SELECT ...
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        dbms_output.put_line('Záznam nenalezen.'); --
    WHEN TOO_MANY_ROWS THEN
        dbms_output.put_line('Nejednoznačný záznam.'); --
    WHEN OTHERS THEN
        dbms_output.put_line('Neočekávaná chyba: ' || sqlerrm); --
        ROLLBACK;
END;
```

* **Zastavení operace s chybou (hlavně v Triggerech):**
  ```sql
  RAISE_APPLICATION_ERROR(-20001, 'Text tvojí chyby ' || sqlerrm); --
  ```

---

# 📂 8. Systémové tabulky (Znalost na testy nutná!)

Testovací zadání velmi často chtějí ověřit existenci tabulky nebo sloupce. Názvy v těchto katalozích jsou vždy **VELKÝMI PÍSMENY**!

* **Existuje tabulka?**
  ```sql
  SELECT COUNT(*) INTO v_exists 
  FROM USER_TABLES 
  WHERE table_name = 'MOJE_TABULKA'; 
  ```
* **Jaké má tabulka sloupce?**
  ```sql
  SELECT column_name 
  FROM USER_TAB_COLUMNS 
  WHERE table_name = 'Z_INSTITUTION'; 
  ```

---

# 🪄 9. Šikovné DDL/DML Triky

* **Rychlé zkopírování struktury tabulky bez dat:**
  ```sql
  CREATE TABLE nova_tabulka AS 
  SELECT * FROM stara_tabulka WHERE 0 = 1; 
  ```
* **Testování BOOLEAN funkcí:**
  Nemůžeš zavolat funkci vracející BOOLEAN přímo ze SELECTu. Musíš použít anonymní blok:
  ```sql
  DECLARE
      v_res BOOLEAN;
  BEGIN
      v_res := TvojeFunkce('Parametr');
      IF v_res THEN dbms_output.put_line('TRUE'); END IF;
  END;
  /
  ```
```