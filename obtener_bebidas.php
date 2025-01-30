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

// Consulta para obtener las bebidas incluyendo el precio
$sql = "SELECT id, izena, prezioa FROM edaria"; // Ahora incluye el precio (prezioa)
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $bebidas = array();

    while ($row = $result->fetch_assoc()) {
        $bebidas[] = array(
            'id' => $row['id'],       // ID de la bebida
            'izena' => $row['izena'], // Nombre de la bebida
            'prezioa' => $row['prezioa'] // Precio de la bebida
        );
    }

    // Devolver datos en formato JSON
    echo json_encode($bebidas);
} else {
    echo json_encode([]); // Si no hay datos, devuelve un array vacío
}

$conn->close();
?>

