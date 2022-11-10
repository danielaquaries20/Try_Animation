package com.example.tryanimation.try_chat_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.R

class TryChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private var chatArray: ArrayList<PersonModel> = ArrayList()
    private var chatAdapter: ChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_chat)

        rvChat = findViewById(R.id.rvChat)
        dummyData()
        initView()
    }

    private fun dummyData() {
        val dummyData = arrayOf(
            PersonModel(
                id = 1,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "10-November-2022",
                type = 1,
                chat = "Hallo, apa kabar?",
                time = "10.00"
            ),
            PersonModel(
                id = 2,
                nama = "Elysia",
                nomer = "8905627482",
                panggilan = "Elysia",
                date = "10-November-2022",
                type = 2,
                chat = "Hai, kabarku baik\nKalau kamu dan?",
                time = "10.01"
            ),
            PersonModel(
                id = 3,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "10-November-2022",
                type = 1,
                chat = "Baik juga kok El.\nEl, kamu tau nggak hari ini hari apa?",
                time = "10.02"
            ),
            PersonModel(
                id = 4,
                nama = "Elysia",
                nomer = "8905627482",
                panggilan = "Elysia",
                date = "10-November-2022",
                type = 2,
                chat = "Lha emang hari apa?",
                time = "10.03"
            ),
            PersonModel(
                id = 5,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "10-November-2022",
                type = 1,
                chat = "Hari ini hari pahlawan lho.\nKamu ikut lomba nggak?",
                time = "10.04"
            ),
            PersonModel(
                id = 6,
                nama = "Elysia",
                nomer = "8905627482",
                panggilan = "Elysia",
                date = "10-November-2022",
                type = 2,
                chat = "Oh iya, tak kira apa lho dan. hehehehehe, Kalau lomba aku nggak ikut sih.\nLha ada apa dan?",
                time = "10.05"
            ),
            PersonModel(
                id = 7,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "10-November-2022",
                type = 1,
                chat = "Hmm, gimana kalau kita jalan jalan di domain mu? (Elysian Realm)",
                time = "10.05"
            ),
            PersonModel(
                id = 8,
                nama = "Elysia",
                nomer = "8905627482",
                panggilan = "Elysia",
                date = "10-November-2022",
                type = 2,
                chat = "Boleh boleh, jam 2 siang ya aku tak kerumahmu terus tak ajak ke duniaku. Okey?",
                time = "10.06"
            ),
            PersonModel(
                id = 9,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "10-November-2022",
                type = 1,
                chat = "Okey.",
                time = "10.06"
            ),
            PersonModel(
                id = 10,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "10-November-2022",
                type = 1,
                chat = "JABCbcknckjdncjkdnsjkvkjancsukvfbsdncjsdnvjknskjvndsjcnajkncvbaxmqoiwifncrwibvqcnvnuksmxdbwbvuwjdcnwbvwjdcnubvnwicunrwuinwjucnufnchwnvwijcnvbnwjnceuvnwdjnceyvbjsdcnwjkenvuwnackjqneivnqkjxnajkncewbajkncywbvjksncuivnkjdcnyrvnkdcnuifvnksjdcnvbskjdcnfdvbjksdnvjfvnsjkvndjsvdvnsdjkdnhkjndv",
                time = "10.20"
            ),
            PersonModel(
                id = 10,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "10-November-2022",
                type = 1,
                chat = "Sorry, typo El. Hehehehehehe",
                time = "10.20"

            ),
            PersonModel(
                id = 11,
                nama = "Elysia",
                nomer = "8905627482",
                panggilan = "Elysia",
                date = "10-November-2022",
                type = 2,
                chat = "Hehehehhe, ok nggak papa kok",
                time = "10.25"
            ),
        )

        chatArray.clear()
        chatArray.addAll(dummyData)
    }

    private fun initView() {
        chatAdapter = ChatAdapter(this, chatArray)
        rvChat.layoutManager = LinearLayoutManager(this)
        rvChat.adapter = chatAdapter
        ViewCompat.setNestedScrollingEnabled(rvChat, false)
    }

}