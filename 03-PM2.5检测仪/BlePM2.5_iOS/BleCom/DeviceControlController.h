//
//  DeviceControlController.h
//  BleCom
//
//  Created by kalen blue on 15-10-2.
//  Copyright (c) 2015年 TRY. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SerialGATT.h"
#import "LeSensorObserver.h"

//#define RSSI_THRESHOLD -60
//#define WARNING_MESSAGE @"z"

@class CBPeripheral;
@class SerialGATT;

@interface DeviceControlController : UIViewController<BTSmartSensorDelegate>

@property (strong, nonatomic) SerialGATT *sensor;

@property (weak, nonatomic) IBOutlet UITextView *showResTv;

@end
