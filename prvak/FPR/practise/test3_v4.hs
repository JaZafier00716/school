import Data.List (transpose, reverse, minimumBy)
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
           "*s*   *e*",
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

ms :: Maze
ms = ["*.......",
      ".....#..",
      "..*.*#..",
      "..*.*#..",
      "..***#..",
      ".#####..",
      "......*.",
      "........"
      ]

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
getFromMaze xs (a, b) = (xs !! a) !! b

putIntoMaze :: Maze -> [(Int, Int, Char)] -> Maze
putIntoMaze maze [] = maze
putIntoMaze maze ((a, b, c) : xs) =
  let rows_before = take a maze
      row_current = head $ drop (length rows_before) maze
      char_before = take b row_current
      char_after = drop (length char_before +1) row_current
      row_after = drop (length rows_before +1) maze
      in putIntoMaze (rows_before ++ [char_before ++ [c] ++ char_after] ++ row_after) xs

getPart :: Maze -> (Int, Int) -> (Int, Int) -> Maze
getPart maze (x, y) (h, w) =
  let before = drop x maze
      new = take h before
    in map (take w) (map (drop y) new)

findChar :: Char -> Maze -> Int -> (Int, Int)
findChar c (x:xs) n
  | (c `elem` x) = head [(n, i) | i <- [0..length (x)-1], x !! i == c]
  | otherwise = findChar c xs (n+1)



-- maze, current, known_positions, length -> pocet kroku
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
      test_above  | above == 'e' = delka +1
                  | above == ' ' && ((x-1), y) `notElem` seen = solve maze ((x-1), y) ((x,y):seen) (delka+1)           
                  | otherwise = 0
      test_below  | below == 'e' = delka +1
                  | below == ' ' && ((x+1), y) `notElem` seen = solve maze ((x+1), y) ((x,y):seen) (delka+1)
                  | otherwise = 0
      test_left   | left == 'e' = delka +1
                  | left == ' ' && (x, (y-1)) `notElem` seen = solve maze (x, (y-1)) ((x,y):seen) (delka+1)
                  | otherwise = 0
      test_right  | right == 'e' = delka +1
                  | right == ' ' && (x, (y+1)) `notElem` seen = solve maze (x, (y+1)) ((x,y):seen) (delka+1)
                  | otherwise = 0
      in if not (any (/= 0) [test_above, test_below, test_left, test_right]) then 0 else minimum $ filter (/= 0) [test_above, test_below, test_left, test_right]




solveMaze :: Maze -> Int
solveMaze maze =
  let start_pos = findChar 's' maze 0
      in solve maze start_pos [] 0

mines :: Maze -> (Int, Int) -> Int -> Int
mines maze (x,y) pocet =
      let   above | x-1 >=0 = (maze !! (x-1)) !! y
                  | otherwise = '#'
            aleft | x-1 >=0 = (maze !! (x-1)) !! (y-1)
                  | otherwise = '#'
            arigh | x-1 >=0 = (maze !! (x-1)) !! (y+1)
                  | otherwise = '#'
            below | x+1 < length maze = (maze !! (x+1)) !! y
                  | otherwise = '#'
            bleft | x+1 < length maze = (maze !! (x+1)) !! (y-1)
                  | otherwise = '#'
            brigh | x+1 < length maze = (maze !! (x+1)) !! (y+1)
                  | otherwise = '#'
            left  | y-1 >= 0 = (maze !! x) !! (y-1)
                  | otherwise = '#'
            right | y+1 < length (maze !! x) = (maze !! x) !! (y+1)
                  | otherwise = '#'
            test_above  | above == '*' = pocet + 1
                        | otherwise = 0
            test_aleft  | aleft == '*' = pocet + 1
                        | otherwise = 0
            test_arigh  | arigh == '*' = pocet + 1
                        | otherwise = 0
            test_below  | below == '*' = pocet + 1
                        | otherwise = 0
            test_bleft  | bleft == '*' = pocet + 1
                        | otherwise = 0
            test_brigh  | brigh == '*' = pocet + 1
                        | otherwise = 0
            test_left   | left == '*' = pocet + 1
                        | otherwise = 0
            test_right  | right == '*' = pocet + 1
                        | otherwise = 0
      in test_above + test_aleft + test_arigh + test_below + test_bleft + test_brigh + test_left + test_right

markPath :: Maze -> (Int, Int) -> String -> [(Int, Int)]
markPath maze (x,y) [] = []
markPath maze (x,y) (c:cs)    | c == 'd' = (x+1,y) : markPath maze (x+1,y) cs
                              | c == 'u' = (x-1,y) : markPath maze (x-1,y) cs
                              | c == 'l' = (x,y-1) : markPath maze (x,y-1) cs
                              | c == 'r' = (x,y+1) : markPath maze (x,y+1) cs
                              | otherwise = [(0,0)]

createPath :: Maze -> (Int, Int) -> (Int, Int) -> [(Int, Int)] -> String -> String
createPath maze (x, y) end seen string=
  let above | x-1 >=0 = (maze !! (x-1)) !! y
            | otherwise = '*'
      below | x+1 < length maze = (maze !! (x+1)) !! y
            | otherwise = '*'
      left  | y-1 >= 0 = (maze !! x) !! (y-1)
            | otherwise = '*'
      right | y+1 < length (maze !! x) = (maze !! x) !! (y+1)
            | otherwise = '*'
      test_above  | (x-1,y) == end = 'u':string
                  | above == ' ' && ((x-1), y) `notElem` seen = createPath maze ((x-1), y) end ((x,y):seen) ('u' : string)      
                  | otherwise = []
      test_below  | ((x+1), y) == end = 'd':string
                  | below == ' ' && ((x+1), y) `notElem` seen = createPath maze ((x+1), y) end ((x,y):seen) ('d' : string)  
                  | otherwise = []
      test_left   | (x, (y-1)) == end = 'l':string
                  | left == ' ' && (x, (y-1)) `notElem` seen = createPath maze (x, (y-1)) end ((x,y):seen) ('l' : string)  
                  | otherwise = []
      test_right  | (x, (y+1)) == end = 'r':string
                  | right == ' ' && (x, (y+1)) `notElem` seen = createPath maze (x, (y+1)) end ((x,y):seen) ('r' : string)  
                  | otherwise = []
      in if all null [test_above, test_below, test_left, test_right] then [] else reverse $ minimumBy (compare `on` length) [n | n<- [test_above, test_below, test_left, test_right], not $ null n]
    
getPath :: Maze -> (Int, Int) -> (Int, Int) -> Maze
getPath maze start end = 
  let path = start : markPath maze start (createPath maze start end [] [])
    in putIntoMaze maze [(fst (path !! i), snd (path !! i), head $ show (i `mod` 10)) | i <- [0..length(path)-1]]