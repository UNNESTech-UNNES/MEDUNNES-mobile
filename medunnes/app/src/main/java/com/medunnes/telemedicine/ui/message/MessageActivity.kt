package com.medunnes.telemedicine.ui.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.medunnes.telemedicine.databinding.ActivityMessageBinding
import com.medunnes.telemedicine.ui.adapter.MessageAdapter
import com.medunnes.telemedicine.utils.imageBaseUrl
import kotlinx.coroutines.launch
import java.util.Date

class MessageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMessageBinding
    private val viewModel by viewModels<MessageViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: MessageAdapter
    private lateinit var messageRef: DatabaseReference
    private lateinit var email: String
    private lateinit var options: FirebaseRecyclerOptions<Message>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        db = Firebase.database

        val konsultasiId = intent.getIntExtra(KONSULTASI_ID, 0)
        val childMessage = "konsultasi_${konsultasiId}"
        messageRef = db.reference.child(childMessage)

        lifecycleScope.launch { setMessager() }
        setMessageAdapter()
        binding.sendButton.setOnClickListener(this)


        setContentView(binding.root)
    }

    private fun setMessageAdapter() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        binding.rvMessage.layoutManager = layoutManager
        options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(messageRef, Message::class.java)
            .build()

        getUserData()
        viewModel.user.observe(this@MessageActivity) { user ->
            email = user[0].email
            adapter = MessageAdapter(options, email)
            binding.rvMessage.adapter = adapter
        }
    }

    private fun getUserData() {
        lifecycleScope.launch {
            val uid = viewModel.getUserLoginId()
            viewModel.getUserLogin(uid)
        }
    }

    private fun sendMessage() {
        val message = Message(
            email,
            "${binding.tieMessage.text}",
            Date().time
        )

        messageRef.push().setValue(message) { error, _ ->
            if (error != null) {
                Log.d("EMAIL SENDER", email)
                Toast.makeText(this, "Pesan gagal terkirm: " + error.message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Pesan berhasil terkirim", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tieMessage.setText("")
    }

    private suspend fun setMessager() {
        val role = viewModel.getUserRole()
        if (role == 1) {
            val pasienId = intent.getIntExtra(PASIEN_ID, 0)
            viewModel.getPasienrById(pasienId)
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
            val dokterId = intent.getIntExtra(DOKTER_ID, 0)
            viewModel.getDokterByUserLogin(dokterId)
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
        const val DOKTER_ID = "dokter_id"
        const val PASIEN_ID = "pasien_id"
        const val KONSULTASI_ID = "konsultasi_id"
    }

    override fun onResume() {
        super.onResume()
        getUserData()
        viewModel.user.observe(this@MessageActivity) { user ->
            email = user[0].email
            adapter = MessageAdapter(options, email)
            binding.rvMessage.adapter = adapter
            adapter.startListening()
        }
    }

    public override fun onPause() {
        getUserData()
        viewModel.user.observe(this@MessageActivity) { user ->
            email = user[0].email
            adapter = MessageAdapter(options, email)
            binding.rvMessage.adapter = adapter
            adapter.stopListening()
        }
        super.onPause()
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.sendButton -> sendMessage()
        }
    }
}