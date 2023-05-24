package com.example.tryanimation.try_bottom_navigation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.adapter.CoreListAdapter
import com.crocodic.core.base.fragment.CoreFragment
import com.crocodic.core.extension.tos
import com.example.tryanimation.R
import com.example.tryanimation.databinding.FragmentHomeBinding
import com.example.tryanimation.databinding.ItemNoteBinding
import com.example.tryanimation.try_architecture_code.data.model.Note
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : CoreFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private lateinit var viewModel: HomeViewModel

    private var dataList = ArrayList<Note?>()
    private var dataAll = ArrayList<Note?>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this@HomeFragment)[HomeViewModel::class.java]

        observe()

        binding.rvNotes.adapter =
            CoreListAdapter<ItemNoteBinding, Note>(R.layout.item_note).initItem(dataList) { position, data ->
                activity?.tos("Title: ${data?.title}")
            }

        viewModel.getNotes()
//        dummyData()
    }

    private fun observe() {
        viewModel.noteResponse.observe(requireActivity()) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    binding.pbLoading.visibility = View.GONE
                }
                ApiStatus.LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
                else -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.tvEmptyData.visibility = View.VISIBLE
                    activity?.tos("Data have some trouble")
                }
            }
        }

        viewModel.listNote.observe(requireActivity()) { data ->
            Timber.tag("HomeFragment").d("ListNote: $data")
            dataList.clear()
            dataAll.clear()
            binding.rvNotes.adapter?.notifyDataSetChanged()
            dataList.addAll(data)
            dataAll.addAll(data)
            binding.rvNotes.adapter?.notifyItemInserted(0)

            Timber.tag("HomeFragment").d("ListNote: $dataList")

            if (dataList.isEmpty()) {
                binding.rvNotes.visibility = View.GONE
                binding.tvEmptyData.visibility = View.VISIBLE
            } else {
                binding.rvNotes.visibility = View.VISIBLE
                binding.tvEmptyData.visibility = View.GONE
            }
        }

    }


    private fun dummyData() {
        dataList.clear()
        binding.rvNotes.adapter?.notifyDataSetChanged()
        for (i in 1..10) {
            dataList.add(Note("Idke-$i", "Dummy $i", "lorem ipsum dolor", 12311L, 23123L))
        }
        binding.rvNotes.adapter?.notifyItemInserted(0)
    }
}