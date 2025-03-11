data Expr
  = Num Int
  | Add Expr Expr
  | Sub Expr Expr
  | Mul Expr Expr
  | Div Expr Expr
  | Var Char
  deriving (Eq)

eval :: Expr -> Int
eval (Num a) = a
eval (Add a b) = eval a + eval b
eval (Sub a b) = eval a - eval b
eval (Mul a b) = eval a * eval b
eval (Div a b) = eval a `div` eval b

data Operation = Hi | HiDiv | Lo | LoSub | NoOp deriving (Eq)

showExpr :: Expr -> String
showExpr expr = showExpr' expr NoOp

showExpr' :: Expr -> Operation -> String
showExpr' (Num a) _ = show a
showExpr' (Var a) _ = [a]
showExpr' (Add a b) op =
  let
    str = showExpr' a Lo ++ "+" ++ showExpr' b Lo
    in if op == Hi || op == HiDiv || op == LoSub
        then "(" ++ str ++ ")" else str
showExpr' (Sub a b) op =
  let
    str = showExpr' a LoSub ++ "-" ++ showExpr' b LoSub
    in if op == Hi || op == HiDiv || op == Lo
        then "(" ++ str ++ ")" else str
showExpr' (Mul a b) op =
  let
    str = showExpr' a Hi ++ "*" ++ showExpr' b Hi
    in if op == HiDiv then "(" ++ str ++ ")" else str
showExpr' (Div a b) op =
  let
    str = showExpr' a HiDiv ++ "/" ++ showExpr' b HiDiv
    in if op == HiDiv then "(" ++ str ++ ")" else str

instance (Show Expr) where
  show = showExpr


deriv :: Expr-> Char -> Expr
deriv (Num _) _ = Num 0
deriv (Var x) c
  | c == x = Num 1
  | otherwise = Num 0
deriv (Add x y) c = Add (deriv x c) (deriv y c)
deriv (Sub x y) c = Sub (deriv x c) (deriv y c)
deriv (Mul x y) c = Add (Mul x (deriv y c)) (Mul (deriv x c) y)
deriv (Div x y) c = Div (Sub (Mul (deriv x c) y) (Mul x (deriv y c))) (Mul y y) -- Opravit


data Tree a = -- a je hodnota v aktualni bunce
  Leaf a
  | Branch a (Tree a) (Tree a)



sum' :: Tree Int -> Int
sum' (Leaf a) = a
sum' (Branch a left right) = a + sum' left + sum' right

maxTree :: Ord a => Tree a -> a
maxTree (Leaf a) = a
maxTree (Branch a left right) = max a (max (maxTree left) (maxTree right))

getGreaterElements :: Ord a => Tree a -> a -> [a]
getGreaterElements (Leaf a) n = [a | a > n]

getGreaterElements (Branch a left right) n = [a | a > n] ++ getGreaterElements left n ++ getGreaterElements right n

