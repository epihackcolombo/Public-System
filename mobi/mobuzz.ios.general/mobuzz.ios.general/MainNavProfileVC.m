//
//  MainNavProfileVC.m
//  mobuzz.ios.general
//
//  Created by Vajira on 26/8/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import "MainNavProfileVC.h"
#import "GlobalVariableAndMethod.h"
#import "PopupMessages.h"

@interface MainNavProfileVC ()

@property (strong, nonatomic) IBOutlet UIStackView *uiswAnonymousUser;
@property (strong, nonatomic) IBOutlet UIStackView *uiswRegisteredUser;

@end


//Navigation related to view-controllers
@implementation MainNavProfileVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    //Check the user is signed or not, and change UI
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    BOOL isUserLogged = [userDefaults boolForKey:@kIS_USER_LOGGED];
    
    if (isUserLogged)
    {
        self.uiswAnonymousUser.hidden = YES;
    }
    else
    {
        self.uiswRegisteredUser.hidden = YES;
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



- (IBAction)logoutPressed:(UIButton *)sender {
    
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


- (IBAction)editPressed:(UIButton *)sender {

    [self checkInternetConnectionAndMakeGetRequest];
}


-(void)checkInternetConnectionAndMakeGetRequest {
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    //Check the internet connection
    NSURLSession *connTestSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:self delegateQueue:[NSOperationQueue mainQueue] ];
    NSURLSessionDataTask * sessionDataTask = [connTestSession dataTaskWithURL:[NSURL URLWithString:@kCONN_URL_CHECK] completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        
        //Check connection is available or not
        if ((error == nil) && data) {
            
            [self performSegueWithIdentifier:@"EditProfileSegue" sender:nil];
        }
        else {
            
            //Popup message
            [self showPopupDialog:@kMSG_INTERNET_NO_TITLE1 message:@kMSG_INTERNET_NO_PARA1 button:@kMSG_COMMON_OK];
        }
        
    }];
    
    [sessionDataTask resume];

}


- (BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender {
    
    return NO;
}


//--------- Popup messages ----------
-(void)showPopupDialog:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:byTitle message:byMessageText preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *alertAction = [UIAlertAction actionWithTitle:byButtonText style:UIAlertActionStyleDefault handler:nil];
    
    [alertController addAction:alertAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

@end
