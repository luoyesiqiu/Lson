# Lson

## 介绍

Lson是一个可以将Java对象序列化成Json和Json反序列化成Java对象的工具

## 用法

### 声明一个Bean类

```java
import com.luoye.lson.annotation.Alias;
import com.luoye.lson.annotation.Ignore;

public class MyBean {
    public String name;
    public String age;
    @Alias("w")
    public int weight;
    @Ignore
    public double wealth;
}
```

### Java对象 ==> json

调用Lson.toJson将Java对象转成Json

```java
MyBean bean = new MyBean();
bean.name = "luoyesiqiu";
bean.age = 18;
bean.weight = 60;
bean.wealth = 1000000.0lf;
String json = Lson.toJson(bean);
System.out.println(json);
```

输出：

```json
{"name":"luoyesiqiu","age":18,"w":60}
```

### json ==> Java对象

调用Lson.toObject将Json转换成Java对象

```java
String json = "{\"name\":\"luoyesiqiu\",\"age\":18,\"w\":60}";
MyBean bean = Lson.toObject(json,MyBean.class);
System.out.println(bean.name);
```

输出：

```log
luoyesiqiu
```

值得注意的是，在转成Java对象前，对象所属的类必须要拥有默认构造函数。所以，如果当类中添加了自定义的构造函数，那么还需要在类中定义一个无参数公开的默认构造函数，例如：

```java
public MyBean(){
}
```