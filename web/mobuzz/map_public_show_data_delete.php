<?php

$id = intval($_REQUEST['id']);

include 'db_connection.php';

$db = connect_db();

$sql = "delete from hotspots_location_data where id=$id";
mysqli_query($db,$sql);
echo json_encode(array('success'=>true));
?>