<?php
$host = 'localhost:3306';
$dbname = '2mg3_erronka_4taldea';
$user = 'root';
$password = '1WMG2023';

$conn = new mysqli($host, $user, $password, $dbname);

// Verificar conexión
if ($conn->connect_error) {
    die("Conexión fallida: " . $conn->connect_error);
}

// Consulta para obtener los primeros platos (Lehenengoa) con precio
$sql = "SELECT id, izena, prezioa FROM platera WHERE plater_mota = 'Bigarrena'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $primeros = array();

    while ($row = $result->fetch_assoc()) {
        $primeros[] = array(
            'id' => $row['id'],       // ID del plato
            'izena' => $row['izena'], // Nombre del plato
            'prezioa' => $row['prezioa'] // Precio del plato
        );
    }

    // Devolver datos en formato JSON
    echo json_encode($primeros);
} else {
    echo json_encode([]); // Si no hay datos, devuelve un array vacío
}

$conn->close();
?>
