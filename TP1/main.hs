import Stack
import Palet
import Route

main :: IO ()
main = do
    let stack1 = newS 3
    let p1 = newP "Buenos Aires" 4
    let p2 = newP "Rosario" 6
    let stack2 = stackS stack1 p1
    let stack3 = stackS stack2 p2

    let route = newR ["Buenos Aires", "Rosario", "Córdoba", "Mendoza"]
    
    -- test Stack
    print stack1  -- Pila vacía
    print stack2  -- Pila con un palet
    print stack3  -- Pila con dos palets
    print $ freeCellsS stack3  -- Debería mostrar cuántas celdas libres quedan

    -- test Palet
    print $ inOrderR route "Buenos Aires" "Rosario"  -- True
    print $ inOrderR route "Rosario" "Buenos Aires"  -- False
    print $ inOrderR route "Córdoba" "Mendoza"       -- True
    print $ inOrderR route "Mendoza" "Córdoba"       -- False
    print $ inOrderR route "Salta" "Córdoba"         -- False

    -- output:
    -- Sta [] 3
    -- Sta [Pal "Buenos Aires" 4] 3
    -- Sta [Pal "Rosario" 6,Pal "Buenos Aires" 4] 3
    -- 1  
    -- True
    -- False
    -- True
    -- False
    -- False