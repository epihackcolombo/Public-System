//
//  UIProfileRecoverVC.m
//  mobuzz.ios.general
//
//  Created by Vajira on 12/10/15.
//  Copyright © 2015 cosmic. All rights reserved.
//

#import "ProfileRecoverVC.h"
#import "GlobalVariableAndMethod.h"
#import "PopupMessages.h"

@interface ProfileRecoverVC ()

@property (strong, nonatomic) IBOutlet UITextField *txtfUsername;
@property (strong, nonatomic) IBOutlet UITextField *txtfEmail;

@property (strong, nonatomic) IBOutlet UILabel *lablL1;

@property (strong, nonatomic) IBOutlet UIButton *butnBack;
@property (strong, nonatomic) IBOutlet UIButton *butnReset;
@property (strong, nonatomic) IBOutlet UIActivityIndicatorView *actvUpdate;

@end


//Reset user's password to username
@implementation ProfileRecoverVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.butnBack.layer.borderWidth = 1.0f;
    self.butnReset.layer.borderWidth = 1.0f;
    
    self.butnBack.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"ash"]CGColor];
    self.butnReset.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"yellow"]CGColor];

    self.lablL1.text = @"";
}


- (IBAction)backPressed:(UIButton *)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)resetPressed:(UIButton *)sender {
    
    if ([self validateUserInputs]) {
        [self checkInternetConnectionAndMakeGetRequest];
    }
}


#pragma mark - "reset the user profile"

-(void)checkInternetConnectionAndMakeGetRequest {
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    //Check the internet connection
    NSURLSession *connTestSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:self delegateQueue:[NSOperationQueue mainQueue] ];
    NSURLSessionDataTask * sessionDataTask = [connTestSession dataTaskWithURL:[NSURL URLWithString:@kCONN_URL_CHECK] completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        
        //Check connection is available or not
        if ((error == nil) && data) {
            
            //Create the request.
            [self callAsynPostUserGet: [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", @kCONN_URL_BASE, @kCONN_URL_PROFILE_RECOVER] ]];
        }
        else {
            
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
                                    
                                    self.txtfUsername.text, @"user",
                                    [GlobalVariableAndMethod md5HexDigest:self.txtfUsername.text], @"h_user",
                                    self.txtfEmail.text, @"email",
                                    
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


//---------- Validating inputs ----------

-(BOOL) validateUserInputs {
    
    //Note: User validation is bottom up. So the popup will show the top-most validation error.
    NSString *errorTitle = @"", *errorText = @"";
    
    //email
    if ( ![GlobalVariableAndMethod isUserTextValied:self.txtfEmail.text] ) {
        
        errorTitle = @"Invalid input";
        errorText = [NSString stringWithFormat:@"Email is too short \n(more than %d characters)", kVALIDATE_MIN_TEXT_SIZE];
        self.txtfEmail.rightViewMode = UITextFieldViewModeAlways;
        self.txtfEmail.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
        [self.txtfEmail becomeFirstResponder];
    }
    else {
        self.txtfEmail.rightViewMode = UITextFieldViewModeNever;
    }
    
    //name
    if ( ![GlobalVariableAndMethod isUserTextValied:self.txtfUsername.text] ) {
        
        errorTitle = @"Invalid input";
        errorText = [NSString stringWithFormat:@"Your username is too short \n(more than %d characters)", kVALIDATE_MIN_TEXT_SIZE];
        self.txtfUsername.rightViewMode = UITextFieldViewModeAlways;
        self.txtfUsername.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
        [self.txtfUsername becomeFirstResponder];
    }
    else {
        self.txtfUsername.rightViewMode = UITextFieldViewModeNever;
    }
    
    
    if ( ![errorTitle isEqual:[NSNull null]] && (errorTitle.length>1) ) {
        [self showPopupDialog:errorTitle message:errorText button:@"OK"];
        return FALSE;
    }
    else {
        return TRUE;
    }
    
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
                else if ([keyStatus caseInsensitiveCompare:@"ok"] == NSOrderedSame ) {
                    
                    [self showPopupSuccessDialog:@kMSG_RESETPASSWORD_OK_TITLE1 message:@kMSG_RESETPASSWORD_OK_PARA1 button:@kMSG_COMMON_OK];
                    [self doResetView];
                }
                else if ([keyStatus caseInsensitiveCompare:@"no"] == NSOrderedSame ) {
                    //NSLog(@"wrong username or email");
                    [self popdRequestFailed:@kMSG_RESETPASSWORD_ERROR_TITLE1 message:@kMSG_RESETPASSWORD_ERROR_PARA1 button:@kMSG_COMMON_OK];
                    [self doResetView];
                }
                else {
                    //NSLog(@"other response");
                    [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
                    [self doResetView];
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
                                      [self dismissViewControllerAnimated:YES completion:nil];
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

-(void)doResetView {
    self.txtfUsername.text = @"";
    self.txtfEmail.text = @"";
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


//---------- Managing the keyboard ----------




//---------- Managing the keyboard ----------
#pragma mark Managing_the_keyboard

//Shift text fields in order to stop keyboard cover them


-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    
    [self.view endEditing:YES];
}

- (void)textFieldDidBeginEditing:(UITextField *)textField {
    
    [self animateTextField: textField up: YES];
}


- (void)textFieldDidEndEditing:(UITextField *)textField {
    
    [self animateTextField: textField up: NO];
}

- (void) animateTextField: (UITextField*) textField up: (BOOL) up {
    
    const int movementDistance = 80; // tweak as needed
    const float movementDuration = 0.3f; // tweak as needed
    
    int movement = (up ? -movementDistance : movementDistance);
    
    [UIView beginAnimations: @"anim" context: nil];
    [UIView setAnimationBeginsFromCurrentState: YES];
    [UIView setAnimationDuration: movementDuration];
    self.view.frame = CGRectOffset(self.view.frame, 0, movement);
    [UIView commitAnimations];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return NO;
}

-(void)hideKeyBoard {
    [self.txtfUsername resignFirstResponder];
    [self.txtfEmail resignFirstResponder];
}



@end
