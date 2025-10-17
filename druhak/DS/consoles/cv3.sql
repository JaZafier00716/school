-- 1
SELECT rating, COUNT(*)
FROM film
GROUP BY rating

-- 2
SELECT customer.customer_id, COUNT(customer.last_name) as last_name_count
FROM customer
GROUP BY customer.customer_id

-- 3 -- customer without payments will be missing
SELECT payment.customer_id, SUM(payment.amount)
FROM payment
GROUP BY payment.customer_id

-- 5
SELECT YEAR(payment_date) as year, MONTH(payment_date) as month, SUM(amount) as amount
FROM payment
GROUP BY YEAR(payment_date), MONTH(payment_date)
ORDER BY YEAR(payment_date), MONTH(payment_date)

-- 6
SELECT store_id, COUNT(*)
FROM inventory i1
GROUP BY store_id
HAVING COUNT(film_id) > 2300

-- 7
SELECT language_id
FROM film
GROUP BY language_id
HAVING MIN(length) > 46

-- 9
SELECT rating, SUM(length)
FROM film
WHERE length < 50
GROUP BY film.rating
HAVING SUM(length) > 250
ORDER BY rating

-- 10
SELECT language_id, COUNT(*)
FROM film
GROUP BY language_id

-- 11
SELECT language.name, COUNT(film.film_id)
FROM FILM
JOIN language on FILM.language_id = language.language_id
GROUP BY language.name

-- 12
SELECT language.name, COUNT(film.film_id)
FROM FILM
RIGHT JOIN language on FILM.language_id = language.language_id
GROUP BY language.name

--12 v2
SELECT language.name, (
    SELECT COUNT(film.film_id)
    FROM film
    WHERE film.language_id = language.language_id
    )
FROM language

-- 14
SELECT customer.customer_id, CONCAT(customer.first_name, ' ', customer.last_name), COUNT(distinct inventory.film_id)
FROM customer
LEFT JOIN rental on customer.customer_id = rental.customer_id
LEFT JOIN inventory on rental.inventory_id = inventory.inventory_id
GROUP BY customer.customer_id, customer.first_name, customer.last_name
ORDER BY customer.customer_id


-- 16
SELECT customer.customer_id,
       SUM(payment.amount) as sum,
       MIN(payment.amount) as min,
       MAX(payment.amount) as max,
       AVG(payment.amount) as avg
FROM customer
LEFT JOIN rental on customer.customer_id = rental.customer_id
LEFT JOIN payment on rental.customer_id = payment.customer_id
GROUP BY customer.customer_id
ORDER BY customer.customer_id

-- 21
SELECT film.film_id, film.title, COUNT(DISTINCT city.city_id) as address_count
FROM film
JOIN film_category on film.film_id = film_category.film_id
JOIN category on film_category.category_id = category.category_id and category.name = 'Horror'
LEFT JOIN inventory on film.film_id = inventory.film_id
LEFT JOIN rental on inventory.inventory_id = rental.inventory_id
LEFT JOIN customer on rental.customer_id = customer.customer_id
LEFT JOIN address on customer.address_id = address.address_id
LEFT JOIN city on address.city_id = city.city_id
GROUP BY film.film_id, film.title
ORDER BY film.title

-- 23
SELECT language.name, COUNT(film_id)
FROM language
LEFT JOIN film on language.language_id = film.language_id
and film.length > 350
GROUP BY language.name

-- 23 v2
SELECT language.name, (
    SELECT COUNT(film_id)
    FROM film
    WHERE film.language_id = language.language_id and film.length > 350
    )
FROM language


