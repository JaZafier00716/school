-- 1
select *
from film f
join film_actor fa on f.film_id = fa.film_id
where actor_id = 1

select *
from film f
where f.film_id IN (
    select fa.film_id
    from film_actor fa
    where fa.actor_id = 1
    )

select *
from film f
where EXISTS (
    select fa.film_id
    from film_actor fa
    where fa.actor_id = 1 and f.film_id = fa.film_id
)

-- 3
select f.film_id, f.title
from film f
where f.film_id IN(
    select fa.film_id
    from film_actor fa
    WHERE fa.actor_id = 1
)
  and
    f.film_id IN(
        select fa.film_id
        from film_actor fa
        WHERE fa.actor_id = 10
    )

select f.film_id, f.title
from film f
where EXISTS(
    select 1
    from film_actor fa
    where fa.actor_id = 1 and fa.film_id = f.film_id
) and EXISTS(
        select 1
        from film_actor fa
        where fa.actor_id = 10 and fa.film_id = f.film_id
    )

select f.*
from film f
join film_actor fa1 on f.film_id = fa1.film_id
join film_actor fa10 on f.film_id = fa10.film_id
WHERE fa1.actor_id = 1 and fa10.actor_id = 10

select f.*
from film f
         join film_actor fa1 on f.film_id = fa1.film_id and fa1.actor_id = 1
         join film_actor fa10 on f.film_id = fa10.film_id and fa10.actor_id = 10

-- 4
select *
from film f
WHERE EXISTS(
    select *
    from film_actor fa
    WHERE (fa.actor_id = 1 OR fa.actor_id = 10) and f.film_id = fa.film_id
)

-- 5
select *
from film f
where NOT EXISTS(
    select *
    from film_actor fa
    WHERE fa.actor_id = 1 and f.film_id = fa.film_id
)

select *
from film f
WHERE f.film_id NOT IN (
    select fa.film_id
    from film_actor fa
    WHERE fa.actor_id = 1
    )

-- 6
select *
from film f
where (EXISTS(
    select 1
    from film_actor fa
    WHERE f.film_id = fa.film_id and fa.actor_id = 1
) and NOT EXISTS(
    select 1
    from film_actor fa
    WHERE f.film_id = fa.film_id and fa.actor_id = 10
)) OR (NOT EXISTS(
    select 1
    from film_actor fa
    WHERE f.film_id = fa.film_id and fa.actor_id = 1
) and EXISTS(
    select 1
    from film_actor fa
    WHERE f.film_id = fa.film_id and fa.actor_id = 10
))

-- 7
select *
from film
where EXISTS(
    select 1
    from film_actor fa
    where fa.actor_id IN (
        select actor.actor_id
        from actor
        WHERE actor.first_name LIKE 'PENELOPE' and actor.last_name LIKE 'GUINESS'
        ) and film.film_id = fa.film_id
) and EXISTS(
    select 1
    from film_actor fa
    where fa.actor_id IN (
        select actor.actor_id
        from actor
        WHERE actor.first_name LIKE 'CHRISTIAN' and actor.last_name LIKE 'GABLE'
    ) and film.film_id = fa.film_id
)

-- 8
select *
from film
where NOT EXISTS(
    select 1
    from film_actor fa
    where fa.actor_id IN (
        select actor.actor_id
        from actor
        WHERE actor.first_name LIKE 'PENELOPE' and actor.last_name LIKE 'GUINESS'
    ) and film.film_id = fa.film_id
)

-- 13
select *
from film f1
WHERE EXISTS(
    select *
    from film f2
    join film_actor on f2.film_id = film_actor.film_id
    join actor on film_actor.actor_id = actor.actor_id
    where actor.first_name LIKE 'BURT'
      and actor.last_name LIKE 'POSEY'
    and f2.length > f1.length
)

select f1.title
from film f1
WHERE f1.length < any (
    select f2.length
    from film f2
    join film_actor on f2.film_id = film_actor.film_id
    join actor on film_actor.actor_id = actor.actor_id
    where actor.first_name LIKE 'BURT'
      and actor.last_name LIKE 'POSEY'
    )


select f1.title
from film f1
WHERE f1.length < (
    select max(f2.length)
    from film f2
             join film_actor on f2.film_id = film_actor.film_id
             join actor on film_actor.actor_id = actor.actor_id
    where actor.first_name LIKE 'BURT'
      and actor.last_name LIKE 'POSEY'
)

-- 16
select f.film_id, f.title, count(distinct rental.customer_id)
from film f
join inventory on f.film_id = inventory.film_id
join rental on inventory.inventory_id = rental.inventory_id
group by f.film_id, f.title
having count(distinct rental.customer_id) >= 2


select f.film_id, f.title
from film f
WHERE (
          select count(distinct r.customer_id)
          from inventory i
                   join rental r on i.inventory_id = r.inventory_id
          where f.film_id = i.film_id
      ) > 1

-- 21
select *
from customer c
where customer_id not in(
    select customer_id
    from rental
    group by customer_id, month(rental_date)
    having count(*) > 3
)

-- 31
select distinct concat(customer.first_name, ' ', customer.last_name)
from customer
WHERE NOT exists(select *
                 from rental
                          join inventory on rental.inventory_id = inventory.inventory_id
                          join film on inventory.film_id = film.film_id
                 where film.film_id NOT IN (select film_actor.film_id
                                            FROM film_actor
                                                     join actor on film.film_id = film_actor.film_id
                                            where actor.first_name = 'CHRISTIAN'
                                              and actor.last_name = 'GABLE')
                  and rental.customer_id = customer.customer_id
                 )


