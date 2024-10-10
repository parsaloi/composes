module ErrorMessage exposing (ErrorMessage(..), toString)

type ErrorMessage
    = DivideByZero
    | InvalidInput
    | NegativeExponent

toString : ErrorMessage -> String
toString error =
    case error of
        DivideByZero ->
            "Error: Division by zero"
        InvalidInput ->
            "Error: Invalid input"
        NegativeExponent ->
            "Error: Negative exponent not supported"
