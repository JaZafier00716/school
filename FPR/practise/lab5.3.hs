import Data.Char

-- filter (podminka) list - vraci list prvku, pro ktere plati podminka (\prvek_z_listu -> podminka_pro_prvek)
oddList :: Int -> Int -> [Int]
oddList a b = filter (\n -> n `mod` 2 == 1) [a .. b] -- a < b

oddList' :: Int -> Int -> [Int]
oddList' a b = [n | n <- [a .. b], n `mod` 2 == 1]

removeAllUpper :: String -> String
removeAllUpper = filter isLower -- removeAllUpper list = filter (\x -> isLower x) list

removeAllUpper' :: String -> String
removeAllUpper' list = [n | n <- list, isLower n]

union :: (Eq a) => [a] -> [a] -> [a]
union xs ys = filter (`notElem` ys) xs ++ ys

union' :: (Eq a) => [a] -> [a] -> [a]
union' xs ys = [n | n <- xs, n `notElem` ys] ++ ys

intersection :: (Eq a) => [a] -> [a] -> [a]
intersection xs ys = filter (`elem` ys) xs

intersection' :: (Eq a) => [a] -> [a] -> [a]
intersection' xs ys = [n | n <- xs, n `elem` ys]

unique :: String -> String
unique [] = []
unique (x : xs) = x : unique (filter (/= x) xs)

unique' :: String -> String
unique' [] = []
unique' (x : xs) = x : unique ([n | n <- xs, n /= x])

countThem :: String -> [(Char, Int)]
countThem str =
  let u = unique str -- vytvori mi pole prvku, kde se kazdy znak objevuje pouze jednou
   in [(n, length (filter (== n) str)) | n <- u]

isPrime :: Int -> Bool
isPrime 2 = True
isPrime x = null ([n | n <- [2 .. (ceiling (sqrt (fromIntegral x) :: Double))], x `mod` n == 0])

goldbach :: Int -> [(Int, Int)]
goldbach x = [(n, x - n) | n <- [1 .. (x `div` 2)], isPrime n && isPrime (x - n)]

goldbachList :: Int -> Int-> Int -> [(Int, Int)]
goldbachList a b limit = filter (\(x,_) -> x > limit) [head (goldbach n) | n <- [a..b], even n]

combinations :: Int -> String -> [String]
combinations 1 xs = [[x] | x <- xs]
combinations n (x:xs) | n == length (x:xs) = [x:xs]
                      | otherwise = [x : n | n <- combinations (n-1) xs] ++ combinations n xs

checkol :: String -> Int
checkol [] = 0
checkol (x:xs)
  | x == '(' = 1+ checkol xs
  | x == ')' = -1 + checkol xs
  | otherwise = 0 + checkol xs

check :: String -> Bool
check x
  | checkol x == 0 = True
  | otherwise = False