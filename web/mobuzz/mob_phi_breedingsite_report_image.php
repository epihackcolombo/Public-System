<?php

require 'db_connection.php';
require 'mob_profile_phi_validate.php';
require 'global_variables.php';
require 'util_manage_image.php';
include 'Security.php';


function get_data($id, $imagesize) {

    $con = connect_db();

    if (mysqli_connect_errno()) {
        echo DB_ERROR_CONN_JSON;
    }

    $id= $con->real_escape_string($id);

    $result = mysqli_query($con, "SELECT * FROM public_data WHERE id='$id'");

    $image_path = "";

    while ($row = mysqli_fetch_array($result)) {
        $image_path = $row['img_path'];
    }

    if (strcasecmp("thumbnail", $imagesize) == 0) {
        
        $diskimage = read_image_thumbnail($image_path);
        
        if (!empty($diskimage)) 
        {
            //return "{'img_type':'thumbnail', 'img_data':'".$diskimage."'}";
			return "{'img_type':'thumbnail', 'img_name':'".strrchr($image_path,"/").THUMB_EXT."', 'img_data':'".$diskimage."'}";
			
        }
        else 
        {
            //return "{'img_type':'actual', 'img_data':'".read_image($image_path)."'}";
			return "{'img_type':'actual', 'img_name':'".strrchr($image_path,"/")."', 'img_data':'".read_image($image_path)."'}";
        }

    }
    else if (strcasecmp("actual", $imagesize) == 0) {
        
        //return "{'img_type':'actual', 'img_data':'".read_image($image_path)."'}";
		return "{'img_type':'actual', 'img_name':'".strrchr($image_path,"/")."', 'img_data':'".read_image($image_path)."'}";
    }
    else
    {
        return read_image($image_path);
    }
}

header('Content-Type: application/json');

try {

    $data = file_get_contents('php://input');

    $id = "";
    $user = "";
    $time_stamp = "";
    $uudid = "";
    $imagesize = "";

    $FBI=new CI_Security();

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
        
        if (property_exists($json, 'imagesize')) {
            $imagesize = $json->{'imagesize'};
            $imagesize=$FBI->xss_clean($imagesize);
        }
        
        $isvaliduser = user_validate($user, $time_stamp, $uudid);

        if (!empty($isvaliduser)) {
            
            echo $isvaliduser;
        } else {
            
            $response = get_data($id, $imagesize);
            echo $response;
        }
        
    }
} catch (Exception $e) {
    die(INT_ERROR_SERVER); //nothing to do
}
?>
 