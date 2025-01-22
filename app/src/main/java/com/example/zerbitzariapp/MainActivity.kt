@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.zerbitzariapp
//import android.os.Build.VERSION_CODES.R

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.myapplication.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect



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

        // Navegación a la pantalla de mesas
        composable("mesas") {
            MesaScreen { mesaId ->
                navController.navigate("detalleMesa/$mesaId")
            }
        }

        // Navegación al detalle de una mesa (bebidas)
        composable(
            "detalleMesa/{mesaId}",
            arguments = listOf(navArgument("mesaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val mesaId = backStackEntry.arguments?.getInt("mesaId") ?: 0
            BebidaScreen(mesaId = mesaId, navController = navController)
        }

        // Navegación a primeros platos
        composable(
            "primeros/{mesaId}/{productos}",
            arguments = listOf(
                navArgument("mesaId") { type = NavType.IntType },
                navArgument("productos") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mesaId = backStackEntry.arguments?.getInt("mesaId") ?: 0
            val productos = backStackEntry.arguments?.getString("productos")?.split(",") ?: emptyList()
            PrimerosScreen(mesaId = mesaId,  navController = navController)
        }

        // Navegación a segundos platos
        composable(
            "segundos/{mesaId}/{productos}",
            arguments = listOf(
                navArgument("mesaId") { type = NavType.IntType },
                navArgument("productos") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mesaId = backStackEntry.arguments?.getInt("mesaId") ?: 0
            val productos = backStackEntry.arguments?.getString("productos")?.split(",") ?: emptyList()
            SegundosScreen(mesaId = mesaId, productos = productos, navController = navController)
        }

        // Navegación a la pantalla final de resumen del pedido
        composable(
            "comandoTotal/{mesaId}/{productos}",
            arguments = listOf(
                navArgument("mesaId") { type = NavType.IntType },
                navArgument("productos") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mesaId = backStackEntry.arguments?.getInt("mesaId") ?: 0
            val productos = backStackEntry.arguments?.getString("productos")?.split(",") ?: emptyList()
            ComandoTotalScreen(mesaId = mesaId, productos = productos, navController = navController)
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val context = LocalContext.current // Aquí obtenemos el contexto

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
                if (username.value.isNotEmpty() && password.value.isNotEmpty()) {
                    val url = "http://10.0.2.2/login.php" // Cambia IP para dispositivo físico
                    val client = OkHttpClient()
                    val formBody = FormBody.Builder()
                        .add("username", username.value) // 'username' corresponde a 'izena'
                        .add("password", password.value) // 'password' corresponde a 'pasahitza'
                        .build()

                    val request = Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                            Log.d("LoginError", "Error al conectar: ${e.message}")
                            // Cambia al hilo principal para mostrar un Toast
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val responseBody = response.body?.string()
                            Log.d("LoginResponse", "Response recibido: ${responseBody ?: "nulo"}")

                            if (response.isSuccessful) {
                                Log.d("LoginResponse", "Conexión exitosa con el servidor")
                                if (responseBody == "success") {
                                    Log.d("LoginResponse", "Inicio de sesión exitoso. Navegando a 'home'")
                                    // Cambia al hilo principal para navegar
                                    Handler(Looper.getMainLooper()).post {
                                        navController.navigate("home")
                                    }
                                } else {
                                    Log.d("LoginResponse", "Credenciales incorrectas. ResponseBody: $responseBody")
                                    Handler(Looper.getMainLooper()).post {
                                        Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Log.d("LoginResponse", "Error en la conexión con el servidor. Código: ${response.code}")
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(context, "Error en el servidor", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    })
                } else {
                    Log.d("LoginValidation", "Los campos están vacíos")
                    Toast.makeText(context, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
                }
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
                for (mesaId in (row * 2 + 1)..(row * 2 + 2)) {
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
fun BebidaScreen(mesaId: Int, navController: NavController, viewModel: BebidaViewModel = viewModel()) {
    val primaryBackgroundColor = Color(0xFF345A7B)
    val bebidas by viewModel.bebidas.collectAsState() // Observa la lista de bebidas desde el ViewModel
    val bebidaSeleccionada = remember { mutableStateMapOf<String, Int>() }

    // Llamar al método de ViewModel para cargar datos si aún no se han cargado
    LaunchedEffect(Unit) {
        viewModel.obtenerBebidas()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mahai zenbakia: $mesaId",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar las bebidas dinámicamente
            if (bebidas.isEmpty()) {
                Text(
                    text = "Kargatzen...",
                    color = Color.White,
                    fontSize = 18.sp
                )
            } else {
                bebidas.forEach { bebida ->
                    Button(
                        onClick = {
                            bebidaSeleccionada[bebida] = (bebidaSeleccionada[bebida] ?: 0) + 1
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

            if (bebidaSeleccionada.isNotEmpty()) {
                Text(
                    text = "Hautatutako Edariak:",
                    color = Color.White,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    items(bebidaSeleccionada.keys.toList()) { bebida ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    val currentQuantity = bebidaSeleccionada[bebida] ?: 0
                                    if (currentQuantity > 1) {
                                        bebidaSeleccionada[bebida] = currentQuantity - 1
                                    } else {
                                        bebidaSeleccionada.remove(bebida)
                                    }
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = bebida,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "x${bebidaSeleccionada[bebida]}",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
            ) {
                Text(text = "Atzera", color = Color.White)
            }

            Button(
                onClick = {
                    val productos = bebidaSeleccionada.map { "${it.key} x${it.value}" }
                    navController.navigate("primeros/$mesaId/${productos.joinToString(",")}")
                },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
            ) {
                Text(text = "Hurrengoa", color = Color.White)
            }
        }
    }
}

class BebidaViewModel : ViewModel() {
    private val _bebidas = MutableStateFlow<List<String>>(emptyList())
    val bebidas: StateFlow<List<String>> = _bebidas

    fun obtenerBebidas() {
        viewModelScope.launch {
            try {
                Log.d("BebidaViewModel", "Iniciando obtención de bebidas...")
                val bebidasDesdeServidor = obtenerBebidasDesdeServidor()
                if (bebidasDesdeServidor.isEmpty()) {
                    Log.d("BebidaViewModel", "No se recibieron bebidas desde el servidor.")
                } else {
                    Log.d("BebidaViewModel", "Bebidas obtenidas: $bebidasDesdeServidor")
                }
                _bebidas.value = bebidasDesdeServidor
            } catch (e: Exception) {
                Log.e("BebidaViewModel", "Error al obtener bebidas", e)
                _bebidas.value = emptyList() // En caso de error, dejamos la lista vacía
            }
        }
    }

    private suspend fun obtenerBebidasDesdeServidor(): List<String> {
        val url = "http://10.0.2.2/obtener_bebidas.php"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                Log.d("BebidaViewModel", "Enviando solicitud al servidor: $url")
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val json = response.body?.string()
                    Log.d("BebidaViewModel", "Respuesta del servidor: $json")
                    if (!json.isNullOrEmpty()) {
                        val jsonArray = JSONArray(json)
                        val bebidas = mutableListOf<String>()
                        for (i in 0 until jsonArray.length()) {
                            bebidas.add(jsonArray.getString(i))
                        }
                        return@withContext bebidas
                    } else {
                        Log.w("BebidaViewModel", "El cuerpo de la respuesta está vacío.")
                    }
                } else {
                    Log.e("BebidaViewModel", "Error en la respuesta del servidor: ${response.code}")
                }
                return@withContext emptyList()
            } catch (e: Exception) {
                Log.e("BebidaViewModel", "Excepción durante la solicitud", e)
                return@withContext emptyList()
            }
        }
    }
}



@Composable
fun PrimerosScreen(mesaId: Int, navController: NavController, viewModel: PrimerosViewModel = viewModel()) {
    val primaryBackgroundColor = Color(0xFF345A7B)
    val primeros by viewModel.primeros.collectAsState() // Observa la lista de primeros desde el ViewModel
    val primerosSeleccionados = remember { mutableStateMapOf<String, Int>() }

    // Llamar al método de ViewModel para cargar datos si aún no se han cargado
    LaunchedEffect(Unit) {
        viewModel.obtenerPrimeros()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mahai zenbakia: $mesaId",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar los primeros platos dinámicamente
            if (primeros.isEmpty()) {
                Text(
                    text = "Kargatzen...",
                    color = Color.White,
                    fontSize = 18.sp
                )
            } else {
                primeros.forEach { plato ->
                    Button(
                        onClick = {
                            primerosSeleccionados[plato] = (primerosSeleccionados[plato] ?: 0) + 1
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
                    ) {
                        Text(text = plato, color = Color.White, fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (primerosSeleccionados.isNotEmpty()) {
                Text(
                    text = "Hautatutako lehen platerak:",
                    color = Color.White,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    items(primerosSeleccionados.keys.toList()) { plato ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    val currentQuantity = primerosSeleccionados[plato] ?: 0
                                    if (currentQuantity > 1) {
                                        primerosSeleccionados[plato] = currentQuantity - 1
                                    } else {
                                        primerosSeleccionados.remove(plato)
                                    }
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = plato,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "x${primerosSeleccionados[plato]}",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
            ) {
                Text(text = "Atzera", color = Color.White)
            }

            Button(
                onClick = {
                    val productos = primerosSeleccionados.map { "${it.key} x${it.value}" }
                    navController.navigate("segundos/$mesaId/${productos.joinToString(",")}")
                },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
            ) {
                Text(text = "Hurrengoa", color = Color.White)
            }
        }
    }
}

class PrimerosViewModel : ViewModel() {
    private val _primeros = MutableStateFlow<List<String>>(emptyList())
    val primeros: StateFlow<List<String>> = _primeros

    fun obtenerPrimeros() {
        viewModelScope.launch {
            try {
                Log.d("PrimerosViewModel", "Iniciando obtención de primeros platos...")
                val primerosDesdeServidor = obtenerPrimerosDesdeServidor()
                if (primerosDesdeServidor.isEmpty()) {
                    Log.d("PrimerosViewModel", "No se recibieron primeros platos desde el servidor.")
                } else {
                    Log.d("PrimerosViewModel", "Primeros platos obtenidos: $primerosDesdeServidor")
                }
                _primeros.value = primerosDesdeServidor
            } catch (e: Exception) {
                Log.e("PrimerosViewModel", "Error al obtener primeros platos", e)
                _primeros.value = emptyList()
            }
        }
    }

    private suspend fun obtenerPrimerosDesdeServidor(): List<String> {
        val url = "http://10.0.2.2/obtener_primeros.php"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                Log.d("PrimerosViewModel", "Enviando solicitud al servidor: $url")
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val json = response.body?.string()
                    Log.d("PrimerosViewModel", "Respuesta del servidor: $json")
                    if (!json.isNullOrEmpty()) {
                        val jsonArray = JSONArray(json)
                        val primeros = mutableListOf<String>()
                        for (i in 0 until jsonArray.length()) {
                            primeros.add(jsonArray.getString(i))
                        }
                        return@withContext primeros
                    } else {
                        Log.w("PrimerosViewModel", "El cuerpo de la respuesta está vacío.")
                    }
                } else {
                    Log.e("PrimerosViewModel", "Error en la respuesta del servidor: ${response.code}")
                }
                return@withContext emptyList()
            } catch (e: Exception) {
                Log.e("PrimerosViewModel", "Excepción durante la solicitud", e)
                return@withContext emptyList()
            }
        }
    }
}



@Composable
fun SegundosScreen(mesaId: Int, productos: List<String>, navController: NavController) {
    val primaryBackgroundColor = Color(0xFF345A7B)
    val segundos = listOf("Arraina", "Oilaskoa", "Txuleta", "Lekaleak", "Barazkiak")
    val segundosSeleccionados = remember { mutableStateMapOf<String, Int>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mahai zenbakia: $mesaId",
                color = Color.White,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            segundos.forEach { plato ->
                Button(
                    onClick = {
                        segundosSeleccionados[plato] = (segundosSeleccionados[plato] ?: 0) + 1
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
                ) {
                    Text(text = plato, color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (segundosSeleccionados.isNotEmpty()) {
                Text(
                    text = "Hautatutako bigarren platerak:",
                    color = Color.White,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    items(segundosSeleccionados.keys.toList()) { plato ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    val currentQuantity = segundosSeleccionados[plato] ?: 0
                                    if (currentQuantity > 1) {
                                        segundosSeleccionados[plato] = currentQuantity - 1
                                    } else {
                                        segundosSeleccionados.remove(plato)
                                    }
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = plato,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "x${segundosSeleccionados[plato]}",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
            ) {
                Text(text = "Atzera", color = Color.White)
            }

            Button(
                onClick = {
                    val nuevosProductos = productos + segundosSeleccionados.map { "${it.key} x${it.value}" }
                    navController.navigate("comandoTotal/$mesaId/${nuevosProductos.joinToString(",")}")
                },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
            ) {
                Text(text = "Hurrengoa", color = Color.White)
            }
        }
    }
}

@Composable
fun ComandoTotalScreen(mesaId: Int, productos: List<String>, navController: NavController) {
    val primaryBackgroundColor = Color(0xFF345A7B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween // Asegura que el botón quede al final
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
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
        }

        Button(
            onClick = { navController.navigate("home") }, // Navegar a "home"
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
        ) {
            Text(text = "Amaitu", color = Color.White)
        }
    }
}





// Pantalla de Txat
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxatScreen() {
    val primaryBackgroundColor = Color(0xFF345A7B)
    val messageList = listOf("") // Lista de mensajes
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
    // Lista mutable para los pedidos
    val orders = remember { mutableStateListOf<String>() }
    // Simulamos un pedido vacío o con productos, puedes agregar más productos aquí o integrarlos con el proceso de selección
    if (orders.isEmpty()) {
        // Si no hay pedidos, se muestra un mensaje
        orders.add("Sin pedidos actuales")
    }

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

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para vaciar la lista de pedidos
        Button(
            onClick = {
                orders.clear()  // Esto vacía la lista de pedidos
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Eskariak ezabatu", color = Color.White)
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
    val fakeNavController = rememberNavController() // Crear un NavController simulado
    BebidaScreen(mesaId = 0, navController = fakeNavController) // Pasar el NavController
}

@Composable
@Preview
fun PreviewPrimerosScreen() {
    val fakeNavController = rememberNavController() // Crea un controlador ficticio
    PrimerosScreen(
        mesaId = 0,
        navController = fakeNavController
    )
}

@Composable
@Preview
fun PreviewSegundosScreen() {
    val fakeNavController = rememberNavController() // Crear NavController ficticio
    SegundosScreen(
        mesaId = 0,
        navController = fakeNavController,
        productos = listOf()// Pasar el controlador ficticio
    )
}

@Composable
@Preview
fun PreviewComandoTotalScreen() {
    val fakeNavController = rememberNavController()
    ComandoTotalScreen(mesaId = 0, productos = listOf(""), navController = fakeNavController)
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

