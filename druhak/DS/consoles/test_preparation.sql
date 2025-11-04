/*
Naleznete autory clanku, jejichz instituce jsou z Prahy (atribut z_institution.town), 
kteri maji o 60 takovych clanku v casopisech hodnocenych Decil (z_year_field_journal.ranking='Decil') vic, 
nez je prumerny pocet takovych clanku vsech takovych autoru. 
Vypiste rid a jmeno autora a pocet takovych clanku, vysledek bude setrizeny podle poctu clanku sestupne
*/

with tab as (
select 
    au.rid, 
    au.name, 
    count(distinct ar.aid) as ar_count
from z_author au
join z_article_author aa on aa.rid = au.rid
join z_article ar on ar.aid = aa.aid
join z_article_institution ai on ai.aid = ar.aid
join z_institution i on i.iid = ai.iid
join z_year_field_journal yfj 
    on yfj.jid = ar.jid 
    and yfj.year = ar.year
where i.town like 'praha%' 
    and ranking = 'decil'
group by au.rid, au.name
)

select *
from tab as t1
where t1.ar_count > (
	select avg(t2.ar_count)
	from tab t2
) + 60
order by ar_count desc

with tab as (
SELECT 
    a.rid,
    a.name,
    COUNT(DISTINCT ar.aid) AS pocet_clanku
FROM z_author a
JOIN z_article_author ar ON a.rid = ar.rid
JOIN z_article art ON ar.aid = art.aid
JOIN z_article_institution ai ON art.aid = ai.aid
JOIN z_institution i ON ai.iid = i.iid
JOIN z_journal j ON art.jid = j.jid
JOIN z_year_field_journal yfj 
    ON yfj.jid = j.jid AND yfj.year = art.year
WHERE i.town = 'Praha'
  AND yfj.ranking = 'Decil'
GROUP BY a.rid, a.name
)

select *
from tab t1
where t1.pocet_clanku > (
    SELECT AVG(t2.pocet_clanku)
    FROM tab t2
) + 60
ORDER BY pocet_clanku DESC;


/*
Naleznete instituce z Brna, ktere publikuji clanky v oborech FORD (atribut z_field_ford.name)
vedni oblasti 'Engineering and Technology' (atribut z_field_of_science.name) v letech 2019 - 2021.
Vysledek obsahuje insituce, ktere publikuji:
    - clanky pouze v jednm oboru FORD teto vedni oblasti,
    - alespon 10 clanku
Vypiste iid a nazev instituce, pocet clanku s omezenim i pocet clanku celkem
*/

with tab as (
select i.iid, i.name, (
    select count(distinct ar.aid)
    from z_article ar
    join z_article_institution ai on ai.aid = ar.aid
    join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
    join z_field_ford ff on ff.fid = yfj.fid
    join z_field_of_science fs on fs.sid = ff.sid
    where ai.iid = i.iid and fs.name = 'Engineering and Technology'
    having count(distinct ff.fid) = 1
) as ford_count, (
    select count(distinct ar.aid)
    from z_article ar
    join z_article_institution ai on ai.aid = ar.aid
    where ai.iid = i.iid
    having count(distinct ar.aid) > 10
) as general_count
from z_institution i
where i.town = 'brno'
) 

select *
from tab
where ford_count is not null and general_count is not null

/*
Naleznete osobu (nebo osoby) s nejvyssim poctem clanku v casopisech hodnocenych v decilu 
(z_year_field_journal.ranking='decil'), kde insituce clanku sidli v Hradci Kralove 
(z_institution.town='Hradec Kralove'). Vypiste rid a jmeno osoby i pocet clanku v decilu
*/
with tab as (
select au.rid, au.name, count(distinct ar.aid) as ar_count
from z_author au
join z_article_author aa on aa.rid = au.rid
join z_article ar on ar.aid = aa.aid
join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
join z_article_institution ai on ai.aid = ar.aid
join z_institution i on i.iid = ai.iid
where yfj.ranking = 'decil' and i.town = 'Hradec Králové'
group by au.rid, au.name
)

select *
from tab t1
where t1.ar_count >= all (
    select ar_count
    from tab t2
)
order by ar_count desc

/*
Naleznete autory clanku, jejichz instituce jsou z Ostravy (atribut z_institution.town obsahuje slovo Ostrava), 
kteri maji o 15 takovych clanku v casopisech hodnocenych Q1 (z_year_field_journal.ranking='Q1') vic, 
nez je prumerny pocet takovych clanku vsech takovych autoru. 
Vypiste rid a jmeno autora a pocet takovych clanku, vysledek bude setrizeny podle poctu clanku sestupne
*/

with tab as (
select 
    au.rid, 
    au.name, 
    count(distinct ar.aid) as ar_count
from z_author au
join z_article_author aa on aa.rid = au.rid
join z_article ar on ar.aid = aa.aid
join z_article_institution ai on ai.aid = ar.aid
join z_institution i on i.iid = ai.iid
join z_year_field_journal yfj 
    on yfj.jid = ar.jid 
    and yfj.year = ar.year
where i.town like '%ostrava%' 
    and ranking = 'q1'
group by au.rid, au.name
)

select *
from tab as t1
where t1.ar_count > (
	select avg(t2.ar_count)
	from tab t2
) + 15
order by ar_count desc