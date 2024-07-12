package com.medunnes.telemedicine.ui.home

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.Faskes
import com.medunnes.telemedicine.data.response.ArtikelDataItem
import com.medunnes.telemedicine.databinding.FragmentHomeBinding
import com.medunnes.telemedicine.ui.adapter.ArticlesAdapter
import com.medunnes.telemedicine.ui.adapter.FaskesAdapter
import com.medunnes.telemedicine.ui.auth.login.LoginActivity
import com.medunnes.telemedicine.ui.dokter.LayananDokterActivity
import com.medunnes.telemedicine.ui.pasien.LayananPasienActivity
import com.medunnes.telemedicine.utils.imageBaseUrl
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val listArtikel = ArrayList<ArtikelDataItem>()
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

        //getArticleList()

        with(binding) {
            btnKonsultasi.setOnClickListener(this@HomeFragment)
            btnBuatJanji.setOnClickListener(this@HomeFragment)
            tvArtikelAll.setOnClickListener(this@HomeFragment)
            tvFaskesAll.setOnClickListener(this@HomeFragment)
            tvAuthenticate.setOnClickListener(this@HomeFragment)
            btnLogin.setOnClickListener(this@HomeFragment)
            tvFaskesTerdekat.visibility = View.GONE
            tvFaskesAll.visibility = View.GONE
            rvFaskesTerdekat.visibility = View.GONE
            cvKonsultasi.visibility = View.INVISIBLE
            cvBuatJanji.visibility = View.INVISIBLE
            tvKonsultasi.visibility = View.INVISIBLE
            tvJanji.visibility = View.INVISIBLE
        }

        lifecycleScope.launch { setUserProfile() }

        getFaskesList()
        showFaskesRecycleList()

        if (Build.VERSION.SDK_INT >= 33) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun setUserProfile() {
        val userStatus = viewModel.getUserStatus()
        if (userStatus) {
            showProgressBar(true)
            binding.tvMasukLayanan.visibility = View.GONE
            binding.btnLogin.visibility = View.GONE
            binding.cvKonsultasi.visibility = View.VISIBLE
            binding.cvBuatJanji.visibility = View.VISIBLE
            binding.tvKonsultasi.visibility = View.VISIBLE
            binding.tvJanji.visibility = View.VISIBLE
            val userId = viewModel.getUserLoginId()
            viewModel.getUserLogin(userId)
            viewModel.user.observe(viewLifecycleOwner) { data ->
                if (!data.isNullOrEmpty()) {
                    showProgressBar(false)
                    data.forEach {
                        binding.tvAuthenticate.text = it.name
                        binding.tvAuthenticate.isClickable = false
                    }
                }
            }
            setViewDifference(userId)
        }
    }

    private fun showPasienImageFromFile(userId: Int) {
        viewModel.getPasienByUserLogin(userId)
        viewModel.pasien.observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                data.forEach {
                    if (!it.imgPasien.isNullOrEmpty()) {
                        val imagePath = "${imageBaseUrl()}/${it.imgPasien}"
                        Glide.with(this)
                            .load(imagePath)
                            .into(binding.ivUserPicture)
                            .clearOnDetach()
                    }
                }
            }
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.tvAuthenticate.visibility = View.INVISIBLE
            binding.prgressBar2.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.tvAuthenticate.visibility = View.VISIBLE
            binding.prgressBar2.visibility = View.GONE
        }
    }

    private fun showDokterImageFromFile(userId: Int) {
        viewModel.getDokterByUserLogin(userId)
        viewModel.dokter.observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                data.forEach {
                    if (!it.imgDokter.isNullOrEmpty()) {
                        val imagePath = "${imageBaseUrl()}/${it.imgDokter}"
                        Glide.with(this)
                            .load(imagePath)
                            .into(binding.ivUserPicture)
                            .clearOnDetach()
                    }
                }
            }
        }
    }

    private suspend fun setViewDifference(userId: Int) {
        val role = viewModel.getUserRole()
        if (role == 1) {
            showDokterImageFromFile(userId)
            menuDifference(true)
        } else {
            showPasienImageFromFile(userId)
            menuDifference(false)
        }
    }

    private fun showArticleRecycleList(articleList: ArrayList<ArtikelDataItem>) {
        binding.rvArtikelKesehatan.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val artikelAdapter = ArticlesAdapter(articleList)
        binding.rvArtikelKesehatan.adapter = artikelAdapter

        artikelAdapter.setOnItemClickCallback(object : ArticlesAdapter.OnItemClickCallback {
            override fun onItemClicked(artikel: ArtikelDataItem) {
                makeToast(undoneText())
            }
        })

    }

    private fun getArticleList() {
        showProgressBar(true)
        lifecycleScope.launch {
            viewModel.getAllArtikel()
            viewModel.artikel.observe(viewLifecycleOwner) { artikel ->
                showProgressBar(false)
                listArtikel.clear()
                listArtikel.addAll(artikel)
                showArticleRecycleList(listArtikel)
            }
        }
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

    @Suppress("DEPRECATION")
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

    private fun intentLogin() {
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }

    private val requestNotificationPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                makeToast("Notifications permission granted")
            } else {
                makeToast("Notifications permission rejected")
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
                 tvAuthenticate -> intentLogin()
                 btnLogin -> intentLogin()
                 else -> Log.d("CLICK", "error")
             }
        }
    }
}