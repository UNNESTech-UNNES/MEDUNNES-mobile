package com.medunnes.telemedicine.ui.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.Message
import com.medunnes.telemedicine.data.model.Messanger
import com.medunnes.telemedicine.databinding.ActivityMessageBinding
import com.medunnes.telemedicine.ui.adapter.MessageAdapter
import com.medunnes.telemedicine.ui.pasien.konsultasiPasien.KonsultasiDetailFragment
import com.medunnes.telemedicine.ui.profile.ProfileViewModel
import com.medunnes.telemedicine.utils.imageBaseUrl
import kotlinx.coroutines.launch

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private val viewModel by viewModels<MessageViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: MessageAdapter
    private lateinit var messageRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)

//        messageRef = db.reference.child(MESSAGE_CHILD)

        lifecycleScope.launch { setMessager() }

        setContentView(binding.root)
    }

//    private fun setMessageAdapter() {
//        val layoutManager = LinearLayoutManager(this)
//        layoutManager.stackFromEnd = true
//        binding.rvMessage.layoutManager = layoutManager
//
//        db = Firebase.database
//        val options = FirebaseRecyclerOptions.Builder<Messanger>()
//            .setQuery()
//    }

//    private fun sendMessage() {
//        val message = Message(
//            firebase
//            binding.tieMessage.text
//        )
//
//        messageRef.push().setValue()
//    }

    private suspend fun setMessager() {
        val uid = viewModel.getUserLoginId()
        val role = if (viewModel.getUserRole() == 1) "dokter" else "pasien"
        if (role == "dokter") {
            viewModel.getPasienByUserLogin(uid)
            viewModel.pasien.observe(this) { pasien ->
                binding.tvMessangerName.text = pasien[0].namaPasien
                binding.tvMessangerStatus.text = pasien[0].status

                if (!pasien[0].imgPasien.isNullOrEmpty()) {
                    val imagePath = "${imageBaseUrl()}/${pasien[0].imgPasien}"
                    Glide.with(this)
                        .load(imagePath)
                        .into(binding.ivMessangerPicture)
                        .clearOnDetach()
                }
            }
        } else {
            viewModel.getDokterByUserLogin(intent.getIntExtra(DOKTER_ID, 0))
            viewModel.dokter.observe(this) { dokter ->
                binding.tvMessangerName.text = resources
                    .getString(R.string.nama_and_titel, dokter[0].titleDepan, dokter[0].namaDokter, dokter[0].titleBelakang)
                binding.tvMessangerStatus.text = dokter[0].status

                if (!dokter[0].imgDokter.isNullOrEmpty()) {
                    val imagePath = "${imageBaseUrl()}/${dokter[0].imgDokter}"
                    Glide.with(this)
                        .load(imagePath)
                        .into(binding.ivMessangerPicture)
                        .clearOnDetach()
                }
            }
        }
    }

    companion object {
        const val MESSAGE_CHILD = "message_child"
        const val DOKTER_ID = "dokter_id"
        const val PASIEN_ID = "pasien_id"
    }
}