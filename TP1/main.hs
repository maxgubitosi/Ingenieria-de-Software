import Stack
import Palet
import Route

main :: IO ()
main = do
    let stack1 = newS 3
    let p1 = newP "Buenos Aires" 4
    let p2 = newP "Rosario" 6
    let p3 = newP "Córdoba" 5
    let stack2 = stackS stack1 p1
    let stack3 = stackS stack2 p2
    let stack4 = stackS stack3 p3

    let route = newR ["Buenos Aires", "Rosario", "Córdoba", "Mendoza"]
    
    -- test Palet
    print "=== Palet tests ==="
    print $ inOrderR route "Buenos Aires" "Rosario"  -- True
    print $ inOrderR route "Rosario" "Buenos Aires"  -- False
    print $ inOrderR route "Córdoba" "Mendoza"       -- True
    print $ inOrderR route "Mendoza" "Córdoba"       -- False
    print $ inOrderR route "Salta" "Córdoba"         -- False

    -- test Stack
    print "=== Stack basic tests ==="
    print stack1  -- Pila vacía
    print stack2  -- Pila con un palet
    print stack3  -- Pila con dos palets
    print stack4  -- Pila con tres palets
    print $ freeCellsS stack4  -- Debería mostrar cuántas celdas libres quedan (0)
    -- test netS
    print "=== netS tests ==="
    print $ netS stack1  -- 0 (pila vacía)
    print $ netS stack2  -- 4 (solo p1)
    print $ netS stack3  -- 4 + 6 = 10
    print $ netS stack4  -- 4 + 6 + 5 = 15
    -- test popS
    print "=== popS tests ==="
    print $ popS stack4 "Rosario"  -- Debería quitar p2 (Rosario)
    print $ popS stack4 "Buenos Aires"  -- Debería quitar p1 (Buenos Aires)
    print $ popS stack4 "Córdoba"  -- Debería quitar p3 (Córdoba)
    print $ popS stack4 "Salta"    -- No cambia nada, ciudad inexistente

-- === Stack tests ===
-- Sta [] 3
-- Sta [Pal "Buenos Aires" 4] 3
-- Sta [Pal "Rosario" 6,Pal "Buenos Aires" 4] 3
-- Sta [Pal "Córdoba" 5,Pal "Rosario" 6,Pal "Buenos Aires" 4] 3
-- 0
-- === Palet tests ===
-- True
-- False
-- True
-- False
-- False
-- === netS tests ===
-- 0
-- 4
-- 10
-- 15
-- === popS tests ===
-- Sta [Pal "Córdoba" 5,Pal "Buenos Aires" 4] 3
-- Sta [Pal "Córdoba" 5,Pal "Rosario" 6] 3
-- Sta [Pal "Rosario" 6,Pal "Buenos Aires" 4] 3
-- Sta [Pal "Córdoba" 5,Pal "Rosario" 6,Pal "Buenos Aires" 4] 3