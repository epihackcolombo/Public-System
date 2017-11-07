<?php

require 'db_connection.php';
require 'util_manage_time.php';
require 'global_variables.php';
include 'Security.php';


function get_data($user, $password, $imei, $mobile, $uudid, $time_stamp)
{


    $mysqli = connect_db();
    $pref_language = "";


    if (mysqli_connect_errno()) {
        close_db($mysqli);
        return DB_ERROR_CONN_JSON;
    }

    $user = $mysqli->real_escape_string($user);
    $password = $mysqli->real_escape_string($password);
    $imei = $mysqli->real_escape_string($imei);
    $mobile = $mysqli->real_escape_string($mobile);
    $uudid = $mysqli->real_escape_string($uudid);
    $time_stamp = $mysqli->real_escape_string($time_stamp);

    $sql = "SELECT * FROM phi_users WHERE username = '$user'";

    $comments = $mysqli->query($sql);

    if ($comments == false) {
        close_db($mysqli);
        return LOGIN_USER_ERROR_AUTH_JSON;
    }
    if ($comments->num_rows <= 0) {
        close_db($mysqli);
        return LOGIN_USER_ERROR_AUTH_JSON;
    }

    $row = $comments->fetch_array(MYSQLI_ASSOC);

    if (!empty($row['language'])) {
        $pref_language = $row['language'];
    }

    if (strcmp($password, $row['password']) != 0) {
        close_db($mysqli);
        return LOGIN_USER_ERROR_AUTH_JSON;
    }
    if ($row['user_status'] == '0') {
        close_db($mysqli);
        return LOGIN_USER_ERROR_BLOCK_JSON;
    }


    $str_ward = trim($row['ward']," ");

    if(strlen($str_ward)== 0) {
        close_db($mysqli);
        return LOGIN_USER_ERROR_WARD_JSON;        
    }

    if (strpos($str_ward, "*") !== false) {
        close_db($mysqli);
        return LOGIN_USER_ERROR_WARD_JSON;
    }
    $nowtimestamp = nowTimestamp();

    $sql2 = "UPDATE phi_users SET sim_serial_no = '$imei', device_sim_no = '$mobile', device_udid = '$uudid', time_stamp = '$time_stamp', last_server_login = $nowtimestamp WHERE username = '$user'";

    if (!$mysqli->query($sql2)) {
        close_db($mysqli);
        return LOGIN_DB_ERROR_UPDATE_JSON;
    }

    close_db($mysqli);

    return "{'status':'ok', 'language':'$pref_language'}";
}

function close_db($con)
{

    $con->close();
}

try {

    $data = file_get_contents('php://input');

    $json = "";
    $user = "";
    $password = "";
    $imei = "";
    $mobile = "";
    $uudid = "";
    $time_stamp = "";


    if (is_object(json_decode($data))) {

        $json = json_decode($data);
        $FBI = new CI_Security();


        if (property_exists($json, 'user')) {
            $user = $json->{'user'};
            $user = $FBI->xss_clean($user);
        }

        if (property_exists($json, 'password')) {
            $password = $json->{'password'};
            $password = $FBI->xss_clean($password);
        }

        if (property_exists($json, 'imei')) {
            $imei = $json->{'imei'};
            $imei = $FBI->xss_clean($imei);
        }

        if (property_exists($json, 'm_mobile')) {
            $mobile = $json->{'m_mobile'};
            $mobile = $FBI->xss_clean($mobile);
        }

        if (property_exists($json, 'uudid')) {
            $uudid = $json->{'uudid'};
            $uudid = $FBI->xss_clean($uudid);
        }

        if (property_exists($json, 'time_stamp')) {
            $time_stamp = $json->{'time_stamp'};
            $time_stamp = $FBI->xss_clean($time_stamp);
        }

        $response = get_data($user, $password, $imei, $mobile, $uudid, $time_stamp);

        echo $response;
    } else {
        echo LOGIN_USER_ERROR_AUTH_JSON;
    }

} catch (Exception $e) {
    die("internal_error"); //nothing to do
}
?>