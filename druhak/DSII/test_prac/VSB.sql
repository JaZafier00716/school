/*
Bind variables only in DML, in DDL they are not allowed!!!

IMPORTANT STRUCTURES:
    WHEN TOO_MANY_ROWS THEN ...
    WHEN NO_DATA_FOUND THEN ...
    WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);


    ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';
    
    SQL%ROWCOUNT -- number of affected rows
    
    When copying table structre without copying the data, use WHERE 0 = 1;

    RAISE_APPLICATION_ERROR(-20001, 'Something went wrong... ' || sqlerrm);
    
    For max value from 2 values use: GREATEST(v_val, 0);
    
    FOR fetching only 1 row use:
        FETCH FIRST 1 ROW ONLY;
        
    Replace NULL value:
        NVL(hodnota, nahrada)
        e.g. v_something := NVM(v_null, 0);
        
        COALESCE(v_val1, v_val2, v_val3, ...);
        - returns first non null value
        
DATE & TIME:
    SYSDATE - returns current time & date;
    
    EXTRACT(cast FROM datum)
    - cast - YEAR, MONTH, DAY, HOUR, MINUTE, SECOND
    
    MONTHS_BETWEEN(datum1, datum2)
    
    ADD_MONTHS(datum, pocet)
    
MATH:
    MOD(cislo, delitel)
    
    ROUND(cislo, pocet_mist)
    
    TRUNC(cislo, pocet_mist)
    - cuts off decimal places
    
    CEIL(cislo)
    
LISTAGG(sloupec, 'oddelovac') WITHIN GROUP (ORDER BY sloupec): 
Tohle se na testech objevuje, když máš vypsat jeden řádek, ale připojit k němu seznam hodnot z jiné tabulky 
(např. vypsat název článku a vedle toho čárkou oddělený seznam všech jeho autorů v jednom sloupci).
*/

/*
Napište proceduru P_BuildArticleRatingStats(p_year_from INT, p_mode VARCHAR2),
která vytvoří nebo obnoví tabulku ARTICLE_RATING_STATS obsahující agregovanou statistiku
počtu publikovaných článků (article_count) pro každý ranking podle roku.

Výsledná tabulka musí obsahovat sloupce year, ranking a article_count z tabulek z_article a z_year_field_journal.
Vzpomeňte si na vazbu (jid, year) mezi těmito tabulkami

Tabulku vytvořte pomocí dynamického SQL jako CREATE TABLE ... AS SELECT .... Pokud tabulka již existuje:
Při p_mode = REPLACE ji smažte a vytvořte znovu.
Při p_mode = CREATE vyvolejte vyjímku.
Existenci tabulky zkontrolujte pomocí SELECT z USER_TABLES.

Povinné validace:
- p_year_from nesmí být NULL, jinak má být vyvolána chyba.
- p_mode nesmí být NULL a musí být jedna z hodnot: CREATE nebo REPLACE.
- Pokud tabulka ARTICLE_RATING_STATS existuje a p_mode = CREATE, musí být vyvolána vlastní výjímka.
- Pokud tabulka ARTICLE_RATING_STATS existuje a p_mode = REPLACE, musí být smazána a vytvořena znovu.

Kontrolní vstup a výstup na BAYER:
----------------------------------
vstup:
p_year_from = 2018, p_mode = REPLACE

prvních 10 řádků po seřazení year desc, ranking:
2023 D1 2709
2023 Q1 5418
2023 Q2 8528
2023 Q3 4728
2023 Q4 2463
2022 D1 2774
2022 Q1 5596
2022 Q2 8984
2022 Q3 5092
2022 Q4 2642
*/
ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';

CREATE OR REPLACE PROCEDURE CREATE_WORK_TABLE (
    p_table_name VARCHAR2
) AS
BEGIN
    EXECUTE IMMEDIATE('
        CREATE TABLE WORK_' || UPPER(p_table_name) || ' AS 
        SELECT *
        FROM ' || UPPER(p_table_name)
    );
    dbms_output.put_line('table WORK_' || UPPER(p_table_name) || ' created successfully');
EXCEPTION
    WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);
END;



SELECT
    *
FROM
    user_tables;

CREATE OR REPLACE PROCEDURE p_buildarticleratingstats (
    p_year_from INT,
    p_mode      VARCHAR2
) IS
    v_table_exists INT;
    exception_bad_arguments EXCEPTION;
    exception_table_exists EXCEPTION;
BEGIN
    IF p_year_from IS NULL THEN
        dbms_output.put_line('Year cannot be null');
        RAISE exception_bad_arguments;
    END IF;
    IF
        p_mode != 'CREATE'
        AND p_mode != 'REPLACE'
    THEN
        dbms_output.put_line('Only CREATE and REPLACE modes are supported');
        RAISE exception_bad_arguments;
    END IF;

    SELECT
        COUNT(*)
    INTO v_table_exists
    FROM
        user_tables
    WHERE
        table_name = 'ARTICLE_RATING_STATS';

    dbms_output.put_line('Table count: ' || v_table_exists);
    IF
        v_table_exists > 0
        AND p_mode = 'CREATE'
    THEN
        dbms_output.put_line('Table already exists');
        RAISE exception_table_exists;
    END IF;

    IF
        v_table_exists > 0
        AND p_mode = 'REPLACE'
    THEN
        dbms_output.put_line('Existing table dropped');
        EXECUTE IMMEDIATE ( '
            DROP TABLE ARTICLE_RATING_STATS
        ' );
    END IF;

    EXECUTE IMMEDIATE ( '
        CREATE TABLE ARTICLE_RATING_STATS (
            year,
            ranking,
            article_count
        ) AS 
        SELECT yfj.year, yfj.ranking, COUNT(*)
        FROM z_year_field_journal yfj
        LEFT JOIN z_article ar ON ar.jid = yfj.jid AND ar.year = yfj.year
        WHERE yfj.year >= '
                        || p_year_from
                        || '
        GROUP BY yfj.year, yfj.ranking
    ' );
    dbms_output.put_line('Table Created');
EXCEPTION
    WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);
END;

EXECUTE P_BuildArticleRatingStats(2018, 'REPLACE');

SELECT *
FROM ARTICLE_RATING_STATS
ORDER by year
SELECT
    yfj.year,
    yfj.ranking,
    COUNT(*)
FROM
    z_year_field_journal yfj
    LEFT JOIN z_article ar 
        ON ar.jid = yfj.jid
        AND ar.year = yfj.year
WHERE
    yfj.year >= 2020
GROUP BY
    yfj.year,
    yfj.ranking
ORDER BY
    year;
    
/*
Napište funkci F_FindNullableColumns(p_table_name VARCHAR2) RETURN VARCHAR2,
která pro zadanou tabulku vrátí textový seznam sloupců, ve kterém se vyskytuje alespoň jedna hodnota NULL.
Každá položka bude ve formátu <column_name> (<null_count>), položky oddělujte čárkou a mezerou.
Řešení implementujte pomocí kurzoru: nejprve získejte seznam sloupců tabulky a pro každý sloupec
dynamicky zjistěte počet NULL hodnot.

Nápověda: seznam sloupců tabulky najdete v katalogovém pohledu USER_TAB_COLUMNS. V dynamickém SQL se
jména sloupců nemohou předat jako vázané proměnné, pouze hodnoty. Jména sloupců se sestavují zřetězením.

Povinné validace:
- Pokud tabulka neexistuje, funkce musí vrátit NULL a vypsat informativní hlášku.
- Funkce musí detekovat pouze sloupce, ve kterých se vyskytuje alespoň jedna hodnota NULL.
- Testujte na tabulce, kde jsou některé sloupce s NULL a některé bez NULL (např. Z_INSTITUTION).

Kontrolní vstupy a výstupy na BAYER:
------------------------------------
vstup:
p_table_name = Z_INSTITUTION

výstup:
REG_NUMBER (48), STREET (48), POSTAL_CODE (48), TOWN (48),
LEGAL_FORM (48), MAIN_GOAL (48), CREATED (48)

vstup:
p_table_name = TABLE_DOES_NOT_EXIST

výstup:
NULL
*/

SELECT *
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'Z_ARTICLE'

SELECT COUNT(*)
    FROM USER_TABLES
    WHERE TABLE_NAME = 'Z_ARTICLE'



CREATE OR REPLACE FUNCTION F_FindNullableColumns (
    p_table_name VARCHAR2
)
RETURN VARCHAR2
IS
    column_list VARCHAR2(4000) := '';
    table_exists INT;
    v_null_count INT;
    
    CURSOR c_columns IS
        SELECT column_name
        FROM USER_TAB_COLUMNS
        WHERE table_name = UPPER(p_table_name);
BEGIN
    SELECT COUNT(*) INTO table_exists
    FROM USER_TABLES
    WHERE TABLE_NAME = UPPER(p_table_name);
    
    IF table_exists = 0 THEN
        dbms_output.put_line('Table does not exist');
        RETURN 'NULL';
    END IF;

    FOR col IN c_columns LOOP
        EXECUTE IMMEDIATE('
            SELECT COUNT(*)
            FROM ' || p_table_name ||
             ' WHERE ' || col.column_name || ' IS NULL
        ') INTO v_null_count;
        
        IF v_null_count > 0 THEN
            -- IF not first column, add ', '
            IF column_list IS NOT NULL THEN
                column_list := column_list || ', ';
            END IF;
            
            column_list := column_list || col.column_name || ' (' || v_null_count || ')';
        END IF;
        
    END LOOP;
    
    RETURN column_list;
EXCEPTION
     WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);
END;
      

SELECT F_FindNullableColumns('Z_INSTITUTION')
FROM DUAL;
      
SELECT F_FindNullableColumns('TABLE_DOES_NOT_EXIST')
FROM DUAL;

/*
Vytvořte tabulku Z_INSTITUTION_RANK, která bude obsahovat atributy iid (cizí klíč odkazující primární klíč tabulky Z_INSTITUTION)
a rank_order (celé číslo představující pořadí podle úspěšnosti).

Napište uloženou proceduru, která smaže záznamy z tabulky z_insitution_rank a dále každé instituci nastaví pořadí dle celkového počtu článků
(tj. instituce s nejvyšším počtem článků bude mít pořadí 1, instituce s nejnižším počtem článků bude mít pořadí rovné celkovému počtu institucí).

Procedura musí nastavit i institucím bez článku. V případě, že dvě instituce mají stejný počet článků, musí sdílet stejné pořadí.
Úlohu řešte jako transakci.

Prvních 10 nejlepších institucí:
--------------------------------
IID     RANK_ORDER
------- ----------
146     1
75      2
148     3
35      4
203     5
34      6
52      7
197     8
70      9
193     10
*/

CREATE TABLE Z_INSTITUTION_RANK (
    iid NUMBER(38,0) REFERENCES Z_INSTITUTION(iid),
    rank_order INT
);


CREATE OR REPLACE PROCEDURE P_UpdateInstitutionRank
IS
    CURSOR c_institutions IS
        SELECT i.iid, COUNT(ai.aid) AS ar_count
        FROM Z_INSTITUTION i
        LEFT JOIN z_article_institution ai ON ai.iid = i.iid
        GROUP BY i.iid
        ORDER BY COUNT(ai.aid) desc;
    v_prev_ar_count INT := -1;
    v_curr_rank INT := 1;
    v_total_rank INT := 1;
BEGIN
    DELETE FROM Z_INSTITUTION_RANK;
    
    FOR institution IN c_institutions LOOP
        IF institution.ar_count <> v_prev_ar_count THEN
            v_curr_rank := v_total_rank;
        END IF;
    
    
        INSERT INTO Z_INSTITUTION_RANK (iid, rank_order)
        VALUES (institution.iid, v_curr_rank);
        
        dbms_output.put_line(institution.iid || ', ' || v_curr_rank || ', ' || institution.ar_count);
        
        v_total_rank := v_total_rank + 1;
        v_prev_ar_count := institution.ar_count;
    END LOOP;
    
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);
        ROLLBACK;
END;
      
execute P_UpdateInstitutionRank()

select *
from Z_INSTITUTION_RANK

/*
Napište uloženou funkci CopyArticleYear(p_rid, p_year), která vytvoří tabulku article_author_<p_rid> s atributy aid INT and last_update DATE
a jedním příkazem vloží do nové tabulky aid všech článků autora s rid=p_rid pro rok p_year spolu s aktuálním datem.

Pokud autor neexistuje, vrátí funkce -1, v opačném případě vrátí funkce počet záznamů uložených v nové tabulce. N výstup vypište název autora
a název vytvořené tabulky. Před ukončením funkce bude nová tabulka zrušena.

V dynamickém SQL použijte, pokud je to možné, vázané proměnné. Řešte použitím nejvýše 6 databázových operací. Otestujte pro různé varianty výstupu.

Příklad výstupu:
----------------
86577, 2020:

Function outputs:
Gauger, E. F.
article_author_86577
It returns: 45

99999, 2025:

It returns: -1
*/

CREATE OR REPLACE FUNCTION CopyArticleYear (
    p_rid Z_AUTHOR.rid%TYPE,
    p_year Z_ARTICLE.year%TYPE
)
RETURN NUMBER
IS
    v_table_exists  NUMBER;
    v_author_exists NUMBER;
    v_result        NUMBER;
    v_author_name   Z_AUTHOR.name%TYPE;
    v_table_name    VARCHAR2(100);
BEGIN
    v_table_name := 'ARTICLE_AUTHOR_' || p_rid;

--    -- kontrola existence tabulky
--    SELECT COUNT(*) INTO v_table_exists
--    FROM USER_TABLES
--    WHERE table_name = UPPER(v_table_name);
--    
--    IF v_table_exists > 0 THEN
--        EXECUTE IMMEDIATE 'DROP TABLE ' || v_table_name;
--    END IF;
    
    -- kontrola autora
    SELECT COUNT(*) INTO v_author_exists
    FROM z_author
    WHERE rid = p_rid;
    
    IF v_author_exists = 0 THEN
        RETURN -1;
    END IF;
    
    -- vytvoření tabulky
    EXECUTE IMMEDIATE ('
        CREATE TABLE ' || v_table_name || ' AS
        SELECT aa.aid, SYSDATE AS last_update
        FROM z_article_author aa
        LEFT JOIN z_article ar 
            ON ar.aid = aa.aid
        WHERE ar.year = ' || p_year || '
        AND aa.rid = ' || p_rid
    );
    
    -- počet řádků (normální SELECT stačí)
    EXECUTE IMMEDIATE 'SELECT COUNT(*) FROM ' || v_table_name
        INTO v_result;
    
    -- jméno autora
    SELECT name INTO v_author_name
    FROM z_author
    WHERE rid = p_rid;

    DBMS_OUTPUT.PUT_LINE(v_author_name || ' ' || v_table_name);
    
    -- úklid
    EXECUTE IMMEDIATE 'DROP TABLE ' || v_table_name;
    
    RETURN v_result;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('An error has occured: ' || SQLERRM);
        
        -- pokus o cleanup
        BEGIN
            EXECUTE IMMEDIATE 'DROP TABLE ' || v_table_name;
        EXCEPTION
            WHEN OTHERS THEN NULL;
        END;
        
        RETURN NULL;
END;
/

EXECUTE dbms_output.put_line('It returns: ' || CopyArticleYear(3604, 2020));

select au.rid, count(ar.aid) as ar_count
from z_author au
left join z_article_author aa on aa.rid = au.rid
left join z_article ar on ar.aid = aa.aid and year = 2020
where au.rid = 3604
group by au.rid
order by count(ar.aid) desc

/*
Před samotnou implementací si ručně připravte dvě pracovní tabulky: WORK_Z_AUTHOR jako kopii dat ze Z_AUTHOR (CREATE TABLE ... AS SELECT ...) 
a WORK_Z_AUTHOR_NAME_CHANGES pro zaznamenání změn jména autora. Tato pomocná tabulka bude obsahovat sloupce rid, old_name, new_name a change_time.

Napište trigger TR_AuthorNameAudit nad tabulkou WORK_Z_AUTHOR, který se spustí při UPDATE sloupce name pro každý měněný řádek 
a uloží původní a novou hodnotu jména do tabulky WORK_Z_AUTHOR_NAME_CHANGES pouze pro autory, kteří mají více než 5 článků. 
Pokud při zpracování triggeru nastane chyba, ukončete trigger pomocí raise_application_error. Trigger otestujte změnou jména některého autora v tabulce WORK_Z_AUTHOR.

Kontrolní vstupy a výstupy na BAYER:
------------------------------------
vstup:
UPDATE WORK_Z_AUTHOR SET name = 'Sharma, A. TEST' WHERE rid = 3604;

name_changes_recorded: 1

záznam ve WORK_Z_AUTHOR_NAME_CHANGES:
3604 Sharma, A. Sharma, A. TEST 2026-03-25 14:59:08
*/

CREATE TABLE WORK_Z_AUTHOR 
AS SELECT *
FROM Z_AUTHOR

select *
from work_z_author

CREATE TABLE WORK_Z_AUTHOR_NAME_CHANGES (
    rid NUMBER(38,0),
    old_name VARCHAR2(200 BYTE),
    new_name VARCHAR2(200 BYTE),
    change_time DATE
);

CREATE OR REPLACE TRIGGER TR_AuthorNameAudit 
BEFORE UPDATE
ON WORK_Z_AUTHOR
FOR EACH ROW
DECLARE
    v_ar_count INT;
BEGIN
    SELECT COUNT(ar.aid) INTO v_ar_count
    FROM Z_ARTICLE_AUTHOR aa
    LEFT JOIN Z_ARTICLE ar on ar.aid = aa.aid
    WHERE aa.rid = :new.rid;

    IF v_ar_count > 5 THEN
        INSERT INTO WORK_Z_AUTHOR_NAME_CHANGES (rid, old_name, new_name, change_time)
        VALUES (:NEW.rid, :OLD.name, :NEW.name, SYSDATE);
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20001, 'Something went wrong bozo... ' || sqlerrm);
END;


UPDATE WORK_Z_AUTHOR SET name = 'Sharma, A. TEST' WHERE rid = 3604;

SELECT COUNT(*)
FROM WORK_Z_AUTHOR_NAME_CHANGES;


SELECT *
FROM WORK_Z_AUTHOR_NAME_CHANGES;

/*
Napište funkci F_CountDistinctPerColumn(p_table_name VARCHAR2) RETURN VARCHAR2, která pro zadanou tabulku vrátí textový seznam počtů unikátních hodnot pro každý sloupec tabulky. 
Každá položka bude ve formátu <column_name> (<distinct_count>), položky oddělujte čárkou a mezerou. 
Řešení implementujte pomocí kurzoru: nejprve získejte seznam sloupců tabulky a pro každý sloupec dynamicky zjistěte hodnotu COUNT(DISTINCT sloupec). 
Pokud tabulka neexistuje, vraťte NULL a vypište informativní hlášku.

Nápověda: seznam sloupců tabulky najdete v katalogovém pohledu USER_TAB_COLUMNS.

Povinné validace:
- Pokud tabulka neexistuje, funkce musí vrátit NULL a vypsat informativní hlášku.
- Funkce musí vrátit seznam všech sloupců tabulky se jejich COUNT(DISTINCT) hodnotami.
- Formát výsledku musí odpovídat: <název_sloupce> (<distinct_count>), více sloupců odděleno čárkou a mezerou.
- distinct_count musí být správně vypočítán pro každý sloupec.
- Testujte na tabulce s různorodými daty (Z_INSTITUTION).

Kontrolní vstupy a výstupy na BAYER:

vstup:
p_table_name = Z_INSTITUTION

výstup:
IID (277), NAME (277), REG_NUMBER (225), STREET (216), POSTAL_CODE (164), TOWN (76), LEGAL_FORM (16), MAIN_GOAL (31), CREATED (135)

vstup:
p_table_name = TABLE_DOES_NOT_EXIST

výstup:
NULL
*/

--SET SERVEROUTPUT ON

CREATE OR REPLACE FUNCTION F_CountDistinctPerColumn (
    p_table_name VARCHAR2
)
RETURN VARCHAR2
IS
    v_table_exists INT;
    v_count INT;
    v_return_string VARCHAR2(2000) := '';
    
    CURSOR c_table_cols IS
        SELECT *
        FROM USER_TAB_COLUMNS
        WHERE table_name = UPPER(p_table_name);
BEGIN
    SELECT COUNT(*) INTO v_table_exists
    FROM USER_TABLES
    WHERE table_name = UPPER(p_table_name);
    
    IF v_table_exists = 0 THEN
        dbms_output.put_line('Table does not exists');
        RETURN NULL;
    END IF;
    
    FOR col IN c_table_cols LOOP
        EXECUTE IMMEDIATE('
            SELECT COUNT(DISTINCT ' || col.column_name || ')
            FROM ' || p_table_name) INTO v_count;

        IF v_return_string IS NOT NULL THEN
            v_return_string := v_return_string || ', ';
        END IF;
        
        v_return_string := v_return_string || col.column_name || ' (' || v_count || ')';

    END LOOP;
    
    
    return v_return_string;
EXCEPTION
     WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);
        return NULL;
END;

execute dbms_output.put_line(F_CountDistinctPerColumn('Z_INSTITUTION'));

SELECT F_CountDistinctPerColumn('Z_INSTITUTION')
FROM DUAL;

/*
Vytvořte si kopie tabulek Z_INSTITUTION a Z_ARTICLE_INSTITUTION pomocí CREATE TABLE AS SELECT ... a vytvořte tak WORK_Z_INSTITUTION a WORK_Z_ARTICLE_INSTITUTION. Nad těmito kopiemi dále pracujte.
Napište proceduru P_DeleteInstitutionByName(p_inst_name VARCHAR2), která smaže instituci podle zadaného názvu. Před smazáním nejprve ověřte, zda instituce existuje. 
Mazání proveďte transakčně tak, aby se nejprve odstranily záznamy z vazební tabulky WORK_Z_ARTICLE_INSTITUTION a teprve poté záznam v tabulce WORK_Z_INSTITUTION. 
Pokud instituce neexistuje, vypište informativní hlášku a nic nemažte. Při úspěšném smazání vypište potvrzení o úspěšném provedení, při chybě vypište chybovou hlášku. Procedura bude transakční.

Povinné validace:
- p_inst_name nesmí být NULL.
- Pokud instituce neexistuje, vypište informaci a nic nemažte.
- Nejprve smažte všechny záznamy z vazební tabulky WORK_Z_ARTICLE_INSTITUTION pro danou instituci.
- Teprve poté smažte samotnou instituci z WORK_Z_INSTITUTION.
- Při chybě proveďte ROLLBACK.

Kontrolní vstupy a výstupy na BAYER:
------------------------------------
vstup:
p_inst_name = NonExistent Institution

DBMS_OUTPUT:
Instituce NonExistent Institution neexistuje.

vstup:
p_inst_name = Univerzita Karlova

DBMS_OUTPUT:
Instituce Univerzita Karlova byla uspesne smazana.
Odstraneno zaznamu z vazebni tabulky: 23338.
institution_remaining:          0
institution_links_remaining:    0
*/

CREATE TABLE WORK_Z_INSTITUTION AS
SELECT *
FROM Z_INSTITUTION;

CREATE TABLE WORK_Z_ARTICLE_INSTITUTION AS
SELECT *
FROM Z_ARTICLE_INSTITUTION;

CREATE OR REPLACE PROCEDURE P_DeleteInstitutionByName (
    p_inst_name VARCHAR2
)
IS
    v_inst_exists INT;
    v_inst_id WORK_Z_INSTITUTION.iid%TYPE;
    v_deleted_links INT;
BEGIN
    IF p_inst_name IS NULL THEN
        dbms_output.put_line('Institution name cannot be null');
        RETURN;
    END IF;
    
    SELECT COUNT(*) INTO v_inst_exists
    FROM WORK_Z_INSTITUTION
    WHERE name = p_inst_name;
    
    IF v_inst_exists = 0 THEN
        dbms_output.put_line('Instituce ' || p_inst_name || ' neexistuje.');
        RETURN;
    END IF;
    
    SELECT iid INTO v_inst_id
    FROM WORK_Z_INSTITUTION
    WHERE name = p_inst_name;
    
    DELETE FROM WORK_Z_ARTICLE_INSTITUTION 
    WHERE iid = v_inst_id;
    
    v_deleted_links := SQL%ROWCOUNT;
    
    DELETE FROM WORK_Z_INSTITUTION
    WHERE name = p_inst_name;
    
    COMMIT;
    dbms_output.put_line('Instituce ' || p_inst_name || ' byla uspesne smazana.');
    dbms_output.put_line('Odstraneno zaznamu z vazebni tabulky: ' || v_deleted_links);
    dbms_output.put_line('institution_remaining:          0');
    dbms_output.put_line('institution_links_remaining:    0');
    
EXCEPTION
    WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);
        ROLLBACK;
END;
      
      
EXECUTE P_DeleteInstitutionByName('NonExistent Institution');

EXECUTE P_DeleteInstitutionByName('Univerzita Karlova');
      
      
/*
Před samotnou implementací si ručně připravte dvě pracovní tabulky: WORK_Z_ARTICLE jako kopii dat ze Z_ARTICLE (CREATE TABLE ... AS SELECT ...) a WORK_Z_ARTICLE_DELETED se stejnou strukturou jako WORK_Z_ARTICLE.
Napište trigger TR_DeleteAudit nad tabulkou WORK_Z_ARTICLE, který se spustí při DELETE. Trigger bude logovat mazané články, které mají 3 a méně autorů, a zaznamenané články uloží do tabulky WORK_Z_ARTICLE_DELETED. 
Počet autorů určete nad tabulkou WORK_Z_ARTICLE_AUTHOR. Pokud při zpracování triggeru nastane chyba, ukončete trigger pomocí raise_application_error. Trigger otestujte smazáním některého řádku z WORK_Z_ARTICLE.

Kontrolní vstupy a výstupy na BAYER:
------------------------------------
vstup:
DELETE FROM WORK_Z_ARTICLE WHERE aid = 0;

mazaný řádek z WORK_Z_ARTICLE:
0   0   A planar Schrodinger-Newton system with Trudinger-Moser critical growth   2023

záznam ve WORK_Z_ARTICLE_DELETED po smazání:
0   0   A planar Schrodinger-Newton system with Trudinger-Moser critical growth   2023

deleted_audit_count: 1
*/

CREATE TABLE WORK_Z_ARTICLE AS
SELECT *
FROM Z_ARTICLE

CREATE TABLE WORK_Z_ARTICLE_AUTHOR AS
SELECT *
FROM Z_ARTICLE_AUTHOR

CREATE TABLE WORK_Z_ARTICLE_DELETED AS
SELECT *
FROM Z_ARTICLE
WHERE 0 = 1;

SELECT *
FROM WORK_Z_ARTICLE
WHERE aid = 0;

CREATE OR REPLACE TRIGGER TR_DeleteAudit 
BEFORE DELETE
ON WORK_Z_ARTICLE
FOR EACH ROW
DECLARE
    v_author_count INT;
BEGIN
    SELECT COUNT(rid) INTO v_author_count
    FROM WORK_Z_ARTICLE_AUTHOR
    WHERE aid = :old.aid;

    IF v_author_count <= 3 THEN
        dbms_output.put_line('mazaný řádek z WORK_Z_ARTICLE:');
        dbms_output.put_line(:old.aid || ' ' || :old.jid || ' ' || :old.name || ' ' || :old.year);
        INSERT INTO WORK_Z_ARTICLE_DELETED (aid, jid, ut_wos, name, type, year, author_count) 
        VALUES (:old.aid, :old.jid, :old.ut_wos, :old.name, :old.type, :old.year, v_author_count);
        dbms_output.put_line(' ');
        dbms_output.put_line('zaznam ve WORK_Z_ARTICLE_DELETED po smazani:');
        dbms_output.put_line(:old.aid || ' ' || :old.jid || ' ' || :old.name || ' ' || :old.year);
        dbms_output.put_line(' ');
        dbms_output.put_line('deleted_audit_count: ' || SQL%ROWCOUNT);
    END IF;
    
EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20001, 'Something went wrong... ' || sqlerrm);
END;

DELETE FROM WORK_Z_ARTICLE WHERE aid = 0;

DROP table WORK_Z_ARTICLE;
DROP table WORK_Z_ARTICLE_DELETED;
DROP table WORK_Z_ARTICLE_AUTHOR;

/*
Vytvořte si kopie tabulek Z_AUTHOR a Z_ARTICLE_AUTHOR pomocí CREATE TABLE AS SELECT ... a vytvořte tak WORK_Z_AUTHOR a WORK_Z_ARTICLE_AUTHOR. Nad těmito kopiemi dále pracujte.

Napište proceduru P_DeleteAuthorByName(p_author_name VARCHAR2), která smaže autora podle zadaného jména. Před smazáním nejprve ověřte, 
zda autor existuje, a zjistěte jeho identifikátor (RID). Samotné mazání proveďte transakčně tak, 
aby se nejprve odstranily záznamy z vazební tabulky WORK_Z_ARTICLE_AUTHOR a teprve poté záznam v tabulce WORK_Z_AUTHOR. 
Pokud autor neexistuje, vypište informativní hlášku a nic nemažte. Při úspěšném smazání vypište potvrzení o úspěšném provedení, při chybě vypište chybovou hlášku. 
Procedura bude transakční.

Povinné validace:
- p_author_name nesmí být NULL.
- Pokud autor neexistuje, vypište informaci a nic nemažte.
- Nejprve smažte všechny záznamy z vazební tabulky WORK_Z_ARTICLE_AUTHOR pro daného autora.
- Teprve poté smažte samotného autora z WORK_Z_AUTHOR.

Kontrolní vstupy a výstupy na BAYER:
------------------------------------
vstup:
p_author_name = NonExistentAuthor

DBMS_OUTPUT:
Autor NonExistentAuthor neexistuje.

vstup:
p_author_name = Sharma, A.

DBMS_OUTPUT:
Autor Sharma, A. byl uspesne smazan. Odstraneno zaznamu z vazebni tabulky: 1097.
author_remaining:               0
author_links_remaining: 0
*/

CREATE TABLE WORK_Z_AUTHOR AS
SELECT *
FROM Z_AUTHOR;

CREATE TABLE WORK_Z_ARTICLE_AUTHOR AS
SELECT *
FROM Z_ARTICLE_AUTHOR;


CREATE OR REPLACE PROCEDURE P_DeleteAuthorByName (
    p_author_name VARCHAR2
)
IS
    v_author_exists INT;
    v_rid Z_AUTHOR.rid%TYPE;
    v_count INT;
BEGIN
    IF p_author_name IS NULL THEN
        dbms_output.put_line('Auhor name cannot be null');
        RETURN;
    END IF;
    
    SELECT COUNT(*) INTO v_author_exists
    FROM WORK_Z_AUTHOR
    WHERE name = p_author_name;
    
    IF v_author_exists = 0 THEN
        dbms_output.put_line('Autor ' || p_author_name || ' neexistuje');
        return;
    END IF;
    
    SELECT rid INTO v_rid
    FROM WORK_Z_AUTHOR
    WHERE name = p_author_name;
    
    
    
    
    DELETE FROM WORK_Z_ARTICLE_AUTHOR WHERE rid = v_rid;
    v_count := SQL%ROWCOUNT;
    
    DELETE FROM WORK_Z_AUTHOR WHERE rid = v_rid;
    
    COMMIT;
    
    dbms_output.put_line(p_author_name || ' byl uspesne smazan. Odstraneno zaznamu z vazebni tabulky: ' || v_count);
    dbms_output.put_line('author_remaining:       0');
    dbms_output.put_line('author_links_remaining: 0');
    
EXCEPTION
    WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);
        ROLLBACK;
END;

EXECUTE P_DeleteAuthorByName('NonExistentAuthor');
      
EXECUTE P_DeleteAuthorByName('Sharma, A.');


/*
Vytvořte proceduru PSampleIntColumn, která bude provádět následující kroky:

(a) Přijme tři parametry - p_table (název tabulky), p_column (název sloupce), p_sample_rate (míra vzorkování v procentech).
(b) Zkontroluje, zda je hodnota p_sample_rate v rozmezí 0 až 100. Pokud není, vyvolá výjimku.
(c) Vytvoří novou tabulku s názvem <p_table>_SAMPLE, která bude obsahovat pouze sloupec <p_column> typu INT. Pokud tabulka již existuje, bude smazána a znovu vytvořena.
(d) Na základě p_sample_rate navzorkuje sloupec p_column z tabulky p_table do vytvořené tabulky <p_table>_SAMPLE.
(e) Pokud dojde k chybě, vypíše chybovou zprávu a vyvolá výjimku.

Vzorkování musí být uniformní, tzn. nelze pouze vzít prvních p_sample_rate % záznamů z tabulky jako vzorky. Pro otestování procedury spusťte s platnými i neplatnými parametry.

Výsledek:
---------
exec PSampleIntColumn('Z_FIELD_FORD', 'FID', 20); -- vytvori tabulku 'Z_FIELD_FORD_SAMPLE' 
select * from Z_FIELD_FORD_SAMPLE;
+---+
|FID| 
+---+
|16 | 
|24 | 
|29 | 
|41 | 
|52 | 
|57 | 
|64 | 
+---+
*/

CREATE OR REPLACE PROCEDURE PSampleIntColumn (
    p_table VARCHAR2,
    p_column VARCHAR2,
    p_sample_rate INT
)
IS
    e_out_of_range EXCEPTION;
    v_table_exists INT;
    v_sample_every INT;
    v_counter INT := 0;
    c_cursor SYS_REFCURSOR;
    v_val INT;
BEGIN

    IF p_sample_rate < 0 OR p_sample_rate > 100 THEN
        RAISE e_out_of_range;
    END IF;
    
    SELECT COUNT(*) INTO v_table_exists
    FROM USER_TABLES
    WHERE table_name = UPPER(p_table || '_SAMPLE');
    
    IF v_table_exists > 0 THEN
        dbms_output.put_line('Table exists - recreating table');
        EXECUTE IMMEDIATE ('DROP TABLE ' || p_table || '_SAMPLE');
    END IF;
    
    
    EXECUTE IMMEDIATE ('
        CREATE TABLE ' || p_table || '_SAMPLE (' || p_column || ' INT)
    ');
    
    v_sample_every := CEIL(100 / p_sample_rate);
    
    OPEN c_cursor FOR ('SELECT ' || p_column || ' FROM ' || UPPER(p_table));
    
    LOOP
        FETCH c_cursor INTO v_val;
        
        EXIT WHEN c_cursor%NOTFOUND;
        
        IF v_counter >= v_sample_every THEN
            EXECUTE IMMEDIATE ('
                INSERT INTO ' || p_table || '_SAMPLE VALUES (:1)
            ') USING v_val;
            v_counter := 0;
        END IF;
        v_counter := v_counter + 1;
    END LOOP;
    CLOSE c_cursor;

EXCEPTION
    WHEN e_out_of_range THEN
        dbms_output.put_line('Sample rate has to be a number between 0 and 100');
    WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);
        RAISE;
END;

/*
RANDOMIZATION OF VALUES
*/

EXECUTE PSampleIntColumn('Z_FIELD_FORD', 'FID', 20); -- vytvori tabulku 'Z_FIELD_FORD_SAMPLE' 
select * from Z_FIELD_FORD_SAMPLE;

CREATE OR REPLACE PROCEDURE PSampleIntColumn (
    p_table VARCHAR2,
    p_column VARCHAR2,
    p_sample_rate INT
)
IS
    e_out_of_range EXCEPTION;
    v_table_exists INT;
    v_dyn_sql VARCHAR2(1000);
BEGIN
    -- (b) Check if sample rate is between 0 and 100
    IF p_sample_rate < 0 OR p_sample_rate > 100 THEN
        RAISE e_out_of_range;
    END IF;
    
    -- Check if the sample table already exists
    SELECT COUNT(*) INTO v_table_exists
    FROM USER_TABLES
    WHERE table_name = UPPER(p_table || '_SAMPLE');
    
    -- (c) Drop if it exists
    IF v_table_exists > 0 THEN
        EXECUTE IMMEDIATE 'DROP TABLE ' || p_table || '_SAMPLE';
    END IF;
    
    -- (c) Create the new table with the specified column as INT
    EXECUTE IMMEDIATE 'CREATE TABLE ' || p_table || '_SAMPLE (' || p_column || ' INT)';
    
    -- (d) Sample the data uniformly using DBMS_RANDOM and insert it
    -- This fetches exactly the percentage of rows requested, distributed randomly
    IF p_sample_rate > 0 THEN
        v_dyn_sql := 'INSERT INTO ' || p_table || '_SAMPLE ' ||
                     'SELECT ' || p_column || ' FROM (' ||
                     '    SELECT ' || p_column || ' FROM ' || p_table || 
                     '    ORDER BY DBMS_RANDOM.VALUE' || -- Shuffles the table completely
                     ') ' ||
                     'WHERE ROWNUM <= (SELECT CEIL(COUNT(*) * ' || (p_sample_rate / 100) || ') FROM ' || p_table || ')';
                     
        EXECUTE IMMEDIATE v_dyn_sql;
        COMMIT;
    END IF;

EXCEPTION
    WHEN e_out_of_range THEN
        dbms_output.put_line('Chyba: Mira vzorkovani musi byt v rozmezi 0 az 100.');
        RAISE; -- Re-raises the exception to halt execution, as required
    WHEN OTHERS THEN
        -- (e) Print error message and raise exception
        dbms_output.put_line('Doslo k neocekavane chybe: ' || sqlerrm);
        ROLLBACK;
        RAISE; 
END;

/*
Napište funkci FGetArticleReward s parametrem aid článku, která vrátí výšku odměny za článek jako INT. Odměna za článek je určena podle rankingu časopisu. Odměna je dána následovně:
- Decil - 100 000
- Q1 - 50 000
- Q2 - 25 000
- Q3 - 15 000
- Q4 - 5 000
Pokud byl článek (resp. časopis článku) hodnocen v daném roce ve více oborech FORD, odměna je za nejvyšší hodnocení v pořadí Decil, Q1, Q2, Q3, Q4.
Pokud se na článku podílela instituce z Ostravy, odměna se zvýší o 10 000.
Pokud se na článku podílela instituce z Brna, odměna se sníží o 10 000.
Odměna musí být nezáporné číslo (tzn. ≥ 0).

Výsledek:
---------

+-----+----------------------+
|AID  |FGETARTICLEREWARD(AID)| 
+-----+----------------------+
|9    |100000                |
|27   |100000                |
|118  |100000                |
|453  |50000                 |
|1005 |25000                 |
|8499 |100000                |
|11949|25000                 |
+-----+----------------------+
*/

SELECT *
FROM z_institution

CREATE OR REPLACE FUNCTION FGetArticleReward (
    p_aid Z_ARTICLE.aid%TYPE   
)
RETURN INT
IS
    v_reward INT := 0;
    v_brno INT;
    v_ostrava INT;
    v_rank z_year_field_journal.ranking%TYPE;
BEGIN        
    --- TOWN
    SELECT COUNT(*) INTO v_ostrava
    FROM z_article ar
    JOIN z_article_institution ai on ai.aid = ar.aid
    JOIN z_institution i on i.iid = ai.iid
    WHERE ar.aid = p_aid AND i.town LIKE 'OSTRAVA%';
    
    SELECT COUNT(*) INTO v_brno
    FROM z_article ar
    JOIN z_article_institution ai on ai.aid = ar.aid
    JOIN z_institution i on i.iid = ai.iid
    WHERE ar.aid = p_aid AND i.town LIKE 'BRNO%';
    
    SELECT yfj.ranking INTO v_rank
    FROM z_article ar
    JOIN z_year_field_journal yfj on yfj.jid = ar.jid and ar.year = yfj.year
    WHERE ar.aid = p_aid
    ORDER BY yfj.ranking
    FETCH FIRST 1 ROW ONLY;
    
    IF v_rank = 'D1' THEN
        v_reward := v_reward + 100000;
    ELSIF v_rank = 'Q1' THEN
        v_reward := v_reward + 50000;
    ELSIF v_rank = 'Q2' THEN
        v_reward := v_reward + 25000;
    ELSIF v_rank = 'Q3' THEN
        v_reward := v_reward + 15000;
    ELSIF v_rank = 'Q4' THEN
        v_reward := v_reward + 5000;
    END IF;
    
    IF v_ostrava > 0 THEN
        v_reward := v_reward + 10000;
    END IF;
    IF v_brno > 0 THEN
        v_reward := v_reward - 10000;
    END IF;
    
    RETURN GREATEST(v_reward, 0);
EXCEPTION
     WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);
        RETURN -1;
END;
      
SELECT FGetArticleReward(9)
FROM DUAL;
      
SELECT FGetArticleReward(27)
FROM DUAL;
      
SELECT FGetArticleReward(118)
FROM DUAL;
      
SELECT FGetArticleReward(453)
FROM DUAL;
      
SELECT FGetArticleReward(1005)
FROM DUAL;
      
SELECT FGetArticleReward(8499)
FROM DUAL;
      
SELECT FGetArticleReward(11949)
FROM DUAL;

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

/*
Přidejte do tabulky z_article dva nové atributy (article_author_count INT a article_institution_count INT), 
které budou reprezentovat počet autorů a počet institucí podílejících se na článku. 
Poté tyto sloupce aktualizujte na hodnoty odpovídající existujícím atributům author_count a institution_count.
Napište funkci StatisticsConsistent s parametrem p_processType s možnostmi 'Analyze', 'Fix'. 
Pokud uživatel zadá jinou hodnotu, funkce bude ukončena s chybou.
Funkce zjistí, kolik článků má uvedený nesprávný počet institucí a autorů, 
tj. počet institucí/autorů uvedených u článku (v atributech article_author_count a article_institution_count) 
je jiný než skutečný počet uvedený v tabulkách z_article_institution / z_article_author.
Pokud bude p_processType = 'Analyze', funkce pouze vrátí hodnotu true/false 
(podle toho, zda články s nesprávným počtem existují). Pokud bude p_processType = 'Fix', 
funkce hodnoty atributů article_author_count a article_institution_count opraví a vrátí true/false 
(v závislosti na tom, zda aktualizace proběhla v pořádku nebo nastala chyba).

Nachystejte také příkazy pro zrušení dvou nových atributů, ať můžete volání funkce opakovat.
*/

CREATE TABLE WORK_Z_ARTICLE AS
SELECT *
FROM Z_ARTICLE

ALTER TABLE WORK_Z_ARTICLE 
ADD (
    article_author_count INT,
    article_institution_count INT 
)

select *
from work_z_article


CREATE OR REPLACE FUNCTION StatisticsConsistent (
    p_processType VARCHAR2
)
RETURN BOOLEAN
IS
    e_wrong_param EXCEPTION;
    v_au_count INT;
    v_i_count INT;
    
    CURSOR c_articles IS
        SELECT ar.aid, COUNT(distinct aa.rid) as au_count, COUNT(distinct ai.iid) as i_count
        FROM z_article ar
        LEFT JOIN z_article_institution ai ON ai.aid = ar.aid
        LEFT JOIN z_article_author aa on aa.aid = ar.aid
        GROUP BY ar.aid;
BEGIN
    IF p_processType NOT IN ('Analyze', 'Fix') THEN
        RAISE e_wrong_param;
    END IF;
    
    FOR v_article IN c_articles LOOP
        SELECT article_author_count, article_institution_count INTO v_au_count, v_i_count
        FROM WORK_Z_ARTICLE 
        WHERE aid = v_article.aid;
    
        IF v_article.au_count != NVL(v_au_count, -1) OR v_article.i_count != NVL(v_i_count, -1) THEN
            IF p_processType = 'Analyze' THEN
                RETURN TRUE;
            END IF;
            
            UPDATE WORK_Z_ARTICLE
            SET article_author_count = v_article.au_count,
                article_institution_count = v_article.i_count
            WHERE aid = v_article.aid;
        END IF;
    END LOOP;
    
    IF p_processType = 'Analyze' THEN
        RETURN FALSE;
    END IF;
    
    COMMIT;
    RETURN TRUE;
EXCEPTION
    WHEN e_wrong_param THEN
        dbms_output.put_line('Wrong parameter');
        RETURN FALSE;
    WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);
        ROLLBACK;
        RETURN FALSE;
END;

DECLARE
    v_vysledek BOOLEAN;
BEGIN
    -- Test Analýzy
    v_vysledek := StatisticsConsistent('Analyze');
    
    IF v_vysledek THEN
        DBMS_OUTPUT.PUT_LINE('Výsledek Analyze: TRUE (Chyby existují)');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Výsledek Analyze: FALSE (Vše sedí)');
    END IF;
    
    -- Můžeš otestovat i Fix
     v_vysledek := StatisticsConsistent('Fix');
     IF v_vysledek THEN
        DBMS_OUTPUT.PUT_LINE('Výsledek Fix: TRUE (Chyby opraveny)');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Výsledek Fix: FALSE (Chyby se nepodarilo opravit)');
    END IF;
END;

/*
Napište uloženou proceduru ReportFord4Institution(p_institution_name), 
která na výstup vypíše seznam jmen oborů Ford časopisů, 
ve kterých publikovala články instituce s názvem p_institution_name v následujícím formátu:

Název instituce: <název instituce>, iid instituce: <iid instituce>:

    <obor 1>

    ...
    n. <obor n>
    Celkový počet oborů Ford článků instituce: x

Vypište jen obory ze stejného roku, v jakém byl vydán článek, nevypisujte duplicitní obory. Pokud instituce neexistuje, procedura vypíše: Instituce neexistuje. 
Pokud je institucí více, vypíše procedura: Nejednoznačný název instituce.

Otestujte pro dvě instituce existující a jednu instituci neexistující. Řešte použitím nejvýše 3 databázových operací.

Příklad výstupu:
--------------------------------------------------
Název instituce: Sanofi s.r.o., id instituce: 262: 
1. 1.3 Physical sciences
2. 1.6 Biological sciences
3. 1.7 Other natural sciences
4. 2.3 Mechanical engineering
5. 2.7 Environmental engineering
6. 2.11 Other engineering and technologies
7. 2.4 Chemical engineering
8. 1.4 Chemical sciences
9. 2.5 Materials engineering
Celkový počet oborů Ford článků instituce: 9
*/

CREATE OR REPLACE PROCEDURE ReportFord4Institution (
    p_institution_name Z_INSTITUTION.name%TYPE
)
IS
    CURSOR c_ford_names IS
        SELECT DISTINCT ff.name
        FROM Z_INSTITUTION i
        JOIN Z_ARTICLE_INSTITUTION ai on ai.iid = i.iid
        JOIN Z_ARTICLE ar on ar.aid = ai.aid
        JOIN Z_YEAR_FIELD_JOURNAL yfj on yfj.jid = ar.jid and yfj.year = ar.year
        JOIN Z_FIELD_FORD ff on ff.fid = yfj.fid
        WHERE i.name = p_institution_name;
    
    v_ford_num INT := 1;
    v_i_iid Z_INSTITUTION.iid%TYPE;
BEGIN
    SELECT i.iid INTO v_i_iid
    FROM Z_INSTITUTION i
    WHERE i.name = p_institution_name;
    
    dbms_output.put_line('Název instituce: ' || p_institution_name || ', id instituce: ' || v_i_iid || ':');

    FOR ford_name IN c_ford_names
    LOOP
        dbms_output.put_line(v_ford_num || '. ' || ford_name.name);
        v_ford_num := v_ford_num + 1;
    END LOOP;   
    dbms_output.put_line('Celkový počet oborů Ford článků instituce: ' || v_ford_num -1);
EXCEPTION
    WHEN TOO_MANY_ROWS THEN
        dbms_output.put_line('Nejednoznačný název instituce');
    WHEN NO_DATA_FOUND THEN
        dbms_output.put_line('Instituce neexistuje');
    WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);
END;


EXECUTE ReportFord4Institution('Univerzita Hradec Králové');
EXECUTE ReportFord4Institution('Sanofi s.r.o.');

/*
Napište uloženou proceduru P_InsertArticle. Procedura bude jako parametry přijímat ISSN časopisu, jméno instituce, jméno autora, rok a název článku.
Procedura dle parametrů nejprve identifikuje časopis, instituci a autora. Pokud identifikace selže, tj. záznam nelze dohledat nebo je identifikace nejednoznačná, 
vypíše se chybová hláška s uvedením konkrétního problému (např. "Časopis s daným ISSN neexistuje.") a procedura se ukončí.
Jinak dojde k vložení článku a jeho přiřazení k časopisu, instituci a autorovi. Jako ID nového článku použijte nejvyšší existující ID inkrementované o 1.
*/

EXECUTE CREATE_WORK_TABLE('Z_JOURNAL');
EXECUTE CREATE_WORK_TABLE('Z_INSTITUTION');
EXECUTE CREATE_WORK_TABLE('Z_AUTHOR');
EXECUTE CREATE_WORK_TABLE('Z_ARTICLE');
EXECUTE CREATE_WORK_TABLE('Z_ARTICLE_AUTHOR');
EXECUTE CREATE_WORK_TABLE('Z_ARTICLE_INSTITUTION');

SET SERVEROUTPUT ON;
/
CREATE OR REPLACE PROCEDURE P_InsertArticle (
    p_issn VARCHAR2,
    p_i_name Z_INSTITUTION.name%TYPE,
    p_au_name Z_AUTHOR.name%TYPE,
    p_year Z_ARTICLE.year%TYPE,
    p_ar_name Z_ARTICLE.name%TYPE
)
IS
    v_jid Z_JOURNAL.jid%TYPE;
    v_iid Z_INSTITUTION.iid%TYPE;
    v_rid Z_AUTHOR.rid%TYPE;
    
    v_new_aid Z_ARTICLE.aid%TYPE;
BEGIN
    BEGIN
        SELECT jid INTO v_jid
        FROM WORK_Z_JOURNAL 
        WHERE issn = p_issn;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            dbms_output.put_line('Casopis s danym ISSN neexistuje.');
            RETURN;
        WHEN TOO_MANY_ROWS THEN
            dbms_output.put_line('Prilis mnoho Casopisu s danym ISSN.');
            RETURN;
    END;

    BEGIN
        SELECT iid INTO v_iid
        FROM WORK_Z_INSTITUTION
        WHERE name = p_i_name;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            dbms_output.put_line('Instituce s danym jmenem neexistuje.');
            RETURN;
        WHEN TOO_MANY_ROWS THEN
            dbms_output.put_line('Prilis mnoho Instituci s danym jmenem.');
            RETURN;
    END;
    
    BEGIN
        SELECT rid INTO v_rid
        FROM WORK_Z_AUTHOR
        WHERE name = p_au_name;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            dbms_output.put_line('Autor s danym jmenem neexistuje.');
            RETURN;
        WHEN TOO_MANY_ROWS THEN
            dbms_output.put_line('Prilis mnoho Autoru s danym jmenem.');
            RETURN;
    END;
    
    SELECT NVL(MAX(aid), 0)+1 INTO v_new_aid
    FROM WORK_Z_ARTICLE;
    
    
    INSERT INTO WORK_Z_ARTICLE (aid, jid, name, year)
    VALUES (v_new_aid, v_jid, p_ar_name, p_year);
    
    INSERT INTO WORK_Z_ARTICLE_AUTHOR (aid, rid)
    VALUES (v_new_aid, v_rid);
    
    INSERT INTO WORK_Z_ARTICLE_INSTITUTION (aid, iid)
    VALUES (v_new_aid, v_iid);
    
    COMMIT;
    dbms_output.put_line('Article inserted successfully with id: ' || v_new_aid);
EXCEPTION
    WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);
        ROLLBACK;
END;
 /     
EXECUTE P_InsertArticle('0944-2669', 'Vojenský veterinární ústav Hlučín', 'Liu, Zhisu', 2020, 'TEST10');

SELECT issn
from z_journal

select name
from z_institution
      
select name
from z_author

select *
from z_article where name like 'TEST%'

/*
Napište proceduru ScienceReport, která bude mít jako vstup obor sid, následně vytvoří seznam reportů pro jednotlivé roky REPORT<year> (například: REPORT2025). 
Report bude obsahovat informace o podskupinách ford, počtu článků a unikátních časopisů pro daný rok a zadaní sid. (příklad výstupu: year, fid, article_count, journal_count).

Na výstup bude procedura vypisovat názvy jednotlivých reportů (vytvořených tabulek) a Například pro obor s číslem 6.

REPORT2019
REPORT2020
REPORT2021
REPORT2022
REPORT2023

Pokud již tabulka s daným rokem existuje (viz tabulka user_tables), původní tabulka se vymaže a pokračuje se dále v generování. 
Tabulky, které by byly prázdné (v daném roce by neobsahovaly žádný záznam) se nevytvářejí.

V řešení nepoužívejte SQL defragmentaci pomocí kurzoru, pro naplnění tabulky použijte operaci insert – select nebo create – select.
*/


    SELECT ar.year, ff.fid, COUNT(ar.aid) as ar_count, COUNT(DISTINCT yfj.jid) as j_count 
    FROM Z_FIELD_OF_SCIENCE fs
    JOIN Z_FIELD_FORD ff ON ff.sid = fs.sid
    JOIN Z_YEAR_FIELD_JOURNAL yfj on yfj.fid = ff.fid
    JOIN Z_ARTICLE ar on ar.jid = yfj.jid and ar.year = yfj.year
    where fs.sid = 6
    GROUP BY ar.year, ff.fid
    ORDER BY YEAR;

CREATE OR REPLACE PROCEDURE ScienceReport (
    p_sid Z_FIELD_OF_SCIENCE.sid%TYPE
)
IS
    CURSOR c_years IS        
        SELECT DISTINCT ar.year
        FROM Z_FIELD_OF_SCIENCE fs
        JOIN Z_FIELD_FORD ff ON ff.sid = fs.sid
        JOIN Z_YEAR_FIELD_JOURNAL yfj on yfj.fid = ff.fid
        JOIN Z_ARTICLE ar on ar.jid = yfj.jid and ar.year = yfj.year
        where fs.sid = p_sid
        ORDER BY YEAR;
        
    v_table_name VARCHAR2(50);
    v_table_exists INT;
BEGIN
    FOR c_year IN c_years LOOP
        v_table_name := 'REPORT' || TO_CHAR(c_year.year);
        
        SELECT COUNT(*) INTO v_table_exists
        FROM USER_TABLES
        WHERE table_name = v_table_name;
        
        IF v_table_exists > 0 THEN
            EXECUTE IMMEDIATE 'DROP TABLE ' || v_table_name;
        END IF;
    
        EXECUTE IMMEDIATE ('
        CREATE TABLE ' || v_table_name || ' AS
            SELECT ar.year, ff.fid, COUNT(ar.aid) as ar_count, COUNT(DISTINCT yfj.jid) as j_count
            FROM Z_FIELD_OF_SCIENCE fs
            JOIN Z_FIELD_FORD ff ON ff.sid = fs.sid
            JOIN Z_YEAR_FIELD_JOURNAL yfj on yfj.fid = ff.fid
            JOIN Z_ARTICLE ar on ar.jid = yfj.jid and ar.year = yfj.year
            where fs.sid = ' || p_sid || ' and ar.year = ' || c_year.year || ' 
            GROUP BY ar.year, ff.fid
        ');
        
        dbms_output.put_line(v_table_name);
    END LOOP;

EXCEPTION
    WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);
END;

EXECUTE ScienceReport(6);


select *
FROM REPORT2019

/*
Vytvořte tabulku science_count, která bude obsahovat sid, year, article_count, a stamp = časové razítko poslední aktualizace, které jsou pro tento obor uvedeny. 
Napište trigger t_science_count, který při přidání nového sid do tohoto seznamu, spočítá počet publikací s tímto sid, year a tento počet aktualizuji.
Pokud kombinace sid a year pro přidání neexistuje nebo nemá žádný záznam, vyvolejte vlastní výjimku "No article for combinantion SID, YEAR" 
a vykonávání dotazu se zastaví s chybou číslo -20001, ID not exists.

Napište druhy trigger, který při změně v tomto seznamu provede přepočet stavu publikací daného fid. Demonstraci proveďte nad jedním sid a rokem a nad všemi sid v seznamu.
*/

CREATE TABLE SCIENCE_COUNT AS
SELECT fs.sid, ar.year, COUNT(ar.aid) as ar_count, SYSDATE as stamp
FROM Z_FIELD_OF_SCIENCE fs
JOIN Z_FIELD_FORD ff on ff.sid = fs.sid
JOIN Z_YEAR_FIELD_JOURNAL yfj on yfj.fid = ff.fid
JOIN Z_ARTICLE ar on ar.jid = yfj.jid and ar.year = yfj.year
GROUP BY fs.sid, ar.year, SYSDATE

INSERT INTO SCIENCE_COUNT (sid, year) VALUES (6, 2020);

SELECT *
FROM SCIENCE_COUNT

create or replace TRIGGER t_science_count 
BEFORE INSERT OR UPDATE
ON science_count
FOR EACH ROW
DECLARE
    v_ar_count INT;
    e_zadny_zaznam EXCEPTION;
BEGIN
    SELECT COUNT(DISTINCT ar.aid) INTO v_ar_count
    FROM Z_FIELD_OF_SCIENCE fs
    JOIN Z_FIELD_FORD ff on ff.sid = fs.sid
    JOIN Z_YEAR_FIELD_JOURNAL yfj on yfj.fid = ff.fid
    JOIN Z_ARTICLE ar on ar.jid = yfj.jid and ar.year = yfj.year
    WHERE fs.sid = :new.sid and ar.year = :new.year;

    IF v_ar_count = 0 THEN
        RAISE e_zadny_zaznam;
    END IF;

    :new.ar_count := v_ar_count;
    :new.stamp := SYSDATE;

    dbms_output.put_line('ar_count updated to: ' || v_ar_count || ' on: ' || SYSDATE);

EXCEPTION
    WHEN e_zadny_zaznam THEN
        dbms_output.put_line('No article for combinantion SID, YEAR');
        RAISE_APPLICATION_ERROR(-20001, 'ID not exists');

    WHEN OTHERS THEN
        dbms_output.put_line('An error has occured: ' || sqlerrm);
END;

UPDATE science_count 
SET sid = 1 
WHERE sid = 1 AND year = 2020;

-- 2. Demonstrace nad VŠEMI záznamy v tabulce najednou
-- Tento příkaz projde celou tabulku řádek po řádku, pro každý odpálí náš trigger a zaktualizuje počty i čas.
UPDATE science_count 
SET stamp = SYSDATE;

