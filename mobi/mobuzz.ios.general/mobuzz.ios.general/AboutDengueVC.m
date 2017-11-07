//
//  AboutDengueVC.m
//  mobuzz.ios.general
//
//  Created by Vajira on 28/9/15.
//  Copyright © 2015 cosmic. All rights reserved.
//

#import "AboutDengueVC.h"
#import "GlobalVariableAndMethod.h"

@interface AboutDengueVC ()

@property (strong, nonatomic) IBOutlet UIButton *butnBack;

@end


//Navigation related to tab-bar-controller
@implementation AboutDengueVC

- (void)viewDidLoad {
    [super viewDidLoad];

    self.butnBack.layer.borderWidth = 1.0f;
    self.butnBack.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"ash"]CGColor];
}


#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    
    UIButton *button = (UIButton *)sender;

    UITabBarController *tbc = segue.destinationViewController;
    tbc.selectedViewController = [tbc.viewControllers objectAtIndex:button.tag];
    
}

- (BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender {
    
    return NO;
    
}

- (IBAction)infoButtonPressed:(UIButton *)sender {

    [self performSegueWithIdentifier:@"AboutDengueSegue" sender:sender];
}

- (IBAction)backPressed:(UIButton *)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}


@end
