<?php

define('OFFSET', 268435456);
define('RADIUS', 85445659.4471);
define('PI', 3.141592653589793238462);


class Cluster {

    private function lonToX($lon) {
        return round(OFFSET + RADIUS * $lon * PI / 180);
    }

    private function latToY($lat) {
        return round(OFFSET - RADIUS * log((1 + sin($lat * PI / 180)) / (1 - sin($lat * PI / 180))) / 2);
    }

    private function pixelDistance($lat1, $lon1, $lat2, $lon2, $zoom) {
        $x1 = $this->lonToX($lon1);
        $y1 = $this->latToY($lat1);

        $x2 = $this->lonToX($lon2);
        $y2 = $this->latToY($lat2);

        return sqrt(pow(($x1 - $x2), 2) + pow(($y1 - $y2), 2)) >> (21 - $zoom);
    }

    private function haversineDistance($lat1, $lon1, $lat2, $lon2) {
        $latd = deg2rad($lat2 - $lat1);
        $lond = deg2rad($lon2 - $lon1);
        $a = sin($latd / 2) * sin($latd / 2) +
                cos(deg2rad($lat1)) * cos(deg2rad($lat2)) *
                sin($lond / 2) * sin($lond / 2);
        $c = 2 * atan2(sqrt($a), sqrt(1 - $a));
        return (6371.0 * $c) * 1000;
    }

    private function newClusterPoint($x1, $x2, $y1, $y2, $movePercent) {

        $newPoint = array();
        $pixel = sqrt(pow(($x1 - $x2), 2) + pow(($y1 - $y2), 2));

        $cosin = ($x1 - $x2) / $pixel;
        $sinus = ($y1 - $y2) / $pixel;
        $distanceMovePixel = $pixel * $movePercent;
        $newXMove = $cosin * $distanceMovePixel;
        $newYMove = $sinus * $distanceMovePixel;

        $newPoint[0] = $x1 - $newXMove;
        $newPoint[1] = $y1 - $newYMove;

        return $newPoint;
    }

    /**
     * Create Clusters
     * @param $locationPoints
     * @param $distance - pixel distance
     * @param $zoom
     * @param $moreThen - minimum number of points for creating cluster
     * @return clustered
     */
    public function createCluster($locationPoints, $distance, $zoom, $moreThen, $difradius) {

        if ($moreThen > 0)
            $moreThen -= 1;
        if ($moreThen < 0)
            $moreThen = 0;

        $clustered = array();

        for ($i = 0; $i < count($locationPoints);) {

            $marker = array_shift($locationPoints);

            $cluster = 0;
            $clusterFinderIndex = array();
            $movePercent = 0.5;
            $radius = 0;

            $clusterPoint = $marker["location"];

            for ($j = 0; $j < count($locationPoints); $j++) {

                /*
                 * create by pixel for zoom
                 * 
                  $pixel = $this->pixelDistance(
                  $marker["location"][1], $marker["location"][0], $locationPoints[$j]["location"][1], $locationPoints[$j]["location"][0], $zoom
                  );
                 */

                $pixel = $this->haversineDistance(
                        $marker["location"][1], $marker["location"][0], $locationPoints[$j]["location"][1], $locationPoints[$j]["location"][0]
                );


                if ($distance > $pixel) {
                    $cluster ++;
                    $clusterFinderIndex[] = $j;

                    /*
                     * create by pixel for zoom
                     * 
                      $clusterPoint = $this->newClusterPoint(
                      $clusterPoint[0], $locationPoints[$j]["location"][0], $clusterPoint[1], $locationPoints[$j]["location"][1], $movePercent
                      );

                      $movePercent -= ($movePercent * 0.03);
                     */
                }
            }

            if ($cluster > $moreThen) {

                $clusterpoints = array();
                $clusterpoints[] = $marker;

                //create centroid and radius for the cluster
                for ($k = 0; $k < count($clusterFinderIndex); $k++) {
                    $clusterpoints[] = $locationPoints[$clusterFinderIndex[$k]];
                    unset($locationPoints[$clusterFinderIndex[$k]]);
                }

                $centrex = 0;
                $centrey = 0;
                $minradius = 0;

                if ($clusterpoints) {

                    //centroid of points
                    for ($n = 0; $n < count($clusterpoints); $n++) {
                        $centrex += $clusterpoints[$n]["location"][0];
                        $centrey += $clusterpoints[$n]["location"][1];
                    }

                    $centrex /= count($clusterpoints);
                    $centrey /= count($clusterpoints);

                    for ($n = 0; $n < count($clusterpoints); $n++) {

                        $pixelrad = $this->haversineDistance(
                                $centrey, $centrex, $clusterpoints[$n]["location"][1], $clusterpoints[$n]["location"][0]
                        );

                        if ($minradius < $pixelrad) {
                            $minradius = $pixelrad;
                        }
                    }
                }

                $clusterData = array();

                $clusterData["count"] = $cluster + 1;
                //$clusterData["coordinate"] = $clusterPoint;
                $clusterData["coordinate"][0] = $centrex;
                $clusterData["coordinate"][1] = $centrey;
                $clusterData["radius"] = round($minradius, 0, PHP_ROUND_HALF_UP) + $difradius;

                $clustered[] = $clusterData;
            } else {

                //$clustered[] = $marker; //marker is included as one count cluster
                $clusterData = array();
                $clusterData["count"] = 0;
                $clusterData["coordinate"][0] = $marker["location"][0];
                $clusterData["coordinate"][1] = $marker["location"][1];
                $clusterData["radius"] = $difradius;

                $clustered[] = $clusterData;
            }
        }

        return $clustered;
    }

}

?>