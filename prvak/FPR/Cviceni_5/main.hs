import Data.Char (isUpper)

oddList :: Integral a => a -> a -> [a]
oddList x y = [n | n <- [x..y], n `mod` 2 == 1]

removeAllUpper :: String -> String
removeAllUpper xs = [x | x <- xs, not (isUpper x)]

union :: Eq a => [a] -> [a] -> [a]
union xs ys = xs ++ [y | y <- ys, not (elem y xs)]

intersection :: Eq a => [a] -> [a] -> [a]
intersection xs ys = [y | y <- ys, elem y xs]

unique :: String -> String
unique [] = []
unique (x:xs) = x : unique (filter (/=x) xs)

countThem :: String -> [(Char, Int)]
countThem xs =  let u = unique xs
                in [(x, length (filter (==x) xs)) | x <- u]

filteredList [] = []
filteredList (x:xs) = x : filteredList (filter (/=x) xs)

countThem' :: String -> [(Char, Int)]
countThem' str = [(ch, count ch str) | ch <- filteredList str] where -- [(znak, pocet_daneho_znaku) | vyber_znaku <- filtrovany_list]
    count ch str = length (filter (==ch) str) -- Spocita pocet vyskytu daneho znaku v retezci

isPrime :: Int -> Bool
isPrime x = tmp (x-1)
    where
        tmp n   | n <= 1 = True
                | x `mod` n == 0 = False
                | otherwise = tmp (n-1)

goldbach :: Int -> [(Int, Int)]
goldbach n = [(x, n-x) | x <- primes n, isPrime (n-x)]
    where
        primes x = [i | i <- [2..x `div` 2], isPrime i]

goldbachList :: Int -> Int-> Int -> [(Int, Int)]
goldbachList a b t = filter (\(x, _) -> x > t) [head (goldbach i) | i <- [a..b], even i]

-- combinations :: Int -> String -> [String]
-- combinations n str = 