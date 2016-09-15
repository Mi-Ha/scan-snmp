package example.group.sd.rest;

import java.io.Serializable;

public class ResponseMessage implements Serializable {

	private static final long serialVersionUID = 8425087168039589882L;

	private MessageStatusType type;

	private String code;

	private String message;

	private String property;

	public ResponseMessage() {
	}

	public ResponseMessage(MessageStatusType type, String code, String message, String property) {
		this.type = type;
		this.code = code;
		this.message = message;
		this.property = property;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResponseMessage [type=");
		builder.append(type);
		builder.append(", code=");
		builder.append(code);
		builder.append(", message=");
		builder.append(message);
		builder.append(", property=");
		builder.append(property);
		builder.append("]");
		return builder.toString();
	}

	public MessageStatusType getType() {
		return type;
	}

	public void setType(MessageStatusType type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}
