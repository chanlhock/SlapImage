package com.example.slapimage.mp3tagger.mediastore.di

import com.example.slapimage.mp3tagger.mediastore.presentation.MediaStorePageViewModel
//import org.koin.core.module.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel  // <-- Fixes the issue
import org.koin.dsl.module

val mediaStoreViewModelsModule = module {
    viewModel { MediaStorePageViewModel(get()) }
}