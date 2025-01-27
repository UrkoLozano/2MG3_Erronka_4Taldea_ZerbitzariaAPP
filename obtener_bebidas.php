<?php
// Configuración de la base de datos
$host = "localhost:3306"; // Cambia esto según tu configuración
$user = "root"; // Usuario de la base de datos
$password = ""; // Contraseña de la base de datos
$database = "erronka"; // Nombre de la base de datos

// Crear conexión
$conn = new mysqli($host, $user, $password, $database);

// Verificar conexión
if ($conn->connect_error) {
    die("Conexión fallida: " . $conn->connect_error);
}

// Consulta para obtener las bebidas
$sql = "SELECT id, izena FROM edaria"; // Incluir ID y nombre de la bebida
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $bebidas = array();

    while ($row = $result->fetch_assoc()) {
        $bebidas[] = array(
            'id' => $row['id'],       // ID de la bebida
            'izena' => $row['izena']  // Nombre de la bebida
        );
    }

    // Devolver datos en formato JSON
    echo json_encode($bebidas);
} else {
    echo json_encode([]); // Si no hay datos, devuelve un array vacío
}

$conn->close();
?>
