/*
Naleznete autory clanku, jejich zinsitiuce jsou z Prahy (atribut z_institution.town='PRAHA'),
kteri maji o 60 takovych clanku v casopisech hodnocenych Decil (atribut z_year_field_journal.ranking='DECIL')
vic, nez je prumerny pocet takovych clanku vsech takovych autoru.

Vypiste rid, jmeno autora a pocet takovych clanku, vysledek bude setrizeny podle poctu clanku sestupne

*/

with tab as (
	select au.rid, au.name, count(distinct ar.aid) as ar_count
	from z_author au
	join z_article_author aa on aa.rid = au.rid
	join z_article ar on ar.aid = aa.aid
	join z_article_institution ai on ai.aid = ar.aid
	join z_institution i on i.iid = ai.iid
	join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
	where i.town='PRAHA' and yfj.ranking = 'DECIL'
	group by au.rid, au.name
)
-- v1
/*
select *
from tab t1
where t1.ar_count >=  all (
	select 60 + avg(t2.ar_count)
	from tab t2
) 
order by ar_count desc
*/

-- v2
select *
from tab t1
where t1.ar_count > (
	select avg(t2.ar_count)
	from tab t2
)+60
order by ar_count desc

/*
Naleznete instituce z Brna, ktere publikuji clanky v oborech FORD (atribut z_field_ford.name)
vedni oblasti 'Engineering and Technology' (atribut z_field_of_science.name) v letech 2019 - 2021.
Vysledek obsahuje insituce, ktere publikuji:
    - clanky pouze v jednm oboru FORD teto vedni oblasti,
    - alespon 10 clanku
Vypiste iid a nazev instituce, pocet clanku s omezenim i pocet clanku celkem
*/

with tab as (
	select i.iid, 
		i.name, 
		(
			select count(distinct ar.aid)
			from z_article ar
			join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
			join z_field_ford ff on ff.fid = yfj.fid
			join z_field_of_science fs on fs.sid = ff.sid
			join z_article_institution ai on ai.aid = ar.aid
			where fs.name = 'Engineering and Technology' 
			and yfj.year between 2019 and 2021 
			and ai.iid = i.iid
			having count(distinct yfj.fid) = 1
		) as ford_count,
		(
			select count(distinct ai.aid)
			from z_article_institution ai
			where ai.iid = i.iid
			having count(distinct ai.aid) >= 10
		) as ar_count
	from z_institution i
	where i.town = 'BRNO'
	group by i.iid, i.name
)

select *
from tab
where ford_count is not null 
	and ar_count is not null

/*
Naleznete osobu (nebo osoby) s nejvyssim poctem clanku v casopisech hodnocenych v decilu 
(z_year_field_journal.ranking='decil'), kde insituce clanku sidli v Hradci Kralove 
(z_institution.town='Hradec Králové'). Vypiste rid a jmeno osoby i pocet clanku v decilu
*/

with tab as (
	select au.rid, au.name, count(distinct ar.aid) as ar_count
	from z_author au
	join z_article_author aa on aa.rid = au.rid
	join z_article ar on ar.aid = aa.aid
	join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
	join z_article_institution ai on ai.aid = ar.aid
	join z_institution i on i.iid = ai.iid
	where yfj.ranking = 'DECIL' and i.town = 'Hradec Králové'
	group by au.rid, au.name
)

select *
from tab t1
where t1.ar_count >= all (
	select t2.ar_count
	from tab t2
)

/*
Pro kazdy obor FORD z vedniho oboru 'Engineering and Technology' vypiste casopisy,
ktere mely v roce 2020 nejvice publikovanych clanku.

Vypiste jmeno oboru, jmeno casopisu, a pocet clanku. 
Vysledek setridte podle nazvy oboru a jmena casopisu
*/

with tab as (
	select ff.name as ff_name, j.name as j_name, count(distinct ar.aid) as ar_count
	from z_field_ford ff
	join z_field_of_science fs on fs.sid = ff.sid
	join z_year_field_journal yfj on yfj.fid = ff.fid
	join z_journal j on j.jid = yfj.jid
	join z_article ar on ar.jid = yfj.jid and ar.year = yfj.year
	where fs.name = 'Engineering and Technology'
	group by ff.name, j.name
)

select *
from tab t1
where t1.ar_count >= all (
	select ar_count
	from tab t2
	where t1.ff_name = t2.ff_name
)
order by ff_name, j_name

/*
Pro kazdy obor FORD z vedniho oboru 'Engineering and Technology' vypiste:
	- Pocet casopisu, ktere jsou v roce 2020 zarazeny do daneho oboru, ale nemaji v tomto roce zadny clanek.
	- Pocet casopisu, ktere jsou v roce 2020 zarazeny do daneho oboru a maji v tomto roce alespon 1 clanek.
*/

select
	ff.fid, 
	ff.name,
	(
		select count(distinct yfj.jid)
		from z_year_field_journal yfj
		where yfj.year = 2020 
		and yfj.fid = ff.fid
		and not exists (
			select 1
			from z_article ar 
			where ar.jid = yfj.jid and ar.year = yfj.year
		)
	) as no_articles_count,
	(
		select count(distinct yfj.jid)
		from z_year_field_journal yfj
		where yfj.year = 2020 
		and yfj.fid = ff.fid
		and exists (
			select 1
			from z_article ar 
			where ar.jid = yfj.jid and ar.year = yfj.year
		)
	) as has_articles_count
from z_field_ford ff
join z_field_of_science fs on fs.sid = ff.sid
where fs.name = 'Engineering and Technology'

/*
Vypiste casopisy, ktere byly v roce 2020 v 'Q1' rankingu v oboru '5.5 Law' a zaroven v roce 2021
v rankingu 'Q2' v oboru '5.5 Law'.

Vysledek setridte podle nazvu casopisu.
*/

select *
from z_journal j
where exists (
	select 1
	from z_year_field_journal yfj 
	join z_field_ford ff on ff.fid = yfj.fid
	where yfj.jid = j.jid 
		and ff.name = '5.5 Law' 
		and yfj.year = 2020
		and yfj.ranking = 'Q1'
) 
and exists (
	select 1
	from z_year_field_journal yfj 
	join z_field_ford ff on ff.fid = yfj.fid
	where yfj.jid = j.jid 
		and ff.name = '5.5 Law' 
		and yfj.year = 2021
		and yfj.ranking = 'Q2'
)
order by j.name

/*
Naleznete casopisy z oboru obsahujici slova 'Materials engineering',
ktere publikovaly od autoru z vice nez 15 instituci v letech 2020-2021.

Pro kazdy casopis vypiste jeho nazev a prumerny pocet autoru na clanek a pocet instituci.
Vysledek setridte podle nazvu casopisu.

Nezapomente na datovou konverzi poctu pred prumerovanim na datovy typ s desetinnou casti.
*/

WITH autori as (
	SELECT aa.aid, COUNT(DISTINCT aa.rid) AS au_count
    FROM z_article_author aa
    GROUP BY aa.aid
)
SELECT 
    j.name,
    COUNT(DISTINCT ai.iid) AS i_count,
    AVG(CAST(autori.au_count AS FLOAT)) AS avg_author_count
FROM z_journal j
JOIN z_year_field_journal yfj ON yfj.jid = j.jid
JOIN z_field_ford ff ON ff.fid = yfj.fid
JOIN z_article ar ON ar.jid = j.jid and ar.year = yfj.year
JOIN z_article_institution ai ON ai.aid = ar.aid
JOIN autori ON autori.aid = ar.aid
WHERE ff.name LIKE '%Materials engineering%' 
	AND yfj.year BETWEEN 2020 AND 2021
GROUP BY j.name
HAVING COUNT(DISTINCT ai.iid) > 15
ORDER BY j.name

/*
Naleznete autory, kteri publikovali v roce 2019 v institucich z Brna 
a ZAROVEN publikovali v Q1 casopisech v roce 2020.

Vypiste jmena autory a setridte je vzestupne
*/

select au.name
from z_author au
where exists (
	select 1
	from z_article_author aa
	join z_article ar on ar.aid = aa.aid
	join z_article_institution ai on ai.aid = ar.aid
	join z_institution i on i.iid = ai.iid
	where au.rid = aa.rid 
		and ar.year = 2019 
		and i.town = 'BRNO'
) and exists (
	select 1
	from z_article_author aa
	join z_article ar on ar.aid = aa.aid
	join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
	where au.rid = aa.rid 
		and ar.year = 2020 
		and yfj.ranking = 'Q1'
)

/*
Naleznete casopisy z oboru obsahujici '1.2 Computer and Information Sciences', 
ktere publikovaly clanky autoru z vice nez 8 ruznych insituci v letech 2019 - 2021.

Vypiste nazev casopisu, pocet instituci, pocet clanku. Vysledek setridte podle poctu instituci sestupne.
*/

select j.name, count(distinct ar.aid) as ar_count, count(distinct ai.iid) as i_count
from z_year_field_journal yfj
join z_field_ford ff on ff.fid = yfj.fid
join z_journal j on j.jid = yfj.jid
join z_article ar on ar.jid = yfj.jid and ar.year = yfj.year
join z_article_institution ai on ai.aid = ar.aid
where ff.name like '%1.2 Computer and Information Sciences%' and yfj.year between 2019 and 2021
group by j.name
having count(distinct ai.iid) > 8
order by i_count desc

/*
Naleznete autory, kteri maji alespon 30 publikaci v 'DECIL' rankingu a vsechny sve publikace maji pouze v jedne instituci.

Vypiste jmeno autora, nazev instituce, a pocet clanku v 'DECIL' rankingu.
*/

select au.name, i.name, count(distinct ar.aid) ar_count
from z_author au
join z_article_author aa on aa.rid = au.rid
join z_article ar on ar.aid = aa.aid
join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
join z_article_institution ai1 on ai1.aid = ar.aid
join z_institution i on i.iid = ai1.iid
where yfj.ranking = 'DECIL' and not exists (
	select 1
	from z_article_author aa2
	join z_article_institution ai2 on ai2.aid = aa2.aid
	where aa2.rid = aa.rid and ai1.iid != ai2.iid
)
group by au.name, i.name
having count(distinct ar.aid) >= 30

/*
Naleznete instituce, ktere na zadnem clanku nemaji vice nez 5 autoru,
ale maji alespon 10 clanku.

Vypiste nazev instituce setrideny vzestupne
*/

with author_counts as (
	select count(distinct aa.rid) as au_count, aid
	from z_article_author aa 
	group by aid
), i_ar_counts as (
	select ai.iid,
		count(distinct ai.aid) as ar_count,
		MAX(ac.au_count) as max_au_count
	from z_article_institution ai
	join author_counts ac on ac.aid = ai.aid
	group by ai.iid
)

select i.name
from i_ar_counts iac
join z_institution i on i.iid = iac.iid
where iac.ar_count >= 10
	and iac.max_au_count <= 5
order by i.name

/*
Pro kazdy ranking vypiste nasledujici statistiky:
	- Pocet unikatnich autoru, kteri publikovali v casopisech s timto rankingem,
	- Pocet publikaci v casopisech s timto rankingem,
	- Pocet instituci, kteer maji v tomto rankingu publikaci v roce 2020

Vypiste hodnotu rankingu, jednotlive statistiky a vysledek setridte podle poctu unikatnich autoru sestupne.
*/

-- v1
with j_au_count as (
	select count(distinct aa.rid) as au_count, yfj.ranking
	from z_year_field_journal yfj
	join z_article ar on ar.jid = yfj.jid and ar.year = yfj.year
	join z_article_author aa on aa.aid = ar.aid
	group by yfj.ranking
), j_ar_count as (
	select count(distinct ar.aid) as ar_count, yfj.ranking
	from z_year_field_journal yfj
	join z_article ar on ar.jid = yfj.jid and ar.year = yfj.year
	group by yfj.ranking
), j_i_count as (
	select count(distinct ai.iid) as i_count, yfj.ranking
	from z_year_field_journal yfj
	join z_article ar on ar.jid = yfj.jid and ar.year = yfj.year
	join z_article_institution ai on ai.aid = ar.aid
	where yfj.year = 2020
	group by yfj.ranking
	having count(distinct ar.aid) > 0
)

select distinct yfj.ranking, coalesce(au_count, 0) as authors, coalesce(ar_count, 0) as articles, coalesce(i_count, 0) as institutions
from z_year_field_journal yfj
left join j_au_count jauc on jauc.ranking = yfj.ranking
left join j_ar_count jarc on jarc.ranking = yfj.ranking
left join j_i_count jic on jic.ranking = yfj.ranking
order by authors desc;


-- v2
WITH rankings AS (
  SELECT DISTINCT ranking
  FROM z_year_field_journal
)
SELECT
  r.ranking,
  COUNT(DISTINCT aa.rid)            AS pocet_autoru,
  COUNT(DISTINCT a.aid)             AS pocet_clanku,
  COUNT(DISTINCT CASE WHEN a.year = 2020 THEN ai.iid END) AS pocet_instituci_2020
FROM rankings r
LEFT JOIN z_year_field_journal yfj
  ON yfj.ranking = r.ranking
LEFT JOIN z_article a
  ON a.jid = yfj.jid AND a.year = yfj.year
LEFT JOIN z_article_author aa
  ON aa.aid = a.aid
LEFT JOIN z_article_institution ai
  ON ai.aid = a.aid
GROUP BY r.ranking
ORDER BY pocet_autoru DESC;


/*
Pro kazdy obor FORD ve vedni oblasti 'Natural sciences' naleznete prumerny pocet autoru na clanek v casopisech s rankingem Q1,
ktere publikovala instituce 'Nemocnice na Homolce'
*/

with ford_authors as (
	select count(distinct aa.rid) as au_count, yfj.fid
	from z_year_field_journal yfj
	join z_article ar on ar.jid = yfj.jid and ar.year = yfj.year
	join z_article_author aa on aa.aid = ar.aid
	join z_article_institution ai on ai.aid = ar.aid
	join z_institution i on i.iid = ai.iid
	where yfj.ranking = 'Q1' and i.name = 'Nemocnice na Homolce'
	group by ar.aid, yfj.fid
)

select ff.name, avg(cast(fa.au_count as float)) as avg_au_count
from z_field_ford ff
join z_field_of_science fs on fs.sid = ff.sid
left join ford_authors fa on fa.fid = ff.fid
where fs.name = 'Natural sciences'
group by ff.name;

WITH article_author_count AS (
    SELECT 
        aa.aid,
        COUNT(DISTINCT aa.rid) AS pocet_autoru
    FROM z_article_author aa
    GROUP BY aa.aid
)
SELECT 
    ff.name AS obor_ford,
    AVG(CASE WHEN i.name = 'Nemocnice na Homolce' THEN CAST(aac.pocet_autoru AS FLOAT) END) 
        AS prumerny_pocet_autoru
FROM z_field_ford ff
JOIN z_field_of_science fs
    ON fs.sid = ff.sid
LEFT JOIN z_year_field_journal yfj 
    ON yfj.fid = ff.fid AND yfj.ranking = 'Q1'
LEFT JOIN z_article a 
    ON a.jid = yfj.jid AND a.year = yfj.year
LEFT JOIN z_article_institution ai 
    ON ai.aid = a.aid
LEFT JOIN z_institution i 
    ON i.iid = ai.iid
LEFT JOIN article_author_count aac 
    ON aac.aid = a.aid
WHERE fs.name = 'Natural sciences'
GROUP BY ff.name
ORDER BY ff.name;

/*
Naleznete instituce z Ostravy nebo Olomouce (tzn. atribut town obsahuje retezec 'OSTRAVA' nebo 'OLOMOUC'),
ktere maji alespon jeden clanek v prvnim decilu ve vsech letech: 2018, 2019, a 2020.

Vypiste nazev instituce.
*/
select i.name
from z_institution i
where (i.town like '%OSTRAVA%' or i.town like '%OLOMOUC%') and exists (
	select ar.aid
	from z_article ar
	join z_article_institution ai on ai.aid = ar.aid
	join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
	where ai.iid = i.iid 
		and yfj.year = 2018
		and yfj.ranking = 'DECIL'
	group by ar.aid
) and exists (
	select ar.aid
	from z_article ar
	join z_article_institution ai on ai.aid = ar.aid
	join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
	where ai.iid = i.iid 
		and yfj.year = 2019
		and yfj.ranking = 'DECIL'
	group by ar.aid
) and exists (
	select ar.aid
	from z_article ar
	join z_article_institution ai on ai.aid = ar.aid
	join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
	where ai.iid = i.iid 
		and yfj.year = 2020
		and yfj.ranking = 'DECIL'
	group by ar.aid
)

select i.name
from z_institution i
join z_article_institution ai on ai.iid = i.iid
join z_article ar on ar.aid = ai.aid
join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
where (i.town like '%OSTRAVA%' or i.town like '%OLOMOUC%')
and yfj.year between 2018 and 2020 
and yfj.ranking = 'DECIL'
group by i.name
having count(distinct ar.aid) > 0 and count(distinct ar.year) = 3

/*
Naleznete autory, kteri publikovali alespon ve dvou ruznych institucich, ktere maji v nazvu 'agro'
*/
select au.rid, au.name
from z_author au
join z_article_author aa on aa.rid = au.rid
join z_article_institution ai on ai.aid = aa.aid
join z_institution i on i.iid = ai.iid
where i.name like '%agro%'
group by au.rid, au.name
having count(distinct i.iid) >= 2

