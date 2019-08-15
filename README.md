# okhttpkt
[ ![Version](https://api.bintray.com/packages/stokid/library/okhttpkt/images/download.svg?version=1.0.7.1) ](https://bintray.com/stokid/library/okhttpkt/1.0.7.1/link)

![img](readme/OKKT.jpg)

>**专门针对kotlin开发的okhttp 的tool**

## 几大特点：
**1、针对文件请求，重写okio计算方案，可实时显示请求文件进度，防止okhttp在直接做请求文件的时候采取的直传策略，从而导致下载文件间隔过长。**

**2、利用协程完全替代rxjava做主线程通讯以及线程调度，增加程序的交互效率。**

**3、注解注入，完美的api流程，使你毋须对类型进行转换，直接上手**

**4、纯粹的kotlin代码，利用ktx语法糖结合代码达成十分简约的书写方式**

**5、拥有工厂模式与单例模式的config方式，甚至扩展了一个接口用来自定义自己的client做自定义解决方式**

## 简单案例：

### 使用前提导入：
```gradle
implementation 'com.stormkid:okhttpkt:1.0.7.1'
```

### 1、全局注册

```kotlin
class BaseApplication:Application (){
    override fun onCreate() {
        super.onCreate()
        Okkt.instance.setBase("http://xxxx.com").initHttpClient()
    }
}
    
```

### 2、get
```kotlin
   fun doGet(){
         Okkt.instance.Builder().setUrl("/part").get(object:CallbackRule<YourDataClass>{
            override suspend fun onSuccess(entity: YourDataClass, flag: String) {
                
            }

            override suspend fun onFailed(error: String) {
            }

        })
  
   }
  
```

### 3、implement get
```kotlin
  class MainActivity : AppCompatActivity(),CallbackRule<YourDataClass> ,Serializable{
    override suspend fun onSuccess(entity: YourDataClass, flag: String) {
    }

    override suspend fun onFailed(error: String) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Okkt.instance.Builder().setUrl("/part").get(this)
    }
}
```
> **注意：任何实现CallbackRule接口类必须把CallbackRule接口当作第一个接口来实现！注意书写顺序，切勿写成:"
```class Main: Serilizable,CallbackRule<YourDataClass>```"**


# [WIKI](https://github.com/StormKid/okhttpkt/wiki)
