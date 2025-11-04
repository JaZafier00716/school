-- 1.1
with tab as (
select au.rid, au.name, count(distinct ar.aid) as ar_count
from z_author au
join z_article_author aa on aa.rid = au.rid
join z_article ar on ar.aid = aa.aid
join z_article_institution ai on ai.aid = ar.aid
join z_institution i on i.iid = ai.iid
join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
where i.town = 'PRAHA' and yfj.ranking = 'DECIL'
group by au.rid, au.name
)

select *
from tab t1
where t1.ar_count >= (
	select avg(t2.ar_count)
	from tab t2
) + 60
order by t1.ar_count desc

-- 1.2
with tab as (
	select i.iid, i.name, (
		select count(distinct ar.aid)
		from z_article_institution ai
		join z_article ar on ar.aid = ai.aid
		join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
		join z_field_ford ff on ff.fid = yfj.fid
		join z_field_of_science fs on fs.sid = ff.sid
		where i.iid = ai.iid and fs.name = 'Engineering and Technology' and yfj.year between 2019 and 2021
		having count(distinct ff.fid) = 1
	) as ford_count,
	(
		select count(distinct ai.aid)
		from z_article_institution ai
		where i.iid = ai.iid
		having count(distinct ai.aid) >= 10
	) as overall_count
	from z_institution i
	where i.town = 'BRNO'
)

select *
from tab
where ford_count is not null and overall_count is not null

-- 1.3
with tab as (
select au.rid, au.name, count(distinct ar.aid) as ar_count
from z_author au
join z_article_author aa on aa.rid = au.rid
join z_article ar on ar.aid = aa.aid
join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
join z_article_institution ai on ai.aid = ar.aid
join z_institution i on i.iid = ai.iid
where yfj.ranking = 'DECIL' and i.town = 'Hradec Kr�lov�'
group by au.rid, au.name
)

select *
from tab t1
where t1.ar_count >= all (
	select t2.ar_count
	from tab t2
)

-- 2.1
with tab as (
select ff.name as f_name, j.name as j_name, count(distinct ar.aid) as ar_count
from z_field_ford ff
join z_field_of_science fs on fs.sid = ff.sid
join z_year_field_journal yfj on yfj.fid = ff.fid
join z_journal j on j.jid = yfj.jid
join z_article ar on ar.jid = yfj.jid and ar.year = yfj.year
where fs.name = 'Engineering and Technology' and yfj.year = 2020
group by ff.name, j.name
)

select *
from tab t1
where t1.ar_count >= all (
	select t2.ar_count
	from tab t2
	where t1.f_name = t2.f_name
)
order by f_name, j_name

-- 2.2
select ff.fid, ff.name, (
	select count(distinct yfj.jid)
	from  z_year_field_journal yfj
	where yfj.year = 2020 and yfj.fid = ff.fid and not exists (
		select 1
		from z_article ar
		where ar.year = yfj.year and ar.jid = yfj.jid
	)
) as no_articles,
(
	select count(distinct yfj.jid)
	from  z_year_field_journal yfj
	where yfj.year = 2020 and yfj.fid = ff.fid and exists (
		select 1
		from z_article ar
		where ar.year = yfj.year and ar.jid = yfj.jid
	)
) as has_articles
from z_field_ford ff
join z_field_of_science fs on fs.sid = ff.sid
where fs.name = 'Engineering and Technology'

-- 2.3
select j.*
from z_journal j where exists (
	select *
	from z_year_field_journal yfj
	join z_field_ford ff on ff.fid = yfj.fid
	where yfj.year = 2020 
	and ff.name = '5.5 Law' 
	and yfj.ranking = 'Q1' 
	and yfj.jid = j.jid
) and exists (
	select *
	from z_year_field_journal yfj
	join z_field_ford ff on ff.fid = yfj.fid
	where yfj.year = 2021 
	and ff.name = '5.5 Law' 
	and yfj.ranking = 'Q2'
	and yfj.jid = j.jid
)
order by j.name

-- 3.1
/*
	Naleznete casopisy z oboru obsahujici slova 'Materials engineering',
	ktere publikovaly od autoru z vice nez 15 instituci v letech 2020-2021.
	Pro kazdy casopis vypiste jeho nazev a prumerny pocet autoru na clanek a pocet instituci.
	Vysledek setridte podle nazvu casopisu.

	Nezapomente na datovou konverzi poctu pred prumerovanim na datovy typ s desetinnou casti.
	AVG(cast(pocet as float))

	Vysledek 19 zaznamu
*/
with avg_author as (
	select aid, count(distinct rid) as au_count
	from z_article_author aa
	group by aid
)

select j.name,  
	count(distinct ai.iid) as i_count, 
	avg(cast(avg_author.au_count as float)) as avg_author_count
from z_journal j
join z_year_field_journal yfj on yfj.jid = j.jid
join z_field_ford ff on ff.fid = yfj.fid
join z_article ar on yfj.jid = ar.jid and yfj.year = ar.year
join z_article_institution ai on ai.aid = ar.aid
join avg_author on avg_author.aid = ar.aid
where ff.name like '%Materials engineering%' and yfj.year between 2020 and 2021
group by j.name
having count(distinct ai.iid) > 15
order by j.name

-- 3.2
select au.name
from z_author au
where exists (
	select 1
	from z_article_author aa
	join z_article ar on ar.aid = aa.aid
	join z_article_institution ai on ai.aid = ar.aid
	join z_institution i on i.iid = ai.iid
	where ar.year = 2019 and i.town = 'BRNO' and aa.rid = au.rid
)
and exists (
	select *
	from z_year_field_journal yfj
	join z_article ar on ar.jid = yfj.jid and ar.year = yfj.year
	join z_article_author aa on aa.aid = ar.aid
	where yfj.year = 2020 and yfj.ranking = 'Q1' and au.rid = aa.rid
)
order by au.name

-- 3.3
select j.name, count(distinct ai.iid) i_count, count(distinct ar.aid) ar_count
from z_journal j
join z_year_field_journal yfj on yfj.jid = j.jid
join z_field_ford ff on ff.fid = yfj.fid
join z_article ar on ar.jid = yfj.jid and ar.year = yfj.year
join z_article_institution ai on ai.aid = ar.aid
join z_article_author aa on aa.aid = ar.aid
where ff.name like '%1.2 Computer and Information Sciences%' 
	and yfj.year between 2019 and 2021
group by j.name
having count(distinct ai.iid) > 8
order by i_count desc




