package com.ctl.sudoku

fun Sudoku.display() {
    rows.chunked(3).forEach { rows ->
        rows.forEach { row ->
            println(row.chunked(3).joinToString(" ") { it.joinToString("") { cell -> cell.print() } })
        }
        println()
    }
}

fun Cell.print(): String {
    return when (this) {
        is Known -> this.value.toString()
        is Unknown -> "."
    }
}

fun parseCell(char: Char): Cell {
    return when (char) {
        '0', '.' -> Unknown((1 until 10).toList())
        else -> Known(char.toString().toInt())
    }
}

fun parseSudoku(lines: Sequence<String>): Sudoku {
    val grid = lines.map { row -> row.map { parseCell(it) } }.toList()
    return Sudoku(grid)
}
