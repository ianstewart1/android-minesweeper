package us.istewart.minesweeper.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import us.istewart.minesweeper.MainActivity
import us.istewart.minesweeper.R
import us.istewart.minesweeper.model.MinesweeperModel

class MinesweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var paintBackground: Paint = Paint()
    var paintBorder: Paint = Paint()
    var paintLine: Paint = Paint()
    var paintNumText: Paint = Paint()
    var flagIcon: Bitmap = BitmapFactory.decodeResource(context?.resources, R.drawable.ic_flag)
    var bombIcon: Bitmap = BitmapFactory.decodeResource(context?.resources, R.drawable.bomb)

    init {
        paintBackground.color = Color.GRAY
        paintBackground.style = Paint.Style.FILL

        paintBorder.color = Color.BLACK
        paintBorder.style = Paint.Style.STROKE
        paintBorder.strokeWidth = 14f

        paintLine.color = Color.BLACK
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 7f

        paintNumText.color = Color.BLACK
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        paintNumText.textSize = ((height.toFloat() / MinesweeperModel.DIMENSION) / 3) * 2
        flagIcon = Bitmap.createScaledBitmap(
            flagIcon, width / MinesweeperModel.DIMENSION,
            height / MinesweeperModel.DIMENSION, false
        )
        bombIcon = Bitmap.createScaledBitmap(
            bombIcon, width / MinesweeperModel.DIMENSION,
            height / MinesweeperModel.DIMENSION, false
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)
        drawBoard(canvas)
        drawClicked(canvas)
    }

    private fun drawBoard(canvas: Canvas?) {
        // border
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBorder)
        // four horizontal & vertical lines
        for (i in 1 until MinesweeperModel.DIMENSION) {
            canvas?.drawLine(
                0f,
                (i * (height / MinesweeperModel.DIMENSION)).toFloat(),
                width.toFloat(),
                (i * (height / MinesweeperModel.DIMENSION)).toFloat(),
                paintLine
            )
            canvas?.drawLine(
                (i * (width / MinesweeperModel.DIMENSION)).toFloat(),
                0f,
                (i * (width / MinesweeperModel.DIMENSION)).toFloat(),
                height.toFloat(),
                paintLine
            )
        }
    }

    fun drawClicked(canvas: Canvas?) {
        // Draw numbers, or flags
        for (i in 0 until MinesweeperModel.DIMENSION) {
            for (j in 0 until MinesweeperModel.DIMENSION) {
                if (MinesweeperModel.getIsClicked(i, j)) {
                    var unitX = (width / MinesweeperModel.DIMENSION).toFloat()
                    var unitY = (height / MinesweeperModel.DIMENSION).toFloat()
                    if (MinesweeperModel.getIsFlagged(i, j)) {
                        // SHOW FLAG
                        canvas?.drawBitmap(
                            flagIcon,
                            i * unitX,
                            j * unitY,
                            null
                        )
                    } else if (MinesweeperModel.isBomb(i, j)) {
                        // SHOW BOMB
                        canvas?.drawBitmap(
                            bombIcon,
                            i * unitX,
                            j * unitY,
                            null
                        )
                    } else {
                        // SHOW NUMBER
                        canvas?.drawText(
                            "%d".format(MinesweeperModel.getMinesAround(i, j)),
                            i * unitX + unitX/3,
                            j * unitY + 3*(unitY/4),
                            paintNumText
                        )
                    }
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val tX = event.x.toInt() / (width / MinesweeperModel.DIMENSION)
            val tY = event.y.toInt() / (height / MinesweeperModel.DIMENSION)

            if (tX < MinesweeperModel.DIMENSION && tY < MinesweeperModel.DIMENSION && MinesweeperModel.getIsClicked(tX, tY) == false) {
                if ((context as MainActivity).flagOn) {
                    // FLAGGING SQUARES
                    if (!MinesweeperModel.isBomb(tX, tY)) {
                        gameOver()
                    } else {
                        MinesweeperModel.setClicked(tX, tY)
                        MinesweeperModel.setFlagged(tX, tY)
                    }
                } else {
                    // GUESSING SQUARES
                    if (MinesweeperModel.isBomb(tX, tY)) {
                        gameOver()
                    } else {
                        MinesweeperModel.setClicked(tX, tY)
                    }
                }
                if (checkIfWin()) gameWin()
                invalidate()
            }
        }
        return true
    }

    fun checkIfWin(): Boolean {
        for (i in 0 until MinesweeperModel.DIMENSION) {
            for (j in 0 until MinesweeperModel.DIMENSION) {
                if (MinesweeperModel.isBomb(i, j) &&
                    !MinesweeperModel.getIsFlagged(i, j)
                ) return false
            }
        }
        return true
    }

    fun showTiles() {
        for (i in 0 until MinesweeperModel.DIMENSION) {
            for (j in 0 until MinesweeperModel.DIMENSION) {
                MinesweeperModel.setClicked(i, j)
            }
        }
    }

    fun gameWin() {
        showTiles()
        invalidate()
        (context as MainActivity).toastMessage(context.getString(R.string.winMessage))
    }

    fun gameOver() {
        showTiles()
        invalidate()
        (context as MainActivity).toastMessage(context.getString(R.string.loseMessage))
    }

    fun resetGame() {
        MinesweeperModel.resetGameModel()
        invalidate()
    }
}