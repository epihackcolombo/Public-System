<?php

require 'db_connection.php';
require 'mob_profile_phi_validate.php';
require 'global_variables.php';
require 'util_manage_image.php';
require_once 'util_manage_ward.php';
include 'Security.php';


function write_user_imgs($user, $img_data) {

    return write_image($user, $img_data);
}

function get_data($image, $gps, $address, $date_time, $user, $time_stamp, $remarks, $assess)
{
    $image_path = "";

    if ($image == null || empty($image)) {
        //nothing to do, no image.
    } else {
        $image_path = write_user_imgs($user, $image);

        if ($image_path == null) {
            return DB_ERROR_CONN_JSON;
        }
    }


    $mysqli = connect_database();

    //$binary = $mysqli->real_escape_string(base64_decode($image));

    if (mysqli_connect_errno()) {
        return DB_ERROR_CONN_JSON;
    }

    $ll = explode(',', $gps, 3);

    set_boundaries();

    $ward = get_ward(floatval($ll[1]), floatval($ll[0]));

    $gps = $mysqli->real_escape_string($gps);
    $address = $mysqli->real_escape_string($address);
    $date_time = $mysqli->real_escape_string($date_time);
    $user = $mysqli->real_escape_string($user);
    $time_stamp = $mysqli->real_escape_string($time_stamp);
    $remarks = $mysqli->real_escape_string($remarks);
    $assess = $mysqli->real_escape_string($assess);

    $sql = "INSERT INTO public_data (gps, username, address, verified, ward, date_time, remarks, assessment ,img_path)
            VALUES ('$gps', '$user', '$address', 101, $ward, '$date_time', '$remarks', '$assess','$image_path')";


    if (!$mysqli->query($sql)) {
        close_db($mysqli);
        return DB_ERROR_PARAM_JSON;
    }

    close_db($mysqli);

    return "ok";
}

function connect_database()
{
    $con = connect_db();
    return $con;
}

function close_db($con)
{
    $con->close();
}

try {

    $data = file_get_contents('php://input');

    $image = "";
    $location = "";
    $date = "";
    $user = "";
    $time_stamp = "";
    $address = "";
    $remarks = "";
    $assess = "";
    $uudid = "";

    $FBI = new CI_Security();

    if (is_object(json_decode($data))) {

        $json = json_decode($data);


        if (property_exists($json, 'user')) {
            $user = $json->{'user'};
            $user = $FBI->xss_clean($user);
        }

        if (property_exists($json, 'time_stamp')) {
            $time_stamp = $json->{'time_stamp'};
            $time_stamp = $FBI->xss_clean($time_stamp);
        }

        if (property_exists($json, 'image')) {
            $image = $json->{'image'};
            $image = $FBI->xss_clean($image);
        }

        if (property_exists($json, 'location')) {
            $location = $json->{'location'};
            $location = $FBI->xss_clean($location);
        }

        if (property_exists($json, 'date')) {
            $date = $json->{'date'};
            $date = $FBI->xss_clean($date);
        }

        if (property_exists($json, 'address')) {
            $address = $json->{'address'};
            $address = $FBI->xss_clean($address);
        }

        if (property_exists($json, 'remarks')) {
            $remarks = $json->{'remarks'};
            $remarks = $FBI->xss_clean($remarks);
        }

        if (property_exists($json, 'assess')) {
            $assess = $json->{'assess'};
            $assess = $FBI->xss_clean($assess);
        }

        if (property_exists($json, 'uudid')) {
            $uudid = $json->{'uudid'};
            $uudid = $FBI->xss_clean($uudid);
        }


        $isvaliduser = user_validate($user, $time_stamp, $uudid);

        if (!empty($isvaliduser)) {

            echo $isvaliduser;
        } else {

            $response = get_data($image, $location, $address, $date, $user, $time_stamp, $remarks, $assess);
            echo $response . ",";
        }
    }
} catch (Exception $e) {
    die(INT_ERROR_SERVER); //nothing to do
}

?>
