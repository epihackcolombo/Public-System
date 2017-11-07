//
//  AboutFaqVC.m
//  mobuzz.ios.general
//
//  Created by Vajira on 2/9/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import "AboutFaqVC.h"
#import "GlobalVariableAndMethod.h"
#import "LocalizedText.h"

@interface AboutFaqVC ()

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle1Para1;

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para2;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para3;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para4;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para5;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para6;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para7;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para8;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para9;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle2Para10;

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle3;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle3Para1;

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle4;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle4Para1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle4Para2;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle4Para3;

//translation text - 6 is mapped to section - 5
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle5;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle5Para1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle5Para2;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle5Para3;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle5Para4;

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle6;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle6Para1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle6Para2;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle6Para3;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle6Para4;

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle7;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle7Para1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle7Para2;

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle8;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle8Para1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle8Para2;

//translation text - 10 is removed

//translation text - 11 is mapped to section - 9
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle9;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle9Para1;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle9Para2;


@property (strong, nonatomic) IBOutlet UILabel *lablUTitle10;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle10Para1;

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle11;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle11Para1;

@property (strong, nonatomic) IBOutlet UILabel *lablUTitle12;
@property (strong, nonatomic) IBOutlet UILabel *lablUTitle12Para1;



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
@property (strong, nonatomic) IBOutlet UILabel *lablL14;
@property (strong, nonatomic) IBOutlet UILabel *lablL15;
@property (strong, nonatomic) IBOutlet UILabel *lablL16;
@property (strong, nonatomic) IBOutlet UILabel *lablL17;
@property (strong, nonatomic) IBOutlet UILabel *lablL18;
@property (strong, nonatomic) IBOutlet UILabel *lablL19;
@property (strong, nonatomic) IBOutlet UILabel *lablL20;
@property (strong, nonatomic) IBOutlet UILabel *lablL21;
@property (strong, nonatomic) IBOutlet UILabel *lablL22;
@property (strong, nonatomic) IBOutlet UILabel *lablL23;
@property (strong, nonatomic) IBOutlet UILabel *lablL24;
@property (strong, nonatomic) IBOutlet UILabel *lablL25;
@property (strong, nonatomic) IBOutlet UILabel *lablL26;
@property (strong, nonatomic) IBOutlet UILabel *lablL27;
@property (strong, nonatomic) IBOutlet UILabel *lablL28;
@property (strong, nonatomic) IBOutlet UILabel *lablL29;
@property (strong, nonatomic) IBOutlet UILabel *lablL30;
@property (strong, nonatomic) IBOutlet UILabel *lablL31;
@property (strong, nonatomic) IBOutlet UILabel *lablL32;
@property (strong, nonatomic) IBOutlet UILabel *lablL33;
@property (strong, nonatomic) IBOutlet UILabel *lablL34;
@property (strong, nonatomic) IBOutlet UILabel *lablL35;
@property (strong, nonatomic) IBOutlet UILabel *lablL36;
@property (strong, nonatomic) IBOutlet UILabel *lablL37;
@property (strong, nonatomic) IBOutlet UILabel *lablL38;
@property (strong, nonatomic) IBOutlet UILabel *lablL39;
@property (strong, nonatomic) IBOutlet UILabel *lablL40;
@property (strong, nonatomic) IBOutlet UILabel *lablL41;
@property (strong, nonatomic) IBOutlet UILabel *lablL42;
@property (strong, nonatomic) IBOutlet UILabel *lablL43;
@property (strong, nonatomic) IBOutlet UILabel *lablL44;


@property (strong, nonatomic) IBOutlet UIButton *butnBack;

@end


//Showing texts related to FAQs in different languages
@implementation AboutFaqVC

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
    self.lablL14.text = @"";
    self.lablL15.text = @"";
    self.lablL16.text = @"";
    self.lablL17.text = @"";
    self.lablL18.text = @"";
    self.lablL19.text = @"";
    self.lablL20.text = @"";
    self.lablL21.text = @"";
    self.lablL22.text = @"";
    self.lablL23.text = @"";
    self.lablL24.text = @"";
    self.lablL25.text = @"";
    self.lablL26.text = @"";
    self.lablL27.text = @"";
    self.lablL28.text = @"";
    self.lablL29.text = @"";
    self.lablL30.text = @"";
    self.lablL31.text = @"";
    self.lablL32.text = @"";
    self.lablL33.text = @"";
    self.lablL34.text = @"";
    self.lablL35.text = @"";
    self.lablL36.text = @"";
    self.lablL37.text = @"";
    self.lablL38.text = @"";
    self.lablL39.text = @"";
    self.lablL40.text = @"";
    self.lablL41.text = @"";
    self.lablL42.text = @"";
    self.lablL43.text = @"";
    self.lablL44.text = @"";

    
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
    
    
    [[UILabel appearanceWhenContainedInInstancesOfClasses:[NSArray arrayWithObject:[AboutFaqVC class]]] setFont:[UIFont fontWithName:userFontType size:userFontSize]];
    
    //Set label texts
    self.lablUTitle1.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_title1" byLanguageCode:userLanguageCode];
    self.lablUTitle1Para1.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para1" byLanguageCode:userLanguageCode];

 
    self.lablUTitle2.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_title3" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para1.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para3_1" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para2.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para3_2_1" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para3.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para3_2_2" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para4.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para3_3_1" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para5.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para3_3_2" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para6.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para3_3_3" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para7.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para3_3_4" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para8.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para3_4_1" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para9.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para3_4_2" byLanguageCode:userLanguageCode];
    self.lablUTitle2Para10.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para3_4_3" byLanguageCode:userLanguageCode];
 

    self.lablUTitle3.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_title4" byLanguageCode:userLanguageCode];
    self.lablUTitle3Para1.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para4" byLanguageCode:userLanguageCode];
    
    self.lablUTitle4.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_title5" byLanguageCode:userLanguageCode];
    self.lablUTitle4Para1.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para5_1" byLanguageCode:userLanguageCode];
    self.lablUTitle4Para2.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para5_2" byLanguageCode:userLanguageCode];
    self.lablUTitle4Para3.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para5_3" byLanguageCode:userLanguageCode];
    
    //translation text - 6 is mapped to section - 5
    self.lablUTitle5.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_title6" byLanguageCode:userLanguageCode];
    self.lablUTitle5Para1.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para6_1" byLanguageCode:userLanguageCode];
    self.lablUTitle5Para2.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para6_2" byLanguageCode:userLanguageCode];
    self.lablUTitle5Para3.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para6_3" byLanguageCode:userLanguageCode];
    self.lablUTitle5Para4.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para6_4" byLanguageCode:userLanguageCode];
   
    self.lablUTitle6.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_title7" byLanguageCode:userLanguageCode];
    self.lablUTitle6Para1.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para7_1" byLanguageCode:userLanguageCode];
    self.lablUTitle6Para2.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para7_2" byLanguageCode:userLanguageCode];
    self.lablUTitle6Para3.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para7_3" byLanguageCode:userLanguageCode];
    self.lablUTitle6Para4.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para7_4" byLanguageCode:userLanguageCode];
    
    self.lablUTitle7.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_title8" byLanguageCode:userLanguageCode];
    self.lablUTitle7Para1.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para8_1" byLanguageCode:userLanguageCode];
    self.lablUTitle7Para2.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para8_2" byLanguageCode:userLanguageCode];

    self.lablUTitle8.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_title9" byLanguageCode:userLanguageCode];
    self.lablUTitle8Para1.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para9_1" byLanguageCode:userLanguageCode];
    self.lablUTitle8Para2.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para9_2" byLanguageCode:userLanguageCode];

    //translation text - 10 is removed
    
    //translation text - 11 is mapped to section - 9
    self.lablUTitle9.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_title11" byLanguageCode:userLanguageCode];
    self.lablUTitle9Para1.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para11_1" byLanguageCode:userLanguageCode];
    self.lablUTitle9Para2.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para11_2" byLanguageCode:userLanguageCode];

    self.lablUTitle10.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_title12" byLanguageCode:userLanguageCode];
    self.lablUTitle10Para1.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para12" byLanguageCode:userLanguageCode];

    self.lablUTitle11.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_title13" byLanguageCode:userLanguageCode];
    self.lablUTitle11Para1.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para13" byLanguageCode:userLanguageCode];
    
    self.lablUTitle12.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_title14" byLanguageCode:userLanguageCode];
    self.lablUTitle12Para1.text = [LocalizedText translateTextByTextIdentifier:@"faq_mobuzz_para14" byLanguageCode:userLanguageCode];

    
    //Set bold label settings
    [self.lablUTitle1 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle2 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle3 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle4 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle5 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle6 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle7 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle8 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle9 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle10 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle11 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];
    [self.lablUTitle12 setFont:[UIFont fontWithName:userFontTypeBold size:userFontSizeBold]];

    
    //Underline text
    self.lablUTitle2Para2.attributedText = [LocalizedText htmlFormattingString:self.lablUTitle2Para2.text hexColorCode:@kCOLOR_MOBUZZ_GRAY_HTML];
    self.lablUTitle2Para4.attributedText = [LocalizedText htmlFormattingString:self.lablUTitle2Para4.text hexColorCode:@kCOLOR_MOBUZZ_GRAY_HTML];
    self.lablUTitle2Para8.attributedText = [LocalizedText htmlFormattingString:self.lablUTitle2Para8.text hexColorCode:@kCOLOR_MOBUZZ_GRAY_HTML];
    self.lablUTitle11Para1.attributedText = [LocalizedText htmlFormattingString:self.lablUTitle11Para1.text hexColorCode:@kCOLOR_MOBUZZ_GRAY_HTML];
    self.lablUTitle12Para1.attributedText = [LocalizedText htmlFormattingString:self.lablUTitle12Para1.text hexColorCode:@kCOLOR_MOBUZZ_GRAY_HTML];
    
    //Open links in browser
    UITapGestureRecognizer* gestureOnWebsite = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(userTappedOnWebsite:)];
    [self.lablUTitle11Para1 addGestureRecognizer:gestureOnWebsite];
    
    UITapGestureRecognizer* gestureOnEmail = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(userTappedOnEmail:)];
    [self.lablUTitle12Para1 addGestureRecognizer:gestureOnEmail];

}


- (void)userTappedOnWebsite:(UIGestureRecognizer*)gestureRecognizer {
    
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http://www.mo-buzz.org/srilanka/"]];
}

- (void)userTappedOnEmail:(UIGestureRecognizer*)gestureRecognizer {
    
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"mailto:srilanka@mo-buzz.org"]];
}


@end
