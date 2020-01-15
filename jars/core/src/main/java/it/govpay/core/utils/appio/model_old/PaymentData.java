package it.govpay.core.utils.appio.model_old;

public class PaymentData {

	private Integer amount;
	private String notice_number;
	private boolean invalid_after_due_date;
	
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getNotice_number() {
		return notice_number;
	}
	public void setNotice_number(String notice_number) {
		this.notice_number = notice_number;
	}
	public boolean isInvalid_after_due_date() {
		return invalid_after_due_date;
	}
	public void setInvalid_after_due_date(boolean invalid_after_due_date) {
		this.invalid_after_due_date = invalid_after_due_date;
	}
	
	
}
