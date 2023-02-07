package com.example.tryanimation

import android.content.Intent
import android.os.Bundle
import com.example.tryanimation.try_chat_app.TryChatActivity
import com.reprime.onboarder.AhoyOnboarderActivity
import com.reprime.onboarder.AhoyOnboarderCard

class TryOnBoarderActivity : AhoyOnboarderActivity() {

    private var isShouldFinish = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onboarderCard1 = AhoyOnboarderCard(
            "Online Absensi",
            "Fitur pencocokan wajah dan lokasi akan memberikan Anda sebuah pengalaman baru dalam melakukan absensi kerja dengan mudah, cepat, dan akurat.",
            R.drawable.anime_girl
        )

        val onboarderCard2 = AhoyOnboarderCard(
            "Laporan Kerja",
            "Catat serta laporkan aktivitas dan hasil kerja Anda di luar kantor secara real time, efektif, dan efisien.",
            R.drawable.elysia
        )

        val onboarderCard3 = AhoyOnboarderCard(
            "Pengajuan dan Persetujuan",
            "Dapatkan hasil persetujuan dengan cepat ketika Anda melakukan pengajuan ijin cuti, lembur, dan klaim di dalam aplikasi mobile Anda.",
            R.drawable.background_chat
        )

        val pages = ArrayList<AhoyOnboarderCard>()
        pages.add(onboarderCard1)
        pages.add(onboarderCard2)
        pages.add(onboarderCard3)

        setOnboardPages(pages)

    }

    override fun onFinishButtonPressed() {
        isShouldFinish = true
        val intent = Intent(this, TryChatActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (!backOnboard()) {
            finish()
        }
    }

    override fun onStop() {
        if (isShouldFinish) finish()
        super.onStop()
    }
}