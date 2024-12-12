@file:OptIn(ExperimentalMaterial3Api::class)

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zerbitzariapp.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }

    @Composable
    fun MyApp() {
        // Controlador de navegación
        val navController = rememberNavController()

        // Definir las pantallas en la navegación
        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") { LoginScreen(navController) } // Pantalla de login
            composable("home") { HomeScreen() } // Pantalla después del login
        }
    }

    @Composable
    fun LoginScreen(navController: NavController) {
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val passwordVisible = remember { mutableStateOf(false) }

        val primaryBackgroundColor = Color(0xFF345A7B)
        val buttonColor = Color(0xFF666E6C)
        val textFieldBorderColor = Color.White

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryBackgroundColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.inicio_de_sesion), // Reemplaza "inicio_de_sesion" con el nombre de tu imagen
                contentDescription = "Login",
                modifier = Modifier
                    .size(150.dp)
                    .padding(16.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = username.value,
                    onValueChange = { username.value = it },
                    label = { Text("Username") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = textFieldBorderColor,
                        unfocusedBorderColor = textFieldBorderColor,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = textFieldBorderColor,
                        unfocusedBorderColor = textFieldBorderColor,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    ),
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible.value) "Hide" else "Show"
                        Text(
                            text = icon,
                            color = Color.White,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable { passwordVisible.value = !passwordVisible.value }
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Login con navegación
            Button(
                onClick = {
                    // Aquí puedes realizar una validación, luego navegar a la siguiente pantalla
                    navController.navigate("home")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Login", color = Color.White, fontSize = 16.sp)
            }
        }
    }
    @Composable
    fun HomeScreen() {
        // Pantalla que se muestra después de iniciar sesión
        val primaryBackgroundColor = Color(0xFF345A7B) // Color de fondo
        Column(
            modifier = Modifier
                .fillMaxSize() // Llena toda la pantalla
                .background(primaryBackgroundColor)
                .padding(16.dp), // Relleno de la pantalla
            horizontalAlignment = Alignment.CenterHorizontally, // Centrado horizontal
            verticalArrangement = Arrangement.Top // Alineación superior para la imagen
        ) {
            // Imagen más grande en la parte superior
            Image(
                painter = painterResource(id = R.drawable.restaurant_logo), // Reemplaza "restaurant_logo" con el nombre de tu imagen
                contentDescription = "Descripción de la imagen", // Descripción de la imagen (importante para accesibilidad)
                modifier = Modifier
                    .size(350.dp) // Ajusta el tamaño de la imagen
                    .padding(bottom = 32.dp) // Espaciado debajo de la imagen
            )
            // Botón 1: "ESKAERAREKIN HASI"
            Button(
                onClick = { /* Acción del botón */ },
                modifier = Modifier
                    .width(250.dp) // Ajusta el ancho del botón
                    .padding(6.dp), // Relleno entre los botones
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C)) // Color de fondo del botón
            ) {
                Text(text = "ESKAERAREKIN HASI", color = Color.White) // Texto del botón
            }
            // Botón 2: "ESKAERAK IKUSI"
            Button(
                onClick = { /* Acción del botón */ },
                modifier = Modifier
                    .width(250.dp) // Ajusta el ancho del botón
                    .padding(6.dp), // Relleno entre los botones
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
            ) {
                Text(text = "ESKAERAK IKUSI", color = Color.White)
            }
            // Botón 3: "TXATEATU"
            Button(
                onClick = { /* Acción del botón */ },
                modifier = Modifier
                    .width(250.dp) // Ajusta el ancho del botón
                    .padding(6.dp), // Relleno entre los botones
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
            ) {
                Text(text = "TXATEATU", color = Color.White)
            }
        }
    }





    // Previews de las pantallas
    @Preview(showBackground = true)
    @Composable
    fun PreviewLoginScreen() {
        LoginScreen(rememberNavController()) // Se necesita pasar un NavController a la pantalla de login
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewHomeScreen() {
        HomeScreen() // Previsualización de la pantalla principal después de login
    }
}







