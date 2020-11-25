package com.ctl.sudoku

import org.junit.jupiter.api.Test

class ListExtensionKtTest {

    @Test
    fun transpose() {
        val table = listOf(listOf(1, 2, 3), listOf(4, 5, 6))
        displayGrid(table)
        println()
        displayGrid(table.transpose())
    }

    private fun <T> displayGrid(table :List<List<T>>){
        table.forEach { row ->
            println(row.joinToString(separator = " "))
        }
    }
}
