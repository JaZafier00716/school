/*
Napište proceduru InstitutionReport, která bude mít na vstupu id instituce iid, následně vytvoří seznam reportů (samostatných tabulek) pro jednotlivé roky pojmenované REPORT<year> (například: REPORT2025). 
Report bude obsahovat informace o jednotlivých autorech, kde u každého autora bude informace o tom, kolik článků v daném roce napsal 
a v kolika unikátní (různých) časopisech v daném roce publikoval (příklad výstupu: year, rid, article_count, journal_count). Pokud by byl report pro danou instituci a daný rok prázdný, nevytvoříte ho.

Na výstup bude procedura vypisovat názvy jednotlivých reportů (vytvořených tabulek) Například pro instituci s číslem 75:
REPORT2019
REPORT2020
REPORT2021
REPORT2022
REPORT2023

Pokud již tabulka s daným rokem existuje (viz tabulka user_tables), původní tabulka se vymaže a pokračuje se dále v generování. Tabulky, které by byly prázdné (v daném roce by neobsahovaly žádný záznam) se nevytvářejí.

V řešení nepoužívejte SQL defragmentaci pomocí kurzoru, pro naplnění tabulky použijte operaci insert – select nebo create – select.
*/

CREATE OR REPLACE PROCEDURE InstitutionReport (
    p_iid Z_INSTITUTION.iid%TYPE
)
IS
    -- Kurzor zjistí pouze roky, ve kterých má instituce nějaké autory s články
    CURSOR c_years IS
        SELECT DISTINCT ar.year
        FROM Z_ARTICLE_INSTITUTION ai
        JOIN Z_ARTICLE ar ON ar.aid = ai.aid
        WHERE ai.iid = p_iid
        ORDER BY ar.year;
        
    v_year Z_ARTICLE.year%TYPE;
    v_table_exists INT;
    v_table_name VARCHAR2(50);
BEGIN
    OPEN c_years;
    LOOP
        FETCH c_years INTO v_year;
        EXIT WHEN c_years%NOTFOUND;
        
        -- Názvy tabulek v USER_TABLES jsou vždy velkými písmeny
        v_table_name := 'REPORT' || v_year;
        
        -- Zjištění, zda tabulka už existuje
        SELECT COUNT(*) INTO v_table_exists
        FROM USER_TABLES
        WHERE table_name = v_table_name;
        
        -- Pokud existuje, smažeme ji
        IF v_table_exists > 0 THEN
            dbms_output.put_line('Table ' || v_table_name || ' already exists, removing existing table');
            EXECUTE IMMEDIATE 'DROP TABLE ' || v_table_name;
        END IF;
        
        -- Vytvoření tabulky a naplnění daty (bez středníku na konci uvnitř stringu!)
        -- Použití COUNT(DISTINCT) pro zajištění přesných počtů
        EXECUTE IMMEDIATE '
            CREATE TABLE ' || v_table_name || ' AS
            SELECT 
                ar.year, 
                aa.rid, 
                COUNT(DISTINCT ar.aid) AS article_count, 
                COUNT(DISTINCT ar.jid) AS journal_count
            FROM Z_ARTICLE_INSTITUTION ai
            JOIN Z_ARTICLE ar ON ar.aid = ai.aid
            JOIN Z_ARTICLE_AUTHOR aa ON aa.aid = ar.aid 
            WHERE ai.iid = ' || p_iid || ' AND ar.year = ' || v_year || ' 
            GROUP BY ar.year, aa.rid
        ';
        DBMS_OUTPUT.PUT_LINE(v_table_name);
    END LOOP;
    CLOSE c_years;    

EXCEPTION
    WHEN OTHERS THEN
        -- Pokud nastane chyba, je dobré zkontrolovat, zda nezůstal viset otevřený kurzor
        IF c_years%ISOPEN THEN
            CLOSE c_years;
        END IF;
        dbms_output.put_line('An error has occured: ' || sqlerrm);
END;

EXECUTE InstitutionReport(75);

SELECT COUNT(*)
FROM REPORT2019


/*
Vytvořte tabulku institution_count, která bude obsahovat iid, article_count (počet článků pro tuto instituci), a stamp = časové razítko poslední aktualizace, 
které jsou pro danou instituci napsány a nejedná se o české nebo slovenské články (tabulka z_journal, sloupec czech_or_slovak, hodnota NE).
Napište trigger t_institution_count, který při přidání nebo aktualizaci záznamu v tabulce, která již existuje v seznamu, spočítá (aktualizuje) počet jimi publikovaných článků a tento počet aktualizují, 
včetně časového razítka pro poslední změnu záznamu (počet článků se počítá z tabulky z_article_institution).

Pokud instituce s daným iid nemá žádný napsaný článek, vyvolejte vlastní výjimku s informací 'No article for institution' a vykonávání dotazu se zastaví s chybou číslo -20001, ID not exists.

Řešení může být jeden trigger nebo dva samostatné triggery pro různé operace.
*/
 
CREATE TABLE institution_count AS
SELECT ai.iid as iid, COUNT(DISTINCT ar.aid) as article_count, SYSDATE as stamp
FROM Z_ARTICLE_INSTITUTION ai
JOIN Z_ARTICLE ar on ar.aid = ai.aid
JOIN Z_JOURNAL j on j.jid = ar.jid
WHERE czech_or_slovak = 'NE'
GROUP BY iid;

CREATE OR REPLACE TRIGGER T_INSTITUTION_COUNT 
BEFORE INSERT OR UPDATE
ON institution_count
FOR EACH ROW
DECLARE
    v_ar_count INT;
BEGIN
    SELECT COUNT(DISTINCT ar.aid) INTO v_ar_count
    FROM Z_ARTICLE_INSTITUTION ai
    JOIN Z_ARTICLE ar on ar.aid = ai.aid
    JOIN Z_JOURNAL j on j.jid = ar.jid
    WHERE czech_or_slovak = 'NE' AND ai.iid = :new.iid;
    
    IF v_ar_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'ID not exists');
    END IF;
    
    :new.article_count := v_ar_count;
    :new.stamp := SYSDATE;
    
    dbms_output.put_line('Count updated to: ' || v_ar_count);
    
END;

SELECT *
FROM institution_count


-- 1. Vložíme pouze ID instituce
INSERT INTO institution_count (iid) VALUES (2);

-- 2. Zkontrolujeme výsledek. 
-- Měl bys vidět iid 75, vypočítaný počet článků a aktuální datum a čas.
SELECT * FROM institution_count;


-- 1. "Popíchneme" trigger updatem existujícího řádku
UPDATE institution_count SET iid = 75 WHERE iid = 75;

-- 2. Zkontrolujeme výsledek. 
-- Časové razítko (stamp) by mělo být o něco novější než v předchozím kroku.
SELECT * FROM institution_count WHERE iid = 75;