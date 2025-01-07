module Components.MessageDialog exposing (view)

import Html exposing (Html, div, text, button)
import Html.Attributes exposing (class, style)
import Html.Events exposing (onClick)
import Theme

type alias MessageDialogConfig msg =
    { message : String
    , onClose : msg
    }

view : MessageDialogConfig msg -> Html msg
view config =
    div [ class "message-dialog-overlay", style "background-color" "rgba(0, 0, 0, 0.5)" ]
        [ div [ class "message-dialog", style "background-color" "white", style "border-radius" "4px", style "box-shadow" "0 2px 10px rgba(0, 0, 0, 0.2)" ]
            [ div [ class "message-content", style "color" Theme.colors.text ] [ text config.message ]
            , button 
                [ class "close-button"
                , onClick config.onClose
                , style "background-color" Theme.colors.error
                , style "color" "white"
                , style "border" "none"
                , style "border-radius" "4px"
                , style "cursor" "pointer"
                ] 
                [ text "Close" ]
            ]
        ]
