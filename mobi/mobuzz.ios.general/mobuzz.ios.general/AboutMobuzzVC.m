//
//  AboutMobuzzVC.m
//  mobuzz.ios.general
//
//  Created by Vajira on 3/9/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import "AboutMobuzzVC.h"
#import "GlobalVariableAndMethod.h"
#import "LocalizedText.h"

@interface AboutMobuzzVC ()

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para2;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para3;

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para2;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para3;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para4;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para5;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para6;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para7;


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

@property (strong, nonatomic) IBOutlet UIView *butnBack;

@end


//Showing texts related to Mo-Buzz in different languages
@implementation AboutMobuzzVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
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

    
    [[UILabel appearanceWhenContainedInInstancesOfClasses:[NSArray arrayWithObject:[AboutMobuzzVC class]]] setFont:[UIFont fontWithName:userFontType size:userFontSize]];
    
    //Set label texts
    self.lablUTitle1.text = [LocalizedText translateTextByTextIdentifier:@"about_mobuzz_title1" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para1.text = [LocalizedText translateTextByTextIdentifier:@"about_mobuzz_para1_1" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para2.text = [LocalizedText translateTextByTextIdentifier:@"about_mobuzz_para1_2" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para3.text = [LocalizedText translateTextByTextIdentifier:@"about_mobuzz_para1_3" byLanguageCode:userLanguageCode];
    
    self.lablUTitle2.text = [LocalizedText translateTextByTextIdentifier:@"about_mobuzz_title2" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para1.text = [LocalizedText translateTextByTextIdentifier:@"about_mobuzz_para2_1" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para2.text = [LocalizedText translateTextByTextIdentifier:@"about_mobuzz_para2_2" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para3.text = [LocalizedText translateTextByTextIdentifier:@"about_mobuzz_para2_3" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para4.text = [LocalizedText translateTextByTextIdentifier:@"about_mobuzz_para2_4" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para5.text = [LocalizedText translateTextByTextIdentifier:@"about_mobuzz_para2_5" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para6.text = [LocalizedText translateTextByTextIdentifier:@"about_mobuzz_para2_6" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para7.text = [LocalizedText translateTextByTextIdentifier:@"about_mobuzz_para3" byLanguageCode:userLanguageCode];
    
    //Set bold label settings
    [self.lablUTitle1 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle2 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
}


@end