import Data.List
type Pic = [String]

pic :: Pic
pic = [ "....#....",
        "...###...",
        "..#.#.#..",
        ".#..#..#.",
        "....#....",
        "....#....",
        "....#####"]

pp :: Pic -> IO()
pp x = putStr (unlines x)

flipV :: Pic -> Pic
flipV xs = [reverse x | x <- xs]

flipH :: Pic -> Pic
flipH = reverse

above :: Pic -> Pic -> Pic
above xs ys = xs ++ ys

sideBySide :: Pic -> Pic -> Pic
sideBySide xs ys = [x ++ y | (x, y) <- zip xs ys]



rotateR :: Pic -> Pic
rotateR xs = transpose $ reverse xs

rotateL :: Pic -> Pic
rotateL xs = reverse $ transpose xs

zoom :: Int -> Pic -> Pic
zoom n xs = concat $ [replicate n (concatMap (replicate n) x) | x <- xs]

pic2::Pic
pic2 = [ "#########",
         "#.......#",
         "#.......#",
         "#.......#",
         "#.......#",
         "#.......#",
         "#########"]

superimpose :: Pic -> Pic -> Pic
superimpose [] [] = []
superimpose (x:xs) (y:ys) = tmp x y : superimpose xs ys
  where
    tmp [] _ = []
    tmp _ [] = []
    tmp (n:ns) (m:ms) | n == '#' = n : tmp ns ms
                      | m == '#' = m : tmp ns ms
                      | otherwise = n : tmp ns ms

invertColors :: Pic -> Pic
invertColors [] = []
invertColors (x:xs) = tmp x : invertColors xs
  where
    tmp [] = []
    tmp (n:ns)
      | n == '#' = '.' : tmp ns
      | otherwise = '#' : tmp ns

chessBoard :: Int -> Pic
chessBoard 1 = ["#"]
chessBoard n = tmp n
  where
    tmp 0 = []
    tmp m
      | odd m = take n (cycle ['#', '.']) : tmp (m-1)
      | otherwise = take n (cycle ['.', '#']) : tmp (m-1)


createField :: Int -> Int -> Pic
createField j i = replicate i (replicate j '.')

makePicture j i xs = addHash xs (createField j i)
  where
    addHash [] ys = ys
    addHash ((a,b):xs) ys = 
      let before = take a ys
          changed = replicate b '.' ++ ['#'] ++ replicate (j - (b+1)) '.'
          after = drop (a+1) ys
        in addHash xs (before ++ [changed] ++ after)


