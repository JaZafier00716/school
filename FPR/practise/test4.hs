-- -- data Entity =
-- --   Point {x::Double, y::Double}
-- --   | Circle {x::Double, y:: Double, r::Int}
-- --   | Container [Entity]

-- data Component = TextBox {name :: String, text :: String}
--   | Button {name :: String, value :: String}
--   | Container {name :: String, children :: [Component]}
--   deriving (Show)

-- gui :: Component
-- gui =
--   Container
--     "My App"
--     [ Container
--         "Menu"
--         [ Button "btn_new" "New",
--           Button "btn_open" "Open",
--           Button "btn_close" "Close"
--         ],
--       Container "Body" [TextBox "textbox_1" "Some text goes here"],
--       Container "Footer" []
--     ]

-- countButtons :: Component -> Int
-- countButtons (Container _ children) = sum [countButtons c | c <- children ]
-- countButtons (Button _ _) = 1
-- countButtons _ = 0

-- addElement :: Component -> Component -> String -> Component
-- addElement (Container name children) comp cname
--   | name == cname = Container name (children ++ [comp])
--   | otherwise = Container name [addElement c comp cname | c <- children]
-- addElement x _ _ = x

-- data Attribute = Attribute {name :: String, value :: String}
-- data Tag = {tagName :: String, attributes :: [Attribute], children :: [Tag]}
-- data HTMLDocument = HTMLDocument {tags::[Tag]}

-- data Component =
--   TextBox {name :: String, text :: String}
--   | Button {name :: String, value :: String}
--   | Container {name :: String, children :: [Component]}
--   deriving(Show)

-- listButtonNames :: Component -> [String]
-- listButtonNames (Button name _) = [name]
-- listButtonNames (Container _ children) = concat[listButtonNames c | c <- children]
-- listButtonNames _ = []

-- changeText :: Component -> String -> String -> Component
-- changeText (TextBox name text) tname new
--   | name == tname = TextBox name new
--   | otherwise = TextBox name text
-- changeText (Container name children) tname new = Container name [changeText c tname new | c <- children]
-- changeText x _ _ = x

-- data Element =
--   Button {name :: String, text :: String}
--   | Text {text :: String}
--   | Panel {children :: [Element]}

-- data Component
--   = TextBox {name :: String, text :: String}
--   | Button {name :: String, value :: String}
--   | Container {name :: String, children :: [Component]}
--   deriving (Show)

-- listAllButtons :: Component -> [Component]
-- listAllButtons (Button name value) = [Button name value]
-- listAllButtons (Container _ children) = concat [listAllButtons c | c <- children]
-- listAllButtons _ = []

-- filterButtons :: [Component] -> [Component]
-- filterButtons [] = []
-- filterButtons (Button _ _ : xs) = filterButtons xs
-- filterButtons (x: xs) = x : filterButtons xs

-- removeAllButtons :: Component -> Component
-- removeAllButtons (Container name children) = Container name [removeAllButtons c | c <- filterButtons children]
-- removeAllButtons x = x

-- addComponentToContainerAtIndex :: Component -> Component -> String -> Int -> Component
-- addComponentToContainerAtIndex (Container name children) component cname index
--   | name == cname =
--       let prev = take index children
--           post = drop index children
--        in Container name (prev ++ [component] ++ post)
--   | otherwise = Container name [addComponentToContainerAtIndex c component cname index | c <- children]
-- addComponentToContainerAtIndex x _ _ _ = x

-- data Company = Company {
--   name :: String,
--   employs ::Int,
--   ownerOf::[Company]
--   }

-- data Entity
--   = Point
--       {x :: Double, y :: Double}
--   | Circle
--       {x :: Double, y :: Double, r:: Int}
--   | Box [Entity]


-- data TernaryTree 
--   = Leaf Int
--   | Branch TernaryTree TernaryTree TernaryTree

-- data Article =
--   Document Article
--   | Text String
--   | Chapter {name :: String, value :: [Article]}  

data FileType = Image | Executable | SourceCode | TextFile deriving (Show)
data Entry
  = File {name :: String, size :: Int, ftype :: FileType}
  | Directory {name :: String, entries :: [Entry]}
  deriving (Show)

-- root :: Entry
-- root =
--   Directory
--     "root"
--     [ File "notes-fp.txt" 200 TextFile,
--       File "presentation.jpg" 150 Image,
--       Directory
--         "classes"
--         [ File "first_test.hs" 300 SourceCode
--         ]
--     ]

countSizes :: Entry -> Int
countSizes (File _ size _) = size
countSizes (Directory _ entries) = sum [countSizes e | e <- entries]

directorySizes :: Entry -> [(String, Int)]
directorySizes (File _ _ _) = []
directorySizes (Directory name entries) = (name, countSizes (Directory name entries)) : concat [directorySizes e | e <- entries]

root :: Entry
root = Directory "root"
  [
    File "logo.jpg" 5000 Image,
    Directory "classes"
    [
      File "notes-fpr.txt" 200 TextFile,
      File "presentation.jpg" 150 Image,
      File "first_test.hs" 20 SourceCode,
      Directory "empty1" []
    ],
    Directory "empty1" []
  ]

countEmptyDirectories :: Entry -> Int
countEmptyDirectories (File _ _ _) = 0
countEmptyDirectories (Directory _ []) = 1
countEmptyDirectories (Directory _ entries) = sum [countEmptyDirectories e | e <- entries]