<?php

require_once 'global_variables.php';

function write_image($user, $img_data) {

    //$MAX_THUMB_DIMENSION = 250;
    //$THUMB_EXT = "_thumb.jpg";
    
    try {
        
        $user = str_replace("/", "-", $user);

        date_default_timezone_set('Asia/Kolkata');

        $mt = microtime(true);
        $micro = sprintf("%06d", ($mt - floor($mt)) * 1000000);
        $date = new DateTime(date('Y-m-d H:i:s.' . $micro, $mt));
        $printdate = $date->format("_Ymd_His_u");

        $file_name = $user . $printdate . ".jpg";
        $dir_name = "./public_images/";
        $full_name = $dir_name . $file_name;

        while (file_exists($full_name)) {
            $date = new DateTime(date('Y-m-d H:i:s.' . $micro, $mt));
            $printdate = $date->format("_Ymd_His_u");
            $file_name = $user . $printdate . ".jpg";

            $full_name = $dir_name . $file_name;
        }

        $decoded_data = base64_decode($img_data);

        if (file_put_contents($full_name, $decoded_data) > 0) {
            
            // create a thumbnail image ------------------         
            $MaxWe=MAX_THUMB_DIMENSION;
            $MaxHe=MAX_THUMB_DIMENSION;
            $arr_image_details = getimagesize($full_name); 
            $width = $arr_image_details[0];
            $height = $arr_image_details[1];

            $percent = 100;
            if($width > $MaxWe){
                $percent = floor(($MaxWe * 100) / $width);
            }

            if(floor(($height * $percent)/100)>$MaxHe){  
                $percent = (($MaxHe * 100) / $height);
            }

            if($width > $height) {
                $newWidth=$MaxWe;
                $newHeight=round(($height*$percent)/100);
            }else{
                $newWidth=round(($width*$percent)/100);
                $newHeight=$MaxHe;
            }

            if ($arr_image_details[2] == 1) {
                $imgt = "ImageGIF";
                $imgcreatefrom = "ImageCreateFromGIF";
            }
            if ($arr_image_details[2] == 2) {
                $imgt = "ImageJPEG";
                $imgcreatefrom = "ImageCreateFromJPEG";
            }
            if ($arr_image_details[2] == 3) {
                $imgt = "ImagePNG";
                $imgcreatefrom = "ImageCreateFromPNG";
            }

            if ($imgt) {
                $old_image = $imgcreatefrom($full_name);
                $new_image = imagecreatetruecolor($newWidth, $newHeight);
                imagecopyresized($new_image, $old_image, 0, 0, 0, 0, $newWidth, $newHeight, $width, $height);

               $imgt($new_image, $full_name.THUMB_EXT);
            }

            // -------------------------------------------

            return $full_name;
        } else {
            $tmpfname = tempnam($dir_name, $file_name); //To avoid permission and other errors.

            if (file_put_contents($tmpfname, $decoded_data) > 0) {
                return $tmpfname;
            } else {
                return null;
            }
        }
    } catch (Exception $e) {

        return null;
    }
}

function read_image($img_path) {

    try {

        if (file_exists($img_path)) {

            $stringdata = file_get_contents($img_path);
            
            if (strlen($stringdata)>0) {
                
                return base64_encode($stringdata);
            } else {
                
                return "";
            }
        } else {

            return "";
        }
        
    } catch (Exception $e) {
        
        return "";
    }
}

function read_image_thumbnail($img_path) {

    //$THUMB_EXT = "_thumb.jpg";
    
    try {

        if (file_exists($img_path.THUMB_EXT)) {

            $stringdata = file_get_contents($img_path.THUMB_EXT);
            
            if (strlen($stringdata)>0) {
                
                return base64_encode($stringdata);
            } else {
                
                return "";
            }
        } else {

            return "";
        }
        
    } catch (Exception $e) {
        
        return "";
    }
}


?>