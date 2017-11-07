<?php

require 'util_manage_time.php';

function user_validate($user, $time_stamp, $uudid) {

    if ($user == "" | $user == NULL) {
        return "{'status':'error_db_params'}";
    }

    if ($time_stamp == 0 | $time_stamp == NULL | $time_stamp == "") {
        return "{'status':'error_db_params'}";
    }

    if ($uudid == "" | $uudid == NULL) {
        return "{'status':'error_db_params'}";
    }

	$mysqli = connect_db();

    if (mysqli_connect_errno()) {
        return "{'status':'error_db_connect'}";
    }

    $user=   $mysqli->real_escape_string($user);
    $time_stamp=   $mysqli->real_escape_string($time_stamp);
    $uudid=   $mysqli->real_escape_string($uudid);

	$sql_select = "SELECT `device_udid`, `time_stamp`, `user_status`, `last_server_login` FROM public_users WHERE username = '$user'";

	$result = $mysqli->query($sql_select);
	
    if ($result == false || $result->num_rows < 1) {
        $mysqli->close();
	return "{'status':'error_db_params'}";
    }
	
    $c_time_stamp = "";
    $c_uuid = "";
    $c_status = 0;
    $c_last_login = 0;
    $c_nowtimestamp = nowTimestamp();

    while ($row = mysqli_fetch_array($result)) {
        $c_uuid = $row['device_udid'];
        $c_time_stamp = $row['time_stamp'];
        $c_status = $row['user_status'];
        $c_last_login = $row['last_server_login'];
    }

    $mysqli->close();

    $numDays = abs($c_nowtimestamp - $c_last_login) / 60 / 60 / 24;

    // validation of user with: username, device, active-status
    if ($c_uuid != $uudid || $c_time_stamp != $time_stamp || $c_status < 1) {
        return "{'status':'authentication_required'}";
    }

    // if user not login recently 
    if ($numDays > 2.0) {
        return "{'status':'authentication_expired'}";
    }

    return "";
}

?>