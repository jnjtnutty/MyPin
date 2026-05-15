// Figma: https://www.figma.com/design/RbxZunWIJGyF1YrWcgE54q/MyPin-Mobile-Login-Design?node-id=5-2
package com.example.mypin.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mypin.R

private val BackgroundDark = Color(0xFF14120F)
private val White = Color(0xFFFFFFFF)
private val White18 = Color(0x2EFFFFFF)
private val White30 = Color(0x4DFFFFFF)
private val White80 = Color(0xCCFFFFFF)
private val White70 = Color(0xB3FFFFFF)
private val White75 = Color(0xBFFFFFFF)
private val White10 = Color(0x1AFFFFFF)
private val ButtonTextDark = Color(0xFF141414)

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onLoginSuccess((uiState as LoginUiState.Success).email)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        BackgroundImage()
        GradientOverlay()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .imePadding()
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            LogoBadge()

            Spacer(modifier = Modifier.weight(1f))

            HeroTitle()

            Spacer(modifier = Modifier.height(8.dp))

            HeroSubtitle()

            Spacer(modifier = Modifier.height(22.dp))

            EmailField(
                value = email,
                onValueChange = viewModel::onEmailChanged,
                isError = uiState is LoginUiState.Error,
                imeAction = ImeAction.Next,
                onImeAction = { /* focus moves to password */ }
            )

            Spacer(modifier = Modifier.height(12.dp))

            PasswordField(
                value = password,
                onValueChange = viewModel::onPasswordChanged,
                passwordVisible = passwordVisible,
                onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                isError = uiState is LoginUiState.Error,
                imeAction = ImeAction.Done,
                onImeAction = { viewModel.signIn() }
            )

            Spacer(modifier = Modifier.height(18.dp))

            SignInButton(
                onClick = viewModel::signIn,
                isLoading = uiState is LoginUiState.Loading
            )

            Spacer(modifier = Modifier.height(14.dp))

            DemoAccountLink(
                onClick = viewModel::fillDemoAccount
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun BackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.img_hero_bg),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun GradientOverlay() {
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
}

@Composable
private fun LogoBadge() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .clip(CircleShape)
            .background(White18)
            .border(1.dp, White30, CircleShape)
            .padding(start = 10.dp, end = 14.dp, top = 7.dp, bottom = 7.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.LocationOn,
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

@Composable
private fun HeroTitle() {
    Text(
        text = buildAnnotatedString {
            append("Your private\n")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("place diary.")
            }
        },
        color = White,
        fontSize = 34.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 40.sp
    )
}

@Composable
private fun HeroSubtitle() {
    Text(
        text = "Pin, review & remember every place that matters to you.",
        color = White80,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    )
}

@Composable
private fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    imeAction: ImeAction,
    onImeAction: () -> Unit
) {
    val fieldShape = RoundedCornerShape(14.dp)

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(1.dp, White18, fieldShape)
            .clip(fieldShape),
        placeholder = {
            Text(
                text = "Email",
                color = White70,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = null,
                tint = White70,
                modifier = Modifier.size(18.dp)
            )
        },
        textStyle = TextStyle(
            color = White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(onAny = { onImeAction() }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = White10,
            unfocusedContainerColor = White10,
            disabledContainerColor = White10,
            cursorColor = White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = fieldShape
    )
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isError: Boolean,
    imeAction: ImeAction,
    onImeAction: () -> Unit
) {
    val fieldShape = RoundedCornerShape(14.dp)

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(1.dp, White18, fieldShape)
            .clip(fieldShape),
        placeholder = {
            Text(
                text = "Password",
                color = White70,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                tint = White70,
                modifier = Modifier.size(18.dp)
            )
        },
        trailingIcon = {
            IconButton(onClick = onTogglePasswordVisibility) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = White70,
                    modifier = Modifier.size(18.dp)
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        textStyle = TextStyle(
            color = White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(onAny = { onImeAction() }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = White10,
            unfocusedContainerColor = White10,
            disabledContainerColor = White10,
            cursorColor = White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = fieldShape
    )
}

@Composable
private fun SignInButton(
    onClick: () -> Unit,
    isLoading: Boolean
) {
    val buttonShape = RoundedCornerShape(16.dp)

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(elevation = 6.dp, shape = buttonShape, ambientColor = Color(0x2E000000))
            .clip(buttonShape)
            .background(White, buttonShape)
            .clickable(enabled = !isLoading, onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = ButtonTextDark,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = "Sign In",
                color = ButtonTextDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.size(8.dp))
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = null,
                tint = ButtonTextDark,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun DemoAccountLink(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Use demo account",
            color = White75,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline
        )
    }
}


