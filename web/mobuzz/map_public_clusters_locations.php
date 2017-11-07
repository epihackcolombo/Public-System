<?php

require("db_connection.php");


function get_data($year, $week) {

    $mysqli = connect_database();

    if (mysqli_connect_errno()) {
        close_db($mysqli);

        if ($mysqli->connect_errno) {
            printf("Connect failed: %s\n", $mysqli->connect_error);
            exit();
        }
        return "DBError: Database connection failed";
    }

    $sql_select = "SELECT * FROM hotspots_cluster_data WHERE year='$year' and week_of_year='$week'";

    $comments = $mysqli->query($sql_select);

    if ($comments == false) {
        close_db($mysqli);
        return "SQLError: SQL select failed";
    }

    if ($comments->num_rows <= 0) {
        close_db($mysqli);
        return "SQLSuccess: SQL_no_data";
    } else {
        $row = $comments->fetch_array(MYSQLI_ASSOC);
        $json_data = $row['json_data'];
        close_db($mysqli);
        return $json_data;
    }
}

function connect_database() {
	$con=connect_db();
    return $con;
}

function close_db($con) {
    $con->close();
}


try {

    //GET parameters
    $data = filter_input(INPUT_GET, 'q');
    $param = explode("$", $data);

    if (count($param) >= 2) {
        echo get_data($param[0], $param[1]);
    }
    
} catch (Exception $e) {
    die(""); //nothing to do
}

?>