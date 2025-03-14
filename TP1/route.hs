module Route ( Route, newR, inOrderR )
  where

data Route = Rou [ String ] deriving (Eq, Show)

newR :: [ String ] -> Route                    -- construye una ruta segun una lista de ciudades
newR = Rou

inOrderR :: Route -> String -> String -> Bool  -- indica si la primer ciudad consultada esta antes que la segunda ciudad en la ruta
inOrderR (Rou []) _ _ = False
inOrderR (Rou (c:cs)) city1 city2
  | c == city1 = True  -- Encontramos city1 antes que city2
  | c == city2 = False -- Encontramos city2 antes que city1
  | otherwise  = inOrderR (Rou cs) city1 city2

  -- ver casos extra: ciudad_n y ciudad_n u otros errores