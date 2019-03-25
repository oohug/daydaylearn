package com.hug.common;

import java.util.Collections;
import java.util.List;

/**
 * @version 1.0
 * @description 分页对象
 */
public class Pagination<T> {
    // 返回的结果.
    private List<T> rows = Collections.emptyList();

    // 返回的记录总条数.
    private long total = 0;

    // 返回的总页数.
    private int pageCount = 0;

    // 页面索引.
    private int pageIndex;

    // 页面记录条数.
    private int pageSize;

    /**
     * @description 详细说明
     */
    public Pagination() {
    }

    /**
     * @param pageIndex
     * @param pageSize
     * @description 详细说明
     */
    public Pagination(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    /**
     * @param pageIndex
     * @param pageSize
     * @param totel
     * @description 详细说明
     */
    public Pagination(int pageIndex, int pageSize, int totel) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        setTotal(totel);
    }

    /**
     * @return the total
     */
    public final long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;

        pageCount = (int) ((total % pageSize == 0) ? total / pageSize : total
                / pageSize + 1);

        if (pageIndex < 1)
            pageIndex = 1;
        if (pageIndex > pageCount)
            pageIndex = pageCount;

    }

    public int getPageCount() {
        return pageCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the rows
     */
    public final List<T> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public final void setRows(List<T> rows) {
        this.rows = rows;
    }

    public static final <T> Pagination<T> factory(int limit, int offse) {
        final int idx = Math.max(0, limit / offse) + 1;
        return new Pagination<T>(idx, offse);
    }
}