/**
 * 攀藤G5S传感器模块
 */
void shift_data(int *temp_datas, int data);

void loopPTG5S(int *temp_datas, int* pm25, int* pm10, int* oxymethylene){
  while(Serial.available()){
    int data = Serial.read();
    shift_data(temp_datas, data);
    if(temp_datas[0] == 66 && temp_datas[1] == 77){
      *pm25 = temp_datas[12] * 256 + temp_datas[13];
      *pm10 = temp_datas[14] * 256 + temp_datas[15];
      *oxymethylene = temp_datas[28] * 256 + temp_datas[29];
    }
  }
}


void shift_data(int *temp_datas, int data){
  for(int i = 0; i <= 30; i++){
    temp_datas[i] = temp_datas[i + 1];
  }
  temp_datas[31] = data;
}

