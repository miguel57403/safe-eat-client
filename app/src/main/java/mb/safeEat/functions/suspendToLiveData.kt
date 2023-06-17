package mb.safeEat.functions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

typealias SuspendCallback<T> = suspend () -> T

fun <T> suspendToLiveData(callback: SuspendCallback<T>): LiveData<Result<T>> {
    // WARNING: Maybe this was not necessary
    // I will keep this to avoid exposing how to set up the CoroutineScope
    val resultLiveData = MutableLiveData<Result<T>>()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = callback()
            resultLiveData.postValue(Result.success(response))
        } catch (e: Exception) {
            resultLiveData.postValue(Result.failure(e))
        }
    }

    return resultLiveData
}
