package com.ctl.sudoku

import org.junit.jupiter.api.Test
import java.io.InputStream

internal class SudokuTest {

    @Test
    internal fun sudokuTest() {
        val sudoku = parseSudoku(getLines("grid1.txt")!!)
        sudoku.display()
        println()
        sudoku.solve()?.display()
    }

    private fun getStream(file: String): InputStream? = this.javaClass.classLoader.getResourceAsStream(file)
    private fun getLines(file: String): Sequence<String>? =
        getStream(file)?.bufferedReader()?.use { it.readLines().asSequence() }
}
