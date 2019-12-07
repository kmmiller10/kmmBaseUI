package me.kmmiller.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.kmmiller.baseui.KmmBaseFragment
import me.kmmiller.sample.databinding.ActivityMainBinding

class MainFragment : KmmBaseFragment() {
    private lateinit var binding: ActivityMainBinding

    override fun getTitle(): String = getString(R.string.home)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityMainBinding.inflate(inflater, container, false)
        return binding.root
    }
}