package us.istewart.minesweeper.model

data class Field(var type: Int, var minesAround: Int,
                 var isFlagged: Boolean, var isClicked: Boolean)