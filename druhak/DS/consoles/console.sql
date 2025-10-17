-- 1.1 (15)
select email
from customer
where active = 0

-- 1.2 (177)
select title, description
from film
where rating = 'G'
order by title desc

-- 1.3 (78)
select *
from payment
where year(payment_date) >= 2006 and amount < 2

-- 1.4 (371)
select description
from film
where rating IN('G', 'PG')

-- 1.5 (592)
select description
from film
where rating IN('G', 'PG', 'PG-13')

-- 1.6 (405)
select description
from film
where rating NOT IN('G', 'PG', 'PG-13')

-- 1.7 (377)
select *
from film
where length > 50
  and rental_duration IN (3, 5)

-- 1.8 (3)
select title
from film
where title like '%RAINBOW%' or title like 'TEXAS%' and length > 70

-- 1.9 (46)
select title
from film
where description like '%And%' and length between 80 and 90 and rental_duration % 2 = 1

-- 1.10 (14)
select distinct special_features
from film
where replacement_cost between 14 and 16
order by special_features

-- 1.11 (325)
select *
from film
where (not rental_duration < 4 and rating = 'PG') or (rental_duration < 4 and not rating = 'PG')

-- 1.12 (599)
select *
from address
where postal_code is not null

-- 1.13 (182)
select distinct count(r.customer_id)
from rental r
where return_date is null

-- 1.14 (15953)
select distinct payment_id, year(payment_date) as payment_year, month(payment_date) as payment_month, day(payment_date) as payment_day
from payment
order by payment_id

-- 1.15 (974)
select *
from film
where len(title) != 20

-- 1.16 (15865)
select rental_id, DATEDIFF(minute, rental_date, return_date)
from rental
where return_date is not null

-- 1.17 (584)
select customer_id, concat(first_name, ' ', last_name) as full_name
from customer
where active = 1

-- 1.18 (603) !IMPORTANT
select address, coalesce(postal_code, '(prazdne)') as psc
from address

-- 1.19
select concat(cast(rental_date as varchar), ' - ', cast(return_date as varchar)) as from_to
from rental
where return_date is not null

-- 1.20 (16047) !IMPORTANT
select rental_id, cast(rental_date as varchar) + coalesce(' - ' + cast(return_date as varchar), '') as interval
from rental

select rental_id,
       concat(
               rental_date,
               coalesce(
                       concat(' - ', cast(return_date as varchar)), '')
       )
from rental

-- 1.21 (997)
select count(distinct film_id) as pocet_filmu
from film

-- 1.22 (5)
select distinct rating
from film

-- 1.23
select count(distinct address)         as pocet_celkem,
       (select count(address)
        from address
        where postal_code is not null) as pocet_s_psc,
       (select count(distinct postal_code)
        from address)                  as pocet_psc
from address

select count(*)                    as pocet_celkem,
       count(postal_code)          as pocet_s_psc,
       count(distinct postal_code) as pocet_psc
from address

-- 1.24
select min(length) as min,
       max(length) as max,
       avg(cast(length as float)) as avg,
       sum(length) / cast(count(*) as float) as calculates_avg
from film

-- 1.25
select count(*) as amount, sum(amount) as sum
from payment
where year(payment_date) = 2005

-- 1.26
select sum(len(title)) as total_length
from film

-- 2.1 (600)
select *
from city
join country on city.country_id = country.country_id

-- 2.2 (997)
select title, language.name
from film
join language on film.language_id = language.language_id

-- 2.3 (28)
select r.rental_id
from rental r
join customer on r.customer_id = customer.customer_id
where customer.last_name = 'SIMPSON'

-- 2.4
select a.address
from address a
join customer on a.address_id = customer.address_id
where customer.last_name = 'SIMPSON'

-- 2.5 (599)
select c.first_name, c.last_name, a.address, a.postal_code, city.city
from customer c
join address a on c.address_id = a.address_id
join city on a.city_id = city.city_id
order by last_name

-- 2.6 (599)
select c.first_name + ' ' + c.last_name AS name, city
from customer c
         join address on c.address_id = address.address_id
         join city on address.city_id = city.city_id

-- 2.7 (16047)
select r.rental_id, s.first_name + ' ' + s.last_name as staff_name, c.first_name + ' ' + c.last_name as customer_name, f.title
from rental r
join staff s on r.staff_id = s.staff_id
join customer c on r.customer_id = c.customer_id
join inventory i on r.inventory_id = i.inventory_id
join film f on i.film_id = f.film_id
order by r.rental_id

-- 2.8 (5431)
select f.title, a.first_name + ' ' + a.last_name as name
from film f
join film_actor fa on f.film_id = fa.film_id
join actor a on fa.actor_id = a.actor_id
order by f.title

-- 2.9 (5431)
select a.first_name + ' ' + a.last_name as name, f.title
from actor a
join film_actor fa on a.actor_id = fa.actor_id
join film f on fa.film_id = f.film_id
order by a.last_name

-- 2.10 (275)
select title
from film
join film_category fc on film.film_id = fc.film_id
join category c on fc.category_id = c.category_id
where c.name = 'Horror'

select f.title
from category
join film_category fc on category.category_id = fc.category_id
join film f on fc.film_id = f.film_id
where name = 'Horror'

-- 2.11 (2)
select store.store_id,
       country.country,
       city.city,
       address.address,
       staff.first_name + ' ' + staff.last_name as manager_name
from store
join staff on store.manager_staff_id = staff.staff_id
join address on store.address_id = address.address_id
join city on address.city_id = city.city_id
join country on city.country_id = country.country_id

-- 2.12 (22800)
select film.film_id, film.title, fa.actor_id, fc.category_id
from film
join film_actor fa on film.film_id = fa.film_id
join film_category fc on film.film_id = fc.film_id
order by film.film_id

-- 2.13 (22800)
select actor.actor_id, actor.first_name + ' ' + actor.last_name, category.name
from actor
join film_actor fa on actor.actor_id = fa.actor_id
join film on fa.film_id = film.film_id
join film_category fc on film.film_id = fc.film_id
join category on fc.category_id = category.category_id
order by actor.actor_id

select actor.actor_id, actor.first_name, actor.last_name, category.category_id, category.name
from film
join film_actor on film.film_id = film_actor.film_id
join film_category on film.film_id = film_category.film_id
join actor on film_actor.actor_id = actor.actor_id
join category on film_category.category_id = category.category_id
order by actor.actor_id

-- 2.14 (955)
select distinct film.title
from film
join inventory on film.film_id = inventory.film_id

-- 2.15 (198)
select distinct actor.actor_id, actor.first_name + ' ' + actor.last_name as actor_name
from actor
join film_actor fa on actor.actor_id = fa.actor_id
join film f on fa.film_id = f.film_id
join film_category fc on f.film_id = fc.film_id
join category on fc.category_id = category.category_id
where category.name = 'Comedy'

-- 2.16 (2)
select c.first_name + ' ' + c.last_name as customer_name
from customer c
join address a on c.address_id = a.address_id
join city on a.city_id = city.city_id
join country on city.country_id = country.country_id
join rental on c.customer_id = rental.customer_id
join inventory on rental.inventory_id = inventory.inventory_id
join film on inventory.film_id = film.film_id
where country.country = 'ITALY' and film.title = 'MOTIONS DETAILS'

-- 2.17 (10)
select distinct c.first_name, c.last_name
from customer c
join rental on c.customer_id = rental.customer_id and rental.return_date is null
join inventory on rental.inventory_id = inventory.inventory_id
join film on inventory.film_id = film.film_id
join film_actor on film.film_id = film_actor.film_id
join actor on film_actor.actor_id = actor.actor_id
where actor.first_name = 'SEAN' and actor.last_name = 'GUINESS'

select distinct customer.first_name, customer.last_name
from actor
join film_actor on actor.actor_id = film_actor.actor_id
join film on film_actor.film_id = film.film_id
join inventory on film.film_id = inventory.film_id
join rental on inventory.inventory_id = rental.inventory_id
join customer on rental.customer_id = customer.customer_id
where actor.first_name = 'SEAN' and actor.last_name = 'GUINESS' and rental.return_date IS NULL

-- 2.18 (15953)
select p.payment_id, p.amount, rental.rental_date
from payment p
left join rental on p.rental_id = rental.rental_id

-- 2.19 (998)
select l.name, film.title
from language l
left join film on l.language_id = film.language_id

-- 2.20 (997)
select film.film_id, film.title, l.name as language, ol.name as original_language
from film
join language as l on film.language_id = l.language_id
left join language as ol on film.original_language_id = ol.language_id

-- 2.21 (231)
select film.title, coalesce(customer.first_name + ' ' + customer.last_name, '')
from film
left join inventory on film.film_id = inventory.film_id
left join rental on inventory.inventory_id = rental.inventory_id
left join customer on rental.customer_id = customer.customer_id
where customer.first_name = 'TIM' and customer.last_name = 'CARY' or film.length = 48

-- 2.22
select title
from film
where film_id NOT IN(
    select film_id
    from inventory
    )

select film.title
from film
left join inventory on film.film_id = inventory.film_id
where inventory.inventory_id is null

-- 2.23 (133)
select distinct first_name, last_name
from customer
join rental on customer.customer_id = rental.customer_id
left join payment on rental.rental_id = payment.rental_id
where payment.payment_id is null

-- 2.24 (997)
select film.title, l.name
from film
left join language l on film.language_id = l.language_id and l.name like 'I%'
order by l.name desc

-- 2.25 (682)
select c.first_name + ' ' + c.last_name as customer_name, payment.payment_id
from customer c
left join payment on c.customer_id = payment.customer_id and payment.amount > 9

-- 2.26 (16047)
select rental.rental_id, film.title, city.city, country.country
from rental
left join inventory on rental.inventory_id = inventory.inventory_id
left join film on inventory.film_id = film.film_id and title like '%U%'
left join customer on rental.customer_id = customer.customer_id
left join address on customer.address_id = address.address_id and address.address like '%A%'
left join city on address.city_id = city.city_id
left join country on city.country_id = country.country_id

-- 2.27 (15557)
select distinct film.title, customer.last_name
from film
join inventory on film.film_id = inventory.film_id
join rental on inventory.inventory_id = rental.inventory_id
left join customer on rental.customer_id = customer.customer_id and rental.rental_date <= '2006-01-01'
order by film.title


