````

0
实例化 -> 属性赋值 -> 初始化 -> 销毁
1
实例化 Instantiation
2
属性赋值 Populate
3
初始化 Initialization
4
销毁 Destruction

AbstractApplicationContext


InstantiationAwareBeanPostProcessor
postProcessBeforeInstantiation：实例化bean之前，相当于new这个bean之前
postProcessAfterInstantiation：实例化bean之后，相当于new这个bean之后
postProcessPropertyValues：bean已经实例化完成，在属性注入时阶段触发，@Autowired,@Resource等注解原理基于此方法实现

BeanPostProcessor
postProcessBeforeInitialization：初始化bean之前，相当于把bean注入spring上下文之前
postProcessAfterInitialization：初始化bean之后，相当于把bean注入spring上下文之后

DisposableBean
destroy()，触发时机为当此对象销毁时;比如说运行applicationContext.registerShutdownHook 时，就会触发这个方法。

使用场景：这个扩展点非常有用 ，无论是写中间件和业务中，都能利用这个特性。比如对实现了某一类接口的bean在各个生命期间进行收集，或者对某个类型的bean进行统一的设值等等。