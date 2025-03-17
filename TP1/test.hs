-- -- para correr en terminal:
-- -- ghci test.hs
-- -- > main

-- -- TODO: hacer bien este testing. 
-- -- hay cosas que nose si testea mal, mas que nada el orden en el que le da las ciudades a los stacks. 
-- -- creo que el orden es el camion va primero a los de mas a la izq de la lista de ruta (porque popea y agrega a izq de la lista de stacks)
-- -- entonces los tests tienen que tener eso en cuenta. 
-- -- ademas fallan algunos


-- test.hs
module Main where

import Control.Exception (tryJust, SomeException, evaluate)
import System.IO.Unsafe  (unsafePerformIO)

import Palet
import Route
import Stack
import Truck

--------------------------------------------------------------------------------
-- 1) Utility: testF for Checking if an Expression Fails
--------------------------------------------------------------------------------

testF :: Show a => a -> Bool
testF action = unsafePerformIO $ do
  result <- tryJust isException (evaluate action)
  return $ case result of
      Left  _ -> True   -- It threw an exception
      Right _ -> False  -- It did NOT throw
  where
    isException :: SomeException -> Maybe ()
    isException _ = Just ()

--------------------------------------------------------------------------------
-- 2) Helpers for Printing Tests
--------------------------------------------------------------------------------

-- Checks equality of actual vs expected
check :: (Eq a, Show a) => String -> a -> a -> IO ()
check label actual expected = do
  putStrLn $ "- " ++ label
  putStrLn $ "   got:      " ++ show actual
  putStrLn $ "   expected: " ++ show expected
  putStrLn $ if actual == expected then "   ✔ PASSED\n" else "   ✘ FAILED\n"

-- Specialized for Bool
checkBool :: String -> Bool -> Bool -> IO ()
checkBool = check

-- For expecting a failure (an exception)
checkFail :: String -> Bool -> IO ()
checkFail label didFail =
  putStrLn ( "- " ++ label
          ++ if didFail then "\n   ✔ Expression failed as expected!\n"
                        else "\n   ✘ Expression did NOT fail, but we expected an error!\n"
            )

--------------------------------------------------------------------------------
-- 3) Main Testing
--------------------------------------------------------------------------------

main :: IO ()
main = do
  putStrLn "========================"
  putStrLn "  TESTING: Route"
  putStrLn "========================"
  
  -- (1) Route Tests
  let route1 = newR ["BuenosAires","Rosario","Cordoba"]
  let route2 = newR ["Rome","Paris","Berlin","Madrid"]

  putStrLn ("route1 = " ++ show route1)
  putStrLn ("route2 = " ++ show route2)

  -- Check inRouteR
  checkBool "inRouteR route1 'Rosario'" (inRouteR route1 "Rosario") True
  checkBool "inRouteR route1 'MarDelPlata'" (inRouteR route1 "MarDelPlata") False

  -- Check inOrderR with route1 = [BuenosAires, Rosario, Cordoba]
  --  left -> right means BuenosAires < Rosario < Cordoba
  checkBool "inOrderR route1 'BuenosAires' 'Cordoba'" (inOrderR route1 "BuenosAires" "Cordoba") True
  checkBool "inOrderR route1 'Cordoba' 'BuenosAires'" (inOrderR route1 "Cordoba" "BuenosAires") False

  -- Check that creating an empty route fails
  checkFail "newR [] should fail" (testF (newR []))

  putStrLn "========================"
  putStrLn "  TESTING: Palet"
  putStrLn "========================"
  
  -- (2) Palet Tests
  let palBA  = newP "BuenosAires" 3
  let palRos = newP "Rosario" 5
  let palCor = newP "Cordoba" 4
  let palExtra = newP "Montevideo" 3

  check "destinationP palBA" (destinationP palBA) "BuenosAires"
  check "netP palRos" (netP palRos) 5

  putStrLn $ "palCor = " ++ show palCor
  putStrLn $ "palExtra = " ++ show palExtra

  putStrLn "========================"
  putStrLn "  TESTING: Stack"
  putStrLn "========================"
  
  -- (3) Stack Tests
  let stEmpty = newS 2
  check "freeCellsS stEmpty" (freeCellsS stEmpty) 2
  check "netS stEmpty" (netS stEmpty) 0

  -- Let's stack "BuenosAires"
  checkBool "holdsS stEmpty palBA route1" (holdsS stEmpty palBA route1) True
  let st1 = stackS stEmpty palBA
  check "freeCellsS st1" (freeCellsS st1) 1
  check "netS st1" (netS st1) 3

  -- Now top is "BuenosAires". If we try "Rosario" next:
  --  route1 says BuenosAires < Rosario => inOrderR route1 "Rosario" "BuenosAires"?
  --  That should be False, because "BuenosAires" appears first in route => "BuenosAires" < "Rosario".
  --  We want the new city to be "left or same" => i.e. if the top is an earlier city, it fails.
  --  Let's see if your code expects that to fail or pass:
  let canStackRos = holdsS st1 palRos route1
  checkBool "holdsS st1 (top=BuenosAires) palRos => Should fail if route is left->right" canStackRos False

  -- Let's see if "BuenosAires" on top of "BuenosAires" is allowed (same city).
  let palBA2 = newP "BuenosAires" 2
  let canStackBA2 = holdsS st1 palBA2 route1
  checkBool "holdsS st1 (top=BuenosAires) palBA2 => same city => should be True" canStackBA2 True

  -- Actually stack that second BA pallet
  let st2 = if canStackBA2 then stackS st1 palBA2 else st1
  check "freeCellsS st2" (freeCellsS st2) 0
  check "netS st2" (netS st2) 5

  -- st2 is now full. Attempting to stack anything else should fail:
  checkFail "stackS st2 palRos => fails because capacity is full" (testF (stackS st2 palRos))

  -- pop from st2 with city=BuenosAires
  let st3 = popS st2 "BuenosAires"
  putStrLn $ "After popS st2 BuenosAires => " ++ show st3
  check "freeCellsS st3" (freeCellsS st3) 2
  check "netS st3" (netS st3) 0

  -- Now let's do a second scenario: If we first stack "Cordoba", then try "Rosario":
  let stC = stackS (newS 2) palCor  -- top= Cordoba
  let canStackRosOverCor = holdsS stC palRos route1
  checkBool "holdsS stC (top=Cordoba) palRos => should it pass or fail?" canStackRosOverCor False
  

  putStrLn "========================"
  putStrLn "  TESTING: Truck"
  putStrLn "========================"
  
  -- (4) Truck Tests
  let truck1 = newT 2 2 route1
  check "freeCellsT truck1" (freeCellsT truck1) 4  -- 2 bays of capacity 2 => total 4

  -- Load "BuenosAires"
  let truck2 = loadT truck1 palBA
  check "freeCellsT truck2" (freeCellsT truck2) 3
  check "netT truck2" (netT truck2) 3

  -- Attempt to load "Rosario": we expect fails if the top is "BuenosAires"
  -- but there's a second empty bay, so the truck code might choose the second bay.
  -- If it tries the first bay, holdsS fails => it moves to second bay => success:
  let singleBayTruck = newT 1 2 route1
  let singleBayWithBA = loadT singleBayTruck palBA
  let canLoadRosFails = testF (loadT singleBayWithBA palRos)
    -- Explanation: Force the truck to have only 1 bay with top=BA. This ensures we can't fallback to a second empty bay.
  checkFail "Single-bay truck with top=BuenosAires tries to load Ros => fails" canLoadRosFails

  -- However, in a two-bay truck, there's a second bay. So the pallet might go there:
  let truck3 = loadT truck2 palRos
  check "freeCellsT truck3" (freeCellsT truck3) 2
  check "netT truck3" (netT truck3) 8

  -- Now let's load "Cordoba" (which is right of "Rosario"): 
  --   The first bay top=BuenosAires => that fails. The second bay top=Rosario => that also fails. 
  --   So it won't fit in either bay => error or no change. By your code, it tries each bay in turn, fails => throw error?
  let canLoadCordobaFails = testF (loadT truck3 palCor)
  checkFail "truck3 tries to load Cordoba => fails, no bay accepts city=Cordoba to left of (BuenosAires or Rosario)?" canLoadCordobaFails

  -- Unload "BuenosAires" from truck3
  let truckUnBA = unloadT truck3 "BuenosAires"
  check "freeCellsT truckUnBA" (freeCellsT truckUnBA) 3
  check "netT truckUnBA" (netT truckUnBA) 5  -- Only "Rosario" remains

  -- Now that bay1 is empty, let's see if we can load "Cordoba"
  let truck4 = loadT truckUnBA palCor
  check "freeCellsT truck4" (freeCellsT truck4) 2
  check "netT truck4" (netT truck4) 9

  -- Unload "Rosario"
  let truckUnRos = unloadT truck4 "Rosario"
  check "netT truckUnRos" (netT truckUnRos) 4  -- Only Cordoba left
  check "freeCellsT truckUnRos" (freeCellsT truckUnRos) 3

  putStrLn "\n========================"
  putStrLn "  ALL TESTS DONE!"
  putStrLn "========================"