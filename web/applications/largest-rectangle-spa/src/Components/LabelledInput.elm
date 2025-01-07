module Components.LabelledInput exposing (view)

import Html exposing (Html, div, input, label, text)
import Html.Attributes exposing (class, type_, value, style)
import Html.Events exposing (onInput)
import Theme

type alias LabelledInputConfig msg =
    { label : String
    , value : String
    , onInput : String -> msg
    }

view : LabelledInputConfig msg -> Html msg
view config =
    div [ class "labelled-input" ]
        [ label [ style "font-family" Theme.fonts.main, style "color" Theme.colors.text ] [ text config.label ]
        , input
            [ type_ "number"
            , value config.value
            , onInput config.onInput
            , class "input"
            , style "border" ("1px solid " ++ Theme.colors.primary)
            , style "border-radius" "4px"
            , style "padding" "5px"
            ]
            []
        ]
