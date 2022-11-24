package demo08;

public class WebException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private int status;
	
	public WebException(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}

}
