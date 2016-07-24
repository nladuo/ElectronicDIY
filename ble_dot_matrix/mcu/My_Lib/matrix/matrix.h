#ifndef __matrix_H
#define __matrix_H 
#include "ALL_Includes.h"//包含所需的头文件
//IO配置
#define MATRIX_PORT   		GPIOA
#define MATRIX_GPIO_CLK		RCC_AHBPeriph_GPIOA

#define LEDARRAY_D  	GPIO_Pin_0
#define LEDARRAY_C 		GPIO_Pin_1
#define LEDARRAY_B 		GPIO_Pin_2
#define LEDARRAY_A 		GPIO_Pin_3
#define LEDARRAY_G 		GPIO_Pin_4
#define LEDARRAY_DI 	GPIO_Pin_5
#define LEDARRAY_CLK 	GPIO_Pin_6
#define LEDARRAY_LAT 	GPIO_Pin_7


//函数或者变量声明
extern void Matrix_Init(void);
extern void Matrix_Send(u8 dat);
extern void Matrix_Scan_Line(u8 m);
extern void Matrix_Display(u8 dat[][32]);

#endif
