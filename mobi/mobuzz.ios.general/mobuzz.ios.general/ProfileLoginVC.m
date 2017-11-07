//
//  profileLoginVC.m
//  mobuzz.ios.general
//
//  Created by Vajira on 27/7/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import "ProfileLoginVC.h"
#import "GlobalVariableAndMethod.h"
#import "PopupMessages.h"

@interface ProfileLoginVC ()
@property (strong, nonatomic) IBOutlet UITextField *tfldUsername;
@property (strong, nonatomic) IBOutlet UITextField *tfldfPassword;
@property (strong, nonatomic) IBOutlet UIButton *butnSignin;
@property (strong, nonatomic) IBOutlet UIButton *butnWSignin;
@property (strong, nonatomic) IBOutlet UIButton *butnWOSignin;
@property (strong, nonatomic) IBOutlet UIActivityIndicatorView *actvLogin;
@property (strong, nonatomic) IBOutlet UIView *uivwLogin;
@property (strong, nonatomic) IBOutlet UILabel *lablL1;
@property (strong, nonatomic) IBOutlet UILabel *lablL2;
@property (strong, nonatomic) IBOutlet UILabel *lablL3;
@property (strong, nonatomic) IBOutlet UILabel *lablL4;

@property (strong, nonatomic) IBOutlet UIView *uivwSignin;
@property (strong, nonatomic) IBOutlet UIView *uivwOtherOptions;


@property NSUserDefaults *userDefaults;
@property NSString *stringLoggedTimeStamp, *uudid;

@end


//Login and create session if necessary
@implementation ProfileLoginVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.lablL1.text = @" ";
    self.lablL2.text = @" ";
    self.lablL3.text = @" ";
    self.lablL4.text = @" ";
    
    //Button styles
    self.butnSignin.layer.borderWidth = 1.0f;
    self.butnSignin.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"yellow"] CGColor];
    
    self.butnWSignin.layer.borderWidth = 1.0f;
    self.butnWSignin.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"yellow"] CGColor];
    
    self.butnWOSignin.layer.borderWidth = 1.0f;
    self.butnWOSignin.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"ash"] CGColor];

    //Hiding sign-in, register and password-recovery information
    self.tfldUsername.hidden = YES;
    self.tfldfPassword.hidden = YES;
    self.uivwSignin.hidden = YES;
    self.uivwOtherOptions.hidden = YES;
    self.lablL2.hidden = YES;
    self.lablL3.hidden = YES;
}


-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];

    //Auto login if user didn't logout last time
    self.userDefaults = [NSUserDefaults standardUserDefaults];
    bool isLogged = [self.userDefaults boolForKey:@kIS_USER_LOGGED];
    
    if (isLogged) {
        //NSLog(@"Already logged");
        [self performSegueWithIdentifier:@"signinSucessSegue" sender:nil];
    } else {
        //NSLog(@"Not logged in");
        //Nothing to do
    }

}

-(void)viewDidDisappear:(BOOL)animated{
    //Resetting view before appearing again
    
    //Hiding sign-in, register and password-recovery information
    self.tfldUsername.hidden = YES;
    self.tfldfPassword.hidden = YES;
    self.uivwSignin.hidden = YES;
    self.uivwOtherOptions.hidden = YES;
    self.lablL2.hidden = YES;
    self.lablL3.hidden = YES;
    
    //Show use without sign in
    self.butnWSignin.hidden = NO;
    self.butnWOSignin.hidden = NO;
    self.lablL4.hidden = NO;
    
}

- (BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender {
 
    return NO;
}

- (IBAction)withSigninPressed:(id)sender {
    
    //Show sign-in, register and password-recovery information
    self.tfldUsername.hidden = NO;
    self.tfldfPassword.hidden = NO;
    self.uivwSignin.hidden = NO;
    self.uivwOtherOptions.hidden = NO;
    self.lablL1.hidden = NO;
    self.lablL2.hidden = NO;
    self.lablL3.hidden = NO;
    
    //Hiding use without sign in
    self.butnWSignin.hidden = YES;
    self.butnWOSignin.hidden = YES;
    self.lablL4.hidden = YES;
}

- (IBAction)withoutSigninPressed:(id)sender {
    
    //Firing segue to use app without login
    [self performSegueWithIdentifier:@"signinSucessSegue" sender:nil];
}

- (IBAction)signinPressed:(id)sender {
    
    if ( [self validateUserInputsByUsername:self.tfldUsername.text byPassword:self.tfldfPassword.text] ) {
        
        [self checkInternetConnectionAndMakeRequest:0];
    }
}

- (IBAction)RecoverPressed:(UIButton *)sender {
    
    [self checkInternetConnectionAndMakeRequest:1];
}

- (IBAction)registerPressed:(UIButton *)sender {
    
    [self checkInternetConnectionAndMakeRequest:2];
}





-(void)checkInternetConnectionAndMakeRequest:(NSInteger)flag {

    switch (flag) {
        case 0:
            //Mark the start of network call
            [self.actvLogin startAnimating];
            break;

        default:
            [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
            break;
    }
    self.uivwLogin.userInteractionEnabled = NO; // To stop multiple clicks
    
    //Check the internet connection
    NSURLSession *connTestSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:self delegateQueue:[NSOperationQueue mainQueue] ];
    NSURLSessionDataTask * sessionDataTask = [connTestSession dataTaskWithURL:[NSURL URLWithString:@kCONN_URL_CHECK] completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {

        //Mark the end of network call
        [self.actvLogin stopAnimating];
        self.uivwLogin.userInteractionEnabled = YES;
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        
        //Check connection is available or not
        if ((error == nil) && data) {
            
            switch (flag) {
                case 0:
                    //Create a login request
                    [self callAsynPostUserLogin: [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", @kCONN_URL_BASE, @kCONN_URL_PROFILE_LOGIN] ]];
                    break;
                    
                case 1:
                    [self performSegueWithIdentifier:@"ProfileRecoverSegue" sender:nil];
                    break;
                    
                case 2:
                    [self performSegueWithIdentifier:@"ProfileRegisterSegue" sender:nil];
                    break;
                    
                default:
                    break;
            }
            
        }
        else {
            
            //Popup message
            [self popdNetworkFailed:@kMSG_INTERNET_NO_TITLE1 message:@kMSG_INTERNET_NO_PARA1 button:@kMSG_COMMON_OK];
        }
    }];

    [sessionDataTask resume];
 
}






//Make a post call to server to login
-(void)callAsynPostUserLogin: (NSURL *)url {
    
    NSError *errorJson;
    
    //Get the date-time
    self.stringLoggedTimeStamp = [GlobalVariableAndMethod getCurrentDateTime:@kDATE_TIME_FORMAT];
    self.uudid = [GlobalVariableAndMethod getUUID];
    
    //Creating the json
    NSDictionary *jsonDictionary = [NSDictionary dictionaryWithObjectsAndKeys:
                                    self.tfldUsername.text, @"user",
                                    [GlobalVariableAndMethod md5HexDigest:self.tfldfPassword.text], @"password",
                                    self.uudid, @"uudid",
                                    @"iphone-carrier", @"imei",
                                    [NSString stringWithFormat:@"simOf-%@",[GlobalVariableAndMethod getDeviceName]], @"m_mobile",
                                    self.stringLoggedTimeStamp, @"time_stamp",
                                    nil];
    
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:jsonDictionary options:NSJSONWritingPrettyPrinted error:&errorJson];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];

    
    if (!errorJson) {
        
        NSData *postData=[jsonString dataUsingEncoding:NSUTF8StringEncoding];
        [self doProcessNetworkRequest:url data:postData flag:0];
    }
}


-(void)doProcessNetworkRequest:(NSURL *)url data:(NSData*)postData flag:(int)processFlag {
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
    [request setHTTPMethod:@"POST"];
    [request addValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [request setValue:[NSString stringWithFormat:@"%lu",(unsigned long)postData.length] forHTTPHeaderField:@"Content-Length"];
    [request setHTTPBody:postData];

    
    //Mark the start of network call
    [self.actvLogin startAnimating];
    self.uivwLogin.userInteractionEnabled = NO;
    
    NSURLSession *connSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:nil delegateQueue:[NSOperationQueue mainQueue]];
    NSURLSessionDataTask *sessionPostDataTask = [connSession dataTaskWithRequest:request completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable connectionError) {
        
        
        //Mark the end of network call
        [self.actvLogin stopAnimating];
        self.uivwLogin.userInteractionEnabled = YES;
        
        if(data) {
            
            NSInteger responseCode = [(NSHTTPURLResponse *)response statusCode];
            NSString *strData = [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding];

            
            if (!connectionError && responseCode == 200) {
                
                [self doProcessResponse:strData flag:processFlag];
            }
            else {
                [self popdRequestFailed:@kMSG_REQUEST_FAIL_TITLE1 message:@kMSG_REQUEST_FAIL_PARA1 button:@kMSG_COMMON_OK];
            }
            
        }
        else {
            [self popdNetworkORServerFailed:@kMSG_CONNECT_NO_TITLE1 message:@kMSG_CONNECT_NO_PARA1 button:@kMSG_COMMON_OK];
        }
        
        
        self.tfldUsername.text = @"";
        self.tfldfPassword.text = @"";
        
    }];
    [sessionPostDataTask resume];

}


//Process the response
-(void)doProcessResponse:(NSString *)reponseString flag:(int)processFlag {
    
    //validation block ------------------------
    //error_net_connection - no route to host, can't find an active connection
    //error_net_other - network time-out or other connector exceptions
    //error_db_params - inappropriate parameters with the request
    //error_db_connect - database connection issue at server side
    //authentication_required - user need to be authenticate
    //authentication_expired - user session has expired
    
    
    NSString *jsonString = reponseString;
    jsonString = [jsonString stringByReplacingOccurrencesOfString:@"'" withString:@"\""]; //Replacing character ' with " for json translation

    
    NSData *JSONdata = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *jsonError = nil;
    
    if (JSONdata != nil) {
        
        //this you need to know json root is NSDictionary or NSArray , you smaple is NSDictionary
        NSDictionary *dicJson = [NSJSONSerialization JSONObjectWithData:JSONdata options:0 error:&jsonError];
        
        
        if ( (jsonError != nil) || (dicJson == (NSDictionary *)[NSNull null]) ) {
            //Error in getting json. Not the expected outcome.
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
            return;
        }
        
        
        //Checking the values
        NSString *keyStatus = [dicJson objectForKey:@"status"];
        NSString *keyLanugage = [dicJson objectForKey:@"language"];
        
        if ([keyStatus isEqual:[NSNull null]]) {
            //empty response
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
            return;
            
        } else if ([keyStatus caseInsensitiveCompare:@"ok"] == NSOrderedSame ) {

            
            //Set user defaults for this session
            [self.userDefaults setBool:YES forKey:@kIS_USER_LOGGED];
            [self.userDefaults setObject:self.tfldUsername.text forKey:@kLOGGED_USER_NAME];
            [self.userDefaults setObject:self.stringLoggedTimeStamp forKey:@kLOGGED_USER_TOKEN];
            [self.userDefaults setObject:self.uudid forKey:@kLOGGED_USER_UUDID];
            [self.userDefaults setObject:keyLanugage forKey:@kLOGGED_USER_LANGUAGE];
            [self.userDefaults synchronize];

            
            if (self.selfDismiss) {

                //Dismiss current view
                [self dismissViewControllerAnimated:YES completion:nil];
            } else {

                //Firing custom segue
                [self performSegueWithIdentifier:@"signinSucessSegue" sender:nil];
            }
            
        } else if ([keyStatus caseInsensitiveCompare:@"error_db_params"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE2 message:@kMSG_RESPONSE_UNEXPECTED_PARA2 button:@kMSG_COMMON_OK];

        } else if ([keyStatus caseInsensitiveCompare:@"error_db_connect"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE2 message:@kMSG_RESPONSE_UNEXPECTED_PARA2 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"error_db_update"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_failed"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_LOGINUSER_ERROR_TITLE1 message:@kMSG_LOGINUSER_ERROR_PARA1 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_expired"] == NSOrderedSame ) {
            //redirect to login page
            [self popdRequestFailed:@kMSG_RESPONSE_REAUTHENTICATION_TITLE2 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA2 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_required"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_RESPONSE_REAUTHENTICATION_TITLE1 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA1 button:@kMSG_COMMON_OK];
  
        } else if ([keyStatus caseInsensitiveCompare:@"authentication_blocked"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_RESPONSE_REAUTHENTICATION_TITLE3 message:@kMSG_RESPONSE_REAUTHENTICATION_PARA3 button:@kMSG_COMMON_OK];

        } else {
            //This block is for the undifine response
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
        }
        
    }
    else {
        //NSLog(@"jsonError: %@", jsonError);
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

//Show popup dialog
-(void)showPopupDialog:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:byTitle message:byMessageText preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *alertAction = [UIAlertAction actionWithTitle:byButtonText style:UIAlertActionStyleDefault handler:nil];
    
    [alertController addAction:alertAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}


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

- (IBAction)bottemButtonsPressed:(UIButton *)sender {
    //To hide the keyboard
    [self.view endEditing:YES];
}



//---------- Validating inputs ----------


-(BOOL) validateUserInputsByUsername:(NSString*)username byPassword:(NSString*)password {

    
    if ( ![GlobalVariableAndMethod isUserTextValied:username] ) {
        
        [self showPopupDialog:@"Invalid input" message:@"Username is too short" button:@"OK"];
        return FALSE;
    }
    
    if ( ![GlobalVariableAndMethod isUserTextValied:password] ) {
        
        [self showPopupDialog:@"Invalid input" message:@"Password is too short" button:@"OK"];
        return FALSE;
    }
    
    return TRUE;
}


//---------- unwind the navigation to login ----------
-(IBAction)unwindToThisViewController:(UIStoryboardSegue *)segue
{

}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    /*if ([segue.identifier isEqualToString:@"signinSucessSegue"]) {
        
        //Check the user is signed or not
        self.userDefaults = [NSUserDefaults standardUserDefaults];
        bool isLogged = [self.userDefaults boolForKey:@kIS_USER_LOGGED];
        
        if (isLogged) {
            //Already logged in, enable full version. Nothing to do
        } else {
            //Not logged in
            UITabBarController *tbc = segue.destinationViewController;
            NSMutableArray *tbViewControllers = [NSMutableArray arrayWithArray:[tbc viewControllers]];
            [tbViewControllers removeObjectAtIndex:1];
            [tbc setViewControllers:tbViewControllers];
        }
    }*/
}

@end
