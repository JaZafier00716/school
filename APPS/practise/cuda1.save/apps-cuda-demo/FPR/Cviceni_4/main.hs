import Data.Char

{-# OPTIONS_GHC -Wno-overlapping-patterns #-}

take' :: Int -> [a] -> [a]
take' 0 x = []
take' _ [] = []
take' n (x : xs) = x : take' (n - 1) xs

drop' :: Int -> [a] -> [a]
drop' 0 xs = xs
drop' _ [] = []
drop' n (_ : xs) = drop' (n - 1) xs

minimum' :: (Ord a) => [a] -> a -- Must be type Ord
minimum' [x] = x
minimum' (x : y : xs)
  | y < x = minimum' (y : xs)
  | otherwise = minimum' (x : xs)

divisors :: Int -> [Int]
divisors n = tmp n
  where
    tmp 0 = []
    tmp x
      | n `mod` x == 0 = x : tmp (x - 1)
      | otherwise = tmp (x - 1)

divisors' :: Int -> [Int]
divisors' n = filter isDivider [1 .. n]
  where
    isDivider x = n `mod` x == 0

divisors'' :: Int -> [Int]
divisors'' n = filter (\x -> n `mod` x == 0) [1 .. n]

divisors''' n = [x | x <- [1 .. n], n `mod` x == 0]

zipThem :: [a] -> [b] -> [(a, b)]
zipThem _ _ = []
zipThem (x : xs) (y : ys) = (x, y) : zipThem xs ys

dotProduct :: [a] -> [b] -> [(a, b)]
dotProduct [] _ = []
dotProduct (x : xs) ys = tmp ys ++ dotProduct xs ys
  where
    tmp [] = []
    tmp (z : zs) = (x, z) : tmp zs

dotProduct' :: [a] -> [b] -> [(a, b)]
dotProduct' xs ys = [(x, y) | x <- xs, y <- ys]

fibonaci :: Int -> Int
fibonaci n = fst (tmp n)
  where
    tmp 0 = (0, 1)
    tmp n = nextItem (tmp (n - 1))
    nextItem (a, b) = (b, a + b)


allToUpper :: String -> String
allToUpper [] = []
allToUpper xs = [toUpper x | x <- xs]

allToUpper' xs = map toUpper xs

qsort :: (Ord a) => [a] -> [a]
qsort [] = []
qsort (x:xs) = let  lp = filter (<x) xs
                    rp = filter (>=x) xs
                in qsort lp ++ [x] ++ qsort rp

stalin_sort [] = []
stalin_sort (x:xs) = stalin_sort (filter (<x) xs) ++ [x]

filterAll :: Eq a => a -> [a] -> [a]
filterAll x xs = filter (/= x) xs

replicate' :: Int -> a -> [a]
replicate' 0 a = []
replicate' n a = a : replicate' (n-1) a

replicate'' :: Int -> a -> [a]
replicate'' n a = map (\(a, b) -> b) [(i, a) | i <- [1..n]]

filterEvenGt7:: [Int] -> [Int]
filterEvenGt7 xs = filter (\x-> x > 7 && x `mod` 2 == 0)
