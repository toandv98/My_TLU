package com.toandv.mytlu

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel

class MainFragment: Fragment() {
    private val viewModel: MainViewModel by viewModels()
}

class MainViewModel: ViewModel()