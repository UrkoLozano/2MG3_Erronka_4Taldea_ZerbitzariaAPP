@file:OptIn(ExperimentalMaterial3Api::class)

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.zerbitzariapp.R


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController() // Inicializamos NavHostController
            MyApp(navController = navController) // Pasamos el NavController a la función App
        }
    }
}

@Composable
fun MyApp(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login" // Rutas disponibles en la aplicación
    ) {
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController) }

        // Navegación a la pantalla de mesas, pasando el id de la mesa
        composable("mesas") {
            MesaScreen { mesaId ->
                navController.navigate("detalleMesa/$mesaId")
            }
        }

        // Navegación al detalle de una mesa, pasando el id de la mesa en la URL
        composable(
            "detalleMesa/{mesaId}",
            arguments = listOf(navArgument("mesaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val mesaId = backStackEntry.arguments?.getInt("mesaId") ?: 0
            BebidaScreen(mesaId = mesaId)
        }

        // Navegación al chat
        composable("txat") { TxatScreen() }

        // Navegación a la pantalla de ver pedidos
        composable("eskariak") { EskariakIkusiScreen() }
    }
}


    @OptIn(ExperimentalMaterial3Api::class)
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
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.White,
                        focusedBorderColor = textFieldBorderColor,
                        unfocusedBorderColor = textFieldBorderColor,
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
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.White,
                        focusedBorderColor = textFieldBorderColor,
                        unfocusedBorderColor = textFieldBorderColor,
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
    //pantalla de chat
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
    //pantalla para ver las mesas que hay
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
            Text("Zein mahai da?", color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))

            for (row in 0 until 3) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
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
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

@Composable
fun BebidaScreen(mesaId: Int) {
    val primaryBackgroundColor = Color(0xFF345A7B)
    val bebidas = listOf("Coca-Cola", "Kas Naranja", "Kas Limón", "Agua", "Agua con gas", "Cerveza", "Vino Blanco", "Vino Tinto")

    val bebidaSeleccionada = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Mahai zenbakia: $mesaId",
            color = Color.White,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            bebidas.forEach { bebida ->
                Button(
                    onClick = {
                        bebidaSeleccionada.add(bebida)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
                ) {
                    Text(text = bebida, color = Color.White, fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Hautatutako Edariak:",
            color = Color.White,
            fontSize = 20.sp
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(bebidaSeleccionada) { bebida ->
                Text(text = bebida, color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun PrimerosScreen(mesaId: Int) {
    val primaryBackgroundColor = Color(0xFF345A7B)
    val primeros = listOf("Entsalada", "Arroza", "Zopa", "Makarroiak", "Patatak")

    val lehenHautatuak = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Mahai zenbakia: $mesaId",
            color = Color.White,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            primeros.forEach { lehenPlatera ->
                Button(
                    onClick = {
                        lehenHautatuak.add(lehenPlatera)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
                ) {
                    Text(text = lehenPlatera, color = Color.White, fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Hautatutako lehen platerak:",
            color = Color.White,
            fontSize = 20.sp
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(lehenHautatuak) { lehenPlatera ->
                Text(text = lehenPlatera, color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun SegundosScreen(mesaId: Int) {
    val primaryBackgroundColor = Color(0xFF345A7B)
    val segundos = listOf("Arraina", "Oilaskoa", "Txuleta", "Lekaleak", "Barazkiak")

    val bigarrenHautatuak = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Mahai zenbakia: $mesaId",
            color = Color.White,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            segundos.forEach { bigarrenPlatera ->
                Button(
                    onClick = {
                        bigarrenHautatuak.add(bigarrenPlatera)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
                ) {
                    Text(text = bigarrenPlatera, color = Color.White, fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Hautatutako bigarren platerak:",
            color = Color.White,
            fontSize = 20.sp
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(bigarrenHautatuak) { bigarrenPlatera ->
                Text(text = bigarrenPlatera, color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun ComandoTotalScreen(mesaId: Int, productos: List<String>) {
    val primaryBackgroundColor = Color(0xFF345A7B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Comanda de la Mesa $mesaId",
            color = Color.White,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(productos) { producto ->
                Text(
                    text = producto,
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Total de la Comanda: ${productos.size * 5}€", // Precio fijo por producto (por ejemplo)
            color = Color.White,
            fontSize = 20.sp
        )
    }
}



// Pantalla de Txat
    @OptIn(ExperimentalMaterial3Api::class)
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
                label = { Text("Idatzi zure mezua", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
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
                Text(text = "Bidali", color = Color.White)
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

    @Preview(showBackground = true )
    @Composable
    fun PreviewLoginScreen() {
        LoginScreen(navController = rememberNavController())
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewHomeScreen() {
        HomeScreen(navController = rememberNavController())
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewMesaScreen() {
        MesaScreen(onMesaSelected = {})
    }

    @Composable
    @Preview
    fun PreviewBebidaScreen() {
        BebidaScreen(mesaId = 0 )
    }

    @Composable
    @Preview
    fun PreviewPrimerosScreen() {
        PrimerosScreen(mesaId = 0 )
    }
    @Composable
    @Preview
    fun PreviewSegundosScreen() {
        SegundosScreen(mesaId = 0 )
    }

    @Composable
    @Preview
    fun PreviewComandoTotalScreen() {
        ComandoTotalScreen(mesaId = 0, productos = listOf("Producto 1", "Producto 2"))
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewTxatScreen() {
        TxatScreen()
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewEskariakIkusiScreen() {
        EskariakIkusiScreen()
    }

