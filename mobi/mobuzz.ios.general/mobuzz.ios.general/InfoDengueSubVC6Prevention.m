//
//  InfoDengueSubVC6.m
//  mobuzz.ios.general
//
//  Created by Vajira on 25/9/15.
//  Copyright © 2015 cosmic. All rights reserved.
//

#import "InfoDengueSubVC6Prevention.h"
#import "LocalizedText.h"
#import "GlobalVariableAndMethod.h"

@interface InfoDengueSubVC6Prevention ()

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para2;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para3;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para4;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para5;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para6;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para7;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para8;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para9;


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

@property (strong, nonatomic) IBOutlet UIImageView *imgvImLefttSibling;
@property (strong, nonatomic) IBOutlet UIImageView *imgvDsLefttSibling;

@property (strong, nonatomic) IBOutlet UIButton *butnBack;

@end


//Showing texts related to dengue-prevention in different languages
@implementation InfoDengueSubVC6Prevention

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
    
    
    //Handle right sibling image taps
    [self.imgvImLefttSibling addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tappedImLeftSibling)]];
    
    [self.imgvDsLefttSibling addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tappedDsLeftSibling)]];
    
    //Handle swipe gestures
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

- (void) tappedDsLeftSibling {
    [self.tabBarController setSelectedIndex:[self.tabBarController selectedIndex] - 2];
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
    
    
    [[UILabel appearanceWhenContainedInInstancesOfClasses:[NSArray arrayWithObject:[InfoDengueSubVC6Prevention class]]] setFont:[UIFont fontWithName:userFontType size:userFontSize]];
    
    //Set label texts
    self.lablUTitle1.text = [LocalizedText translateTextByTextIdentifier:@"dengue_prevention_title1" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para1.text = [LocalizedText translateTextByTextIdentifier:@"dengue_prevention_para1" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para2.text = [LocalizedText translateTextByTextIdentifier:@"dengue_prevention_para2" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para3.text = [LocalizedText translateTextByTextIdentifier:@"dengue_prevention_para3" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para4.text = [LocalizedText translateTextByTextIdentifier:@"dengue_prevention_para4" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para5.text = [LocalizedText translateTextByTextIdentifier:@"dengue_prevention_para5" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para6.text = [LocalizedText translateTextByTextIdentifier:@"dengue_prevention_para6" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para7.text = [LocalizedText translateTextByTextIdentifier:@"dengue_prevention_para7" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para8.text = [LocalizedText translateTextByTextIdentifier:@"dengue_prevention_para8" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para9.text = [LocalizedText translateTextByTextIdentifier:@"dengue_prevention_para9" byLanguageCode:userLanguageCode];
    
    
    //Set bold label settings
    [self.lablUTitle1 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    
}


@end
