#include "ALL_Includes.h"//包含所需的头文件

void ALL_Config(void);
void RCC_Config(void);

int main(void)
{	
	u8 i;
	ALL_Config();
	
	while(1)
	{
		for(i=0;i<255;i++){
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,i,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255-i,255,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255,i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255-i,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,i,0,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,0,255-i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,i,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255-i,255,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255,i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255-i,255);
			WS2812_Reset(GPIOA,GPIO_Pin_4);
			delay_us(400);
		}
		
		for(i=0;i<255;i++){
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255-i,255,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255,i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255-i,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,i,0,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,0,255-i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,i,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255-i,255,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255,i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255-i,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,i,0,255);
			WS2812_Reset(GPIOA,GPIO_Pin_4);
			delay_us(400);
		}
		
		for(i=0;i<255;i++){
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255,i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255-i,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,i,0,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,0,255-i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,i,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255-i,255,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255,i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255-i,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,i,0,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,0,255-i);
			WS2812_Reset(GPIOA,GPIO_Pin_4);
			delay_us(400);
		}
		
		for(i=0;i<255;i++){
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255-i,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,i,0,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,0,255-i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,i,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255-i,255,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255,i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255-i,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,i,0,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,0,255-i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,i,0);
			WS2812_Reset(GPIOA,GPIO_Pin_4);
			delay_us(400);
		}
		
		for(i=0;i<255;i++){
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,i,0,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,0,255-i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,i,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255-i,255,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255,i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255-i,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,i,0,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,0,255-i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,i,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255-i,255,0);
			WS2812_Reset(GPIOA,GPIO_Pin_4);
			delay_us(400);
		}
		
		for(i=0;i<255;i++){
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,0,255-i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,i,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255-i,255,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255,i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255-i,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,i,0,255);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,0,255-i);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255,i,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,255-i,255,0);
			Send_24bits_to_WS2812(GPIOA,GPIO_Pin_4,0,255,i);
			WS2812_Reset(GPIOA,GPIO_Pin_4);
			delay_us(400);
		}


	}
}

/************************
函数功能：总初始化
输入参数：无
输出参数：无
备    注：无
************************/
void ALL_Config(void)
{
    Delay_Init(48);
		LED_Init();
		WS2812_Config(GPIOA,GPIO_Pin_4);
}

void RCC_Config()
{
	
}
