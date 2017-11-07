<?php

require 'db_connection.php';
require 'mob_profile_phi_validate.php';
require 'global_variables.php';
require 'util_manage_image.php';
include 'Security.php';

function get_data($id) {

	$id = $mysqli->real_escape_string($id);

    $con = connect_db();

    if (mysqli_connect_errno()) {
        //echo "Failed to connect to MySQL: " . mysqli_connect_error();
		return DB_ERROR_CONN_JLIST;
    }

    $result = mysqli_query($con, "SELECT * FROM public_data WHERE id='$id'");

    $image_path = "";

    while ($row = mysqli_fetch_array($result)) {
        $image_path = $row['img_path'];
    }

    return read_image($image_path);
}

header('Content-Type: application/json');

try {

    $data = file_get_contents('php://input');

    $id = "";
    $user = "";
    $time_stamp = "";
    $uudid = "";

    if (is_object(json_decode($data))) {

        $json = json_decode($data);


        if (property_exists($json, 'id')) {
            $id = $json->{'id'};
			$id=$FBI->xss_clean($id);
        }

        if (property_exists($json, 'user')) {
            $user = $json->{'user'};
			$user=$FBI->xss_clean($user);
        }

        if (property_exists($json, 'time_stamp')) {
            $time_stamp = $json->{'time_stamp'};
			$time_stamp=$FBI->xss_clean($time_stamp);
        }

        if (property_exists($json, 'uudid')) {
            $uudid = $json->{'uudid'};
			$uudid=$FBI->xss_clean($uudid);
        }

        $isvaliduser = user_validate($user, $time_stamp, $uudid);

        if (!empty($isvaliduser)) {
            
            echo $isvaliduser;
        } else {
            
            $response = get_data($id);
            echo $response;
        }
        
    }
} catch (Exception $e) {
    die("internal_error"); //nothing to do
}
?>
 