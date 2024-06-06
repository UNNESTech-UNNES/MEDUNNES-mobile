package com.medunnes.telemedicine.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.api.ApiConfig
import com.medunnes.telemedicine.data.model.Artikel
import com.medunnes.telemedicine.data.model.Faskes
import com.medunnes.telemedicine.data.response.UserResponse
import com.medunnes.telemedicine.databinding.FragmentHomeBinding
import com.medunnes.telemedicine.ui.adapter.ArticlesAdapter
import com.medunnes.telemedicine.ui.adapter.FaskesAdapter
import com.medunnes.telemedicine.ui.auth.login.LoginActivity
import com.medunnes.telemedicine.ui.dokter.LayananDokterActivity
import com.medunnes.telemedicine.ui.pasien.LayananPasienActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Calendar

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val listArtikel = ArrayList<Artikel>()
    private val listFaskes = ArrayList<Faskes>()

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        lifecycleScope.launch { setUserProfile() }
        getArticleList()
        showArticleRecycleList()

        getFaskesList()
        showFaskesRecycleList()

        with(binding) {
            btnKonsultasi.setOnClickListener(this@HomeFragment)
            btnBuatJanji.setOnClickListener(this@HomeFragment)
            tvArtikelAll.setOnClickListener(this@HomeFragment)
            tvFaskesAll.setOnClickListener(this@HomeFragment)
            tvAuthenticate.setOnClickListener(this@HomeFragment)
        }

        lifecycleScope.launch { getAllUser() }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun setUserProfile() {
        lifecycleScope.launch {
            val userId = viewModel.getUserLoginId()

            if (viewModel.getUserStatus()) {
                viewModel.getUserLogin(userId).data.forEach {
                    binding.tvAuthenticate.text = it.name
                    binding.tvAuthenticate.isClickable = false
                }
                viewModel.getPasienByUser(userId).data.forEach { pasien ->
                    if (!pasien.imgPasien.isNullOrEmpty()) {
                        val path = Environment.getExternalStorageDirectory()
                        val imageFile = "${File(path, "/Android/data/com.medunnes.telemedicine${pasien.imgPasien}")}"
                        Glide.with(this@HomeFragment)
                            .load(imageFile)
                            .into(binding.ivUserPicture)
                            .clearOnDetach()
                    }
                }
                binding.tvAuthenticate.isClickable = false
            }

            val role = viewModel.getUserRole()
            if (role == 1) {
                menuDifference(true)
            } else {
                menuDifference(false)
            }
        }
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

    private fun menuDifference(dokter: Boolean) {
        with(binding) {
            if (dokter) {
                tvKonsultasi.text = getText(R.string.konsultasi_dokter)
                tvJanji.text = getText(R.string.janji_dokter)
            } else {
                tvKonsultasi.text = getText(R.string.konsultasi_sekarang)
                tvJanji.text = getText(R.string.janji_pasien)
            }
        }
    }

    override fun onClick(view: View?) {
        with(binding) {
             when(view) {
                 btnKonsultasi -> {
                     lifecycleScope.launch {
                         val role = viewModel.getUserRole()
                         if (role == 1) {
                             val intent = Intent(context, LayananDokterActivity::class.java)
                             intent.putExtra(LayananDokterActivity.FRAGMENT, "0")
                             startActivity(intent)
                         } else {
                             val intent = Intent(context, LayananPasienActivity::class.java)
                             intent.putExtra(LayananPasienActivity.FRAGMENT, "0")
                             startActivity(intent)
                         }
                     }
                 }

                 btnBuatJanji -> {
                     lifecycleScope.launch {
                         val role = viewModel.getUserRole()
                         if (role == 1) {
                             val intent = Intent(context, LayananDokterActivity::class.java)
                             intent.putExtra(LayananDokterActivity.FRAGMENT, "1")
                             startActivity(intent)
                         } else {
                             val intent = Intent(context, LayananPasienActivity::class.java)
                             intent.putExtra(LayananPasienActivity.FRAGMENT, "1")
                             startActivity(intent)
                         }
                     }
                 }

                 tvArtikelAll -> makeToast(undoneText())
                 tvFaskesAll -> makeToast(undoneText())
                 tvAuthenticate -> {
                     val intent = Intent(context, LoginActivity::class.java)
                     startActivity(intent)
                 }
                 else -> Log.d("CLICK", "error")
             }
        }
    }

    suspend fun getAllUser() {
        val allUser = viewModel.getAllUser("1")
        Log.d("USERS", allUser.data.toString())
    }
}