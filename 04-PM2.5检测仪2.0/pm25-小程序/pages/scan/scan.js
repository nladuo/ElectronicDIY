// pages/scan/scan.js
Page({
  data: {
    is_scan: false,
    device_list: []
  },
  onLoad: function (options) {
    let that = this;
    /**
     * 打开蓝牙适配器
     */
    wx.openBluetoothAdapter({
      success(res) {
        console.log("openBluetoothAdapter");
        console.log(res);
        /**
         * 开始扫描
         */
        wx.startBluetoothDevicesDiscovery({
          success: function(res) {
            that.setData({is_scan: true})
            console.log("startBluetoothDevicesDiscovery");
            console.log(res);
            /**
             * 每隔一秒获取一次设备
             */
            setInterval(()=>{
              if (that.data.is_scan) {
                wx.getBluetoothDevices({
                  success: function (res) {
                    console.log("getBluetoothDevices");
                    console.log(res);
                    that.setData({
                      device_list: res.devices
                    });
                    console.log(that.data.device_list);
                  },
                })
              }
            }, 1000)
          }
        })
      },
      fail(res) {
        /**
         * 失败回主页
         */
        wx.showModal({
          title: '提示',
          content: '打开蓝牙失败, 请开启蓝牙重试',
          showCancel: false,
          success(res) {
            wx.navigateBack({})
          }
        })
        console.log(res);
      }
    })
  
  },
  bindViewTap: function (e) {
    console.log(e.currentTarget.dataset.title);
    console.log(e.currentTarget.dataset.name);
    console.log(e.currentTarget.dataset.advertisData);

    let title = e.currentTarget.dataset.title;
    let name = e.currentTarget.dataset.name;
    /**
     * 停止扫描
     */
    if (this.data.is_scan) {
      this.setData({ is_scan: false })
      wx.stopBluetoothDevicesDiscovery({})
      console.log("stop scanning")
    }
    /**
     * 跳转到control页
     */
    wx.redirectTo({
      url: '../control/control?deviceId=' + title + '&name=' + name,
    })
  },

  onUnload: function () {
    if (this.data.is_scan) {
      /**
       * 停止扫描
       */
      this.setData({is_scan: false})
      wx.stopBluetoothDevicesDiscovery({})
      console.log("stop scanning")
    }
  }
})