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
where year(payment_date) >= 2006
  and amount < 2

-- 1.4 (371)
select description
from film
where rating IN ('G', 'PG')

-- 1.5 (592)
select description
from film
where rating IN ('G', 'PG', 'PG-13')

-- 1.6 (405)
select description
from film
where rating NOT IN ('G', 'PG', 'PG-13')

-- 1.7 (377)
select *
from film
where length > 50
  and rental_duration IN (3, 5)

-- 1.8 (3)
select title
from film
where title like '%RAINBOW%'
   or title like 'TEXAS%' and length > 70

-- 1.9 (46)
select title
from film
where description like '%And%'
  and length between 80 and 90
  and rental_duration % 2 = 1

-- 1.10 (14)
select distinct special_features
from film
where replacement_cost between 14 and 16
order by special_features

-- 1.11 (325)
select *
from film
where (not rental_duration < 4 and rating = 'PG')
   or (rental_duration < 4 and not rating = 'PG')

-- 1.12 (599)
select *
from address
where postal_code is not null

-- 1.13 (182)
select distinct count(r.customer_id)
from rental r
where return_date is null

-- 1.14 (15953)
select distinct payment_id,
                year(payment_date)  as payment_year,
                month(payment_date) as payment_month,
                day(payment_date)   as payment_day
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
select address, coalesce(postal_code, '(empty)') as psc
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
select min(length)                           as min,
       max(length)                           as max,
       avg(cast(length as float))            as avg,
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
select r.rental_id,
       s.first_name + ' ' + s.last_name as staff_name,
       c.first_name + ' ' + c.last_name as customer_name,
       f.title
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
where country.country = 'ITALY'
  and film.title = 'MOTIONS DETAILS'

-- 2.17 (10)
select distinct c.first_name, c.last_name
from customer c
         join rental on c.customer_id = rental.customer_id and rental.return_date is null
         join inventory on rental.inventory_id = inventory.inventory_id
         join film on inventory.film_id = film.film_id
         join film_actor on film.film_id = film_actor.film_id
         join actor on film_actor.actor_id = actor.actor_id
where actor.first_name = 'SEAN'
  and actor.last_name = 'GUINESS'

select distinct customer.first_name, customer.last_name
from actor
         join film_actor on actor.actor_id = film_actor.actor_id
         join film on film_actor.film_id = film.film_id
         join inventory on film.film_id = inventory.film_id
         join rental on inventory.inventory_id = rental.inventory_id
         join customer on rental.customer_id = customer.customer_id
where actor.first_name = 'SEAN'
  and actor.last_name = 'GUINESS'
  and rental.return_date IS NULL

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
where customer.first_name = 'TIM' and customer.last_name = 'CARY'
   or film.length = 48

-- 2.22
select title
from film
where film_id NOT IN (select film_id
                      from inventory)

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


-- 3.1
select rating, count(*) film_count
from film
group by rating

-- 3.2
select customer_id, count(last_name)
from customer
group by customer_id;

-- 3.3
with v1 as (select c.customer_id, sum(payment.amount) as suma
            from customer c
                     join payment on c.customer_id = payment.customer_id
            group by c.customer_id),
     v2 as (select customer_id, sum(amount) as suma
            from payment
            group by customer_id)

    (select *
     from v1)
except
(select *
 from v2)
union all
(select *
 from v2)
except
(select *
 from v1)

-- 3.4
select first_name, last_name, count(*) as amount
from actor
group by first_name, last_name
order by amount desc

-- 3.5

select YEAR(payment_date) as year, MONTH(payment_date) as month, sum(amount) as amount
from payment
group by YEAR(payment_date), MONTH(payment_date)
order by year, month

-- select *
-- from payment
-- where year(payment_date) not in (2005, 2006)

-- 3.6
select store_id, count(*) as amount
from inventory
group by store_id
having count(film_id) > 2300

-- 3.7
select language_id, min(length) as min_length
from film
group by language_id
having min(length) > 46

-- 3.8
select year(payment_date), month(payment_date), sum(amount)
from payment
group by year(payment_date), month(payment_date)
having sum(amount) > 20000

-- 3.9
select rating
from film
where length < 50
group by rating
having sum(length) > 250
order by rating desc

-- 3.10
select language_id, count(distinct film_id) as film_count
from film
group by language_id

-- 3.11
select language.name, count(distinct film_id) as film_count
from film
         join language on film.language_id = language.language_id
group by language.name

-- 3.12
select language.name, count(distinct film_id) as film_count
from film
         right join language on film.language_id = language.language_id
group by language.name;

-- 3.13
with t1 as (select c.customer_id, c.first_name, c.last_name, count(*) as amount
            from customer c
                     left join rental on c.customer_id = rental.customer_id
            group by c.customer_id, c.first_name, c.last_name)
select *
from t1
where customer_id = 318

with t2 as (select c.customer_id, c.first_name, c.last_name, count(rental.rental_id) as amount
            from customer c
                     left join rental on c.customer_id = rental.customer_id
            group by c.customer_id, c.first_name, c.last_name)
-- results in amount = 1 instead of 0 when null => t2 is right

select *
from t2
where customer_id = 318
-- except
-- (select *
--  from t2)
-- union all
-- (select *
--  from t2)
-- except
-- (select *
--  from t1)

-- select *
-- from rental
-- where customer_id = 1

-- 3.14
select c.customer_id, c.first_name, c.last_name, count(distinct film_id) as film_amount
from customer c
         left join rental on c.customer_id = rental.customer_id
         left join inventory on rental.inventory_id = inventory.inventory_id
group by c.customer_id, c.first_name, c.last_name
-- having count(distinct film_id) = 0

-- 3.15
select actor.first_name, actor.last_name
from actor
         left join film_actor on actor.actor_id = film_actor.actor_id
group by actor.first_name, actor.last_name
having count(distinct film_id) > 20

-- 3.16
select c.customer_id,
       c.first_name + ' ' + c.last_name as customer_name,
       coalesce(sum(payment.amount), 0) as total,
       min(payment.amount)              as minimum,
       max(payment.amount)              as maximum,
       avg(payment.amount)              as average
from customer c
         left join rental on c.customer_id = rental.customer_id
         left join payment on rental.rental_id = payment.rental_id
group by c.customer_id, c.first_name + ' ' + c.last_name
order by total

-- 3.17
select category.category_id, category.name, avg(cast(film.length as float)) as avg_length
from category
         left join film_category on category.category_id = film_category.category_id
         left join film on film_category.film_id = film.film_id
group by category.category_id, category.name

-- 3.18
select film.film_id, film.title, sum(payment.amount) as revenue
from film
         left join inventory on film.film_id = inventory.film_id
         left join rental on inventory.inventory_id = rental.inventory_id
         left join payment on rental.rental_id = payment.rental_id
group by film.film_id, film.title
having sum(payment.amount) > 100

-- 3.19
select actor.actor_id, actor.first_name, actor.last_name, count(distinct fc.category_id) as category_amount
from actor
         left join film_actor on actor.actor_id = film_actor.actor_id
         left join film on film_actor.film_id = film.film_id
         left join film_category fc on film.film_id = fc.film_id
group by actor.actor_id, actor.first_name, actor.last_name

-- 3.20
select address.address, city.city, country.country
from customer
         join address on customer.address_id = address.address_id
         join city on address.city_id = city.city_id
         join country on city.country_id = country.country_id
         left join rental on customer.customer_id = rental.customer_id
         left join inventory on rental.inventory_id = inventory.inventory_id
         left join film_actor on inventory.film_id = film_actor.film_id
group by address.address, city.city, country.country
having sum(distinct actor_id) > 40

-- 3.21
select film.film_id, film.title, count(distinct address.city_id) as city_amount
from film
         join film_category on film.film_id = film_category.film_id
         join category on film_category.category_id = category.category_id
         left join inventory on film.film_id = inventory.film_id
         left join rental on inventory.inventory_id = rental.inventory_id
         left join customer on rental.customer_id = customer.customer_id
         left join address on customer.address_id = address.address_id
where category.name = 'Horror'
group by film.film_id, film.title

-- 3.22
select customer.customer_id, count(distinct fc.category_id) as category_amount
from customer
         join address on customer.address_id = address.address_id
         join city on address.city_id = city.city_id
         join country on city.country_id = country.country_id
         left join rental on customer.customer_id = rental.customer_id
         left join inventory on rental.inventory_id = inventory.inventory_id
         left join film_category fc on inventory.film_id = fc.film_id
where country.country = 'Poland'
group by customer.customer_id

-- 3.23
select language.name, count(film.film_id) as film_count
from language
         left join film on language.language_id = film.language_id and film.length > 350
group by language.name

-- 3.24
select customer.customer_id, customer.first_name, customer.last_name, coalesce(sum(payment.amount), 0) as amount
from customer
         left join rental on customer.customer_id = rental.customer_id and MONTH(rental_date) = 6
         left join payment on rental.rental_id = payment.rental_id
group by customer.customer_id, customer.first_name, customer.last_name

-- 3.25
select category.category_id, category.name, count(distinct film.film_id) as film_amount
from category
         left join dbo.film_category on category.category_id = film_category.category_id
         left join film on film_category.film_id = film.film_id
         left join language on film.language_id = language.language_id and language.name like 'E%'
group by category.category_id, category.name
order by film_amount

-- 3.26
select film.title
from film
         join inventory on film.film_id = inventory.film_id
         join rental on inventory.inventory_id = rental.inventory_id
         join customer on rental.customer_id = customer.customer_id
where length < 50
  and customer.last_name = 'BELL'
group by film.title
having count(customer.customer_id) = 1
