package com.example.cryptoapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.cryptoapp.R
import com.example.cryptoapp.adapter.TopLossGainPagerAdapter
import com.example.cryptoapp.adapter.TopMarketAdapter
import com.example.cryptoapp.apis.ApiInterface
import com.example.cryptoapp.apis.ApiUtilities
import com.example.cryptoapp.databinding.FragmentHomefragmentBinding
import com.example.cryptoapp.databinding.TopCurrencyLayoutBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomefragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHomefragmentBinding.inflate(layoutInflater)

        getTopCurrencyList()

        setTabLayout()

        return binding.root
    }



    private fun setTabLayout() {
        val adapter = TopLossGainPagerAdapter(this)
        binding.contentViewPager.adapter = adapter
        binding.contentViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    binding.topGainIndicator.visibility = VISIBLE
                    binding.topLoseIndicator.visibility = GONE
                } else {
                    binding.topGainIndicator.visibility = GONE
                    binding.topLoseIndicator.visibility = VISIBLE
                }
            }
        })

        TabLayoutMediator(binding.tabLayout, binding.contentViewPager){
            tab, position ->
            var title = if (position == 0){
                "Top Gainer"
            }else{
                "Top Losers"
            }
            tab.text = title
        }.attach()

    }


    private fun getTopCurrencyList() {
        lifecycleScope.launch(Dispatchers.IO)   {
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketdata()

            withContext(Dispatchers.Main){
                binding.topCurrencyRecyclerView.adapter = TopMarketAdapter(requireContext(), res.body()!!.data.cryptoCurrencyList)
            }


            Log.d("Akash", "getTopCurrencyList: ${res.body()!!.data.cryptoCurrencyList}")
        }
    }


}