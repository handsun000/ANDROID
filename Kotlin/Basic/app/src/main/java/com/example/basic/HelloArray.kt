package com.example.basic

fun main() {
    var arr1 = listOf("1", "2")
    var arr2 = mutableListOf("1","2")

    for (item in arr1) {
        println(item)
    }

    for ((index, item) in arr1.withIndex()) {
        println("$index $item")
    }

    var hello: Any = "hello"
    if (hello is String) {
        var str: String = hello
    }
}