

# 多版本API 

## 背景和原理
目的是在API接口同时存在多个版本时便于维护和管理，基于spring mvc 的RequestMapping实现。

##说明介绍
API提供方和调用方约定一个版本KEY，通过请求header传递进行路由目标版本API。此处约定 版本KEY为 (V|v|)([0-9]\d*\.?\d*);
当API方法返回对象中如果含有 ver_trace （string）属性时，会将版本trace返回给调用方。

## 接入步骤
1. 引入 multi-api jar
```text
<dependency>
    <groupId>com.hug</groupId>
    <artifactId>multi-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
2. 在Controller的类或者方法上使用@ApiVersion 注解
示例1：注解在方法上
```java
@ApiVersion("1.1")
@ApiOperation(value = "postJson", notes = "POST格式测试", response = BaseResponse.class)
@PostMapping(value = "/post/json")
public BaseResponse postJsonV1(@RequestBody OrderBaseReq orderBaseReq) {
    long start = System.currentTimeMillis();
    log.info("start {}", start);
    return new BaseResponse("v1.1");
}

@ApiVersion("2.1")
@ApiOperation(value = "postJson", notes = "POST格式测试", response = BaseResponse.class)
@PostMapping(value = "/post/json")
public BaseResponse postJsonV2(@RequestBody OrderBaseReq orderBaseReq) {
    long start = System.currentTimeMillis();
    log.info("start {}", start);
    return new BaseResponse("v2.1");
}
```

示例2：@ApiVersion 注解在类上
```java
@Slf4j
@RequestMapping("/api/class")
@RestController
@ApiVersion("1.1")
@Api(value = "MultiApiOnClassController", tags = "MultiApiOnClassController API", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MultiApiOnClassController {

//    @ApiVersion("1.2")
    @ApiOperation(value = "hello", notes = "Hello Api Test(v1.1)", response = String.class)
    @GetMapping("hello")
    public Object hello1() {
        return "v1.1";
    }

//    @ApiVersion("1.2")
    @ApiOperation(value = "postJson", notes = "POST格式测试", response = BaseResponse.class)
    @PostMapping(value = "/post/json")
    public BaseResponse postJsonV1(@RequestBody OrderBaseReq orderBaseReq) {
        long start = System.currentTimeMillis();
        log.info("start {}", start);
        return new BaseResponse("v1.1");
    }
}
```
返回：
```ftl
{
    "code": "100",
    "message": "成功",
    "ver_trace": "v1.2 -> v1.2",
    "data": "v1.1"
}
```