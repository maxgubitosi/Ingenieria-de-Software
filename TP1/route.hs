module Route (Route, newR, inOrderR, inRouteR)
  where

data Route = Rou [ String ] 
  deriving (Eq, Show)

newR :: [ String ] -> Route                    -- construye una ruta segun una lista de ciudades
newR []     = error "No se puede crear una ruta sin ciudades"
newR cities = Rou cities

inOrderR :: Route -> String -> String -> Bool     -- indica si la primer ciudad consultada esta antes que la segunda ciudad en la ruta
inOrderR r@(Rou cities) city1 city2
  | not (inRouteR r city1) = error (city1 ++ " no está en la ruta. Usar inRouteR para checkear")
  | not (inRouteR r city2) = error (city2 ++ " no está en la ruta. Usar inRouteR para checkear")
  | city1 == city2         = True  -- si son la misma ciudad queremos permitir que se considere antes que sí misma, (para permitir agregar palets)
  | otherwise              = inOrder_ cities
  where
    inOrder_ (c:cs)
      | c == city1 = True 
      | c == city2 = False
      | otherwise  = inOrder_ cs
-- Nota de implementación: Decidimos añadir checkeo (con inRouteR) en inOrderR.
-- Esto es redundante si el usuario usa inRouteR de antemano,
-- pero asi garantizamos no propagar errores en caso de uso inadecuado (si el usuario usa ciudades que no están en la ruta)
-- Por ejemplo, sin un checkeo, inOrderR (Rou ["Roma"]) "Roma" "Paris" podría retornar True, lo cual podría propagar errores.
-- Consideramos que el caso de uso de este modulo es en un contexto donde la eficiencia óptima no es crítica, 
-- y es más valioso tener un chequeo más robusto

inRouteR :: Route -> String -> Bool -- indica si la ciudad consultada está en la ruta
inRouteR (Rou []) _ = False
inRouteR (Rou (c:cs)) city
  | c == city = True
  | otherwise = inRouteR (Rou cs) city
