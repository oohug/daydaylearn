package com.hug.pipeline;

import java.util.UUID;

public final class PipelineContext {
    private String id = UUID.randomUUID().toString().replace("-", "");

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
