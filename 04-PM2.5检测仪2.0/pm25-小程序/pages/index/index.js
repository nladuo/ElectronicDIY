//index.js
//获取应用实例
let app = getApp()
Page({
  data: {
    userInfo: {}
  },
  onLoad: function () {
    console.log('onLoad')
    var that = this
    //读取传感器型号
    //调用应用实例的方法获取全局数据
    app.getUserInfo(function(userInfo){
      //更新数据
      that.setData({
        userInfo:userInfo
      })
    })
  },
  //扫描蓝牙
  goScan: function () {
    wx.navigateTo({
      url: '../scan/scan'
    })
  },
  swithChanged: function (e) {
    console.log('发生 change 事件，携带值为', e.detail.value)
    app.globalData.isG5S = e.detail.value
    
  }
})
