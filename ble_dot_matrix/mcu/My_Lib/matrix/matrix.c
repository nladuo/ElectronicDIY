#include "matrix.h"

void Matrix_Init(void)
{
		GPIO_InitTypeDef GPIO_InitStructure;

	  RCC_AHBPeriphClockCmd(MATRIX_GPIO_CLK, ENABLE);
	
    GPIO_InitStructure.GPIO_Pin = LEDARRAY_D | LEDARRAY_C 
																|LEDARRAY_B|LEDARRAY_A
																|LEDARRAY_G|LEDARRAY_DI
																|LEDARRAY_CLK|LEDARRAY_LAT;
    GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
    GPIO_InitStructure.GPIO_Mode = GPIO_Mode_OUT;
    GPIO_InitStructure.GPIO_OType = GPIO_OType_PP;	
    GPIO_Init(LED_PORT, &GPIO_InitStructure);
	
}

//************************************************************
//num为字数  dat[][32]为字模的名称
//*************************************************************
void Matrix_Display(u8 dat[][32])					
{
	u8 i,j;
	for( i = 0 ; i < 16 ; i++ )
	{
		GPIO_SetBits(MATRIX_PORT,LEDARRAY_G);//更新数据时候关闭显示。等更新完数据，打开138显示行。防止重影。

		//Display_Buffer[0] = dat[0][i];		
		//Display_Buffer[1] = dat[0][i+16];

		Matrix_Send(dat[0][i+16]);
		Matrix_Send(dat[0][i]);

		GPIO_SetBits(MATRIX_PORT,LEDARRAY_LAT);//锁存数据
		delay_us(1);
	
		GPIO_ResetBits(MATRIX_PORT,LEDARRAY_LAT);
		delay_us(1);

		Matrix_Scan_Line(i);						//选择第i行

		GPIO_ResetBits(MATRIX_PORT,LEDARRAY_G);
		
		for( j = 0 ; j < 100 ; j++);		//延时一段时间，让LED亮起来。				
	}	
}

//****************************************************
//扫描某一行
//****************************************************
void Matrix_Scan_Line(u8 m)
{	
	switch(m)
	{
		case 0:
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_A);				
			break;
		
		case 1:	
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_A);		
			break;
		
		case 2:		
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_A);					
			break;
		
		case 3:		
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_A);	
			break;
		
		case 4:
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_A);		
			break;
		case 5:
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_A);		
			break;
		case 6:
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_A);		
			break;
		case 7:
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_A);	
			break;
		case 8:
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_A);		
			break;
		case 9:
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_A);			
			break;	
		case 10:
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_A);		
			break;
		case 11:
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_A);			
			break;
		case 12:
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_A);		
			break;
		case 13:
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_A);			
			break;
		case 14:
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_A);			
			break;
		case 15:
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_D);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_C);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_B);
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_A);	 		
			break;	
	}
}

//****************************************************
//发送数据
//****************************************************
void Matrix_Send(u8 dat)
{
	u8 i;
//	char j = 0;
	GPIO_ResetBits(MATRIX_PORT,LEDARRAY_CLK);
	delay_us(1);	
	GPIO_ResetBits(MATRIX_PORT,LEDARRAY_LAT);
	delay_us(1);

	for( i = 0 ; i < 8 ; i++ )
	{
		if( dat&0x01 )
		{
			GPIO_SetBits(MATRIX_PORT,LEDARRAY_DI);
		}
		else
		{
			GPIO_ResetBits(MATRIX_PORT,LEDARRAY_DI);
		}

		GPIO_SetBits(MATRIX_PORT,LEDARRAY_CLK);//上升沿发送数据
		delay_us(1);
		GPIO_ResetBits(MATRIX_PORT,LEDARRAY_CLK);
		delay_us(1);

		dat >>= 1;
			
	}			
}
