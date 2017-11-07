//
//  AboutCosmicVC.m
//  mobuzz.ios.general
//
//  Created by Vajira on 3/9/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import "AboutCosmicVC.h"
#import "GlobalVariableAndMethod.h"
#import "LocalizedText.h"

@interface AboutCosmicVC ()
@property (strong, nonatomic) IBOutlet UIView *butnBack;

@property (strong, nonatomic) IBOutlet UILabel *lablL1;
@property (strong, nonatomic) IBOutlet UILabel *lablL2;
@property (strong, nonatomic) IBOutlet UILabel *lablL3;
@property (strong, nonatomic) IBOutlet UILabel *lablL4;
@property (strong, nonatomic) IBOutlet UILabel *lablL5;

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTPara1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTPara2;
@property (strong, nonatomic) IBOutlet UILabel *lablUTPara3;

@end


//Showing texts related to COSMIC in different languages
@implementation AboutCosmicVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.lablL1.text = @"";
    self.lablL2.text = @"";
    self.lablL3.text = @"";
    self.lablL4.text = @"";
    self.lablL5.text = @"";
    
    //Button styles
    self.butnBack.layer.borderWidth = 1.0f;
    self.butnBack.layer.borderColor = [[GlobalVariableAndMethod getMobuzzColor:@"ash"]CGColor];
    
    [self setLanguageText];
}

- (IBAction)backPressed:(UIButton *)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
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

    
    [[UILabel appearanceWhenContainedInInstancesOfClasses:[NSArray arrayWithObject:[AboutCosmicVC class]]] setFont:[UIFont fontWithName:userFontType size:userFontSize]];
    
    //Set label texts
    self.lablUTitle1.text = [LocalizedText translateTextByTextIdentifier:@"about_cosmic_title2" byLanguageCode:userLanguageCode];
    self.lablUTPara1.text = [LocalizedText translateTextByTextIdentifier:@"about_cosmic_para2_1" byLanguageCode:userLanguageCode];
    self.lablUTPara2.text = [LocalizedText translateTextByTextIdentifier:@"about_cosmic_para2_2" byLanguageCode:userLanguageCode];
    self.lablUTPara3.text = [LocalizedText translateTextByTextIdentifier:@"about_cosmic_para2_3" byLanguageCode:userLanguageCode];
    
    //Set bold label settings
    [self.lablUTitle1 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    
}




@end
