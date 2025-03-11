import Data.List (transpose)

not' :: Bool -> Bool
not' True = False
not' False = True

infixl 5 `not'`

and' :: Bool -> Bool -> Bool
and' True True = True
and' _ _ = False

infixl 4 `and'`

or' :: Bool -> Bool -> Bool
or' False False = False
or' _ _ = True

infixl 3 `or'`

nand' :: Bool -> Bool -> Bool
nand' True True = False
nand' _ _ = True

infixl 4 `nand'`

xor' :: Bool -> Bool -> Bool
xor' True False = True
xor' False True = True
xor' _ _ = False

infixl 3 `xor'`

impl' :: Bool -> Bool -> Bool
impl' True False = False
impl' _ _ = True

infixl 2 `impl'`

equ' :: Bool -> Bool -> Bool
equ' True True = True
equ' False False = True
equ' _ _ = False

infixl 6 `equ'`

table :: (Bool -> Bool -> Bool) -> IO ()
table f =
  let c = [(n `mod` 2 == 1, n `div` 2 `mod` 2 == 1) | n <- [0 .. 3]]
   in mapM_ (\(a, b) -> putStrLn (show a ++ " " ++ show b ++ " " ++ show (f a b))) c

-- tablen :: Int -> ([Bool] -> Bool) -> IO ()let c = 
tablen x f = putStr (concat ([nicePrint x ++ "=>\t" ++ show (f x) ++ "\n" | x <- allValues x]))
  where
    nicePrint xs = concat [show x ++ "\t" | x <- xs]
    allValues 1 = [[False], [True]]
    allValues n = [x:y | x <- [False, True], y <- allValues (n-1)]


-- Cviko 7

type Pic = [String]

pic:: Pic
pic = [ "....#....",
        "...###...",
        "..#.#.#..",
        ".#..#..#.",
        "....#....",
        "....#....",
        "....#####"]

pp :: Pic -> IO()
pp xs = putStr (unlines xs)

flipH :: Pic -> Pic
flipH xs = [reverse x | x <- xs]

flipH' :: Pic -> Pic
flipH' = map reverse

flipV :: Pic -> Pic
flipV = reverse

above :: Pic -> Pic -> Pic
above xs ys = xs ++ ys

sideBySide :: Pic -> Pic -> Pic
sideBySide = zipWith (curry (\(a,b) -> a ++ b))

sideBySide' :: Pic -> Pic -> Pic
sideBySide' = zipWith (++)

rotateR :: Pic -> Pic
rotateR xs = map reverse (transpose xs)

rotateL xs = reverse (transpose xs)

zoom :: Int -> Pic -> Pic
zoom n xs = [concat (map (replicate n) x) | x <- concat (map (replicate n) xs)]

pic2::Pic
pic2 = [ "#########",
         "#.......#",
         "#.......#",
         "#.......#",
         "#.......#",
         "#.......#",
         "#########"]
         
superimpose :: Pic -> Pic -> Pic
superimpose xs ys = [tmp x y | x <- xs, y <- ys]
  where
    tmp [] [] = []
    tmp (n:ns) (m:ms) | n == '#' = n : tmp ns ms
                      | otherwise = m : tmp ns ms
