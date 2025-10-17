-- 1.
-- SELECT c.email FROM customer c WHERE c.active = 0

-- 2
--SELECT top 10 f.title, f.description 
--FROM film f 
--WHERE rating = 'G' ORDER BY f.title DESC
-- desc = descending

-- 3
--SELECT * 
--FROM payment p 
--WHERE year(p.payment_date) >= 2006 AND p.amount < 2

-- 4
--SELECT description
--FROM film
----WHERE rating = 'G' OR rating = 'PG'
--WHERE rating IN('G', 'PG')

-- 5
--SELECT description
--FROM film
--WHERE rating IN('G', 'PG', 'PG-13')

-- 6
--SELECT description
--FROM film
--WHERE rating NOT IN('G', 'PG') OR rating IN('PG-13')

-- 7
--SELECT *
--FROM film
--WHERE length > 50 AND (rental_duration = 3 OR rental_duration = 5)

-- 8
--SELECT title
--FROM film
--WHERE title LIKE '%RAINBOW%' OR title LIKE 'TEXAS%'

-- 9
--SELECT title
--FROM  film
--WHERE description LIKE '%And%' 
--AND length BETWEEN 80 AND 90 
--AND rental_duration % 2 = 1

-- 10
--SELECT DISTINCT special_features
--FROM film
--WHERE replacement_cost BETWEEN 14 AND 16
--ORDER BY special_features 

-- 11
--SELECT *
--FROM film
--WHERE 
--	(rental_duration < 4 AND NOT rating = 'PG') OR 
--	(NOT rental_duration < 4  AND rating = 'PG')

-- 12
--SELECT *
--FROM address
--WHERE postal_code IS NOT NULL

-- 13
--SELECT COUNT(DISTINCT customer_id)
--FROM rental
--WHERE return_date IS NULL

-- 14
--SELECT 
--	payment_id, 
--	year(payment_date) AS payment_year, 
--	month(payment_date) AS payment_month, 
--	day(payment_date) AS payment_day
--FROM payment

-- 15
--SELECT *
--FROM film
--WHERE LEN(title) != 20

-- 16
--SELECT DATEDIFF(MINUTE, rental_date, return_date) AS duration_in_minutes
--FROM rental

-- 17
--SELECT customer_id, CONCAT(first_name, ' ', last_name)
--FROM customer

-- 18
--SELECT COALESCE(postal_code, '(prazdne)')
--FROM address

-- 21
--SELECT COUNT(DISTINCT film_id)
--FROM film

-- 22
--SELECT COUNT(DISTINCT rating)
--FROM film

-- 23
-- SELECT COUNT(postal_code) as filled_postal, COUNT (DISTINCT postal_code) as unique_postal
-- FROM address

-- 24
-- SELECT
--     MAX(rental_duration) as maximal,
--     AVG(rental_duration) as average,
--     MIN(rental_duration) as minimal,
--     SUM(rental_duration) / COUNT(rental_duration) as calculated_average
-- from film


