package example.group.sd.rest;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ RestConst.ROW_INDEX, RestConst.ROW_COUNT, RestConst.PAGE_INDEX, RestConst.PAGE_COUNT,
		RestConst.PAGE_SIZE })
//@JsonPropertyOrder({"rowIndex", "rowCount", "pageIndex", "pageCount", "pageSize"})
public class ResponsePagination implements Serializable {

	private static final long serialVersionUID = 5498465598203056253L;

	/**
	 * Row count
	 */
	private Long rowCount;

	/**
	 * First row of the data tuple, corresponding to the user request
	 */
	private Long rowIndex;

	/**
	 * Start page index
	 */
	private Long pageIndex;

	/**
	 * Размер страницы
	 */
	private Long pageSize;

	/**
	 * Количество страниц
	 */
	private Long pageCount;

	ResponsePagination() {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResponseMeta [rowCount=");
		builder.append(rowCount);
		builder.append(", rowIndex=");
		builder.append(rowIndex);
		builder.append(", pageIndex=");
		builder.append(pageIndex);
		builder.append(", pageSize=");
		builder.append(pageSize);
		builder.append(", pageCount=");
		builder.append(pageCount);
		builder.append("]");
		return builder.toString();
	}

	@JsonProperty(RestConst.ROW_COUNT)
	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	@JsonProperty(RestConst.ROW_INDEX)
	public Long getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Long rowIndex) {
		this.rowIndex = rowIndex;
	}

	@JsonProperty(RestConst.PAGE_INDEX)
	public Long getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Long pageIndex) {
		this.pageIndex = pageIndex;
	}

	@JsonProperty(RestConst.PAGE_SIZE)
	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	@JsonProperty(RestConst.PAGE_COUNT)
	public Long getPageCount() {
		return pageCount;
	}

	public void setPageCount(Long pageCount) {
		this.pageCount = pageCount;
	}

}