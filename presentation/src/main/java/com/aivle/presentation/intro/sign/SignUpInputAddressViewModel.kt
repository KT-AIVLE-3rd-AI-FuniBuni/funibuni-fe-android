package com.aivle.presentation.intro.sign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aivle.domain.model.kakao.KakaoAddress
import com.aivle.domain.model.kakao.KakaoAddressDocument
import com.aivle.domain.response.DataResponse
import com.aivle.domain.usecase.kakao.GetKakaoAddressListUseCase
import com.aivle.domain.usecase.kakao.GetKakaoCoord2AddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpInputAddressViewModel @Inject constructor(
    private val GetKakaoAddressListUseCase: GetKakaoAddressListUseCase,
    private val GetKakaoCoord2AddressUseCase: GetKakaoCoord2AddressUseCase,
) : ViewModel() {

    private val _eventFlow: MutableStateFlow<Event> = MutableStateFlow(Event.Init)
    val eventFlow: SharedFlow<Event> get() = _eventFlow

    private val addresses: MutableList<KakaoAddress> = mutableListOf()
    private var lastQuery: String? = null

    private val currentPage: Int
        get() = addresses.size

    private val isEndPage: Boolean
        get() = addresses.isEmpty() || addresses.last().meta.is_end

    fun searchAddress(query: String) {
        if (query.isBlank()) {
            return
        }

        addresses.clear()
        lastQuery = query

        viewModelScope.launch {
            // Loading
            _eventFlow.emit(Event.Loading)

            GetKakaoAddressListUseCase(query, currentPage + 1, SIZE)
                .catch {
                    // 통신 에러
                    _eventFlow.emit(Event.Failure(it.message))
                }
                .collect { response -> when (response) {
                    // 응답 성공
                    is DataResponse.Success -> {
                        val address = response.data
                        val documents = address.documents
                            .filterNot { it.road_address == null }

                        if (documents.isEmpty()) { // 검색 결과 0개
                            _eventFlow.emit(Event.Success.Empty)
                        } else { // 검색 결과 있음
                            _eventFlow.emit(Event.Success.SearchAddress(documents))
                            addresses.add(address)
                        }
                    }
                    // 응답 실패
                    is DataResponse.Failure -> {
                        _eventFlow.emit(Event.Failure(response.message))
                    }
                }}
        }
    }

    fun searchAddressNextPage() {
        if (lastQuery == null || isEndPage) {
            return
        }

        viewModelScope.launch {
            GetKakaoAddressListUseCase(lastQuery!!, currentPage + 1, SIZE)
                .catch {
                    // 통신 에러
                    _eventFlow.emit(Event.Failure(it.message))
                }
                .collect { response -> when (response) {
                    // 응답 성공
                    is DataResponse.Success -> {
                        val address = response.data
                        val documents = response.data.documents
                            .filterNot { it.road_address == null }

                        if (documents.isEmpty()) { // 검색 결과 0개
                        } else {
                            _eventFlow.emit(Event.Success.SearchAddress(addresses.flatMap { it.documents }))
                            addresses.add(address)
                        }
                    }
                    // 응답 실패
                    is DataResponse.Failure -> {
                        _eventFlow.emit(Event.Failure(response.message))
                    }
                }}
        }
    }

    fun coordinateToAddress(x: Double, y: Double) {
        viewModelScope.launch {
            GetKakaoCoord2AddressUseCase(x, y)
                .catch {
                    // 통신 에러
                    _eventFlow.emit(Event.Failure(it.message))
                }
                .collect { response -> when (response) {
                    // 응답 성공
                    is DataResponse.Success -> {
                        val firstDocument = response.data.documents.firstOrNull()
                        if (firstDocument == null) { // 검색 결과 0개
                            _eventFlow.emit(Event.Success.Empty)
                        } else { // 검색 결과 있음
                            val addressName = firstDocument.road_address?.address_name
                                ?: firstDocument.land_address?.address_name!!

                            _eventFlow.emit(Event.Success.CoordinateToAddress(addressName))

                            searchAddress(addressName)
                        }
                    }
                    // 응답 실패
                    is DataResponse.Failure -> {
                        _eventFlow.emit(Event.Failure(response.message))
                    }
                }}
        }
    }

    sealed class Event {
        object Init: Event()
        object Loading : Event()
        sealed class Success : Event() {
            data class SearchAddress(val documents: List<KakaoAddressDocument>) : Success()
            data class CoordinateToAddress(val addressName: String) : Success()
            object Empty : Success()
        }
        data class Failure(val message: String?) : Event()
    }

    companion object {
        private const val SIZE = 30
    }
}