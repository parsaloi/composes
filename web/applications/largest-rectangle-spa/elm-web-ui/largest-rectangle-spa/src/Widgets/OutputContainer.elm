module Widgets.OutputContainer exposing (view)

import Html exposing (Html, div, text)
import Html.Attributes exposing (class, style)
import Theme

view : Maybe Int -> Html msg
view maybeResult =
    div [ class "output-container", style "background-color" "white", style "border-radius" "4px", style "box-shadow" "0 2px 4px rgba(0,0,0,0.1)" ]
        [ case maybeResult of
            Just result ->
                text ("Largest rectangle area: " ++ String.fromInt result)

            Nothing ->
                text "No result yet"
        ]
