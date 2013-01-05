package com.pipeline;

import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/2/13
 */
public class Pipeline {
    private List<Node> nodes;

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
