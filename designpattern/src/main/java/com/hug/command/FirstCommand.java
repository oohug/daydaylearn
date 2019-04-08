package com.hug.command;

/**
 * 具体的命令实现
 */
public class FirstCommand implements Command {

    private Receiver receiver;

    public FirstCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        this.receiver.doAction();
    }

    @Override
    public void undo() {
        // TODO
    }
}
