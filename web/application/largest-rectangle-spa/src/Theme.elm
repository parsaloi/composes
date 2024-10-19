module Theme exposing (colors, fonts)

colors : { primary : String, secondary : String, background : String, text : String, error : String }
colors =
    { primary = "#4CAF50"
    , secondary = "#2196F3"
    , background = "#f0f0f0"
    , text = "#333333"
    , error = "#f44336"
    }

fonts : { main : String, header : String }
fonts =
    { main = "Arial, sans-serif"
    , header = "Georgia, serif"
    }
