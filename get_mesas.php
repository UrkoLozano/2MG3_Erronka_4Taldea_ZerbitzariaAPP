<?php
// Configuración de la base de datos
$host = "localhost:3306"; // Ajusta el puerto si es necesario
$user = "talde4"; // Usuario de la base de datos
$password = "1WMG2023"; // Contraseña de la base de datos
$database = "2mg3_erronka_4taldea"; // Nombre de la base de datos

// Crear conexión
$conn = new mysqli($host, $user, $password, $database);

// Verificar conexión
if ($conn->connect_error) {
    die("Conexión fallida: " . $conn->connect_error);
}

// Consulta para obtener las mesas
$sql = "SELECT id, izena FROM mahaia"; // Ajusta los nombres de columnas si es necesario
$result = $conn->query($sql);

$mesas = [];

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $mesas[] = [
            'id' => $row['id'],       // ID de la mesa
            'izena' => $row['izena']  // Nombre de la mesa
        ];
    }


    echo json_encode($mesas);
} else {
    echo json_encode([]); // Si no hay datos, devuelve un array vacío
}

$conn->close();
?>
