package com.elevatestudio.careerlink.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.elevatestudio.careerlink.R
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.TextGray

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    // State buat ngatur visibility password (nampilin/sembunyiin)
    val isPasswordVisible = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = PrimaryGreen,
            focusedLabelColor = PrimaryGreen,
            cursorColor = PrimaryGreen
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        // Logika buat nampilin/sembunyiin password
        visualTransformation = if (isPassword && !isPasswordVisible.value) {
            PasswordVisualTransformation() // Kalo password & disembunyiin -> jadi bintang-bintang
        } else {
            VisualTransformation.None // Kalo bukan password / ditampilin -> teks biasa
        },
        trailingIcon = {
            if (isPassword) {
                // Tombol ikon mata
                IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                    Icon(
                        painter = if (isPasswordVisible.value) {
                            painterResource(id = R.drawable.ic_eye_on) // Mata kebuka
                        } else {
                            painterResource(id = R.drawable.ic_eye_off) // Mata ketutup
                        },
                        contentDescription = "Toggle password visibility",
                        tint = TextGray
                    )
                }
            }
        }
    )
}