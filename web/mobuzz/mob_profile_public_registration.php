<?php

require 'db_connection.php';
require 'global_variables.php';
include 'Security.php';


function get_data($user, $password, $name, $email, $gender, $residence, $language, $p_mobile, $imei, $m_mobile, $uudid, $time_stamp, $age, $model) {

    $mysqli = connect_db();

    if ($user == NULL || $user == "") {
        close_db($mysqli);
        return DB_ERROR_PARAM_JSON;
    }

    if ($password == NULL || $password == "") {
        close_db($mysqli);
        return DB_ERROR_PARAM_JSON;
    }

    if (mysqli_connect_errno()) {
        close_db($mysqli);
        return DB_ERROR_CONN_JSON;
    }

    $user=   $mysqli->real_escape_string($user);
    $password=   $mysqli->real_escape_string($password);
    $name=   $mysqli->real_escape_string($name);
    $email=   $mysqli->real_escape_string($email);
    $gender=   $mysqli->real_escape_string($gender);
    $residence=   $mysqli->real_escape_string($residence);
    $language=   $mysqli->real_escape_string($language);
    $p_mobile=   $mysqli->real_escape_string($p_mobile);
    $m_mobile=   $mysqli->real_escape_string($m_mobile);
    $uudid=   $mysqli->real_escape_string($uudid);
    $time_stamp=   $mysqli->real_escape_string($time_stamp);
    $age=   $mysqli->real_escape_string($age);
    $model=   $mysqli->real_escape_string($model);

    $sql = "SELECT * FROM public_users WHERE username = '$user'";

    $comments = $mysqli->query($sql);

    if ($comments == false) {
        close_db($mysqli);
        return DB_ERROR_SELECT_JSON;
    }
    if ($comments->num_rows > 0) {
        close_db($mysqli);
        return PROF_REG_ERROR_AUTH_DUP;
    }

    if ($name == NULL)
        $name = "";
    if ($gender == NULL)
        $gender = "";
    if ($residence == NULL)
        $residence = "";
    if ($language == NULL)
        $language = "";
    if ($m_mobile == NULL)
        $m_mobile = "";
    if ($p_mobile == NULL)
        $$p_mobile = "";
    if ($imei == NULL)
        $imei = "";
    if ($uudid == NULL)
        $uudid = "";

    $sql_insert = "INSERT INTO public_users (username, password, full_name, email, gender, residence, language, contact_no, sim_serial_no, device_sim_no, device_udid, user_status, time_stamp, registered_age, registered_device_model, registered_date_time ) VALUES ('$user','$password', '$name','$email','$gender','$residence', '$language','$p_mobile', '$imei', '$m_mobile', '$uudid', 1, '$time_stamp', '$age', '$model', '$time_stamp')";

    if (!$mysqli->query($sql_insert)) {
        close_db($mysqli);
        return DB_ERROR_INSERT_JSON;
    }

    close_db($mysqli);

    return PROF_REG_SUCCESS;
}

function close_db($con) {

    $con->close();
}

try {

    $data = file_get_contents('php://input');

    $user = "";
    $password = "";
    $name = "";
    $email = "";
    $gender = "";
    $residence = "";
    $language = "";
    $p_mobile = "";
    $imei = "";
    $m_mobile = "";
    $uudid = "";
    $time_stamp = "";
    $age = 0;
    $model = "";
	
	$FBI=new CI_Security();

    if (is_object(json_decode($data))) {

        $json = json_decode($data);


        if (property_exists($json, 'user')) {
            $user = $json->{'user'};
            $user=$FBI->xss_clean($user);
        }

        if (property_exists($json, 'password')) {
            $password = $json->{'password'};
            $password=$FBI->xss_clean($password);
        }

        if (property_exists($json, 'name')) {
            $name = $json->{'name'};
            $name=$FBI->xss_clean($name);
        }

        if (property_exists($json, 'email')) {
            $email = $json->{'email'};
            $email=$FBI->xss_clean($email);
        }

        if (property_exists($json, 'gender')) {
            $gender = $json->{'gender'};
            $gender=$FBI->xss_clean($gender);
        }

        if (property_exists($json, 'residence')) {
            $residence = $json->{'residence'};
            $residence=$FBI->xss_clean($residence);
        }

        if (property_exists($json, 'language')) {
            $language = $json->{'language'};
            $language=$FBI->xss_clean($language);
        }

        if (property_exists($json, 'p_mobile')) {
            $p_mobile = $json->{'p_mobile'};
            $imei=$FBI->xss_clean($imei);
        }

        if (property_exists($json, 'imei')) {
            $imei = $json->{'imei'};
            $imei=$FBI->xss_clean($imei);
        }

        if (property_exists($json, 'mobile')) {
            $m_mobile = $json->{'mobile'};
            $m_mobile=$FBI->xss_clean($m_mobile);
        }

        if (property_exists($json, 'uudid')) {
            $uudid = $json->{'uudid'};
            $uudid=$FBI->xss_clean($uudid);
        }

        if (property_exists($json, 'time_stamp')) {
            $time_stamp = $json->{'time_stamp'};
            $time_stamp=$FBI->xss_clean($time_stamp);
        }

        if (property_exists($json, 'age')) {
            $age = $json->{'age'};
            $age=$FBI->xss_clean($age);
        }

        if (property_exists($json, 'model')) {
            $model = $json->{'model'};
            $model=$FBI->xss_clean($model);
        }

        $response = get_data($user, $password, $name, $email, $gender, $residence, $language, $p_mobile, $imei, $m_mobile, $uudid, $time_stamp, $age, $model);
        echo $response . ",";
    }
} catch (Exception $e) {
    die(INT_ERROR_SERVER); //nothing to do
}
?>