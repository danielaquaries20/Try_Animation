package com.example.tryanimation.try_chat_app

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.R

class TryChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var ivBack: ImageView
    private lateinit var cardSendMessage: CardView
    private lateinit var etTypeMessage: EditText

    private var chatArray: ArrayList<PersonModel> = ArrayList()
    private var dummyChatArray: ArrayList<PersonModel> = ArrayList()
    private var chatAdapter: ChatAdapter? = null

    private var messageFromMe: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_chat)
        /*window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT*/

        rvChat = findViewById(R.id.rvChat)
        ivBack = findViewById(R.id.ivBack)
        cardSendMessage = findViewById(R.id.cardSendMessage)
        etTypeMessage = findViewById(R.id.etTypeMessage)

        initView()
        dummyData()

        initOnClick()
    }

    private fun initOnClick() {
        ivBack.setOnClickListener { onBackPressed() }
        cardSendMessage.setOnClickListener {
            sendMessageFromMe()
            /*Toast.makeText(
                this,
                "Send Message",
                Toast.LENGTH_SHORT
            ).show()*/
        }
    }

    private fun sendMessageFromMe() {
        val message = etTypeMessage.text.trim().toString()
        val chatLastIndex = if (chatArray.lastIndex == -1) null else chatArray.lastIndex
        val lastChatId = if (chatLastIndex == null) 0 else chatArray[chatLastIndex].id
        val newId = lastChatId?.plus(1)
        if (message.isNotEmpty()) {
            dummyChatArray.add(
                PersonModel(
                    id = newId,
                    nama = "Daniel Aquaries Pratama",
                    nomer = "098765237642",
                    panggilan = "Daniel",
                    date = "11 November 2022",
                    type = 1,
                    chat = message,
                    time = "00.00"
                )
            )
            setChat()
            chatAdapter?.itemCount?.let { chatAdapter?.notifyItemInserted(it) }
            etTypeMessage.setText("")
//            Log.d("Chat", "data: $chatArray")
        }
    }

    private fun setChat() {
        chatArray.clear()
        for (data in dummyChatArray.withIndex()) {
            if (data.index == 0) {
                chatArray.add(PersonModel(type = 0, date = data.value.date))
            } else if (data.value.date?.substring(0, 10) != dummyChatArray[data.index - 1].date?.substring(0, 10)
            ) {
                chatArray.add(PersonModel(type = 0, date = data.value.date))
            }
            chatArray.add(data.value)
        }
        rvChat.adapter?.let { rvChat.smoothScrollToPosition(it.itemCount) }
    }

    private fun initView() {
        chatAdapter = ChatAdapter(this, chatArray)
        rvChat.layoutManager = LinearLayoutManager(this)
        rvChat.adapter = chatAdapter
        ViewCompat.setNestedScrollingEnabled(rvChat, false)

        etTypeMessage.doOnTextChanged { text, start, before, count ->
            rvChat.adapter?.let { rvChat.smoothScrollToPosition(it.itemCount) }
        }
    }

    private fun dummyData() {
        val dummyData = arrayOf(
            PersonModel(
                id = 1,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "10 November 2022",
                type = 1,
                chat = "Hallo, apa kabar?",
                time = "10.00"
            ),
            PersonModel(
                id = 2,
                nama = "Elysia",
                nomer = "8905627482",
                panggilan = "Elysia",
                date = "10 November 2022",
                type = 2,
                chat = "Hai, kabarku baik\nKalau kamu dan?",
                time = "10.01"
            ),
            PersonModel(
                id = 3,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "10 November 2022",
                type = 1,
                chat = "Baik juga kok El.\nEl, kamu tau nggak hari ini hari apa?",
                time = "10.02"
            ),
            PersonModel(
                id = 4,
                nama = "Elysia",
                nomer = "8905627482",
                panggilan = "Elysia",
                date = "10 November 2022",
                type = 2,
                chat = "Lha emang hari apa?",
                time = "10.03"
            ),
            PersonModel(
                id = 5,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "10 November 2022",
                type = 1,
                chat = "Hari ini hari pahlawan lho.\nKamu ikut lomba nggak?",
                time = "10.04"
            ),
            PersonModel(
                id = 6,
                nama = "Elysia",
                nomer = "8905627482",
                panggilan = "Elysia",
                date = "10 November 2022",
                type = 2,
                chat = "Oh iya, tak kira apa lho dan. hehehehehe, Kalau lomba aku nggak ikut sih.\nLha ada apa dan?",
                time = "10.05"
            ),
            PersonModel(
                id = 7,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "10 November 2022",
                type = 1,
                chat = "Hmm, gimana kalau kita jalan jalan di domain mu? (Elysian Realm)",
                time = "10.05"
            ),
            PersonModel(
                id = 8,
                nama = "Elysia",
                nomer = "8905627482",
                panggilan = "Elysia",
                date = "10 November 2022",
                type = 2,
                chat = "Boleh boleh, jam 2 siang ya aku tak kerumahmu terus tak ajak ke duniaku. Okey?",
                time = "10.06"
            ),
            PersonModel(
                id = 9,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "10 November 2022",
                type = 1,
                chat = "Okey.",
                time = "10.06"
            ),
            PersonModel(
                id = 10,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "10 November 2022",
                type = 1,
                chat = "JABCbcknckjdncjkdnsjkvkjancsukvfbsdncjsdnvjknskjvndsjcnajkncvbaxmqoiwifncrwibvqcnvnuksmxdbwbvuwjdcnwbvwjdcnubvnwicunrwuinwjucnufnchwnvwijcnvbnwjnceuvnwdjnceyvbjsdcnwjkenvuwnackjqneivnqkjxnajkncewbajkncywbvjksncuivnkjdcnyrvnkdcnuifvnksjdcnvbskjdcnfdvbjksdnvjfvnsjkvndjsvdvnsdjkdnhkjndv",
                time = "10.20"
            ),
            PersonModel(
                id = 10,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "10 November 2022",
                type = 1,
                chat = "Sorry, typo El. Hehehehehehe",
                time = "10.20"

            ),
            PersonModel(
                id = 11,
                nama = "Elysia",
                nomer = "8905627482",
                panggilan = "Elysia",
                date = "10 November 2022",
                type = 2,
                chat = "Hehehehhe, ok nggak papa kok",
                time = "10.25"
            ),
            PersonModel(
                id = 12,
                nama = "Elysia",
                nomer = "8905627482",
                panggilan = "Elysia",
                date = "11 November 2022",
                type = 2,
                chat = "Halo dan",
                time = "13.00"
            ),
            PersonModel(
                id = 13,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "11 November 2022",
                type = 1,
                chat = "Oh, hai El\nSorry baru bales lagi ngoding.",
                time = "13.50"

            ),
            PersonModel(
                id = 14,
                nama = "Elysia",
                nomer = "8905627482",
                panggilan = "Elysia",
                date = "11 November 2022",
                type = 2,
                chat = "Oh ok, sorry kalau ganggu",
                time = "13.51"
            ),
            PersonModel(
                id = 15,
                nama = "Daniel Aquaries Pratama",
                nomer = "098765237642",
                panggilan = "Daniel",
                date = "11 November 2022",
                type = 1,
                chat = "Nggak papa kok El.\nLagian udah hampir selesai",
                time = "13.51"

            ),
        )

        chatArray.clear()
        dummyChatArray.clear()
        chatAdapter?.notifyDataSetChanged()

        dummyChatArray.addAll(dummyData)

        setChat()
        /*for (data in dummyChatArray.withIndex()) {
            if (data.index == 0) {
                chatArray.add(PersonModel(type = 0, date = data.value.date))
            } else if (data.value.date?.substring(0, 10) != dummyChatArray[data.index - 1].date?.substring(0, 10)
            ) {
                chatArray.add(PersonModel(type = 0, date = data.value.date))
            }
            chatArray.add(data.value)
        }*/
        chatAdapter?.notifyDataSetChanged()
        /*chatArray.addAll(dummyData)*/
    }
}