package com.example.basic

fun main() {
    //1. class
    //2. data class

    var cls = HelloClass()
    var cls2 = HelloClass(10)

    println(cls2.age)

    var person = Person(5, "son")

    println(person.age)
    println(person.name)
}
class HelloClass {
    var age: Int = 0
    init {

    }

    //def 생성자, 보조 생성자
    constructor()   //기본 생성자
    constructor(age: Int) {
        this.age = age
        //보조 생성자
    }
}

data class Person (var age: Int, val name: String)