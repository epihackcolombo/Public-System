//
//  InfoDengueSubVC1.m
//  mobuzz.ios.general
//
//  Created by Vajira on 25/9/15.
//  Copyright © 2015 cosmic. All rights reserved.
//

#import "InfoDengueSubVC1Statistics.h"
#import "LocalizedText.h"
#import "GlobalVariableAndMethod.h"

@interface InfoDengueSubVC1Statistics ()

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para2;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para3;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para4;

@property (strong, nonatomic) IBOutlet UILabel *lablL1;
@property (strong, nonatomic) IBOutlet UILabel *lablL2;
@property (strong, nonatomic) IBOutlet UILabel *lablL3;
@property (strong, nonatomic) IBOutlet UILabel *lablL4;
@property (strong, nonatomic) IBOutlet UILabel *lablL5;

@property (strong, nonatomic) IBOutlet UIImageView *imgvImRightSibling;
@property (strong, nonatomic) IBOutlet UIImageView *imgvDsRightSibling;

@property (strong, nonatomic) IBOutlet UIButton *butnBack;

@end


//Showing texts related to dengue-statistics in different languages
@implementation InfoDengueSubVC1Statistics

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
    
    //Handle right sibling image taps
    [self.imgvImRightSibling addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tappedImRightSibling)]];
    
    [self.imgvDsRightSibling addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tappedIDsRightSibling)]];
    
    //Handle swipe gestures
    UISwipeGestureRecognizer *swipeLeft = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(tappedImRightSibling)];
    [swipeLeft setDirection:UISwipeGestureRecognizerDirectionLeft];
    [self.view addGestureRecognizer:swipeLeft];
    
    [self setLanguageText];
}


- (IBAction)backPressed:(UIButton *)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}


- (void) tappedImRightSibling {
    [self.tabBarController setSelectedIndex:[self.tabBarController selectedIndex] + 1];
}
     
- (void) tappedIDsRightSibling {
    [self.tabBarController setSelectedIndex:[self.tabBarController selectedIndex] + 2];
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
    
    
    [[UILabel appearanceWhenContainedInInstancesOfClasses:[NSArray arrayWithObject:[InfoDengueSubVC1Statistics class]]] setFont:[UIFont fontWithName:userFontType size:userFontSize]];
    
    //Set label texts
    self.lablUTitle1.text = [LocalizedText translateTextByTextIdentifier:@"dengue_statistics_title" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para1.text = [LocalizedText translateTextByTextIdentifier:@"dengue_statistics_para1" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para2.text = [LocalizedText translateTextByTextIdentifier:@"dengue_statistics_para3" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para3.text = [LocalizedText translateTextByTextIdentifier:@"dengue_statistics_para4" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para4.text = [LocalizedText translateTextByTextIdentifier:@"dengue_statistics_para5" byLanguageCode:userLanguageCode];
    
    //Set bold label settings
    [self.lablUTitle1 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    
}

@end
