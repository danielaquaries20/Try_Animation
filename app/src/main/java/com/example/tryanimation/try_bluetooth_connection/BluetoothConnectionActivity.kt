package com.example.tryanimation.try_bluetooth_connection

import android.os.Bundle
import android.view.View
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.base.adapter.ReactiveListAdapter
import com.crocodic.core.extension.snacked
import com.crocodic.core.extension.text
import com.example.tryanimation.R
import com.example.tryanimation.databinding.ActivityBluetoothConnectionBinding
import com.example.tryanimation.databinding.ItemBluetoothDetectedBinding

class BluetoothConnectionActivity :
    NoViewModelActivity<ActivityBluetoothConnectionBinding>(R.layout.activity_bluetooth_connection) {

    private val adapter =
        object :
            ReactiveListAdapter<ItemBluetoothDetectedBinding, String>(R.layout.item_bluetooth_detected) {
            override fun onBindViewHolder(
                holder: ItemViewHolder<ItemBluetoothDetectedBinding, String>,
                position: Int,
            ) {
                val item = getItem(position)
                holder.binding.tvBluetoothCast.text(item)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.rvBluetoothList.adapter = adapter
        dummyData()

        binding.fabCast.setOnClickListener(this)
        binding.fabCheckHeart.setOnClickListener(this)
        binding.fabCheckSport.setOnClickListener(this)
    }

    private fun dummyData() {
        val data = ArrayList<String>()
        for (i in 1..15) {
            data.add("Bluetooth $i")
        }
        adapter.submitList(data)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.fabCast -> binding.root.snacked("Casting")
            binding.fabCheckHeart -> binding.root.snacked("Check Heart")
            binding.fabCheckSport -> binding.root.snacked("Check Sport")
        }
    }


}