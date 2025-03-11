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
countThem' str = [(ch, count ch str) | ch <- filteredList str] where
    count ch str = length (filter (==ch) str)