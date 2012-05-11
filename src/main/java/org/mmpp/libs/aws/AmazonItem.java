package org.mmpp.libs.aws;


/**
 * アマゾンからとってきたデータの参照XML
 * 
 * 参考
 * http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=1TT32G33GM90M3JXVYG2&Operation=ItemLookup&IdType=ISBN&ItemId=9784774109077&SearchIndex=Books&ResponseGroup=Request,Small&Version=2008-12-10
 * での、ItemAttributes
 * @author kou
 */
public class AmazonItem {
	public AmazonItem(){
		Creators = new java.util.ArrayList<java.util.HashMap<String,String>>();
		Authors = new java.util.ArrayList<String>();
		
	}
	public String Author;
	public String Manufacturer;
	public String ProductGroup;
	public String Title; 
	public String ISBN; 
	// 複数の著者対応 2009/05/23 //
	public java.util.ArrayList<java.util.HashMap<String,String>> Creators;
	public java.util.ArrayList<String> Authors;

	// アマゾン管理ID ISBN10ならそのまま、ISBN13ならチェックサムがXで頭がなし //
	public String ASIN;
}
