import Data.Char

oddList :: Int -> Int -> [Int]
oddList a b = filter (\x -> x `mod` 2 == 1) [a .. b]

removeAllUpper :: String -> String
removeAllUpper = filter (not . isUpper)

union :: (Eq a) => [a] -> [a] -> [a]
union x y = filter (`notElem` y) x ++ y

intersection :: (Eq a) => [a] -> [a] -> [a]
intersection x y = filter (`elem` y) x

unique :: String -> String
unique [] = []
unique (x : xs) = x : unique (filter (/= x) xs)

countThem str =
  let u = unique str
   in [(s, length (filter (== s) str)) | s <- u]

isPrime :: Int -> Bool
isPrime 2 = True
isPrime x = null [n | n <- [2 .. (ceiling (sqrt (fromIntegral x) :: Double))], x `mod` n == 0]

goldbach :: Int -> [(Int, Int)]
goldbach num = [(x, num - x) | x <- [2 .. (num `div` 2) + 1], isPrime x && isPrime (num - x)]

goldbachList :: Int -> Int -> Int -> [(Int, Int)]
goldbachList a b limit = filter (\(x, _) -> x > limit) [head (goldbach n) | n <- [a .. b], even n]

combinations :: Int -> String -> [String]
combinations 1 xs = [[x] | x <- xs]
combinations n (x : xs)
  | n == length (x : xs) = [(x : xs)] -- if n is equal to the length of the string, return the string
  | otherwise = [[x] ++ y | y <- combinations (n - 1) xs] ++ (combinations n xs)

foldrMap :: (a -> b) -> [a] -> [b]
foldrMap f l = [f n | n <- l]

foldlConcatMap :: (a -> [b]) -> [a] -> [b]
foldlConcatMap f (x:xs) = f x ++ foldlConcatMap f xs

partition :: (a -> Bool) -> [a] -> ([a],[a])
partition f l = ([n | n <- l, f n], [n | n<- l, not $ f n])

split :: [(a,b)] -> ([a],[b])
split xs = ([fst n | n <- xs], [snd n | n <- xs])

divideList :: [a] -> Int -> [[a]]
divideList [] _ = []
divideList xs n = if n <= length (xs) then [take n xs] ++ divideList (drop n xs) n else []

sequences :: Ord a => [a] -> a -> [[a]]
sequences [] _ = []
sequences l x = let a = tmp 0 where
                          tmp n | n >= length l = []
                                | (l !! n) > x = (l !! n) : tmp (n+1)
                                | otherwise = []
                  in if null a then sequences (drop 1 l) x else a : sequences (drop (length a) l) x