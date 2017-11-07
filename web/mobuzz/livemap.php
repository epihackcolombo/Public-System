 <?php

require("constantes.php");

// Create connection
$con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));

if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }
else{

}

mysql_select_db(constant("DATABASE"), $con);

$result = mysql_query("SELECT GPS FROM pubapp");

$array = "";

while($row = mysql_fetch_array($result))
  {
  $g = explode(",", $row['GPS']);
  $array .= trim($g[0]).",".trim($g[1]);
  $array .= ':';
  }

echo $array;

mysql_close($con);

?>