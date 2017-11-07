//
//  LocalizedText.m
//  mobuzz.ios.general
//
//  Created by Vajira on 1/9/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import "LocalizedText.h"
#import "GlobalVariableAndMethod.h"

@implementation LocalizedText

+(NSString*)translateTextByTextIdentifier:(NSString*)textIdentifier byLanguageCode:(NSString*)languageCode {
    
    NSString *path = [[NSBundle mainBundle] pathForResource:languageCode ofType:@"lproj"];
    if (path) {
        NSBundle *localeBundle = [NSBundle bundleWithPath:path];
        NSString *localText = NSLocalizedStringFromTableInBundle(textIdentifier, nil, localeBundle, nil);
        
        if ([textIdentifier isEqualToString:localText]) { //there is no translation text
            
            //Try english text
            path = [[NSBundle mainBundle] pathForResource:@kLANGUAGE_DEFAULT ofType:@"lproj"];
            if (path) {
                localeBundle = [NSBundle bundleWithPath:path];
                return NSLocalizedStringFromTableInBundle(textIdentifier, nil, localeBundle, nil);
            }
            else {
                return @"";
            }
        }
        else {
            return localText;
        }
    }
    else {
        return @"";
    }
}

+(NSString*)translateTextByTextIdentifier:(NSString*)textIdentifier byLanguage:(NSString*)language {
    
    NSString *languageCode = [self getLanuageCode:language];
    
    if (languageCode) {
        return [self translateTextByTextIdentifier:textIdentifier byLanguageCode:languageCode];
    }
    else {
        return nil;
    }
}

+(NSString*)getLanuageCode:(NSString*)language {
    
    NSArray *languageArray = [kARRAY_LANGUAGES componentsSeparatedByString:@", "];
    NSArray *languageCodeArray = [kARRAY_LANGUAGE_CODES componentsSeparatedByString:@", "];
    NSString *currentLanguage, *languageCode;
    
    for (int i=0; i<languageArray.count; i++) {
        
        currentLanguage = [languageArray objectAtIndex:i];
        
        if ([currentLanguage caseInsensitiveCompare:language] == NSOrderedSame) {
            languageCode = [languageCodeArray objectAtIndex:i];
            break;
        }
    }
    
    if (languageCode) {
        return languageCode;
    }
    else {
        return nil;
    }
}


+(NSString*)getFontNameByLanguageCode:(NSString*)languageCode isBold:(BOOL)bold {
        
    if ([languageCode caseInsensitiveCompare:@"en"] == NSOrderedSame) {
        if (bold) {
            return @"GillSans-Bold";
        } else {
            return @"GillSans";
        }
        
    } else if ([languageCode caseInsensitiveCompare:@"si"] == NSOrderedSame) {
        //IskoolaPota
        if (bold) {
            return @"Sinhala Sangam MN";
            //return @"Malithi Web";
        } else {
            return @"Sinhala Sangam MN";
            //return @"Malithi Web";
        }
        
    } else if ([languageCode caseInsensitiveCompare:@"ta-LK"] == NSOrderedSame) {
        return @"Tamil Sangam MN";
    }
    else {
        return @"GillSans"; //Default font type
    }
}


+(int)getFontSizeByLanguageCode:(NSString*)languageCode isBold:(BOOL)bold {
    
    if ([languageCode caseInsensitiveCompare:@"en"] == NSOrderedSame) {
        return 16;
    } else if ([languageCode caseInsensitiveCompare:@"si"] == NSOrderedSame) {
        if (bold) {
            return 15;
        } else {
            return 13;
        }
    } else if ([languageCode caseInsensitiveCompare:@"ta-LK"] == NSOrderedSame) {
        if (bold) {
            return 18;
        } else {
            return 16;
        }
    }
    else {
        return 14; //Default font size
    }
}


+(NSAttributedString*)underlineString:(NSString*)string startPos:(int)start length:(int)length {
    
    if ([GlobalVariableAndMethod validateString:string]) {
        
        NSMutableAttributedString *underliningString = [[NSMutableAttributedString alloc] initWithString:string];
        [underliningString addAttribute:NSUnderlineStyleAttributeName
                                  value:[NSNumber numberWithInt:1]
                                  range:(NSRange){start,length}];
        return [underliningString copy];
        
    } else {
        
        return [[NSMutableAttributedString alloc] initWithString:@""];
    }
}


+(NSAttributedString*)htmlFormattingString:(NSString*)string hexColorCode:(NSString*)hexColorCode {
    
    if ([GlobalVariableAndMethod validateString:string]) {

        NSString * htmlString = [NSString stringWithFormat:@"<html><body><font color=\"%@\">%@</font></body></html>", hexColorCode, string];
        NSAttributedString * attrStr = [[NSAttributedString alloc] initWithData:[htmlString dataUsingEncoding:NSUnicodeStringEncoding] options:@{ NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType } documentAttributes:nil error:nil];
        
        return attrStr;
        
    } else {
        
        return [[NSMutableAttributedString alloc] initWithString:@""];
    }
}

@end
