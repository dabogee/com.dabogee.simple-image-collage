package com.dabogee.tools.image.collage.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollageInstance {

    @Getter
    private final List<CollageRow> rows = new ArrayList<>();

    public void add(CollageRow input) {
        rows.add(input);
    }

    public Set<Integer> getUsedIds() {
        Set<Integer> result = new HashSet<>();
        rows.forEach(r -> result.addAll(r.getIds()));
        return result;
    }

    public Integer getRowsCount() {
        return rows.size();
    }
}