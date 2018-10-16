// pages/control/control.js
import { updateBuffer } from "./data"

let app = getApp();

Page({
  data: {
    deviceId: '',           // BLE设备id
    name: '',               // BLE设备名称
    serviceId: '',          // 服务ID，以FFE0开头
    characteristicId: '',   // 特征ID，以FFE1开头 
    result: '',             // 显示text到界面
    isG5S: false            // 是否测量甲醛
  },

  onLoad: function (options) {
    let that = this;
    console.log("onLoad");
    console.log('deviceId=' + options.deviceId);
    console.log('name=' + options.name);
    that.setData({deviceId: options.deviceId});
    // 读传感器类型
    // let value = wx.getStorageSync('isG5S')
    console.log('isG5S=' + app.globalData.isG5S);
    that.setData({ isG5S: app.globalData.isG5S });
    /**
     * 设置NavigationBar的Title
     */
    wx.setNavigationBarTitle({
      title: options.name,
    });
    /**
     * 监听设备的连接状态
     */
    wx.onBLEConnectionStateChange((res) => {
      console.log(`Connected status: ${res.connected}`)
    })
    /**
     * 连接到BLE设备
     */
    wx.createBLEConnection({
      deviceId: that.data.deviceId,
      success: function(res) {
        wx.getBLEDeviceServices({
          deviceId: that.data.deviceId,
          success: function(res) {
            console.log(res.services);
            // 找服务ID
            res.services.forEach((service) => {
              console.log("serviceId->", service.uuid)
              if (service.uuid.indexOf("FFE0") != -1) {
                that.setData({ serviceId: service.uuid });
              }
            })
            // 没找到，返回
            if (that.data.serviceId == '') {
              wx.showModal({
                title: '错误',
                content: '没找到指定服务ID, 请检查蓝牙型号是否正确',
                showCancel: false,
                success(res) {
                  wx.redirectTo({
                    url: '../scan/scan'
                  })
                }
              })
            }
            // 等待加载框
            wx.showLoading({
              title: '连接中...',
            })
            /**
             * 设置3秒延时，根据服务ID获取特征ID
             */
            setTimeout(() => {
              wx.hideLoading(); //隐藏加载框
              wx.getBLEDeviceCharacteristics({
                deviceId: that.data.deviceId,
                serviceId: that.data.serviceId,
                success: function (res) {
                  /**
                   * 找characteristicId
                   */
                  res.characteristics.forEach((characteristic) => {
                    console.log("characteristic->", characteristic.uuid);
                    if (characteristic.uuid.indexOf("FFE1") != -1) {
                      that.setData({
                        characteristicId: characteristic.uuid,
                      });
                    }
                    // 没找到，返回
                    if (that.data.characteristicId == '') {
                      wx.showModal({
                        title: '错误',
                        content: '没找到指定服务ID, 请检查蓝牙型号是否正确',
                        showCancel: false,
                        success(res) {
                          wx.redirectTo({
                            url: '../scan/scan'
                          })
                        }
                      })
                    }
                    /**
                     * 设置接受数据通知
                     */
                    wx.notifyBLECharacteristicValueChange({
                      deviceId: that.data.deviceId,
                      serviceId: that.data.serviceId,
                      characteristicId: that.data.characteristicId,
                      state: true,
                      success: function (res) {
                        /**
                         * 监听接受的数据，渲染到界面
                         */
                        wx.onBLECharacteristicValueChange(function (characteristic) {
                          let x = new Uint8Array(characteristic.value);
                          let str = ""
                          for (let i = 0; i < x.length; i++) {
                            /**
                             * 更新到Buffer中
                             */
                            updateBuffer(x[i], that.data.isG5S, (text)=>{
                              that.setData({ result: text });
                            })
                            let ch = x[i].toString(16)
                            if (ch.length == 1) {
                              str += "0" + ch;
                            } else {
                              str += ch;
                            }
                            str += " ";
                          }
                          console.log(str);
                        })
                      },
                    })

                  })
                },
              })
            }, 3000)

          }
        })
      }
    })
  
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    wx.closeBLEConnection({
      deviceId: this.data.deviceId
    })
  }
})