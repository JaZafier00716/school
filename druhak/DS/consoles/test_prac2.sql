
select rid, name
from (
	select au.rid, au.name, yfj.year
	from z_author au
	join z_article_author aa on aa.rid = au.rid
	join z_article ar on ar.aid = aa.aid
	join z_year_field_journal yfj on ar.jid = yfj.jid and yfj.year = ar.year
	where yfj.ranking = 'DECIL' and yfj.year in (2017,2018,2019,2020)
	group by au.rid, au.name, yfj.year
	having count(distinct ar.aid) >= 15
) t
group by rid, name
having count(*) = 4


with baca as (
select ar.aid
from z_article ar
join z_article_author aa on aa.aid = ar.aid
join z_author au on au.rid = aa.rid
where au.name = 'Baca, Radim'
), kratky as (
	select baca.aid
	from baca
	join z_article_author aa on aa.aid = baca.aid
	join z_author au on au.rid = aa.rid
	where au.name = 'Kratky, Michal'
)

select *
from kratky
join z_article_author aa on aa.aid = kratky.aid
join z_author au on au.rid = aa.rid
where au.name not in ('Baca, Radim', 'Kratky, Michal')

with tab as (
select ar.aid, ar.name, count(distinct aa.rid) a_count
from z_article ar
join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
join z_field_ford ff on ff.fid = yfj.fid
join z_field_of_science fs on fs.sid = ff.sid
join z_article_author aa on aa.aid = ar.aid 
where fs.name = 'Natural sciences'
group by ar.aid, ar.name
)

select aid, name, a_count
from tab t1
where t1.a_count >= all (
	select t2.a_count
	from tab t2
)

-- 1
with tab as (
select i.iid, i.name as i_name, au.rid, au.name as au_name, count(distinct ar.aid) ar_count
from z_institution i
join z_article_institution ai on ai.iid = i.iid
join z_article ar on ar.aid = ai.aid
join z_year_field_journal yfj on yfj.jid = ar.jid and yfj.year = ar.year
join z_article_author aa on ar.aid = aa.aid
join z_author au on au.rid = aa.rid
where town like 'PRAHA%' and yfj.ranking = 'DECIL'
group by i.iid, i.name, au.rid, au.name
)

select iid, i_name, rid, au_name, ar_count
from tab t1
where t1.ar_count >= all (
	select t2.ar_count
	from tab t2
	where t1.iid = t2.iid
)
order by ar_count desc

--2 
select au.rid, au.name
from z_author au
where exists (
	select *
	from z_journal j
	join z_article ar on ar.jid = j.jid
	join z_article_author aa on aa.aid = ar.aid
	where au.rid = aa.rid and j.issn = '0004-3702'
) and not exists (
	select *
	from z_journal j
	join z_article ar on ar.jid = j.jid
	join z_article_author aa on aa.aid = ar.aid
	where au.rid = aa.rid and j.issn = '2169-3536'
)
order by au.rid


-- 3
select ff.fid, ff.name,
(
	select count(a1.aid)
	from z_article a1
	join z_year_field_journal yfj1 on yfj1.jid = a1.jid and yfj1.year = a1.year
	where yfj1.fid = ff.fid
) as ar_count,
(
	select count(a2.aid)
	from z_article a2
	join z_year_field_journal yfj2 on yfj2.jid = a2.jid and yfj2.year = a2.year
	where yfj2.fid = ff.fid and yfj2.ranking = 'DECIL'
) as decil_count,
(
	select count(a3.aid)
	from z_article a3
	join z_year_field_journal yfj3 on yfj3.jid = a3.jid and yfj3.year = a3.year
	join z_article_institution ai on ai.aid = a3.aid
	join z_institution i on i.iid = ai.iid
	where yfj3.fid = ff.fid and i.name = 'Vysoká škola báňská - Technická univerzita Ostrava'
)
from z_field_ford ff
join z_field_of_science fs on fs.sid = ff.sid
where fs.name = 'Engineering and Technology'
order by ff.name