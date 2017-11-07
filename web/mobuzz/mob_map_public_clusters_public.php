<?php

require 'db_connection.php';
require 'profile_public_validate.php';
include 'Security.php';
require 'global_variables.php';


function get_data($year, $week)
{

    //------------------ Get MOH Data ------------------//
    
    $jresultmoh = " ";
    $mysqli2 = connect_database();

    if (mysqli_connect_errno()) {
        close_db($mysqli2);

        if ($mysqli2->connect_errno) {
            printf("Connect failed: %s\n", $mysqli2->connect_error);
            exit();
        }
        return DB_ERROR_CONN_JSON;
    }
    
    $sql_select2 = "SELECT * FROM cmc_district WHERE id <= 6";

    $comments2 = $mysqli2->query($sql_select2);

    if ($comments2 == false) {
        close_db($mysqli2);
        return DB_ERROR_SELECT_JSON;
    }

    $jresultmoh = "[";
    $x=0;
    
    if ($comments2->num_rows > 0) {
        
        while ($row2 = mysqli_fetch_array($comments2)) {
            
            if($x==0)
            {
                $jresultmoh .= "{'id':$x, 'district_moh':'$row2[district_moh]', 'moh_address':'$row2[moh_address]', 'moh_telephone':'$row2[moh_telephone]', 'moh_doctor':'$row2[moh_doctor]', 'moh_doctor_telephone':'$row2[moh_doctor_telephone]', 'moh_location':'$row2[moh_location]'}";
            }
            else {
                $jresultmoh .= ",{'id':$x, 'district_moh':'$row2[district_moh]', 'moh_address':'$row2[moh_address]', 'moh_telephone':'$row2[moh_telephone]', 'moh_doctor':'$row2[moh_doctor]', 'moh_doctor_telephone':'$row2[moh_doctor_telephone]', 'moh_location':'$row2[moh_location]'}";
            }
            
            $x++;
        }
        
    }
    
    close_db($mysqli2);

    if(strlen($jresultmoh)<5)
    {
        $jresultmoh = " ";
    }
    else
    {
        $jresultmoh .= "]";
    }

    //echo $jresultmoh;

    //------------------ Get Cluster Data ------------------//
    
    $jresultcluster = " ";
    $mysqli = connect_database();

    if (mysqli_connect_errno()) {
        close_db($mysqli);

        if ($mysqli->connect_errno) {
            printf("Connect failed: %s\n", $mysqli->connect_error);
            exit();
        }
        return DB_ERROR_CONN_JSON;
    }

    $sql_select = "SELECT * FROM hotspots_cluster_data WHERE year='$year' and week_of_year='$week'";

    $comments = $mysqli->query($sql_select);

    if ($comments == false) {
        close_db($mysqli);
        return DB_ERROR_SELECT_JSON;
    }

    if ($comments->num_rows <= 0) {

        //get the nearest week 
        $sql_select_max_week = "SELECT *, MAX(HS.week_of_year) AS week_of_year_max FROM (SELECT * FROM hotspots_cluster_data WHERE year='$year' and week_of_year < '$week' ORDER BY week_of_year DESC) AS HS WHERE year='$year'"; //this is a work-around to get the first line from the inner select

        $comments2 = $mysqli->query($sql_select_max_week);

        if ($comments2 == false) {
            close_db($mysqli);
            return DB_ERROR_SELECT_JSON;
        }

        if ($comments2->num_rows <= 0) {
            //no sql data
            close_db($mysqli);
            //return DB_ERROR_NODATA_JSON;
        } else {
            $row2 = $comments2->fetch_array(MYSQLI_ASSOC);
            $json_data2 = $row2['json_data'];
            close_db($mysqli);
            //return $json_data2;
            $jresultcluster = $json_data2;
        }

    } else {
        $row = $comments->fetch_array(MYSQLI_ASSOC);
        $json_data = $row['json_data'];
        close_db($mysqli);
        //return $json_data;
        $jresultcluster = $json_data;
    }
    
    return $jresultmoh."&".$jresultcluster;
    
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

    $user = "";
    $time_stamp = "";
    $uudid = "";

    if (is_object(json_decode($data))) {

        $json = json_decode($data);
        $FBI = new CI_Security();


        if (property_exists($json, 'user')) {
            $user = $json->{'user'};
            $user = $FBI->xss_clean($user);
        }

        if (property_exists($json, 'time_stamp')) {
            $time_stamp = $json->{'time_stamp'};
            $time_stamp = $FBI->xss_clean($time_stamp);
        }

        if (property_exists($json, 'uudid')) {
            $uudid = $json->{'uudid'};
            $uudid = $FBI->xss_clean($uudid);
        }


        $isvaliduser = user_validate($user, $time_stamp, $uudid);

        if (!empty($isvaliduser)) {

            echo $isvaliduser;
        } else {

            date_default_timezone_set('Asia/Kolkata');
            $date = new DateTime();
            $tm_year = $date->format('Y');
            $tm_week = $date->format('W');

            echo get_data($tm_year, $tm_week);
        }
    }
} catch (Exception $e) {
    die(""); //nothing to do
}
?>