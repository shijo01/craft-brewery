package com.shijo.beercraft.ui.main.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.shijo.beercraft.utils.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject


class ProfileFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    private lateinit var viewModel: ProfileViewModel

    companion object {
        private val TAG = ProfileFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.shijo.beercraft.R.layout.fragment_profile, container, false)
        Log.d(TAG, "onCreateView: view created")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: Profile fragment view created")
        viewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(
            ProfileViewModel::class.java
        )
        imageViewGithub.setOnClickListener { openGitHub() }
        imageViewLinkdIn.setOnClickListener { openLinkdIn() }
    }

    private fun openLinkdIn() {
        val url = "https://www.linkedin.com/in/shijopv/"
        startActivity(url)
    }

    private fun startActivity(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun openGitHub() {
        val url = "https://github.com/shijo01"
        startActivity(url)
    }

}