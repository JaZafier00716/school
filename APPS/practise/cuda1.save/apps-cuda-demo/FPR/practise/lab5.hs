import Data.Bool
import Data.Char (isUpper)

oddList :: Int -> Int -> [Int]
oddList x y = [n | n <- [x .. y], n `mod` 2 == 1]

oddList' :: Int -> Int -> [Int]
oddList' x y = filter (\n -> n `mod` 2 == 1) [x .. y]

removeAllUpper :: String -> String
removeAllUpper str = [x | x <- str, not $ isUpper x]

removeAllUpper' :: String -> String
removeAllUpper' = filter (not . isUpper)

union :: (Eq a) => [a] -> [a] -> [a]
union x y = filter (`notElem` y) x ++ y

union' :: (Eq a) => [a] -> [a] -> [a]
union' x y = [n | n <- x, n `notElem` y] ++ y

intersection :: (Eq a) => [a] -> [a] -> [a]
intersection x y = filter (`elem` y) x

intersection' :: (Eq a) => [a] -> [a] -> [a]
intersection' x y = [n | n <- x, n `elem` y]

unique :: String -> String
unique [] = []
unique (x : xs) = x : unique (filter (/= x) xs)

countThem :: String -> [(Char, Int)]
countThem str =
  let u = unique str
   in [(s, length (filter (== s) str)) | s <- u]

isPrime :: Int -> Bool
isPrime 0 = True
isPrime 1 = True
isPrime n = null [x | x <- [2 .. ceiling $ sqrt $ fromIntegral n], n `mod` x == 0]

goldbach :: Int -> [(Int, Int)]
goldbach n =
  let primes = [x | x <- [2 .. n `div` 2 + 1], isPrime x]
   in [(x, n - x) | x <- primes, isPrime (n - x)]

goldbachList :: Int -> Int -> Int -> [(Int, Int)]
goldbachList a b limit = filter (\(x, _) -> x > limit) [head $ goldbach x | x <- [a .. b], even x]

combinations :: Int -> String -> [String]
combinations 1 xs = [[x] | x <- xs]
combinations n (x : xs)
  | n == length (x : xs) = [x : xs]
  | otherwise = [x : y | y <- combinations (n - 1) xs] ++ combinations n xs

foldrMap :: (a -> b) -> [a] -> [b]
foldrMap f xs = [f n | n <- xs]

foldlConcatMap :: (a -> [b]) -> [a] -> [b]
foldlConcatMap f [x] = f x
foldlConcatMap f (x : xs) = f x ++ foldlConcatMap f xs

partition f xs =
  let l = [n | n <- xs, f n]
   in (l, [n | n <- xs, not $ f n])

split :: [(a, b)] -> ([a], [b])
split xs = ([fst n | n <- xs], [snd m | m <- xs])

divideList :: [a] -> Int -> [[a]]
divideList [] _ = []
divideList list x = take x list : divideList (drop x list) x

sequences :: (Ord a) => [a] -> a -> [[a]]
sequences [] _ = []
sequences list x =
  let l = tmp 0
        where
          tmp n
            | n >= length list = []
            | list !! n > x = list !! n : tmp (n + 1)
            | otherwise = []
   in if null l then sequences (drop 1 list) x else l : sequences (drop (length l) list) x

and' :: Bool -> Bool -> Bool
and' a b = a && b

or' :: Bool -> Bool -> Bool
or' a b = a || b

table :: (Bool -> Bool -> Bool) -> IO ()
table f = let p = map (\n -> f ((n `mod` 2) == 1) ((n `div` 2) `mod` 2 == 1)) [0..3]
              q = tmp 0 p where
                  tmp _ [] = []
                  tmp n (x:xs) = (n `mod` 2 == 1, (n `div` 2) `mod` 2 == 1, x) : tmp (n+1) xs
            in mapM_ (\(a, b, y) -> putStrLn $ show a ++ " " ++ show b ++ " " ++ show y) (reverse q)
