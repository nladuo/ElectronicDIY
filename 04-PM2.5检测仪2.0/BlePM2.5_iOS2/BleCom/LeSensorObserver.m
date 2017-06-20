//
//  LeSensorObserver.m
//  BlePM2.5
//
//  Created by 刘嘉铭 on 2017/6/20.
//  Copyright © 2017年 KalenBlue. All rights reserved.
//

#import "LeSensorObserver.h"

#import "LeSensorObserver.h"

@implementation LeSensorObserver

@synthesize observers;

static LeSensorObserver* sensorObserver = nil;


- (instancetype)init
{
    self = [super init];
    if (self) {
        observers = [[NSMutableArray alloc] init];
    }
    return self;
}

+(LeSensorObserver*) getInstance
{
    if (sensorObserver == nil) {
        sensorObserver = [[LeSensorObserver alloc] init];
    }
    
    return sensorObserver;
}


-(void) addLeSensorObserver: (id<BTSmartSensorDelegate>)observed
{
    [observers addObject:observed];
}

-(void) removeLeSensorObserver: (id<BTSmartSensorDelegate>)observed
{
    if ([observers containsObject:observed]) {
        [observers removeObject:observed];
    }
}

-(void)setConnect
{
    id<BTSmartSensorDelegate> observed;
    for (int i = 0; i < observers.count; i++) {
        observed = [observers objectAtIndex:i];
        [observed setConnect];
    }
}

-(void)setDisconnect
{
    id<BTSmartSensorDelegate> observed;
    for (int i = 0; i < observers.count; i++) {
        observed = [observers objectAtIndex:i];
        [observed setDisconnect];
    }
}

- (void) peripheralFound:(CBPeripheral *)peripheral
{
    id<BTSmartSensorDelegate> observed;
    for (int i = 0; i < observers.count; i++) {
        observed = [observers objectAtIndex:i];
        [observed peripheralFound:peripheral];
    }
}

- (void) serialGATTCharValueUpdated: (NSString *)UUID value: (NSData *)data
{
    id<BTSmartSensorDelegate> observed;
    for (int i = 0; i < observers.count; i++) {
        observed = [observers objectAtIndex:i];
        [observed serialGATTCharValueUpdated:UUID value:data];
    }
}


@end
