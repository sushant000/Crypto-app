package com.example.cryptoapp.fragment

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.R
import com.example.cryptoapp.adapter.MarketAdapter
import com.example.cryptoapp.apis.ApiInterface
import com.example.cryptoapp.apis.ApiUtilities
import com.example.cryptoapp.databinding.FragmentWatchlistfragmentBinding
import com.example.cryptoapp.models.CryptoCurrency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WatchlistFragment : Fragment() {

    private lateinit var binding: FragmentWatchlistfragmentBinding
    private lateinit var watchlist: ArrayList<String>
//    private lateinit var watchLisitem : ArrayList<CryptoCurrency>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchlistfragmentBinding.inflate(layoutInflater)

        readData()
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketdata()
            if (res.body() != null ){


                withContext(Dispatchers.Main){
                    val watchLisitem = ArrayList<CryptoCurrency>()
                    for (watchData in watchlist){
                        for (item in res.body()!!.data.cryptoCurrencyList){
                            if (watchData == item.symbol){
                                watchLisitem.add(item)
                            }
                        }
                    }

                    binding.spinKitView.visibility= GONE
                    binding.watchlistRecyclerView.adapter = MarketAdapter(requireContext(),watchLisitem,"watchfragment")
                }
            }
        }
        return binding.root
    }



    private fun readData() {
        val sharedPreferences  = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchlist",ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchlist = gson.fromJson(json,type)
    }
    }