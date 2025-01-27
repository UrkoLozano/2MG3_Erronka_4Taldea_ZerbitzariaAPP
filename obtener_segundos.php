<?php
// Configuración de la base de datos
$host = "localhost:3306"; // Cambia según tu configuración
$user = "root"; // Usuario de la base de datos
$password = ""; // Contraseña de la base de datos
$database = "erronka"; // Nombre de la base de datos

// Crear conexión
$conn = new mysqli($host, $user, $password, $database);

// Verificar conexión
if ($conn->connect_error) {
    die("Conexión fallida: " . $conn->connect_error);
}

// Consulta para obtener los segundos platos (Bigarrena)
$sql = "SELECT id, izena FROM platera WHERE plater_mota = 'Bigarrena'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $segundos = array();

    while ($row = $result->fetch_assoc()) {
        $segundos[] = array(
            'id' => $row['id'],       // ID del plato
            'izena' => $row['izena']  // Nombre del plato
        );
    }

    // Devolver datos en formato JSON
    echo json_encode($segundos);
} else {
    echo json_encode([]); // Si no hay datos, devuelve un array vacío
}

$conn->close();
?>
