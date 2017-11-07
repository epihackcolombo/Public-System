//
//  BreedingsiteDetailsVC.m
//  mobuzz.ios.general
//
//  Created by Vajira on 3/9/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import "BreedingsiteDetailsVC.h"
#import "GlobalVariableAndMethod.h"
#import "PopupMessages.h"


@interface BreedingsiteDetailsVC ()

@property (strong, nonatomic) IBOutlet UILabel *lablDate;
@property (strong, nonatomic) IBOutlet UILabel *lablWard;
@property (strong, nonatomic) IBOutlet UILabel *lablAddress;
@property (strong, nonatomic) IBOutlet UILabel *lablRemarks;
@property (strong, nonatomic) IBOutlet UILabel *lablAssessment;
@property (strong, nonatomic) IBOutlet UILabel *lablCmcMessage;

@property (strong, nonatomic) IBOutlet UILabel *lablL1;
@property (strong, nonatomic) IBOutlet UILabel *lablL2;
@property (strong, nonatomic) IBOutlet UILabel *lablL3;
@property (strong, nonatomic) IBOutlet UILabel *lablL4;
@property (strong, nonatomic) IBOutlet UILabel *lablL5;
@property (strong, nonatomic) IBOutlet UILabel *lablL6;
@property (strong, nonatomic) IBOutlet UILabel *lablL7;
@property (strong, nonatomic) IBOutlet UILabel *lablL8;
@property (strong, nonatomic) IBOutlet UILabel *lablL9;
@property (strong, nonatomic) IBOutlet UILabel *lablL10;
@property (strong, nonatomic) IBOutlet UILabel *lablL11;

@property (strong, nonatomic) IBOutlet UIButton *butnFullImage;
@property (strong, nonatomic) IBOutlet UIButton *butnBack;


@property (strong, nonatomic) IBOutlet MKMapView *mapView;
@property (strong, nonatomic) IBOutlet UIActivityIndicatorView *actvUpdate;
@property (strong, nonatomic) IBOutlet UIActivityIndicatorView *actvUpdateFullImage;

@property (strong, nonatomic) IBOutlet UIImageView *imgvPhoto;
@property (strong, nonatomic) IBOutlet UIView *uivwDownloadFullImage;

@property NSArray *imageArray;
@property NSMutableArray *mutableArray;
@property NSString *imageSize;

@end


//Show complaint details, and download image from the server if necessary
@implementation BreedingsiteDetailsVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.butnFullImage.layer.borderWidth = 1.0f;
    self.butnBack.layer.borderWidth = 1.0f;

    self.butnFullImage.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"yellow"]CGColor];
    self.butnBack.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"ash"]CGColor];
    
    self.lablL1.text = @"";
    self.lablL2.text = @"";
    self.lablL3.text = @"";
    self.lablL4.text = @"";
    self.lablL5.text = @"";
    self.lablL6.text = @"";
    self.lablL7.text = @"";
    self.lablL8.text = @"";
    self.lablL9.text = @"";
    self.lablL10.text = @"";
    self.lablL11.text = @"";

    
    //Fill text data
    self.lablDate.text = [self.complaintDescription objectForKey:@"date"];
    self.lablWard.text = [self.complaintDescription objectForKey:@"ward"];
    self.lablAddress.text = [self.complaintDescription objectForKey:@"address"];
    self.lablRemarks.text = [self.complaintDescription objectForKey:@"remarks"];
    self.lablAssessment.text = [self.complaintDescription objectForKey:@"assessment"];
    self.lablCmcMessage.text = [self.complaintDescription objectForKey:@"cmcmessage"];
    
    NSArray *wardNameLongArray = [kARRAY_WARD_NAME_LONG componentsSeparatedByString:@", "];
    int wardNo = [[self.complaintDescription objectForKey:@"ward"] intValue];
    if (wardNo >= 0 && wardNo <= 47) {
        self.lablWard.text = [wardNameLongArray objectAtIndex:wardNo];
    } else {
        self.lablWard.text = [wardNameLongArray objectAtIndex:0];
    }

    //Show the user location
    NSArray *locationArray = [[self.complaintDescription objectForKey:@"gps"] componentsSeparatedByString:@","];
    if (locationArray) {
        
        CLLocationCoordinate2D location;
        location.latitude = [[locationArray objectAtIndex:0] floatValue];
        location.longitude = [[locationArray objectAtIndex:1] floatValue];
        
        MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(location, 800, 800);
        [self.mapView setRegion:[self.mapView regionThatFits:region] animated:YES];
        
        // Add an annotation
        MKPointAnnotation *point = [[MKPointAnnotation alloc] init];
        point.coordinate = location;

        [self.mapView addAnnotation:point];
    }
    
    NSNumber *isImage = [self.complaintDescription objectForKey:@"image"];

    self.actvUpdate.hidden = YES;
    self.actvUpdateFullImage.hidden = YES;
    self.imgvPhoto.hidden = YES;
    self.uivwDownloadFullImage.hidden = YES;
    
    if ([isImage intValue] > 0) {
        //Has image
        self.imageSize = @"thumbnail";
        [self checkInternetConnectionAndMakeLoadRequest];
    }
    else {
        self.lablL2.hidden = YES;
        self.lablL3.hidden = YES;
    }

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)backPressed:(UIButton *)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)fullImagePressed:(UIButton *)sender {
    self.imageSize = @"actual";
    [self checkInternetConnectionAndMakeLoadRequest];
}


#pragma mark - "make new complaint for breedingsite"

-(void)checkInternetConnectionAndMakeLoadRequest {
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    //Check the internet connection
    NSURLSession *connTestSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:self delegateQueue:[NSOperationQueue mainQueue] ];
    NSURLSessionDataTask * sessionDataTask = [connTestSession dataTaskWithURL:[NSURL URLWithString:@kCONN_URL_CHECK] completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        
        //Check connection is available or not
        if ((error == nil) && data) {
                
            //Create the request.
            [self callAsynPostComplaintHistory: [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", @kCONN_URL_BASE, @kCONN_URL_COMPLAINT_DETAILS_IMAGE] ]];
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
                                    
                                    [self.complaintDescription objectForKey:@"id"], @"id",
                                    self.imageSize, @"imagesize",
                                    
                                    nil];
  
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:jsonDictionary options:NSJSONWritingPrettyPrinted error:&errorJson];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    
    if (!errorJson) {
        NSData *postData=[jsonString dataUsingEncoding:NSUTF8StringEncoding];
        [self doProcessNetworkComplaintHistoryRequest:url data:postData flag:0];
    }
}


-(void)doProcessNetworkComplaintHistoryRequest:(NSURL *)url data:(NSData*)postData flag:(int)processFlag {
    

    
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
    [request setHTTPMethod:@"POST"];
    [request addValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [request setValue:[NSString stringWithFormat:@"%lu",(unsigned long)postData.length] forHTTPHeaderField:@"Content-Length"];
    [request setHTTPBody:postData];
    
    
    //Mark the start of network call
    if ([self.imageSize isEqualToString: @"thumbnail"]) {
        self.actvUpdateFullImage.hidden = YES;
        self.actvUpdate.hidden = NO;
        [self.actvUpdate startAnimating];
    } else {
        self.actvUpdate.hidden = YES;
        self.actvUpdateFullImage.hidden = NO;
        [self.actvUpdateFullImage startAnimating];
        self.butnFullImage.userInteractionEnabled = NO;
    }
    
    NSURLSession *connSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:nil delegateQueue:[NSOperationQueue mainQueue]];
    NSURLSessionDataTask *sessionPostDataTask = [connSession dataTaskWithRequest:request completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable connectionError) {
        
        
        //Mark the end of network call
        [self.actvUpdate stopAnimating];
        [self.actvUpdateFullImage stopAnimating];
        self.actvUpdate.hidden = YES;
        self.actvUpdateFullImage.hidden = YES;
        self.butnFullImage.userInteractionEnabled = YES;
        
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

        //This you need to know whether it's json or string
        NSDictionary *dicJson = [NSJSONSerialization JSONObjectWithData:JSONdata options:0 error:&jsonError];
        
        if ( (jsonError != nil) || (dicJson == (NSDictionary *)[NSNull null]) ) {
            
            //It's a string response
            //NSLog(@"response error");
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
            
            return;
        }
        
        //Checking the values
        NSString *keyStatus;
        
        keyStatus = [dicJson objectForKey:@"img_type"];
        
        if (keyStatus) {
            
            NSString *imageData = [dicJson objectForKey:@"img_data"];
            
            //Decode image data and create a UI image
            NSData *data = [[NSData alloc] initWithBase64EncodedString:imageData options:0];
            UIImage *image = [UIImage imageWithData:data];
            
            CGSize newSize;
            UIImage* newImage;

            
            if ([keyStatus caseInsensitiveCompare:@"thumbnail"] == NSOrderedSame ) {
                
                int removeWidth = 40; //Make the thumbnail smaller
                newSize = CGSizeMake( (self.imgvPhoto.bounds.size.width - removeWidth), (self.imgvPhoto.bounds.size.width - removeWidth)*(image.size.height/image.size.width) );
                
                self.uivwDownloadFullImage.hidden = NO;
            }
            else {
                newSize = CGSizeMake(self.imgvPhoto.bounds.size.width, self.imgvPhoto.bounds.size.width*(image.size.height/image.size.width) );
                
                self.uivwDownloadFullImage.hidden = YES;
            }
            
            
            UIGraphicsBeginImageContext( newSize );
            
            [image drawInRect:CGRectMake(0,0,newSize.width,newSize.height)];
            newImage = UIGraphicsGetImageFromCurrentImageContext();
            UIGraphicsEndImageContext();
            
            //set the image
            [self.imgvPhoto setImage:newImage];
            self.imgvPhoto.hidden = NO;
            
            return;
        }

        
        //Checking the values
        keyStatus = [dicJson objectForKey:@"status"];
        
        
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

@end
