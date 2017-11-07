<?php

require 'db_connection.php';
require 'profile_public_validate.php';
require 'global_variables.php';
include 'Security.php';


function write_user_logs($user, $log_data) {

    date_default_timezone_set('Asia/Kolkata');

    $mt = microtime(true);
    $micro = sprintf("%06d", ($mt - floor($mt)) * 1000000);
    $date = new DateTime(date('Y-m-d H:i:s.' . $micro, $mt));
    $printdate = $date->format("_Ymd_His_u");

    $file_name = "./user_logs/" . $user . $printdate . ".txt";
    $log_data = str_replace('\n', '', $log_data); //to remove /n character if exist
    $decoded_data = base64_decode($log_data);
    file_put_contents($file_name, $decoded_data);
}

function get_data($user, $time_stamp, $log_data) {

    $mysqli = connect_database();

    if (mysqli_connect_errno()) {
        close_db($mysqli);

        if ($mysqli->connect_errno) {
            //printf("Connect failed: %s\n", $mysqli->connect_error);
            //exit();
        }
        return LOGOUT_DB_ERROR_CONN;
    }

    $user=   $mysqli->real_escape_string($user);
    $time_stamp=   $mysqli->real_escape_string($time_stamp);
    $log_data=   $mysqli->real_escape_string($log_data);

    $sql = "SELECT * FROM public_users WHERE username = '$user'";

    $comments = $mysqli->query($sql);

    if ($comments == false) {
        close_db($mysqli);
        return LOGOUT_AUTH_ERROR;
    }

    if ($comments->num_rows <= 0) {
        close_db($mysqli);
        return LOGOUT_AUTH_ERROR;
    }

    $sql2 = "UPDATE public_users SET time_stamp = '0000-00-00 00:00:01' WHERE username = '$user'";

    if (!$mysqli->query($sql2)) {
        close_db($mysqli);
        return LOGOUT_DB_ERROR_UPDATE;
    } else {
        write_user_logs($user, $log_data);
    }

    close_db($mysqli);

    return LOGOUT_SUCCESS;
}

function connect_database() {
    $con = connect_db();
    return $con;
}

function close_db($con) {
    $con->close();
}

try {

    $data = file_get_contents('php://input');

    $user = "";
    $log_data = "";
    $time_stamp = "";
    $uudid = "";
	
	$FBI=new CI_Security();

    if (is_object(json_decode($data))) {

        $json = json_decode($data);

        if (property_exists($json, 'user')) {
            $user = $json->{'user'};
			$user=$FBI->xss_clean($user);

        }

        if (property_exists($json, 'log_data')) {
            $log_data = $json->{'log_data'};
			$log_data=$FBI->xss_clean($log_data);

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

            $response = get_data($user, $time_stamp, $log_data);
            echo $response . ",";
        }
    }
} catch (Exception $e) {
    die(LOGOUT_ERROR); //nothing to do
}
?>