package com.zolfagharipour.market.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zolfagharipour.market.R
import com.zolfagharipour.market.adapter.LastProductsAdapter
import com.zolfagharipour.market.data.room.entities.ProductRepository
import com.zolfagharipour.market.databinding.FragmentHomeBinding
import com.zolfagharipour.market.viewModel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    private fun setRecyclerView() {
        val lastProductsAdapter = LastProductsAdapter(homeViewModel, this)
        binding.recyclerViewLastProducts.apply {
            layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = lastProductsAdapter
        }
    }

    private fun getData() {

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Main) {
                homeViewModel.productList.value = ProductRepository.lastProductList
                setRecyclerView()
            }
        }
    }

}