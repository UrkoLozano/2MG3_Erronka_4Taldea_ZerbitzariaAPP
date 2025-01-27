<?php
// Configuración de la conexión a la base de datos
$host = 'localhost:3306';
$dbname = 'erronka';
$username = 'root';
$password = '';

try {
    // Conexión a la base de datos
    $pdo = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Verifica que se han recibido los datos necesarios
    $jsonData = file_get_contents("php://input");
    $data = json_decode($jsonData, true);

    if (!isset($data['mesaId'], $data['productos'])) {
        echo json_encode(['status' => 'error', 'message' => 'Faltan datos necesarios (mesaId o productos)']);
        exit();
    }

    $mesaId = $data['mesaId']; // ID de la mesa
    $productos = $data['productos']; // Lista de productos con ID y nombre

    // Valida que los productos sean un array
    if (!is_array($productos)) {
        echo json_encode(['status' => 'error', 'message' => 'El formato de productos no es válido']);
        exit();
    }

    // Inserción de datos en la nueva tabla
    $sqlProductos = "INSERT INTO comanda_productos_nueva (id, mesa_id, producto, fecha_hora) 
                     VALUES (:id, :mesaId, :producto, NOW()) ORDER BY fecha_hora asc";
    $stmtProductos = $pdo->prepare($sqlProductos);

    foreach ($productos as $producto) {
        $stmtProductos->execute([
            ':id' => $producto['id'],        // ID del producto
            ':mesaId' => $mesaId,
            ':producto' => $producto['nombre'] // Nombre del producto
        ]);
    }

    // Respuesta de éxito
    echo json_encode(['status' => 'success', 'message' => 'Comanda insertada correctamente']);
} catch (PDOException $e) {
    // Manejo de errores de la base de datos
    echo json_encode(['status' => 'error', 'message' => $e->getMessage()]);
}
?>
