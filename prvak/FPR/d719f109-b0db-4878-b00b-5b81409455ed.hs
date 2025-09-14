
factorial :: (Num a, Ord a) => a -> a
factorial n | n == 0 = 1
            | otherwise = tmp n 1 1 where
                tmp n x y
                        | x <= n = tmp n (x+1) ((x)*y)
                        | otherwise = y
factorial' :: (Eq t, Num t) => t -> t
factorial' n
            | n == 0 = 1
            | otherwise = n*factorial' (n-1)

fib :: (Eq t, Num t, Num a) => t -> a
fib n
    | n == 0 = 0
    | n == 1 = 1
    | otherwise = tmp n 0 1 where
        tmp n x y
            | n == 0 = x
            | otherwise = tmp (n-1) y (x+y)

-- Vytorte funkci, ktera spocita pocet vyskytu znaku, zadaneho jako parameyt, v zadanem retezci
countIt :: Char-> String -> Int
countIt x y = tmp 0 x y where
                tmp n _ [] = n
                tmp n x (y:xs)
                        | x == y = tmp (n+1) x xs
                        | otherwise = tmp n x xs

-- Create a function that computes length of a list
length':: [a] -> Int
length' x = tmp x 0 where
            tmp [] n = n
            tmp (_:xs) n = tmp xs (n+1)

-- Create a function that sums the list of integers
sumIt:: [Int] -> Int
sumIt x = tmp x 0 where
            tmp [] n = n
            tmp (x:xs) n = tmp xs (x+n)

-- Create a function that returns the first element in the list.
getHead:: [a] -> a
getHead (x:_) = x

-- Create a function that returns the last element in the list.
getLast :: [a] -> a
getLast (x:xs) = tmp x xs where
                tmp x [] = x
                tmp _ (y:ys) = tmp y ys
getLast' [x] = x
getLast' (_:xs) = getLast' xs

-- Create a function that checks if an element is a member of the list.
isElement :: Eq a => a -> [a] -> Bool
isElement x y = tmp x y where
                    tmp x [] = False
                    tmp x (y:ys)   | x == y = True
                                    | otherwise = tmp x ys

-- Create a function that returns the list without the first element.
getTail :: [a] -> [a]
getTail (x:xs) = xs

-- Create a function that returns the list without the last element.
getInit :: [Int] -> [Int]
getInit [_] = []
getInit (x:xs) = x:getInit xs

-- Create a function that merge two lists into one list.
combine :: [a] -> [a] -> [a]
combine x y = x ++ y

-- Create a function that finds the maximum in the list of integers.
max' :: [Int] -> Int
max' (x:xs) = tmp x xs where
                tmp x [] = x
                tmp x (y:ys)    | y > x = tmp y ys
                                | otherwise = tmp x ys

-- Create a function that reverse a list.
reverse' :: [a] -> [a]
reverse' x = tmp x [] where
                tmp [] y = y
                tmp (x:xs) y = tmp xs (x:y)

-- Create a function that product scalar multiplication of two vectors.
scalar :: Num t => [t] -> [t] -> t
scalar x y = tmp 0 x y where
                tmp n [] [] = n
                tmp n (x:xs) (y:ys) = tmp (n+ x*y) xs ys

-- Create a function that eliminates all occurrences of zeros in a list.
nonZeros :: (Eq a, Num a) => [a] -> [a]
nonZeros [] = []
nonZeros (x:xs) | x == 0 = nonZeros xs
                | otherwise = x : nonZeros xs

-- Create a function that realizes the left rotation of a list by one element.
rotateLeft :: [a] -> [a]
rotateLeft (x:xs) = xs ++ [x]

-- Create a function that realizes the right rotation of a list by one element.
rotateRight :: [a] -> [a]
rotateRight x = tmp x [] where
                    tmp [x] y = x:y
                    tmp (x:xs) y = tmp xs (y ++ [x])

-- Create a function that eliminates all even numbers from a list.
oddMembers :: [Int] -> [Int]
oddMembers x = tmp x [] where
                    tmp [] y = y
                    tmp (y:ys) z    | y == 0 = tmp ys z

-- removeOne :: Eq a => a -> [a] -> [a]
removeOne n x = [y | x /= n, y <- x]