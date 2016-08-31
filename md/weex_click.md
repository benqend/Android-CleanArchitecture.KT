Weex-Android中点击事件时序分析
#### 简介
本文介绍了Weex点击事件的流程图，其目的是通过点击事件的时序来阅读Weex的源码.
准备工作
[Weex的入门体验小结](http://www.atatech.org/articles/59495)一文中介绍了Weex的一个入门demo，本文的时序图是在该demo的基础上进行断点调试所整理的，读者也可以自己用一个有点击事件的demo进行尝试.WXComponment中的addEvent中为添加点击事件的位置，读者可以在此加上断点，然后运行程序进行调试
click的时序图：

#### 注意
- 途中的框图表示当前的线程，如WeexJSBridgeThread.
- main.js文件位于weex-sdk的assets目录中，地址为：[main.js](https://github.com/alibaba/weex/blob/dev/android/sdk/assets/main.js)
- 时序图的地址[weex-study](),打开程序为[VisualParadigm](https://www.visual-paradigm.com/)

#### 总结
- Weex的click事件涉及到三个线程：MainThread,WXJsBridgeThread,V8EngineThread
- WXBridge是Java与JavaScript进行交互的中介
- main.js包含Weex的JavaScript层的运行环境
J- ava ==> JavaScript通过WXBridge.execJS, JavaScript ==> Java通过WXBridge.callNative.
