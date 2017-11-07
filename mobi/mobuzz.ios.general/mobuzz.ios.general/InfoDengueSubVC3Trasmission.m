//
//  InfoDengueSubVC3.m
//  mobuzz.ios.general
//
//  Created by Vajira on 25/9/15.
//  Copyright © 2015 cosmic. All rights reserved.
//

#import "InfoDengueSubVC3Trasmission.h"
#import "LocalizedText.h"
#import "GlobalVariableAndMethod.h"

@interface InfoDengueSubVC3Trasmission ()

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para2;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para3;

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para2;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para3;

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle3;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle4;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle5;

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
@property (strong, nonatomic) IBOutlet UILabel *lablL13;


@property (strong, nonatomic) IBOutlet UIImageView *imgvImLeftSibling;
@property (strong, nonatomic) IBOutlet UIImageView *imgvImRightSibling;

@property (strong, nonatomic) IBOutlet UIButton *butnBack;

@end


//Showing texts related to dengue-statistics in different languages
@implementation InfoDengueSubVC3Trasmission

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.tabBarController.hidesBottomBarWhenPushed = YES;
    self.navigationController.navigationBar.hidden = YES;
    self.tabBarController.tabBar.hidden = YES;
    
    self.butnBack.layer.borderWidth = 1.0f;
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
    self.lablL12.text = @"";
    self.lablL13.text = @"";
    
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
    
    
    [[UILabel appearanceWhenContainedInInstancesOfClasses:[NSArray arrayWithObject:[InfoDengueSubVC3Trasmission class]]] setFont:[UIFont fontWithName:userFontType size:userFontSize]];
    
    //Set label texts
    self.lablUTitle1.text = [LocalizedText translateTextByTextIdentifier:@"dengue_spread_title1" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para1.text = [LocalizedText translateTextByTextIdentifier:@"dengue_spread_para1" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para2.text = [LocalizedText translateTextByTextIdentifier:@"dengue_spread_para2" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para3.text = [LocalizedText translateTextByTextIdentifier:@"dengue_spread_para3" byLanguageCode:userLanguageCode];
    
    self.lablUTitle2.text = [LocalizedText translateTextByTextIdentifier:@"dengue_spread_title2" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para1.text = [LocalizedText translateTextByTextIdentifier:@"dengue_spread_para4" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para2.text = [LocalizedText translateTextByTextIdentifier:@"dengue_spread_para5" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para3.text = [LocalizedText translateTextByTextIdentifier:@"dengue_spread_para6" byLanguageCode:userLanguageCode];
    
    self.lablUTitle3.text = [LocalizedText translateTextByTextIdentifier:@"dengue_spread_title3" byLanguageCode:userLanguageCode];
    self.lablUTitle4.text = [LocalizedText translateTextByTextIdentifier:@"dengue_spread_title4" byLanguageCode:userLanguageCode];
    self.lablUTitle5.text = [LocalizedText translateTextByTextIdentifier:@"dengue_spread_title5" byLanguageCode:userLanguageCode];
    
    //Set bold label settings
    [self.lablUTitle1 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle2 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle3 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle4 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle5 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    
    //Underline text
    self.lablUTitle1Para2.attributedText = [LocalizedText htmlFormattingString:self.lablUTitle1Para2.text hexColorCode:@kCOLOR_MOBUZZ_GRAY_HTML];
    self.lablUTitle1Para3.attributedText = [LocalizedText htmlFormattingString:self.lablUTitle1Para3.text hexColorCode:@kCOLOR_MOBUZZ_GRAY_HTML];

}


@end
