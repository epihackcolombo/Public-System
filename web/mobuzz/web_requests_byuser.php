<?php
session_start();
date_default_timezone_set('UTC');
include 'Security.php';
$CI = new CI_Security();
?>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Request by users - Mo-Buzz Dengue Management Console</title>
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
                                            class="icon-user"></i>&nbsp;<strong>Hi&nbsp;&nbsp;</strong><?php echo $_SESSION['username'] ?></span><b class="caret"></b></a>
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
                        <li ><a href="web_requests.php"><i class="icon-share"></i><span>Complaints</span> </a> </li>
                        <li><a href="web_phi.php"><i class="icon-user-md"></i><span>PHI</span> </a></li>
                        <li><a href="web_map.php"><i class="icon-map-marker"></i><span>Map</span> </a> </li>
                        <li class="dropdown"><a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown"> <i class="icon-long-arrow-down"></i><span>Clusters</span> <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="web_map_public_show.php"><i class="icon-map-marker"></i><span>&nbsp;Generate clusters</span> </a></li>
                                <li><a href="web_map_public_show_data.php"><i class="icon-map-marker"></i><span>&nbsp;Cluster data</span> </a>   </li> 
                            </ul></li>
                        <li><a href="web_public_users.php"><i class="icon-group"></i><span>Public Users</span> </a> </li>
                        <li class="active"><a href="web_requests_byuser.php"><i class="icon-search"></i><span>Search</span> </a> </li>
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

                        <div class="span12">


                            <!-- /widget -->
                            <div class="widget">

                                <div class="widget-content">
                                    <h1>Search criteria</h1><br>
                                    <form action="web_requests_byuser.php" method="post" id="request_form">
                                        <div class="input-prepend input-group">


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
                                            <table border="0" width="100%">
                                                <tbody><tr><td width="40%"><h3>By Time Period</h3></td><td width="30%"><h3>By Username</h3></td><td width="30%"><h3>By Status</h3></td></tr>
                                                    <tr><td><span class="add-on input-group-addon"><i class="glyphicon glyphicon-calendar fa fa-calendar"></i></span><input type="text" style="width:82%" name="reservation" id="reservation" class="form-control" value="<?php echo $temp_date; ?>" />
                                                        </td>
                                            <?php
                                            if (isset($_REQUEST['name'])) {
                                                $v = $CI->xss_clean($_REQUEST['name']);
                                                ;
                                            } else {
                                                $v = '';
                                            }
                                            ?>
                                                        <td><input type="text" name="name" id="name"  value="<?php echo $v; ?>" >
                                                            <input type="submit" name="Search" class="btn btn-primary" id="Search" value="Submit"></td>

                                                        <td>       
                        <select name="select" id="select" onChange="selected_submit()">
                        <?php
                        
                        $query_select = (isset($_REQUEST['select']) ? $_REQUEST['select'] : null);

							if ($query_select == '') {
								echo ' <option value="">All</option>
										<option value="0">Not Verified</option>
										<option value="1">Accepted </option>
										<option value="101">Accepted (PHI)</option>
										<option value="-1">Rejected (Misuse)</option>
										';
							}
							if ($query_select == '1') {
								echo '  <option value="1">Accepted </option>
										<option value="101">Accepted (PHI)</option>
										<option value="-1">Rejected (Misuse)</option>
										<option value="">All</option>
										<option value="0">Not Verified</option>
										';
							}
							if ($query_select == '-1') {
								echo '  <option value="-1">Rejected (Misuse)</option>
										<option value="1">Accepted </option>
										<option value="101">Accepted (PHI)</option>
										<option value="0">Not Verified</option>
										<option value="">All</option>
										';
							}
							if ($query_select == '0') {
								echo '  <option value="0">Not Verified</option>
										<option value="-1">Rejected (Misuse)</option>
										<option value="1">Accepted</option>
										<option value="101">Accepted (PHI)</option>
										<option value="">All</option>
										';
							}
							if ($query_select == '101') {
								echo '  <option value="101">Accepted (PHI)</option>
										<option value="">All</option>
										<option value="0">Not Verified</option>
										<option value="-1">Rejected (Misuse)</option>
										<option value="1">Accepted</option>
										';
							}

							if (isset($_REQUEST['name'])) {
								$v = $CI->xss_clean($_REQUEST['name']);
								;
							} else {
								$v = '';
							}
                        ?>

                        </select>
                                                        </td></tr></tbody></table>


                                        </div>
                                        <input name="ss_start_date"  id="ss_start_date" type="hidden" value="<?php echo date("Y-m-d", strtotime($ss_start_date)); ?>" />
                                        <input name="ss_end_date"  id="ss_end_date" type="hidden" value="<?php echo date("Y-m-d", strtotime($ss_end_date)); ?>" />

                                    </form>
                                </div> <!-- /widget-content -->

                            </div> <!-- /widget -->
                        </div>
                        <!-- /span8-->

                    </div>
                    <!-- /row --> 

                    <div class="row">

                        <div class="span12">


						<!-- /widget -->
						<div class="widget">

						<div class="widget-content">
							<h1> Complaints </h1><br>

                        <?php
                        if (isset($_POST['ss_start_date']) && isset($_POST['ss_end_date'])) {

                            $start_date = new DateTime(date("Y-m-d", strtotime($_POST['ss_start_date'])));
                            $start_date3 = date("Y-m-d", strtotime($_POST['ss_start_date']));
                            $end_date = new DateTime(date("Y-m-d", strtotime($_POST['ss_end_date'])));
                            $end_date3 = date("Y-m-d", strtotime($_POST['ss_end_date']));

                        } else {

                            $start_date = new DateTime(date("Y-m-d", strtotime("-1 month")));
                            $end_date = new DateTime(date("Y-m-d", strtotime("today")));

                            $start_date3 = date("Y-m-d", strtotime("-1 month"));
                            $end_date3 = date("Y-m-d", strtotime("+24 hours"));
                        }

                        $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));

                        if (!$con) {
                            die('Could not connect: ' . mysql_error());
                        }

                        mysql_select_db(constant("DATABASE"), $con);
                        if (isset($_REQUEST['name'])) {
                            $v = $CI->xss_clean($_REQUEST['name']);
                            ;
                        } else {
                            $v = '';
                        }
                        $v = mysql_real_escape_string($v);
                        $query = "SELECT DISTINCT
                                    vw_public_users.username,
                                    vw_public_users.full_name
                                    FROM `vw_public_users`
                                    WHERE
                                    vw_public_users.username LIKE '%" . $v . "%' OR
                                    vw_public_users.full_name LIKE '%" . $v . "%'
                                    ORDER BY
                                    vw_public_users.username ASC";

                        $phiusers = mysql_query($query, $con);

                        $i = 0; //Current row
						$isDataShowing = 0; //To check the data availability
						
						//------------------------------------------------------
						
                        echo "<div class='accordion' id='accordion2'>"; //Start showing user details
							
							while ($row = mysql_fetch_array($phiusers, MYSQL_ASSOC)) 
							{
								//inner table start-------------------------------
								
								if (isset($_REQUEST['select'])) 
								{
									if ($_REQUEST['select'] == '') 
									{
										$where = '';
									} else {
										$where = "and  public_data.verified = " . $_REQUEST['select'] . "";
									}
								} else {
									$where = '';
								}         
								
								$query = "SELECT
											public_data.id,
											public_data.username,
											public_data.gps,
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
											cmc_ward.ward_name
										FROM `public_data`
										INNER JOIN cmc_ward ON public_data.ward = cmc_ward.ward_no
										WHERE public_data.username = '" . mysql_real_escape_string($row['username']) . "' " . $where . " and public_data.verified!='-2' AND public_data.date_time BETWEEN '" . $start_date3 . "' AND '" . $end_date3 . ' 23:59:59' . "'
										ORDER BY public_data.date_time DESC";

								$comments = mysql_query($query, $con);
								$rowCount = mysql_num_rows($comments);
								
								if ($rowCount > 0) {
								
									$isDataShowing = 1; // Some data is showing
									$i = $i + 1;
								
									echo "<div class='accordion-group'>
											<div class='accordion-heading'>
												<a class='accordion-toggle' data-toggle='collapse' data-parent='#accordion2' href='#collapse" . $i . "'> Username : " . $row['username'] .
													"<span style='text-align:right;color:blue;display: block;margin-top: -25px; font-weight:bold;'>Total : " . $rowCount . "<span id='total" . $rowCount . "'></span> 
													</span>
												</a>
											</div>";
								
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
														<th> Date TIME</th>
														<th> Ward</th>
														<th> Status</th>
														<th class='td-actions'>Action </th>
													  </tr>
													</thead>
													<tbody>
														<tr>";
														$t2 = 0;
														$check = true;
														while ($row = mysql_fetch_array($comments, MYSQL_ASSOC)) {

															$date = new DateTime(trim($row['date_time']));

															if ($rowCount != null) 
															{
																$t2 = $t2 + 1;
																echo "<tr>";
																	$check = false;

																	echo "<td>" . $t2 . "</td>";
																	//echo "<td>".$date->format('Y-m-d h:i A')."</td>";
																	echo "<td>" . $row['date_time'] . "</td>";

																	if ($row['ward'] == '0') {
																		$temp = 'Out of CMC Jurisdiction';
																	} else {
																		//$temp = $row['ward'];
																		$temp = $row['ward_name']." ( Ward No : ".$row['ward']." )";
																	}
																	echo "<td>" . $temp . "</td>";


																	if ($row['verified'] == '1') {
																		echo "<td>Accepted</td>";
																	}
																	if ($row['verified'] == '101') {
																		echo "<td>Accepted (PHI)</td>";
																	}
																	if ($row['verified'] == '0') {
																		echo "<td> Not Verified</td>";
																	}
																	if ($row['verified'] == '-1') {
																		echo "<td> Rejected </td>";
																	}

																	echo "<td class='td-actions'><a href='web_view.php?id=" . $row['id'] . "' class='btn btn-small btn-success'>Edit</a>
																			<input type='button' class='btn btn-danger btn-small' value='Delete' onclick='del(" . $row['id'] . ");' />
																		  </td>";
																echo "</tr>";
															}
														}

													echo '<input name="ward1" type="hidden" id="ward' . $i . '" value="' . $t2 . '">';
													if ($check) {
														echo "<td colspan=5>No data to display</td>";
													}
												echo "</tbody>";
												echo "</table> </div>";
											
											echo "</div>";
									echo "</div>";
								
								}
							}
							
							if($isDataShowing == 0) // no data to show
							{
								echo" <div class='accordion-group'>
									  <div class='accordion-heading'>
									  <a class='accordion-toggle' data-toggle='collapse' data-parent='#accordion2' > No data to show </div></div>";
							}		
							
						echo "</div></div>";
						
						//------------------------------------------------------
                        
                        echo '<input name="ward1" type="hidden" id="wardt" value="">';
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
