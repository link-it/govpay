package it.govpay.model;

public class PatchOp extends BasicModel {


	private static final long serialVersionUID = 1L;

	public enum OpEnum {

		ADD("ADD"),
		DELETE("DELETE"),
		REPLACE("REPLACE");

		private String value;

		OpEnum(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return String.valueOf(this.value);
		}

		public static OpEnum fromValue(String text) {
			for (OpEnum b : OpEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}



	private OpEnum op = null;
	private String path = null;
	private Object value = null;
	public OpEnum getOp() {
		return op;
	}
	public void setOp(OpEnum op) {
		this.op = op;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}



