<?php
session_start();
date_default_timezone_set('UTC');
if (!isset($_SESSION['admin'])) {
    header('Location: web_adminlogin.php');
}
?>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>DengueHotspots - Dengue Breeding Site Reporting Application</title>

        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/bootstrap-responsive.min.css">
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600">
        <link rel="stylesheet" href="css/font-awesome.css">
        <link rel="stylesheet" href="css/style.css">

        <link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/icon.css">
        <!--link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/demo/demo.css"-->

        <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script> 
        <script src="js/excanvas.min.js"></script> 
        <script src="js/bootstrap.js"></script>
        <script src="js/base.js"></script> 

        <script type="text/javascript" src="http://code.jquery.com/jquery-1.6.min.js"></script>
        <script type="text/javascript" src="http://www.jeasyui.com/easyui/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="http://www.jeasyui.com/easyui/jquery.edatagrid.js"></script>

        <script type="text/javascript">
            $(function() {
                $('#dg').edatagrid({
                    url: 'map_public_show_data_get.php',
                    saveUrl: 'map_public_show_data_save.php',
                    updateUrl: 'map_public_show_data_update.php',
                    destroyUrl: 'map_public_show_data_delete.php'
                });
            });
        </script>  

        <style>

            body{
                text-align:left;
            }

            label input{ display:inline !important; margin-right:8px !important;}

        </style>
    </head>

    <body>
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
                        <li class="dropdown" ><a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown"> <i class="icon-long-arrow-down"></i><span>Clusters</span> <b class="caret"></b></a>
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

                </div> <!-- /controls -->	
            </div> <!-- /control-group -->

        </div>
        <!-- /subnavbar -->
        <div class="main">
            <div class="main-inner">
                <div class="container">
                    <div class="row">
                        <div class="span12">
                            <div class="widget widget-nopad">
                                <div class="widget-header"> <i class="icon-th-list"></i>
                                    <h3>Dengue Incidences' Location Data</h3>
                                </div>
                                <div class="widget-content" style="padding: 1px;">

                                    <table id="dg" style="width: 100%; height: 600px;" 
                                           border="0"
                                           toolbar="#toolbar" 
                                           pagination="false" 
                                           idField="id"
                                           rownumbers="false" 
                                           fitColumns="true" 
                                           singleSelect="true">
                                        <thead>
                                            <tr>
                                                <th field="id" width="20" editor="text">DB Item</th>
                                                <th field="year" width="50" editor="text">Year</th>
                                                <th field="week_of_year" width="50" editor="text">Week Of Year</th>
                                                <th field="location_lat" width="50" editor="text">Latitude (North)</th>
                                                <th field="location_lng" width="50" editor="text">Longitude (East)</th>
                                                <th field="ref_id" width="50" editor="text">Reference ID</th>
                                            </tr>
                                        </thead>
                                    </table>
                                    <div id="toolbar">
                                        <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="javascript:$('#dg').edatagrid('addRow')">New</a>
                                        <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="javascript:$('#dg').edatagrid('destroyRow')">Delete</a>
                                        <a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="javascript:$('#dg').edatagrid('saveRow')">Save</a>
                                        <a href="#" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="javascript:$('#dg').edatagrid('cancelRow')">Cancel</a>
                                    </div>
                                </div>
                                <!-- /widget-content --> 
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

    </body>

</html>