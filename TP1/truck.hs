module Truck (Truck, newT, freeCellsT, loadT, unloadT, netT)
  where

import Palet
import Stack
import Route

data Truck = Tru [Stack] Route 
  deriving (Eq, Show)

newT :: Int -> Int -> Route -> Truck  -- construye un camion según una cantidad de bahias, la altura de las mismas y una ruta
newT numBays stackHeight route
  | numBays <= 0 = error "cantidad de bahías debe ser >0"
  | stackHeight <= 0 = error "altura de bahías debe ser >0"
  | otherwise = Tru (replicate numBays (newS stackHeight)) route

freeCellsT :: Truck -> Int            -- responde la celdas disponibles en el camion
freeCellsT (Tru stacks _) = sum $ map freeCellsS stacks

loadT :: Truck -> Palet -> Truck      -- carga un palet en el camion
loadT (Tru [] _) _ = error "No hay bahías disponibles"
loadT (Tru (s:ss) route) palet
  | holdsS s palet route = Tru (stackS s palet:ss) route
  | otherwise =
    let Tru newstacks _ = loadT (Tru ss route) palet
    in Tru (s:newstacks) route

unloadT :: Truck -> String -> Truck   -- responde un camion al que se le han descargado los paletes que podían descargarse en la ciudad
unloadT (Tru stacks route) city = Tru (map (\s -> popS s city) stacks) route

netT :: Truck -> Int                  -- responde el peso neto en toneladas de los paletes en el camion
netT (Tru stacks _) = sum $ map netS stacks
