package com.hug.command;

/**
 * 客户端->调用者->[命令实现和绑定]->接收者
 *
 * 命令模式：将请求封装成一个对象，以使你可用不同的请求对客户端进行参数化；可对请求进行排除、记录日志、或撤销操作。
 *           将客户端的请求封装成一个对象，从而使用不同的请求对客户端进行参数化。
 *           1、Receiver 接收者，处理具体的业务逻辑
 *           2、Command 抽象的命令，定义一个命令对象所具备的一系列操作命令，比如 execute()、undo()、redo()。当命令操作被调用的时候会触发接收者去做具体的命令对应的业务逻辑。
 *           3、FirstCommand 具体的命令实现，通常绑定 命令操作 和 接收者 之间的关系，execute（）,命令的实现委托给Receiver.doAction()方法。
 *           4、Invoker 调用者，它持有一个命令对象，并且可以在需要的时候通过命令对象 完成 具体的业务逻辑
 */
public class Client {

    public static void main(String[] args) {

        Receiver receiver = new Receiver();
        Command command = new FirstCommand(receiver);   // 绑定 命令操作 和 接收者 之间的关系
        Invoker invoker = new Invoker();    // 调用者，它持有一个命令对象
        invoker.setCommand(command);
        invoker.doAction(); // 客户端通过调用者来执行命令
    }
}
