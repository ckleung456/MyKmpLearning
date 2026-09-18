import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import model.FeatureEvent

/**
 * Generic pub/sub bus for cross-feature communication (e.g. one feature
 * asking the app shell to navigate into another) without any feature module
 * depending on another feature's concrete implementation or API.
 *
 * Features publish a [FeatureEvent] subtype declared in their own -api
 * module; the single app shell per platform subscribes once via [events]
 * and maps received events to real navigation/side effects. FeatureEvent is
 * a plain (non-sealed) interface on purpose - new event types are additive
 * and never require existing collectors to handle every case.
 */
class FeatureEventBus {
    private val _events = MutableSharedFlow<FeatureEvent>(extraBufferCapacity = 8)
    val events: SharedFlow<FeatureEvent> = _events.asSharedFlow()

    fun publish(event: FeatureEvent) {
        _events.tryEmit(event)
    }
}
