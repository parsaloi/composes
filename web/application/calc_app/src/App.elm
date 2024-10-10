module App exposing (Model, Msg, init, update, view)

import CalculatorPage
import Html exposing (Html)
import Layout

type alias Model =
    { calculatorModel : CalculatorPage.Model
    }

init : () -> (Model, Cmd Msg)
init _ =
    ( { calculatorModel = CalculatorPage.init }
    , Cmd.none
    )

type Msg
    = CalculatorMsg CalculatorPage.Msg

update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
    case msg of
        CalculatorMsg subMsg ->
            ( { model | calculatorModel = CalculatorPage.update subMsg model.calculatorModel }
            , Cmd.none
            )

view : Model -> Html Msg
view model =
    Layout.view (Html.map CalculatorMsg (CalculatorPage.view model.calculatorModel))
