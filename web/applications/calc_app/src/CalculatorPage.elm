module CalculatorPage exposing (Model, Msg, init, update, view)

import ArithmeticEngine exposing (Operation(..), calculate)
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (onClick, onInput)
import ErrorMessage exposing (ErrorMessage)

type alias Model =
    { input : String
    , result : Maybe Float
    , error : Maybe ErrorMessage
    }

init : Model
init =
    { input = ""
    , result = Nothing
    , error = Nothing
    }

type Msg
    = UpdateInput String
    | PerformOperation Operation
    | Clear

update : Msg -> Model -> Model
update msg model =
    case msg of
        UpdateInput value ->
            { model | input = value, error = Nothing }

        PerformOperation op ->
            case String.toFloat model.input of
                Just value ->
                    case model.result of
                        Just prevResult ->
                            case calculate op prevResult value of
                                Ok newResult ->
                                    { model | result = Just newResult, input = "", error = Nothing }
                                Err err ->
                                    { model | error = Just err }
                        Nothing ->
                            { model | result = Just value, input = "" }
                Nothing ->
                    { model | error = Just ErrorMessage.InvalidInput }

        Clear ->
            init

view : Model -> Html Msg
view model =
    div [ style "width" "100%" ]
        [ viewDisplay model
        , viewKeypad
        ]

viewDisplay : Model -> Html Msg
viewDisplay model =
    div [ style "margin-bottom" "10px" ]
        [ input
            [ type_ "text"
            , value model.input
            , onInput UpdateInput
            , style "width" "100%"
            , style "text-align" "center"
            , style "margin-bottom" "5px"
            ]
            []
        , div 
            [ style "width" "100%"
            , style "text-align" "center"
            , style "font-size" "24px"
            ] 
            [ text <|
                case model.error of
                    Just err ->
                        ErrorMessage.toString err
                    Nothing ->
                        model.result
                            |> Maybe.map String.fromFloat
                            |> Maybe.withDefault ""
            ]
        ]

viewKeypad : Html Msg
viewKeypad =
    div []
        [ div [ style "display" "flex", style "justify-content" "space-between", style "margin-bottom" "5px" ] 
            (List.map (viewButton PerformOperation) [ Add, Subtract, Multiply, Divide ])
        , div [ style "display" "flex", style "justify-content" "space-between" ] 
            [ viewButton PerformOperation Exponentiate
            , viewClearButton
            ]
        ]

viewButton : (Operation -> Msg) -> Operation -> Html Msg
viewButton toMsg operation =
    button
        [ onClick (toMsg operation)
        , style "background-color" "#c8c8c8"
        , style "border" "none"
        , style "border-radius" "5px"
        , style "padding" "10px"
        , style "width" "60px"
        , style "text-align" "center"
        , style "cursor" "pointer"
        ]
        [ text (operationToString operation) ]

viewClearButton : Html Msg
viewClearButton =
    button
        [ onClick Clear
        , style "background-color" "#ffc8c8"
        , style "border" "none"
        , style "border-radius" "5px"
        , style "padding" "10px"
        , style "width" "60px"
        , style "text-align" "center"
        , style "cursor" "pointer"
        ]
        [ text "C" ]

operationToString : Operation -> String
operationToString operation =
    case operation of
        Add -> "+"
        Subtract -> "-"
        Multiply -> "*"
        Divide -> "/"
        Exponentiate -> "^"
