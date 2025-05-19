module Layout exposing (view)

import Html exposing (..)
import Html.Attributes exposing (..)
import Theme
import Widgets.TopNavBar as TopNavBar
import Widgets.SideNavBar as SideNavBar
import Widgets.Footer as Footer
import Styles

type alias LayoutConfig msg =
    { content : Html msg
    , title : String
    }

view : LayoutConfig msg -> Html msg
view config =
    div [ class "layout" ]
        [ Styles.globalStyles
        , TopNavBar.view config.title
        , div [ class "main-content" ]
            [ SideNavBar.view
            , div [ class "content" ] [ config.content ]
            ]
        , Footer.view
        ]
