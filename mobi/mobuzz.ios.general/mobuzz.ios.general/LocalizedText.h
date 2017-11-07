//
//  LocalizedText.h
//  mobuzz.ios.general
//
//  Created by Vajira on 1/9/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface LocalizedText : NSObject

+(NSString*)translateTextByTextIdentifier:(NSString*)textIdentifier byLanguageCode:(NSString*)languageCode;
+(NSString*)translateTextByTextIdentifier:(NSString*)textIdentifier byLanguage:(NSString*)language;
+(NSString*)getLanuageCode:(NSString*)language;
+(NSString*)getFontNameByLanguageCode:(NSString*)languageCode isBold:(BOOL)bold;
+(int)getFontSizeByLanguageCode:(NSString*)languageCode isBold:(BOOL)bold;
+(NSAttributedString*)underlineString:(NSString*)string startPos:(int)start length:(int)length;
+(NSAttributedString*)htmlFormattingString:(NSString*)string hexColorCode:(NSString*)hexColorCode;

@end
