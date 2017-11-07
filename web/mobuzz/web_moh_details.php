<?php session_start(); 
date_default_timezone_set('UTC');
include 'Security.php';
$CI = new CI_Security();
?>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Public Users - Dengue Breeding Site Reporting Application</title>
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
            header('Location: web_adminlogin.php');
        }
        ?>
        
        <?php
        



            //---------------- update moh ----------------//
  
            if(isset($_POST['updatemoh']))
            {
                $updateloc = "";
                $updatetitle = "";
                $updateaddress = "";
                $updatetel = "";
                $updatedoctor = "";
                $updatedoctortel = "";
                
                if(isset($_POST['savemohlocation']))
                {
                    $updateloc = $_POST['savemohlocation'];
                    $updateloc=$CI->xss_clean($updateloc);
                }
                if(isset($_POST['savemohtitle']))
                {
                    $updatetitle = $_POST['savemohtitle'];
                    $updatetitle=$CI->xss_clean($updatetitle);
                }
                if(isset($_POST['savemohaddress']))
                {
                    $updateaddress = $_POST['savemohaddress'];
                    $updateaddress=$CI->xss_clean($updateaddress);
                }
                if(isset($_POST['savemohtel']))
                {
                    $updatetel = $_POST['savemohtel'];
                    $updatetel=$CI->xss_clean($updatetel);
                }
                if(isset($_POST['savemohdoctor']))
                {
                    $updatedoctor = $_POST['savemohdoctor'];
                    $updatedoctor=$CI->xss_clean($updatedoctor);
                }
                if(isset($_POST['savemohdoctortel']))
                {
                    $updatedoctortel = $_POST['savemohdoctortel'];
                    $updatedoctortel=$CI->xss_clean($updatedoctortel);
                }
				
                
                $con1 = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));

                if (!$con1) {
                    die('Could not connect: ' . mysql_error());
                }

                mysql_select_db(constant("DATABASE"), $con1);
                
                $updateloc = mysql_real_escape_string($updateloc);
                $updatetitle = mysql_real_escape_string($updatetitle);
                $updateaddress = mysql_real_escape_string($updateaddress);
                $updatetel = mysql_real_escape_string($updatetel);
                $updatedoctor =  mysql_real_escape_string($updatedoctor);
                $updatedoctortel =  mysql_real_escape_string($updatedoctortel);

                $queryupdate =  "UPDATE `mobuzz_db`.`cmc_district`
                                SET
                                `district_moh` = '$updatetitle',
                                `moh_address` = '$updateaddress',
                                `moh_telephone` = '$updatetel',
                                `moh_doctor` = '$updatedoctor',
                                `moh_doctor_telephone` = '$updatedoctortel',
                                `moh_location` = '$updateloc'
                                 WHERE `id` = ".$_POST['updatemoh'];

                $updatestatus = mysql_query($queryupdate, $con1);
                
                if($updatestatus)
                {
                    echo "<script> alert('MOH details updated!'); </script>";
                }
                else 
                {
                    echo "<script> alert('Update failed'); </script>";
                }

                mysql_close($con1);
            }
            else {
                //echo "no query!!!";
            }

            
            //---------------- edit moh ----------------//
            
            $editid = "";
            $editdistrict = "";
            $editloc = "";
            $edittitle = "";
            $editaddress = "";
            $edittel = "";
            $editdoctor = "";
            $editdoctortel = "";
            
            
            if(isset($_POST['editid']))
            {
                $editid = $_POST['editid'];
            }
            if(isset($_POST['editdistrict']))
            {
                $editdistrict = $_POST['editdistrict'];
            }
            if(isset($_POST['editloc']))
            {
                $editloc = $_POST['editloc'];
            }
            if(isset($_POST['edittitle']))
            {
                $edittitle = $_POST['edittitle'];
            }
            if(isset($_POST['editaddress']))
            {
                $editaddress = $_POST['editaddress'];
            }
            if(isset($_POST['edittel']))
            {
                $edittel = $_POST['edittel'];
            }
            if(isset($_POST['editdoctor']))
            {
                $editdoctor = $_POST['editdoctor'];
            }
            if(isset($_POST['editdoctortel']))
            {
                $editdoctortel = $_POST['editdoctortel'];
            }
            

        ?> 


        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container"> <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"><span
                            class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span> </a><a class="brand" href="web_tcg_cpanel.php">Mo-Buzz Dengue Management Console</a>
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
                    <ul class="mainnav" style="margin-right: -200px;">
                        <li><a href="web_tcg_cpanel.php"><i class="icon-dashboard"></i><span>Dashboard</span> </a> </li>
                        <li><a href="web_requests.php"><i class="icon-share"></i><span>Complaints</span> </a> </li>
                        <li ><a href="web_phi.php"><i class="icon-user-md"></i><span>PHI</span> </a></li>
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
                        <li  class="active"><a href="web_moh_details.php"><i class="icon-home"></i><span>MOH</span> </a> </li>
                        <li><a href="web_admin_users.php"><i class="icon-briefcase"></i><span>Administrator</span> </a> </li> 
                        <li> </li>
                    </ul>
                    <!-- Modal -->


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
                <div class="span12">
                    
                    <div id="moheditdetails">

                        <div class="widget widget-table action-table">
                            <div class="widget-header"> <i class="icon-briefcase"></i>
                                <h3>Edit MOH Details</h3>
                            </div>
                            <!-- /widget-header -->
                            <div class="widget-content">

                                <div style="margin-left:12px; margin-top:10px; margin-bottom:15px;">

                                    <form id="updatemohdetails" method="POST">

                                        <table>
                                            <tr>
                                                <td><h3>CMC District </h3></p></td>
                                                <td style="width:235px;"><h3> : <span style="color:#FF7F74;"><?php echo $editdistrict; ?></span></h3></p></td>
                                                <td style="width:350px;"></td>
                                            </tr>                                        
                                            <tr>
                                                <td><h3>MOH Title </h3></p></td>
                                                <td><h3> : <span style="color:#FF7F74;"><input type="text" id="savemohtitle" name="savemohtitle" size="40" value='<?php echo $edittitle; ?>'></span></h3></p></td>
                                            </tr>
                                            <tr>
                                                <td><h3>MOH Location </h3></p></td>
                                                <td><h3> : <span style="color:#FF7F74;"><input type="text" id="savemohlocation" name="savemohlocation" size="40" value='<?php echo $editloc; ?>'></span></h3></p></td>
                                            </tr>
                                            <tr>
                                                <td><h3>MOH Address </h3></p></td>
                                                <td colspan="2"><h3> : <span style="color:#FF7F74;"><input type="text"  class="input-xxlarge" id="savemohaddress" name="savemohaddress" size="200" value='<?php echo $editaddress; ?>'></span></h3></p></td>
                                            </tr>
                                            <tr>
                                                <td><h3>MOH Telephone </h3></p></td>
                                                <td><h3> : <span style="color:#FF7F74;"><input type="text" id="savemohtel" name="savemohtel" size="40" value='<?php echo $edittel; ?>'></span></h3></p></td>
                                                <td style="vertical-align:top"><span style="color:#FF7F74;">Numbers, Spaces and Hyphens only!</span></td>
                                            </tr>
                                            <tr>
                                                <td><h3>Doctor </h3></p></td>
                                                <td><h3> : <span style="color:#FF7F74;"><input type="text" id="savemohdoctor" name="savemohdoctor" size="50" value='<?php echo $editdoctor; ?>'></span></h3></p></td>
                                            </tr>
                                            <tr>
                                                <td><h3>Doctor Telephone </h3></p></td>
                                                <td><h3> : <span style="color:#FF7F74;"><input type="text" id="savemohdoctortel" name="savemohdoctortel" size="40" value='<?php echo $editdoctortel; ?>'></span></h3></p></td>
                                                <td style="vertical-align:top"><span style="color:#FF7F74;">Numbers, Spaces and Hyphens only!</span></td>
                                            </tr>
                                            <tr>
                                                <td><input type='button' class='btn btn-small btn-warning' id="submitmohbutton" value=' Save Changes '/></td>
                                                <td><input type="hidden" id="updatemoh" name="updatemoh" value="<?php echo $editid; ?>"></td>
                                            </tr>
                                        </table>

                                    </form>

                                </div>

                            </div>
                        </div>

                    </div>
                    
                    <div class="widget widget-table action-table">
                        <div class="widget-header"> <i class="icon-briefcase"></i>
                            <h3>MOH Popup Details</h3>
                        </div>
                        <!-- /widget-header -->
                        <div class="widget-content">

                            <?php
                            $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));

                            if (!$con) {
                                die('Could not connect: ' . mysql_error());
                            }

                            mysql_select_db(constant("DATABASE"), $con);

                            $query = "SELECT * FROM cmc_district WHERE id <= 6";

                            $comments = mysql_query($query, $con);
                            
                            
                            ?>   
                            <table class="table table-striped table-bordered">
                                <thead>
                                    <tr>
                                        <th> </th>
                                        <th> District</th>
                                        <th> MOH Title</th>
                                        <th> MOH Location (N,E)</th>
                                        <th> MOH Address</th>
                                        <th> MOH Telephone</th>
                                        <th> Doctor</th>
                                        <th> Doctor Telephone</th>
                                        <th> </th>
                                    </tr>
                                </thead>
                                <tbody>

                                    <?php
                                    
                                    $x = 0;
                                    while ($row = mysql_fetch_array($comments, MYSQL_ASSOC)) {
                                        $x = $x + 1;
                                        echo "<form id='editmohdetails$x' method='POST'>";
                                        echo "<input type='hidden' id='editid' name='editid' value='".$row['id']."'>";
                                        echo "<input type='hidden' id='editdistrict' name='editdistrict' value='".$row['district_name']."'>";
                                        echo "<input type='hidden' id='editloc' name='editloc' value='".$row['moh_location']."'>";
                                        echo "<input type='hidden' id='edittitle' name='edittitle' value='".$row['district_moh']."'>";
                                        echo "<input type='hidden' id='editaddress' name='editaddress' value='".$row['moh_address']."'>";
                                        echo "<input type='hidden' id='edittel' name='edittel' value='".$row['moh_telephone']."'>";
                                        echo "<input type='hidden' id='editdoctor' name='editdoctor' value='".$row['moh_doctor']."'>";
                                        echo "<input type='hidden' id='editdoctortel' name='editdoctortel' value='".$row['moh_doctor_telephone']."'>";
                                        echo "<tr>";
                                        echo "<td>" . $x . "</td>";
                                        echo "<td>" . $row['district_name'] . "</td>";
                                        echo "<td>" . $row['district_moh'] . "</td>";
                                        echo "<td>" . $row['moh_location'] . "</td>";
                                        echo "<td>" . $row['moh_address'] . "</td>";
                                        echo "<td>" . $row['moh_telephone'] . "</td>";
                                        echo "<td>" . $row['moh_doctor'] . "</td>";
                                        echo "<td>" . $row['moh_doctor_telephone'] . "</td>";
                                        echo "<td><div align='center'><input type='button' class='btn btn-small btn-success' value='&nbsp;&nbsp; Edit &nbsp;&nbsp;' onclick='submitedit($x)' /></div></td>";
                                        echo "</tr>";
                                        echo "</form>";
                                    }
                                    echo "</tbody></table>";
                                    mysql_close($con);
                                    ?>


                                    </div>
                                    <!-- /widget-content --> 

                                    </div>
                                    </div>
                                </div>
                                <!-- /widget -->

                            </div>
                            <!-- /span12 -->

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
            <script src="js/jquery-1.7.2.min.js"></script> 
            <script src="js/excanvas.min.js"></script> 
            <script src="js/bootstrap.js"></script>
            <script src="js/base.js"></script> 
            <script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/rollups/md5.js"></script>


            <script>

                function submitedit($x) {
                    $('#editmohdetails'+$x).submit();
                }

            </script><!-- /Calendar -->
            <script>
                $(document).ready(function() {

                    $('#submitmohbutton').click(function() {
                        $('#updatemohdetails').submit();
                    });
                    
                    var moh_id = document.getElementById('updatemoh').value;
                    
                    if(moh_id!=null && moh_id>0)
                    {
                        $('#moheditdetails').show();
                    }
                    else
                    {
                        $('#moheditdetails').hide();
                    }

                });
        </script>
    </body>
</html>
