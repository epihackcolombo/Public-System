<?php


$year = $_REQUEST['year'];
$week_of_year = $_REQUEST['week_of_year'];
$location_lat = $_REQUEST['location_lat'];
$location_lng = $_REQUEST['location_lng'];
$ref_id = $_REQUEST['ref_id'];

include 'db_connection.php';
$db = connect_db();

$sql = "insert into hotspots_location_data(year,week_of_year,location_lat,location_lng,ref_id) values('$year','$week_of_year','$location_lat','$location_lng','$ref_id')";
mysqli_query($db,$sql);
echo json_encode(array(
	'id' => mysql_insert_id(),
	'year' => $year,
	'week_of_year' => $week_of_year,
	'location_lat' => $location_lat,
	'location_lng' => $location_lng,
	'ref_id' => $ref_id
));

?>