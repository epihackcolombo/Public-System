<?php

require("db_connection.php");


function save_data($year, $week, $zone, $radius, $density, $jason_data) {

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
    $sql_insert = "INSERT INTO `hotspots_cluster_data`(`year`,`week_of_year`,`json_data`,`zone`,`radius`,`density`) VALUES('$year','$week','$jason_data','$zone','$radius','$density')";

    $comments = $mysqli->query($sql_select);

    if ($comments == false) {
        close_db($mysqli);
        return "SQLError: SQL select failed";
    }

    if ($comments->num_rows <= 0) {
        
        //Add record
        $comments = $mysqli->query($sql_insert);

        if ($comments == false) {
            return "SQLError: SQL insert failed";
        } else {
            return "SQLSuccess: SQL insert succeed";
        }
        close_db($mysqli);
    } else {

        //Edit record
        $row = $comments->fetch_array(MYSQLI_ASSOC);
        $id = $row['id'];

        $sql_update = "UPDATE `hotspots_cluster_data` SET `json_data`='$jason_data',`zone`='$zone',`radius`='$radius',`density`='$density' WHERE `id`='$id'";

        $comments = $mysqli->query($sql_update);

        if ($comments == false) {
            return "SQLError: SQL update failed";
        } else {
            return "SQLSuccess: SQL update succeed";
        }
        close_db($mysqli);
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

    //POST parameters
    $year = filter_input(INPUT_POST, 'year');
    $week_of_year = filter_input(INPUT_POST, 'week_of_year');
    $danger_zone = filter_input(INPUT_POST, 'danger_zone');
    $cluster_radius = filter_input(INPUT_POST, 'cluster_radius');
    $cluster_density = filter_input(INPUT_POST, 'cluster_density');
    $jason_data = filter_input(INPUT_POST, 'checkString');

    if (($year > 0) and ($week_of_year > 0) and ($danger_zone >= 0) and ($cluster_radius >= 0) and ($cluster_density >= 0) and (strlen($jason_data) > 4)) {
        echo save_data($year, $week_of_year, $danger_zone, $cluster_radius, $cluster_density, $jason_data);
    }
    
} catch (Exception $e) {
    die(""); //nothing to do
}

?>