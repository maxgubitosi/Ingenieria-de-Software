module Route (Route, newR, inOrderR, inRouteR)
  where

data Route = Rou [ String ] 
  deriving (Eq, Show)

newR :: [ String ] -> Route                    -- construye una ruta segun una lista de ciudades
newR []     = error "No se puede crear una ruta sin ciudades"
newR cities = Rou cities

inOrderR :: Route -> String -> String -> Bool  -- indica si la primer ciudad consultada esta antes que la segunda ciudad en la ruta
inOrderR (Rou []) _ _ = False
inOrderR (Rou (c:cs)) city1 city2             
  | city1 == city2 = True
  | c == city1 = True  -- Encontramos city1 antes que city2
  | c == city2 = False -- Encontramos city2 antes que city1
  | otherwise  = inOrderR (Rou cs) city1 city2
-- TODO: Esto no funciona si solo una ciudad esta en la ruta, o ninguna. Ver que hacer para eso, o simplemente usar siempre inRouteR antes de usar este


inRouteR :: Route -> String -> Bool -- indica si la ciudad consultada está en la ruta
inRouteR (Rou []) _ = False
inRouteR (Rou (c:cs)) city
  | c == city = True
  | otherwise = inRouteR (Rou cs) city
