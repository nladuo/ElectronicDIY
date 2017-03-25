#include "ws2812.h"

void WS2812_Config(GPIO_TypeDef* GPIOx, u16 GPIO_Pinx)
{
	GPIO_InitTypeDef GPIO_InitStructure;
  GPIO_InitStructure.GPIO_Pin = GPIO_Pinx;
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_10MHz;
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_OUT;
  GPIO_InitStructure.GPIO_OType = GPIO_OType_PP;	
  GPIO_Init(GPIOx, &GPIO_InitStructure);
	GPIO_ResetBits(GPIOx, GPIO_Pinx);
}

void WS2812_Write0(GPIO_TypeDef* GPIOx, u16 GPIO_Pinx)
{
	GPIO_SetBits(GPIOx,GPIO_Pinx);
	
	GPIO_ResetBits(GPIOx,GPIO_Pinx);
	GPIO_ResetBits(GPIOx,GPIO_Pinx); 
	GPIO_ResetBits(GPIOx,GPIO_Pinx);//800ns
}

void WS2812_Write1(GPIO_TypeDef* GPIOx, u16 GPIO_Pinx)
{
	GPIO_SetBits(GPIOx,GPIO_Pinx); 	
	GPIO_SetBits(GPIOx,GPIO_Pinx); //690ns
	
	GPIO_ResetBits(GPIOx,GPIO_Pinx); 
}
void WS2812_Reset(GPIO_TypeDef* GPIOx, u16 GPIO_Pinx)
{
	GPIO_ResetBits(GPIOx,GPIO_Pinx); 
	delay_us(52);//delay 50us“‘…œ
}
void Send_8bits_to_WS2812(GPIO_TypeDef* GPIOx, u16 GPIO_Pinx, u8 temp)
{
	u8 i;
	for(i = 0; i < 8; i++){
		if(temp & 1<<(7-i) ){
			WS2812_Write1(GPIOx, GPIO_Pinx);
		}else{
			WS2812_Write0(GPIOx, GPIO_Pinx);
		}
	}
}

void Send_24bits_to_WS2812(GPIO_TypeDef* GPIOx, u16 GPIO_Pinx, 
													u8 redTemp, u8 greenTemp, u8 blueTemp)
{
	Send_8bits_to_WS2812(GPIOx,GPIO_Pinx,greenTemp);
	Send_8bits_to_WS2812(GPIOx,GPIO_Pinx,redTemp);
	Send_8bits_to_WS2812(GPIOx,GPIO_Pinx,blueTemp);
}

void Send_Serial_Data_to_WS2812(GPIO_TypeDef* GPIOx, u16 GPIO_Pinx, u8 Size, u8 Content[][3])
{
	u8 i=0;
	for(i = 0; i < Size; i++)
	{
		Send_24bits_to_WS2812(GPIOx, GPIO_Pinx, Content[i][0], Content[i][1], Content[i][2]);
	}
	WS2812_Reset(GPIOx, GPIO_Pinx);
}
