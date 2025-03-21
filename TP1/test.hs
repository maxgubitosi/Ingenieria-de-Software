module Main where

import Control.Exception (tryJust, SomeException, evaluate)
import System.IO.Unsafe (unsafePerformIO)

import Palet
import Route
import Stack
import Truck

-- testF: retorna True si la expresión tira error, False si no
testF :: Show a => a -> Bool
testF action = unsafePerformIO $ do
    result <- tryJust isException (evaluate action)
    return $ case result of
      Left _  -> True
      Right _ -> False
  where
    isException :: SomeException -> Maybe ()
    isException _ = Just ()

main :: IO ()
main = do

  let rutaCorta = newR ["Roma"]
      rutaLarga = newR ["Roma", "Paris", "Mdq", "Berna"]
      palBA     = newP "BuenosAires" 3
      palRos    = newP "Rosario" 5
      palMdq    = newP "Mdq" 2
      emptyStack= newS 2

  let tests = [
          ("crear ruta vacía falla", testF (newR [])),
          ("inRoute 'Roma' en " ++ show rutaCorta, inRouteR rutaCorta "Roma"),
          ("inRoute 'Paris' en " ++ show rutaCorta ++ " falla", not (inRouteR rutaCorta "Paris")),
          ("inOrder 'Roma' antes que 'Mdq' en "  ++ show rutaLarga, inOrderR rutaLarga "Roma" "Mdq"),
          ("inOrder 'Mdq' antes que 'Roma' en "  ++ show rutaLarga ++ " falla", not (inOrderR rutaLarga "Mdq" "Roma")),
          ("inOrder 'Mdq' antes que 'Mdq' en "  ++ show rutaLarga, inOrderR rutaLarga "Mdq" "Mdq"),
          ("destino de pallet 'BuenosAires'", destinationP (newP "BuenosAires" 3) == "BuenosAires"),
          ("netP de pallet 'Rosario'", netP (newP "Rosario" 5) == 5),
          ("freeCellsS stack vacío", freeCellsS emptyStack == 2),
          ("netS stack vacio", netS emptyStack == 0),
          ("holdsS valido: stack palet Mdq a stack vacío con ruta "  ++ show rutaLarga, holdsS emptyStack palMdq rutaLarga),
          ("holdsS invalido: stack palet Rosario a stack vacío con ruta "  ++ show rutaLarga ++ " falla", not (holdsS emptyStack palRos rutaLarga)),
          ("freeCells post stackS", let st1 = stackS emptyStack palBA in freeCellsS st1 == 1),
          ("netS del stack sube", let st1 = stackS emptyStack palBA in netS st1 == 3),
          ("stackS funciona", let st1 = (stackS (stackS emptyStack palBA) palRos) in freeCellsS st1 == 0),
          ("stackS lleno falla", testF (stackS (stackS (stackS emptyStack palBA) palRos) palRos)),
          ("freeCellsT de truck", freeCellsT (newT 2 2 rutaCorta) == 4),
          ("loadT un truck", let truck1 = loadT (newT 2 2 rutaCorta) (newP "Roma" 3) in netT truck1 == 3),
          ("loadT un truck sobrepeso falla", let truck1 = loadT (newT 2 2 rutaCorta) (newP "Roma" 11) in testF truck1),
          ("loadT un truck sin espacio falla", let truck1 = loadT (loadT (newT 1 1 rutaCorta) (newP "Paris" 3)) (newP "Roma" 3) in testF truck1),
          ("loadT un truck con ciudad no en ruta falla", let truck1 = loadT (newT 2 2 rutaCorta) (newP "Paris" 3) in testF truck1),
          ("loadT un truck con ciudad que esta despues en la ruta que ciudad actual pero solo en un stack funciona", let truck1 = loadT(loadT (newT 2 2 rutaLarga) (newP "Roma" 3)) (newP "Paris" 3) in netT truck1 == 6),
          ("loadT un truck con ciudad que esta despues en la ruta que ciudad actual en ambos stacks falla", testF (loadT (loadT(loadT (newT 2 2 rutaLarga) (newP "Roma" 3)) (newP "Paris" 4)) (newP "Mdq" 3))),
          ("unloadT un truck", let truck1 = unloadT (loadT (newT 2 2 rutaCorta) (newP "Roma" 3)) "Roma" in netT truck1 == 0),
          ("unloadT un truck vacio simplemente no cambia", let truck1 = unloadT (newT 2 2 rutaCorta) "Roma" in netT truck1 == 0)
        ]


  putStrLn "Resultados:"
  mapM_ (\(i, (desc, res)) ->
            putStrLn $ "Test " ++ show i ++ ": \"" ++ desc ++ "\": " ++ (if res then "Pasa" else "No Pasa"))
        (zip [1..] tests)
  

  let passedCount = length (filter snd tests)
  putStrLn $ "\nPasó " ++ show passedCount ++ " tests del total de " ++ show (length tests) ++ "."