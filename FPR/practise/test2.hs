import Data.List (sort, maximumBy)

-- ================ --
-- === Zadani 1 === --
-- ================ --

-- === Uloha 1 (6b) === --
-- mejme seznam dvojic (Nazev statu, Pocet obyvatel), vytvorte funkci countries,
-- ktera vrati vsechny staty, ktere maji pocet obyvatel, vetsi, nez cislo zadane
-- na vstupu jako druhy parametr

-- countries [("CR",10), ("SR", 5), ("USA", 330)] 9 = ["CR", "USA"]
countries :: [(String, Int)] -> Int -> [String]
countries xs n = [stat | (stat, obyvatel) <- xs, obyvatel > n] -- [Vystup | vstup, podminky]

countries' :: [(String, Int)] -> Int -> [String]
countries' xs n = map (fst) (filter (\(stat, obyvatel) -> obyvatel > n) xs) -- filter (podminka) [seznam] 

-- === Uloha 2 (9b) === --
-- Vytvorte funkci complement, tato funkce bude mit tri parametry: dva cela cisla
-- 'a' a 'b' a seznam cisel 'xs'. Funkce vrati vsechna cisla v intervalu 'a' az 'b',
-- ktera nejsou v seznamu xs

-- complement 1 10 [2,4,7,9] = [1,3,5,6,8,10]
complement :: Int -> Int -> [Int] -> [Int]
complement a b xs = [n | n <- [a..b], n `notElem` xs] -- notElem n xs --or-- not(elem)

complement' :: Int -> Int -> [Int] -> [Int]
complement' a b xs = filter (`notElem` xs)[a..b]

-- === Uloha 3 (10b) === --
-- Vytvorte funkci, ktera dostane jako parametry dva setrizene seznamy.
-- Tyto seznamy spoji do jednoho setrizeneho seznamu

-- merge [1,3,5,6] [2,3,8] = [1,2,3,5,6,8]
merge :: (Ord a) => [a] -> [a] -> [a]
merge [] ys = ys
merge xs [] = xs
merge (x:xs) (y:ys) -- 1:2:3:5:6:[8]
  | x < y = x : merge xs (y:ys) -- 1 : 2 : 3 : [] -> [1,2,3]
  | x == y = x : merge xs ys
  | otherwise = y : merge (x:xs) ys

merge' :: Ord a => [a] -> [a] -> [a]
merge' xs ys = sort ([x | x <- xs, x `notElem` ys] ++ ys)

-- ================ --
-- === Zadani 2 === --
-- ================ --

-- === Uloha 1 === --
-- Mejme seznam zbozi reprezentovany dvojicemi (nazev, cena), vratte zbozi,
-- ktere je nejdrazsi

-- select [("jogurt", 15.5), ("mleko", 20), ("maslo", 50)] = "maslo"
select :: [(String, Float)] -> String
select xs = fst $ maximumBy (\(_, a)(_, b) -> compare a b)xs

-- === Uloha 2 (9b) === --
-- Pro vsechna cisla z rozsahu cisel 'a' az 'b', vytvorte seznam,
-- ktery bude pro kazde cislo obsahovat dvojici: (cislo, faktorial cisla).
-- Cisla 'a', 'b' jsou parametry funkce

-- factorials 2 5 = [(2,2), (3,6), (4,24), (5,120)]
factorial :: Int -> Int
factorial 1 = 1
factorial n = n * factorial (n-1)

factorials :: Int -> Int -> [(Int, Int)]
factorials a b = [(n, factorial n) | n <- [a..b]] -- neni podminka

-- === Uloha 3 (10b) === --
-- Vytvorte funkci, ktera ma dva parametry. Prvni reprezentuje vstup
-- a druhy seznam oddelovacu. Funkce rozdeli prvni sekvenci dle zadanych oddelovacu.
-- Pokud je vice elementu z mnoziny oddelovacu za sebou, berou se jako jeden oddelovac

-- splitWith "Functional programming\n and Haskell!!!" " \n!" =
-- ["Functional", "programming", "and", "Haskell"]

splitWith :: (Eq a) => [a] -> [a] -> [[a]]
splitWith [] _ = []
splitWith xs ys =
  let slovo = takeWhile (`notElem` ys) xs       -- takeWhile -> "Functional programming\n and Haskell!!!" -> "Functional"
      zbytek = drop (length slovo) xs           -- "Functional programming\n and Haskell!!!" -> odeber tolik prvku, kolik je delku slova -> " programming\n and Haskell!!!"
      dalsi_veta = dropWhile (`elem` ys)zbytek  -- " programming\n and Haskell!!!" -> "programming\n and Haskell!!!"
    in slovo: splitWith dalsi_veta ys           -- "Functional" : "programming" : "and" : "Haskell" : []

-- ================ --
-- === Zadani 3 === --
-- ================ --

-- === Uloha 1 === --
-- Mejme seznam dvojic (Prijmeni, Zustatek na uctu), vytvorte funkci, ktera vrati
-- prijmeni vsech, kteri maji kladny zustatek na uctu

-- accounts [("A", 100), ("B", -1), ("C", 10)] = ["A", "C"]
accounts :: [(String, Int)] -> [String]
accounts xs = [str | (str, n) <- xs, n > 0]

accounts' :: [(String, Int)] -> [String]
accounts' xs = [fst x | x <- xs, snd x > 0] -- fst - vrati prvni prvek z tuplu, snd - vrati 2. prvek z tuplu

-- === Uloha 2 === --
-- Mejme seznam celych cisel, najdete cislo, ktere se opakuje nejvice krat.
-- Je-li jich vice, staci jedno z nich

-- mostFrequent [1,2,3,1,2,3,1] = 1
unique :: Eq a => [a] -> [a]
unique [] = []
unique (x:xs) = x : unique (filter(/=x)xs) -- 1 : 2 : 3 : []

mostFrequent :: [Int] -> Int
mostFrequent xs = fst $ maximumBy (\(_,a)(_,b) -> compare a b) [(n, length(filter (==n)xs)) | n <- unique xs] 
-- if n = 1 -> filter (==n) xs -> [1,1,1] -> length [1,1,1] = 3

-- === Uloha 3 === --
-- Vytvorte funkci, ktera na vstupu dostane seznam prvku a hash funkci.
-- Tato hash funkce pro kazdy prvek ze vstupu vrati nejakou celociselnou hodnotu.
-- Vice prvku muze mit stejnou ciselnou hodnotu. Tuto hodnotu oznacime jako klic.
-- Vytvorena funkce rozdeli vstupni sekvenci na seznam dvojic, kde prvni hodnota
-- ve dvojici bude prislusny klic a druha bude seznam prvku s timto klicem

-- Na poradi dvojic ve vysledku nezalezi

-- getHashMap [1..12] (\x -> x `mod` 3) = [(0, [3,6,9,12]), (1, [1,4,7,10]), (2, [2,5,8,11])]
-- getHashMap [1,2,3] (\x -> x) = [(1, [1]), (2, [2]), (3, [3])]

getHashMap :: [a] -> (a -> Int) -> [(Int, [a])]
getHashMap xs f = 
  let hashes = sort $ unique [f n | n <- xs] -- vrati vsechny mozne hashe
    in map (\h -> (h, [n | n <- xs, f n == h]))hashes -- projde vsechny hashe a pro kazdy vrati vsechny prvky ze seznamu, jejichz hash odpovida aktualnimu hashi

-- ================ --
-- === Zadani 4 === --
-- ================ --

-- === Uloha 1 === --
-- Mejme nakupni seznam reprezentovany trojicemi (nazev, pocet, cena), prevedte tento seznam
-- na seznam dvojic (nazev, celkova cena), kde celkova cena je dana jako pocet krat cena

-- convert [("jogurt", 4, 15.5), ("mleko", 1, 20), ("maslo", 1, 50)] = [("jogurt", 62), ("mleko", 20), ("maslo", 50)]
convert :: [(String, Int, Float)] -> [(String, Float)]
convert xs = [(nazev, fromIntegral(pocet) * cena) | (nazev, pocet, cena) <- xs]

-- === Uloha 2 === --
-- napiste funkci, ktera v retezci na vstupu, nahradi dany symbol 'x',
-- 'n'-nasobnym opakovanim tohoto symbolu.
-- Textovy retezec, symbol 'x' a pocet opakovani 'n' jsou parametry funkce.

-- replaceByRepeat "programovani" 'r' 2 = prrogrramovani
replaceByRepeat :: String -> Char -> Int -> String
replaceByRepeat (x:xs) c n 
  | x == c = replicate n x ++ replaceByRepeat xs c n
  | otherwise = x : replaceByRepeat xs c n

-- === Uloha 1 === --
-- Mejme Na vstupu seznam dvojic (osoba, vek) a cislo 'n'. Vratte seznam osob,
-- jejichz vek je mensi nez cislo 'n' na vstupu

-- filter' [("James", 40), ("Penny", 20)] 30 = ["Penny"]
filter' :: [(String, Int)] -> Int -> [String]
filter' xs n = [name | (name, age) <- xs, age < n]

-- === Uloha 2 === --
-- Napiste funkci, ktera vrati seznam pozic vyskytu pismene v textovem retezci.
-- Retezec i pismeno jsou parametry funkce. Pocitani pozic v retezci zaciname od 0

-- positions "programovani" 'r' = [1,4]
positions :: String -> Char -> [Int]
positions xs c = [i | i <- [0..length(xs)-1], xs !! i == c]

-- ================ --
-- === Zadani 6 === --
-- ================ --

-- === Uloha 1 === --
-- Mejme na vstupu seznam dvojic (osoba, vek). Vratte prumerny vek lidi v seznamu

-- average [("James", 40), ("Penny", 20), ("Oliver", 33)] = 31.0
average :: [(String, Int)] -> Double
average xs = sum [fromIntegral age | (_, age) <- xs] / fromIntegral (length xs)
-- average xs = [snd x | x <- xs]

-- === Uloha 2 === --
-- Napiste funkci, ktera dostane tri parametry. Prvni je seznam dvojic 'xs'
-- obsahujici klic a hodnota, druhy je klic 'a' a posledni je nova hodnota 'b'.
-- Funkce vrati modifikovany seznam 'xs', kde bude vymenena hodnota vsech
-- prvku s klicem 'a' na novou hodnotu 'b'.

-- changes [(1, "One"), (2, "Two"), (0, "Zero"), (2, "Two")] 2 "Many" 
-- = [(1, "One"), (2, "Many"), (0, "Zero"), (2, "Many")]
changes :: Eq a => [(a,b)] -> a -> b -> [(a, b)]
changes [] _ _ = []
changes ((klic, hodnota):xs) a b 
  | klic == a = (klic, b) : changes xs a b
  | otherwise = (klic, hodnota) : changes xs a b

changes' :: Eq a => [(a, b)] -> a -> b -> [(a, b)]
changes' [] _ _ = []
changes' (x:xs) a b
  | fst x == a = (fst x, b) : changes xs a b
  | otherwise = x : changes xs a b