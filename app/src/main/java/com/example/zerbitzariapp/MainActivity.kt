@file:OptIn(ExperimentalMaterial3Api::class)

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zerbitzariapp.R
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.input.TextFieldValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }

    @Composable
    fun MyApp() {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") { LoginScreen(navController) }
            composable("home") { HomeScreen(navController) }
            composable("mesas") { MesaScreen { navController.navigate("detalleMesa/$it") } }
            composable("detalleMesa/{mesaId}") { backStackEntry ->
                val mesaId = backStackEntry.arguments?.getString("mesaId")?.toInt() ?: 0
                DetalleMesaScreen(mesaId)
            }
            composable("txat") { TxatScreen() } // Pantalla de Txat
            composable("eskariak") { EskariakIkusiScreen() } // Pantalla de Eskariak Ikusi
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
                painter = painterResource(id = R.drawable.inicio_de_sesion),
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
                    label = { Text("Erabiltzailea", color = Color.White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = textFieldBorderColor,
                        unfocusedBorderColor = textFieldBorderColor,
                        cursorColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Pasahitza", color = Color.White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = textFieldBorderColor,
                        unfocusedBorderColor = textFieldBorderColor,
                        cursorColor = Color.White
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

            Button(
                onClick = {
                    navController.navigate("home")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Saioa hasi", color = Color.White, fontSize = 16.sp)
            }
        }
    }

    @Composable
    fun HomeScreen(navController: NavController) {
        val primaryBackgroundColor = Color(0xFF345A7B)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryBackgroundColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.restaurant_logo),
                contentDescription = "Descripción de la imagen",
                modifier = Modifier
                    .size(350.dp)
                    .padding(bottom = 32.dp)
            )
            Button(
                onClick = { navController.navigate("mesas") },
                modifier = Modifier
                    .width(250.dp)
                    .padding(6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
            ) {
                Text(text = "ESKAERAREKIN HASI", color = Color.White)
            }

            Button(
                onClick = { navController.navigate("eskariak") },
                modifier = Modifier
                    .width(250.dp)
                    .padding(6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
            ) {
                Text(text = "ESKAERAK IKUSI", color = Color.White)
            }

            Button(
                onClick = { navController.navigate("txat") },
                modifier = Modifier
                    .width(250.dp)
                    .padding(6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
            ) {
                Text(text = "TXATEATU", color = Color.White)
            }
        }
    }

    @Composable
    fun MesaScreen(onMesaSelected: (Int) -> Unit) {
        val primaryBackgroundColor = Color(0xFF345A7B)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryBackgroundColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.restaurant_logo),
                contentDescription = "Descripción de la imagen",
                modifier = Modifier
                    .size(350.dp)
                    .padding(bottom = 32.dp)
            )
            Text("Aukeratu Mahai Bat", color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Generamos las 3 filas de 4 mesas
            for (row in 0 until 3) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Generamos las 4 mesas por fila
                    for (mesaId in (row * 4 + 1)..(row * 4 + 4)) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color.Gray, shape = CircleShape)
                                .clickable { onMesaSelected(mesaId) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "M$mesaId", color = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp)) // Espacio entre filas
            }
        }
    }

    @Composable
    fun DetalleMesaScreen(mesaId: Int) {
        val primaryBackgroundColor = Color(0xFF345A7B)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryBackgroundColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Detalles de la Mesa $mesaId", color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            // Aquí podrías agregar más contenido relacionado con la mesa
        }
    }

    // Pantalla de Txat
    @Composable
    fun TxatScreen() {
        val primaryBackgroundColor = Color(0xFF345A7B)
        val messageList = listOf("Mensaje 1", "Mensaje 2", "Mensaje 3", "Mensaje 4") // Lista de mensajes
        val newMessage = remember { mutableStateOf(TextFieldValue("")) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryBackgroundColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Txateatu", color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Lista de mensajes
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(messageList) { message ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = message, color = Color.White)
                    }
                }
            }

            // Campo para nuevo mensaje
            OutlinedTextField(
                value = newMessage.value,
                onValueChange = { newMessage.value = it },
                label = { Text("Escribe un mensaje", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para enviar mensaje
            Button(
                onClick = {
                    // Acción de enviar mensaje (podrías agregar la lógica aquí)
                    newMessage.value = TextFieldValue("") // Limpiar el campo después de enviar el mensaje
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Enviar", color = Color.White)
            }
        }
    }

    // Pantalla de Eskariak Ikusi (Ver pedidos)
    @Composable
    fun EskariakIkusiScreen() {
        val primaryBackgroundColor = Color(0xFF345A7B)
        val orders = listOf(
            "Pedido 1: Pizza - 2x - En preparación",
            "Pedido 2: Pasta - 1x - Listo",
            "Pedido 3: Ensalada - 3x - En camino"
        ) // Lista de pedidos

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryBackgroundColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Eskariak Ikusi", color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Lista de pedidos
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(orders) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = Color.Gray)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = order, color = Color.White, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun PreviewLoginScreen() {
        LoginScreen(navController = rememberNavController())
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun PreviewHomeScreen() {
        HomeScreen(navController = rememberNavController())
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun PreviewMesaScreen() {
        MesaScreen(onMesaSelected = {})
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun PreviewDetalleMesaScreen() {
        DetalleMesaScreen(mesaId = 1)
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun PreviewTxatScreen() {
        TxatScreen()
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun PreviewEskariakIkusiScreen() {
        EskariakIkusiScreen()
    }
}
