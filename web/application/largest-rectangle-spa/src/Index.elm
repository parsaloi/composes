module Index exposing (Model, Msg, init, update, view)

import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (onClick)
import Http
import Json.Encode as Encode
import Json.Decode as Decode
import Widgets.InputWidget as InputWidget
import Components.IconButton as IconButton
import Components.OutputContainer as OutputContainer
import Components.MessageDialog as MessageDialog

type alias Model =
    { matrix : List (List Int)
    , result : Maybe Int
    , error : Maybe String
    }

init : () -> ( Model, Cmd Msg )
init _ =
    ( { matrix = [ [ 0 ] ]
      , result = Nothing
      , error = Nothing
      }
    , Cmd.none
    )

type Msg
    = UpdateMatrix (List (List Int))
    | CalculateResult
    | GotResult (Result Http.Error Int)
    | CloseError

update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        UpdateMatrix newMatrix ->
            ( { model | matrix = newMatrix }, Cmd.none )

        CalculateResult ->
            case validateMatrix model.matrix of
                Ok validMatrix ->
                    ( model, calculateLargestRectangle validMatrix )

                Err errorMsg ->
                    ( { model | error = Just errorMsg }, Cmd.none )

        GotResult result ->
            case result of
                Ok area ->
                    ( { model | result = Just area, error = Nothing }, Cmd.none )

                Err _ ->
                    ( { model | error = Just "Awaiting calculation service to be ready. Please try again later." }, Cmd.none )

        CloseError ->
            ( { model | error = Nothing }, Cmd.none )

validateMatrix : List (List Int) -> Result String (List (List Int))
validateMatrix matrix =
    if List.isEmpty matrix then
        Err "Matrix cannot be empty"
    else if not (List.all (\row -> List.length row == List.length (List.head matrix |> Maybe.withDefault [])) matrix) then
        Err "All rows must have the same length"
    else if not (List.all (List.all (\n -> n == 0 || n == 1)) matrix) then
        Err "Matrix should contain only 0's and 1's"
    else
        Ok matrix

calculateLargestRectangle : List (List Int) -> Cmd Msg
calculateLargestRectangle matrix =
    let
        encodedMatrix =
            Encode.list (Encode.list Encode.int) matrix

        body =
            Http.jsonBody encodedMatrix
    in
    Http.post
        { url = "http://localhost:8080/largest-rectangle"
        , body = body
        , expect = Http.expectJson GotResult (Decode.field "largestRectangleArea" Decode.int)
        }

view : Model -> Html Msg
view model =
    div [ class "index-container" ]
        [ InputWidget.view { onUpdate = UpdateMatrix, matrix = model.matrix }
        , IconButton.view { onClick = CalculateResult, icon = "calculate", label = "Calculate" }
        , OutputContainer.view model.result
        , viewErrorDialog model.error
        ]

viewErrorDialog : Maybe String -> Html Msg
viewErrorDialog maybeError =
    case maybeError of
        Just errorMsg ->
            MessageDialog.view
                { message = errorMsg
                , onClose = CloseError
                }

        Nothing ->
            text ""
