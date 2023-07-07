package com.example.tryanimation.try_bluetooth_connection.bluetooth_trial.ble_library

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.tryanimation.R

class AdapterELVServiceCharacteristic(
    private val groups: List<BluetoothGattService>,
    private val children: Map<BluetoothGattService, List<BluetoothGattCharacteristic>>,
    val onClickChar : (BluetoothGattService, BluetoothGattCharacteristic?) -> Unit
) : BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        return groups.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val group = groups[groupPosition]
        return children[group]?.size ?: 0
    }

    override fun getGroup(groupPosition: Int): Any {
        return groups[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        // TODO: Warning Return is String, Expected BluetoothGattCharacteristic
        val group = groups[groupPosition]
        return children[group]?.get(childPosition) ?: ""
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?,
    ): View {
        val view: View = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_ble_service, parent, false)

        val textView: TextView = view.findViewById(R.id.tv_service)

        val dataService = getGroup(groupPosition) as BluetoothGattService
        textView.text = "${groupPosition + 1}. $dataService"

        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?,
    ): View {
        val view: View = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_ble_characteristic, parent, false)

        val textView: TextView = view.findViewById(R.id.tv_characteristic)
        val tvNumber: TextView = view.findViewById(R.id.tv_number)

        val dataChild = getChild(groupPosition, childPosition) as BluetoothGattCharacteristic
        textView.text = dataChild.toString()
        tvNumber.text = "${childPosition + 1}."

        val service = groups[groupPosition]
        val characteristics = children[service]

        view.setOnClickListener {
            onClickChar(service, characteristics?.get(childPosition))
        }

        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}