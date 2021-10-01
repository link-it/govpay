package it.govpay.bd.configurazione.model;

public class MailServer extends it.govpay.model.MailServer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String host;
	private int port;
	private String username;
	private String password;
	private String from;
	private Integer readTimeout;
	private Integer connectionTimeout;
	private SslConfig sslConfig;
	private boolean startTls;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public Integer getReadTimeout() {
		return readTimeout;
	}
	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}
	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public SslConfig getSslConfig() {
		return sslConfig;
	}
	public void setSslConfig(SslConfig sslConfig) {
		this.sslConfig = sslConfig;
	}
	public boolean isStartTls() {
		return startTls;
	}
	public void setStartTls(boolean startTls) {
		this.startTls = startTls;
	}
}
