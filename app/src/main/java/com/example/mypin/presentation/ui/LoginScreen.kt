package com.example.mypin.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mypin.R
import com.example.mypin.presentation.viewmodel.LoginViewModel
import com.example.mypin.ui.theme.BackgroundDark
import com.example.mypin.ui.theme.DarkText
import com.example.mypin.ui.theme.MyPinTheme
import com.example.mypin.ui.theme.White
import com.example.mypin.ui.theme.White70
import com.example.mypin.ui.theme.White75
import com.example.mypin.ui.theme.White80
import com.example.mypin.ui.theme.WhiteOverlay10
import com.example.mypin.ui.theme.WhiteOverlay18
import com.example.mypin.ui.theme.WhiteOverlay30
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    viewModel: LoginViewModel = koinViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val loginState by viewModel.loginState.observeAsState(UiState.Idle)

    LaunchedEffect(loginState) {
        if (loginState is UiState.Success) {
            onLoginSuccess()
        }
    }

    LoginScreenContent(
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        passwordVisible = passwordVisible,
        onPasswordVisibleChange = { passwordVisible = it },
        isLoading = loginState is UiState.Loading,
        errorMessage = if (loginState is UiState.Error) (loginState as UiState.Error).message else null,
        onSignInClick = { viewModel.login(email, password) },
        onDemoAccountClick = {
            val creds = viewModel.fillDemoCredentials()
            email = creds.email
            password = creds.password
        }
    )
}

@Composable
private fun LoginScreenContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: (Boolean) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onSignInClick: () -> Unit,
    onDemoAccountClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_login_hero),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0x0D000000),
                            Color(0x26000000),
                            Color(0xA6000000),
                            Color(0xF0000000)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(0f, Float.POSITIVE_INFINITY)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            LogoBadge(modifier = Modifier.padding(start = 20.dp, top = 32.dp))

            Spacer(modifier = Modifier.weight(1f, fill = false))

            HeroSection(modifier = Modifier.padding(horizontal = 20.dp))

            Spacer(modifier = Modifier.height(32.dp))

            LoginFormSection(
                email = email,
                onEmailChange = onEmailChange,
                password = password,
                onPasswordChange = onPasswordChange,
                passwordVisible = passwordVisible,
                onPasswordVisibleChange = onPasswordVisibleChange,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onSignInClick = onSignInClick,
                onDemoAccountClick = onDemoAccountClick,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LogoBadge(modifier: Modifier = Modifier) {
    Surface(
        shape = CircleShape,
        color = WhiteOverlay18,
        border = androidx.compose.foundation.BorderStroke(1.dp, WhiteOverlay30),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(start = 10.dp, end = 14.dp, top = 7.dp, bottom = 7.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "MyPin",
                color = White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun HeroSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Your private\nplace diary.",
            color = White,
            fontSize = 34.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Pin, review & remember every place that matters to you.",
            color = White80,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun LoginFormSection(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: (Boolean) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onSignInClick: () -> Unit,
    onDemoAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = {
                Text(
                    text = "Email",
                    color = White70,
                    fontSize = 15.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = null,
                    tint = White70,
                    modifier = Modifier.size(18.dp)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = WhiteOverlay10,
                unfocusedContainerColor = WhiteOverlay10,
                cursorColor = White,
                focusedBorderColor = WhiteOverlay18,
                unfocusedBorderColor = WhiteOverlay18,
                focusedTextColor = White,
                unfocusedTextColor = White
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color(0xFFFF6B6B),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = {
                Text(
                    text = "Password",
                    color = White70,
                    fontSize = 15.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = White70,
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { onPasswordVisibleChange(!passwordVisible) }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = null,
                        tint = White70,
                        modifier = Modifier.size(18.dp)
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = WhiteOverlay10,
                unfocusedContainerColor = WhiteOverlay10,
                cursorColor = White,
                focusedBorderColor = WhiteOverlay18,
                unfocusedBorderColor = WhiteOverlay18,
                focusedTextColor = White,
                unfocusedTextColor = White
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onSignInClick() }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onSignInClick,
            enabled = !isLoading,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = White,
                contentColor = DarkText,
                disabledContainerColor = White.copy(alpha = 0.5f),
                disabledContentColor = DarkText.copy(alpha = 0.5f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = DarkText,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Sign In",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        TextButton(
            onClick = onDemoAccountClick,
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
}

@Preview(showBackground = true, backgroundColor = 0xFF14120F)
@Composable
private fun LoginScreenPreview() {
    MyPinTheme {
        LoginScreenContent(
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            passwordVisible = false,
            onPasswordVisibleChange = {},
            isLoading = false,
            errorMessage = null,
            onSignInClick = {},
            onDemoAccountClick = {}
        )
    }
}
