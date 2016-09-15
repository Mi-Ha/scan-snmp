package example.group.sd.rest;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ResponseWrapperBuilder<T> {
	private T data;

	public static ResponseWrapper<String> RESPONSE_OK = new ResponseWrapperBuilder<String>().build();

	private ResponsePagination pagination;

	private List<ResponseMessage> messages;

	public ResponseWrapperBuilder() {
		messages = new ArrayList<>();
	}

	public ResponseWrapperBuilder<T> setData(T data) {
		this.data = data;
		return this;
	}

	public ResponseWrapperBuilder<T> setPagination(ResponsePagination pagination) {
		this.pagination = pagination;
		return this;
	}

	public ResponseWrapperBuilder<T> setMessages(List<ResponseMessage> messages) {
		this.messages = messages;
		return this;
	}

	public ResponseWrapperBuilder<T> info(String code, String message, Object... arguments) {
		messages.add(new ResponseMessage(MessageStatusType.INFO, code, format(message, arguments), null));
		return this;
	}

	public ResponseWrapperBuilder<T> warning(String code, String message, Object... arguments) {
		messages.add(new ResponseMessage(MessageStatusType.WARNING, code, format(message, arguments), null));
		return this;
	}

	public ResponseWrapperBuilder<T> debug(String code, String message, Object... arguments) {
		messages.add(new ResponseMessage(MessageStatusType.DEBUG, code, format(message, arguments), null));
		return this;
	}

	public ResponseWrapperBuilder<T> error(String code, String message, Object... arguments) {
		messages.add(new ResponseMessage(MessageStatusType.ERROR, code, format(message, arguments), null));
		return this;
	}

	private static String format(String pattern, Object arguments) {
		try {
			return MessageFormat.format(pattern, arguments);
		} catch (Exception e) {
			return pattern;
		}
	}

	public ResponseWrapper<T> build() {
		ResponseWrapper<T> rw = new ResponseWrapper<T>();
		rw.setData(data);
		rw.setPagination(pagination);
		rw.setMessages(messages);
		rw.getSuccess();
		return rw;
	}

	public ResponseWrapperBuilder<T> setException(Throwable exceptionInfo) {
		ResponseMessage msg = new ResponseMessage(MessageStatusType.ERROR, "Exception", exceptionInfo.getMessage(),
				null);
		messages.add(msg);
		return this;
	}
}