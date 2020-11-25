package com.ctl.sudoku

import java.util.*


fun <T> List<List<T>>.transpose(): List<List<T>> {
    val table = this;
    if(table.isEmpty()){
        return listOf()
    }
    val n = table.first().size

    val transposed = ArrayList<List<T>>(n)
    for (i in 0 until n) {
        val col: MutableList<T> = ArrayList(table.size)
        table.forEach { row ->
            col.add(row[i])
        }
        transposed.add(col)
    }
    return transposed
}
