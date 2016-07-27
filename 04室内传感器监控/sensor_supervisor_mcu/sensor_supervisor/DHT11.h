/**
 * DHT11传感器模块
 * 代码修改自:http://www.arduino.cn/forum.php?mod=viewthread&tid=3604
 */

#define DHT11_PIN 2

int temp;//温度
int humi;//湿度
int tol;//校对码
int j;
unsigned int loopCnt;
int chr[40] = {0};//创建数字数组，用来存放40个bit
unsigned long time;

void loopDHT11(int* humidity, int* temperature){
  //设置2号接口模式为：输出
  //输出低电平20ms（>18ms）
  //输出高电平40μs
  pinMode(DHT11_PIN,OUTPUT);
  digitalWrite(DHT11_PIN,LOW);
  delay(20);
  digitalWrite(DHT11_PIN,HIGH);
  delayMicroseconds(40);
  digitalWrite(DHT11_PIN,LOW);
  //设置2号接口模式：输入
  pinMode(DHT11_PIN,INPUT);
  //高电平响应信号
  loopCnt=10000;
  while(digitalRead(DHT11_PIN) != HIGH){
    if(loopCnt-- == 0){
      //如果长时间不返回高电平，输出个提示，返回
      return;
    }
  }
  //低电平响应信号
  loopCnt=30000;
  while(digitalRead(DHT11_PIN) != LOW){
    if(loopCnt-- == 0){
      //如果长时间不返回低电平，输出个提示，返回。
      return;
    }
  }
  //开始读取bit1-40的数值  
  for(int i=0;i<40;i++){
    while(digitalRead(DHT11_PIN) == LOW){}
    //当出现高电平时，记下时间“time”
    time = micros();
    while(digitalRead(DHT11_PIN) == HIGH)
    {}
    //当出现低电平，记下时间，再减去刚才储存的time
    //得出的值若大于50μs，则为‘1’，否则为‘0’
    //并储存到数组里去
    if (micros() - time >50){
      chr[i]=1;
    }else{
      chr[i]=0;
    }
  }
  
  //湿度，8位的bit，转换为数值
  humi=chr[0]*128+chr[1]*64+chr[2]*32+chr[3]*16+chr[4]*8+chr[5]*4+chr[6]*2+chr[7];
  //温度，8位的bit，转换为数值
  temp=chr[16]*128+chr[17]*64+chr[18]*32+chr[19]*16+chr[20]*8+chr[21]*4+chr[22]*2+chr[23];
  //校对码，8位的bit，转换为数值
  tol=chr[32]*128+chr[33]*64+chr[34]*32+chr[35]*16+chr[36]*8+chr[37]*4+chr[38]*2+chr[39];

  if( (humi + temp) == tol){
    *humidity = humi;
    *temperature = temp;
  }
  
}

