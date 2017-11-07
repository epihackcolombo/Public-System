//
//  GlobalVariableAndMethod.m
//  mobuzz.ios.general
//
//  Created by Vajira on 27/7/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import "GlobalVariableAndMethod.h"

@interface GlobalVariableAndMethod ()

@end

@implementation GlobalVariableAndMethod


//Create MD5 for the input string
+(NSString*)md5HexDigest:(NSString*)input {
    
    const char* str = [input UTF8String];
    unsigned char result[CC_MD5_DIGEST_LENGTH];
    CC_MD5(str, (CC_LONG)strlen(str), result);
    
    NSMutableString *ret = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH*2];
    for(int i = 0; i<CC_MD5_DIGEST_LENGTH; i++) {
        [ret appendFormat:@"%02x",result[i]];
    }
    return [ret uppercaseString];
}


+(NSString*)getCurrentDateTime:(NSString *)dateFormat {
    
    NSDateFormatter* df = [[NSDateFormatter alloc]init];
    df.dateFormat = dateFormat;
    return [df stringFromDate:[NSDate date]];
}


+(NSString*)getDeviceName {
    
    NSString *deviceName;
    
    @try {
        struct utsname systemInfo;
        uname(&systemInfo);
        
        deviceName = [NSString stringWithCString:systemInfo.machine encoding:NSUTF8StringEncoding];
    }
    @catch (NSException *exception) {
        //If exception happens, simply ignore the model name
        deviceName = [NSString stringWithFormat:@""];
    }
    @finally {
        return deviceName;
    }
    
}


+(NSString *)getUUID {
    
    NSString *universallyUID;
    
    @try {
        CFUUIDRef newUniqueId = CFUUIDCreate(kCFAllocatorDefault);
        universallyUID = (__bridge_transfer NSString*)CFUUIDCreateString(kCFAllocatorDefault, newUniqueId);
        CFRelease(newUniqueId);
    }
    @catch (NSException *exception) {
        //Minor chance to happen, but if exception happens, ignore and continue work
        universallyUID = [NSString stringWithFormat:@""];
    }
    @finally {
        return universallyUID;
    }
}

+(BOOL)isNetworkAvailable {
    
    //Data connection check with a small web-call
    NSURL *scriptUrl = [NSURL URLWithString:@kCONN_URL_CHECK];
    NSData *data = [NSData dataWithContentsOfURL:scriptUrl];
    if (data) {
        return TRUE;
    }
    else {
        return FALSE;
    }
    
}


+(BOOL)isUserTextValied:(NSString *)inputText {
    
    int validateMinTextSize = (int)kVALIDATE_MIN_TEXT_SIZE;
    
    if ( ![inputText isEqual:[NSNull null]] && (inputText.length>validateMinTextSize) ) {
        return TRUE;
    }
    else {
        return FALSE;
    }
}



+(UIColor *)getMobuzzColor: (NSString *)byName {
    
    if ([byName isEqualToString:@"yellow"]) {
        return [UIColor colorWithRed:255.0f/255.0f green:186.0f/255.0f blue:67.0f/255.0f alpha:1.0f];
    }
    else if ([byName isEqualToString:@"ash"]) {
        return [UIColor colorWithRed:100.0f/255.0f green:100.0f/255.0f blue:100.0f/255.0f alpha:1.0f];
    }
    else if ([byName isEqualToString:@"disable"]) {
        return [self colorFromHexString:@"#C7C7CF"];
    }
    else {
        return [UIColor colorWithRed:100.0f/255.0f green:100.0f/255.0f blue:100.0f/255.0f alpha:0.0f]; //transparent
    }
}


+(UIColor *)colorFromHexString:(NSString *)hexString {
    unsigned rgbValue = 0;
    NSScanner *scanner = [NSScanner scannerWithString:hexString];
    [scanner setScanLocation:1]; // bypass '#' character
    [scanner scanHexInt:&rgbValue];
    return [UIColor colorWithRed:((rgbValue & 0xFF0000) >> 16)/255.0 green:((rgbValue & 0xFF00) >> 8)/255.0 blue:(rgbValue & 0xFF)/255.0 alpha:1.0];
    
    //return [UIColor colorWithRed:((rgbValue & 0xFF000000) >> 24)/255.0 green:((rgbValue & 0xFF0000) >> 16)/255.0 blue:((rgbValue & 0xFF00) >> 8)/255.0 alpha:(rgbValue & 0xFF)/255.0];
}

//Validate string before use for null or empty
+(BOOL)validateString:(NSString *)byString {
    if (byString == (id)[NSNull null] || byString.length == 0 ) {
        return FALSE;
    }
    else {
        return TRUE;
    }
}

@end
