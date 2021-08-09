package com.hampson.parabara.ui.home.product_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.hampson.parabara.data.repository.NetworkState
import com.hampson.parabara.data.vo.Product
import io.reactivex.disposables.CompositeDisposable

class ProductInfoViewModel (private val productInfoRepository: ProductInfoRepository, private val productId: Long) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val updateProductLiveData: MediatorLiveData<Product> = MediatorLiveData()

    val productLiveData : LiveData<Product> by lazy {
        productInfoRepository.getProduct(compositeDisposable, productId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        productInfoRepository.getNetworkState()
    }

    fun getUpdateLiveData() : LiveData<Product> {
        return updateProductLiveData
    }

    fun updateProduct(id: Long, title: String, price: Long, content: String) {
        val repositoryLiveData: LiveData<Product> =
            productInfoRepository.updateProduct(compositeDisposable, id, title, price, content)

        updateProductLiveData.addSource(repositoryLiveData) {
            updateProductLiveData.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}