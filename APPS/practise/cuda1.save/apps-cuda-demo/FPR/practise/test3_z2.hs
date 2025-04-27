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

putIntoMaze :: Maze -> [(Int, Int, Char)] -> Maze
putIntoMaze maze [] = maze
putIntoMaze maze ((a, b, c) : xs) =
  let rows_before = take a maze
      row_current = head $ drop (length rows_before) maze
      char_before = take b row_current
      char_after = drop (length char_before +1) row_current
      row_after = drop (length rows_before +1) maze
      in putIntoMaze (rows_before ++ [char_before ++ [c] ++ char_after] ++ row_after) xs

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
            test_above  | ((x-1), y) == end = end : (x,y) : seen
                        | above == ' ' && ((x-1), y) `notElem` seen = solveNum maze ((x-1), y) end ((x,y):seen) (delka+1)
                        | otherwise = []
            test_below  | ((x+1), y) == end = end : (x,y) : seen
                        | below == ' ' && ((x+1), y) `notElem` seen = solveNum maze ((x+1), y) end ((x,y):seen) (delka+1)
                        | otherwise = []
            test_left   | (x, (y-1)) == end = end : (x,y) : seen
                        | left == ' ' && (x, (y-1)) `notElem` seen = solveNum maze (x, (y-1)) end ((x,y):seen) (delka+1)
                        | otherwise = []
            test_right  | (x, (y+1)) == end = end : (x,y) : seen
                        | right == ' ' && (x, (y+1)) `notElem` seen = solveNum maze (x, (y+1)) end ((x,y):seen) (delka+1)
                        | otherwise = []
            in (if (delka > (length maze * length (maze !! 0))) || all null [test_above, test_below, test_left, test_right] then [] else minimumBy (compare `on` length) ([ n | n <- [test_above, test_below, test_left, test_right], not $ null n]))



makePath :: Maze -> (Int, Int) -> (Int, Int) -> Maze
makePath maze start end =
      let path = reverse (solveNum maze start end [] 0)
            in putIntoMaze maze [(fst (path !! i), snd (path !! i), head $ show (i `mod` 10)) | i <- [0..length (path)-1]]