<?php
$host = 'localhost:3306';
$dbname = '2mg3_erronka_4taldea';
$user = 'root';
$password = '1WMG2023';

try {
    // Conexión a la base de datos
    $pdo = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $user, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Verifica que se han recibido los datos necesarios
    $jsonData = file_get_contents("php://input");
    $data = json_decode($jsonData, true);

    if ($data === null) {
        echo json_encode(['status' => 'error', 'message' => 'Error al decodificar los datos JSON']);
        exit();
    }

    // Obtener el ID de la mesa y la lista de productos (bebidas seleccionadas)
    $mesaId = $data['mesa_id']; // ID de la mesa
    $productos = $data['producto']; // Lista de productos con ID, nombre, cantidad y precio
    $langileId = 1; // El ID del trabajador, puedes ajustarlo según el sistema

    // Calcular el total de la comanda
    $total = array_reduce($productos, function($sum, $producto) {
        return $sum + ($producto['prezioa'] * $producto['kantitatea']);
    }, 0); // Calcular el total de la comanda

    // Insertar en la tabla 'eskaera' (Solicitud)
    $sqlEskaera = "INSERT INTO eskaera (mahaia_id, langile_id, totala) VALUES (:mahaia_id, :langile_id, :totala)";
    $stmtEskaera = $pdo->prepare($sqlEskaera);
    $stmtEskaera->execute([ ':mahaia_id' => $mesaId, ':langile_id' => $langileId, ':totala' => $total ]); 

    // Obtener el ID de la solicitud (eskaera_id) recién insertada
    $eskaeraId = $pdo->lastInsertId();

    // Recorrer los productos para insertar según el tipo de menú
    foreach ($productos as $producto) {
        // Verificar que los valores necesarios están presentes
        if (!isset($producto['prezioa']) || !isset($producto['kantitatea']) || $producto['prezioa'] == 0 || $producto['kantitatea'] == 0) {
            echo json_encode(['status' => 'error', 'message' => 'Producto con precio o cantidad incorrectos']);
            exit();
        }

        // Si el tipo de menú es 'E' (Edariak), insertar en la tabla 'eskaera_edaria'
        if ($producto['tipo_menu'] == 'E') {
            $sqlEskaeraEdaria = "INSERT INTO eskaera_edaria (eskaera_id, edaria_id, prezioa, kantiatea) 
                                 VALUES (:eskaera_id, :edaria_id, :prezioa, :kantiatea)";
            $stmtEskaeraEdaria = $pdo->prepare($sqlEskaeraEdaria);
            $stmtEskaeraEdaria->execute([ 
                ':eskaera_id' => $eskaeraId, 
                ':edaria_id' => $producto['id'],
                ':prezioa' => $producto['prezioa'], 
                ':kantiatea' => $producto['kantitatea']
            ]);
        }

        // Si el tipo de menú es 'P' (Platerak), insertar en la tabla 'platera_eskaera'
        elseif ($producto['tipo_menu'] == 'P') {
            // Obtener el tiempo de preparación del plato (prestaketa_denbora) de la tabla 'platera'
            $sqlPrestaketa = "SELECT prestaketa_denbora FROM platera WHERE id = :platera";
            $stmtPrestaketa = $pdo->prepare($sqlPrestaketa);
            $stmtPrestaketa->execute([':platera' => $producto['id']]);
            $prestaketaDenbora = $stmtPrestaketa->fetchColumn(); // Tiempo en minutos

            if (!$prestaketaDenbora) {
                echo json_encode(['status' => 'error', 'message' => 'Tiempo de preparación no encontrado para el producto']);
                exit();
            }

            // Calcular la hora estimada para servir sumando el tiempo de preparación
            $eskaeraOrdua = new DateTime();
            $eskaeraOrduaFormatted = $eskaeraOrdua->format("Y-m-d H:i:s");

            $zerbitzatzekoOrdua = clone $eskaeraOrdua;
            $zerbitzatzekoOrdua->add(new DateInterval("PT" . $prestaketaDenbora . "M"));
            $zerbitzatzekoOrduaFormatted = $zerbitzatzekoOrdua->format("Y-m-d H:i:s");

            // Insertar en la tabla 'platera_eskaera'
            $sqlPlateraEskaera = "INSERT INTO platera_eskaera (eskaera_id, platera_id, prezioa, eskaera_ordua, zerbitzatzeko_ordua, kantiatea) 
                                  VALUES (:eskaera_id, :platera_id, :prezioa, :eskaera_ordua, :zerbitzatzeko_ordua, :kantiatea)";
            $stmtPlateraEskaera = $pdo->prepare($sqlPlateraEskaera);
            $stmtPlateraEskaera->execute([ 
                ':eskaera_id' => $eskaeraId, 
                ':platera_id' => $producto['id'], 
                ':prezioa' => $producto['prezioa'], 
                ':eskaera_ordua' => $eskaeraOrduaFormatted, 
                ':zerbitzatzeko_ordua' => $zerbitzatzekoOrduaFormatted, 
                ':kantiatea' => $producto['kantitatea']
            ]);
        }

        // Depuración: muestra lo que se está insertando
        echo json_encode([
            'status' => 'success', 
            'producto' => $producto, 
            'eskaera_id' => $eskaeraId, 
            'prezioa' => $producto['prezioa'],
            'kantitatea' => $producto['kantitatea']
        ]);
    }

    echo json_encode(['status' => 'success', 'message' => 'Comanda insertada correctamente']);
} catch (PDOException $e) {
    echo json_encode(['status' => 'error', 'message' => $e->getMessage()]);
}
?>
