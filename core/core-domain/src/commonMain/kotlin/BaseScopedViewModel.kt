import androidx.lifecycle.ViewModel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

open class BaseScopedViewModel(scopeQualifier: String) : ViewModel(), KoinScopeComponent {
    override val scope: Scope by lazy {
        createScope(named(scopeQualifier))
    }

    override fun onCleared() {
        super.onCleared()
        scope.close()
    }
}