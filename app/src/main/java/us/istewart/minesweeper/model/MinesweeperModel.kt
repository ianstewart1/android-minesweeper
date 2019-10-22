package us.istewart.minesweeper.model

import androidx.annotation.DimenRes
import kotlin.random.Random

object MinesweeperModel {

    val SAFE = 0;
    val MINE = 1;

    val DIMENSION = 6
    var NUM_MINES = (DIMENSION * DIMENSION) / 5 - 1

    private val fieldMatrix: Array<Array<Field>>

    init {
        fieldMatrix = Array(DIMENSION) { _ ->
            Array(DIMENSION) { _ ->
                Field(SAFE, 0, false, false)
            }
        }
        generateBombs()
        computeMinesAround()
    }

    fun getMinesAround(x: Int, y: Int) = fieldMatrix[x][y].minesAround

    fun getIsFlagged(x: Int, y: Int) = fieldMatrix[x][y].isFlagged

    fun getIsClicked(x: Int, y: Int) = fieldMatrix[x][y].isClicked

    fun setClicked(x: Int, y: Int) {
        fieldMatrix[x][y].isClicked = true
    }

    fun setFlagged(x: Int, y: Int) {
        fieldMatrix[x][y].isFlagged = true
    }

    fun isBomb(x: Int, y: Int): Boolean {
        return fieldMatrix[x][y].type == MINE
    }

    fun resetGameModel() {
        resetMatrix()
        generateBombs()
        computeMinesAround()
    }

    fun resetMatrix() {
        for (i in 0 until DIMENSION) {
            for (j in 0 until DIMENSION) {
                fieldMatrix[i][j].type = SAFE
                fieldMatrix[i][j].minesAround = 0
                fieldMatrix[i][j].isFlagged = false
                fieldMatrix[i][j].isClicked = false
            }
        }
    }

    fun generateBombs() {
        val bombCoords = List(NUM_MINES * 2) { Random.nextInt(0, DIMENSION) }
        for (i in 0 until NUM_MINES) {
            fieldMatrix[bombCoords[i * 2]][bombCoords[i * 2 + 1]].type = MINE
        }
    }

    fun computeMinesAround() {
        for (x in 0 until DIMENSION) {
            for (y in 0 until DIMENSION) {
                var neighborMines = 0
                if (x - 1 > -1 && y - 1 > -1) if (fieldMatrix[x - 1][y - 1].type == MINE) neighborMines += 1
                if (y - 1 > -1) if (fieldMatrix[x][y - 1].type == MINE) neighborMines += 1
                if (x + 1 < DIMENSION && y - 1 > -1) if (fieldMatrix[x + 1][y - 1].type == MINE) neighborMines += 1
                if (x + 1 < DIMENSION) if (fieldMatrix[x + 1][y].type == MINE) neighborMines += 1
                if (x + 1 < DIMENSION && y + 1 < DIMENSION) if (fieldMatrix[x + 1][y + 1].type == MINE) neighborMines += 1
                if (y + 1 < DIMENSION) if (fieldMatrix[x][y + 1].type == MINE) neighborMines += 1
                if (x - 1 > -1 && y + 1 < DIMENSION) if (fieldMatrix[x - 1][y + 1].type == MINE) neighborMines += 1
                if (x - 1 > -1) if (fieldMatrix[x - 1][y].type == MINE) neighborMines += 1
                fieldMatrix[x][y].minesAround = neighborMines
            }
        }
    }


}