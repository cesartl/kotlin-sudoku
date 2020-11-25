package com.ctl.sudoku

typealias Value = Int
typealias Vector<A> = List<A>


sealed class Cell
data class Known(val value: Value) : Cell()
data class Unknown(val candidates: List<Value>) : Cell()

data class Position(val x: Int, val y: Int)

typealias Row = Vector<Cell>

data class Sudoku(val rows: Vector<Row>) {

    val isFinished: Boolean by lazy {
        rows.flatten().all { it is Known }
    }

    fun isValueAllowed(position: Position, value: Value): Boolean {
        return isValueAllowedInRow(position, value)
                && isValueAllowedInColumn(position, value)
                && isValueAllowedInSquare(position, value)
    }

    private fun isValueAllowedInRow(position: Position, value: Value): Boolean {
        return !rows[position.x].contains(Known(value))
    }

    private fun isValueAllowedInColumn(position: Position, value: Value): Boolean {
        return !rows.transpose()[position.y].contains(Known(value))
    }

    private fun isValueAllowedInSquare(position: Position, value: Value): Boolean {
        val ci = position.x / 3
        val cj = position.y / 3
        val squareCells: List<Cell> = rows.chunked(3)[ci].flatMap { row -> row.chunked(3)[cj] }
        return !squareCells.contains(Known(value))
    }
}

tailrec fun Sudoku.solve(): Sudoku? {
    val reducedRows = this.rows.withIndex()
        .map { (rowIdx, row) ->
            row.withIndex()
                .map { (colIdx, cell) ->
                    val position = Position(rowIdx, colIdx)
                    when (cell) {
                        is Unknown -> Unknown(cell.candidates.filter { value -> isValueAllowed(position, value) })
                        else -> cell
                    }
                }
                .map { cell ->
                    when (cell) {
                        is Unknown -> if (cell.candidates.size == 1) Known(cell.candidates.first()) else cell
                        else -> cell
                    }
                }
        }
    return if (reducedRows == this.rows) {
        if (this.isFinished) this else null
    } else {
        Sudoku(reducedRows).solve()
    }
}

