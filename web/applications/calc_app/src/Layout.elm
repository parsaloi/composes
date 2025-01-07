module Layout exposing (view)

import Html exposing (..)
import Html.Attributes exposing (..)

view : Html msg -> Html msg
view content =
    div 
        [ style "display" "flex"
        , style "justify-content" "center"
        , style "align-items" "center"
        , style "height" "100vh"
        , style "background-color" "#f0f0f0"
        , style "font-family" "Arial, sans-serif"
        ]
        [ div 
            [ style "width" "300px"
            , style "background-color" "#ffffff"
            , style "border-radius" "10px"
            , style "padding" "20px"
            , style "box-shadow" "0 2px 10px rgba(0, 0, 0, 0.1)"
            ]
            [ content ]
        ]
