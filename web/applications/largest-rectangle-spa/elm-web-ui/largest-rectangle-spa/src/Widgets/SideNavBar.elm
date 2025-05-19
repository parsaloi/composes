module Widgets.SideNavBar exposing (view)

import Html exposing (Html, div, ul, li, a, text)
import Html.Attributes exposing (class, href, style)
import Theme

view : Html msg
view =
    div [ class "side-nav-bar", style "background-color" Theme.colors.secondary ]
        [ ul [ style "list-style-type" "none", style "padding" "0" ]
            [ li [] [ a [ href "#", style "color" "white", style "text-decoration" "none" ] [ text "Home" ] ]
            , li [] [ a [ href "#", style "color" "white", style "text-decoration" "none" ] [ text "About" ] ]
            , li [] [ a [ href "#", style "color" "white", style "text-decoration" "none" ] [ text "Contact" ] ]
            ]
        ]
