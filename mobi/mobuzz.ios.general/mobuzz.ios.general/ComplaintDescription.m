//
//  ComplaintDescription.m
//  mobuzz.ios.general
//
//  Created by Vajira on 16/9/15.
//  Copyright (c) 2015 cosmic. All rights reserved.
//

#import "ComplaintDescription.h"

@interface ComplaintDescription()

@property(readwrite) NSString *date;
@property NSString *address;
@property NSString *cmcmessage;
@property NSString *ward;
@property NSString *photo;
@property NSString *gps;


@end

@implementation ComplaintDescription


-(instancetype)initWithParamsDate:(NSString *)strDate address:(NSString *)strAddress cmcMessage:(NSString *)strCmcmessage ward:(NSString *)strWard photo:(NSString *)strPhoto gps:(NSString *)strGps {
    
    self = [super init];
    self.date = strDate;
    self.address = strAddress;
    self.cmcmessage = strCmcmessage;
    self.ward = strWard;
    self.photo = strPhoto;
    self.gps = strGps;
    
    return self;
}

-(instancetype)init {
    
    NSException *exception = [NSException exceptionWithName:@"Invalid method called" reason:@"Use the initWithParams method" userInfo:nil];
    [exception raise];
    
    return self;
}

@end
