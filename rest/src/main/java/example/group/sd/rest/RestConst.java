package example.group.sd.rest;

public interface RestConst extends CommonConst {

	/**
	 * UTF-8
	 */
	String JSON_UTF8 = "application/json;charset=UTF-8";

	/**
	 * Default number of records per page (for UI, usually)
	 */
	long DEFAULT_UI_PAGE_SIZE = 20L;

	/**
	 * Pagination parameter names
	 */
	String PAGE_COUNT = "pageCount";

	String PAGE_INDEX = "pageIndex";

	String PAGE_SIZE = "pageSize";

	String ROW_COUNT = "rowCount";

	String ROW_INDEX = "rowIndex";

	/**
	 * Paths in REST services
	 */
	String GET_BY_ID_PATH = "/{id}";

	String GET_BY_ID_W_PATH = "/w/{id}";

	String GET_BY_GUID_PATH = "/guid/{guid}";

	String GET_BY_GUID_W_PATH = "/w/guid/{guid}";

	String GET_LIST_PATH = "/list";

	String GET_LIST_W_PATH = "/w";

	String CREATE_ENTITY_PATH = "/create";

	String UPDATE_ENTITY_PATH = "/update";

	String DELETE_ENTITY_PATH = "/delete";

	String DELETE_ENTITY_BY_ID_PATH = "/delete/{id}";

}
