import androidx.compose.runtime.Composable

@Composable
fun<T> UIStatefulContent(
    state: UiState<T>,
    loadingContent: @Composable () -> Unit = { SimpleLoadingView() },
    errorContent: @Composable (String, Exception) -> Unit,
    successContent: @Composable (T) -> Unit
) {
    when(state) {
        is UiState.Loading -> loadingContent.invoke()
        is UiState.Error -> errorContent.invoke(state.message, state.exception)
        is UiState.Success -> successContent.invoke(state.data)
    }
}