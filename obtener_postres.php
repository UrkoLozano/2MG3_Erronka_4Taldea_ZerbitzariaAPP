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

// Consulta para obtener los segundos platos
$sql = "SELECT izena FROM platera WHERE plater_mota = 'Postre'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $segundos = array();

    while ($row = $result->fetch_assoc()) {
        $segundos[] = $row['izena'];
    }

    // Devolver datos en formato JSON
    echo json_encode($segundos);
} else {
    echo json_encode([]); // Si no hay datos, devuelve un array vacío
}

$conn->close();
?>
