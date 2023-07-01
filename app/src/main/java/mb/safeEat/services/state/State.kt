package mb.safeEat.services.state

import androidx.lifecycle.MutableLiveData
import mb.safeEat.services.api.models.User

data class State(
    var user: MutableLiveData<User?>,
) {
    fun logout() {
        user.postValue(null)
    }
}

val state = State(
    MutableLiveData<User?>()
)
