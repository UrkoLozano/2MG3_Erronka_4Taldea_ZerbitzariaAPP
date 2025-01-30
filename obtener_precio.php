<?php 
// Establece la conexión con la base de datos
$servername = "localhost";
$username = "root";
$password = "1WMG2023";
$dbname = "2mg3_erronka_4taldea";  // Asegúrate de usar el nombre correcto de tu base de datos

$conn = new mysqli($servername, $username, $password, $dbname);

// Comprueba la conexión
if ($conn->connect_error) {
    die("Conexión fallida: " . $conn->connect_error);
}

// Obtén el ID del producto desde la solicitud
$id_producto = $_GET['id']; 
$tipoMenu = $_GET['tipoMenu']; 


if($tipoMenu == "E"){
    $query = "SELECT prezioa FROM edaria WHERE id = ?";
}elseif($tipoMenu == "P"){
    $query = "SELECT prezioa FROM platera WHERE id = ?";
}
// Consulta para obtener el precio de la bebida o del plato, según el ID


// Prepara y ejecuta la consulta
$stmt = $conn->prepare($query);
$stmt->bind_param("i", $id_producto); // Asumiendo que el ID es un número entero
$stmt->execute();

// Obtener el resultado
$result = $stmt->get_result();
$precio = 0;

if ($row = $result->fetch_assoc()) {
    $precio = $row['prezioa'];
}

// Cerrar la conexión
$stmt->close();
$conn->close();

// Devuelve el precio como JSON
echo json_encode(['precio' => $precio]);
?>