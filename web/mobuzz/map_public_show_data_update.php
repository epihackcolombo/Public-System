<?php

$id = intval($_REQUEST['id']);
$year = $_REQUEST['year'];
$week_of_year = $_REQUEST['week_of_year'];
$location_lat = $_REQUEST['location_lat'];
$location_lng = $_REQUEST['location_lng'];
$ref_id = $_REQUEST['ref_id'];

include 'db_connection.php';

$db = connect_db();

$sql = "update hotspots_location_data set year='$year',week_of_year='$week_of_year',location_lat='$location_lat',location_lng='$location_lng',ref_id='$ref_id' where id=$id";
mysqli_query($db,$sql);
echo json_encode(array(
	'id' => $id,
	'year' => $year,
	'week_of_year' => $week_of_year,
	'location_lat' => $location_lat,
	'location_lng' => $location_lng,
	'ref_id' => $ref_id
));
?>