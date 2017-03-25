#ifndef __WS2812_H
#define __WS2812_H
#include "ALL_Includes.h"

extern void WS2812_Config(GPIO_TypeDef* GPIOx, u16 GPIO_Pinx);//????,????IO??
extern void WS2812_Write0(GPIO_TypeDef* GPIOx, u16 GPIO_Pinx);
extern void WS2812_Write1(GPIO_TypeDef* GPIOx, u16 GPIO_Pinx);
extern void WS2812_Reset(GPIO_TypeDef* GPIOx, u16 GPIO_Pinx);
extern void Send_8bits_to_WS2812(GPIO_TypeDef* GPIOx, u16 GPIO_Pinx, u8 temp);
extern void Send_24bits_to_WS2812(GPIO_TypeDef* GPIOx, u16 GPIO_Pinx, u8 redTemp, u8 greenTemp, u8 blueTemp);
extern void Send_Serial_Data_to_WS2812(GPIO_TypeDef* GPIOx, u16 GPIO_Pinx, u8 Size, u8 Content[][3]);

#endif