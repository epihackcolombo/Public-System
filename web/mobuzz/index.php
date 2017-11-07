<?php session_start(); ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Untitled Document</title>
</head>
<body>
<?php

    header('Location: web_adminlogin.php');  

?>
<link rel="stylesheet" type="text/css" href="styles.css">

<div id="headerbg">
<div align="right"><a href="web_adminlogin.php?logout=logout"><h3>Logout</h3></a></div>
<h1>CMC Mobitel Dengue Breeding Site Reporting Application</h1>
</div>

<div id="container-map">

<h1> Map </h1><br/>
<div align="center">
<?php include 'googlemap.php'; ?>
</div>

</div>
<div id="footer"><p>Copyright 2013. All Rights Reserved.</p></div>

</body>
</html>
