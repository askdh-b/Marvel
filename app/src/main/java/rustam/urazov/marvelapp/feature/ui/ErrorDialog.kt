package rustam.urazov.marvelapp.feature.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rustam.urazov.marvelapp.R
import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.core.platform.UiEvent
import rustam.urazov.marvelapp.core.platform.UiState

@Composable
fun ErrorDialog(
    state: ErrorDialogState,
    onOkClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state is ErrorDialogState.Visible) {
        AlertDialog(
            onDismissRequest = onDismiss, buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = onOkClick) {
                        Text(stringResource(id = R.string.ok))
                    }
                }
            },
            title = {
                Text(
                    text = state.message,
                    color = Color.Black,
                    fontSize = 12.sp
                )
            }, modifier = modifier.padding(16.dp)
        )
    }
}

sealed interface ErrorDialogState : UiState {
    data class Visible(val message: String) : ErrorDialogState
    object Invisible : ErrorDialogState
}

sealed class ErrorDialogEvent : UiEvent {
    data class Open(val failure: Failure) : ErrorDialogEvent()
    object Close : ErrorDialogEvent()
}