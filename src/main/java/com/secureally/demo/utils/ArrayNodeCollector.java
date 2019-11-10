package com.secureally.demo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ArrayNodeCollector implements Collector<JsonNode, ArrayNode, ArrayNode> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * A function that creates and returns a new mutable result container.
     *
     * @return a function which returns a new, mutable result container
     */
    @Override
    public Supplier<ArrayNode> supplier() {
        return MAPPER::createArrayNode;
    }

    /**
     * A function that folds a value into a mutable result container.
     *
     * @return a function which folds a value into a mutable result container
     */
    @Override
    public BiConsumer<ArrayNode, JsonNode> accumulator() {
        return ArrayNode::add;
    }

    /**
     * A function that accepts two partial results and merges them.  The combiner function may fold state from one argument into the other and return
     * that, or may return a new result container.
     *
     * @return a function which combines two partial results into a combined result
     */
    @Override
    public BinaryOperator<ArrayNode> combiner() {
        return (x, y) -> {
            x.addAll(y);
            return x;
        };
    }

    /**
     * Perform the final transformation from the intermediate accumulation type {@code A} to the final result type {@code R}.
     *
     * <p>If the characteristic {@code IDENTITY_FINISH} is
     * set, this function may be presumed to be an identity transform with an unchecked cast from {@code A} to {@code R}.
     *
     * @return a function which transforms the intermediate result to the final result
     */
    @Override
    public Function<ArrayNode, ArrayNode> finisher() {
        return accumulator -> accumulator;
    }

    /**
     * Returns a {@code Set} of {@code Collector.Characteristics} indicating the characteristics of this Collector.  This set should be immutable.
     *
     * @return an immutable set of collector characteristics
     */
    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
