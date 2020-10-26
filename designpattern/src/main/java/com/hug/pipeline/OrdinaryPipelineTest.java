package com.hug.pipeline;

import java.util.Arrays;

public class OrdinaryPipelineTest {

    public static void main(String[] args) {
        Pipeline<?> pipeline =
                OrdinaryPipeline.getInstance(
                        Arrays.asList(new DemoPipeline("1"), new DemoPipeline("2"), new DemoPipeline("3")));

        PipelineContext context = new PipelineContext();
        pipeline.process(context, null);
    }

    private static final class DemoPipeline extends OrdinaryPipeline<String> {

        public DemoPipeline(String name) {
            super(name);
        }

        @Override
        public void process(PipelineContext ctx, String s) {
            // TODO
            System.out.println("d uid:" + ctx.getId() + ", do " + s);
            if (true) {
                super.forward(ctx, s);
            } else {

            }
        }
    }
}