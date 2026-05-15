// Figma: https://www.figma.com/design/RbxZunWIJGyF1YrWcgE54q/MyPin-Mobile-Login-Design?node-id=5-2 node-id=5:2
package com.example.mypin.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mypin.ui.theme.MyPinTheme

private val BackgroundDark = Color(0xFF14120F)
private val White = Color(0xFFFFFFFF)
private val White70 = Color(0xB3FFFFFF)
private val White80 = Color(0xCCFFFFFF)
private val White75 = Color(0xBFFFFFFF)
private val InputBackground = Color(0x1AFFFFFF)
private val InputBorder = Color(0x2EFFFFFF)
private val SignInButtonBg = Color(0xFFFFFFFF)
private val SignInButtonText = Color(0xFF141414)
private val ErrorText = Color(0xFFFF6B6B)

private val GradientOverlayBrush = Brush.verticalGradient(
    colors = listOf(Color.Transparent, Color.Transparent, Color(0xA6000000), Color(0xF0000000)),
    startY = 0f,
    endY = 1500f
)

@Composable
fun LoginScreen(
    uiState: LoginUiState<Unit>,
    onLogin: (String, String) -> Unit,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = GradientOverlayBrush)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            LogoBadge()

            Spacer(modifier = Modifier.weight(1f))

            HeroTitle()

            Spacer(modifier = Modifier.height(8.dp))

            HeroSubtitle()

            Spacer(modifier = Modifier.height(22.dp))

            EmailField(
                email = email,
                onEmailChange = { email = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            PasswordField(
                password = password,
                onPasswordChange = { password = it },
                isPasswordVisible = isPasswordVisible,
                onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
                onDone = {
                    keyboardController?.hide()
                    onLogin(email, password)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SignInButton(
                enabled = uiState !is LoginUiState.Loading,
                onClick = {
                    keyboardController?.hide()
                    onLogin(email, password)
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            DemoAccountLink(
                onClick = {
                    email = "nutty@gmail.com"
                    password = "123456"
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState is LoginUiState.Error) {
                Text(
                    text = uiState.message,
                    color = ErrorText,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun LogoBadge() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(InputBorder)
            .padding(start = 10.dp, top = 7.dp, end = 14.dp, bottom = 7.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = White,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "MyPin",
            color = White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun HeroTitle() {
    Text(
        text = "Your private\nplace diary.",
        color = White,
        fontSize = 34.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun HeroSubtitle() {
    Text(
        text = "Pin, review & remember every place that matters to you.",
        color = White80,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
}

@Composable
private fun EmailField(
    email: String,
    onEmailChange: (String) -> Unit
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        placeholder = {
            Text(
                text = "Email",
                color = White70,
                fontSize = 15.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = White70,
                modifier = Modifier.size(18.dp)
            )
        },
        textStyle = TextStyle(
            color = White,
            fontSize = 15.sp
        ),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = InputBackground,
            unfocusedContainerColor = InputBackground,
            cursorColor = White,
            focusedBorderColor = InputBorder,
            unfocusedBorderColor = InputBorder
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
private fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onDone: () -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        placeholder = {
            Text(
                text = "Password",
                color = White70,
                fontSize = 15.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = White70,
                modifier = Modifier.size(18.dp)
            )
        },
        trailingIcon = {
            IconButton(onClick = onTogglePasswordVisibility) {
                Icon(
                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                    tint = White70,
                    modifier = Modifier.size(18.dp)
                )
            }
        },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        textStyle = TextStyle(
            color = White,
            fontSize = 15.sp
        ),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = InputBackground,
            unfocusedContainerColor = InputBackground,
            cursorColor = White,
            focusedBorderColor = InputBorder,
            unfocusedBorderColor = InputBorder
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        )
    )
}

@Composable
private fun SignInButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = SignInButtonBg,
            contentColor = SignInButtonText,
            disabledContainerColor = SignInButtonBg.copy(alpha = 0.5f),
            disabledContentColor = SignInButtonText.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = "Sign In",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun DemoAccountLink(
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Use demo account",
            color = White75,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Preview
@Composable
private fun LogoBadgePreview() {
    MyPinTheme { LogoBadge() }
}

@Preview
@Composable
private fun HeroTitlePreview() {
    MyPinTheme { HeroTitle() }
}

@Preview
@Composable
private fun HeroSubtitlePreview() {
    MyPinTheme { HeroSubtitle() }
}

@Preview
@Composable
private fun EmailFieldPreview() {
    MyPinTheme { EmailField(email = "user@example.com", onEmailChange = {}) }
}

@Preview
@Composable
private fun PasswordFieldPreview() {
    MyPinTheme {
        PasswordField(
            password = "secret",
            onPasswordChange = {},
            isPasswordVisible = false,
            onTogglePasswordVisibility = {},
            onDone = {}
        )
    }
}

@Preview
@Composable
private fun PasswordFieldVisiblePreview() {
    MyPinTheme {
        PasswordField(
            password = "secret",
            onPasswordChange = {},
            isPasswordVisible = true,
            onTogglePasswordVisibility = {},
            onDone = {}
        )
    }
}

@Preview
@Composable
private fun SignInButtonPreview() {
    MyPinTheme { SignInButton(enabled = true, onClick = {}) }
}

@Preview
@Composable
private fun SignInButtonDisabledPreview() {
    MyPinTheme { SignInButton(enabled = false, onClick = {}) }
}

@Preview
@Composable
private fun DemoAccountLinkPreview() {
    MyPinTheme { DemoAccountLink(onClick = {}) }
}

@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun LoginScreenPreview() {
    MyPinTheme {
        LoginScreen(
            uiState = LoginUiState.Idle,
            onLogin = { _, _ -> },
            onLoginSuccess = {}
        )
    }
}

@Preview
@Composable
private fun LoginScreenLoadingPreview() {
    MyPinTheme {
        LoginScreen(
            uiState = LoginUiState.Loading,
            onLogin = { _, _ -> },
            onLoginSuccess = {}
        )
    }
}

@Preview
@Composable
private fun LoginScreenErrorPreview() {
    MyPinTheme {
        LoginScreen(
            uiState = LoginUiState.Error("Invalid email or password"),
            onLogin = { _, _ -> },
            onLoginSuccess = {}
        )
    }
}
