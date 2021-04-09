package com.example.demo

class  A {
    fun foo() = 1
}

fun A.foo() = 2


fun main2() {
    println(A().foo())

    var approach = "Kotlin is the best"

    var opinion: String? = "Kotlin is the best"
    opinion = null // ok

    val length = opinion?.length // ok

    println(opinion + approach + length)

    double(6)
}

fun double(x: Int) = 2 * x

fun myPrint(arr: List<Any>) = print(arr)
fun test1(){
    val listStr = listOf("one", "two")
    myPrint(listStr)
    val listNumbers = listOf(1, 2)
    myPrint(listNumbers)
}

fun test2(){
    val strings = ArrayList<String>()
    val objects = strings
    objects.add("new")
//    objects.add(1)
    myPrint(objects)
}

fun test3(){
    val strings = ArrayList<String>()
    val objects = ArrayList<Any>()
//    objects = strings
    objects.add("new")
    objects.add(1)
    myPrint(objects)
}
