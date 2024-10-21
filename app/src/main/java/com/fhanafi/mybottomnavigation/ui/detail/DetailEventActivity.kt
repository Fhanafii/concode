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
import com.fhanafi.mybottomnavigation.R
import com.fhanafi.mybottomnavigation.data.local.room.EventDatabase
import com.fhanafi.mybottomnavigation.data.remote.response.ListEventsItem
import com.fhanafi.mybottomnavigation.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var viewModel: DetailEventViewModel
    private lateinit var eventDatabase: EventDatabase
    private var currentEvent: ListEventsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val eventId = intent.getStringExtra("EVENT_ID") ?: return
        viewModel = ViewModelProvider(this)[DetailEventViewModel::class.java]

        // Initialize the database
        eventDatabase = EventDatabase.getDatabase(this)

        setupObservers()
        setupListeners()

        // Fetch event details
        viewModel.getDetailEvent(eventId)
    }

    private fun setupObservers() {
        viewModel.detailEvent.observe(this) { event ->
            currentEvent = event
            bindEventData(event)
            viewModel.checkIfFavorite(event.id.toString(), eventDatabase)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar1.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.goToWeb.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.isFavorite.observe(this) { isFavorite ->
            // Update the favorite icon based on isFavorite status
            binding.imgFavorite.setImageResource(
                if (isFavorite) R.drawable.heart_24px else R.drawable.favorite_24px
            )
        }

    }

    private fun setupListeners() {
        // Set up the Favorite button click listener
        binding.favButtonContainer.setOnClickListener {
            currentEvent?.let { event ->
                viewModel.toggleFavoriteStatus(event, eventDatabase)
            }
        }

        // Set up the Go to Web button click listener
        binding.goToWeb.setOnClickListener {
            currentEvent?.link?.let { link -> openLinkInBrowser(link) }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindEventData(event: ListEventsItem) {
        supportActionBar?.title = event.name

        Glide.with(this).load(event.mediaCover).into(binding.image)
        binding.title.text = event.name
        binding.ownerName.text = event.ownerName
        binding.subTitle.text = "Informasi\n\n ${event.summary}"
        binding.sisakouta.text = "Sisa Kouta: ${event.quota - event.registrants}"
        binding.waktumulai.text = "Mulai: ${event.beginTime}"
        binding.waktuselesai.text = "Selesai: ${event.endTime}"

        val cleanedHtml = cleanHtmlContent(event.description)
        binding.text.text = HtmlCompat.fromHtml(cleanedHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun cleanHtmlContent(html: String): String {
        val imgRegex = "<img[^>]*>".toRegex()
        return html.replace(imgRegex, "")
            .replace("Time", "")
            .replace("Session", "")
            .replace("Speaker", "")
            .replace("\\s+".toRegex(), " ")
            .trim()
    }

    private fun openLinkInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

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
