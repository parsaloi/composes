module ArithmeticEngine exposing (Operation(..), calculate)

import ErrorMessage exposing (ErrorMessage(..))

type Operation
    = Add
    | Subtract
    | Multiply
    | Divide
    | Exponentiate

calculate : Operation -> Float -> Float -> Result ErrorMessage Float
calculate operation a b =
    case operation of
        Add ->
            Ok (a + b)
        Subtract ->
            Ok (a - b)
        Multiply ->
            Ok (a * b)
        Divide ->
            if b == 0 then
                Err DivideByZero
            else
                Ok (a / b)
        Exponentiate ->
            if b < 0 then
                Err NegativeExponent
            else
                Ok (a ^ b)
