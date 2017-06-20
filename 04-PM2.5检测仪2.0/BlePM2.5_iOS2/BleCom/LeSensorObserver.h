//
//  LeSensorObserver.h
//  BlePM2.5
//
//  Created by 刘嘉铭 on 2017/6/20.
//  Copyright © 2017年 KalenBlue. All rights reserved.
//


#import <Foundation/Foundation.h>
#import "SerialGATT.h"

/*
 * 用于通知观察者们接收到消息
 *
 */
@interface LeSensorObserver : NSObject<BTSmartSensorDelegate>

+(LeSensorObserver*) getInstance;
-(void) addLeSensorObserver: (id<BTSmartSensorDelegate>)observed;
-(void) removeLeSensorObserver: (id<BTSmartSensorDelegate>)observed;

@property (nonatomic, retain) NSMutableArray *observers;
@end
