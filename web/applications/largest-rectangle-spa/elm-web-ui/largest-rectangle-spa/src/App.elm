module App exposing (main)

import Browser
import Html exposing (Html)
import Index
import Layout

main : Program () Index.Model Index.Msg
main =
    Browser.element
        { init = Index.init
        , update = Index.update
        , view = view
        , subscriptions = \_ -> Sub.none
        }

view : Index.Model -> Html Index.Msg
view model =
    Layout.view
        { content = Index.view model
        , title = "Largest Rectangle Calculator (1-based)"
        }
