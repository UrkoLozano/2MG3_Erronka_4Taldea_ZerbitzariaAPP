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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.runtime.rememberCoroutineScope
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket



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
        startDestination = "login" // Mantengo 'login' como pantalla inicial
    ) {
        // Pantalla de Login
        composable("login") {
            LoginScreen(navController)
        }

        // Pantalla Home
        composable(
            route = "home/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "User"
            HomeScreen(navController = navController, username = username)
        }

        // Pantalla de Chat
        composable(
            route = "txat/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "User"
            TxatScreen(navController = navController, username = username)
        }

        // Pantalla de Mesas
        composable("mesas") {
            MesaScreen(navController = navController) // ✅ Solo pasamos el NavController
        }

        // Detalles de una mesa específica
        composable(
            route = "detalleMesa/{mesaId}",
            arguments = listOf(navArgument("mesaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val mesaId = backStackEntry.arguments?.getInt("mesaId") ?: 0
            BebidaScreen(mesaId = mesaId, navController = navController)
        }

        // Pantalla de primeros platos
        composable(
            route = "primeros/{mesaId}/{productos}",
            arguments = listOf(
                navArgument("mesaId") { type = NavType.IntType },
                navArgument("productos") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mesaId = backStackEntry.arguments?.getInt("mesaId") ?: 0
            val productos = backStackEntry.arguments?.getString("productos")?.split(",") ?: emptyList()
            PrimerosScreen(mesaId = mesaId, navController = navController, productos = productos)
        }

        // Pantalla de segundos platos
        composable(
            route = "segundos/{mesaId}/{productos}",
            arguments = listOf(
                navArgument("mesaId") { type = NavType.IntType },
                navArgument("productos") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mesaId = backStackEntry.arguments?.getInt("mesaId") ?: 0
            val productos = backStackEntry.arguments?.getString("productos")?.split(",") ?: emptyList()
            SegundosScreen(mesaId = mesaId, productos = productos, navController = navController)
        }

        // Comanda total
        composable(
            route = "comandoTotal/{mesaId}/{productos}",
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
    val context = LocalContext.current

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
                        .add("username", username.value)
                        .add("password", password.value)
                        .build()

                    val request = Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val responseBody = response.body?.string()
                            if (response.isSuccessful) {
                                if (responseBody == "success") {
                                    Handler(Looper.getMainLooper()).post {
                                        navController.navigate("home/${username.value}")
                                    }
                                } else {
                                    Handler(Looper.getMainLooper()).post {
                                        Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(context, "Error en el servidor", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    })
                } else {
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
fun HomeScreen(navController: NavController, username: String) {
    val primaryBackgroundColor = Color(0xFF345A7B)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top // Keep image at the top
    ) {
        Image(
            painter = painterResource(id = R.drawable.restaurant_logo),
            contentDescription = "Descripción de la imagen",
            modifier = Modifier
                .size(350.dp)
                .padding(bottom = 32.dp)
        )
        Spacer(modifier = Modifier.height(80.dp)) // Added space between image and buttons
        Button(
            onClick = { navController.navigate("mesas") },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp) // Increased height for larger button
                .padding(6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
        ) {
            Text(text = "ESKAERAREKIN HASI", color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp)) // Added spacing between buttons

        Button(
            onClick = { navController.navigate("txat/$username") },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp) // Increased height for larger button
                .padding(6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
        ) {
            Text(text = "TXATEATU", color = Color.White, fontSize = 20.sp)
        }
    }
}


@Composable
fun MesaScreen(navController: NavController, viewModel: MesaViewModel = viewModel()) {
    val primaryBackgroundColor = Color(0xFF345A7B)
    val mesas by viewModel.mesas.collectAsState() // Observa las mesas desde el ViewModel
    val mesaSeleccionada = remember { mutableStateOf<Pair<Int, String>?>(null) }

    LaunchedEffect(Unit) {
        viewModel.obtenerMesas()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hautatu zure mahaia",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (mesas.isEmpty()) {
            Text(
                text = "Kargatzen...",
                color = Color.White,
                fontSize = 18.sp
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(mesas) { (id, nombre) ->
                    Button(
                        onClick = { navController.navigate("detalleMesa/$id") }, // Navegación al detalle de la mesa
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
                    ) {
                        Text(text = "$id - $nombre", color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        mesaSeleccionada.value?.let { (id, nombre) ->
            Text(
                text = "Mahai hautatua: $nombre",
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigate("detalleMesa/$id") }, // Navegación al detalle de la mesa
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
            ) {
                Text(text = "Hurrengoa", color = Color.White)
            }
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
        ) {
            Text(text = "Atzera", color = Color.White)
        }
    }
}


class MesaViewModel : ViewModel() {
    private val _mesas = MutableStateFlow<List<Pair<Int, String>>>(emptyList())
    val mesas: StateFlow<List<Pair<Int, String>>> = _mesas

    fun obtenerMesas() {
        viewModelScope.launch {
            try {
                Log.d("MesaViewModel", "Iniciando obtención de mesas...")
                val mesasDesdeServidor = obtenerMesasDesdeServidor()
                if (mesasDesdeServidor.isEmpty()) {
                    Log.d("MesaViewModel", "No se recibieron mesas desde el servidor.")
                } else {
                    Log.d("MesaViewModel", "Mesas obtenidas: $mesasDesdeServidor")
                }
                _mesas.value = mesasDesdeServidor
            } catch (e: Exception) {
                Log.e("MesaViewModel", "Error al obtener mesas", e)
                _mesas.value = emptyList() // En caso de error, lista vacía
            }
        }
    }

    private suspend fun obtenerMesasDesdeServidor(): List<Pair<Int, String>> {
        val url = "http://10.0.2.2/get_mesas.php" // Reemplaza con tu URL
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                Log.d("MesaViewModel", "Enviando solicitud al servidor: $url")
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val json = response.body?.string()
                    Log.d("MesaViewModel", "Respuesta del servidor: $json")
                    if (!json.isNullOrEmpty()) {
                        val jsonArray = JSONArray(json)
                        val mesas = mutableListOf<Pair<Int, String>>()
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            mesas.add(Pair(obj.getInt("id"), obj.getString("izena")))
                        }
                        return@withContext mesas
                    } else {
                        Log.w("MesaViewModel", "El cuerpo de la respuesta está vacío.")
                    }
                } else {
                    Log.e("MesaViewModel", "Error en la respuesta del servidor: ${response.code}")
                }
                return@withContext emptyList()
            } catch (e: Exception) {
                Log.e("MesaViewModel", "Excepción durante la solicitud", e)
                return@withContext emptyList()
            }
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
                bebidas.forEach { (id, nombre) ->
                    Button(
                        onClick = {
                            val bebidaKey = "E"+"$id - $nombre"
                            bebidaSeleccionada[bebidaKey] = (bebidaSeleccionada[bebidaKey] ?: 0) + 1
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
                    ) {
                        Text(text = "E"+"$id - $nombre", color = Color.White, fontSize = 16.sp)
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
                    val bebidasSeleccionadas = bebidaSeleccionada.map { "${it.key} x${it.value}" }
                    navController.navigate("primeros/$mesaId/${bebidasSeleccionadas.joinToString(",")}")
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
    private val _bebidas = MutableStateFlow<List<Pair<Int, String>>>(emptyList())
    val bebidas: StateFlow<List<Pair<Int, String>>> = _bebidas

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

    private suspend fun obtenerBebidasDesdeServidor(): List<Pair<Int, String>> {
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
                        val bebidas = mutableListOf<Pair<Int, String>>()
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            bebidas.add(Pair(obj.getInt("id"), obj.getString("izena")))
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
fun PrimerosScreen(mesaId: Int, navController: NavController, productos: List<String>, viewModel: PrimerosViewModel = viewModel()) {
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
                primeros.forEach { (id, nombre) ->
                    Button(
                        onClick = {
                            val platoKey = "P" + "$id - $nombre"
                            primerosSeleccionados[platoKey] = (primerosSeleccionados[platoKey] ?: 0) + 1
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
                    ) {
                        Text(text = "P" + "$id - $nombre", color = Color.White, fontSize = 16.sp)
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
                    val nuevosProductos = productos + primerosSeleccionados.map { "${it.key} x${it.value}" }
                    navController.navigate("segundos/$mesaId/${nuevosProductos.joinToString(",")}")
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
    private val _primeros = MutableStateFlow<List<Pair<Int, String>>>(emptyList())
    val primeros: StateFlow<List<Pair<Int, String>>> = _primeros

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

    private suspend fun obtenerPrimerosDesdeServidor(): List<Pair<Int, String>> {
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
                        val primeros = mutableListOf<Pair<Int, String>>()
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            primeros.add(Pair(obj.getInt("id"), obj.getString("izena")))
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
fun SegundosScreen(mesaId: Int, productos: List<String>, navController: NavController, viewModel: SegundosViewModel = viewModel()) {
    val primaryBackgroundColor = Color(0xFF345A7B)
    val segundos by viewModel.segundos.collectAsState() // Observa la lista de segundos desde el ViewModel
    val segundosSeleccionados = remember { mutableStateMapOf<String, Int>() }

    // Llamar al método de ViewModel para cargar datos si aún no se han cargado
    LaunchedEffect(Unit) {
        viewModel.obtenerSegundos()
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

            // Mostrar los segundos platos dinámicamente
            if (segundos.isEmpty()) {
                Text(
                    text = "Kargatzen...",
                    color = Color.White,
                    fontSize = 18.sp
                )
            } else {
                segundos.forEach { (id, nombre) ->
                    Button(
                        onClick = {
                            val platoKey = "P" + "$id - $nombre"
                            segundosSeleccionados[platoKey] = (segundosSeleccionados[platoKey] ?: 0) + 1
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
                    ) {
                        Text(text = "P" + "$id - $nombre", color = Color.White, fontSize = 16.sp)
                    }
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

class SegundosViewModel : ViewModel() {
    private val _segundos = MutableStateFlow<List<Pair<Int, String>>>(emptyList())
    val segundos: StateFlow<List<Pair<Int, String>>> = _segundos

    fun obtenerSegundos() {
        viewModelScope.launch {
            try {
                Log.d("SegundosViewModel", "Iniciando obtención de segundos platos...")
                val segundosDesdeServidor = obtenerSegundosDesdeServidor()
                if (segundosDesdeServidor.isEmpty()) {
                    Log.d("SegundosViewModel", "No se recibieron segundos platos desde el servidor.")
                } else {
                    Log.d("SegundosViewModel", "Segundos platos obtenidos: $segundosDesdeServidor")
                }
                _segundos.value = segundosDesdeServidor
            } catch (e: Exception) {
                Log.e("SegundosViewModel", "Error al obtener segundos platos", e)
                _segundos.value = emptyList()
            }
        }
    }

    private suspend fun obtenerSegundosDesdeServidor(): List<Pair<Int, String>> {
        val url = "http://10.0.2.2/obtener_segundos.php"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                Log.d("SegundosViewModel", "Enviando solicitud al servidor: $url")
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val json = response.body?.string()
                    Log.d("SegundosViewModel", "Respuesta del servidor: $json")
                    if (!json.isNullOrEmpty()) {
                        val jsonArray = JSONArray(json)
                        val segundos = mutableListOf<Pair<Int, String>>()
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            segundos.add(Pair(obj.getInt("id"), obj.getString("izena")))
                        }
                        return@withContext segundos
                    } else {
                        Log.w("SegundosViewModel", "El cuerpo de la respuesta está vacío.")
                    }
                } else {
                    Log.e("SegundosViewModel", "Error en la respuesta del servidor: ${response.code}")
                }
                return@withContext emptyList()
            } catch (e: Exception) {
                Log.e("SegundosViewModel", "Excepción durante la solicitud", e)
                return@withContext emptyList()
            }
        }
    }
}


@Composable
fun ComandoTotalScreen(mesaId: Int, productos: List<String>, navController: NavController, ) {
    val context = LocalContext.current
    val primaryBackgroundColor = Color(0xFF345A7B)
    val precios = remember { mutableStateOf<Map<Int, Float>>(emptyMap()) }

    // Obtener los precios de los productos al cargar la pantalla
    LaunchedEffect(productos) {
        val preciosMap = obtenerPreciosDeProductos(productos) // Obtiene todos los precios
        precios.value = preciosMap
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
                    val partes = producto.split(" - ")

                    val parteZero = partes[0]
                    val tipoMenu = partes[0][0]
                    val id = parteZero.substring(1).toInt()

                    // Obtenemos el precio para el producto
                    val precio = precios.value[id] ?: 0f

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A708B)), // Fondo del card
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Sombra del card
                        shape = RoundedCornerShape(8.dp) // Bordes redondeados
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star, // Cambia este ícono si prefieres otro
                                contentDescription = null,
                                tint = Color.Yellow,
                                modifier = Modifier.size(24.dp) // Tamaño del ícono
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "$producto - Precio: $precio €",
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f) // Ocupa el resto del espacio disponible
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                if (productos.isNotEmpty()) {
                    val url = "http://10.0.2.2/enviar_comanda.php" // Cambia la IP si es necesario
                    val client = OkHttpClient()

                    // Convertimos la lista de productos (String) a un formato que incluya ID y nombre
                    val productosConId = productos.mapIndexed { index, producto ->
                        val partes = producto.split(" - ")
                        val parteZero = partes[0]
                        val tipoMenu = partes[0][0]
                        val id = parteZero.substring(1).toInt()
                        val nombreCantidad = partes[1].split(" x")
                        val nombre = nombreCantidad[0]
                        val cantidad = if (nombreCantidad.size > 1) nombreCantidad[1].toInt() else 1

                        val precio = precios.value[id] ?: 0f // Obtenemos el precio del producto

                        Log.d("ProductoConPrecio", "Producto: $nombre, Precio: $precio, Cantidad: $cantidad")

                        JSONObject().apply {
                            put("id", id)
                            put("nombre", nombre)
                            put("kantitatea", cantidad)
                            put("prezioa", precio) // Usamos el precio aquí
                            put("tipo_menu", tipoMenu.toString())
                            put("fecha_hora", System.currentTimeMillis() + index * 1000)
                        }
                    }

                    val jsonBody = JSONObject().apply {
                        put("mesa_id", mesaId)
                        put("producto", JSONArray(productosConId))  // Cambiado 'producto' por 'productos' para coincidir con el PHP
                    }

                    val requestBody = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        jsonBody.toString()
                    )

                    val request = Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val responseBody = response.body?.string()
                            if (response.isSuccessful) {
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(context, "Comanda enviada con éxito", Toast.LENGTH_SHORT).show()
                                    val username = "NombreDeUsuario" // Reemplaza con tu lógica para obtener el nombre de usuario
                                    navController.navigate("home/$username")
                                }
                            } else {
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(context, "Error al enviar la comanda", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    })
                } else {
                    Toast.makeText(context, "No hay productos para enviar", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF666E6C))
        ) {
            Text(text = "Amaitu", color = Color.White)
        }
    }
}

// Función suspendida para obtener los precios de los productos
suspend fun obtenerPreciosDeProductos(productos: List<String>): Map<Int, Float> {
    val preciosMap = mutableMapOf<Int, Float>()
    val tipoDePlato = 0; //1 => Edariak
    for (producto in productos) {
        val partes = producto.split(" - ")
        val parteZero = partes[0]
        val tipoMenu = partes[0][0]
        val id = parteZero.substring(1).toInt()
        val precio = obtenerPrecioDelProducto(id, tipoMenu) // Llamamos la función suspendida
        preciosMap[id] = precio
    }
    return preciosMap
}

suspend fun obtenerPrecioDelProducto(id: Int, tipoMenu: Char): Float {
    val url = "http://10.0.2.2/obtener_precio.php?id=$id&tipoMenu=$tipoMenu"  // Ajusta la URL

    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    return withContext(Dispatchers.IO) {
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonResponse = JSONObject(responseBody)
                return@withContext jsonResponse.optDouble("precio", 0.0).toFloat()
            } else {
                return@withContext 0f
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext 0f
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxatScreen(navController: NavHostController, username: String, host: String = "192.168.115.153", port: Int = 5555) {
    val primaryBackgroundColor = Color(0xFF345A7B)
    val messageList = remember { mutableStateListOf<String>() } // Lista mutable para mensajes
    val newMessage = remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Variables para la conexión
    val socket = remember { mutableStateOf<Socket?>(null) }
    val out = remember { mutableStateOf<PrintWriter?>(null) }
    val input = remember { mutableStateOf<BufferedReader?>(null) }

    // Conexión al servidor al iniciar la pantalla
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val socketConnection = Socket(host, port)
                socket.value = socketConnection
                out.value = PrintWriter(OutputStreamWriter(socketConnection.getOutputStream()), true)
                input.value = BufferedReader(InputStreamReader(socketConnection.getInputStream()))

                // Envía el nombre de usuario al servidor al conectarse
                out.value?.println("$username se ha conectado")

                // Recibir mensajes del servidor
                while (socketConnection.isConnected) {
                    val message = input.value?.readLine()
                    if (message != null) {
                        withContext(Dispatchers.Main) {
                            messageList.add(message)
                        }
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error al conectar con el servidor: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Cerrar conexión al salir de la pantalla
    DisposableEffect(Unit) {
        onDispose {
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    out.value?.println("$username se ha desconectado")
                    input.value?.close()
                    out.value?.close()
                    socket.value?.close()
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error al cerrar conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Interfaz de usuario
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackgroundColor)
            .padding(16.dp)
    ) {
        // Botón de retroceso y título
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Etxera", color = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Txateatu", color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de mensajes
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(messageList) { message ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = message, color = Color.White)
                }
            }
        }

        // Campo para escribir un nuevo mensaje
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
                val message = newMessage.value.text
                if (message.isNotEmpty()) {
                    coroutineScope.launch(Dispatchers.IO) {
                        try {
                            val formattedMessage = "$username: $message"
                            out.value?.println(formattedMessage)
                            withContext(Dispatchers.Main) {
                                messageList.add("Tú: $message")
                                newMessage.value = TextFieldValue("") // Limpiar campo
                            }
                        } catch (e: IOException) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Error al enviar el mensaje: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "No puedes enviar un mensaje vacío", Toast.LENGTH_SHORT).show()
                }
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


@Preview(showBackground = true )
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(navController = rememberNavController(),
        username = ""
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMesaScreen() {
    val navController = rememberNavController()

    MesaScreen(navController = navController) // ✅ Solo pasamos el NavController
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
        navController = fakeNavController,
        productos = listOf()
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
    val navController = rememberNavController()
    TxatScreen(navController = navController, username = "PreviewUser")
}