import Data.List (sort, maximumBy)

-- === Uloha 3 (10b) === --
-- Vytvorte funkci, ktera dostane jako parametry dva setrizene seznamy.
-- Tyto seznamy spoji do jednoho setrizeneho seznamu

-- merge [1,3,5,6] [2,3,8] = [1,2,3,5,6,8]
merge :: (Ord a) => [a] -> [a] -> [a]
merge xs ys = sort ([x | x <- xs, x `notElem` ys] ++ ys)

-- === Uloha 3 (10b) === --
-- Vytvorte funkci, ktera ma dva parametry. Prvni reprezentuje vstup
-- a druhy seznam oddelovacu. Funkce rozdeli prvni sekvenci dle zadanych oddelovacu.
-- Pokud je vice elementu z mnoziny oddelovacu za sebou, berou se jako jeden oddelovac

-- splitWith "Functional programming\n and Haskell!!!" " \n!" =
-- ["Functional", "programming", "and", "Haskell"]
splitWith :: (Eq a) => [a] -> [a] -> [[a]]
splitWith [] _ = []
splitWith xs ys =
  let word = takeWhile (`notElem` ys) xs
      removed_word = drop (length word) xs
      removed_spacer = dropWhile (`elem` ys) removed_word
    in word : splitWith removed_spacer ys

-- === Uloha 3 === --
-- Vytvorte funkci, ktera na vstupu dostane seznam prvku a hash funkci.
-- Tato hash funkce pro kazdy prvek ze vstupu vrati nejakou celociselnou hodnotu.
-- Vice prvku muze mit stejnou ciselnou hodnotu. Tuto hodnotu oznacime jako klic.
-- Vytvorena funkce rozdeli vstupni sekvenci na seznam dvojic, kde prvni hodnota
-- ve dvojici bude prislusny klic a druha bude seznam prvku s timto klicem

-- Na poradi dvojic ve vysledku nezalezi

-- getHashMap [1..12] (\x -> x `mod` 3) = [(0, [3,6,9,12]), (1, [1,4,7,10]), (2, [2,5,8,11])]
-- getHashMap [1,2,3] (\x -> x) = [(1, [1]), (2, [2]), (3, [3])]

unique :: Ord a => [a] -> [a]
unique [] = []
unique (x:xs) = x : unique (filter (/=x) xs)

getHashMap :: [a] -> (a -> Int) -> [(Int, [a])]
getHashMap xs f = map (\hash -> (hash, [n | n <- xs, f n == hash])) (sort $ unique [f n | n <- xs])

-- === Uloha 3 === --
-- Vytvorte funkci, ktera provede nekolik otoceni (reverse) casti seznamu.
-- Jedna operace, jedno otoceni, je definovano jako dvojice (start, delka),
-- kde prvni clen je pozice definujici zacatek otacene podsekvence
-- (elementy seznamu budou cislovany od 0) a druhy je delka otaceni useku.
-- Funkce ma dva parametry. Prvni je seznam, se kterym budeme pracovat.
-- Druhy je seznam dvojic (start, delka) popisujici jednotlive operace.
-- Vysledek bude seznam po provedeni vsech otoceni

-- change [1,2,3,4,5] [(0,4)] = [4,3,2,1,5]
-- change [1,2,3,4,5] [(0,4),(2,3)] = [4,3,5,1,2] let new =
change :: [a] -> [(Int, Int)] -> [a]
change xs [] = xs
change xs (y:ys) = 
  let before = take (fst y) xs
      without_before = drop (fst y) xs
      reversed = reverse (take (snd y) without_before)
      after = drop (snd y) without_before
    in change (before ++ reversed ++ after) ys

  -- === Uloha 3 === --
-- Napiste funkci, ktera dostane jako parametr tri seznamy a jako vysledek
-- vrati seznam prvku, ktere jsou prave v jednom ze zadanych seznamu

-- Poradi prvku ve vyslednem seznamu nehraje roli

-- unique [1,2,3,4] [3,4,5,6] [1,6,7] = [2,5,7]

unique' :: (Eq a) => [a] -> [a] -> [a] -> [a]
unique' xs ys zs = [x | x <- xs, x `notElem` ys && x `notElem` zs] ++ [y | y <- ys, y `notElem` xs && y `notElem` zs] ++ [z | z <- zs, z `notElem` xs && z `notElem` ys]


-- === Uloha 3 === --
-- Napiste funkci, ktera dostane seznam dvojic, kde kazda dvojice obsahuje
-- text (oznacime ho 'a', bude typu String) a pocet jeho opakovani
-- (oznacime 'n' typu Int). Pro kazdou takovouto dvojici bude vysledkem text,
-- kde se 'n'-krat zopakuje text 'a'. Vysledny text, ktery vytvorena funkce vrati, bude
-- spojenim fragmentu vygenerovanych pro vsechny dvojice ze vstupu (poradi bude stejne
-- jako poradi dvojic na vstupu)

-- buildText [("He", 1), ("l", 2), ("o", 1), ("!", 3)] = "Hello!!!"
buildText :: [(String, Int)] -> String
buildText xs = concat [take (n*length str) (cycle str) | (str, n) <- xs];