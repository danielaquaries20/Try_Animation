package com.example.tryanimation.test

import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.math.sin


fun main() {
//    println("Hello World!")
    sampleZero()
    sampleOne()
    sampleTwo()
    sampleThree()
    sampleFour()
}

private fun sampleZero() {
    println("Sample Zero: ")
    val work = "(2 + 4) * 5 - 4 / 2"
    val expression = ExpressionBuilder(work).build()
    val result = expression.evaluate()
    val task = (2 + 4) * 5 - 4 / 2

    println("Hasil 1: $result")
    println("Expected: $task")
    println("")
    println("")

   /* val sudut = Math.PI / 4 // Sudut 45 derajat dalam radian

    val sinValue = kotlin.math.sin(sudut)
    val cosValue = kotlin.math.cos(sudut)
    val tanValue = kotlin.math.tan(sudut)

    println("sin: $sinValue")
    println("cos: $cosValue")
    println("tan: $tanValue")*/
}

private fun sampleOne() {
    // Untuk mengevaluasi ekspresi. Kelas ExpressionBuilder dapat digunakan untuk membuat objek
    // Expression yang mampu evaluasi. Fungsi kustom dan operator kustom dapat diatur melalui
    // panggilan ke ExpressionBuilder.function() dan ExpressionBuilder.operator(). Setiap variabel
    // yang diberikan dapat diatur pada objek Expression yang dikembalikan oleh
    // ExpressionBuilder.build() melalui panggilan ke Expression.variable()
    println("Sample One: ")
    val task = 3 * sin(3.14) - 2 / (2.3 - 2)
    val e: Expression = ExpressionBuilder("3 * sin(y) - 2 / (x - 2)")
        .variables("x", "y")
        .build()
        .setVariable("x", 2.3)
        .setVariable("y", 3.14)
    val result: Double = e.evaluate()
    println("Result: $result")
    println("Expected: $task")
    println("")
    println("")
}

private fun sampleTwo() {
    // Untuk mengevaluasi ekspresi secara asinkron, pengguna hanya perlu
    // menyediakan java.util.concurrent.ExecutorService.
    println("Sample Two: ")
    val exec: ExecutorService = Executors.newFixedThreadPool(1)
    val e = ExpressionBuilder("3log(y)/(x+1)")
        .variables("x", "y")
        .build()
        .setVariable("x", 2.3)
        .setVariable("y", 3.14)
    val future: Future<Double> = e.evaluateAsync(exec)
    val result: Double = future.get()
    println("Result: $result")
    println("")
    println("")
}

private fun sampleThree() {
    // Support Implicit Multiplication.
    // Oleh karena itu ekspresi seperti 2cos (yx) akan ditafsirkan sebagai 2 * cos (y * x)
    println("Sample Three: ")
    val result = ExpressionBuilder("2cos(xy)")
        .variables("x", "y")
        .build()
        .setVariable("x", 0.5)
        .setVariable("y", 0.25)
        .evaluate()
    println("Result: $result")
    println("")
    println("")
}

private fun sampleFour() {
    // Konstanta umum berikut telah ditambahkan ke exp4j dan terikat secara otomatis:
    // pi dan π nilai π sebagaimana didefinisikan dalam Math.PI,
    // e nilai bilangan Euler e, φ nilai rasio emas (1,61803398874)
    println("Sample Four: ")
    val expr = "pi+π+e+φ"
    val expected = 2 * Math.PI + Math.E + 1.61803398874
    val e = ExpressionBuilder(expr).build()
    println("Result: ${e.evaluate()}")
    println("Expected: $expected")
    println("")
    println("")
}


/*
private fun sampleFive() {
    println("Sample Five: ")
    val expr = "7.2973525698e-3"
    val expected = expr.toDouble()
    val e = ExpressionBuilder(expr).build()
    println("Result: ${e.evaluate()}")
    println("Expected: $expected")
    println("")
    println("")
}*/
