//
//  MapVC.m
//  mobuzz.ios.general
//
//  Created by Vajira on 25/9/15.
//  Copyright © 2015 cosmic. All rights reserved.
//

#import "MapVC.h"
#import "GlobalVariableAndMethod.h"
#import "PopupMessages.h"
#import "CMCRegions.h"
#import "ProfileLoginVC.h"

@interface MapVC ()

@property (strong, nonatomic) IBOutlet UILabel *lablFromDate;
@property (strong, nonatomic) IBOutlet UILabel *lablToDate;

@property (strong, nonatomic) IBOutlet MKMapView *mapView;

@property (strong, nonatomic) IBOutlet UIButton *butnBack;

@property NSString *hexColorCode;
@property NSString *markerId, *markerLocation;
@property NSArray *subArrayMohData;

@end


//Show geospatial data in apple-map
@implementation MapVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.butnBack.layer.borderWidth = 1.0f;
    self.butnBack.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"ash"]CGColor];
    
    [self checkInternetConnectionAndMakeLoadRequest];

    self.hexColorCode = @"#7FF5A9A9";//Set default value
}

- (IBAction)backPressed:(UIButton *)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}


#pragma mark - "getting MOH details and clusters"

-(void)checkInternetConnectionAndMakeLoadRequest {
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    //Check the internet connection
    NSURLSession *connTestSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:self delegateQueue:[NSOperationQueue mainQueue] ];
    NSURLSessionDataTask * sessionDataTask = [connTestSession dataTaskWithURL:[NSURL URLWithString:@kCONN_URL_CHECK] completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        
        //Check connection is available or not
        if ((error == nil) && data) {
            
            //Create the request.
            [self callAsynPostComplaintHistory: [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", @kCONN_URL_BASE, @kCONN_URL_MAP] ]];
        }
        else {
            
            //Popup message
            [self popdNetworkFailed:@kMSG_INTERNET_NO_TITLE1 message:@kMSG_INTERNET_NO_PARA1 button:@kMSG_COMMON_OK];
        }
        
    }];
    [sessionDataTask resume];
    
}


//Make a post call to server to login
-(void)callAsynPostComplaintHistory: (NSURL *)url {
    
    
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSString *username = [userDefaults stringForKey:@kLOGGED_USER_NAME];
    NSString *timeStamp = [userDefaults stringForKey:@kLOGGED_USER_TOKEN];
    NSString *uudid = [userDefaults stringForKey:@kLOGGED_USER_UUDID];
    
    NSError *errorJson;
    
    //Creating the json
    NSDictionary *jsonDictionary = [NSDictionary dictionaryWithObjectsAndKeys:
                                    
                                    username, @"user",
                                    timeStamp, @"time_stamp",
                                    uudid, @"uudid",

                                    nil];
    
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:jsonDictionary options:NSJSONWritingPrettyPrinted error:&errorJson];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];

    
    if (!errorJson) {
        NSData *postData=[jsonString dataUsingEncoding:NSUTF8StringEncoding];
        [self doProcessNetworkMohDetailsRequest:url data:postData];
    }
}

-(void)doProcessNetworkMohDetailsRequest:(NSURL *)url data:(NSData *)postData {
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
    [request setHTTPMethod:@"POST"];
    [request addValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [request setValue:[NSString stringWithFormat:@"%lu",(unsigned long)postData.length] forHTTPHeaderField:@"Content-Length"];
    [request setHTTPBody:postData];
    
    //Mark the start of network call
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    NSURLSession *connSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:nil delegateQueue:[NSOperationQueue mainQueue]];
    NSURLSessionDataTask *sessionPostDataTask = [connSession dataTaskWithRequest:request completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable connectionError) {
        
        
        //Mark the end of network call
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        
        if(data) {
            
            NSInteger responseCode = [(NSHTTPURLResponse *)response statusCode];
            NSString *strData = [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding];

            
            if (!connectionError && responseCode == 200) {
                
                [self doProcessMohResponse:strData];
            }
            else {
                [self popdRequestFailed:@kMSG_REQUEST_FAIL_TITLE1 message:@kMSG_REQUEST_FAIL_PARA1 button:@kMSG_COMMON_OK];
            }
            
        }
        else {
            [self popdNetworkORServerFailed:@kMSG_CONNECT_NO_TITLE1 message:@kMSG_CONNECT_NO_PARA1 button:@kMSG_COMMON_OK];
        }
        
    }];
    [sessionPostDataTask resume];

}



//Process the response
-(void)doProcessMohResponse:(NSString *)reponseString {
    
    //validation block ------------------------
    //error_net_connection - no route to host, can't find an active connection
    //error_net_other - network time-out or other connector exceptions
    //error_db_params - inappropriate parameters with the request
    //error_db_connect - database connection issue at server side
    //authentication_required - user need to be authenticate
    //authentication_expired - user session has expired
    
    //Handle json data
    NSString *jsonString = [reponseString stringByReplacingOccurrencesOfString:@"'" withString:@"\""]; //Replacing character ' with " for json translation
    
    
    NSData *JSONdata = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *jsonError = nil;
    
    //Handle json data-part
    if (JSONdata != nil) {
        
        //This you need to know whether it's json or string
        NSDictionary *dicJson = [NSJSONSerialization JSONObjectWithData:JSONdata options:0 error:&jsonError];
        
        if ( (jsonError != nil) || (dicJson == (NSDictionary *)[NSNull null]) ) {
            
            //Check whether it's a string response
            if (jsonString != nil) {
                
                //Note: response is not json, its comma seperated valus. Value before first comma is the key.
                NSArray *serverResponseArray = [jsonString componentsSeparatedByString:@"&"];
                
                
                //Manage empty or error key
                if (!serverResponseArray || !serverResponseArray.count){
                    [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
                    return;
                } else {
                    
                    //Show colombo on the map
                    CLLocationCoordinate2D location;
                    location.latitude = [@kGPS_COLOMBO_LAT floatValue];
                    location.longitude = [@kGPS_COLOMBO_LNG floatValue];
                    
                    MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(location, 5000, 8000);
                    [self.mapView setRegion:[self.mapView regionThatFits:region] animated:YES];
                    
                    //Add MOH boundries
                    NSArray *stringLatLng = [@kCMC_MOH1 componentsSeparatedByString:@";"];
                    [self doDrawMohRegions:stringLatLng countLatLng:stringLatLng.count];
                    
                    stringLatLng = [@kCMC_MOH2 componentsSeparatedByString:@";"];
                    [self doDrawMohRegions:stringLatLng countLatLng:stringLatLng.count];
                    
                    stringLatLng = [@kCMC_MOH3 componentsSeparatedByString:@";"];
                    [self doDrawMohRegions:stringLatLng countLatLng:stringLatLng.count];
                    
                    stringLatLng = [@kCMC_MOH4 componentsSeparatedByString:@";"];
                    [self doDrawMohRegions:stringLatLng countLatLng:stringLatLng.count];
                    
                    stringLatLng = [@kCMC_MOH5 componentsSeparatedByString:@";"];
                    [self doDrawMohRegions:stringLatLng countLatLng:stringLatLng.count];
                    
                    stringLatLng = [@kCMC_MOH6 componentsSeparatedByString:@";"];
                    [self doDrawMohRegions:stringLatLng countLatLng:stringLatLng.count];

                    
                    //Processing MOH data
                    NSData *subJSONdata = [[serverResponseArray objectAtIndex:0] dataUsingEncoding:NSUTF8StringEncoding];
                    
                    [self doProcessMOHData:subJSONdata];

                    
                    //Processing Hotspot data
                    subJSONdata = [[serverResponseArray objectAtIndex:1] dataUsingEncoding:NSUTF8StringEncoding];
                    
                    [self doProcessHotspotData:subJSONdata];

                }

            }
            else {
                //NSLog(@"response error");
                [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
            }
            
            return;
        }
        
        //Checking the values
        NSString *keyStatus = [dicJson objectForKey:@"status"];
        
        
        if ([keyStatus caseInsensitiveCompare:@"error_db_params"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE2 message:@kMSG_RESPONSE_UNEXPECTED_PARA2 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"error_db_connect"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE2 message:@kMSG_RESPONSE_UNEXPECTED_PARA2 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"error_db_update"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_failed"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_LOGINUSER_ERROR_TITLE1 message:@kMSG_LOGINUSER_ERROR_PARA1 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_expired"] == NSOrderedSame ) {
            //redirect to login page
            [self showPopupReAuthenticateDialog:@kMSG_RESPONSE_REAUTHENTICATION_TITLE2 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA2 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_required"] == NSOrderedSame ) {
            //redirect to login page
            [self showPopupReAuthenticateDialog:@kMSG_RESPONSE_REAUTHENTICATION_TITLE1 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA1 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_blocked"] == NSOrderedSame ) {
            //redirect to login page
            [self showPopupReAuthenticateDialog:@kMSG_RESPONSE_REAUTHENTICATION_TITLE3 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA3 button:@kMSG_COMMON_OK];
            
        } else {
            //This block is for the undifine response
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
        }
        
    }
    else {
        //Error in getting json. Not the expected outcome.
        [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
        
    }
}


//--------- Popup messages ----------
-(void)showPopupDialog:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:byTitle message:byMessageText preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *alertAction = [UIAlertAction actionWithTitle:byButtonText style:UIAlertActionStyleDefault handler:nil];
    
    [alertController addAction:alertAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

-(void)popdNetworkFailed:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    [self showPopupDialog:byTitle message:byMessageText button:byButtonText];
}

-(void)popdRequestFailed:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    [self showPopupDialog:byTitle message:byMessageText button:byButtonText];
}

-(void)popdNetworkORServerFailed:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    [self showPopupDialog:byTitle message:byMessageText button:byButtonText];
}

-(void)showPopupSuccessDialog:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:byTitle message:byMessageText preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *alertAction = [UIAlertAction actionWithTitle:byButtonText style:UIAlertActionStyleDefault handler:^(UIAlertAction * action)
                                  {
                                      //Reset the UI
                                  }];
    
    [alertController addAction:alertAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

-(void)showPopupReAuthenticateDialog:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:byTitle message:byMessageText preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *alertAction = [UIAlertAction actionWithTitle:byButtonText style:UIAlertActionStyleDefault handler:^(UIAlertAction * action)
                                  {
                                      [self doLogoutUser];
                                  }];
    
    [alertController addAction:alertAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}


-(void)doLogoutUser {
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    //Set user defaults for this session
    [userDefaults setBool:NO forKey:@kIS_USER_LOGGED];
    [userDefaults setObject:@"" forKey:@kLOGGED_USER_NAME];
    [userDefaults setObject:@"" forKey:@kLOGGED_USER_TOKEN];
    [userDefaults setObject:@""forKey:@kLOGGED_USER_UUDID];
    [userDefaults setObject:@"" forKey:@kLOGGED_USER_LANGUAGE];
    [userDefaults synchronize];
    
    [self performSegueWithIdentifier:@"unwindToProfileLoginVC" sender:self]; //Navigate to the login form.
}




-(void)doProcessMOHData:(NSData*)jsonData {

    NSError *jsonError = nil;
    
    self.subArrayMohData = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:&jsonError];
    
    @try {
        
        if (jsonError == nil) {
            
            CLLocationCoordinate2D location;
            MKPointAnnotation *mohPointAnnotation = nil;
            
            for (NSDictionary *dicTempMoh in self.subArrayMohData) {

                NSArray *gpsLocation = [[dicTempMoh objectForKey:@"moh_location"] componentsSeparatedByString:@","];
                
                location.latitude = [[gpsLocation objectAtIndex:0] floatValue];
                location.longitude = [[gpsLocation objectAtIndex:1] floatValue];

                // Add an annotation
                mohPointAnnotation = [[MKPointAnnotation alloc] init];
                mohPointAnnotation.coordinate = location;
                mohPointAnnotation.title = [NSString stringWithFormat:@"%@",[dicTempMoh objectForKey:@"id"]];
                [self.mapView addAnnotation:mohPointAnnotation];
            }
        }
        
    }
    @catch (NSException * e) {
        //NSLog(@"Exception: %@", e);
    }
    @finally {
        //NSLog(@"finally");
    }

}


-(void)doProcessHotspotData:(NSData*)jsonData {
    
    NSError *jsonError = nil;
     
    NSArray *subArrayData = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:&jsonError];
     
    @try {
        
        BOOL isFirst = YES;
        NSNumber *lat, *lng, *rad;
        NSString *fromDate, *toDate;
        
        for (NSDictionary *dicTempHs in subArrayData) {

            if (isFirst) {
                
                fromDate = [dicTempHs objectForKey:@"fromdate"];
                toDate = [dicTempHs objectForKey:@"todate"];
                NSNumber *toYear = [dicTempHs objectForKey:@"toyear"];
                NSNumber *toWeek = [dicTempHs objectForKey:@"toweek"];

                NSDate *currDate = [NSDate date];
                NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
                NSDateComponents *components = [calendar components:NSCalendarUnitYear|NSCalendarUnitWeekOfYear fromDate:currDate];
                
                NSInteger calYear = [components year];
                NSInteger calWeek = [components weekOfYear];

                
                if ( ([toYear integerValue] == calYear) && ([toWeek integerValue] == calWeek) ) {
                    
                    toDate =[GlobalVariableAndMethod getCurrentDateTime:@"yyyy-MMM-dd"];
                }
                
                if ([GlobalVariableAndMethod validateString:fromDate] && [GlobalVariableAndMethod validateString:toDate]) {

                    self.lablFromDate.text = [NSString stringWithFormat:@"From: %@", fromDate];
                    self.lablToDate.text = [NSString stringWithFormat:@"To: %@", toDate];
                }
                
                isFirst = NO;
            }
            
            
            lat = [dicTempHs objectForKey:@"lat"];
            lng = [dicTempHs objectForKey:@"lng"];
            rad = [dicTempHs objectForKey:@"rad"];
            self.hexColorCode = [dicTempHs objectForKey:@"color"];
            
            CLLocationCoordinate2D center = {[lat floatValue], [lng floatValue]};
            
            // Add an overlay
            MKCircle *circle = [MKCircle circleWithCenterCoordinate:center radius:[rad intValue]];
            [self.mapView addOverlay:circle];

        }
     
    }
    @catch (NSException * e) {
        //NSLog(@"Exception: %@", e);
    }
    @finally {
        //NSLog(@"finally");
    }
}


- (MKOverlayView *)mapView:(MKMapView *)mapView viewForOverlay:(id<MKOverlay>)overlay
{
    
    if([overlay isKindOfClass:[MKPolyline class]]){
        
        MKPolylineView *polylineView = [[MKPolylineView alloc] initWithPolyline:overlay];
        polylineView.strokeColor = [UIColor blackColor];
        polylineView.lineWidth = 2.0;
        return polylineView;
    }
    else if([overlay isKindOfClass:[MKCircle class]]){
        
         MKCircleView *circleView = [[MKCircleView alloc] initWithOverlay:overlay];
         [circleView setFillColor:[GlobalVariableAndMethod colorFromHexString:self.hexColorCode]];
         [circleView setAlpha:0.5f];
         return circleView;
    }
    else {
        return nil;
    }
}


-(MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation
{
    // If it's the user location, just return nil.
    if ([annotation isKindOfClass:[MKUserLocation class]])
        return nil;
    
    // Handle any custom annotations.
    if ([annotation isKindOfClass:[MKPointAnnotation class]])
    {
        MKPointAnnotation *mohPointAnnotation = annotation;
        
        // Try to dequeue an existing pin view first.
        MKAnnotationView *pinView = (MKAnnotationView*)[mapView dequeueReusableAnnotationViewWithIdentifier:@"CustomPinAnnotationView"];
        if (!pinView)
        {
            // If an existing pin view was not available, create one.
            pinView = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"CustomPinAnnotationView"];
            pinView.image = [UIImage imageNamed:@"icon_moh"];
            pinView.layer.anchorPoint = CGPointMake(0.5f, 1.0f);
            pinView.tag = [mohPointAnnotation.title intValue];

            [pinView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tappedMarker:)]];
            
        } else {
            pinView.annotation = annotation;
        }
        return pinView;
    }
    
    return nil;
}



-(void)doDrawMohRegions:(NSArray *)stringLatLng countLatLng:(long)countLatLng {

    NSArray *latLng;
    CLLocationCoordinate2D coordinates[countLatLng];

    for (NSInteger index = 0; index < countLatLng; index++) {
        
        @try {
            
            latLng = [[stringLatLng objectAtIndex:index] componentsSeparatedByString:@","];
            CLLocation *location = [[CLLocation alloc] initWithLatitude:[[latLng objectAtIndex:1] floatValue] longitude:[[latLng objectAtIndex:0] floatValue]];
            CLLocationCoordinate2D coordinate = location.coordinate;
            coordinates[index] = coordinate;
            
        }
        @catch (NSException *exception) {
            //Nothing to do
        }
        @finally {
            //Nothing to do
        }
    }
    
    MKPolyline *myPolyline = [MKPolyline polylineWithCoordinates:coordinates count:countLatLng];
    [self.mapView addOverlay:myPolyline];
}


- (void) tappedMarker:(id)sender {
    
    UITapGestureRecognizer *tapRecognizer = (UITapGestureRecognizer *)sender;
    NSNumber *tagValue = [NSNumber numberWithInteger: [tapRecognizer.view tag]];
    NSString *markerTag = [NSString stringWithFormat:@"%d", tagValue.intValue];
    NSString *dataID;

    
    for (NSDictionary *dicTempMoh in self.subArrayMohData) {
        dataID = [NSString stringWithFormat:@"%@",[dicTempMoh objectForKey:@"id"]];
        
        if ([markerTag isEqualToString:dataID]) {
            
            NSString *mohTitle = [NSString stringWithFormat:@"MOH  office - %@",[dicTempMoh objectForKey:@"district_moh"]];
            
            NSString *callDoctorPhone = [dicTempMoh objectForKey:@"moh_doctor_telephone"];
            NSString *callMohPhone = [dicTempMoh objectForKey:@"moh_telephone"];
            
            NSString *mohAddress = [NSString stringWithFormat:@"Address: %@ \nDoctor: %@ \n%@(Doc) | %@(MOH)", [dicTempMoh objectForKey:@"moh_address"], [dicTempMoh objectForKey:@"moh_doctor"], callDoctorPhone, callMohPhone];
            
            callDoctorPhone = [NSString stringWithFormat:@"telprompt://%@",[[callDoctorPhone stringByReplacingOccurrencesOfString:@" " withString:@""] stringByReplacingOccurrencesOfString:@"-" withString:@""] ];
            callMohPhone = [NSString stringWithFormat:@"telprompt://%@",[[callMohPhone stringByReplacingOccurrencesOfString:@" " withString:@""] stringByReplacingOccurrencesOfString:@"-" withString:@""] ];
            

            UIImageView *mohImageView;
            switch ([dataID intValue]) {
                case 0:
                    mohImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_marker_moh_d1"]];
                    break;
                case 1:
                    mohImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_marker_moh_d2a"]];
                    break;
                case 2:
                    mohImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_marker_moh_d2b"]];
                    break;
                case 3:
                    mohImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_marker_moh_d3"]];
                    break;
                case 4:
                    mohImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_marker_moh_d4"]];
                    break;
                case 5:
                    mohImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_marker_moh_d5"]];
                    break;
                default:
                    break;
            }
            
            float newXPos = (self.view.frame.size.width-mohImageView.frame.size.width-20)>0 ? (self.view.frame.size.width-mohImageView.frame.size.width-20)/2 : 0; // to get the x coordinates of the picture. 20.0 is reduced to set the x coordinate, to the edge of the screen.

            float imageHeight = mohImageView.frame.size.height;
            
            if (self.view.frame.size.height < 500.0) { //To identify iPhone 4S screen
                
                //To support small screen sizes
                imageHeight -= 60.0;
                newXPos += 20;
                [mohImageView setFrame:CGRectMake(newXPos, (-imageHeight-10), mohImageView.frame.size.width-40, imageHeight)];
                mohImageView.contentMode = UIViewContentModeScaleToFill;
            }
            else {
                
                [mohImageView setFrame:CGRectMake(newXPos, (-imageHeight-10), mohImageView.frame.size.width, imageHeight)];
                 mohImageView.contentMode = UIViewContentModeScaleAspectFit;
            }

            
            //- BezierPath -------------
            CGFloat imgvRadius = 10.0, imgvLineWidth = 1.0;

            CGRect rect = mohImageView.bounds;
            
            //Make round
            // Create the path for to make circle
            UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:rect
                                                           byRoundingCorners:UIRectCornerAllCorners
                                                                 cornerRadii:CGSizeMake(imgvRadius, imgvRadius)];
            
            // Create the shape layer and set its path
            CAShapeLayer *maskLayer = [CAShapeLayer layer];
            
            maskLayer.frame = rect;
            maskLayer.path  = maskPath.CGPath;
            
            // Set the newly created shape layer as the mask for the view's layer
            mohImageView.layer.mask = maskLayer;
            
            //Give Border
            //Create path for border
            UIBezierPath *borderPath = [UIBezierPath bezierPathWithRoundedRect:rect
                                                             byRoundingCorners:UIRectCornerAllCorners
                                                                   cornerRadii:CGSizeMake(imgvRadius, imgvRadius)];
            
            // Create the shape layer and set its path
            CAShapeLayer *borderLayer = [CAShapeLayer layer];
            
            borderLayer.frame       = rect;
            borderLayer.path        = borderPath.CGPath;
            borderLayer.strokeColor = [UIColor whiteColor].CGColor;
            borderLayer.fillColor   = [UIColor clearColor].CGColor;
            borderLayer.lineWidth   = imgvLineWidth;
            
            //Add this layer to give border.
            [[mohImageView layer] addSublayer:borderLayer];
            //--------------------------
            
            
            UIAlertController *alertView = [UIAlertController alertControllerWithTitle:mohTitle message:mohAddress preferredStyle:UIAlertControllerStyleActionSheet];
             
            UIAlertAction *callDoctor = [UIAlertAction actionWithTitle:@"\ue00a Call Doctor" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
                [[UIApplication sharedApplication] openURL:[NSURL URLWithString:callDoctorPhone]];
            }];
            UIAlertAction *callMOH = [UIAlertAction actionWithTitle:@"\ue009 Call MOH office" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
                [[UIApplication sharedApplication] openURL:[NSURL URLWithString:callMohPhone]];
            }];
            
            UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
                [alertView dismissViewControllerAnimated:YES completion:nil];
            }];
            

            [alertView.view addSubview:mohImageView];
            [alertView addAction:callDoctor];
            [alertView addAction:callMOH];
            [alertView addAction:cancel];
            [self.view bringSubviewToFront:mohImageView];
            [self presentViewController:alertView animated:YES completion:nil];
            
            break;
        }
    }

    
    
    

    
}



@end
