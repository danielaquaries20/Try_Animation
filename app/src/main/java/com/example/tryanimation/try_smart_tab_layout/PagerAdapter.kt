package com.example.tryanimation.try_smart_tab_layout

import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(manager: FragmentManager, private val items: Array<Fragment>) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val registeredFragments = SparseArray<Fragment>()

    override fun getCount(): Int {
        return this.items.size
    }

    override fun getItem(position: Int): Fragment {
        return this.items[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        Log.d("PagerAdapter", "Position: $position")
        return "Page $position"
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

}