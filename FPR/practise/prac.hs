countIt :: Int->[Int]->Int
countIt _ [] = 0
countIt n (x:xs)    | n == x = 1 + countIt n xs
                    | otherwise = countIt n xs

countIt' :: Char -> String -> Int
countIt' _ [] = 0
countIt' n (x:xs)   | n == x = 1 + countIt' n xs
                    | otherwise = countIt' n xs

countZeros :: [Int] -> Int
countZeros [] = 0
countZeros (x:xs)   | x == 0 = 1 + countZeros xs
                    | otherwise = countZeros xs

countPositive :: [Int] -> Int
countPositive [] = 0
countPositive (x:xs)    | x > 0 = 1 + countPositive xs
                        | otherwise = countPositive xs

