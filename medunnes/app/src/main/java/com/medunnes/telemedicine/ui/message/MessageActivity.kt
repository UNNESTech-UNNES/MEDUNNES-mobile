package com.medunnes.telemedicine.ui.message

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.Message
import com.medunnes.telemedicine.data.response.CatatanDataItem
import com.medunnes.telemedicine.databinding.ActivityMessageBinding
import com.medunnes.telemedicine.ui.adapter.MessageAdapter
import com.medunnes.telemedicine.ui.dialog.CatatanBottomSheetDialog
import com.medunnes.telemedicine.utils.imageBaseUrl
import kotlinx.coroutines.launch
import java.util.Date

class MessageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMessageBinding
    private val viewModel by viewModels<MessageViewModel> {
        ViewModelFactory.getInstance(this)
    }
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
        setViewBasedOnStatus()
        binding.sendButton.setOnClickListener(this)
        binding.tvCatatan.setOnClickListener(this)
        binding.ivCatatan.setOnClickListener(this)


        setContentView(binding.root)
    }

    private fun setViewBasedonRole(status: String) {
        lifecycleScope.launch {
            val role = viewModel.getUserRole()
            if (role == 2) {
                if (status == "berlangsung") {
                    binding.tvCatatan.visibility = View.GONE
                    binding.ivCatatan.visibility = View.GONE
                } else {
                    binding.tvCatatan.visibility = View.VISIBLE
                    binding.ivCatatan.visibility = View.VISIBLE
                    binding.tvCatatan.text = getString(R.string.lihat_catatan)
                }
            }
        }
    }

    private fun setViewBasedOnStatus() {
        val konsultasiId = intent.getIntExtra(KONSULTASI_ID, 0)
        viewModel.getKonsultasiById(konsultasiId)
        viewModel.konsultasi.observe(this) { konsultasi ->
            if (konsultasi[0].status == "berakhir") {
                binding.tilMessage.visibility = View.GONE
                binding.sendButton.visibility = View.GONE
                binding.tblSesiBerakhir.visibility = View.VISIBLE
                binding.ivMessangerStatus.setImageResource(R.drawable.circle_red)
                binding.tvMessangerStatus.text = getString(R.string.berakhir)
            } else {
                binding.ivMessangerStatus.setImageResource(R.drawable.circle_green)
                binding.tvMessangerStatus.text = getString(R.string.berlangsung)
            }
            setViewBasedonRole(konsultasi[0].status)
        }
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
                binding.tvMessangerStatus.text = pasien[0].status
                binding.tvMessangerName.text = pasien[0].user.name
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
            Log.d("doid", dokterId.toString())
            viewModel.getDokterById(dokterId)
            viewModel.dokter.observe(this) { dokter ->
                binding.tvMessangerStatus.text = dokter[0].status
                binding.tvMessangerName.text = dokter[0].user.name
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

    private fun showButtomSheet() {
        val cbsd = CatatanBottomSheetDialog()
        val bundle = Bundle()
        val konsultasiId = intent.getIntExtra(KONSULTASI_ID, 0)

        setCatatanButton(true)
        viewModel.getCatatanByKonsultasiId(konsultasiId)
        viewModel.catatan.observe(this@MessageActivity) { data ->
            supportFragmentManager.let {
                if (!cbsd.isAdded) {
                    if (data.isNotEmpty()) {
                        with(bundle) {
                            putInt(CatatanBottomSheetDialog.ID_CATATAN, data[0].idCatatan)
                            putString(CatatanBottomSheetDialog.GEJALA, data[0].gejala)
                            putString(CatatanBottomSheetDialog.DIAGNOSIS, data[0].diagnosis)
                            putString(CatatanBottomSheetDialog.CATATAN, data[0].catatan)
                        }

                        viewModel.getKonsultasiById(konsultasiId)
                        viewModel.konsultasi.observe(this) { konsultasi ->
                            bundle.putString(CatatanBottomSheetDialog.STATUS, konsultasi[0].status)
                        }

                        cbsd.arguments = bundle
                    }

                    cbsd.show(it, CatatanBottomSheetDialog.TAG)
                    cbsd.setOnItemClickCallback(object : CatatanBottomSheetDialog.OnItemClickCallback {
                        override fun onBtnSimpanCatatanClicked(catatan: CatatanDataItem) {
                            try {
                                if (
                                    catatan.catatan.isNotEmpty() &&
                                    catatan.gejala.isNotEmpty() &&
                                    catatan.diagnosis.isNotEmpty()
                                ) {
                                    if (data.isEmpty()) {
                                        insertCatatan(
                                            catatan.gejala,
                                            catatan.diagnosis,
                                            catatan.catatan
                                        )

                                        makeToast("Catatan disimpan")
                                    } else {
                                        updateCatatan(
                                            data[0].idCatatan,
                                            catatan.gejala,
                                            catatan.diagnosis,
                                            catatan.catatan
                                        )
                                        makeToast("Catatan diperbarui")
                                    }
                                    cbsd.dismiss()
                                } else {
                                    makeToast("Lengkapi catatan terlebih dahulu")
                                }
                            } catch (e: Exception) {
                                makeToast("Catatan gagal disimpan")
                                Log.d("INSERT/UPDATE CATATAN FAIL", e.message.toString())
                            }
                        }

                        override fun onBtnAkhiriPesanClicked() {
                            viewModel.getKonsultasiById(konsultasiId)
                            viewModel.konsultasi.observe(this@MessageActivity) { konsultasi ->
                                updateKonsultasi(
                                    konsultasiId,
                                    konsultasi[0].pasienId,
                                    konsultasi[0].dokterId,
                                    konsultasi[0].topik,
                                    "berakhir"
                                )
                            }
                            setViewBasedOnStatus()
                            cbsd.dismiss()
                        }

                    })
                    setCatatanButton(false)
                }
            }
        }
    }

    private fun insertCatatan(gejala: String, diagnosis: String, catatan: String) {
        val konsultasiId = intent.getIntExtra(KONSULTASI_ID, 0)
        lifecycleScope.launch {
            viewModel.insertCatatan(
                konsultasiId.toLong(),
                gejala,
                diagnosis,
                catatan
            )
        }
    }

    private fun updateCatatan(id: Int, gejala: String, diagnosis: String, catatan: String) {
        val konsultasiId = intent.getIntExtra(KONSULTASI_ID, 0)
        lifecycleScope.launch {
            viewModel.updateCatatan(
                id,
                konsultasiId.toLong(),
                gejala,
                diagnosis,
                catatan
            )
        }
    }

    private fun updateKonsultasi(
        id: Int,
        pasienId: Long,
        dokterId: Long,
        topik: String,
        status: String
    ) {
        lifecycleScope.launch {
            viewModel.updateKonsultasi(id, pasienId, dokterId, topik, status)
        }
    }

    private fun setCatatanButton(isClicked: Boolean) {
        if (isClicked) {
            binding.tvCatatan.visibility = View.INVISIBLE
            binding.ivCatatan.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.tvCatatan.visibility = View.VISIBLE
            binding.ivCatatan.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
            binding.tvCatatan -> showButtomSheet()
            binding.ivCatatan -> showButtomSheet()
        }
    }
}