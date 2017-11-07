<?php

require("db_connection.php");


function get_data($fromdate, $todate, $reporttype) {

    $mysqli = connect_database();

    if (mysqli_connect_errno()) {
        close_db($mysqli);

        if ($mysqli->connect_errno) {
            printf("Connect failed: %s\n", $mysqli->connect_error);
            exit();
        }
        return "DBError: Database connection failed";
    }

    $sql_select = "SELECT * FROM public_data WHERE (verified=$reporttype) AND (date_time BETWEEN '$fromdate' AND '$todate')";
    
    if($reporttype < -1)
    {
        $sql_select = "SELECT * FROM public_data WHERE (verified>-2) AND (date_time BETWEEN '$fromdate' AND '$todate')";
    }
  
    
    
	
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
        $jstring = "[";

        while ($row = mysqli_fetch_array($comments)) {

            if(!empty( $row["gps"]))
            {
                 $location = explode(",", $row["gps"]);
                 if (count($location) >= 2) {
            
                    if ($i == 0) {
                        $jstring .= '{tag:' . $i . ', lat:' . $location[0] . ', lng:' . $location[1] . ', data:"' . $row["username"] . '|' . $row["date_time"] . '|' . $row["ward"] . '|' . $row["cmcmessage"] . '|' . $row["verified_by"] . '|' . $row["verified_date_time"] . '|'. $row["id"] . '"}';
                        $i++;
                    } else {
                        $jstring .= ',{tag:' . $i . ', lat:' . $location[0] . ', lng:' . $location[1] . ', data:"' . $row["username"]. '|' . $row["date_time"] . '|' . $row["ward"] . '|' . $row["cmcmessage"] . '|' . $row["verified_by"] . '|' . $row["verified_date_time"] . '|'. $row["id"] . '"}';
                        $i++;
                    }

                }
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

    if (count($param) >= 2) {
        echo get_data($param[0], $param[1], $param[2]);
    }
    
} catch (Exception $e) {
    die(""); //nothing to do
}

?>