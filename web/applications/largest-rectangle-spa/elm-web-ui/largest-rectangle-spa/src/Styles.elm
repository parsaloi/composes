module Styles exposing (globalStyles)

import Html exposing (Html)
import Html.Attributes exposing (style)
import Theme

globalStyles : Html msg
globalStyles =
    Html.node "style"
        []
        [ Html.text ("""
            body {
                font-family: """ ++ Theme.fonts.main ++ """;
                margin: 0;
                padding: 0;
                background-color: """ ++ Theme.colors.background ++ """;
                color: """ ++ Theme.colors.text ++ """;
            }
            .layout {
                display: flex;
                flex-direction: column;
                min-height: 100vh;
            }
            .main-content {
                display: flex;
                flex: 1;
            }
            .content {
                flex: 1;
                padding: 20px;
            }
            .icon-button {
                display: flex;
                align-items: center;
                justify-content: center;
                padding: 10px 20px;
                background-color: """ ++ Theme.colors.primary ++ """;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 16px;
                transition: background-color 0.3s ease;
            }
            .icon-button:hover {
                background-color: """ ++ Theme.colors.secondary ++ """;
            }
            .icon {
                margin-right: 10px;
            }
            .output-container {
                margin-top: 20px;
                padding: 20px;
                background-color: white;
                border-radius: 4px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .message-dialog-overlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.5);
                display: flex;
                justify-content: center;
                align-items: center;
            }
            .message-dialog {
                background-color: white;
                padding: 20px;
                border-radius: 4px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
            }
            .close-button {
                margin-top: 10px;
                padding: 5px 10px;
                background-color: """ ++ Theme.colors.error ++ """;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }
            .input-widget {
                margin-bottom: 20px;
            }
            .matrix-input {
                display: flex;
                flex-direction: column;
                gap: 10px;
            }
            .matrix-row {
                display: flex;
                gap: 10px;
            }
            .labelled-input {
                display: flex;
                flex-direction: column;
            }
            .labelled-input label {
                margin-bottom: 5px;
            }
            .labelled-input input {
                padding: 5px;
                border: 1px solid #ccc;
                border-radius: 4px;
            }
            .top-nav-bar {
                background-color: """ ++ Theme.colors.primary ++ """;
                color: white;
                padding: 10px 20px;
            }
            .top-nav-bar h1 {
                margin: 0;
                font-family: """ ++ Theme.fonts.header ++ """;
            }
            .side-nav-bar {
                width: 200px;
                background-color: """ ++ Theme.colors.secondary ++ """;
                padding: 20px;
            }
            .side-nav-bar ul {
                list-style-type: none;
                padding: 0;
            }
            .side-nav-bar li {
                margin-bottom: 10px;
            }
            .side-nav-bar a {
                color: white;
                text-decoration: none;
            }
            .footer {
                background-color: """ ++ Theme.colors.primary ++ """;
                color: white;
                padding: 10px 20px;
                text-align: center;
            }
            .footer a {
                color: white;
            }
        """)
        ]
