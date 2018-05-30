# XbbBus
引用方式:

Step 1. Add it in your root build.gradle at the end of repositories:
  
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
    
  Step 2. Add the dependency
  
  	dependencies {
	        implementation 'com.github.piaoguodeyu:xbbbus:1.0.1'
	}


订阅者模式
在onCreate订阅，onDestroy解订阅。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XbbBus.getDefaut().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XbbBus.getDefaut().unRegister(this);
    }
# 发送信息使用方法
1:指定发送到某个订阅者。

 XbbBus.getDefaut().post("data", MainActivity.class);
 
 XbbBus.getDefaut().post(new Object(), MainActivity.class);
 
2:发送到所有订阅者。

 XbbBus.getDefaut().post("data");
 
 XbbBus.getDefaut().post(new Object());
 
 # 订阅者接收信息使用方法
 
 1: 在主线程执行
 
    @XbbMainThreadSubscriber
    void subscriber(String string) {
      
    }
    
 2: 在发送者当前线程执行
 
    @XbbSubscriber
    void currentThread(Message string) {
       
    }
    
#注意：订阅者方法参数只能有一个，参数类型最好确定。如：String,Integer,User等。 
