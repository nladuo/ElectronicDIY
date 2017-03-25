#include "IOSTM8S103F3.h"
#define BIT(x)      (1<<(x))
#define IN1_HIGH    PC_ODR|=BIT(3)
#define IN2_HIGH    PC_ODR|=BIT(4)
#define IN3_HIGH    PC_ODR|=BIT(5)
#define IN4_HIGH    PC_ODR|=BIT(6)
#define IN1_LOW     PC_ODR&=~BIT(3)
#define IN2_LOW     PC_ODR&=~BIT(4)
#define IN3_LOW     PC_ODR&=~BIT(5)
#define IN4_LOW     PC_ODR&=~BIT(6)
void setVelocity(int,int,int,int);

int count=0;
int pwm_in1=0;
int pwm_in2=0;
int pwm_in3=0;
int pwm_in4=0;
int velocity=10;

//---  微秒级延时--------------------------   
void delay_us(void)   
{    
    asm("nop"); //一个asm("nop")函数经过示波器测试代表100ns   
    asm("nop");   
    asm("nop");   
    asm("nop");    
}   
  
//---- 毫秒级延时程序-----------------------   
void delay_ms(unsigned int ms)
{
       unsigned char i;
       while(ms!=0)
       {
              for(i=0;i<250;i++)
              {}
              for(i=0;i<75;i++)
              {}
              ms--;
       }
}


void Init_Timer4(void)
{
    
    TIM4_CNTR=0; //计数器值  
    TIM4_ARR=0xFA; //自动重装寄存器  250，产生125次定时1S  
    TIM4_PSCR=0x07; //预分频系数为128  
    TIM4_EGR=0x01; //手动产生一个更新事件,用于PSC生效       注意，是手动更新  
      
    TIM4_IER=0x01; //更新事件中断 使能  
    TIM4_CR1=0x01; //使能计时器，TIM4_CR0停止计时器  

}

void Port_Config(void)
{
    PC_DDR|=BIT(3)|BIT(4)|BIT(5)|BIT(6);
    PC_CR1|=BIT(3)|BIT(4)|BIT(5)|BIT(6);
    PC_CR2|=0x00;
    
    //PB_DDR|=BIT(4)|BIT(5);
    //PB_CR1|=BIT(4)|BIT(5);
    //PB_CR2|=0x00;
}



void Init_UART1(void)
{
      UART1_CR1=0x00;
      UART1_CR2=0x00;
      UART1_CR3=0x00;
      // 设置波特率，必须注意以下几点：
      // (1) 必须先写BRR2
      // (2) BRR1存放的是分频系数的第11位到第4位，
      // (3) BRR2存放的是分频系数的第15位到第12位，和第3位
      // 到第0位
      // 例如对于波特率位9600时，分频系数=2000000/9600=208
      // 对应的十六进制数为00D0，BBR1=0D,BBR2=00

      UART1_BRR2=0x00;
      UART1_BRR1=0x0d;

      UART1_CR2=0x2c;//允许接收，发送，开接收中断
}

int main( void )
{
    Init_Timer4();
    Init_UART1();
    Port_Config();
    asm("rim");
    while(1){}
    return 0;
}

void setVelocity(int aVal,int bVal,int cVal,int dVal)
{
    pwm_in1=aVal;
    pwm_in2=bVal;
    pwm_in3=cVal;
    pwm_in4=dVal;
}

#pragma vector= UART1_R_OR_vector//0x19
__interrupt void UART1_R_OR_IRQHandler(void)
{
      unsigned char dat;
      unsigned char temp;
      dat=UART1_DR;
      //UART1_sendchar(ch+1);
      switch(dat)
      {
      case 0x00: setVelocity(0,0,0,0);break;
      case 0x01: setVelocity(0,10,0,10);break;//前
      case 0x02: setVelocity(10,0,10,0);break;//后
      case 0x03: setVelocity(0,10,0,4);break;//左
      case 0x04: setVelocity(0,4,0,10);break;//you
      //default:temp=dat%100;
      
      }
      return;
}

#pragma vector=TIM4_OVR_UIF_vector
__interrupt void TIM4_OVR_UIF_IRQHandler(void)
{
      count++;
      TIM4_SR=0x00;//清除中断标志
      //产生四路pwm
      if(count<pwm_in1)
        IN1_HIGH;
      else
        IN1_LOW;
      
      if(count<pwm_in2)
        IN2_HIGH;
      else
        IN2_LOW;
      
      if(count<pwm_in3)
        IN3_HIGH;
 
      else
        IN3_LOW;
      
      if(count<pwm_in4)
        IN4_HIGH;
      else
        IN4_LOW;
      
      if(count == 10)
          count=0;
}
