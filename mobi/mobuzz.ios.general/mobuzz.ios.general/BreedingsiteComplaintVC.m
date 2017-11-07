//
//  BreedingsiteComplaintVC.m
//  mobuzz.ios.general
//
//  Created by Vajira on 3/9/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import "BreedingsiteComplaintVC.h"
#import "GlobalVariableAndMethod.h"
#import "PopupMessages.h"
#import "ProfileLoginVC.h"
#import "CMCRegions.h"

@interface BreedingsiteComplaintVC () <UIImagePickerControllerDelegate>

@property (strong, nonatomic) IBOutlet UILabel *lablL1;
@property (strong, nonatomic) IBOutlet UILabel *lablL2;
@property (strong, nonatomic) IBOutlet UILabel *lablL3;
@property (strong, nonatomic) IBOutlet UILabel *lablL4;
@property (strong, nonatomic) IBOutlet UILabel *lablL5;
@property (strong, nonatomic) IBOutlet UILabel *lablL6;
@property (strong, nonatomic) IBOutlet UILabel *lablL7;
@property (strong, nonatomic) IBOutlet UILabel *lablL8;

@property (strong, nonatomic) IBOutlet UIImageView *imgvHistory;

@property (strong, nonatomic) IBOutlet UIButton *butnCamera;
@property (strong, nonatomic) IBOutlet UIButton *butnGallery;
@property (strong, nonatomic) IBOutlet UIButton *butnBack;
@property (strong, nonatomic) IBOutlet UIButton *butnSubmit;
@property (strong, nonatomic) IBOutlet UIButton *butnUrgency;

@property (strong, nonatomic) IBOutlet UIImageView *imgvPhoto;
@property (strong, nonatomic) IBOutlet UITextField *txtfAddress;
@property (strong, nonatomic) IBOutlet UITextField *txtfRemark;

@property (strong, nonatomic) IBOutlet UIScrollView *uiswContent;
@property (strong, nonatomic) IBOutlet UIActivityIndicatorView *actvUpdate;

@property NSString *currentUrgency;
@property NSArray *urgencyArray;
@property NSString *currentGPS;
@property NSString *photoData;

@property (strong, nonatomic) IBOutlet MKMapView *mapview;
@property(nonatomic, retain) CLLocationManager *locationManager;

@property int keyboardOffSet;

@end


//Make a dengue complaint, and sent it to the server
@implementation BreedingsiteComplaintVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    //Hide the tabbar
    self.tabBarController.tabBar.hidden = YES;

    //Make the image clickable for navigation
    [self.imgvHistory setUserInteractionEnabled:YES];
    [self.imgvHistory addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tappedImage)]];
    
    //Manage touch gesture out side the text fields
    UITapGestureRecognizer * tapGesture = [[UITapGestureRecognizer alloc]
                                           initWithTarget:self
                                           action:@selector(hideKeyBoard)];
    
    self.keyboardOffSet = (int)kKEYBOARD_MOVE_OFFSET;
    [self.view addGestureRecognizer:tapGesture];
    [self.uiswContent setContentOffset:CGPointZero animated:YES];
    
    //Update visual styles for buttons
    self.butnCamera.layer.borderWidth = 1.0f;
    self.butnGallery.layer.borderWidth = 1.0f;
    self.butnSubmit.layer.borderWidth = 1.0f;
    self.butnBack.layer.borderWidth = 1.0f;

    self.butnBack.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"ash"]CGColor];
    self.butnCamera.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"yellow"]CGColor];
    self.butnGallery.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"yellow"]CGColor];
    self.butnSubmit.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"yellow"]CGColor];
    
    self.lablL1.text = @" ";
    self.lablL2.text = @" ";
    self.lablL3.text = @" ";
    self.lablL4.text = @" ";
    self.lablL5.text = @" ";
    self.lablL6.text = @" ";
    self.lablL7.text = @" ";
    self.lablL8.text = @" ";

    self.imgvPhoto.image = nil;
    self.imgvPhoto.hidden = YES;
    self.lablL4.hidden = YES;
    
    //Setup values
    self.urgencyArray = [[NSArray alloc]initWithObjects:@"Very urgent", @"Urgent", @"Not so urgent, but important", nil];

    [self setMap];
}


-(void)tappedImage {
    [self.tabBarController setSelectedIndex:1];
}

- (IBAction)backPressed:(UIButton *)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)urgencyPressed:(UIButton *)sender {
    [self hideKeyBoard];
    [self setUrgency];
}

- (IBAction)cameraPressed:(UIButton *)sender {
    [self hideKeyBoard];
    [self takePhoto];
}

- (IBAction)galleryPressed:(UIButton *)sender {
    [self hideKeyBoard];
    [self loadPhoto];
}

- (IBAction)submitPressed:(UIButton *)sender {
    [self hideKeyBoard];
    if ([self validateUserInputs]) {
        [self checkInternetConnectionAndMakeNewRequest];
    }
}

-(BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender {
    return NO;
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    
    if ([segue.identifier isEqualToString:@"ComplaintProfileLoginSegue"]) {
        ProfileLoginVC *plvc = segue.destinationViewController;
        plvc.selfDismiss = YES;
    }
    
}


-(void)setMap {
    self.mapview.delegate = self;
    self.locationManager = [[CLLocationManager alloc] init];
    self.locationManager.delegate = self;
    
    [self.locationManager requestWhenInUseAuthorization];
    [self.locationManager requestAlwaysAuthorization];
    
    CLAuthorizationStatus authorizationStatus= [CLLocationManager authorizationStatus];
    if (authorizationStatus == kCLAuthorizationStatusAuthorizedAlways || authorizationStatus == kCLAuthorizationStatusAuthorizedWhenInUse) {
        
        [self.locationManager startUpdatingLocation];
        self.mapview.showsUserLocation = YES;
        
        self.currentGPS = [NSString stringWithFormat:@"%f,%f", self.locationManager.location.coordinate.latitude, self.locationManager.location.coordinate.longitude];
    }

    [self.mapview setMapType:MKMapTypeStandard];
    [self.mapview setZoomEnabled:YES];
    [self.mapview setScrollEnabled:YES];
   
    //Add CMC boundries
    NSArray *stringLatLng = [@kCMC_REGION componentsSeparatedByString:@";"];
    [self doDrawCMCRegions:stringLatLng countLatLng:stringLatLng.count];
}

- (void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation
{
    MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(userLocation.coordinate, 800, 800);
    [self.mapview setRegion:[self.mapview regionThatFits:region] animated:YES];
    
    self.currentGPS = [NSString stringWithFormat:@"%f,%f", userLocation.coordinate.latitude, userLocation.coordinate.longitude];

}

-(void)setUrgency {

    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Rate the level of urgency" message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    //Handle the pop-up menu options
    for (NSString *title in self.urgencyArray) {
        UIAlertAction *alertAction = [UIAlertAction actionWithTitle:title style:UIAlertActionStyleDefault handler:^(UIAlertAction * action)
                                      {
                                          [self.butnUrgency setTitle:[NSString stringWithFormat: @" %@",title] forState:   UIControlStateNormal];
                                          [self.butnUrgency setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                                          self.currentUrgency = title;
                                      }];
        [alertController addAction:alertAction];
    }
    
    //for Cancel
    UIAlertAction *alertCancelAction = [UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:nil];
    [alertController addAction:alertCancelAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}


-(void)takePhoto {
    
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) { //If camera available
        
        UIImagePickerController *imagePickerController = [[UIImagePickerController alloc] init];
        imagePickerController.delegate = (id)self;
        imagePickerController.sourceType = UIImagePickerControllerSourceTypeCamera;
        imagePickerController.mediaTypes = [NSArray arrayWithObjects: (NSString *) kUTTypeImage, nil];
        imagePickerController.allowsEditing = YES;
        
        [self presentViewController:imagePickerController animated:true completion:nil];
    }
}

-(void)loadPhoto {

    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeSavedPhotosAlbum]) {
        
        UIImagePickerController *imagePickerController = [[UIImagePickerController alloc] init];
        imagePickerController.delegate = (id)self;
        imagePickerController.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
        imagePickerController.mediaTypes = [NSArray arrayWithObjects: (NSString *) kUTTypeImage, nil];
        imagePickerController.allowsEditing = NO; //Allow cropping
        
        [self presentViewController:imagePickerController animated:true completion:nil];
    }
}

-(void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    
    NSData *dataImage = UIImageJPEGRepresentation([info objectForKey:@"UIImagePickerControllerOriginalImage"],1);
    UIImage *img = [[UIImage alloc] initWithData:dataImage];

    
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera])
    {
        
        if(picker.sourceType==UIImagePickerControllerSourceTypeCamera)
        {
            //The camera is available
            UIImageWriteToSavedPhotosAlbum(img ,
                                           self, // send the message to 'self' when calling the callback
                                           nil, // the selector to tell the method to call on completion
                                           nil);
        }
        
    }else
    {
        //No camera available
    }
    
    [self dismissViewControllerAnimated:YES completion:nil];

    
    CGSize newSize = CGSizeMake(self.imgvPhoto.bounds.size.width, self.imgvPhoto.bounds.size.width*(img.size.height/img.size.width) );
    UIGraphicsBeginImageContext( newSize );
    
    [img drawInRect:CGRectMake(0,0,newSize.width,newSize.height)];
    
    UIImage* newImage = UIGraphicsGetImageFromCurrentImageContext();
    //UIGraphicsEndImageContext();
    
    //set image
    self.imgvPhoto.hidden = NO;
    [self.imgvPhoto setImage:newImage];
    
    //[self.lablL4 setText:@" "];
    self.lablL4.hidden = NO;

}


-(void)doDrawCMCRegions:(NSArray *)stringLatLng countLatLng:(long)countLatLng {
    
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
    
    MKPolygon *myPolygon = [MKPolygon polygonWithCoordinates:coordinates count:countLatLng];
    [self.mapview addOverlay:myPolygon];
}

- (MKOverlayView *)mapView:(MKMapView *)mapView viewForOverlay:(id<MKOverlay>)overlay
{
    
    if([overlay isKindOfClass:[MKPolygon class]]){
        
        MKPolygonView *polygonView = [[MKPolygonView alloc] initWithOverlay:overlay];
        polygonView.strokeColor = [UIColor blackColor];
        polygonView.lineWidth = 2.0;
        polygonView.fillColor = [[GlobalVariableAndMethod getMobuzzColor:@"yellow"] colorWithAlphaComponent:0.7];
        return polygonView;
    }
    else {
        return nil;
    }
}

#pragma mark - "make new complaint for breedingsite"

-(void)checkInternetConnectionAndMakeNewRequest {
    
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    //Check the internet connection
    NSURLSession *connTestSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:self delegateQueue:[NSOperationQueue mainQueue] ];
    NSURLSessionDataTask * sessionDataTask = [connTestSession dataTaskWithURL:[NSURL URLWithString:@kCONN_URL_CHECK] completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        
        //Check connection is available or not
        if ((error == nil) && data) {
            
            //Create the request.
            [self callAsynPostComplaintNew: [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", @kCONN_URL_BASE, @kCONN_URL_COMPLAINT_NEW] ]];
        }
        else {
            
            //Popup message
            [self popdNetworkFailed:@kMSG_INTERNET_NO_TITLE1 message:@kMSG_INTERNET_NO_PARA1 button:@kMSG_COMMON_OK];
        }
        
    }];
    [sessionDataTask resume];
        
}



//Make a post call to server to login
-(void)callAsynPostComplaintNew: (NSURL *)url {
    

    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSString *username = [userDefaults stringForKey:@kLOGGED_USER_NAME];
    NSString *timeStamp = [userDefaults stringForKey:@kLOGGED_USER_TOKEN];
    NSString *uudid = [userDefaults stringForKey:@kLOGGED_USER_UUDID];
    
    //NUll values may break the array in the middle
    NSString *_address = (self.txtfAddress.text) ? self.txtfAddress.text : @"";
    NSString *_remarks = (self.txtfRemark.text) ? self.txtfRemark.text : @"";
    NSString *_image = (self.photoData) ? self.photoData :  @"";
    NSString *_assess = (self.currentUrgency) ? self.currentUrgency : @"";
    NSString *_discption = @"";
    
    
    NSError *errorJson;
    
    //Creating the json
    NSDictionary *jsonDictionary = [NSDictionary dictionaryWithObjectsAndKeys:
                                    _address, @"address",
                                    _remarks, @"remarks",
                                    _image, @"image",
                                    _assess, @"assess",
                                    _discption, @"discption",
                                    
                                    [GlobalVariableAndMethod getCurrentDateTime:@kDATE_TIME_FORMAT], @"date",
                                    self.currentGPS, @"location",
                                    [GlobalVariableAndMethod getDeviceName], @"mobile",
                                    
                                    username, @"user",
                                    timeStamp, @"time_stamp",
                                    uudid, @"uudid",
                                    nil];
    

    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:jsonDictionary options:NSJSONWritingPrettyPrinted error:&errorJson];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];

    
    if (!errorJson) {
        NSData *postData=[jsonString dataUsingEncoding:NSUTF8StringEncoding];
        [self doProcessNetworkComplaintNewRequest:url data:postData flag:0];
    }
}


-(void)doProcessNetworkComplaintNewRequest:(NSURL *)url data:(NSData*)postData flag:(int)processFlag {
    
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
    [request setHTTPMethod:@"POST"];
    [request addValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [request setValue:[NSString stringWithFormat:@"%lu",(unsigned long)postData.length] forHTTPHeaderField:@"Content-Length"];
    [request setHTTPBody:postData];

    //Mark the start of network call
    self.actvUpdate.hidden = NO;
    self.view.userInteractionEnabled = NO;
    [self.actvUpdate startAnimating];
    
    NSURLSession *connSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:nil delegateQueue:[NSOperationQueue mainQueue]];
    NSURLSessionDataTask *sessionPostDataTask = [connSession dataTaskWithRequest:request completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable connectionError) {
        
        //Mark the end of network call
        [self.actvUpdate stopAnimating];
        self.actvUpdate.hidden = YES;
        self.view.userInteractionEnabled = YES;
        
        
        if(data) {
            
            NSInteger responseCode = [(NSHTTPURLResponse *)response statusCode];
            NSString *strData = [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding];

            
            if (!connectionError && responseCode == 200) {
                
                [self doProcessUpdateResponse:strData flag:processFlag];
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
-(void)doProcessUpdateResponse:(NSString *)reponseString flag:(int)processFlag {
    
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
        
        //this you need to know json root is NSDictionary or NSArray , you smaple is NSDictionary
        NSDictionary *dicJson = [NSJSONSerialization JSONObjectWithData:JSONdata options:0 error:&jsonError];
        
        
        if ( (jsonError != nil) || (dicJson == (NSDictionary *)[NSNull null]) ) {
            
            //Check whether it's a string response
            if (jsonString != nil) {
                
                //Note: response is not json, its comma seperated valus. Value before first comma is the key.
                NSArray *serverResponseArray = [jsonString componentsSeparatedByString:@","];
                NSString *keyStatus, *valueStatus;
                
                //Manage empty or error key
                if (!serverResponseArray || !serverResponseArray.count){
                    keyStatus = @"";
                } else {
                    keyStatus = [serverResponseArray objectAtIndex:0];
                    keyStatus = [keyStatus stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]]; //Trim the string
                }
                
                valueStatus = [serverResponseArray objectAtIndex:1];
                valueStatus = [valueStatus stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]]; //Trim the string

                
                if ([keyStatus isEqual:[NSNull null]]) {
                    //empty response
                    [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
                    return;
                    
                }
                else if ([keyStatus caseInsensitiveCompare:@"ok"] == NSOrderedSame ) {
                    
                    [self showPopupSuccessDialog:@kMSG_REPORT_SUCCESS_TITLE1 message:@kMSG_REPORT_SUCCESS_PARA1 button:@kMSG_COMMON_OK];
                }
                else {
                    //NSLog(@"other response");
                    [self popdRequestFailed:@kMSG_REPORT_FAIL_TITLE1 message:@kMSG_REPORT_FAIL_PARA1 button:@kMSG_COMMON_OK];
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



//---------- Validating inputs ----------

-(BOOL) validateUserInputs {
    
    //Update the current gps, important for when gps privileges are just granted
    CLAuthorizationStatus authorizationStatus= [CLLocationManager authorizationStatus];
    if (authorizationStatus == kCLAuthorizationStatusAuthorizedAlways || authorizationStatus == kCLAuthorizationStatusAuthorizedWhenInUse) {
        
        [self.locationManager startUpdatingLocation];
        self.mapview.showsUserLocation = YES;
        
        self.currentGPS = [NSString stringWithFormat:@"%f,%f", self.locationManager.location.coordinate.latitude, self.locationManager.location.coordinate.longitude];
    }
    
    //Note: User validation is bottom up. So the popup will show the top-most validation error.
    NSString *errorTitle = @"", *errorText = @"";
    
    self.photoData = [UIImagePNGRepresentation(self.imgvPhoto.image) base64EncodedStringWithOptions:NSDataBase64Encoding64CharacterLineLength];
    
    
    //Validate discription
    if ( ![GlobalVariableAndMethod isUserTextValied:self.txtfRemark.text] ) {
        
        if ( [self.photoData isEqual:[NSNull null]] || !(self.photoData.length>0)) {

            //No Description or photo
            errorTitle = @"Invalid input";
            errorText = [NSString stringWithFormat:@"Description is too short \n(more than %d characters)", kVALIDATE_MIN_TEXT_SIZE];
            self.txtfRemark.rightViewMode = UITextFieldViewModeAlways;
            self.txtfRemark.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
            //[self.txtfRemark becomeFirstResponder];
            self.photoData = @"";
        }
    }
    else {
        self.txtfRemark.rightViewMode = UITextFieldViewModeNever;
    }

    //Validate address
    if ( ![GlobalVariableAndMethod isUserTextValied:self.txtfAddress.text] ) {
        
        errorTitle = @"Invalid input";
        errorText = [NSString stringWithFormat:@"Landmark/address is too short \n(more than %d characters)", kVALIDATE_MIN_TEXT_SIZE];
        self.txtfAddress.rightViewMode = UITextFieldViewModeAlways;
        self.txtfAddress.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
        //[self.txtfAddress becomeFirstResponder];
    }
    else {
        self.txtfAddress.rightViewMode = UITextFieldViewModeNever;
    }

    //Validate location
    if ( [self.currentGPS isEqual:[NSNull null]] || !(self.currentGPS.length>0)) {
        
        errorTitle = @"Invalid location";
        errorText = @"Incorrect GPS coordinates. Please check your GPS settings";
    }
    
    if ( ![errorTitle isEqual:[NSNull null]] && (errorTitle.length>1) ) {
        [self showPopupDialog:errorTitle message:errorText button:@"OK"];
        return FALSE;
    }
    else {
        return TRUE;
    }
    
    return NO;
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
                                      [self doResetUI];
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
    
    //[self performSegueWithIdentifier:@"unwindToProfileLoginVC" sender:self]; //Navigate to the login form.
    [self performSegueWithIdentifier:@"ComplaintProfileLoginSegue" sender:nil];
}


-(void)doResetUI {
    //Reset the UI
    self.txtfAddress.text = @"";
    self.txtfRemark.text = @"";
    self.currentUrgency =  NULL;
    [self.butnUrgency setTitle:@" Rate the level of urgency" forState:UIControlStateNormal];
    [self.butnUrgency setTitleColor:[GlobalVariableAndMethod getMobuzzColor:@"disable"] forState:UIControlStateNormal];
    self.imgvPhoto.image = nil;
    self.imgvPhoto.hidden = YES;
    self.lablL4.hidden = YES;
}


//---------- Managing the keyboard ----------

- (void)textFieldDidBeginEditing:(UITextField *)textField {
    
    int newLocationY = textField.frame.origin.y - self.keyboardOffSet;
    if (newLocationY>0) {
        CGPoint scrollPoint = CGPointMake(0, newLocationY);
        [self.uiswContent setContentOffset:scrollPoint animated:YES];
    }
}

- (void)textFieldDidEndEditing:(UITextField *)textField {
    
    [self.uiswContent setContentOffset:CGPointZero animated:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return NO;
}

-(void)hideKeyBoard {
    [self.txtfAddress resignFirstResponder];
    [self.txtfRemark resignFirstResponder];
}


@end
