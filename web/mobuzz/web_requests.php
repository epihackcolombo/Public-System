<?php
session_start();
date_default_timezone_set('UTC');
?>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Request - Mo-Buzz Dengue Management Console</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600"
              rel="stylesheet">
        <link href="css/font-awesome.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/pages/dashboard.css" rel="stylesheet">
        <link href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" rel="stylesheet">
        <link rel="stylesheet" href="datepicker/daterangepicker-bs3.css" />
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

        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container"> <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"><span
                            class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span> </a><a class="brand" href="tcg_cpanel.php">Mo-Buzz Dengue Management Console</a>
                    <div class="nav-collapse">
                        <ul class="nav pull-right">

                            <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><span style="color:#333;font-size:14px;"><i
                                            class="icon-user"></i>&nbsp;<strong>Hi&nbsp;&nbsp;</strong><?php echo $_SESSION['username'] ?></span> <b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="web_adminlogin.php?logout=logout">Logout</a></li>
                                </ul>
                            </li>
                        </ul>
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
                        <li class="active"><a href="web_requests.php"><i class="icon-share"></i><span>Complaints</span> </a> </li>
                        <li><a href="web_phi.php"><i class="icon-user-md"></i><span>PHI</span> </a></li>
                        <li><a href="web_map.php"><i class="icon-map-marker"></i><span>Map</span> </a> </li>
                        <li class="dropdown"><a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown"> <i class="icon-long-arrow-down"></i><span>Clusters</span> <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="web_map_public_show.php"><i class="icon-map-marker"></i><span>&nbsp;Clusters spot map</span> </a></li>
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

                            <!-- /widget -->
                            <div class="widget">

                                <div class="widget-content">
                                    <h1>Pick a Time Range</h1><br>
                                    <form action="web_requests.php" method="post" id="request_form">
                                        <div class="input-prepend input-group">
                                            <span class="add-on input-group-addon"><i class="glyphicon glyphicon-calendar fa fa-calendar"></i></span>

                                            <?php
                                            if (isset($_POST['ss_start_date'])) {
                                                $ss_start_date = date("m/d/Y", strtotime($_POST['ss_start_date']));
                                            } else {
                                                $ss_start_date = date("m/d/Y", strtotime("-1 month"));
                                            }
                                            if (isset($_POST['ss_end_date'])) {
                                                $ss_end_date = date("m/d/Y", strtotime($_POST['ss_end_date']));
                                            } else {
                                                $ss_end_date = date("m/d/Y", strtotime("today"));
                                            }

                                            $temp_date = date("F j, Y", strtotime($ss_start_date)) . ' - ' . date("F j, Y", strtotime($ss_end_date));
                                            ?>     

                                            <input type="text" style="width:50%" name="reservation" id="reservation" class="form-control" value="<?php echo $temp_date; ?>" />
                                            
                                            <select name="select" id="select" onChange="selected_submit()">
                                                <?php
                                                if ($_REQUEST['select'] == '') {
                                                    echo ' <option value="">All</option>
                                                            <option value="0">Not Verified</option>
                                                            <option value="1">Accepted </option>
															<option value="101">Accepted (PHI)</option>
                                                            <option value="-1">Rejected (Misuse)</option>
                                                            ';
                                                }
                                                if ($_REQUEST['select'] == '0') {
                                                    echo '  <option value="0">Not Verified</option>
                                                            <option value="1">Accepted</option>
															<option value="101">Accepted (PHI)</option>
															<option value="-1">Rejected (Misuse)</option>
                                                            <option value="">All</option>
                                                            ';												
												}
                                                if ($_REQUEST['select'] == '1') {
                                                    echo '  <option value="1">Accepted </option>
															<option value="101">Accepted (PHI)</option>
                                                            <option value="-1">Rejected (Misuse)</option>
                                                            <option value="">All</option>
                                                            <option value="0">Not Verified</option>
                                                            ';
                                                }
												if ($_REQUEST['select'] == '101') {
													echo ' <option value="101">Accepted (PHI)</option>
															<option value="-1">Rejected (Misuse)</option>
															<option value="">All</option>
															<option value="0">Not Verified</option>
															<option value="1">Accepted</option>
															';
												}																   			   
                                                if ($_REQUEST['select'] == '-1') {
                                                    echo '  <option value="-1">Rejected (Misuse)</option>
															<option value="">All</option>
															<option value="0">Not Verified</option>
                                                            <option value="1">Accepted </option>
															<option value="101">Accepted (PHI)</option>
                                                            ';
                                                }
                                                ?>

                                            </select>
                                        </div>
                                        <input name="ss_start_date"  id="ss_start_date" type="hidden" value="<?php echo date("Y-m-d", strtotime($ss_start_date)); ?>" />
                                        <input name="ss_end_date"  id="ss_end_date" type="hidden" value="<?php echo date("Y-m-d", strtotime($ss_end_date)); ?>" />

                                    </form>
                                </div> <!-- /widget-content -->

                            </div> <!-- /widget -->
                        </div>
                        <!-- /span8-->

                        <div class="span4">

                            <!-- /widget -->
                            <div class="widget">

                                <div class="widget-content"> 

                                    <div class="stats-box-all-info">

                                        <?php echo "<h2><i class='icon-time' style='color:#3C3 ;'></i>    Today is  " . date("Y-m-d") . "</h2>"; ?></div>
                                </div>
                            </div>
                        </div> <!-- /span8-->
                    </div><!-- /row --> 

                    <div class="row">

                        <div class="span12">


                            <!-- /widget -->
                            <div class="widget">

                                <div class="widget-content">
                                    <h1> Complaints </h1><br>

                                    <?php
                                    if (isset($_POST['ss_start_date']) && isset($_POST['ss_end_date'])) {

                                        $start_date = new DateTime(date("Y-m-d", strtotime($_POST['ss_start_date'])));
                                        $end_date = new DateTime(date("Y-m-d", strtotime($_POST['ss_end_date'])));

                                    } else {

                                        $start_date = new DateTime(date("Y-m-d", strtotime("-1 month")));
                                        $end_date = new DateTime(date("Y-m-d", strtotime("today")));
                                    }

                                    $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));

                                    if (!$con) {
                                        die('Could not connect: ' . mysql_error());
                                    }
                                    
                                    $end_date->add(new DateInterval('PT23H59M59S'));

                                    mysql_select_db(constant("DATABASE"), $con);
                                    
                                    $querywards ="SELECT dis.district_id, dis.district_name, ward.ward_no, ward.ward_name 
                                                 FROM mobuzz_db.cmc_district dis 
                                                 INNER JOIN mobuzz_db.cmc_ward ward 
                                                 ON dis.district_id = ward.district_id
                                                 ORDER BY dis.district_id, ward.ward_no";
                                    
                                    $rowsward = mysql_query($querywards, $con);
                                    $rowCountWard = mysql_num_rows($rowsward);
									
									$isDataShowing = 0; // to check the data availability
									
                                    if ($rowCountWard > 0) {

                                        echo "<div class='accordion' id='accordion2'>";
                                        
                                        while ($rowward = mysql_fetch_array($rowsward, MYSQL_ASSOC)) {
                                            
                                            $i=$rowward['ward_no']; //get the word number
                                            
											//inner table start-------------------------------
											
											if (isset($_REQUEST['select'])) {

                                                if ($_REQUEST['select'] == '') {
                                                    $where = "public_data.verified > -2";
                                                } else {
                                                    $where = "public_data.verified = " . $_REQUEST['select'] . "";
                                                }

                                            } else {
                                                $where = 'public_data.verified > -2';
                                            }
											
											$query = "SELECT
                                                    public_data.id,
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
                                                    vw_public_users.username,
                                                    vw_public_users.full_name
                                                    FROM
                                                    public_data
                                                    INNER JOIN vw_public_users ON vw_public_users.username = public_data.username 
                                                    WHERE  (ward = $i)  and ($where)  and (public_data.date_time BETWEEN '" . date_format($start_date, 'Y-m-d 00:00:00') . "' AND '" . date_format($end_date, 'Y-m-d 23:59:59') . "') ORDER BY public_data.date_time DESC"; 


                                            $comments = mysql_query($query, $con);
                                            $rowCount = mysql_num_rows($comments);

											
											if($rowCount>0)
											{
												$isDataShowing = 1; // Some data is showing
											
												echo" <div class='accordion-group'>
													  <div class='accordion-heading'>";

												if($i<1)
												{
													echo "<a class='accordion-toggle' data-toggle='collapse' data-parent='#accordion2' href='#collapse" . $i . "'> Out of CMC Jurisdiction"; 
												}
												else
												{
													echo "<a class='accordion-toggle' data-toggle='collapse' data-parent='#accordion2' href='#collapse" . $i . "'> District ".$rowward['district_name']." &nbsp;-&nbsp; " .$rowward['ward_name']." ( Ward No: ". $i ." )";
												}
												
												//----------------
												
												echo "<span style='text-align:right;color:blue;display: block;margin-top: -25px; font-weight:bold;'>Total : <span id='total" . $i . "'>$rowCount</span> </span></a></div>";

												echo "<div id='collapse" . $i . "' ";
												if ($i == 1) {
													echo "class='accordion-body collapse in' >";
												} else {
													echo "class='accordion-body collapse ' >";
												}

												echo "
													   <div class='accordion-inner'>
														<table class='table table-striped table-bordered'>
														<thead>
														  <tr>
															<th> </th>
															<th> Date Time</th>
															<th> Username</th>
															<th> Full Name</th>
															<th> Status</th>
															<th class='td-actions'>Action </th>
														  </tr>
														</thead>
														<tbody>
														  <tr>";
												
												$t2 = 0;
												$check = true;
												
												while ($row = mysql_fetch_array($comments, MYSQL_ASSOC)) {
													
													$t2++;
													echo "<tr>";
														$check = false;

														echo "<td>" . $t2 . "</td>";
														echo "<td>" . $row['date_time'] . "</td>";
														echo "<td>" . $row['username'] . "</td>";
														echo "<td>" . $row['full_name'] . "</td>";

														if ($row['verified'] == '1') {
															echo "<td> Accepted </td>";
														}
														if ($row['verified'] == '0') {
															echo "<td> Not Verified </td>";
														}
														if ($row['verified'] == '-1') {
															echo "<td> Rejected </td>";
														}
														if ($row['verified'] == '101') {
															echo "<td> Accepted (PHI) </td>";
														}

														echo "<td class='td-actions'><a href='web_view.php?id=" . $row['id'] . "' class='btn btn-small btn-success'>Edit</a>
															  <input type='button' class='btn btn-danger btn-small' value='Delete' onclick='del(" . $row['id'] . ");' />
															  </td>";
													echo "</tr>";
	  
												}

												echo '<input name="ward1" type="hidden" id="ward' . $i . '" value="' . $t2 . '">';
												
												if ($check) {
													echo "<td colspan=6>No data to display</td>";
												}
												echo "</tbody>";
												echo "</table> </div></div></div>";

											}
											else
											{
												//hide the inner table
											}

											//inner table end-------------------------------
                                        }

										if($isDataShowing == 0) // no data to show
										{
											echo" <div class='accordion-group'>
												  <div class='accordion-heading'>
												  <a class='accordion-toggle' data-toggle='collapse' data-parent='#accordion2' > No data to show </div></div>";
										}										
									
                                        echo "</div>";
                                    }
   
                                    mysql_close($con);
                                    ?>

                                </div> <!-- /widget-content -->

                            </div> <!-- /widget -->
                        </div>
                        <!-- /span6 --> 
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
                        <div class="span12"><span style="text-align:center"> &copy; 2014 All Rights Reserved.</span> </div>
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
            #request_form .input-prepend .add-on, .input-prepend .btn { margin-top: -9px;}
            .stats-box-all-info i {
                font-size:40px;
            }
        </style>
        <script src="js/jquery-1.7.2.min.js"></script> 
        <script src="js/excanvas.min.js"></script> 
        <script src="js/bootstrap.js"></script>

        <script type="text/javascript" src="datepicker/moment.js"></script>
        <script type="text/javascript" src="datepicker/daterangepicker.js"></script>


        <script src="js/base.js"></script> 
        <script>
                $(document).ready(function() {

                    $('#reservation').daterangepicker({format: 'MMMM D ,YYYY'}, function(start, end, label) {

                        $('#ss_end_date').val(end.format('YYYY-MM-DD'));
                        $('#ss_start_date').val(start.format('YYYY-MM-DD'));
                        document.getElementById('request_form').submit();
                    });

                });

                function selected_submit() {

                    $('#ss_end_date').val();
                    $('#ss_start_date').val();

                    document.getElementById('request_form').submit();
                }

                function del(id) {

                    var r = confirm("Are you sure that you want to permanently delete  this?");
                    if (r == true) {
                        $.post("delete_request.php", {id: id}).done(function(data) {

                            if (data = '1111')
                            {
                                location.reload();
                                //  window.location = window.location.href;
                            }
                            else
                            {
                                alert("Access denied or Database connection failure");
                            }

                        });

                    }

                }
        </script><!-- date picker -->
    </body>
</html>
