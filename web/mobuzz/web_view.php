<?php
session_start();
date_default_timezone_set('Asia/Calcutta');
?>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Request action- Dengue Breeding Site Complaining Application</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600" rel="stylesheet">
        <link href="css/font-awesome.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/pages/dashboard.css" rel="stylesheet">
        <link rel="stylesheet" href="http://blueimp.github.io/Gallery/css/blueimp-gallery.min.css">
        <link rel="stylesheet" href="css/bootstrap-image-gallery.min.css">
        <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
            <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
    </head>
    <style>
        #map {
            width:500px;
            height: 250px;
            margin: 0px;
            padding: 0px
        }
    </style>
    <body>
        <?php
        require("constantes.php");
        require("global_variables.php");

        if (!isset($_SESSION['admin'])) {
            header('Location: web_adminlogin.php');
        }

        ?>

        
        <?php
        
        $cmcvmsg1 = 'The CMC thanks you for your cooperation!';
        $cmcvmsg2 = 'This message confirms that CMC has investigated your complaint, and is grateful for your observation!';
        $cmcvmsg3 = 'Your breeding site complaint has been assigned to the following division: Drainage.';
        $cmcvmsg4 = 'Your breeding site complaint has been assigned to the following division: Solid Waste.';
        $cmcvmsg5 = 'Your breeding site complaint has been assigned to the following division: District Engineer.';
        $cmcvmsg6 = 'Thank you for your complaint. The CMC has checked the site you complained and found that problem has been rectified.';
        $cmcvmsg7 = 'Thank you for your complaint. The CMC has checked the site you complained and no problem was found.';
		$cmcvmsg8 = 'Thank you for your complaint. Unfortunately, we are unable to investigate it as it has been sent from a location outside CMC jurisdiction.'; //This is an acceptance but not done anything 
        $cmcvmsg9 ='The complainant was not reachable despite repeated attempts. The complainant has now been rejected.'; //Rejected.       
		$cmcvmsg10 = 'The information you sent has been found to be irrelevant. Please note that repeated complaining of irrelevant information will result in your account being blocked by the CMC.'; //Rejected. Misuse
        $cmcvmsg11 = 'Your account has been blocked.'; //Rejected. Misuse
        
        
        ?>
        
        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container"> <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"><span
                            class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span> </a><a class="brand" href="tcg_cpanel.php">Mo-Buzz Dengue Management Console</a>
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
                        <li><a href="web_tcg_cpanel.php"><i class="icon-dashboard"></i><span>Dashboard</span> </a> </li>
                        <li><a href="web_requests.php"><i class="icon-share"></i><span>Complaints</span> </a> </li>
                        <li ><a href="web_phi.php"><i class="icon-user-md"></i><span>PHI</span> </a></li>
                        <li><a href="web_map.php"><i class="icon-map-marker"></i><span>Map</span> </a> </li>
                        <li class="dropdown"><a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown"> <i class="icon-long-arrow-down"></i><span>Clusters</span> <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="web_map_public_show.php"><i class="icon-map-marker"></i><span>&nbsp;Generate clusters</span> </a></li>
                                <li><a href="web_map_public_show_data.php"><i class="icon-map-marker"></i><span>&nbsp;Cluster data</span> </a>   </li> 
                            </ul></li>
                        <li ><a href="web_public_users.php"><i class="icon-group"></i><span>Public Users</span> </a> </li>
                        <li><a href="web_requests_byuser.php"><i class="icon-search"></i><span>Search</span> </a> </li>
                        <li ><a href="web_gallery.php"><i class="icon-th-large"></i><span>Gallery</span> </a> </li>
                        <li><a href="web_download.php"><i class="icon-download-alt"></i><span>Downloads</span> </a> </li>
                        <li><a href="web_moh_details.php"><i class="icon-home"></i><span>MOH</span> </a> </li>
                        <li><a href="web_admin_users.php"><i class="icon-briefcase"></i><span>Administrator</span> </a> </li> 
                        <li> </li>
                    </ul>

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
                    <div class="widget widget-nopad">
                        <div class="widget-header"> <i class="icon-group"></i>
                            <h3>User complaint detail</h3>
                        </div>
                        <!-- /widget-header -->
                        <div class="widget-content">

                            <div style="padding-left:10px;">

                                <?php
                                if (isset($_REQUEST['dataid'])) {
                                    $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));
                                    if (!$con) {
                                        die('Could not connect: ' . mysql_error());
                                    }
                                    mysql_select_db(constant("DATABASE"), $con);

                                    $qu_count = "SELECT
                                                public_data.id as datatid,
                                                vw_public_users.user_status,
                                                vw_public_users.user_misuse_counter
                                                FROM
                                                public_data
                                                INNER JOIN vw_public_users ON vw_public_users.username = public_data.username
                                                WHERE public_data.id= " . mysql_real_escape_string($_REQUEST['dataid']);

                                    $result = mysql_query($qu_count, $con);
                                    $row = mysql_fetch_array($result, MYSQL_ASSOC);

                                    $cmcstatus;
                                    $userblock_sta;
                                    $mis_use_counter = $row['user_misuse_counter'];
                                    $querychanges1 = false; //Block user query
                                    $querychanges2 = false; //Misused query
                                    if (isset($_REQUEST['cmcmsg'])) {
                                        if ($_REQUEST['cmcmsg'] == $cmcvmsg1) //Just thank you, request is at new status
										{ 
                                            $cmcstatus = 0;
                                        } 
										else if ( ($_REQUEST['cmcmsg'] == $cmcvmsg2) || ($_REQUEST['cmcmsg'] == $cmcvmsg3) || ($_REQUEST['cmcmsg'] == $cmcvmsg4) || ($_REQUEST['cmcmsg'] == $cmcvmsg5) || ($_REQUEST['cmcmsg'] == $cmcvmsg6) || ($_REQUEST['cmcmsg'] == $cmcvmsg7) || ($_REQUEST['cmcmsg'] == $cmcvmsg8)) //Report verified
										{
                                            $cmcstatus = 1;
  										} 
										else if ($_REQUEST['cmcmsg'] == $cmcvmsg9) //Report rejected, but not misused and user is not blocked
										{
											$cmcstatus = -1;
										} 
										else if ($_REQUEST['cmcmsg'] == $cmcvmsg10) // Report rejected and misused (user may blocked)
										{
                                            $cmcstatus = -1;
                                            $mis_use_counter++;
                                            
                                            if($mis_use_counter >= MAXMISUSE)
                                            {
                                                $querychanges1 = true;
                                            }
                                            else 
                                            {
                                                $querychanges2 = true;
                                            }
                                        } 
										else if ($_REQUEST['cmcmsg'] == $cmcvmsg11) //Report rejected. User is blocked, misused
										{
                                            $mis_use_counter++;
                                            $cmcstatus = -1;
                                            $querychanges1 = true;
                                        }

                                        $dataid = mysql_real_escape_string($_REQUEST['dataid']);

                                        $dateupdate = new DateTime();
                                        $printdate = $dateupdate->format("Y-m-d H:i:s");

                                        if ($querychanges2) {
                                            $query = "UPDATE `public_data` pd  JOIN `public_users` pu  
                                                    ON pu.username = pd.username SET pd.`cmcmessage`='" . mysql_real_escape_string($_REQUEST['cmcmsg']) . "' , pd.`verified`='" . mysql_real_escape_string($cmcstatus) . "', pu.`user_misuse_counter`='" . mysql_real_escape_string($mis_use_counter) . "', pu.`user_status_changed_by`='".$_SESSION['username']."', pu.`user_status_changed_date`='".$printdate."', pd.`verified_by`='".$_SESSION['username']."', pd.`verified_date_time`='".$printdate."' WHERE (pd.`id`='" . mysql_real_escape_string($_REQUEST['dataid']) . "')";
                                        
                                        } else if ($querychanges1) {
                                            $query = "UPDATE `public_data` pd  JOIN `public_users` pu  
                                                    ON pu.username = pd.username SET pd.`cmcmessage`='" . mysql_real_escape_string($_REQUEST['cmcmsg']) . "' , pd.`verified`='" . mysql_real_escape_string($cmcstatus) . "' , pu.`user_status`=-1, pu.`user_misuse_counter`='" . mysql_real_escape_string($mis_use_counter) . "', pu.`user_status_changed_by`='".$_SESSION['username']."', pu.`user_status_changed_date`='".$printdate."', pd.`verified_by`='".$_SESSION['username']."', pd.`verified_date_time`='".$printdate."' WHERE (pd.`id`='" . mysql_real_escape_string($_REQUEST['dataid']) . "')";
                                        } else {
                                            $query = "UPDATE `public_data` pd SET pd.`cmcmessage`='" . mysql_real_escape_string($_REQUEST['cmcmsg']) . "', pd.`verified`='" . mysql_real_escape_string($cmcstatus) . "', pd.`verified_by`='".$_SESSION['username']."', pd.`verified_date_time`='".$printdate."'  WHERE (pd.`id`='" . mysql_real_escape_string($_REQUEST['dataid']) . "')";
                                        }


                                        mysql_query($query, $con);
                                    }

                                    if (isset($_REQUEST['update_ward'])) {
                                        $qu2 = "UPDATE `public_data` pd  JOIN `vw_public_users` pu  
                                                ON pu.username = pd.username SET pd.`ward`='" . mysql_real_escape_string($_REQUEST['update_ward']) . "' WHERE (pd.`id`='" . mysql_real_escape_string($_REQUEST['dataid']) . "')";

                                        mysql_query($qu2, $con);
                                    }
                                }


                                if (isset($_GET['id'])) {
                                    $id = $_GET['id'];

                                    $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));

                                    if (!$con) {
                                        die('Could not connect: ' . mysql_error());
                                    }

                                    mysql_select_db(constant("DATABASE"), $con);
                                    $id = mysql_real_escape_string($id);
                                    $id == preg_replace('/[^a-zA-Z0-9_ %\[\]\.\(\)%&-]/s', '', $id);

                                    $query = "SELECT
                                            public_data.id as datatid,
                                            public_data.username,
                                            public_data.gps,
                                            public_data.image,
                                            public_data.date_time,
                                            public_data.address,
                                            public_data.remarks,
                                            public_data.assessment,
                                            public_data.ward,
                                            public_data.cmcmessage,
                                            public_data.is_camera_photo,
                                            public_data.verified,
                                            public_data.verified_by,
                                            public_data.verified_date_time,
                                            public_data.report_ref_id,
                                            public_data.img_path,
                                            vw_public_users.username,
                                            vw_public_users.full_name,
                                            vw_public_users.id,
                                            vw_public_users.`password`,
                                            vw_public_users.email,
                                            vw_public_users.gender,
                                            vw_public_users.residence,
                                            vw_public_users.`language`,
                                            vw_public_users.contact_no,
                                            vw_public_users.registered_age,
                                            vw_public_users.registered_date_time,
                                            vw_public_users.registered_device_model,
                                            vw_public_users.sim_serial_no,
                                            vw_public_users.device_sim_no,
                                            vw_public_users.device_udid,
                                            vw_public_users.time_stamp,
                                            vw_public_users.user_status,
                                            vw_public_users.user_misuse_counter,
                                            vw_public_users.user_status_changed_by,
                                            vw_public_users.user_status_changed_date,
                                            vw_public_users.last_server_login
                                            FROM
                                            public_data
                                            INNER JOIN vw_public_users ON vw_public_users.username = public_data.username

                                             WHERE public_data.id= " . $id;
                                            //echo $query;
                                    
                                    $result = mysql_query($query, $con);

                                    $row = mysql_fetch_array($result, MYSQL_ASSOC);

                                    $date = date('Y-m-d h:i A', strtotime($row['date_time']));
                                    $name = $row['full_name'];

                                    if ($row['gender'] == '0') {
                                        $ti = "Mrs ";
                                    } else {
                                        $ti = "Mr ";
                                    }

                                    if ($name == "") {
                                        $name = "";
                                        ;
                                    }


                                    $image = $row['image'];
                                    $datatid = $row['datatid'];

                                    if ($image == "") {
                                        $image = "";
                                    }
                                    $email = $row['email'];
                                    if ($email == "") {
                                        $email = "";
                                    }

                                    //$GLOBALS['mis_use_counter'] = $row['user_misuse_counter'];

                                    $mobile = $row['contact_no'];
                                    $verified = $row['verified'];
                                    if ($mobile == "") {
                                        $mobile = "";
                                    }

                                    $discription = $row['remarks'];

                                    if ($discription == "") {
                                        $discription = "";
                                    }

                                    $ward = $row['ward'];

                                    if ($ward == "") {
                                        $ward = "";
                                        ;
                                    }
                                    if ($verified == "") {
                                        $verified = "";
                                        ;
                                    }
                                }
                                ?>
                                <?php 
                                    $gpsArray = explode(',', $row['gps']);
                                    $replocx = $gpsArray[0];
                                    $replocy = $gpsArray[1];
                                ?>

                                <br>
                                <div class="tabbable">
                                    <ul class="nav nav-tabs">
                                        <li>
                                            <a href="#user_detail" data-toggle="tab">User details</a>
                                        </li>
                                        <li class="active">
                                            <a href="#report_detail" data-toggle="tab">Complaint details</a>
                                        </li>


                                    </ul><br>

                                    <div class="tab-content">
                                        <div class="tab-pane" id="user_detail">
                                            <h3>
                                                UserName :
                                                <span style="color:#FF7F74;"><?php echo $row['username']; ?></span>
                                            </h3><br>
                                            <h3>
                                                Name :
                                                <span style="color:#FF7F74;"><?php echo $ti . $name; ?></span>
                                            </h3><br>

                                            <h3>Mobile :
                                                <span style="color:#FF7F74;"><?php echo $mobile; ?> </span></p></h3>
                                            <br>
                                            <h3>E mail :
                                                <span style="color:#FF7F74;"><?php echo $email; ?> </span></p></h3>
                                            <br>
                                        </div>
                                        <div class="tab-pane active" id="report_detail">
                                            <h3>Date : 
                                                <span style="color:#FF7F74;"><?php echo $row['date_time']; ?></span>
                                            </h3><br>
                                            <h3>
                                                Discription :
                                                <span style="color:#FF7F74;"><?php echo $discription; ?></span>
                                            </h3><br>
                                            <h3>Landmark : 
                                                <span style="color:#FF7F74;"><?php echo $row['address']; ?></span></h3><br> 

                                            <h3>Level of urgency : <span style="color:#FF7F74;"><?php echo $row['assessment']; ?></span></h3><br> 

                                            <?php
                                            $cmcmsg;
                                            if ($verified == 0) {
                                                $cmcmsg = $cmcvmsg1;
                                            } else if ($verified == 1) {
                                                $cmcmsg = $cmcvmsg2;
                                            } else {
                                                $cmcmsg = $cmcvmsg10;
                                            }
                                            ?>

                                            <h3> CMC Message : <span style="color:#FF7F74;"> <?php echo $row['cmcmessage'] ?> </span> <a class="btn  btn-warning" data-toggle="modal" role="button" href="#myModal">Change cmc message</a>

                                                <!-- Modal -->
                                                <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                                    <div class="modal-header">
                                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                                        <h3 id="myModalLabel">CMC Status messages</h3>
                                                    </div>
                                                    <div class="modal-body">
                                                        <form action="" method="post">

                                                            <input name="dataid" type="hidden" class="btn" value="<?php echo $datatid ?>">
								
                                                            <?php
															/*Note: Values of the radio buttons should as same as parametr values. Font color is overwritten by the framework.*/
                                                            echo '<input type="radio" name="cmcmsg" value="'.$cmcvmsg1.'" checked><font color=\"rgb(255,0,255)\">The CMC thanks you for your cooperation!</font><br><br>
                                                                    <input type="radio" name="cmcmsg" value="'.$cmcvmsg2.'"><font color=\"#088A08\">This message confirms that CMC has investigated your complaint, and is <b><u>grateful for your observation</u></b>!</font><br><br>
                                                                    <input type="radio" name="cmcmsg" value="'.$cmcvmsg3.'"><font color=\"#088A08\">Your breeding site complaint has been assigned to the following division: <b><u>Drainage</u></b>.</font><br><br>
                                                                    <input type="radio" name="cmcmsg" value="'.$cmcvmsg4.'"><font color=\"#088A08\">Your breeding site complaint has been assigned to the following division: <b><u>Solid Waste</u></b>.</font><br><br>
                                                                    <input type="radio" name="cmcmsg" value="'.$cmcvmsg5.'"><font color=\"#088A08\">Your breeding site complaint has been assigned to the following division: <b><u>District Engineer</u></b>.</font><br><br>
                                                                    <input type="radio" name="cmcmsg" value="'.$cmcvmsg6.'"><font color=\"#088A08\">Thank you for your complaint. The CMC has checked the site you complained and found that <b><u>problem has been rectified</u></b>.</font><br><br>
                                                                    <input type="radio" name="cmcmsg" value="'.$cmcvmsg7.'"><font color=\"#088A08\">Thank you for your complaint. The CMC has checked the site you complained and <b><u>no problem was found</u></b>.</font><br><br>
																	<input type="radio" name="cmcmsg" value="'.$cmcvmsg8.'"><font color=\"#088A08\">Thank you for your complaint. Unfortunately, we are unable to investigate it as <b><u>it has been sent from a location outside CMC jurisdiction</u></b>.</font><br><br>
																	<input type="radio" name="cmcmsg" value="'.$cmcvmsg9.'"><font color=\"#736F6E\">The complainant was not reachable despite repeated attempts. <b><u>The complainant has now been rejected</u></b>.</font><br><br>
                                                                    <input type="radio" name="cmcmsg" value="'.$cmcvmsg10.'"><font color=\"#736F6E\"><b><u>The information you sent has been found to be irrelevant</u></b>. Please note that repeated reporting of irrelevant information will result in your account being blocked by the CMC.</font><br><br>
                                                                    <input type="radio" name="cmcmsg" value="'.$cmcvmsg11.'"><font color=\"#736F6E\">Your account has been blocked.</font><br>';
                                                            ?>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                                                                <button class="btn btn-primary" type="submit">Save changes</button>
                                                        </form>
                                                    </div>
                                                </div>


                                            </h3>
                                            <br>   
                                            <h3>Ward : 
                                                <span style="color:#FF7F74;"><?php
                                                    if ($ward == '0') {
                                                        echo 'Out of CMC';
                                                    } else {
                                                        echo $ward;
                                                    }
                                                    ?></span> <a class="btn  btn-warning" data-toggle="modal" role="button" href="#myModal2">Change Current Ward</a>

                                                <!-- Modal -->
                                                <div id="myModal2" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                                    <div class="modal-header">
                                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                                        <h3 id="myModalLabel">Change current ward</h3>
                                                    </div>
                                                    <div class="modal-body">
                                                        <form action="" method="post">

                                                            <input name="dataid" type="hidden" class="btn" value="<?php echo $datatid ?>">

                                                            <table>
                                                                <tbody>
                                                                    <tr>
                                                                        <?php
                                                                        for ($i = 0; $i < 48; $i++) {


                                                                            echo '<td><input type="radio" name="update_ward" value="' . $i . '">';
                                                                            if ($i == 0) {
                                                                                echo 'Out of CMC &nbsp;</td>';
                                                                            } else {
                                                                                echo "Ward No $i &nbsp;</td>";
                                                                            }

                                                                            if ($i % 3 == 0) {
                                                                                echo '</tr><tr>';
                                                                            }
                                                                        }
                                                                        ?>
                                                            </table>
                                                            </tbody>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                                                        <button class="btn btn-primary" type="submit">Update changes</button>
                                                        </form>
                                                    </div></h3>
                                            <br>
                                            <h3>Current Status : <span style="color:#FF7F74;"><?php
                                                    if ($verified == '0') {
                                                        echo "Not verified";
                                                    } else if ($verified == '1') {
                                                        echo "Accepted";
                                                    } else if ($verified == '101') {
                                                        echo "Accepted";
                                                    } else if ($verified == '-1') {
                                                        echo "Rejected";
                                                    }
                                                    ?></span></h3><br> 

                                            <h3>Complained Location : </h3><div id="map"></div>
                                            <br>
                                            <h3>Image :</h3> 
                                            <?php
                                                if (empty($row['img_path'])) {

                                                } else {
                                            ?>
 
                                            <?php
                                                    $picpath = $row['img_path'];
                                                    echo '<img src="' . $picpath . '" style="max-width:850px; ">';
                                                }
                                            ?> 
                                            <br><br>
                                            
                                            <h3>Verified By : 
                                                <span style="color:#FF7F74;"><?php echo $row['verified_by']; ?></span></h3><br> 
                                            <h3>Verified Date : 
                                                <span style="color:#FF7F74;"><?php echo $row['verified_date_time']; ?></span></h3> 

                                        </div>

                                    </div>

                                    <br><br>

                                </div>    
                                    
                                    <a href="javascript:history.back();" style="font-size:14px; margin-left:0px;" ><button class="btn btn-primary">Back</button></a>
                                    <br><br>
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
<!--script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script-->
<script src="http://maps.googleapis.com/maps/api/js?sensor=false" type="text/javascript"></script>
<script type="text/javascript" src="js/gmap3.js"></script>
<script>

    $(function() {
        $('#map').gmap3({
            map: {
                options: {
                    center: [6.911640, 79.87],
                    zoom: 12
                }
            }
        });
        
        $("#map").gmap3({
            marker: {
                latLng: new google.maps.LatLng(<?php echo $replocx.",".$replocy ?>),
                options: {
                }
            }
        }, "autofit");
        
    });

</script>
<?php mysql_close($con); ?>
</body>
</html>
