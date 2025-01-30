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
