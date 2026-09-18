import android.content.Context
import android.os.Build
import dalvik.system.DexClassLoader
import model.FeatureApi

actual class FeatureLoader actual constructor() {
    @androidx.annotation.RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    actual suspend fun loadFeature(location: String, featureId: String): FeatureApi? {
        return try {
            val context = AndroidContextHolder.appContext
            val classLoader = DexClassLoader(
                location,
                context.codeCacheDir.absolutePath,
                null,
                context.classLoader
            )
            val clazz = classLoader.loadClass(featureId)
            clazz.getDeclaredConstructor().newInstance() as? FeatureApi
        } catch (e: Exception) {
            null
        }
    }
}

object AndroidContextHolder {
    lateinit var appContext: Context
}