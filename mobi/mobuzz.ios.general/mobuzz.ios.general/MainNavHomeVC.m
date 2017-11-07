//
//  MainNavHomeVC.m
//  mobuzz.ios.general
//
//  Created by Vajira on 26/8/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import "MainNavHomeVC.h"
#import "GlobalVariableAndMethod.h"
#import "PopupMessages.h"

@interface MainNavHomeVC ()

@property (strong, nonatomic) IBOutlet UIButton *butnComplaint;
@property BOOL isUserLogged;

@end


//Navigation related to tab-bar-controller
@implementation MainNavHomeVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    //Check the user is signed or not
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    self.isUserLogged = [userDefaults boolForKey:@kIS_USER_LOGGED];
    
    if (self.isUserLogged)
    {
        [self.butnComplaint setImage:[UIImage imageNamed:@"img_but_breedingsiteComplaint"] forState:UIControlStateNormal];
    }
    else
    {
        //User not logged in, disable complaint module
        [self.butnComplaint setImage:[UIImage imageNamed:@"img_but_breedingsiteComplaint-disable"] forState:UIControlStateNormal];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)complaintPressed:(UIButton *)sender {
    
    if (self.isUserLogged) {
        [self checkInternetConnectionAndMakeGetRequest:0];
    } else {
        [self showPopupDialog:nil message:@kMSG_LOGINUSER_ERROR_PARA3 button:@kMSG_COMMON_OK];
    }
}

- (IBAction)MapPressed:(UIButton *)sender {
    
    [self checkInternetConnectionAndMakeGetRequest:1];
}

-(void)checkInternetConnectionAndMakeGetRequest:(NSInteger)flag {
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    //Check the internet connection
    NSURLSession *connTestSession = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration] delegate:self delegateQueue:[NSOperationQueue mainQueue] ];
    NSURLSessionDataTask * sessionDataTask = [connTestSession dataTaskWithURL:[NSURL URLWithString:@kCONN_URL_CHECK] completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        
        //Check connection is available or not
        if ((error == nil) && data) {
            
            switch (flag) {
                case 0:
                    [self performSegueWithIdentifier:@"ComplaintSegue" sender:nil];
                    break;
                    
                case 1:
                    [self performSegueWithIdentifier:@"MapSegue" sender:nil];
                    break;
                    
                default:
                    break;
            }
            
        }
        else {
            
            //Popup message
            [self showPopupDialog:@kMSG_INTERNET_NO_TITLE1 message:@kMSG_INTERNET_NO_PARA1 button:@kMSG_COMMON_OK];
        }

    }];
    
    [sessionDataTask resume];

}


- (BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender {
    
    if ([identifier isEqualToString:@"AboutDengueSegue"]) {
        return YES;
    } else {
        return NO;
    }
}


//--------- Popup messages ----------
-(void)showPopupDialog:(NSString *)byTitle message:(NSString *)byMessageText button:(NSString*)byButtonText {
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:byTitle message:byMessageText preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *alertAction = [UIAlertAction actionWithTitle:byButtonText style:UIAlertActionStyleDefault handler:nil];
    
    [alertController addAction:alertAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

@end
