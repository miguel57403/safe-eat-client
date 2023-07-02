package mb.safeEat.functions

import android.app.Activity
import android.os.Build
import java.io.Serializable

// https://stackoverflow.com/questions/72571804/getserializableextra-and-getparcelableextra-deprecated-what-is-the-alternative
@Suppress("DEPRECATION")
fun <T : Serializable?> getSerializableExtra(activity: Activity, name: String, clazz: Class<T>): T
{
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        activity.intent.getSerializableExtra(name, clazz)!!
    else
        activity.intent.getSerializableExtra(name) as T
}
