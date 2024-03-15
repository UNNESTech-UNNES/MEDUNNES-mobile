package com.medunnes.telemedicine.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.data.model.Artikel
import com.medunnes.telemedicine.data.model.Faskes
import com.medunnes.telemedicine.databinding.FragmentHomeBinding
import com.medunnes.telemedicine.ui.adapter.ArticlesAdapter
import com.medunnes.telemedicine.ui.adapter.FaskesAdapter
import com.medunnes.telemedicine.ui.auth.login.LoginActivity
import java.util.Calendar

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val listArtikel = ArrayList<Artikel>()
    private val listFaskes = ArrayList<Faskes>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        listArtikel.addAll(getArticleList())
        showArticleRecycleList()

        listFaskes.addAll(getFaskesList())
        showFaskesRecycleList()

        with(binding) {
            cvKonsultasi.setOnClickListener(this@HomeFragment)
            cvBuatJanji.setOnClickListener(this@HomeFragment)
            tvArtikelAll.setOnClickListener(this@HomeFragment)
            tvFaskesAll.setOnClickListener(this@HomeFragment)
            tvAuthenticate.setOnClickListener(this@HomeFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showArticleRecycleList() {
        binding.rvArtikelKesehatan.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val artikelAdapter = ArticlesAdapter(listArtikel)
        binding.rvArtikelKesehatan.adapter = artikelAdapter

        artikelAdapter.setOnItemClickCallback(object : ArticlesAdapter.OnItemClickCallback {
            override fun onItemClicked(artikel: Artikel) {
                makeToast(undoneText())
            }

        })

    }

    private fun getArticleList() : ArrayList<Artikel> {
        val judulArtikel = resources.getStringArray(R.array.film_judul)
        val headlineArtikel = resources.getStringArray(R.array.poster_film)
        for (i in judulArtikel.indices) {
            val film = Artikel(judulArtikel[i], headlineArtikel[i])
            listArtikel.add(film)
        }
        return listArtikel
    }

    private fun showFaskesRecycleList() {
        binding.rvFaskesTerdekat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val faskesAdapter = FaskesAdapter(listFaskes)
        binding.rvFaskesTerdekat.adapter = faskesAdapter

        faskesAdapter.setOnItemClickCallback(object : FaskesAdapter.OnItemClickCallback {
            override fun onItemClicked(faskes: Faskes) {
                makeToast(undoneText())
            }
        })

        setGreeting()

    }

    private fun getFaskesList() : ArrayList<Faskes> {
        val namaFaskes = resources.getStringArray(R.array.film_judul)
        val fotoFaskes = resources.getStringArray(R.array.poster_film)
        for (i in namaFaskes.indices) {
            val faskes = Faskes(namaFaskes[i], fotoFaskes[i])
            listFaskes.add(faskes)
        }
        return listFaskes
    }

    private fun setGreeting() {
        val currentTime: String = when (Calendar.getInstance().time.hours) {
            in 4..11 -> {
                "Pagi"
            }
            in 12 .. 14 -> {
                "Siang"
            }
            in 15..17 -> {
                "Sore"
            }
            else -> {
                "Malam"
            }
        }
        binding.greeting.text = resources.getString(R.string.greeting, currentTime)

    }

    private fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun undoneText(): String = "Fitur belum tersedia"

    override fun onClick(view: View?) {
        with(binding) {
         when(view) {
             cvKonsultasi -> makeToast(undoneText())
             cvBuatJanji -> makeToast(undoneText())
             tvArtikelAll -> makeToast(undoneText())
             tvFaskesAll -> makeToast(undoneText())
             tvAuthenticate -> {
                 val intent = Intent(context, LoginActivity::class.java)
                 startActivity(intent)
             }
         }
        }
    }
}