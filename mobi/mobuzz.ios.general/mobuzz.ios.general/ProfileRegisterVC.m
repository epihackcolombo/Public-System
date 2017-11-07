//
//  ProfileRegisterVC.m
//  mobuzz.ios.general
//
//  Created by Vajira on 29/7/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import "ProfileRegisterVC.h"
#import "GlobalVariableAndMethod.h"
#import "PopupMessages.h"

@interface ProfileRegisterVC ()

@property (strong, nonatomic) IBOutlet UIScrollView *scrollview;

@property (strong, nonatomic) IBOutlet UITextField *txtfName;
@property (strong, nonatomic) IBOutlet UITextField *txtfEmail;
@property (strong, nonatomic) IBOutlet UITextField *txtfContactNo;
@property (strong, nonatomic) IBOutlet UITextField *txtfAge;
@property (strong, nonatomic) IBOutlet UITextField *txtfUsername;
@property (strong, nonatomic) IBOutlet UITextField *txtfPassword;
@property (strong, nonatomic) IBOutlet UITextField *txtfRetypePassword;

@property (strong, nonatomic) IBOutlet UITextField *txtfResidnece;
@property (strong, nonatomic) IBOutlet UITextField *txtfLanguage;
@property (strong, nonatomic) IBOutlet UITextField *txtfGender;


@property (strong, nonatomic) IBOutlet UIActivityIndicatorView *actvRegister;
@property (strong, nonatomic) IBOutlet UIView *uivwRegister;

@property (strong, nonatomic) IBOutlet UIButton *butnResidence;
@property (strong, nonatomic) IBOutlet UIButton *butnLanguage;
@property (strong, nonatomic) IBOutlet UIButton *butnGender;

@property (strong, nonatomic) IBOutlet UIButton *butnBack;
@property (strong, nonatomic) IBOutlet UIButton *butnSave;

@property int keyboardOffSet;

@property NSString *filteredCharacterSet;
@property NSString *nearestCity, *preferredLanguage, *userGender;

@end


//Create a user account for the system
@implementation ProfileRegisterVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    self.keyboardOffSet = (int)kKEYBOARD_MOVE_OFFSET;
    
    //Manage touch gesture out side the text fields
    UITapGestureRecognizer * tapGesture = [[UITapGestureRecognizer alloc]
                                           initWithTarget:self
                                           action:@selector(hideKeyBoard)];
    
    [self.view addGestureRecognizer:tapGesture];
    [self.scrollview setContentOffset:CGPointZero animated:YES];
    
    self.txtfResidnece.userInteractionEnabled = NO;
    self.txtfLanguage.userInteractionEnabled = NO;
    self.txtfGender.userInteractionEnabled = NO;

    self.butnBack.layer.borderWidth = 1.0f;
    self.butnSave.layer.borderWidth = 1.0f;
    
    self.butnBack.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"ash"]CGColor];
    self.butnSave.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"yellow"]CGColor];
    
    //------------------------------
    self.nearestCity = @"";
    self.preferredLanguage = @"";
    self.userGender = @"";
    
}

- (IBAction)savePressed:(UIButton *)sender {
    
    //Validate user input and make the registration-request
    if([self validateUserInputs]) {
        [self checkInternetConnectionAndMakeRequest];
    }
}

- (IBAction)backPressed:(UIButton *)sender {
    
    //Navigate to the login form.
    [self dismissViewControllerAnimated:YES completion:nil];
}


-(void)checkInternetConnectionAndMakeRequest {
    
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    //Check the internet connection
    NSURLSession *connTestSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:self delegateQueue:[NSOperationQueue mainQueue]];
    NSURLSessionDataTask *sessionDataTask = [connTestSession dataTaskWithURL:[NSURL URLWithString:@kCONN_URL_CHECK] completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
        
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        
        //Check connection is available or not
        if ((error == nil) && data) {
            
            //Create the request.
            [self callAsynPostUserRegister: [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", @kCONN_URL_BASE, @kCONN_URL_PROFILE_REGISTER] ]];
        }
        else {
            
            //Popup message
            [self popdNetworkFailed:@kMSG_INTERNET_NO_TITLE1 message:@kMSG_INTERNET_NO_PARA1 button:@kMSG_COMMON_OK];
        }
        
    }];
    
    [sessionDataTask resume];
    
}


//Make a post call to server to login
-(void)callAsynPostUserRegister: (NSURL *)url {
    
    NSError *errorJson;
    
    //Creating the json
    NSDictionary *jsonDictionary = [NSDictionary dictionaryWithObjectsAndKeys:

                                    self.txtfName.text, @"name",
                                    self.txtfEmail.text, @"email",
                                    self.txtfContactNo.text, @"p_mobile",
                                    self.nearestCity, @"residence",
                                    self.preferredLanguage, @"language",
                                    self.userGender, @"gender",
                                    
                                    self.txtfUsername.text, @"user",
                                    [GlobalVariableAndMethod md5HexDigest:self.txtfPassword.text], @"password",
                                    
                                    self.txtfAge.text, @"age",
                                    
                                    @"iPhone-imei", @"imei",
                                    @"iPhone-carrier", @"mobile",
                                    [GlobalVariableAndMethod getUUID], @"uudid",
                                    [GlobalVariableAndMethod getCurrentDateTime:@kDATE_TIME_FORMAT], @"time_stamp",
                                    [NSString stringWithFormat:@"simOf-%@",[GlobalVariableAndMethod getDeviceName]], @"model",

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
    [self.actvRegister startAnimating];
    self.uivwRegister.userInteractionEnabled = NO;
    
    NSURLSession *connSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:nil delegateQueue:[NSOperationQueue mainQueue]];
    NSURLSessionDataTask *sessionPostDataTask = [connSession dataTaskWithRequest:request completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable connectionError) {

        //Mark the end of network call
        [self.actvRegister stopAnimating];
        self.uivwRegister.userInteractionEnabled = YES;
        
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

    //If the response is in json
    
    if (jsonString != nil) {
        
        //Note: response is not json, its comma seperated valus. Value before first comma should considered.
        NSArray *serverResponseArray = [jsonString componentsSeparatedByString:@","];
        NSString *keyStatus;
        
        if (!serverResponseArray || !serverResponseArray.count){
            keyStatus = @"";
        } else {
            keyStatus = [serverResponseArray objectAtIndex:0];
        }

        //Checking the values
        
        if ([keyStatus isEqual:[NSNull null]]) {
            //empty response
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
            return;
            
        } else if ([keyStatus caseInsensitiveCompare:@"ok"] == NSOrderedSame ) {
            
            //Popup message
            [self showPopupSuccessDialog:@kMSG_REGISTERUSER_OK_TITLE1 message:@kMSG_REGISTERUSER_OK_PARA1 button:@"OK"];
            
        } else if ([keyStatus caseInsensitiveCompare:@"error_db_params"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE2 message:@kMSG_RESPONSE_UNEXPECTED_PARA2 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"error_db_connect"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE2 message:@kMSG_RESPONSE_UNEXPECTED_PARA2 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"error_db_update"] == NSOrderedSame ) {
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
            
        } else if ([keyStatus caseInsensitiveCompare:@"username_exists_db"] == NSOrderedSame ) {
            self.txtfPassword.text = @"";
            self.txtfRetypePassword.text = @"";
            [self popdRequestFailed:@kMSG_REGISTERUSER_NO_TITLE2 message:@kMSG_REGISTERUSER_NO_PARA2 button:@kMSG_COMMON_OK];
            
        } else {
            //This block is for the undifine response
            [self popdRequestFailed:@kMSG_RESPONSE_UNEXPECTED_TITLE1 message:@kMSG_RESPONSE_UNEXPECTED_PARA1 button:@kMSG_COMMON_OK];
        }
        
    }
    else {
        //NSLog(@"response error");
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
                                      //[self performSegueWithIdentifier:@"registerSucessSegue" sender:nil]; //Navigate to the login form.
                                      //[self performSegueWithIdentifier:@"unwindToProfileLoginVC" sender:self];
                                      [self dismissViewControllerAnimated:YES completion:nil];
                                  }];
    
    [alertController addAction:alertAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}


//---------- Validating inputs ----------


-(BOOL) validateUserInputs {
    
    //Note: User validation is bottom up. So the popup will show the top-most validation error.
    NSString *errorTitle = @"", *errorText = @"";
    
    //Retype password
    if ( ![self.txtfPassword.text isEqual:self.txtfRetypePassword.text] ) {
        
        errorTitle = @"Password mismatch";
        errorText = @"Your password and retype password do not match.";
        self.txtfRetypePassword.rightViewMode = UITextFieldViewModeAlways;
        self.txtfRetypePassword.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
        [self.txtfRetypePassword becomeFirstResponder];
    }
    else {
        self.txtfRetypePassword.rightViewMode = UITextFieldViewModeNever;
    }
    
    //Password equal to username
    if ( [self.txtfPassword.text isEqual:self.txtfUsername.text] ) {
        
        errorTitle = @"Please select a different password";
        errorText = @"Username and password are similar!";
        self.txtfPassword.rightViewMode = UITextFieldViewModeAlways;
        self.txtfPassword.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
        [self.txtfPassword becomeFirstResponder];
    }
    else {
        self.txtfPassword.rightViewMode = UITextFieldViewModeNever;
    }
    
    //Password
    if ( ![GlobalVariableAndMethod isUserTextValied:self.txtfPassword.text] ) {
        
        errorTitle = @"Invalid input";
        errorText = [NSString stringWithFormat:@"Password is too short (at least %d characters)", kVALIDATE_MIN_TEXT_SIZE]; //@"Password is too short";
        self.txtfPassword.rightViewMode = UITextFieldViewModeAlways;
        self.txtfPassword.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
        [self.txtfPassword becomeFirstResponder];
    }
    else {
        self.txtfPassword.rightViewMode = UITextFieldViewModeNever;
    }
    
    //Username
    if ( ![GlobalVariableAndMethod isUserTextValied:self.txtfUsername.text] ) {
        
        errorTitle = @"Invalid input";
        errorText = [NSString stringWithFormat:@"Username is too short \n(more than %d characters)", kVALIDATE_MIN_TEXT_SIZE];
        self.txtfUsername.rightViewMode = UITextFieldViewModeAlways;
        self.txtfUsername.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
        [self.txtfUsername becomeFirstResponder];
    }
    else {
        self.txtfUsername.rightViewMode = UITextFieldViewModeNever;
    }
    
    //age
    if ( [self.txtfAge.text isEqual:[NSNull null]] || !(self.txtfAge.text.length>0)) {
        
        errorTitle = @"Invalid input";
        errorText = @"incorrect age";
        self.txtfAge.rightViewMode = UITextFieldViewModeAlways;
        self.txtfAge.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
    }
    else {
        self.txtfAge.rightViewMode = UITextFieldViewModeNever;
    }

    //gender
    if ( [self.userGender isEqual:[NSNull null]] || !(self.userGender.length>0)) {
        
        errorTitle = @"Invalid input";
        errorText = @"incorrect gender";
        self.txtfGender.rightViewMode = UITextFieldViewModeAlways;
        self.txtfGender.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
    }
    else {
        self.txtfGender.rightViewMode = UITextFieldViewModeNever;
    }

    //language
    if ( [self.preferredLanguage isEqual:[NSNull null]] || !(self.preferredLanguage.length>0)) {
        
        errorTitle = @"Invalid input";
        errorText = @"incorrect language";
        self.txtfLanguage.rightViewMode = UITextFieldViewModeAlways;
        self.txtfLanguage.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
    }
    else {
        self.txtfLanguage.rightViewMode = UITextFieldViewModeNever;
    }
    
    //residence
    if ( [self.nearestCity isEqual:[NSNull null]] || !(self.nearestCity.length>0)) {
        
        errorTitle = @"Invalid input";
        errorText = @"incorrect residence";
        self.txtfResidnece.rightViewMode = UITextFieldViewModeAlways;
        self.txtfResidnece.rightView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_error.png"]];
        [self.txtfResidnece becomeFirstResponder];
    }
    else {
        self.txtfResidnece.rightViewMode = UITextFieldViewModeNever;
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
    else if (textField == self.txtfUsername) {
        //Setting max character length for the string
        if(range.length + range.location > textField.text.length) { // Prevent crashing undo bug
            return NO;
        }
        NSUInteger newLength = [textField.text length] + [string length] - range.length;
        if (newLength > 100) {
            return NO;
        }
        
        //Set the valid input character pattern
        self.filteredCharacterSet = [ [string componentsSeparatedByCharactersInSet:[[NSCharacterSet characterSetWithCharactersInString:kVALIDE_INPUT_VALUES_USERNAME] invertedSet] ] componentsJoinedByString:@""];
        return [string isEqualToString:self.filteredCharacterSet];
    }
    else if (textField == self.txtfAge) {
        //Setting max character length for the string
        if(range.length + range.location > textField.text.length) { // Prevent crashing undo bug
            return NO;
        }

        NSUInteger newLength = [textField.text length] + [string length] - range.length;
        if (newLength > 2) {
            return NO;
        }
        
        //Set the valid input character pattern
        self.filteredCharacterSet = [ [string componentsSeparatedByCharactersInSet:[[NSCharacterSet characterSetWithCharactersInString:kVALIDE_INPUT_VALUES_NUMBERS] invertedSet] ] componentsJoinedByString:@""];
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
                                          [self.butnResidence setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
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
                                          [self.butnLanguage setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                                      }];
        [alertController addAction:alertAction];
    }
    
    //for Cancel
    UIAlertAction *alertCancelAction = [UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:nil];
    [alertController addAction:alertCancelAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

- (IBAction)genderButtonPressed:(UIButton *)sender {
    
    [self hideKeyBoard];
    
    
    NSArray *genderArray = [kARRAY_GENDER componentsSeparatedByString:@", "];
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Select the preferred language" message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    //Handle the pop-up menu options
    for (NSString *title in genderArray) {
        UIAlertAction *alertAction = [UIAlertAction actionWithTitle:title style:UIAlertActionStyleDefault handler:^(UIAlertAction * action)
                                      {
                                          self.userGender = title;
                                          [self.butnGender setTitle:[NSString stringWithFormat:@"  %@", self.userGender] forState:UIControlStateNormal];
                                          [self.butnGender setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                                      }];
        [alertController addAction:alertAction];
    }
    
    //for Cancel
    UIAlertAction *alertCancelAction = [UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:nil];
    [alertController addAction:alertCancelAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}


//---------- Managing the keyboard ----------

- (void)textFieldDidBeginEditing:(UITextField *)textField {
    
    int newLocationY = textField.frame.origin.y - self.keyboardOffSet;
    if (newLocationY>0) {
        CGPoint scrollPoint = CGPointMake(0, newLocationY);
        [self.scrollview setContentOffset:scrollPoint animated:YES];
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
    
    [self.scrollview setContentOffset:CGPointZero animated:YES];
    [self.txtfName resignFirstResponder];
    [self.txtfEmail resignFirstResponder];
    [self.txtfContactNo resignFirstResponder];
    [self.txtfAge resignFirstResponder];
    [self.txtfUsername resignFirstResponder];
    [self.txtfPassword resignFirstResponder];
    [self.txtfRetypePassword resignFirstResponder];
}




@end
