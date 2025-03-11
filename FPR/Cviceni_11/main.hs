data Point = Point {column :: Int, row :: Int} deriving (Show)

data Position = Position {leftTopCorner :: Point, width :: Int, height :: Int}

data Event
  = MouseEvent Point
  | KeyEvent {keyPressed :: Char}
  deriving (Show)

data Component
  = TextBox {name :: String, position :: Position, text :: String}
  | Button {name :: String, position :: Position, text :: String, onClick :: Maybe (Event -> String)}
  | Container {name :: String, children :: [Component]}

instance Show Position where
  show :: Position -> String
  show (Position (Point col row) width height) =
    "("
      ++ show col
      ++ ","
      ++ show row
      ++ ")"
      ++ "["
      ++ show width
      ++ ","
      ++ show height
      ++ "]"

instance Show Component where
  show :: Component -> String
  show = showInd ""
    where
      showInd ind (TextBox name position text) = ind ++ show position ++ "TextBox[" ++ name ++ "]: " ++ text ++ "\n"
      showInd ind (Button name position text _) = ind ++ show position ++ "Button[" ++ name ++ "]: " ++ text ++ "\n"
      showInd ind (Container name children) =
        let header = "Container - " ++ name
            inner = concat [showInd (ind ++ "\t") x | x <- children]
         in ind ++ show header ++ "\n" ++ inner

insertInto :: Component -> String -> Component -> Component
insertInto (Container name children) nameToAdd comp
  | name == nameToAdd = Container name $ children ++ [comp]
  | otherwise = Container name $ map (\c -> insertInto c nameToAdd comp) children
insertInto x _ _ = x

deleteFrom :: Component -> String -> Component
deleteFrom (Container cname children) nameToRemove = Container cname $ [deleteFrom c nameToRemove | c <- children, name c /= nameToRemove]
deleteFrom x _ = x

isInPosition :: Point -> Position -> Bool
isInPosition (Point pCol pRow) (Position (Point cornerCol cornerRow) width height) =
  cornerCol <= pCol && pCol <= cornerCol + width && cornerRow <= pRow && pRow <= cornerRow + height

clickOnButton :: Component -> Event -> Maybe String
clickOnButton (Container name children) event = getFirstOrNothing [clickOnButton c event | c <- children]
clickOnButton (Button _ position _ (Just onClickFn)) (MouseEvent point)
  | isInPosition point position = Just $ onClickFn (MouseEvent point)
  | otherwise = Nothing
clickOnButton _ _ = Nothing

getFirstOrNothing :: [Maybe String] -> Maybe String
getFirstOrNothing [] = Nothing
getFirstOrNothing (Nothing : xs) = getFirstOrNothing xs
getFirstOrNothing (Just x : xs) = Just x

gui :: Component
gui =
  Container
    "My App"
    [ Container
        "Menu"
        [ Button "btn_new" (Position (Point 0 0) 100 20) "New" (Just (\event -> "Clicked on new button.")),
          Button "btn_open" (Position (Point 100 0) 100 20) "Open" Nothing,
          Button "btn_close" (Position (Point 200 0) 100 20) "Close" (Just (\event -> "Clicked on close button."))
        ],
      Container "Body" [TextBox "textbox_1" (Position (Point 0 20) 300 500) "Some text goes here"],
      Container "Footer" []
    ]
