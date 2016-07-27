//
//  DeviceControlController.m
//  BleCom
//
//  Created by kalen blue on 15-10-2.
//  Copyright (c) 2015年 TRY. All rights reserved.
//
#import "DeviceControlController.h"

@interface DeviceControlController ()

@end

@implementation DeviceControlController

@synthesize sensor;

@synthesize showResTv;

- (instancetype)initWithCoder:(NSCoder *)coder
{
    self = [super initWithCoder:coder];
    if (self) {
        //
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    if ([self respondsToSelector:@selector(edgesForExtendedLayout)]){
        self.edgesForExtendedLayout = UIRectEdgeNone;
    }
    self.title = self.sensor.activePeripheral.name;
    //添加当前viewcontroller到观察者中，接收蓝牙状态的改变
    [[LeSensorObserver getInstance] addLeSensorObserver:self];

    self.showResTv.editable = FALSE;
    self.showResTv.textAlignment = NSTextAlignmentCenter;

}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}


#pragma mark - BTSmartSensorDelegate
-(void)setConnect
{
    //receiveDataTv.text = @"Device Connected\n";
}

-(void)setDisconnect
{
    //receiveDataTv.text = [receiveDataTv.text stringByAppendingString:@"\nDevice Lost\n"];
}

char buffer[16];

int checkOutOneSequence(){
    return (buffer[0] == 0x42) && (buffer[1] == 0x4D) && (buffer[2] == 0x00) && (buffer[3] == 0x1C);
}

void shiftLeft(char nowNum){
    for (int i = 0; i < 15; i++) {
        buffer[i] = buffer[i + 1];
    }
    buffer[15] = nowNum;
}

int getIntValByTwoBytes(char high, char low){
    int retVal = low & 0xFF;
    retVal += ((int)high & 0xFF) << 8;
    return retVal;
}

//recv data
-(void) serialGATTCharValueUpdated:(NSString *)UUID value:(NSData *)data
{
    char *datas = (char *)data.bytes;
    for (int i = 0; i < data.length; i++) {
        char nowNum = datas[i];
        shiftLeft(nowNum);
        if (checkOutOneSequence()) {
            NSString *showStr = [[NSString alloc] initWithFormat:@"美国标准：\nPM1.0 : %dug/m3\nPM2.5 : %dug/m3\nPM10 : %dug/m3\n\n中国标准：\nPM1.0 : %dug/m3\nPM2.5 : %dug/m3\nPM10 : %dug/m3\n",
                                 getIntValByTwoBytes(buffer[4], buffer[5]), getIntValByTwoBytes(buffer[6], buffer[7]),getIntValByTwoBytes(buffer[8], buffer[9]),
                                 getIntValByTwoBytes(buffer[10], buffer[11]),getIntValByTwoBytes(buffer[12], buffer[13]),getIntValByTwoBytes(buffer[14], buffer[15])];
            showResTv.text = showStr;
        }

    }

}

- (void) peripheralFound:(CBPeripheral *)peripheral{} // do nothing



@end
