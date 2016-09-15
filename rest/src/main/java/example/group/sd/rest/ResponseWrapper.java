package example.group.sd.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseWrapper<T> implements Serializable {

	private static final long serialVersionUID = 4175906475221558968L;

	private T data;

	private ResponsePagination pagination;

	private List<ResponseMessage> messages;

	boolean success;

	/**
	 * Intentionally has visibility <code>package</code>. Use
	 * {@link ResponseWrapperBuilder} to make an instance of
	 * {@link ResponseWrapper}
	 */
	ResponseWrapper() {
		messages = new ArrayList<>();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResponseWrapper [success=");
		builder.append(getSuccess());
		builder.append(", messages=");
		builder.append(messages);
		builder.append(", pagination=");
		builder.append(pagination);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}

	@JsonProperty("success")
	public boolean getSuccess() {
		if (messages != null) {
			for (ResponseMessage msg : messages) {
				if (msg.getType() != null && msg.getType() == MessageStatusType.ERROR) {
					success = false;
					return false;
				}
			}
		}
		success = true;
		return true;
	}

	public void setSuccess(boolean value) {
		// doing nothing, just declaring the setter method
	}

	@JsonProperty("data")
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@JsonProperty("pagination")
	public ResponsePagination getPagination() {
		return pagination;
	}

	public void setPagination(ResponsePagination pagination) {
		this.pagination = pagination;
	}

	@JsonProperty("messages")
	public List<ResponseMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<ResponseMessage> messages) {
		this.messages = messages;
	}

	@JsonIgnore(value=true)
	public ResponseMessage getTopMessage() {
		return messages!=null && messages.size()>0 ? messages.get(0) : null;
	}

}
