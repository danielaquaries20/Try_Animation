package com.example.tryanimation.try_chat_app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tryanimation.R
import java.text.SimpleDateFormat
import java.util.*

class TryChatActivity : AppCompatActivity() {

    private lateinit var rvChat: RecyclerView
    private lateinit var ivBack: ImageView
    private lateinit var cardSendMessage: CardView
    private lateinit var etTypeMessage: EditText
    private lateinit var ivSettings: ImageView
    private lateinit var cardSettings: CardView
    private lateinit var tvBotStart: TextView
    private lateinit var tvBotEnd: TextView
    private lateinit var tvSayHay: TextView
    private lateinit var tvAskYourCondition: TextView

    private var chatArray: ArrayList<PersonModel> = ArrayList()
    private var dummyChatArray: ArrayList<PersonModel> = ArrayList()
    private var chatAdapter: ChatAdapter? = null
    private var settingsShow = false
    private var statusBot = 0

    private var messageFromMe: String = ""
    private var messageFromBotBefore: String = ""
    private var memoryFromBotBefore: String = ""

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
        ivSettings = findViewById(R.id.ivSettings)
        cardSettings = findViewById(R.id.cardSettings)
        tvBotStart = findViewById(R.id.tvStartBot)
        tvBotEnd = findViewById(R.id.tvEndBot)
        tvSayHay = findViewById(R.id.tvSayHay)
        tvAskYourCondition = findViewById(R.id.tvAskYourCondition)

        initView()
//        dummyData()

        initOnClick()
    }

    override fun onBackPressed() {
        if (settingsShow) {
            settingsShow = false
            cardSettings.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
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
        ivSettings.setOnClickListener {
            if (settingsShow) {
                settingsShow = false
                cardSettings.visibility = View.GONE
            } else {
                settingsShow = true
                cardSettings.visibility = View.VISIBLE
            }
        }
        tvBotStart.setOnClickListener {
            statusBot = 1
            Toast.makeText(this, "Bot Activated", Toast.LENGTH_SHORT).show()
            cardSettings.visibility = View.GONE
            settingsShow = false
        }
        tvBotEnd.setOnClickListener {
            statusBot = 0
            Toast.makeText(this, "Bot Diactivated", Toast.LENGTH_SHORT).show()
            cardSettings.visibility = View.GONE
            settingsShow = false
        }
        tvSayHay.setOnClickListener {
            statusBot = 1
            cardSettings.visibility = View.GONE
            settingsShow = false
            val chatLastIndex = if (chatArray.lastIndex == -1) null else chatArray.lastIndex
            val lastChatId = if (chatLastIndex == null) 0 else chatArray[chatLastIndex].id
            val newId = lastChatId?.plus(1)
            dummyChatArray.add(
                PersonModel(
                    id = newId,
                    nama = "Daniel Aquaries Pratama",
                    nomer = "098765237642",
                    panggilan = "Daniel",
                    date = dateNow(),
                    type = 1,
                    chat = "Hay Elysia",
                    time = timeNow()
                )
            )
            messageFromMe = "Hay Elysia"
            setChat()
            chatAdapter?.itemCount?.let { chatAdapter?.notifyItemInserted(it) }
            etTypeMessage.setText("")
            sendMessageFromOpponent()
        }
        tvAskYourCondition.setOnClickListener {
            statusBot = 1
            cardSettings.visibility = View.GONE
            settingsShow = false
            val chatLastIndex = if (chatArray.lastIndex == -1) null else chatArray.lastIndex
            val lastChatId = if (chatLastIndex == null) 0 else chatArray[chatLastIndex].id
            val newId = lastChatId?.plus(1)
            setResponseBot(newId, "Halo dan, gimana kabarmu?")
        }
    }

    private fun sendMessageFromMe() {
        val message = etTypeMessage.text.trim().toString()
        messageFromMe = message
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
                    date = dateNow(),
                    type = 1,
                    chat = message,
                    time = timeNow()
                )
            )
            setChat()
            chatAdapter?.itemCount?.let { chatAdapter?.notifyItemInserted(it) }
            etTypeMessage.setText("")
//            Log.d("Chat", "data: $chatArray")
            sendMessageFromOpponent()
        } else {
            Toast.makeText(this, "Tidak bisa mengirim pesan kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMessageFromOpponent() {
        if (statusBot == 1) {
            chatAdapter?.addLoading("Elysia")
            val chatLastIndex = if (chatArray.lastIndex == -1) null else chatArray.lastIndex
            val lastChatId = if (chatLastIndex == null) 0 else chatArray[chatLastIndex].id
            val newId = lastChatId?.plus(1)
            Handler(Looper.getMainLooper()).postDelayed({
                responseFromBot(newId)
            }, 500)
        } else {
            Toast.makeText(this, "Aktifkan Bot terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setResponseBot(id: Int? = 0, pesan: String) {
        dummyChatArray.add(
            PersonModel(
                id = id,
                nama = "Elysia",
                nomer = "8905627482",
                panggilan = "Elysia",
                date = dateNow(),
                type = 2,
                chat = pesan,
                time = timeNow()
            )
        )
        chatAdapter?.removeLoading()
        messageFromBotBefore = pesan
        setChat()
        chatAdapter?.itemCount?.let { chatAdapter?.notifyItemInserted(it) }

    }

    private fun setChat() {
        chatArray.clear()
        for (data in dummyChatArray.withIndex()) {
            if (data.index == 0) {
                chatArray.add(PersonModel(type = 0, date = data.value.date))
            } else if (data.value.date?.substring(
                    0,
                    10
                ) != dummyChatArray[data.index - 1].date?.substring(0, 10)
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

    private fun responseFromBot(newId: Int?) {
        if (messageFromMe == "") {
            setResponseBot(newId, "Maaf dan, aku nggak ngerti apa yang kamu maksud")
        } else if (messageFromMe.contains("Halo") || messageFromMe.contains("Hallo") || messageFromMe.contains(
                "Helo"
            ) || messageFromMe.contains("Hello") || messageFromMe.contains("halo") || messageFromMe.contains(
                "hallo"
            ) || messageFromMe.contains("helo") || messageFromMe.contains("hello") || messageFromMe.contains(
                "HALLO"
            ) || messageFromMe.contains("HALO") || messageFromMe.contains("HELLO") || messageFromMe.contains(
                "HELO"
            )
        ) {
            if (messageFromMe.contains("gimana kabarmu")) {
                setResponseBot(newId, "Hai dan, kabarku baik\nKalau kabarmu?")
            } else {
                setResponseBot(newId, "Hai dan, gimana kabarmu?")
            }
        } else if (messageFromMe.contains("Hai") || messageFromMe.contains("hai") || messageFromMe.contains(
                "HAI"
            ) || messageFromMe.contains("Hi") || messageFromMe.contains("hi") || messageFromMe.contains(
                "HI"
            ) || messageFromMe.contains("Hey") || messageFromMe.contains("HEY") || messageFromMe.contains(
                "HAY"
            ) || messageFromMe.contains("Hay")
        ) {
            if (messageFromMe.contains("gimana kabarmu")) {
                setResponseBot(newId, "Halo dan, kabarku baik\nKalau kabarmu?")
            } else {
                setResponseBot(newId, "Halo dan, gimana kabarmu?")
            }
        } else if (messageFromMe.contains("Elysia") || messageFromMe.contains("elysia") || messageFromMe.contains(
                "ELYSIA"
            ) || messageFromMe.contains("EL") || messageFromMe.contains("el") || messageFromMe.contains(
                "El"
            )
        ) {
            if (messageFromBotBefore.contains("Elysia") || messageFromBotBefore.contains("elysia") || messageFromBotBefore.contains(
                    "ELYSIA"
                ) || messageFromBotBefore.contains("EL") || messageFromBotBefore.contains("el") || messageFromBotBefore.contains(
                    "El"
                )
            ) {
                setResponseBot(newId, "Iyaa, ada apa?")
            } else {
                setResponseBot(newId, "Ya, ada apa dan?\nApa ada yang bisa tak bantu?")
            }
        } else if (messageFromMe.contains("Tanyain kabar") || messageFromMe.contains("tanyain kabar") || messageFromMe.contains(
                "Tanyain Kabar"
            ) || messageFromMe.contains("TANYAIN KABAR") || messageFromMe.contains("Tanya kabar") || messageFromMe.contains(
                "Tanya Kabar"
            ) || messageFromMe.contains("tanya kabar") || messageFromMe.contains("TANYA KABAR")
        ) {
            if (messageFromBotBefore == "" || messageFromBotBefore.isEmpty()) {
                setResponseBot(newId, "Halo dan, gimana kabarmu?")
            } else {
                if (memoryFromBotBefore.contains("Baik")) {
                    setResponseBot(
                        newId,
                        "Okey, kabarmu gimana dan?\nBukannya tadi kabarmu baik ya? Hehehehehehe"
                    )
                } else if (memoryFromBotBefore.contains("Buruk")) {
                    setResponseBot(
                        newId,
                        "Okey, kabarmu gimana dan?\nBukannya tadi kabarmu buruk ya, sebenarnya masalahmu serumit apa?\nSampe butuh pertahian banget sama aku."
                    )
                } else {
                    setResponseBot(newId, "Okey, kabarmu gimana dan?")
                }
            }
        } else if (messageFromMe.contains("Kabarku baik") || messageFromMe.contains("Kabarku Baik") || messageFromMe.contains(
                "kabarku baik"
            ) || messageFromMe.contains("Baik kok") || messageFromMe.contains("baik kok") || messageFromMe.contains(
                "baik"
            ) || messageFromMe.contains("Baik") || messageFromMe.contains("Syukurlah baik") || messageFromMe.contains(
                "Syukurlah, baik"
            ) || messageFromMe.contains("Puji Tuhan, baik") || messageFromMe.contains("Alhamdulilah, baik")
        ) {
            if (messageFromBotBefore.contains("gimana kabarmu") || messageFromBotBefore.contains(
                    "kalau kabarmu"
                ) || messageFromBotBefore.contains("Kalau kabarmu")
            ) {
                setResponseBot(newId, "Syukurlah kalau baik\nAku jadi tenang")
                memoryFromBotBefore = "Baik"
            } else {
                setResponseBot(newId, "Maaf dan, aku nggak ngerti apa yang kamu maksud")
            }
        } else if (messageFromMe.contains("Buruk") || messageFromMe.contains("buruk") || messageFromMe.contains(
                "BURUK"
            ) || messageFromMe.contains("kabarku buruk") || messageFromMe.contains("tidak baik") || messageFromMe.contains(
                "Tidak baik"
            ) || messageFromMe.contains("Tidak Baik") || messageFromMe.contains("TIDAK BAIK") || messageFromMe.contains(
                "tidak baik baik saja"
            ) || messageFromMe.contains("Tidak baik baik saja") || messageFromMe.contains("Tidak Baik Baik Saja") || messageFromMe.contains(
                "TIDAK BAIK BAIK SAJA"
            ) || messageFromMe.contains("nggak baik") || messageFromMe.contains("Nggak baik") || messageFromMe.contains(
                "NGGAK BAIK"
            )
        ) {
            if (messageFromBotBefore.contains("gimana kabarmu") || messageFromBotBefore.contains(
                    "kalau kabarmu"
                ) || messageFromBotBefore.contains("Kalau kabarmu")
            ) {
                setResponseBot(newId, "Waduhh, kenapa dan?\nKalau mau cerita, cerita aja")
                memoryFromBotBefore = "Buruk"
            } else {
                setResponseBot(newId, "Maaf dan, aku nggak ngerti apa yang kamu maksud")
            }
        } else if (messageFromMe.contains("Aku ada masalah") || messageFromMe.contains("aku ada masalah") || messageFromMe.contains(
                "Aku ada masalah"
            ) || messageFromMe.contains("Aku Ada Masalah") || messageFromMe.contains("AKU ADA MASALAH") || messageFromMe.contains(
                "lagi ada masalah"
            ) || messageFromMe.contains("Lagi ada masalah") || messageFromMe.contains("Lagi Ada Masalah") || messageFromMe.contains(
                "LAGI ADA MASALAH"
            ) || messageFromMe.contains("Lagi bingung") || messageFromMe.contains("lagi bingung") || messageFromMe.contains(
                "Lagi Bingung"
            ) || messageFromMe.contains("LAGI BINGUNG")
        ) {
            if (messageFromMe.contains("Masalah") || messageFromMe.contains("masalah") || messageFromMe.contains(
                    "MASALAH"
                )
            ) {
                setResponseBot(newId, "Masalah apa dan?")
            } else if (messageFromMe.contains("Bingung") || messageFromMe.contains("bingung") || messageFromMe.contains(
                    "BINGUNG"
                )
            ) {
                setResponseBot(newId, "Bingung kenapa dan?")
            }
        } else if (messageFromMe.contains("OK") || messageFromMe.contains("Ok") || messageFromMe.contains(
                "ok"
            ) || messageFromMe.contains("Okey") || messageFromMe.contains("okey") || messageFromMe.contains(
                "OKEY"
            ) || messageFromMe.contains("Baiklah") || messageFromMe.contains("baiklah") || messageFromMe.contains(
                "BAIKLAH"
            )
        ) {
            if (messageFromBotBefore.contains("Syukurlah kalau baik") || messageFromBotBefore.contains(
                    "Aku jadi tenang"
                )
            ) {
                setResponseBot(newId, "Okey")
            } else {
                setResponseBot(newId, "Maaf, aku tidak tau apa yang kamu maksud")
            }
        } else if (messageFromMe.contains("hehe") || messageFromMe.contains("HEHE") || messageFromMe.contains(
                "Hehe"
            ) || messageFromMe.contains("wkwk") || messageFromMe.contains("Wkwk") || messageFromMe.contains(
                "WKWK"
            )
        ) {
            if (messageFromMe.substring(0, 4).contains("hehe") || messageFromMe.substring(0, 4)
                    .contains("HEHE") || messageFromMe.substring(0, 4)
                    .contains("Hehe") || messageFromMe.substring(0, 4)
                    .contains("wkwk") || messageFromMe.substring(0, 4)
                    .contains("Wkwk") || messageFromMe.substring(0, 4)
                    .contains("WKWK") || messageFromMe.substring(0, 4)
                    .contains("HAHA") || messageFromMe.substring(0, 4)
                    .contains("haha") || messageFromMe.substring(0, 4)
                    .contains("Haha") || messageFromMe.substring(0, 4)
                    .contains("HIHI") || messageFromMe.substring(0, 4)
                    .contains("hihi") || messageFromMe.substring(0, 4)
                    .contains("Hihi")
            ) {
                setResponseBot(newId, messageFromMe.substring(0, 4))
            } else {
                setResponseBot(newId, "Aku nggak paham dan")
            }
        } else {
            if (messageFromBotBefore.contains("Maaf dan, aku nggak ngerti apa yang kamu maksud")) {
                setResponseBot(newId, "Maaf, aku tidak tau apa yang kamu maksud")
            } else if (messageFromBotBefore.contains("Maaf, aku tidak tau apa yang kamu maksud")) {
                setResponseBot(newId, "Tolong apakah kamu typo atau gimana?")
            } else if (messageFromBotBefore.contains("Tolong apakah kamu typo atau gimana?")) {
                setResponseBot(newId, "Jangan aneh aneh...!")
            } else if (messageFromBotBefore.contains("Jangan aneh aneh...!")) {
                setResponseBot(newId, "......")
            } else if (messageFromBotBefore.contains("......")) {
                setResponseBot(newId, "-_-")
            } else if (messageFromBotBefore.contains("-_-")) {
                setResponseBot(newId, "-_-")
            } else {
                setResponseBot(newId, "Maaf dan, aku nggak ngerti apa yang kamu maksud")
            }
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

    private fun dateNow(): String {
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        return formatter.format(Date())
    }

    private fun timeNow(): String {
        val formatter = SimpleDateFormat("HH.mm", Locale("id", "ID"))
        return formatter.format(Date())
    }


}