<?php

require("db_connection.php");
require("util_manage_time.php");
require_once('map_public_cluster_create.php');


function create_clusters($points, $def_radius, $cluster_radius, $cluster_density) {

    $cluster = new Cluster;
    $clusterPoint = $cluster->createCluster($points, $cluster_radius, 20, $cluster_density, $def_radius);
    //var_dump($clusterPoint);

    if (count($clusterPoint) > 0) {

        $jstring = '[';
        for ($i = 0; $i < count($clusterPoint); $i++) {

            if ($i == 0) {
                $jstring.= '{tag:' . $i . ', lat:' . $clusterPoint[$i]["coordinate"][1] . ', lng:' . $clusterPoint[$i]["coordinate"][0] . ', data:"' . $clusterPoint[$i]["coordinate"][1] . ',' . $clusterPoint[$i]["coordinate"][0] . ',' . $i . '", rad:' . $clusterPoint[$i]["radius"] . ', fill:"#610B0B", stroke:"#610B0B"}';
            } else {
                $jstring.= ',{tag:' . $i . ', lat:' . $clusterPoint[$i]["coordinate"][1] . ', lng:' . $clusterPoint[$i]["coordinate"][0] . ', data:"' . $clusterPoint[$i]["coordinate"][1] . ',' . $clusterPoint[$i]["coordinate"][0] . ',' . $i . '", rad:' . $clusterPoint[$i]["radius"] . ', fill:"#610B0B", stroke:"#610B0B"}';
            }
        }
        $jstring.= ']';
        return $jstring;
    } else {
        return "";
    }
}

function get_data($year, $week, $def_radius, $cluster_radius, $cluster_density) {

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

        $points = array();
        while ($row = mysqli_fetch_array($comments)) {
            $loc["location"] = array($row["location_lng"], $row["location_lat"]);
            $points[] = $loc;
        }

        return create_clusters($points, $def_radius, $cluster_radius, $cluster_density);
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

    if (count($param) >= 5 and $param[0] > 0 and $param[1] > 0) {
        echo get_data($param[0], $param[1], $param[2], $param[3], $param[4]);
    }
    
} catch (Exception $e) {
    die(""); //nothing to do
}

?>