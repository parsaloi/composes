module Components.IconButton exposing (view)

import Html exposing (Html, button, text)
import Html.Attributes exposing (class, style)
import Html.Events exposing (onClick)
import Theme

type alias IconButtonConfig msg =
    { onClick : msg
    , icon : String
    , label : String
    }

view : IconButtonConfig msg -> Html msg
view config =
    button
        [ class "icon-button"
        , onClick config.onClick
        , style "background-color" Theme.colors.primary
        , style "color" "white"
        ]
        [ Html.i [ class ("icon " ++ config.icon) ] []
        , text config.label
        ]
