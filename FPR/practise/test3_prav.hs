import Data.List (transpose, reverse, sortBy)
import Data.Foldable ( minimumBy )
import Data.Function (on)
type Maze = [String]

printMaze :: Maze -> IO ()
printMaze x = putStr (concat (map (++"\n") x))

sample1 :: Maze
sample1 = ["*********",
           "* *   * *",
           "* * * * *",
           "* * * * *",
           "*   *   *",
           "******* *",
           "        *",
           "*********"]
sample2 :: Maze
sample2 = ["       ",
           "       ",
           "  ***  ",
           "  ***  ",
           "  ***  ",
           "       ",
           "       "]
sample3 :: Maze
sample3 = ["  * *  ",
           " ##### ",
           "  ***  ",
           "  * *  ",
           "  ***  ",
           "     * ",
           "       "]
sample4 :: Maze
sample4 = ["*********",
           "* *   * *",
           "* *   * *",
           "* *   * *",
           "*       *",
           "******* *",
           "        *",
           "*********"]
arrow :: Maze
arrow = [ "....#....",
          "...###...",
          "..#.#.#..",
          ".#..#..#.",
          "....#....",
          "....#....",
          "....#####"]

above :: Maze -> Maze -> Maze
above x y = x ++ y

sideBySide :: Maze -> Maze -> Maze
sideBySide [] y = y
sideBySide x [] = x
sideBySide (x:xs) (y:ys) = (x++y) : sideBySide xs ys

rotateR :: Maze -> Maze
rotateR x = transpose $ reverse x

rotateL :: Maze -> Maze
rotateL x = reverse $ transpose x


getFromMaze :: Maze -> (Int, Int) -> Char
getFromMaze xs (a, b) = (xs !! a) !! b -- pole !! pozice = prvek_na_pozici -- pozice od 0

putIntoMaze :: Maze -> [(Int, Int, Char)] -> Maze
putIntoMaze maze [] = maze
putIntoMaze maze ((a, b, c) : xs) =
  let rows_before = take a maze
      row_current = head $ drop (length rows_before) maze
      char_before = take b row_current
      char_after = drop (length char_before +1) row_current
      row_after = drop (length rows_before +1) maze
      in putIntoMaze (rows_before ++ [char_before ++ [c] ++ char_after] ++ row_after) xs

-- maze, pozice leveho horniho rohu, vyska a sirka
getPart :: Maze -> (Int, Int) -> (Int, Int) -> Maze
getPart maze (x, y) (h, w) =
  let before = drop x maze
      new = take h before
    in map (take w) (map (drop y) new)

findChar :: Char -> Maze -> Int -> (Int, Int)
findChar c (x:xs) n
  | (c `elem` x) = head [(n, i) | i <- [0..length (x)-1], x !! i == c]
  | otherwise = findChar c xs (n+1)



-- maze, current, known_positions, length
solve :: Maze -> (Int, Int) -> [(Int, Int)] -> Int -> Int
solve maze (x, y) seen delka =
  let above | x-1 >=0 = (maze !! (x-1)) !! y
            | otherwise = '*'
      below | x+1 < length maze = (maze !! (x+1)) !! y
            | otherwise = '*'
      left  | y-1 >= 0 = (maze !! x) !! (y-1)
            | otherwise = '*'
      right | y+1 < length (maze !! x) = (maze !! x) !! (y+1)
            | otherwise = '*'
      test_above  | above == ' ' && ((x-1), y) `notElem` seen = solve maze ((x-1), y) ((x,y):seen) (delka+1)
                  | above == 'e' = delka +1
                  | otherwise = 0
      test_below  | below == ' ' && ((x+1), y) `notElem` seen = solve maze ((x+1), y) ((x,y):seen) (delka+1)
                  | below == 'e' = delka +1
                  | otherwise = 0
      test_left   | left == ' ' && (x, (y-1)) `notElem` seen = solve maze (x, (y-1)) ((x,y):seen) (delka+1)
                  | left == 'e' = delka +1
                  | otherwise = 0
      test_right  | right == ' ' && (x, (y+1)) `notElem` seen = solve maze (x, (y+1)) ((x,y):seen) (delka+1)
                  | right == 'e' = delka +1
                  | otherwise = 0
      in if not (any (/= 0) [test_above, test_below, test_left, test_right]) then 0 else minimum $ filter (/= 0) [test_above, test_below, test_left, test_right]

solveMaze :: Maze -> Int
solveMaze maze =
  let start_pos = findChar 's' maze 0
      in solve maze start_pos [] 0


solveNum :: Maze -> (Int, Int) -> (Int, Int) -> [(Int, Int)] -> Int -> [(Int, Int)]
solveNum maze (x, y) end seen delka =
        let above | x-1 >=0 = (maze !! (x-1)) !! y
                  | otherwise = '*'
            below | x+1 < length maze = (maze !! (x+1)) !! y
                  | otherwise = '*'
            left  | y-1 >= 0 = (maze !! x) !! (y-1)
                  | otherwise = '*'
            right | y+1 < length (maze !! x) = (maze !! x) !! (y+1)
                  | otherwise = '*'
            test_above  | above == ' ' && ((x-1), y) `notElem` seen = solveNum maze ((x-1), y) end ((x,y):seen) (delka+1)
                        | ((x-1), y) == end = seen
                        | otherwise = []
            test_below  | below == ' ' && ((x+1), y) `notElem` seen = solveNum maze ((x+1), y) end ((x,y):seen) (delka+1)
                        | ((x+1), y) == end = seen
                        | otherwise = []
            test_left   | left == ' ' && (x, (y-1)) `notElem` seen = solveNum maze (x, (y-1)) end ((x,y):seen) (delka+1)
                        | (x, (y-1)) == end = seen
                        | otherwise = []
            test_right  | right == ' ' && (x, (y+1)) `notElem` seen = solveNum maze (x, (y+1)) end ((x,y):seen) (delka+1)
                        | (x, (y+1)) == end = seen
                        | otherwise = []
            in if all null [test_above, test_below, test_left, test_right] then [] else minimumBy (compare `on` length) ([ n | n <- [test_above, test_below, test_left, test_right], not $ null n])



makePath :: Maze -> (Int, Int) -> (Int, Int) -> Maze
makePath maze start end =
      let path = reverse (solveNum maze start end [] 0)
            in putIntoMaze maze [(fst (path !! i), snd (path !! i), head $ show (i `mod` 10)) | i <- [0..length(path)-1]]