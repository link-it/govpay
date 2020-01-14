package it.govpay.core.utils.appio.model;

import java.util.Date;

public class MessageContent {

	private String subject;
	private String markdown;
	private PaymentData payment_data;
	private Date due_date;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMarkdown() {
		return markdown;
	}
	public void setMarkdown(String markdown) {
		this.markdown = markdown;
	}
	public PaymentData getPayment_data() {
		return payment_data;
	}
	public void setPayment_data(PaymentData payment_data) {
		this.payment_data = payment_data;
	}
	public Date getDue_date() {
		return due_date;
	}
	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}
	
	
}
