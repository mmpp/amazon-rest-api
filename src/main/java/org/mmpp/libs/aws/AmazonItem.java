package org.mmpp.libs.aws;


/**
 * �A�}�]������Ƃ��Ă����f�[�^�̎Q��XML
 * 
 * �Q�l
 * http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=1TT32G33GM90M3JXVYG2&Operation=ItemLookup&IdType=ISBN&ItemId=9784774109077&SearchIndex=Books&ResponseGroup=Request,Small&Version=2008-12-10
 * �ł́AItemAttributes
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
	// �����̒��ґΉ� 2009/05/23 //
	public java.util.ArrayList<java.util.HashMap<String,String>> Creators;
	public java.util.ArrayList<String> Authors;

	// �A�}�]���Ǘ�ID ISBN10�Ȃ炻�̂܂܁AISBN13�Ȃ�`�F�b�N�T����X�œ����Ȃ� //
	public String ASIN;
}
