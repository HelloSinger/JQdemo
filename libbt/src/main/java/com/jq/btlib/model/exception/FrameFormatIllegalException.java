package com.jq.btlib.model.exception;


/**
 * 帧格式异常
 * */
public class FrameFormatIllegalException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public FrameFormatIllegalException(String message){
		super(message);
	}
	
	public FrameFormatIllegalException(){
		super();
	}
	
}
