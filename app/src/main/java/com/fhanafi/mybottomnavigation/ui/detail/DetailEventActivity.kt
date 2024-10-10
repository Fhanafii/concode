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

        // Set up the ActionBar (or Toolbar) and enable the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Event Detail"

        val eventId = intent.getStringExtra("EVENT_ID") ?: return
        viewModel = ViewModelProvider(this)[DetailEventViewModel::class.java]

        viewModel.getDetailEvent(eventId)

        // Observe data
        viewModel.detailEvent.observe(this) { event ->
            bindEventData(event) // event will now be of type ListEventsItem
        }

        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar1.visibility = View.VISIBLE // Show ProgressBar
                binding.goToWeb.visibility = View.GONE // Hide Button
            } else {
                binding.progressBar1.visibility = View.GONE // Hide ProgressBar
                binding.goToWeb.visibility = View.VISIBLE // Show Button
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindEventData(event: ListEventsItem) {
        event.let {
            Glide.with(this).load(it.mediaCover).into(binding.image)
            binding.title.text = it.name
            binding.subTitle.text = "Informasi\n\n ${it.summary}"
            binding.sisakouta.text = "Sisa Kouta: ${it.quota}"
            binding.waktumulai.text = it.beginTime

            // Clean the HTML content
            val cleanedHtml = cleanHtmlContent(it.description)

            // Render the cleaned HTML content
            binding.text.text = HtmlCompat.fromHtml(
                cleanedHtml,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            binding.goToWeb.setOnClickListener {
                openLinkInBrowser(event.link)
            }
        }
    }

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

    // Handle the Up button action
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Navigate back when the back button is pressed
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
