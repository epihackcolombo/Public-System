<?php

include 'db_connection.php';

$db = connect_db();

$rs = mysqli_query($db,'select * from hotspots_location_data order by year, week_of_year');
$result = array();
while($row = mysqli_fetch_object($rs)){
	array_push($result, $row);
}

echo json_encode($result);

?>