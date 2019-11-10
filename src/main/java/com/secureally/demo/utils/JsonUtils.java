package com.secureally.demo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;

import java.util.List;

public class JsonUtils {

    private static ObjectMapper MAPPER;

    public static void setStaticMembers(ObjectMapper mapper) {
        MAPPER = mapper;
    }
    public static JsonNode getDetailDiffs(Object left, Object right) {
        Javers javers = JaversBuilder.javers().build();
        Diff diff = javers.compare(left, right);
        JsonNode deltaNode = MissingNode.getInstance();
        if (diff.hasChanges()) {
            List<ValueChange> valueChanges = diff.getChangesByType(ValueChange.class);
            ArrayNode changeArrayNode = valueChanges.stream()
                    .filter(v -> v.getPropertyName() != "method")
                    .map(j -> {
                        JsonNode delta = MAPPER.createObjectNode()
                                .put("before", null == j.getRight() ? "" : j.getRight().toString())
                                .put("after", j.getLeft().toString());
                        return MAPPER.createObjectNode().set(j.getPropertyName(), delta);
                    })
                    .collect(new ArrayNodeCollector());
            deltaNode = changeArrayNode;
        }
        return deltaNode;
    }
}
