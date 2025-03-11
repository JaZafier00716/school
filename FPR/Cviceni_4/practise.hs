length' :: [a] -> Int
length' = tmp 0
  where
    tmp n [] = n
    tmp n (_ : xs) = tmp (n + 1) xs

sumIt :: [Int] -> Int
sumIt [] = 0
sumIt (x : xs) = x + sumIt xs

getHead :: [a] -> a
getHead (x : _) = x

getLast :: [a] -> a
getLast [x] = x
getLast (_ : xs) = getLast xs

isElement :: (Eq a) => a -> [a] -> Bool
isElement _ [] = False
isElement n (x : xs)
  | n == x = True
  | otherwise = isElement n xs

getTail :: [a] -> [a]
getTail (_ : xs) = xs

getInit :: [a] -> [a]
getInit [_] = []
getInit (x : xs) = x : getInit xs

combine :: [a] -> [a] -> [a]
combine x y = x ++ y

max' :: [Int] -> Int
max' [x] = x
max' (x : y : xs)
  | y > x = max' (y : xs)
  | otherwise = max' (x : xs)

reverse' :: [a] -> [a]
reverse' [] = []
reverse' (x : xs) = reverse' xs ++ [x]

scalar :: [Int] -> [Int] -> Int
scalar [x] [y] = x * y
scalar (x : xs) (y : ys) = x * y + scalar xs ys

take' :: Int -> [a] -> [a]
take' 0 _ = []
take' n (x : xs) = x : take' (n - 1) xs

drop' :: Int -> [a] -> [a]
drop' 0 x = x
drop' n (_ : xs) = drop' (n - 1) xs

minimum' :: (Ord a) => [a] -> a
minimum' [x] = x
minimum' (x : y : z)
  | y < x = minimum' (y : z)
  | otherwise = minimum' (x : z)

divisors :: Int -> [Int]
divisors x = tmp x
  where
    tmp 0 = []
    tmp n
      | x `mod` n == 0 = n : tmp (n - 1)
      | otherwise = tmp (n - 1)

divisors'' :: Int -> [Int]
divisors'' n = tmp n where
        tmp 0 = []
        tmp x | n `mod` x == 0 = x: tmp (x-1)
              | otherwise = tmp (x-1)

divisors' :: Int -> [Int]
divisors' x = [n | n <- [1..x], x `mod` n == 0]

countZeros :: [Int] -> Int
countZeros [] = 0
countZeros (x:xs) | x == 0 = 1 + countZeros xs
                  | otherwise = countZeros xs