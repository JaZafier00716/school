import Data.List (transpose, reverse, sortBy)
import Data.Foldable ( minimumBy )
import Data.Function (on)
type Maze = [String]

printMaze :: Maze -> IO ()
printMaze x = putStr (concat (map (++"\n") x))

ms :: Maze
ms = ["*.......",
      ".....#..",
      "..*.*#..",
      "..*.*#..",
      "..***#..",
      ".#####..",
      "......*.",
      "........"]

putIntoMaze :: Maze -> [(Int, Int, Char)] -> Maze
putIntoMaze maze [] = maze
putIntoMaze maze ((a, b, c) : xs) =
  let rows_before = take a maze
      row_current = (maze !! max 0 (length rows_before))
      char_before = take b row_current
      char_after = drop (length char_before +1) row_current
      row_after = drop (length rows_before +1) maze
      in putIntoMaze (rows_before ++ [char_before ++ [c] ++ char_after] ++ row_after) xs

countMines :: Maze -> (Int, Int) -> (Int, Int, Char)
countMines maze (row, col) =
  let above         | row-1 >= 0 = if maze !! (row-1) !! col == '*' then 1 else 0
                    | otherwise = 0
      below         | row+1 < length maze = if maze !! (row+1) !! col == '*' then 1 else 0
                    | otherwise = 0
      right         | col+1 < length (maze!!row) = if maze !! row !! (col+1) == '*' then 1 else 0
                    | otherwise = 0
      left          | col-1 >= 0 = if maze !! row !! (col-1) == '*' then 1 else 0
                    | otherwise = 0
      above_left    | row-1 >= 0 && col-1 >= 0 = if maze !! (row-1) !! (col-1) == '*' then 1 else 0
                    | otherwise = 0
      above_right   | row-1 >= 0 && col+1 < length (maze!!row) = if maze !! (row-1) !! (col+1) == '*' then 1 else 0
                    | otherwise = 0
      below_left    | row+1 < length maze && col-1 >= 0 = if maze !! (row+1) !! (col-1) == '*' then 1 else 0
                    | otherwise = 0
      below_right   | row+1 < length maze && col+1 < length (maze!!row) = if maze !! (row+1) !! (col+1) == '*' then 1 else 0
                    | otherwise = 0
      sum = above + below + right + left + above_left + above_right + below_left + below_right
      in if sum > 0 then (row, col, head $ show sum :: Char) else (row, col, maze !! row !! col)


minesInMaze :: Maze -> Maze
minesInMaze maze = putIntoMaze maze (concat [goThrough i | i <- [0..length maze -1]])
  where
    goThrough i = [countMines maze (i,j) | j <- [0..length (maze !! i)-1], maze !! i !! j == '.']