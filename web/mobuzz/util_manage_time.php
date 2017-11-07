<?php

function getTimeStatus($year, $week) {
    date_default_timezone_set('Asia/Kolkata');
    $firstWeekThursDay = date('W', strtotime("January $year first thursday", date(time())));

    $time = 0;

    if ($firstWeekThursDay == "01") {
        $time = strtotime("January $year first thursday", date(time()));
        $time = ($time - (5 * 24 * 3600)) + (((7 * $week) - 6) * 24 * 3600);
    } else {
        $time = strtotime("January 1 $year", time());
        $time = ($time - (5 * 24 * 3600)) + (((7 * $week) - 6) * 24 * 3600);
    }

    $time_wk_sun = $time;
    $time_wk_sat = $time + 6*24*3600;
    $time_4wkb_sun = $time - 4 * 7 * 24 * 3600;
    $time_4wkb_mon = $time - 4 * 7 * 24 * 3600 + 24 * 3600;

    //$return[0] = date('Y-M-d', $time_wk_sun);// this sunday
    $return[0] = date('Y-M-d', $time_wk_sat);// this saturday
    $return[1] = date('Y-M-d', $time_4wkb_sun); // sunday 4 weeks before to this week
    $return[2] = date("YW", strtotime(date('Y-m-d', $time_4wkb_mon)));
    $return[3] = $year.sprintf("%02d", $week);
    $return[4] = date("Y", strtotime(date('Y-m-d', $time_4wkb_mon))); // year  of, sunday 4 weeks before to this week
    $return[5] = date("W", strtotime(date('Y-m-d', $time_4wkb_mon))); // week-of-year of, sunday 4 weeks before to this week

    return $return;
}


function nowTimestamp()
{
    date_default_timezone_set('Asia/Kolkata');
    $now = new DateTime();
    return $now->getTimestamp();
}

?>