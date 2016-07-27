#include "DHT11.h"
#include "PT_G5S.h"

int     humidity;     //湿度
int     temperature;  //温度
int     pm25;         //PM2.5浓度
int     pm10;         //PM10浓度
int     oxymethylene; //甲醛浓度
int     temp_datas[32];

void setup () {
  Serial.begin(9600);
}

void loop () {
  //获取
  loopDHT11(&humidity, &temperature);
  loopPTG5S(temp_datas, &pm25, &pm10, &oxymethylene);
  Serial.println("-------------------------");
  Serial.print("temperature:");
  Serial.println(temperature);
  Serial.print("humidity:");
  Serial.println(humidity);
  Serial.print("pm2.5:");
  Serial.println(pm25);
  Serial.print("pm10:");
  Serial.println(pm10);
  Serial.print("oxymethylene:");
  Serial.print(oxymethylene);
  Serial.println(" / 1000");

  delay(3000);
}
