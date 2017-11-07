//
//  ComplaintDescription.h
//  mobuzz.ios.general
//
//  Created by Vajira on 16/9/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ComplaintDescription : NSObject

@property(readonly) NSString *date;

-(instancetype)initWithParamsDate:(NSString *)strDate address:(NSString *)strAddress cmcMessage:(NSString *)strCmcmessage ward:(NSString *)strWard photo:(NSString *)strPhoto gps:(NSString *)strGps;

@end
