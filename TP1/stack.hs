module Stack (Stack, newS, freeCellsS, stackS, netS, holdsS, popS)
  where

import Palet
import Route

data Stack = Sta [ Palet ] Int
  deriving (Eq, Show)

newS :: Int -> Stack                      -- construye una Pila con la capacidad indicada 
newS capacity = Sta [] capacity

freeCellsS :: Stack -> Int                -- responde la celdas disponibles en la pila
freeCellsS (Sta pallets capacity) = capacity - length pallets

stackS :: Stack -> Palet -> Stack         -- apila el palet indicado en la pila
stackS s@(Sta pallets capacity) pallet
  | freeCellsS s == 0 = error "Ya esta lleno"
  | otherwise = Sta (pallet:pallets) capacity

netS :: Stack -> Int                      -- responde el peso neto de los paletes en la pila
netS (Sta pallets _) = sum $ map netP pallets

holdsS :: Stack -> Palet -> Route -> Bool -- indica si la pila puede aceptar el palet considerando las ciudades en la ruta
holdsS (Sta [] capacity) pallet _ = 
  netP pallet <= 10 && capacity >= 1
holdsS (Sta pallets capacity) pallet route =
  let pesoActual = netS (Sta pallets capacity)
      destinoNuevo = destinationP pallet
      destinoTope = destinationP (head pallets)
  in length pallets < capacity
     && pesoActual + netP pallet <= 10
     && inOrderR route destinoNuevo destinoTope

-- Este quita todos independientemente de si estan en el tope ojo
-- popS :: Stack -> String -> Stack          -- quita del tope los paletes con destino en la ciudad indicada
-- popS (Sta pallets capacity) city = Sta (filter (\p -> destinationP p /= city) pallets) capacity

-- Este quita solo los del tope
popS :: Stack -> String -> Stack          -- quita del tope los paletes con destino en la ciudad indicada
popS (Sta [] capacity) _ = Sta [] capacity
popS (Sta (p:ps) capacity) city
  | destinationP p == city = popS (Sta ps capacity) city
  | otherwise = Sta (p:ps) capacity
