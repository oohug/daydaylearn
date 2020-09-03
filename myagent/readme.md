
将其打包为jar文件

```
mvn assembly:assembly
```

执行mvn命令后就可以在项目的target目录下看到生成的myagent-jar-with-dependencies.jar文件。

然后编译在最开始用来测试的类:
```
javac MyAgentTest.java
```

编译后就生成了.class文件，为了方便，我们把.class文件放到和myagent-jar-with-dependencies.jar同一个目录。

测试
```
// 常规调用
java MyAgentTest

// java -javaagent: 
// 1.执行所有方法前，会执行MyAgent的premain方法。
// 能直观看到，MyAgentTest运行时先进入main方法，然后是test方法，
// 执行完test方法逻辑后退出test方法，最后退出main方法，不仅能看到每个方法的最终耗时也能看到方法执行的轨迹。
java -javaagent:myagent-jar-with-dependdencies.jar MyAgentTest