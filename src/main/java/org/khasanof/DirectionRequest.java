package org.khasanof;

import org.khasanof.enums.Sort;

public class DirectionRequest {
    private int page;
    private int size;
    private Sort sortKey;
    private String sortValue;
    private String[] sortValues;

    public DirectionRequest() {
        this.size = 10;
    }

    public DirectionRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public DirectionRequest(int page, int size, Sort sortKey, String sortValue) {
        this.page = page;
        this.size = size;
        this.sortKey = sortKey;
        this.sortValue = sortValue;
    }

    private DirectionRequest(int page, int size, Sort sortKey, String... sortValues) {
        this.page = page;
        this.size = size;
        this.sortKey = sortKey;
        this.sortValues = sortValues;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Sort getSortKey() {
        return sortKey;
    }

    public void setSortKey(Sort sortKey) {
        this.sortKey = sortKey;
    }

    public String getSortValue() {
        return sortValue;
    }

    public void setSortValue(String sortValue) {
        this.sortValue = sortValue;
    }

    public String[] getSortValues() {
        return sortValues;
    }

    public void setSortValues(String[] sortValues) {
        this.sortValues = sortValues;
    }
}
