package org.mmpp.libs.aws;

public class AmazonAccount {
	private String _affiliateid;
	private String _secid;
	private String _awsid;

	public AmazonAccount(String asid,String key,String affiliateid){
		super();
		setAwsID(asid);
		setSecretKey(key);
		setAffiliateID(affiliateid);
		saveToEnvironment();
	}


	private void setAffiliateID(String affiliateid) {
		this._affiliateid = affiliateid;
	}
	public String getAffiliateID(){
		return _affiliateid;
	}
	private void setSecretKey(String key) {
		this._secid = key;
	}
	/**
	 * シークレットアクセスキー
	 * @return シークレットアクセスキー
	 */
	public String getSecretKey(){
		return this._secid;
	}
	
	private void setAwsID(String awsid) {
		this._awsid = awsid;
	}
	/**
	 * アクセスキーID
	 * @return アクセスキーID
	 */
	public String getAwsID(){
		return this._awsid;
	}
	private void saveToEnvironment() {
		System.setProperty("org.mmpp.aws.id",_awsid);
		System.setProperty("org.mmpp.aws.secid",_secid);
		System.setProperty("org.mmpp.aws.affiliateid",_affiliateid);
	}

	public static AmazonAccount loadFromEnviroment(){
		String asid, secid, affiliateid;
		asid = System.getProperty("org.mmpp.aws.id");
		secid = System.getProperty("org.mmpp.aws.secid");		
		affiliateid = System.getProperty("org.mmpp.aws.affiliateid");		
		return new AmazonAccount(asid,secid,affiliateid);
	}
}
