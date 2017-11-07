//
//  ProfileEditVC.m
//  mobuzz.ios.general
//
//  Created by Vajira on 26/8/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import "ProfileEditVC.h"
#import "GlobalVariableAndMethod.h"
#import "PopupMessages.h"
#import "ProfileLoginVC.h"

@interface ProfileEditVC ()

@property (strong, nonatomic) IBOutlet UITextField *txtfName;
@property (strong, nonatomic) IBOutlet UITextField *txtfEmail;
@property (strong, nonatomic) IBOutlet UITextField *txtfContactNo;
@property (strong, nonatomic) IBOutlet UITextField *txtfCurrentPassword;
@property (strong, nonatomic) IBOutlet UITextField *txtfNewPassword;
@property (strong, nonatomic) IBOutlet UITextField *txtfRetypeNewPassword;

@property (strong, nonatomic) IBOutlet UIButton *butnResidence;
@property (strong, nonatomic) IBOutlet UIButton *butnLanguage;
@property (strong, nonatomic) IBOutlet UIButton *butnSignout;
@property (strong, nonatomic) IBOutlet UIButton *butnChangePassword;
@property (strong, nonatomic) IBOutlet UIButton *butnEdit;
@property (strong, nonatomic) IBOutlet UIButton *butnBack;

@property (strong, nonatomic) IBOutlet UILabel *lablUserName;

@property (strong, nonatomic) IBOutlet UIActivityIndicatorView *actvUpdate;

@property (strong, nonatomic) IBOutlet UIView *uivwSignOut;
@property (strong, nonatomic) IBOutlet UIView *uivwChangePassword;
@property (strong, nonatomic) IBOutlet UIScrollView *uiswContent;


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
@property (strong, nonatomic) IBOutlet UILabel *lablL12;

@property BOOL isEdited;

@property int keyboardOffSet;

@property NSString *filteredCharacterSet;
@property NSString *nearestCity, *preferredLanguage;
@property NSString *username, *timeStamp, *uudid;

@end


//Allow edit user profile data
@implementation ProfileEditVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.isEdited = NO;

    //Manage touch gesture out side the text fields
    UITapGestureRecognizer * tapGesture = [[UITapGestureRecognizer alloc]
                                           initWithTarget:self
                                           action:@selector(hideKeyBoard)];
    
    [self.view addGestureRecognizer:tapGesture];
    [self.uiswContent setContentOffset:CGPointZero animated:YES];
    
    self.keyboardOffSet = 100;

    self.butnBack.layer.borderWidth = 1.0f;
    self.butnEdit.layer.borderWidth = 1.0f;
    self.butnSignout.layer.borderWidth = 1.0f;
    self.butnChangePassword.layer.borderWidth = 1.0f;
    
    self.butnBack.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"ash"]CGColor];
    self.butnEdit.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"yellow"]CGColor];
    self.butnSignout.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"ash"]CGColor];
    self.butnChangePassword.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"yellow"]CGColor];
    
    //Holders for select button
    self.nearestCity = @"";
    self.preferredLanguage = @"";
    
    //Preset ui items
    [self presetUItoDefaulf];
    self.uivwSignOut.hidden = YES;

    //Get the saved parameters for the user
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    self.username = [userDefaults stringForKey:@kLOGGED_USER_NAME];
    self.timeStamp = [userDefaults stringForKey:@kLOGGED_USER_TOKEN];
    self.uudid = [userDefaults stringForKey:@kLOGGED_USER_UUDID];
    
    self.lablUserName.text = self.username;
    
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
    self.lablL12.text = @"";
    
    
    //Get data for the current user
    [self checkInternetConnectionAndMakeGetRequest];
}



- (IBAction)signoutPressed:(UIButton *)sender {
    
    //Logout the user
    [self doLogoutUser:0]; //Logout and don't come-back
    
    //Navigate to the login form.
    //[self performSegueWithIdentifier:@"unwindToProfileLoginVC" sender:self]; //Navigate to the login form.
}


-(BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender {
    return NO;
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    
    if ([segue.identifier isEqualToString:@"ProfileEditProfileLoginSegue"]) {
        ProfileLoginVC *plvc = segue.destinationViewController;
        plvc.selfDismiss = YES;
    }
    
}

#pragma mark - "get information for the user profile"

-(void)checkInternetConnectionAndMakeGetRequest {
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    //Check the internet connection
    NSURLSession *connTestSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:self delegateQueue:[NSOperationQueue mainQueue] ];
    NSURLSessionDataTask * sessionDataTask = [connTestSession dataTaskWithURL:[NSURL URLWithString:@kCONN_URL_CHECK] completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        
        //Check connection is available or not
        if ((error == nil) && data) {
            
            self.butnEdit.hidden = NO;
            
            //Create the request.
            [self callAsynPostUserGet: [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", @kCONN_URL_BASE, @kCONN_URL_PROFILE_GET] ]];
        }
        else {
            
            self.butnEdit.hidden = YES;
            
            //Popup message
            [self popdNetworkFailed:@kMSG_INTERNET_NO_TITLE1 message:@kMSG_INTERNET_NO_PARA1 button:@kMSG_COMMON_OK];
        }
    }];
    
    [sessionDataTask resume];
    
}


//Make a post call to server to get user data
-(void)callAsynPostUserGet: (NSURL *)url {

    NSError *errorJson;
 
    //Creating the json
    NSDictionary *jsonDictionary = [NSDictionary dictionaryWithObjectsAndKeys:
                                    
                                    self.username, @"user",
                                    self.timeStamp, @"time_stamp",
                                    self.uudid, @"uudid",
                                    
                                    nil];
    
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:jsonDictionary options:NSJSONWritingPrettyPrinted error:&errorJson];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];

    
    if (!errorJson) {
        
        NSData *postData=[jsonString dataUsingEncoding:NSUTF8StringEncoding];
        [self doProcessNetworkGetRequest:url data:postData flag:0];
    }

}

-(void)doProcessNetworkGetRequest:(NSURL *)url data:(NSData*)postData flag:(int)processFlag {
    
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
                
                [self doProcessGetResponse:strData flag:processFlag];
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
-(void)doProcessGetResponse:(NSString *)reponseString flag:(int)processFlag {
    
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
                NSString *keyStatus;
                
                //Manage empty or error key
                if (!serverResponseArray || !serverResponseArray.count){
                    keyStatus = @"";
                } else {
                    keyStatus = [serverResponseArray objectAtIndex:0];
                    keyStatus = [keyStatus stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]]; //Trim the string
                }
                
                if ([keyStatus isEqual:[NSNull null]]) {
                    //empty response
                    [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
                    return;
                    
                }
                else if ([keyStatus caseInsensitiveCompare:@"profiledata"] == NSOrderedSame ) {
                    
                    //Populate the views
                    
                    self.txtfName.text = [[serverResponseArray objectAtIndex:1] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
                    self.txtfEmail.text = [[serverResponseArray objectAtIndex:2] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
                    self.txtfContactNo.text = [[serverResponseArray objectAtIndex:3] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
                    
                    self.nearestCity = [[serverResponseArray objectAtIndex:5] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
                    self.preferredLanguage = [[serverResponseArray objectAtIndex:6] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
                    
                    [self.butnResidence setTitle:[NSString stringWithFormat:@"  %@", self.nearestCity] forState:UIControlStateNormal];
                    [self.butnLanguage setTitle:[NSString stringWithFormat:@"  %@", self.preferredLanguage] forState:UIControlStateNormal];
                }
                else {
                    //NSLog(@"other response");
                    [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
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
            [self showPopupReAuthenticateDialog:@kMSG_RESPONSE_REAUTHENTICATION_TITLE2 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA2 button:@kMSG_COMMON_OK flag:processFlag];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_required"] == NSOrderedSame ) {
            //redirect to login page
            [self showPopupReAuthenticateDialog:@kMSG_RESPONSE_REAUTHENTICATION_TITLE1 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA1 button:@kMSG_COMMON_OK flag:processFlag];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_blocked"] == NSOrderedSame ) {
            //redirect to login page
            [self showPopupReAuthenticateDialog:@kMSG_RESPONSE_REAUTHENTICATION_TITLE3 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA3 button:@kMSG_COMMON_OK flag:processFlag];
            
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





#pragma mark - "update information for the user profile"

-(void)checkInternetConnectionAndMakeUpdateRequest {
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    //Check the internet connection
    NSURLSession *connTestSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:self delegateQueue:[NSOperationQueue mainQueue] ];
    NSURLSessionDataTask * sessionDataTask = [connTestSession dataTaskWithURL:[NSURL URLWithString:@kCONN_URL_CHECK] completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        
        //Check connection is available or not
        if ((error == nil) && data) {
            
            //Create the request.
            [self callAsynPostUserUpdate: [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", @kCONN_URL_BASE, @kCONN_URL_PROFILE_EDIT] ]];
        }
        else {
            
            //Popup message
            [self popdNetworkFailed:@kMSG_INTERNET_NO_TITLE1 message:@kMSG_INTERNET_NO_PARA1 button:@kMSG_COMMON_OK];
        }
    }];
    
    [sessionDataTask resume];
    
}


//Make a post call to server to login
-(void)callAsynPostUserUpdate: (NSURL *)url {

    //Get the saved parameters for the user
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    self.username = [userDefaults stringForKey:@kLOGGED_USER_NAME];
    self.timeStamp = [userDefaults stringForKey:@kLOGGED_USER_TOKEN];
    self.uudid = [userDefaults stringForKey:@kLOGGED_USER_UUDID];
    
    
    NSError *errorJson;
    
    //Creating the json
    NSDictionary *jsonDictionary;
    
    if ( (self.txtfNewPassword.text != nil) && (self.txtfNewPassword.text.length>0) ) { //password length is validated previously

        jsonDictionary = [NSDictionary dictionaryWithObjectsAndKeys:
           
                           self.txtfName.text, @"name",
                           self.txtfEmail.text, @"email",
                           self.txtfContactNo.text, @"p_mobile",
                           self.nearestCity, @"residence",
                           self.preferredLanguage, @"language",
                           [GlobalVariableAndMethod md5HexDigest:self.txtfCurrentPassword.text], @"password",
                           [GlobalVariableAndMethod md5HexDigest:self.txtfNewPassword.text], @"newpassword",
                           
                           self.username, @"user",
                           self.timeStamp, @"time_stamp",
                           self.uudid, @"uudid",
                           
                           nil];
        
    } else {

        jsonDictionary = [NSDictionary dictionaryWithObjectsAndKeys:
                          
                          self.txtfName.text, @"name",
                          self.txtfEmail.text, @"email",
                          self.txtfContactNo.text, @"p_mobile",
                          self.nearestCity, @"residence",
                          self.preferredLanguage, @"language",
                          [GlobalVariableAndMethod md5HexDigest:self.txtfCurrentPassword.text], @"password",
                          @"", @"newpassword",
                          
                          self.username, @"user",
                          self.timeStamp, @"time_stamp",
                          self.uudid, @"uudid",
                          
                          nil];
    }
    

    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:jsonDictionary options:NSJSONWritingPrettyPrinted error:&errorJson];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];

    
    if (!errorJson) {
        
        NSData *postData=[jsonString dataUsingEncoding:NSUTF8StringEncoding];
        [self doProcessNetworkUpdateRequest:url data:postData flag:1];
    }
    
}


-(void)doProcessNetworkUpdateRequest:(NSURL *)url data:(NSData*)postData flag:(int)processFlag {
    
    
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
        
        //Remove passwords
        self.txtfCurrentPassword.text = @"";
        if (!self.txtfNewPassword.isHidden) {
            self.txtfNewPassword.text = @"";
            self.txtfRetypeNewPassword.text = @"";
        }
        
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
                else if ([keyStatus caseInsensitiveCompare:@"edited"] == NSOrderedSame ) {
                    
                    //Take action based on response
                    if ([valueStatus caseInsensitiveCompare:@"Invalid Password"] == NSOrderedSame) {
                        //Invalid password
                        [self popdRequestFailed:@kMSG_LOGINUSER_ERROR_TITLE1 message:@kMSG_LOGINUSER_ERROR_PARA2 button:@kMSG_COMMON_OK];
                    } else if ([valueStatus caseInsensitiveCompare:@"success"] == NSOrderedSame) {
                        //Success
                        NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
                        [userDefaults setObject:self.preferredLanguage forKey:@kLOGGED_USER_LANGUAGE];
                        [userDefaults synchronize];
                        [self showPopupSuccessDialog:@kMSG_UPDATEUSER_OK_TITLE1 message:@kMSG_UPDATEUSER_OK_PARA1 button:@kMSG_COMMON_OK];
                        [self presetUItoDefaulf];
                        
                    } else {
                        //Other response
                        [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
                    }

                }
                else {
                    //NSLog(@"other response");
                    [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
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
            [self showPopupReAuthenticateDialog:@kMSG_RESPONSE_REAUTHENTICATION_TITLE2 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA2 button:@kMSG_COMMON_OK flag:processFlag];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_required"] == NSOrderedSame ) {
            //redirect to login page
            [self showPopupReAuthenticateDialog:@kMSG_RESPONSE_REAUTHENTICATION_TITLE1 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA1 button:@kMSG_COMMON_OK flag:processFlag];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_blocked"] == NSOrderedSame ) {
            //redirect to login page
            [self showPopupReAuthenticateDialog:@kMSG_RESPONSE_REAUTHENTICATION_TITLE3 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA3 button:@kMSG_COMMON_OK flag:processFlag];
            
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
    
    //Note: User validation is bottom up. So the popup will show the top-most validation error.
    NSString *errorTitle = @"", *errorText = @"";
    
    
    if (!self.txtfNewPassword.isHidden) {
        
        //Retype password
        if ( ![self.txtfNewPassword.text isEqual:self.txtfRetypeNewPassword.text] ) {
            
            errorTitle = @"Password mismatch";
            errorText = @"Your new password and retype new password do not match.";
            self.txtfRetypeNewPassword.rightViewMode = UITextFieldViewModeAlways;
            self.txtfRetypeNewPassword.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
            [self.txtfRetypeNewPassword becomeFirstResponder];
        }
        else {
            self.txtfRetypeNewPassword.rightViewMode = UITextFieldViewModeNever;
        }
        
        //Password
        if ( ![GlobalVariableAndMethod isUserTextValied:self.txtfNewPassword.text] ) {
            
            errorTitle = @"Invalid input";
            errorText = [NSString stringWithFormat:@"New Password is too short (at least %d characters)", kVALIDATE_MIN_TEXT_SIZE]; //@"Password is too short";
            self.txtfNewPassword.rightViewMode = UITextFieldViewModeAlways;
            self.txtfNewPassword.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
            [self.txtfNewPassword becomeFirstResponder];
        }
        else {
            self.txtfNewPassword.rightViewMode = UITextFieldViewModeNever;
        }
        
    }
    
    //Password
    if ( ![GlobalVariableAndMethod isUserTextValied:self.txtfCurrentPassword.text] ) {
        
        errorTitle = @"Invalid input";
        errorText = [NSString stringWithFormat:@"Password is too short (at least %d characters)", kVALIDATE_MIN_TEXT_SIZE]; //@"Password is too short";
        self.txtfCurrentPassword.rightViewMode = UITextFieldViewModeAlways;
        self.txtfCurrentPassword.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
        [self.txtfCurrentPassword becomeFirstResponder];
    }
    else {
        self.txtfCurrentPassword.rightViewMode = UITextFieldViewModeNever;
    }

    //contact
    if ( [self.txtfContactNo.text isEqual:[NSNull null]] || !(self.txtfContactNo.text.length>=10)) {
        
        errorTitle = @"Invalid input";
        errorText = @"Contact number is too short \n(should be 10 characters)";
        self.txtfContactNo.rightViewMode = UITextFieldViewModeAlways;
        self.txtfContactNo.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
        [self.txtfContactNo becomeFirstResponder];
    }
    else {
        self.txtfContactNo.rightViewMode = UITextFieldViewModeNever;
    }
    
    //email
    if ( ![GlobalVariableAndMethod isUserTextValied:self.txtfEmail.text] ) {
        
        errorTitle = @"Invalid input";
        errorText = [NSString stringWithFormat:@"Email is too short \n(more than %d characters)", kVALIDATE_MIN_TEXT_SIZE];
        self.txtfEmail.rightViewMode = UITextFieldViewModeAlways;
        self.txtfEmail.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
        [self.txtfEmail becomeFirstResponder];
    }
    else {
        
        NSString *emailRegex = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
        if ([emailTest evaluateWithObject:self.txtfEmail.text]) {
            self.txtfEmail.rightViewMode = UITextFieldViewModeNever;
        } else {
            errorTitle = @"Invalid input";
            errorText = @"Incorrect email address";
            self.txtfEmail.rightViewMode = UITextFieldViewModeAlways;
            self.txtfEmail.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
            [self.txtfEmail becomeFirstResponder];
        }
    }
    
    //name
    if ( ![GlobalVariableAndMethod isUserTextValied:self.txtfName.text] ) {
        
        errorTitle = @"Invalid input";
        errorText = [NSString stringWithFormat:@"Your name is too short \n(more than %d characters)", kVALIDATE_MIN_TEXT_SIZE];
        self.txtfName.rightViewMode = UITextFieldViewModeAlways;
        self.txtfName.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
        [self.txtfName becomeFirstResponder];
    }
    else {
        self.txtfName.rightViewMode = UITextFieldViewModeNever;
    }
    
    
    if ( ![errorTitle isEqual:[NSNull null]] && (errorTitle.length>1) ) {
        [self showPopupDialog:errorTitle message:errorText button:@"OK"];
        return FALSE;
    }
    else {
        return TRUE;
    }
    
}

//Filter out unwanted characters and lengths based on the text-field
-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    
    if (textField == self.txtfName) {
        //Setting max character length for the string
        if(range.length + range.location > textField.text.length) { // Prevent crashing undo bug
            return NO;
        }
        NSUInteger newLength = [textField.text length] + [string length] - range.length;
        if (newLength > 200) {
            return NO;
        }
        
        //Set the valid input character pattern
        self.filteredCharacterSet = [ [string componentsSeparatedByCharactersInSet:[[NSCharacterSet characterSetWithCharactersInString:kVALIDE_INPUT_VALUES_NAME] invertedSet] ] componentsJoinedByString:@""];
        return [string isEqualToString:self.filteredCharacterSet];
    }
    else if (textField == self.txtfEmail) {
        //Setting max character length for the string
        if(range.length + range.location > textField.text.length) { // Prevent crashing undo bug
            return NO;
        }
        NSUInteger newLength = [textField.text length] + [string length] - range.length;
        if (newLength > 100) {
            return NO;
        }
        
        //Set the valid input character pattern
        self.filteredCharacterSet = [ [string componentsSeparatedByCharactersInSet:[[NSCharacterSet characterSetWithCharactersInString:kVALIDE_INPUT_VALUES_EMAIL] invertedSet] ] componentsJoinedByString:@""];
        return [string isEqualToString:self.filteredCharacterSet];
    }
    else if (textField == self.txtfContactNo) {
        //Setting max character length for the string
        if(range.length + range.location > textField.text.length) { // Prevent crashing undo bug
            return NO;
        }
        NSUInteger newLength = [textField.text length] + [string length] - range.length;
        if (newLength > 10) {
            return NO;
        }
        
        //Set the valid input character pattern
        self.filteredCharacterSet = [ [string componentsSeparatedByCharactersInSet:[[NSCharacterSet characterSetWithCharactersInString:kVALIDE_INPUT_VALUES_NUMBERS] invertedSet] ] componentsJoinedByString:@""];
        return [string isEqualToString:self.filteredCharacterSet];
    }
    else {
        return YES;
    }
    
}

- (IBAction)residenceButtonPressed:(UIButton *)sender {
    
    [self hideKeyBoard];
    
 
    NSArray *cityArray = [kARRAY_RESIDENCES componentsSeparatedByString:@", "];
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Select the nearest city" message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    //Handle the pop-up menu options
    for (NSString *title in cityArray) {
        UIAlertAction *alertAction = [UIAlertAction actionWithTitle:title style:UIAlertActionStyleDefault handler:^(UIAlertAction * action)
                                      {
                                          self.nearestCity = title;
                                          [self.butnResidence setTitle:[NSString stringWithFormat:@"  %@", self.nearestCity] forState:UIControlStateNormal];
                                      }];
        [alertController addAction:alertAction];
    }
    
    //for Cancel
    UIAlertAction *alertCancelAction = [UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:nil];
    [alertController addAction:alertCancelAction];

    [self presentViewController:alertController animated:YES completion:nil];

}


- (IBAction)languageButtonPressed:(UIButton *)sender {
    
    [self hideKeyBoard];
    
    
    NSArray *languageArray = [kARRAY_LANGUAGES componentsSeparatedByString:@", "];
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Select the preferred language" message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    //Handle the pop-up menu options
    for (NSString *title in languageArray) {
        UIAlertAction *alertAction = [UIAlertAction actionWithTitle:title style:UIAlertActionStyleDefault handler:^(UIAlertAction * action)
                                      {
                                          self.preferredLanguage = title;
                                          [self.butnLanguage setTitle:[NSString stringWithFormat:@"  %@", self.preferredLanguage] forState:UIControlStateNormal];
                                      }];
        [alertController addAction:alertAction];
    }
    
    //for Cancel
    UIAlertAction *alertCancelAction = [UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:nil];
    [alertController addAction:alertCancelAction];
    
    [self presentViewController:alertController animated:YES completion:nil];

}


- (IBAction)editPressed:(UIButton *)sender {
    
    if (!self.isEdited) {
        
        self.uiswContent.userInteractionEnabled = YES;
        
        [sender setTitle:@"save" forState:UIControlStateNormal];
        
        [self editContent:sender];

        self.isEdited = YES;
    } else {
        [self saveContent:sender];
    }

}

-(void)editContent:(UIButton *)sender  {
    
    self.txtfName.userInteractionEnabled = YES;
    self.txtfEmail.userInteractionEnabled = YES;
    self.txtfContactNo.userInteractionEnabled = YES;
    self.butnResidence.userInteractionEnabled = YES;
    self.butnLanguage.userInteractionEnabled = YES;
    
    self.txtfCurrentPassword.hidden = NO;

    self.uivwSignOut.hidden = YES;
    self.uivwChangePassword.hidden = NO;
    self.lablL7.hidden = YES;
    
    self.txtfName.textColor = [UIColor blackColor];
    self.txtfEmail.textColor = [UIColor blackColor];
    self.txtfContactNo.textColor = [UIColor blackColor];
    self.txtfCurrentPassword.textColor = [UIColor blackColor];
    self.txtfNewPassword.textColor = [UIColor blackColor];
    self.txtfRetypeNewPassword.textColor = [UIColor blackColor];
    [self.butnResidence setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [self.butnLanguage setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    
}

- (void)saveContent:(UIButton *)sender {
    
    //Validate user input and make the registration-request
    if([self validateUserInputs]) {
        [self checkInternetConnectionAndMakeUpdateRequest];
    }
}

-(void)presetUItoDefaulf {
    
    //Preset ui items
    self.txtfName.userInteractionEnabled = NO;
    self.txtfEmail.userInteractionEnabled = NO;
    self.txtfContactNo.userInteractionEnabled = NO;
    self.butnResidence.userInteractionEnabled = NO;
    self.butnLanguage.userInteractionEnabled = NO;
    
    self.txtfCurrentPassword.hidden = YES;
    self.txtfNewPassword.hidden = YES;
    self.txtfRetypeNewPassword.hidden = YES;
    
    self.uivwChangePassword.hidden = YES;
    
    self.actvUpdate.userInteractionEnabled = NO;
    self.actvUpdate.hidden = YES;
    
    [self.butnEdit setTitle:@"edit" forState:UIControlStateNormal];
    self.isEdited = NO;
    [self hideKeyBoard];

}

- (IBAction)changePasswordPressed:(UIButton *)sender {
    
    self.txtfNewPassword.hidden = NO;
    self.txtfRetypeNewPassword.hidden = NO;
    
    self.uivwChangePassword.hidden = YES;
}



#pragma mark - "common functions"


- (IBAction)backPressed:(UIButton *)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}


//--------- Popup messages ----------
-(void)popdRequestFailed:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    [self showPopupDialog:byTitle message:byMessageText button:byButtonText];
}

-(void)popdNetworkORServerFailed:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    [self showPopupDialog:byTitle message:byMessageText button:byButtonText];
}

-(void)popdNetworkFailed:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    [self showPopupDialog:byTitle message:byMessageText button:byButtonText];
}

-(void)showPopupDialog:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:byTitle message:byMessageText preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *alertAction = [UIAlertAction actionWithTitle:byButtonText style:UIAlertActionStyleDefault handler:nil];
    
    [alertController addAction:alertAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

-(void)showPopupSuccessDialog:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:byTitle message:byMessageText preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *alertAction = [UIAlertAction actionWithTitle:byButtonText style:UIAlertActionStyleDefault handler:^(UIAlertAction * action)
                                  {
                                      //self.uiswContent.userInteractionEnabled = NO;
                                      //[self hideKeyBoard];
                                      
                                  }];
    
    [alertController addAction:alertAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}



-(void)showPopupReAuthenticateDialog:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText flag:(BOOL)flag{
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:byTitle message:byMessageText preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *alertAction = [UIAlertAction actionWithTitle:byButtonText style:UIAlertActionStyleDefault handler:^(UIAlertAction * action)
                                  {
                                      [self doLogoutUser:flag];
                                  }];
    
    [alertController addAction:alertAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}


-(void)doLogoutUser:(BOOL)flag{
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    //Set user defaults for this session
    [userDefaults setBool:NO forKey:@kIS_USER_LOGGED];
    [userDefaults setObject:@"" forKey:@kLOGGED_USER_NAME];
    [userDefaults setObject:@"" forKey:@kLOGGED_USER_TOKEN];
    [userDefaults setObject:@""forKey:@kLOGGED_USER_UUDID];
    [userDefaults setObject:@"" forKey:@kLOGGED_USER_LANGUAGE];
    [userDefaults synchronize];
    
    if (flag == 0) {
        //Getting data to populate form
        [self performSegueWithIdentifier:@"unwindToProfileLoginVC" sender:self]; //Navigate to the login form.
    } else {
        //Saving updated data
        [self performSegueWithIdentifier:@"ProfileEditProfileLoginSegue" sender:nil];
    }
    
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
    
    [self hideKeyBoard];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    
    [textField resignFirstResponder];
    return NO;
}

-(void)hideKeyBoard {
    
    [self.uiswContent setContentOffset:CGPointZero animated:YES];
    
    [self.txtfName resignFirstResponder];
    [self.txtfEmail resignFirstResponder];
    [self.txtfContactNo resignFirstResponder];
    [self.txtfCurrentPassword resignFirstResponder];
    [self.txtfNewPassword resignFirstResponder];
    [self.txtfRetypeNewPassword resignFirstResponder];
}


@end
