package com.fhanafi.mybottomnavigation.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.fhanafi.mybottomnavigation.data.response.ListEventsItem
import com.fhanafi.mybottomnavigation.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var viewModel: DetailEventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val eventId = intent.getStringExtra("EVENT_ID") ?: return
        viewModel = ViewModelProvider(this)[DetailEventViewModel::class.java]

        viewModel.getDetailEvent(eventId)

        viewModel.detailEvent.observe(this) { event ->
            bindEventData(event)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar1.visibility = View.VISIBLE
                binding.goToWeb.visibility = View.GONE
            } else {
                binding.progressBar1.visibility = View.GONE
                binding.goToWeb.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindEventData(event: ListEventsItem) {
        event.let {
            supportActionBar?.title = it.name

            Glide.with(this).load(it.mediaCover).into(binding.image)
            binding.title.text = it.name
            binding.ownerName.text = it.ownerName
            binding.subTitle.text = "Informasi\n\n ${it.summary}"
            binding.sisakouta.text = "Sisa Kouta: ${it.registrants}"
            binding.kouta.text = "Kouta: ${it.quota}"
            binding.waktumulai.text = "Mulai: ${it.beginTime}"
            binding.waktuselesai.text = "Selesai: ${it.endTime}"

            // Membersihkan HTML content
            val cleanedHtml = cleanHtmlContent(it.description)

            binding.text.text = HtmlCompat.fromHtml(
                cleanedHtml,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            binding.goToWeb.setOnClickListener {
                openLinkInBrowser(event.link)
            }
        }
    }
    // Fungsi untuk membersihkan HTML content dari gambar
    private fun cleanHtmlContent(html: String): String {
        val imgRegex = "<img[^>]*>".toRegex()
        var cleanedHtml = html.replace(imgRegex, "")
        cleanedHtml = cleanedHtml.replace("Time", "")
            .replace("Session", "")
            .replace("Speaker", "")
        cleanedHtml = cleanedHtml.replace("\\s+".toRegex(), " ").trim()
        return cleanedHtml
    }

    private fun openLinkInBrowser(url: String?) {
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        }
    }

    // tombol back di toolbar untuk kembali ke activity sebelumnya
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
