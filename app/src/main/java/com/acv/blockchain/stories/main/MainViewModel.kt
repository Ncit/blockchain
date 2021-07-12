package com.acv.blockchain.stories.main

import androidx.lifecycle.ViewModel
import com.acv.blockchain.network.models.ProfileInfoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _profile = MutableStateFlow<ProfileInfoModel?>(null)
    val profile: StateFlow<ProfileInfoModel?> = _profile

    private val _totalSum = MutableStateFlow<Long>(0)
    val totalSum: StateFlow<Long> = _totalSum

    fun setup(profileInfoModel: ProfileInfoModel) {
        _profile.value = profileInfoModel
    }

    fun appendSum(value: Long) {
        _totalSum.value = _totalSum.value + value
    }

    fun resetSum() {
        _totalSum.value = 0
    }

}