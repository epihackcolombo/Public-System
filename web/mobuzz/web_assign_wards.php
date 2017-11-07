<?php
session_start();

include 'Security.php';
$CI = new CI_Security();
?>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>PHI assign Wards - Dengue Breeding Site Reporting Application</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600"
              rel="stylesheet">
        <link href="css/font-awesome.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/pages/dashboard.css" rel="stylesheet">
        <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
              <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
            <![endif]-->
    </head>
    <body>
        <?php
        require("constantes.php");

        if (!isset($_SESSION['admin'])) {
            header('Location: /web_adminlogin.php');
        }
        ?>

        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container"> <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"><span
                            class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span> </a><a class="brand" href="index.html">CMC Mobitel Dengue Breeding Site Reporting Application </a>
                    <div class="nav-collapse">
                        <ul class="nav pull-right">
                            <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><span style="color:#333;font-size:14px;"><i
                                            class="icon-user"></i>&nbsp;<strong>Hi&nbsp;&nbsp;</strong><?php echo $_SESSION['username'] ?></span><b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="web_adminlogin.php?logout=logout">Logout</a></li>
                                </ul>
                            </li>
                        </ul>
                        <!-- <form class="navbar-search pull-right">
                           <input type="text" class="search-query" placeholder="Search">
                         </form> -->
                    </div>
                    <!--/.nav-collapse --> 
                </div>
                <!-- /container --> 
            </div>
            <!-- /navbar-inner --> 
        </div>
        <!-- /navbar -->
        <div class="subnavbar">
            <div class="subnavbar-inner">
                <div class="container">
                    <ul class="mainnav">
                        <li ><a href="web_tcg_cpanel.php"><i class="icon-dashboard"></i><span>Dashboard</span> </a> </li>
                        <li><a href="web_requests.php"><i class="icon-share"></i><span>Complaints</span> </a> </li>
                        <li><a href="web_phi.php"><i class="icon-user-md"></i><span>PHI</span> </a></li>
                        <li><a href="web_map.php"><i class="icon-map-marker"></i><span>Map</span> </a> </li>
                        <li class="dropdown"><a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown"> <i class="icon-long-arrow-down"></i><span>Clusters</span> <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="web_map_public_show.php"><i class="icon-map-marker"></i><span>&nbsp;Generate clusters</span> </a></li>
                            <li><a href="web_map_public_show_data.php"><i class="icon-map-marker"></i><span>&nbsp;Cluster data</span> </a>   </li>
                        </ul></li>
                        <li><a href="web_public_users.php"><i class="icon-group"></i><span>Public Users</span> </a> </li>
                        <li><a href="web_requests_byuser.php"><i class="icon-search"></i><span>Search</span> </a> </li>
                        <li><a href="web_gallery.php"><i class="icon-th-large"></i><span>Gallery</span> </a> </li>
                        <li><a href="web_download.php"><i class="icon-download-alt"></i><span>Downloads</span> </a> </li>
                        <li><a href="web_moh_details.php"><i class="icon-home"></i><span>MOH</span> </a> </li>
                        <li><a href="web_admin_users.php"><i class="icon-briefcase"></i><span>Administrator</span> </a> </li> 
                        <li> </li>
                    </ul>
                    <!-- Modal -->
                    <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h3 id="myModalLabel">Thank you</h3>
                        </div>
                        <div class="modal-body">
                            <p>Click Send button to confirm</p>
                        </div>
                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                            <button class="btn btn-primary" onClick="send();">Send</button>
                        </div>
                    </div>
                </div> <!-- /controls -->	
            </div> <!-- /control-group -->

        </div>
        <!-- /container --> 
    </div>
    <!-- /subnavbar-inner --> 
</div>
<!-- /subnavbar -->
<div class="main">
    <div class="main-inner">
        <div class="container">
            <div class="row">
                <div class="span8">
                    <div class="widget widget-nopad">
                        <div class="widget-header"> <i class="icon-list-alt"></i>
                            <h3> Current information</h3>
                        </div>
                        <!-- /widget-header -->
                        <div class="widget-content">

                            <?php
                            if (isset($_GET['id'])) {
                                $id = $_GET['id'];

                                $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD")); //"mysql5.000webhost.com","a3035840_pubapp","manju104");

                                if (!$con) {
                                    die('Could not connect: ' . mysql_error());
                                }

                                mysql_select_db(constant("DATABASE"), $con); //"a3035840_pubapp",$con);
                                $id = $CI->xss_clean(mysql_real_escape_string($id));
                                $id = preg_replace('/[^a-zA-Z0-9_ %\[\]\.\(\)%&-]/s', '', $id);
                                if (isset($_POST['Submit'])) {

                                    $w = $_POST["wards"];
                                    $w = $_POST["wards"];
                                    $w = implode(",", $w);
                                    $w = mysql_real_escape_string($w);
                                    $query = "UPDATE phi_users SET ward = '$w' WHERE id=" . $id . "";
                                    //echo $query;
                                    $result = mysql_query($query, $con);
                                }
                                $id = $CI->xss_clean(mysql_real_escape_string($id));
                                $id = preg_replace('/[^a-zA-Z0-9_ %\[\]\.\(\)%&-]/s', '', $id);
                                $query = "SELECT * FROM phi_users WHERE id=" . $id . "";
//echo $query;
                                $result = mysql_query($query, $con);

                                $row = mysql_fetch_array($result, MYSQL_ASSOC);
                                echo '<div class="news-item-date"><a class="shortcut" href="javascript:;"><i class="shortcut-icon icon-user" style="font-size:45px;"></i><span class="shortcut-label"></span> </a> </div>';
                                echo '<div class="news-item-detail">';
                                echo "<br/>";
                                echo "<h3> Name : <span style='color:#FF7F74;' >" . $row['full_name'] . "</span></h3>";
                                echo "<br/>";
                                echo "<h3> User name : <span style='color:#FF7F74;' >" . $row['username'] . "</span></h3>";
                                echo "<br/>";
                                echo "<h3> Contact no : <span style='color:#FF7F74;' >" . $row['contact_no'] . "</span></h3>";
                                echo "<br/>";
                                echo "<h3> EMail : <span style='color:#FF7F74;' >" . $row['email'] . "</span></h3>";
                                echo "<br/>";

                                $output = $row['ward'];

                                echo "<h3> Current Wards :  <span style='color: #ff7f74;
display: block;
max-width: 500px;
word-wrap: break-word;' >" . str_replace('*', 'noward', $output) . "</span></h3>";

                                echo "<br/>";
                                echo "</div>";
                                mysql_close($con);
                            }
                            ?> 

                        </div>
                        <!-- /widget-content --> 

                    </div>
                    <!-- /widget -->

                </div>
                <!-- /span8 -->
                <div class="span4">
                    <div class="widget widget-nopad">
                        <div class="widget-header"> <i class="icon-list-alt"></i>
                            <h3> Change Wards</h3>
                        </div>
                        <!-- /widget-header -->
                        <div class="widget-content"><br>
                            <form action="" method="post">
                                New Wards: <span id="sprytextfield1">
                                <!--<input type="text" name="wards" value="<?php /* echo $row['ward'] */ ?>" /> -->
                                    <!-- Build your select: -->
                                    <select class="multiselect" multiple="multiple" name="wards[]">
                                        <?php
                                        $comma_separated = explode(',', $row['ward']);
                                        if (in_array('*', $comma_separated)) {
                                            echo '<option value="*" selected="selected" >No wards</option>';
                                        } else {
                                            echo '<option value="*">No wards</option>';
                                        }
                                        if (in_array('0', $comma_separated)) {
                                            echo '<option value="0" selected="selected" >Outside CMC</option>';
                                        } else {
                                            echo '<option value="0">Outside CMC</option>';
                                        }										
                                        for ($i = 1; $i < 48; $i++) {
                                            if (in_array($i, $comma_separated)) {
                                                echo '<option value="' . $i . '" selected="selected" >' . $i . '</option>';
                                            } else {
                                                echo "<option value='$i'>$i</option>";
                                            }
                                        }
                                        ?>
                                    </select>

                                    <span class="textfieldRequiredMsg">A value is required.</span></span>
                                <input type="Submit" class="btn btn-primary" name="Submit" value="Submit" />
                            </form>
                        </div>

                    </div>
                    <a href="/mobuzz/web_phi.php" style="font-size:14px; margin-left:10px;" > <button class="btn btn-primary">Back</button></a>
                </div>
            </div>
            <!-- /row --> 
        </div>
        <!-- /container --> 
    </div>
    <!-- /main-inner --> 
</div>
<!-- /main -->
<div class="footer">
    <div class="footer-inner">
        <div class="container">
            <div class="row">
                <div class="span12"><span style="text-align:center">  &copy; 2014 All Rights Reserved. </span></div>
                <!-- /span12 --> 
            </div>
            <!-- /row --> 
        </div>
        <!-- /container --> 
    </div>
    <!-- /footer-inner --> 
</div>
<!-- /footer --> 
<!-- Le javascript
================================================== --> 
<!-- Placed at the end of the document so the pages load faster --> 
<style>

    .textfieldRequiredMsg, .textfieldInvalidFormatMsg, .textfieldMinValueMsg, .textfieldMaxValueMsg, .textfieldMinCharsMsg, .textfieldMaxCharsMsg, .textfieldValidMsg {
        display: none;
    }


    .textfieldRequiredState .textfieldRequiredMsg, .textfieldInvalidFormatState .textfieldInvalidFormatMsg, .textfieldMinValueState .textfieldMinValueMsg, .textfieldMaxValueState .textfieldMaxValueMsg, .textfieldMinCharsState .textfieldMinCharsMsg, .textfieldMaxCharsState .textfieldMaxCharsMsg {
        color: #cc3333;
        display: inline;
    }
</style>
<script src="js/jquery-1.7.2.min.js"></script> 
<script src="js/excanvas.min.js"></script> 
<script src="js/bootstrap.js"></script>

<script src="js/base.js"></script> 
<script type="text/javascript">
                                /*var sprytextfield1 = new Spry.Widget.ValidationTextField("sprytextfield1");*/
</script>
</body>
</html>
