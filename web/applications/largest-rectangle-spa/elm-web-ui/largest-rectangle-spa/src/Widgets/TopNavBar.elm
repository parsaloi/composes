module Widgets.TopNavBar exposing (view)

import Html exposing (Html, div, h1, text)
import Html.Attributes exposing (class, style)
import Theme

view : String -> Html msg
view title =
    div [ class "top-nav-bar", style "background-color" Theme.colors.primary, style "color" "white" ]
        [ h1 [ style "font-family" Theme.fonts.header ] [ text title ]
        ]
