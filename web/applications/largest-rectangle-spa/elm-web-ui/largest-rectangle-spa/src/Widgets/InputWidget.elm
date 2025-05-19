module Widgets.InputWidget exposing (view)

import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput, onClick)
import Widgets.LabelledInput as LabelledInput
import Theme

type alias InputWidgetConfig msg =
    { onUpdate : List (List Int) -> msg
    , matrix : List (List Int)
    }

view : InputWidgetConfig msg -> Html msg
view config =
    div [ class "input-widget" ]
        [ h3 [] [ text "Enter Matrix" ]
        , div [ class "matrix-input" ]
            (List.indexedMap
                (\rowIndex row ->
                    div [ class "matrix-row" ]
                        (List.indexedMap
                            (\colIndex value ->
                                LabelledInput.view
                                    { label = "Row " ++ String.fromInt (rowIndex + 1) ++ ", Col " ++ String.fromInt (colIndex + 1)
                                    , value = String.fromInt value
                                    , onInput = \newValue ->
                                        let
                                            newMatrix =
                                                List.indexedMap
                                                    (\r ->
                                                        if r == rowIndex then
                                                            List.indexedMap
                                                                (\c v ->
                                                                    if c == colIndex then
                                                                        Maybe.withDefault 0 (String.toInt newValue)
                                                                    else
                                                                        v
                                                                )
                                                        else
                                                            identity
                                                    )
                                                    config.matrix
                                        in
                                        config.onUpdate newMatrix
                                    }
                            )
                            row
                        )
                )
                config.matrix
            )
        , button 
            [ class "add-button"
            , style "background-color" Theme.colors.primary
            , style "color" "white"
            , onClick (config.onUpdate (config.matrix ++ [ List.repeat (Maybe.withDefault 0 (List.head config.matrix |> Maybe.map List.length)) 0 ]))
            ]
            [ text "Add Row" ]
        , button 
            [ class "add-button"
            , style "background-color" Theme.colors.secondary
            , style "color" "white"
            , onClick (config.onUpdate (List.map (\row -> row ++ [ 0 ]) config.matrix))
            ]
            [ text "Add Column" ]
        ]
