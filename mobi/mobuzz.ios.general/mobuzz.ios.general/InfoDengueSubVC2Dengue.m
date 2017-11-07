//
//  InfoDengueSubVC2.m
//  mobuzz.ios.general
//
//  Created by Vajira on 25/9/15.
//  Copyright © 2015 cosmic. All rights reserved.
//

#import "InfoDengueSubVC2Dengue.h"
#import "LocalizedText.h"
#import "GlobalVariableAndMethod.h"

@interface InfoDengueSubVC2Dengue ()

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para2;

@property (strong, nonatomic) IBOutlet UILabel *lablL1;
@property (strong, nonatomic) IBOutlet UILabel *lablL2;

@property (strong, nonatomic) IBOutlet UIImageView *imgvImLeftSibling;
@property (strong, nonatomic) IBOutlet UIImageView *imgvImRightSibling;

@property (strong, nonatomic) IBOutlet UIButton *butnBack;

@end


//Showing texts related to dengue-information in different languages
@implementation InfoDengueSubVC2Dengue

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.tabBarController.hidesBottomBarWhenPushed = YES;
    self.navigationController.navigationBar.hidden = YES;
    self.tabBarController.tabBar.hidden = YES;
    
    self.butnBack.layer.borderWidth = 1.0f;
    self.butnBack.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"ash"]CGColor];
    
    self.lablL1.text = @"";
    self.lablL2.text = @"";
    
    //Handle right sibling image taps
    [self.imgvImLeftSibling addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tappedImLeftSibling)]];
    
    [self.imgvImRightSibling addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tappedImRightSibling)]];
    
    //Handle swipe gestures
    UISwipeGestureRecognizer *swipeLeft = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(tappedImRightSibling)];
    [swipeLeft setDirection:UISwipeGestureRecognizerDirectionLeft];
    [self.view addGestureRecognizer:swipeLeft];
    
    UISwipeGestureRecognizer *swipeRight = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(tappedImLeftSibling)];
    [swipeRight setDirection:UISwipeGestureRecognizerDirectionRight];
    [self.view addGestureRecognizer:swipeRight];
    
    [self setLanguageText];
}

- (IBAction)backPressed:(UIButton *)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void) tappedImLeftSibling {
    [self.tabBarController setSelectedIndex:[self.tabBarController selectedIndex] - 1];
}

- (void) tappedImRightSibling {
    [self.tabBarController setSelectedIndex:[self.tabBarController selectedIndex] + 1];
}


-(void)setLanguageText {
    
    //Get the user preference
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    
    NSString *userLanguage = [userDefaults stringForKey:@kLOGGED_USER_LANGUAGE];
    NSString *userLanguageCode, *userFontType, *userFontTypeBold;
    int userFontSize, userFontSizeBold;
    
    //Set font according to the language
    if (userLanguage) {
        userLanguageCode = [LocalizedText getLanuageCode:userLanguage];
        userFontType = [LocalizedText getFontNameByLanguageCode:userLanguageCode isBold:NO];
        userFontSize = [LocalizedText getFontSizeByLanguageCode:userLanguageCode isBold:NO];
        userFontTypeBold = [LocalizedText getFontNameByLanguageCode:userLanguageCode isBold:YES];
        userFontSizeBold = [LocalizedText getFontSizeByLanguageCode:userLanguageCode isBold:YES];
    }
    else {
        //Set default values
        userLanguageCode = @kLANGUAGE_DEFAULT;
        userFontType = [LocalizedText getFontNameByLanguageCode:userLanguageCode isBold:NO];
        userFontSize = [LocalizedText getFontSizeByLanguageCode:userLanguageCode isBold:NO];
        userFontTypeBold = [LocalizedText getFontNameByLanguageCode:userLanguageCode isBold:YES];
        userFontSizeBold = [LocalizedText getFontSizeByLanguageCode:userLanguageCode isBold:YES];
    }
    
    //------------------------------
    
    
    [[UILabel appearanceWhenContainedInInstancesOfClasses:[NSArray arrayWithObject:[InfoDengueSubVC2Dengue class]]] setFont:[UIFont fontWithName:userFontType size:userFontSize]];
    
    //Set label texts
    self.lablUTitle1.text = [LocalizedText translateTextByTextIdentifier:@"about_dengue_title" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para1.text = [LocalizedText translateTextByTextIdentifier:@"about_dengue_para1" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para2.text = [LocalizedText translateTextByTextIdentifier:@"about_dengue_para2" byLanguageCode:userLanguageCode];

    
    //Set bold label settings
    [self.lablUTitle1 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    
}


@end
