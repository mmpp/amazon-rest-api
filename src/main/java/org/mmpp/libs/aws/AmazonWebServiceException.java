package org.mmpp.libs.aws;

/**
 * AmazonWebService (AWS) ������O
 * @author kou
 * @since 2009/05/31
 */
public class AmazonWebServiceException extends java.lang.Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AmazonWebServiceException(String message){
		super(message);
	}
	public String code=null;
	

}
