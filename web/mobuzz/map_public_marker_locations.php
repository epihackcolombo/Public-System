<?php

require("db_connection.php");
require("util_manage_time.php");


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

	$datearray = getTimeStatus($year, $week);

	$sql_select = "SELECT * FROM vw_hotspots_location_data WHERE year_week BETWEEN '$datearray[2]' AND '$datearray[3]'";
	
    $comments = $mysqli->query($sql_select);

    if ($comments == false) {
        close_db($mysqli);
        return "SQLError: SQL select failed";
    }

    if ($comments->num_rows <= 0) {
        close_db($mysqli);
        return "SQLSuccess: SQL_no_data";
    } else {

        $i = 0;
        $jstring = $year . "$" . $week . "$[";

        while ($row = mysqli_fetch_array($comments)) {

            if ($i == 0) {
                $jstring .= '{tag:' . $i . ', dbid:'.$row["id"].', lat:' . $row["location_lat"] . ', lng:' . $row["location_lng"] . ', data:"' . $row["location_lat"] . ',' . $row["location_lng"] . ',' . $i . '", rad:20000, fill:"#0000FF", stroke:"#0000FF"}';
                $i++;
            } else {
                $jstring .= ',{tag:' . $i . ', dbid:'.$row["id"].', lat:' . $row["location_lat"] . ', lng:' . $row["location_lng"] . ', data:"' . $row["location_lat"] . ',' . $row["location_lng"] . ',' . $i . '", rad:20000, fill:"#0000FF", stroke:"#0000FF"}';
                $i++;
            }
        }

        $jstring .="]";
        close_db($mysqli);
        return $jstring;
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

    if (count($param) >= 2 and $param[0] > 0 and $param[1] > 0) {
        echo get_data($param[0], $param[1]);
    }
    
} catch (Exception $e) {
    die(""); //nothing to do
}
?>