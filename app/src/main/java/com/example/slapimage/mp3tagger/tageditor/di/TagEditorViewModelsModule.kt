package com.example.slapimage.mp3tagger.tageditor.di

import com.example.slapimage.mp3tagger.tageditor.presentation.pages.tageditor.MetadataEditorViewModel
import com.example.slapimage.mp3tagger.tageditor.presentation.pages.tageditor.spotify.MetadataBottomSheetViewModel
import org.koin.android.ext.koin.androidContext
//import org.koin.core.module.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel  // <-- Fixes the issue
import org.koin.dsl.module

val tagEditorViewModelsModule = module {
    viewModel {
        MetadataEditorViewModel(
            context = androidContext(),
            stateHandle = get()
        )
    }
    viewModel {
        MetadataBottomSheetViewModel(
            searchService = get()
        )
    }
}