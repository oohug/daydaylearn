package com.hug.pipeline;

/**
 * next引用是通过set方法来设置的。按照初衷，这个next指针应该在初始化的同时进行设置，在初始化之后就不再应该被修改。
 * 这种设计相当于为对象引入了额外的状态，带来了不安全因素；
 * 其次，在转发的时候每次都需要对next是否为空进行判断，可能会带来轻微的性能损失。
 *
 * @param <T>
 */
public interface Pipeline<T> {

    void process(PipelineContext ctx, T t);

    void forward(PipelineContext ctx, T t);
}