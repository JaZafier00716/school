-- 1 a 4 + extension
select film_id, title, (
    select count(distinct fa.actor_id)
    from film_actor fa
    where f.film_id = fa.film_id
    ) as herci,
    (
    select count(distinct fc.category_id)
    from film_category fc
    where f.film_id = fc.film_id
    ) as kategorie,
    (
    select count(distinct rental.customer_id)
    from inventory
    join rental on inventory.inventory_id = rental.inventory_id
    where inventory.film_id = f.film_id and month(rental_date) = 8
    ) as customer_count,
    (
     select avg(amount)
     from inventory
     join rental on inventory.inventory_id = rental.inventory_id
     join payment on rental.rental_id = payment.rental_id
     where inventory.film_id = f.film_id
    ) as avg_price
from film f

select count(distinct rental.customer_id)
from film
join inventory on film.film_id = inventory.film_id
join rental on inventory.inventory_id = rental.inventory_id
where film.film_id = 1 and month(rental_date) = 8


select avg(amount)
from film
join inventory on film.film_id = inventory.film_id
join rental on inventory.inventory_id = rental.inventory_id
join payment on rental.rental_id = payment.rental_id
where film.film_id = 1


with t as (
    select film_id,
           title,
           (select count(distinct fa.actor_id)
            from film_actor fa
            where f.film_id = fa.film_id) as herci,
           (select count(distinct fc.category_id)
            from film_category fc
            where f.film_id = fc.film_id) as kategorie
    from film f
)

select *
from t
where herci > 10 and kategorie <= 2

with payment_amount as (
    select customer_id, count(*) as payment_count
    from payment
    group by customer_id),
    max_film_length as (
        select customer_id, max(length) as max_length
        from film
        join inventory on film.film_id = inventory.film_id
        join rental on inventory.inventory_id = rental.inventory_id
        group by customer_id
    )

-- 5
select *
from customer
join payment_amount on customer.customer_id = payment_amount.customer_id
join max_film_length on max_film_length.customer_id = customer.customer_id
where payment_count > 5 and max_length > 185

-- 11
with rental_count as (
select customer_id, (
    select count(*)
    from rental
    where rental.customer_id = customer.customer_id
    ) as count
from customer
), english_only as
(
select customer_id
from customer
where not exists(
    select *
    from rental
    join inventory on rental.inventory_id = inventory.inventory_id
    join film on inventory.film_id = film.film_id
    join language on film.language_id = language.language_id
    where customer.customer_id = rental.rental_id and language.name != 'English'
))

select *
from customer
join english_only on english_only.customer_id = customer.customer_id
join rental_count on rental_count.customer_id = customer.customer_id

-- 14
select title, rating, length
from
    (
    select title, rating, rank() over ( partition by film.rating order by length desc) as max_length, length
    from film
    ) length_per_group
where max_length = 1


select t.title, rating, max(length) as max_length
from (
    select f1.title, length, rating
    from film f1
    where not exists(
        select  1
        from film f2
        where f2.length > f1.length and f1.rating = f2.rating
    )
) as t
group by t.title, t.rating


-- 16 nefunguje ma jich byt 200 ne 17
select actor.actor_id, actor.first_name, actor.last_name, title
from actor
left join (
    select actor_id, rank() over (partition by actor_id order by length desc) as rnk, film.title
    from film
    join film_actor on film.film_id = film_actor.film_id
) t on t.actor_id = actor.actor_id
where t.rnk = 1

-- tester function
select actor_id, rank() over (order by length desc) as rnk, film.title
from film
join film_actor on film.film_id = film_actor.film_id
where actor_id = 116

select distinct actor_id
from actor;

-- 25
with max_rentals as (
    select c.customer_id, c.first_name, c.last_name, count(*) as pocet_vypujcek
    from customer c
    join rental r on c.customer_id = r.customer_id
    group by c.customer_id, c.first_name, c.last_name
), tab_rank as (
    select *, rank() over (order by pocet_vypujcek desc) rnk
    from max_rentals
)

select *
from tab_rank
where rnk = 1
