package com.ctl.sudoku

sealed class Cell {
    fun print(): String {
        return when (this) {
            is Fixed -> this.e.toString()
            is Unknown -> "."
        }
    }

    companion object {
        fun parse(char: Char): Cell {
            return when (char) {
                '0', '.' -> Unknown((1 until 10).toList())
                else -> Fixed(char.toString().toInt())
            }
        }
    }
}

typealias Value = Int

data class Fixed(val e: Value) : Cell()
data class Unknown(val candidates: List<Value>) : Cell()


typealias Vector<A> = List<A>

data class Position(val x: Int, val y: Int)

data class Sudoku(val grid: Vector<Vector<Cell>>) {

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

    val isFinished: Boolean by lazy {
        grid.flatten().all { it is Fixed }
    }

    private fun reduceCell(cell: Cell, rowIdx: Int, colIdx: Int): Cell {
        return when (cell) {
            is Unknown -> Unknown(cell.candidates.filter { value ->
                isValueAllowed(Position(rowIdx, colIdx), value)
            })
            else -> cell
        }
    }

    fun display() {
        grid.chunked(3).forEach { rows ->
            rows.forEach { row ->
                println(row.chunked(3).joinToString(" ") { it.joinToString("") { cell -> cell.print() } })
            }
            println()
        }
    }

    companion object {
        fun parse(lines: Sequence<String>): Sudoku {
            val grid = lines.map { row -> row.map { Cell.parse(it) } }.toList()
            return Sudoku(grid)
        }

        tailrec fun solve(sudoku: Sudoku): Sudoku? {
            val reduced = sudoku.grid.withIndex()
                .map { (rowIdx, row) ->
                    row.withIndex()
                        .map { (colIdx, cell) -> sudoku.reduceCell(cell, rowIdx, colIdx) }
                        .map { cell ->
                            when (cell) {
                                is Unknown -> if (cell.candidates.size == 1) Fixed(cell.candidates.first()) else cell
                                else -> cell
                            }
                        }
                }
            return if (reduced == sudoku.grid) {
                if (sudoku.isFinished) sudoku else null
            } else {
                solve(Sudoku(reduced))
            }
        }
    }
}


