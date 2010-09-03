package com.phonegap.api;

public class CommandResult {
	private final int status;
	private final String result;
	
	public CommandResult(Status status, String result) {
		this.status = status.ordinal();
		this.result = result;
	}

	/* (non-Javadoc)
	 * @see com.phonegap.api.CommandResultInterface#getStatus()
	 */
	public int getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see com.phonegap.api.CommandResultInterface#getResult()
	 */
	public String getResult() {
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.phonegap.api.CommandResultInterface#toSuccessCallbackString(java.lang.String)
	 */
	public String toSuccessCallbackString(String callbackId) {
		if (status==0)
		return "javascript:PhoneGap.callbackSuccess('"+callbackId+"', " + this.getResult()+ ");";
		else return "javascript:PhoneGap.callbackSuccessListener('"+callbackId+"', " + this.getResult()+ ");";
		
	}
	
	/* (non-Javadoc)
	 * @see com.phonegap.api.CommandResultInterface#toErrorCallbackString(java.lang.String)
	 */
	public String toErrorCallbackString(String callbackId) {
		if (status==0)
		return "javascript:PhoneGap.callbackError('"+callbackId+"', " + this.getResult()+ ");";
		else return "javascript:PhoneGap.callbackErrorListener('"+callbackId+"', " + this.getResult()+ ");";
	}
	
	public enum Status {
		OK,
		CLASSNOTFOUNDEXCEPTION,
		ILLEGALACCESSEXCEPTION,
		INSTANTIATIONEXCEPTION,
		MALFORMEDURLEXCEPTION,
		IOEXCEPTION,
		INVALIDACTION,
		JSONEXCEPTION,
		OKLISTENER
	}
}
