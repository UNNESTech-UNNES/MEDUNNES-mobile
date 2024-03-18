package com.medunnes.telemedicine.ui.message

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.data.model.Messanger
import com.medunnes.telemedicine.databinding.FragmentMessageBinding
import com.medunnes.telemedicine.ui.adapter.MessangersAdapter

class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val listMessanger = ArrayList<Messanger>()
    private val filteredListMessanger = ArrayList<Messanger>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(MessageViewModel::class.java)

        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        searchMessanger()
        showRecycleList(getMessangerList())

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRecycleList(listAdapter: ArrayList<Messanger>) {
        binding.rvMessageList.layoutManager = LinearLayoutManager(context)
        val messangersAdapter = MessangersAdapter(listAdapter)
        binding.rvMessageList.adapter = messangersAdapter

        messangersAdapter.setOnItemClickCallback(object : MessangersAdapter.OnItemClickCallback {
            override fun onItemClicked(messanger: Messanger) {
                Toast.makeText(context, "Fitur belum tersedia", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getMessangerList() : ArrayList<Messanger> {
        val fotoMessanger = resources.getStringArray(R.array.poster_film)
        val namaMessanger = resources.getStringArray(R.array.film_judul)
        val sesiMessanger = resources.getStringArray(R.array.rilis_tahun)
        val statusMessanger = resources.getStringArray(R.array.rilis_tahun)
        listMessanger.clear()
        if (namaMessanger.isNotEmpty()) {
            for (i in namaMessanger.indices) {
                val messanger = Messanger(fotoMessanger[i], namaMessanger[i], sesiMessanger[i], statusMessanger[i])
                listMessanger.add(messanger)
            }
        }
        return listMessanger
    }

    private fun getFilteredMessangerList(filter: String): ArrayList<Messanger> {
        return getMessangerList().filter {
            it.namaMessanger.lowercase().contains(filter.lowercase())
        } as ArrayList<Messanger>
    }

    private fun searchMessanger() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    filteredListMessanger.clear()
                    filteredListMessanger.addAll(getFilteredMessangerList("${searchView.text}"))
                    showRecycleList(filteredListMessanger)
                    Log.d("GET", "${getFilteredMessangerList("${searchView.text}")}")
                    false
                }
        }
    }
}