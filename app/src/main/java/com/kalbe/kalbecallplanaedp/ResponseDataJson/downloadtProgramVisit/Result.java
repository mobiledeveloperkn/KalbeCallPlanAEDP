package com.kalbe.kalbecallplanaedp.ResponseDataJson.downloadtProgramVisit;

public class Result{
	private String methodName;
	private String message;
	private boolean status;

	public void setMethodName(String methodName){
		this.methodName = methodName;
	}

	public String getMethodName(){
		return methodName;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Result{" + 
			"method_name = '" + methodName + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}
