package com.kurostream.tv.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinLockViewModel @Inject constructor(
    private val profileRepository: com.kurostream.tv.data.local.profile.ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PinLockUiState())
    val uiState: StateFlow<PinLockUiState> = _uiState.asStateFlow()

    fun onDigitPressed(digit: Int) {
        if (_uiState.value.enteredPin.length < 4) {
            _uiState.value = _uiState.value.copy(
                enteredPin = _uiState.value.enteredPin + digit.toString()
            )
        }
    }

    fun onBackspacePressed() {
        if (_uiState.value.enteredPin.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                enteredPin = _uiState.value.enteredPin.dropLast(1)
            )
        }
    }
    fun onVerifyPin(profileId: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            val isValid = profileRepository.verifyPin(profileId, _uiState.value.enteredPin)
            if (isValid) {
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(
                    enteredPin = "",
                    errorMessage = "Incorrect PIN. Please try again."
                )
                onFailure()
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class PinLockUiState(
    val enteredPin: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

@Composable
fun PinLockScreen(
    profileId: String,
    title: String = "Enter PIN",
    onUnlockSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: PinLockViewModel = androidx.lifecycle.viewmodel.compose.hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // PIN Display
            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                if (index < uiState.enteredPin.length) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (index < uiState.enteredPin.length) "•" else "",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }

            // Error message
            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            // Number pad
            Column(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // First row
                Row(horizontalArrangement = Arrangement.Center) {
                    PinButton(number = 1) { viewModel.onDigitPressed(1) }
                    PinButton(number = 2) { viewModel.onDigitPressed(2) }
                    PinButton(number = 3) { viewModel.onDigitPressed(3) }
                }

                // Second row
                Row(horizontalArrangement = Arrangement.Center) {
                    PinButton(number = 4) { viewModel.onDigitPressed(4) }
                    PinButton(number = 5) { viewModel.onDigitPressed(5) }
                    PinButton(number = 6) { viewModel.onDigitPressed(6) }
                }

                // Third row
                Row(horizontalArrangement = Arrangement.Center) {
                    PinButton(number = 7) { viewModel.onDigitPressed(7) }
                    PinButton(number = 8) { viewModel.onDigitPressed(8) }
                    PinButton(number = 9) { viewModel.onDigitPressed(9) }
                }

                // Fourth row
                Row(horizontalArrangement = Arrangement.Center) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Cancel",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    PinButton(number = 0) { viewModel.onDigitPressed(0) }
                    IconButton(
                        onClick = { viewModel.onBackspacePressed() },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Backspace",
                            tint = MaterialTheme.colorScheme.onSurface                        )
                    }
                }
            }
        }
    }

    // Handle verification success
    LaunchedEffect(uiState.enteredPin) {
        if (uiState.enteredPin.length == 4) {
            viewModel.onVerifyPin(profileId, 
                onSuccess = { onUnlockSuccess() },
                onFailure = { /* Already handled in VM */ }
            )
        }
    }
}

@Composable
private fun PinButton(
    number: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(64.dp)
            .padding(4.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text(
            text = number.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
