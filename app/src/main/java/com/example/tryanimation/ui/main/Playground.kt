package com.example.tryanimation.ui.main

import com.example.tryanimation.ui.kl_basic.chapter_8.model.Buah
import com.example.tryanimation.ui.kl_basic.chapter_8.model.Human
import com.example.tryanimation.ui.kl_basic.chapter_8.model.Person

fun main() {
//    val penambahan = {x: Int, y: Int -> x + y}
//    println("Penambahan: ${penambahan(4, 3)}")

    val message: String? = null

    if (message != null) {
        println("Biasa: $message")
    }

    message?.let { println("Lambda: $it") }
}


/*region Function*/
fun initOldFunction() {
    val human = Human("12342341", "Roger", 16)

    human.name = "Yunyun"

    println(human.name)
    println(human.id)
    println(human.age)

    val orang = Person("Febri", null, "Male", null, null, "Main Game", null)
    val buah = Buah("Jeruk", "Jingga")

    val febri = orang.copy(description = "Hallo saya Febri")

}

fun increment(x: Int, y: Int): Int {
    return x + y
}
/*endregion*/


