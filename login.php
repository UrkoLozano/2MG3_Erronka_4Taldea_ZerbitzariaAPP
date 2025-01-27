<?php
$host = "localhost:3306"; // Cambiar puerto si es necesario
$user = "root";
$password = "";
$dbname = "erronka";

$conn = new mysqli($host, $user, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$username = $_POST['username']; // Recibes el valor de 'izena'
$password = $_POST['password']; // Recibes el valor de 'pasahitza'

// Ajustar la consulta a la estructura de la tabla
$sql = "SELECT * FROM langile WHERE izena = ? AND pasahitza = ? AND lan_postua = 'Camarero'";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ss", $username, $password);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    echo "success";
} else {
    echo "failure";
}

$stmt->close();
$conn->close();
?>
