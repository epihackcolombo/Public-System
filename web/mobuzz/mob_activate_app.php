<?php

require 'db_connection.php';
require 'global_variables.php';

function isactive($key) {

    $mysqli = connect_db();

    if (mysqli_connect_errno()) {
        close_db($mysqli);
        return DB_ERROR_CONN_JSON;
    }

    $sql = "SELECT * FROM activation_keys WHERE BINARY activate_key = '$key'";
    
    $comments = $mysqli->query($sql);

    if ($comments == false) {
        close_db($mysqli);
        return DB_ERROR_SELECT_JSON;
    }
    
    if ($comments->num_rows <= 0) {
        close_db($mysqli);
        return "action#no1";
    }

    $row = $comments->fetch_array(MYSQLI_ASSOC);

    if ($row['is_active'] > 0) {
        close_db($mysqli);
        return "action#ok";
    } else {
        close_db($mysqli);
        return "action#no2";
    }

}

function close_db($con) {

    $con->close();
}

try {

    $data = file_get_contents('php://input');

    $json = "";
    $key = "";

    if (is_object(json_decode($data))) {

        $json = json_decode($data);

        if (property_exists($json, 'key')) {
            $key = $json->{'key'};
        }

        $response = isactive($key);

        echo $response;
    } else {
        echo DB_ERROR_PARAM_JSON;
    }
} catch (Exception $e) {
    die(INT_ERROR_SERVER); //nothing to do
}

?>