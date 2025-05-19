module Widgets.Footer exposing (view)

import Html exposing (Html, div, text, a)
import Html.Attributes exposing (class, href, style)
import Theme

view : Html msg
view =
    div [ class "footer", style "background-color" Theme.colors.primary, style "color" "white" ]
        [ text "© 2025 Elvis Parsaloi | "
        , a [ href "https://github.com/parsaloi/composes", style "color" "white" ] [ text "Source Code" ]
        ]
