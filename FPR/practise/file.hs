-- data FileType = Image | Executable | SourceCode | TextFile deriving (Show)
-- data Entry
--   = File {name :: String, size :: Int, ftype :: FileType}
--   | Directory {name :: String, entries :: [Entry]}
--   deriving (Show)

-- countSizes :: Entry -> Int
-- countSizes (File _ size _) = size
-- countSizes (Directory _ entries) = sum [countSizes e | e <- entries]

-- directorySizes :: Entry -> [(String, Int)]
-- directorySizes (File _ _ _) = []
-- directorySizes (Directory name entries) = (name, countSizes (Directory name entries)) : concat [directorySizes e | e <- entries]

-- root :: Entry
-- root = Directory "root"
--   [
--     File "logo.jpg" 5000 Image,
--     Directory "classes"
--     [
--       File "notes-fpr.txt" 200 TextFile,
--       File "presentation.jpg" 150 Image,
--       File "first_test.hs" 20 SourceCode,
--       Directory "empty1" []
--     ],
--     Directory "empty1" []
--   -- ]

-- countEmptyDirectories :: Entry -> Int
-- countEmptyDirectories (File _ _ _) = 0
-- countEmptyDirectories (Directory _ []) = 1
-- countEmptyDirectories (Directory _ entries) = sum [countEmptyDirectories e | e <- entries]

-- data Component = TextBox {name::String, text::String}
--                | Button {name::String, value::String}
--                | Container {name::String, children::[Component]}
--                deriving(Show)

-- gui::Component
-- gui = Container "My App" [
--         Container "Menu" [
--             Button "btn_new" "new",
--             Button "btn_open" "open",
--             Button "btn_close" "close"],
--         Container "body" [TextBox "textbox_1" "some text"],
--         Container "footer" []]

-- printPath :: Component -> String -> String
-- printPath (Container name children) cname =
--   let x = concat[printPath c cname | c <- children]
--     in if x == "" then "" else name ++ " / " ++ x
-- printPath c cname
--   | cname == name c = name c
--   | otherwise = ""

-- data Company = Company {
--   name:: String,
--   employees :: Int,
--   ownerOf::[Company]
--   }deriving(Show)

-- firm :: Company
-- firm = Company "ahoj" 15 [
--     Company "cau" 42 [],
--     Company "Hi" 12 [
--       Company "Hello" 14 []
--     ]
--   ]

-- data Component = TextBox {name::String, text::String}
--                 | Button {name::String, value::String}
--                 | Container {name::String, children::[Component]}
--                 deriving(Show)

-- gui::Component
-- gui = Container "My App" [
--         Container "Menu" [
--             Button "btn_new" "new",
--             Button "btn_open" "open",
--             Button "btn_close" "close"],
--         Container "body" [TextBox "textbox_1" "some text"],
--         Container "footer" []]

-- countAllComponents :: Component -> Int
-- countAllComponents (Container _ children) = sum[countThem c | c <- children]
--   where
--     countThem (Container _ children) = 1 + sum[countThem c | c <- children]
--     countThem _ = 1

-- filterEmpty :: [Component] -> [Component]
-- filterEmpty [] = []
-- filterEmpty ((Container _ []) : xs) = filterEmpty xs
-- filterEmpty ((Container name children) : xs) = Container name (filterEmpty children) : filterEmpty xs
-- filterEmpty (x:xs) = x : filterEmpty xs

-- removeEmptyContainers :: Component -> Component
-- removeEmptyContainers (Container name children) = Container name [removeEmptyContainers c | c <- filterEmpty children]
-- removeEmptyContainers x = x

-- data FileType = Image | Executable | SourceCode | TextFile deriving(Show)
-- data Entry
--   = File {name :: String, size :: Int, ftype :: FileType}
--   | Directory {name :: String, entries :: [Entry]} deriving(Show)

-- root :: Entry
-- root = Directory "root"
--   [
--     File "logo.jpg" 5000 Image,
--     Directory "classes"
--       [
--         File "notes-fpr.txt" 200 TextFile,
--         File "presentation.jpg" 150 Image,
--         File "first_test.hs" 20 SourceCode
--       ]
--   ]

-- countLargerFiles :: Int -> Entry -> Int
-- countLargerFiles limit (Directory _ entries) = sum [countLargerFiles limit c | c <- entries]
-- countLargerFiles limit x
--   | size x > limit = 1
--   | otherwise = 0

-- filterNames :: String -> [Entry] -> [Entry]
-- filterNames _ [] = []
-- filterNames cname ((Directory name entries) : xs) = Directory name (filterNames cname entries) : filterNames cname xs
-- filterNames cname (x:xs)
--   | cname == name x = filterNames cname xs
--   | otherwise = x : filterNames cname xs

-- removeFile :: String -> Entry -> Entry
-- removeFile str (Directory name entries) = Directory name [removeFile str c | c <- filterNames str entries]
-- removeFile _ x = x

-- data Entity
--   = Point Double Double
--   | Circle (Double, Double) Int
--   | Box [Entity]

-- something :: Entity
-- something = Box [
--     Point 15 20,
--     Circle (14, 22) 25,
--     Box []
--   ]

-- data FileType = Image | Executable | SourceCode | TextFile deriving(Show)
-- data Entry
--   = File {name :: String, size :: Int, ftype :: FileType}
--   | Directory {name :: String, entries :: [Entry]} deriving(Show)

-- root :: Entry
-- root = Directory "root"
--   [
--     File "logo.jpg" 5000 Image,
--     Directory "classes"
--       [
--         File "notes-fpr.txt" 200 TextFile,
--         File "presentation.jpg" 150 Image,
--         File "first_test.hs" 20 SourceCode,
--         Directory "empty1" []
--       ],
--       Directory "empty2" []
--   ]

-- countSize :: Entry -> Int
-- countSize (Directory name entries) = sum [countSize c | c <- entries]
-- countSize x = size x

-- directorySizes :: Entry -> [(String, Int)]
-- directorySizes (Directory name entries) = (name, countSize (Directory name entries)) : concat [directorySizes c | c <- entries]
-- directorySizes _ = []

-- data TernaryTree a
--   = Branch (TernaryTree a) (TernaryTree a) (TernaryTree a)
--   | Leaf a

-- strom :: TernaryTree Int
-- strom = Leaf 15

-- countEmptyDirectories :: Entry -> Int
-- countEmptyDirectories (Directory _ []) = 1
-- countEmptyDirectories (Directory _ children) = sum [countEmptyDirectories c | c <- children]
-- countEmptyDirectories _ = 0

-- getFiles :: Entry -> String -> [String]
-- getFiles (Directory _ children) x = concat [getFiles c x | c <- children]
-- getFiles x cname =
--   let y = drop (length (name x) - length cname) (name x)
--     in ([name x | y == cname])

-- data Article
--   = Text {text :: String}
--   | Chapter {name :: String, value :: [Article]}

-- idk :: Article
-- idk = Chapter "ahoj" [
--     Text "Nevim co napsat",
--     Chapter "pepik" []
--   ]

-- data FileType = Image | Executable | SourceCode | TextFile deriving(Eq, Show)
-- data Entry
--   = File {name :: String, size :: Int, ftype :: FileType}
--   | Directory {name :: String, entries :: [Entry]} deriving(Show)

-- root :: Entry
-- root = Directory "root"
--   [
--     File "logo.jpg" 5000 Image,
--     Directory "classes"
--       [
--         File "notes-fpr.txt" 200 TextFile,
--         File "presentation.jpg" 150 Image,
--         File "first_test.hs" 20 SourceCode
--       ]
--   ]

-- countDirectories :: Entry -> Int
-- countDirectories (Directory _ children) = 1 + sum [countDirectories c | c <- children]
-- countDirectories _ = 0

-- fimage :: [Entry] -> [Entry]
-- fimage [] = []
-- fimage ((Directory name children) : xs) = Directory name (fimage children) : fimage xs
-- fimage ((File _ _ Image):xs) = fimage xs
-- fimage (x:xs) = x : fimage xs

-- filterImages :: Entry -> Entry
-- filterImages (Directory  name children) = Directory name [filterImages x | x <- fimage children]
-- filterImages x = x

-- data Element
--   = Button {name :: String, text :: String}
--   | Text {text :: String}
--   | Panel [Element]

-- idk :: Element
-- idk = Panel [
--     Button "nevim" "co",
--     Text "napsat, jelikoz pisu test",
--     Panel [
--       Panel []
--     ]
--   ]

-- data Component = TextBox {name::String, text::String}
--                 | Button {name::String, value::String}
--                 | Container {name::String, children::[Component]}
--                 deriving(Show)

-- gui::Component
-- gui = Container "My App" [
--         Container "Menu" [
--             Button "btn_new" "new",
--             Button "btn_open" "open",
--             Button "btn_close" "close"],
--         Container "body" [TextBox "textbox_1" "some text"],
--         Container "footer" []]

-- listAllButtons :: Component -> [Component]
-- listAllButtons (Container _ children) = concat [listAllButtons c | c <- children]
-- listAllButtons (Button name value) = [Button name value]
-- listAllButtons _ = []

-- removeButtons :: [Component] -> [Component]
-- removeButtons [] = []
-- removeButtons ((Container name children) : xs) = Container name (removeButtons children) : removeButtons xs
-- removeButtons ((Button _ _): xs) = removeButtons xs
-- removeButtons (x:xs) = x : removeButtons xs

-- removeAllButtons :: Component -> Component
-- removeAllButtons (Container name children) = Container name [removeAllButtons c | c <- removeButtons children]
-- removeAllButtons x = x

-- data Entity
--   = Point Double Double
--   | Circle Double Double Int
--   | Box [Entity]

-- idk :: Entity
-- idk =
--   Box
--     [ Circle 10 15 20,
--       Point 11.2 14.5,
--       Circle 10.2 11.5 8,
--       Box [],
--       Box
--         [ Point 10 11,
--           Box [],
--           Box [],
--           Box [],
--           Box
--             [ Box [],
--               Box []
--             ]
--         ]
--     ]

-- data Entity
--   = Point {x :: Double, y :: Double}
--   | Circle {x :: Double, y :: Double, r:: Int}
--   | Box {children :: [Entity]}

-- data Company
--   = C {name :: String, employes :: Int, ownerOf :: [Company]}
--   | C2 {name :: String, employes :: Int}

data FileType = Image | Executable | SourceCode | TextFile deriving (Show)

data Entry
  = File {name :: String, size :: Int, ftype :: FileType}
  | Directory {name :: String, entries :: [Entry]}
  deriving (Show)

root :: Entry
root = Directory
    "root"
    [ File "logo.jpg" 5000 Image,
      Directory
        "classes"
        [ File "notes-fpr.txt" 200 TextFile,
          File "presentation.jpg" 150 Image,
          File "first_test.hs" 20 SourceCode
        ]
    ]

countLargeFiles :: Int -> Entry -> Int
countLargeFiles limit (Directory _ children) = sum [countLargeFiles limit c | c <- children]
countLargeFiles limit x -- countLargeFiles limit (File _ size _)
  | size x > limit = 1 --   | size > limit = 1
  | otherwise = 0 --   | otherwise = 0

countSize :: Entry -> Int
countSize (Directory _ children) = foldl1 (+) [countSize c | c <- children] -- sum ...
countSize x = size x

data TernaryTree a
  = Leaf a
  | Branch (TernaryTree a) (TernaryTree a) (TernaryTree a)
  deriving (Show)

strom :: TernaryTree Int
strom =
  Branch
    (Leaf 15)
    ( Branch
        (Leaf 8)
        (Leaf 6)
        ( Branch
            (Leaf 18)
            (Leaf 0)
            (Leaf 4)
        )
    )
    (Leaf 15)

countEmptyDirectories :: Entry -> Int
countEmptyDirectories (Directory _ []) = 1
countEmptyDirectories (Directory _ children) = sum [countEmptyDirectories c | c <- children]
countEmptyDirectories _ = 0

data Article
  = Text {text :: String}
  | Chapter {name' :: String, value :: [Article]}

idk2 :: Article
idk2 =
  Chapter
    { name' = "Ahoj",
      value =
        [ Text {text = "Nevim co napsat"},
          Chapter
            "Pepik"
            [ Text "",
              Chapter "Cau" []
            ]
        ]
    }

--------------------------------------------------------------------------------------
countDirectories :: Entry -> Int
countDirectories (Directory _ children) = 1 + sum[countDirectories c | c <- children]
countDirectories _ = 0

countImage :: Entry -> Int
countImage (Directory _ children) =sum [countImage c | c <- children]
countImage (File _ _ Image) = 1
countImage _ = 0

getImageNames :: Entry -> [String]
getImageNames (Directory _ children) = concat[getImageNames c | c <- children]
getImageNames (File name _ Image) = [name]
getImageNames _ = []
