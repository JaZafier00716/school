-- 1
select distinct au.rid, au.name
from z_institution i
join z_article_institution ai on ai.iid = i.iid
join z_article ar on ar.aid = ai.aid
join z_article_author aa on aa.aid = ar.aid
join z_author au on au.rid = aa.rid
where i.town like '%ostrava%'

select distinct au.rid, au.name
from z_institution i
join z_article_institution ai on ai.iid = i.iid
join z_article_author aa on aa.aid = ai.aid
join z_author au on au.rid = aa.rid
where i.town like '%ostrava%'

select au.rid, au.name
from z_author au 
where au.rid in (
	select aa.rid
	from z_article_author aa
	join z_article_institution ai on aa.aid = ai.aid
	join z_institution i on i.iid = ai.iid
	where town like '%ostrava%'
	)


-- 2
select distinct i.iid, i.name
from z_institution i
join z_article_institution ai on ai.iid = i.iid
join z_article ar on ar.aid = ai.aid
join z_article_author aa on aa.aid = ar.aid
join z_author au on au.rid = aa.rid
where au.name = 'Pumera, Martin'
order by i.iid

-- kolik clanku v institucich
select i.iid, i.name, count(distinct ai.aid)
from z_institution i
join z_article_institution ai on ai.iid = i.iid
join z_article ar on ar.aid = ai.aid
join z_article_author aa on aa.aid = ar.aid
join z_author au on au.rid = aa.rid
where au.name = 'Pumera, Martin'
group by i.iid, i.name


-- 3
-- helper
select distinct ranking
from z_year_field_journal


select distinct ar.aid ,ar.name
from z_article ar
join z_year_field_journal yfj  on yfj.jid = ar.jid and ar.year = yfj.year
where yfj.ranking = 'decil'
order by ar.aid

select a.aid, a.name
from z_article a
where exists(
	select 1
	from z_year_field_journal yfj
	where yfj.jid = a.jid and yfj.year = a.year and yfj.ranking = 'decil'
)


-- 4
select distinct ar.*
from z_article ar
join z_year_field_journal yfj on yfj.jid = ar.jid and ar.year = yfj.year
join z_article_institution ai on ai.aid = ar.aid
join z_institution i on i.iid = ai.iid
where yfj.ranking = 'decil' and i.name = 'Vysoká škola báňská - Technická univerzita ostrava'

-- helper
select *
from z_institution i
where i.name = 'Vysoká škola báňská - Technická univerzita ostrava'


-- 5
select distinct j.jid, j.name
from z_journal j
join z_article ar on ar.jid = j.jid
join z_year_field_journal yfj on yfj.jid = ar.jid and ar.year = yfj.year
join z_field_ford ff on ff.fid = yfj.fid
where yfj.ranking = 'decil' 
	and ff.name = '1.2 Computer and Information Sciences'
order by j.name 

select *
from z_journal j
where exists(
	select 1
	from z_year_field_journal yfj
	join z_field_ford ff on yfj.fid = ff.fid
	join z_article a on a.jid = j.jid 
	where yfj.jid = j.jid
		and yfj.year = a.year
		and yfj.ranking = 'decil'
		and ff.name = '1.2 Computer and Information Sciences'
)

select jid
from z_journal j
except
select jid
from z_article


-- 6
select distinct ar.aid, ar.name
from z_article ar 
join z_year_field_journal yfj on yfj.jid = ar.jid and ar.year = yfj.year
join z_field_ford ff on ff.fid = yfj.fid
join z_article_institution ai on ar.aid = ai.aid
join z_institution i on i.iid = ai.iid
where yfj.ranking = 'Q1' 
	and ff.name = '1.2 Computer and Information Sciences'
	and i.name = 'Vysoká škola báňská - Technická univerzita ostrava'
order by ar.name 


-- 7
select au.rid, au.name, count(ar.aid) as article_count
from z_author au
join z_article_author aa on au.rid = aa.rid
join z_article ar on ar.aid = aa.aid
join z_year_field_journal yfj on yfj.jid = ar.jid  and ar.year = yfj.year
join z_field_ford ff on ff.fid = yfj.fid
join z_article_institution ai on ar.aid = ai.aid
join z_institution i on i.iid = ai.iid
where (yfj.ranking = 'decil' or yfj.ranking = 'q1')
	and ff.name = '1.2 Computer and Information Sciences'
	and i.name = 'Vysoká škola báňská - Technická univerzita ostrava'
group by au.rid, au.name
order by article_count desc


-- 8
with tab as (
    select a.rid, a.name, ar.jid, ar.aid, ar.year
    from z_author a
    join z_article_author aa on aa.rid = a.rid
    join z_article ar on ar.aid = aa.aid
    join z_article_institution ai on ai.aid = ar.aid
    join z_institution i on i.iid = ai.iid
    where i.name like 'Vysoká škola báňská%' 
)

select distinct t2.rid, t2.name,
(
    select count(distinct t1.aid)
    from tab t1
    join z_year_field_journal yfj on yfj.jid = t1.jid and yfj.year = t1.year
    where yfj.ranking = 'DECIL' and t1.rid = t2.rid
) as decil_count,
(
    select count(distinct t1.aid)
    from tab t1
    join z_year_field_journal yfj on yfj.jid = t1.jid and yfj.year = t1.year
    where yfj.ranking = 'Q1' and t1.rid = t2.rid
) q1_count
from tab t2
order by decil_count desc, q1_count desc


-- 9
with tab as (
select au.rid, au.name as a_name, ff.fid, ff.name as f_name, count(distinct ar.aid) as a_count
from z_author au
    join z_article_author aa on aa.rid = au.rid
    join z_article ar on ar.aid = aa.aid
    join z_year_field_journal yfj on yfj.jid = ar.jid
    join z_field_ford ff on ff.fid = yfj.fid 
group by ff.name, ff.fid, au.rid, au.name
)

select fid, f_name, rid, a_name, a_count
from tab t1
where a_count >= all(
    select t2.a_count
    from tab t2
    where t1.fid = t2.fid and t1.rid != t2.rid
)
order by f_name, a_name


-- 10
with tab as (
select ff.fid, ff.name as f_name, aa.aid, count(distinct aa.rid) as a_count
from z_article_author aa
    join z_article ar on ar.aid = aa.aid
    join z_year_field_journal yfj on yfj.jid = ar.jid
    join z_field_ford ff on ff.fid = yfj.fid 
group by ff.name, ff.fid, aa.aid
)

select *
from tab t1
where a_count >= all (
    select t2.a_count
    from tab t2
    where t1.fid = t2.fid and t1.aid != t2.aid
)
order by a_count desc