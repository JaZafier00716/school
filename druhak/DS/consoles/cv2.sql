-- 2.
-- 1
SELECT ci.city, co.country
FROM city AS ci
JOIN country AS co ON ci.country_id = co.country_id

-- 2
SELECT f.title
FROM language l
JOIN film f
ON f.language_id = l.language_id

-- 3
SELECT rental.rental_id
FROM rental
JOIN customer ON customer.customer_id = rental.customer_id
WHERE customer.last_name LIKE 'SIMPSON'

-- 7
SELECT rental.rental_id, CONCAT(staff.first_name, ' ', staff.last_name) as employee_name, CONCAT(customer.first_name, ' ', customer.last_name) as customer_name, film.title
FROM rental
JOIN staff on staff.staff_id = rental.staff_id
JOIN customer on rental.customer_id = customer.customer_id
JOIN inventory ON inventory.inventory_id = rental.inventory_id
JOIN film ON inventory.film_id = film.film_id

-- 8
SELECT film.title, CONCAT(actor.first_name, ' ', actor.last_name)
FROM film
JOIN film_actor ON film.film_id = film_actor.film_id
JOIN actor ON film_actor.actor_id = actor.actor_id
ORDER BY film.title

-- 10
SELECT film.title
FROM film
JOIN film_category ON film.film_id = film_category.film_id
JOIN category ON category.category_id = film_category.category_id
WHERE category.name LIKE 'Horror'

-- 11
SELECT s.store_id, a1.address, CONCAT(m.first_name, ' ', m.last_name) as manager_name, a2.address
FROM store s
JOIN address a1 ON s.address_id = a1.address_id
JOIN staff m ON s.manager_staff_id = m.staff_id
JOIN address a2 ON m.address_id = a2.address_id

-- 14
SELECT DISTINCT film.title
FROM film
JOIN inventory ON film.film_id = inventory.film_id

-- 16
SELECT CONCAT(customer.first_name, ' ', customer.last_name) as customer_name
FROM customer
JOIN address on customer.address_id = address.address_id
JOIN city on address.city_id = city.city_id
JOIN country on city.country_id = country.country_id
JOIN rental on customer.customer_id = rental.customer_id
JOIN inventory on rental.inventory_id = inventory.inventory_id
JOIN film on inventory.film_id = film.film_id
WHERE country LIKE 'Italy' AND film.title LIKE 'MOTIONS DETAILS'

-- 18
SELECT payment.payment_id, payment.amount, rental.rental_date
FROM payment
LEFT OUTER JOIN rental ON payment.rental_id = rental.rental_id

-- 20
SELECT film.film_id, film.title, l1.name, l2.name
FROM film
JOIN language l1 ON l1.language_id = film.language_id
LEFT OUTER JOIN language l2 ON l2.language_id = film.original_language_id

-- 22
SELECT film.title
FROM film
LEFT JOIN inventory on film.film_id = inventory.film_id
WHERE inventory.film_id IS NULL

-- 25
SELECT customer.customer_id
FROM customer
LEFT JOIN payment on customer.customer_id = payment.customer_id
and (payment.amount > 9 OR payment.payment_id IS NULL)






