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

// Consulta para obtener los primeros platos (Lehenengoa)
$sql = "SELECT id, izena FROM platera WHERE plater_mota = 'Lehenengoa'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $primeros = array();

    while ($row = $result->fetch_assoc()) {
        $primeros[] = array(
            'id' => $row['id'],       // ID del plato
            'izena' => $row['izena']  // Nombre del plato
        );
    }

    // Devolver datos en formato JSON
    echo json_encode($primeros);
} else {
    echo json_encode([]); // Si no hay datos, devuelve un array vacío
}

$conn->close();
?>
