<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Google Map</title>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Google Map</title>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript">
	

function livesearch()
{

    
    var xmlhttp;
    if (window.XMLHttpRequest)
    {
        xmlhttp=new XMLHttpRequest();
    }
    else
    {
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function()
        {
///////////////////////////////////////////
            if (xmlhttp.readyState==4 && xmlhttp.status==200)
            {
                //window.alert(xmlhttp.responseText);
             
                                  //center point
                                         var latlngcenter = new google.maps.LatLng(6.92132,79.86065);
					  var settings = {
						zoom: 13,
						center:latlngcenter,
						mapTypeControl: true,
						mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU},
						navigationControl: true,
						navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
						mapTypeId: google.maps.MapTypeId.ROADMAP
				};
				var map = new google.maps.Map(document.getElementById("map_canvas"), settings);

  var result = xmlhttp.responseText.split(":");
   for (var i = 0; i < result.length; i++)
   {
	//alert(result[i])
       var s=result[i];
       var stringToArray = new Array;
    stringToArray = s.split(",");
       var lati=stringToArray[0];
        var long=stringToArray[1];
      // alert(lati); 
       // alert(long);

                         $lat=lati;
		           $lng=long;
           
				
				var companyLogo = new google.maps.MarkerImage('image/logo.png',
				new google.maps.Size(100,50),
				new google.maps.Point(0,0),
				new google.maps.Point(50,50)
			);
			var companyShadow = new google.maps.MarkerImage('image/logosado.png',
				new google.maps.Size(130,50),
				new google.maps.Point(0,0),
				new google.maps.Point(65, 50)
			);



			var companyPos = new google.maps.LatLng( $lat,$lng);
			var companyMarker = new google.maps.Marker({
				position: companyPos,
				map: map,
				icon: companyLogo,
				 shadow: companyShadow,
				title:"DENGU",
				zIndex: 4
			
			});
			var contentString = '<div id="content">'+
				'<div id="siteNotice">'+
				'</div>'+
				'<h1 id="firstHeading" class="firstHeading">DENGU</h1>'+
				'<div id="bodyContent">'+
				'<p>Dengu is very denger. becuase be care full</p>'+
				'</div>'+
				'</div>';
			 
			var infowindow = new google.maps.InfoWindow({
				content: contentString
			});	
				
				
				
			google.maps.event.addListener(companyMarker, 'click', function() {
			  infowindow.open(map,companyMarker);
			});	
}
            }
/////////////////////////////////////////////
        }
    xmlhttp.open("GET","livemap.php",true);
    xmlhttp.send();
}	 
	 
	
	
	
</script>
</head>
<body >
  <div id="map_canvas" style="width:100%; height:640px"></div>

 
<script>
window.onload = livesearch;
</script>

</body>
</html>