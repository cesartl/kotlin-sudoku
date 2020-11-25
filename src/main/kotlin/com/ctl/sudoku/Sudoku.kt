package com.ctl.sudoku

typealias Value = Int
typealias Vector<A> = List<A>

sealed class Cell
data class Fixed(val e: Value) : Cell()
data class Unknown(val candidates: List<Value>) : Cell()


data class Position(val x: Int, val y: Int)

data class Sudoku(val grid: Vector<Vector<Cell>>) {

    val isFinished: Boolean by lazy {
        grid.flatten().all { it is Fixed }
    }

    fun isValueAllowed(position: Position, value: Value): Boolean {
        return isValueAllowedInRow(position, value)
                && isValueAllowedInColumn(position, value)
                && isValueAllowedInSquare(position, value)
    }

    private fun isValueAllowedInRow(position: Position, value: Value): Boolean {
        return !grid[position.x].contains(Fixed(value))
    }

    private fun isValueAllowedInColumn(position: Position, value: Value): Boolean {
        return !grid.transpose()[position.y].contains(Fixed(value))
    }

    private fun isValueAllowedInSquare(position: Position, value: Value): Boolean {
        val ci = position.x / 3
        val cj = position.y / 3
        val squareCells: List<Cell> = grid.chunked(3)[ci].flatMap { row -> row.chunked(3)[cj] }
        return !squareCells.contains(Fixed(value))
    }
}

tailrec fun Sudoku.solve(): Sudoku? {
    val reduced = this.grid.withIndex()
        .map { (rowIdx, row) ->
            row.withIndex()
                .map { (colIdx, cell) ->
                    when (cell) {
                        is Unknown -> Unknown(cell.candidates.filter { value ->
                            isValueAllowed(Position(rowIdx, colIdx), value)
                        })
                        else -> cell
                    }
                }
                .map { cell ->
                    when (cell) {
                        is Unknown -> if (cell.candidates.size == 1) Fixed(cell.candidates.first()) else cell
                        else -> cell
                    }
                }
        }
    return if (reduced == this.grid) {
        if (this.isFinished) this else null
    } else {
        Sudoku(reduced).solve()
    }
}

