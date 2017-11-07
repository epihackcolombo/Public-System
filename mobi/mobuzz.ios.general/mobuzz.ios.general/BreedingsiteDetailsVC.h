//
//  BreedingsiteDetailsVC.h
//  mobuzz.ios.general
//
//  Created by Vajira on 3/9/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import <MapKit/MapKit.h>
#import <MapKit/MKAnnotation.h>
#import <AssetsLibrary/AssetsLibrary.h>
#import <ImageIO/ImageIO.h>
#import "ComplaintDescription.h"

@interface BreedingsiteDetailsVC : UIViewController <NSURLSessionDelegate>

//@property ComplaintDescription *complaintDescription;
@property NSDictionary *complaintDescription;

@end
