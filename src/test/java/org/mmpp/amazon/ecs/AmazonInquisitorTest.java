package org.mmpp.amazon.ecs;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.mmpp.amazon.ecs.AbstractAccessService;
import org.mmpp.amazon.ecs.AmazonInquisitor;
import org.mmpp.amazon.ecs.AmazonInquisitorImpl;
import org.mmpp.amazon.ecs.model.Account;
import org.mmpp.amazon.ecs.request.generator.RequestParameterException;
import org.mmpp.amazon.ecs.request.model.AccessCertificate;
import org.mmpp.amazon.ecs.request.model.AffiliateAccount;
import org.mmpp.amazon.ecs.request.model.ItemLookupRequest;
import org.mmpp.amazon.ecs.request.model.Request;
import org.mmpp.amazon.ecs.response.model.AbstractItemRequest;
import org.mmpp.amazon.ecs.response.model.Author;
import org.mmpp.amazon.ecs.response.model.ItemAttribute;
import org.mmpp.amazon.ecs.response.model.ItemLink;
import org.mmpp.amazon.ecs.response.model.ItemSearchRequest;
import org.mmpp.amazon.ecs.response.model.ItemSearchResponse;
import org.mmpp.amazon.ecs.response.model.ItemSearchResult;
import org.mmpp.amazon.ecs.response.model.Manufacturer;
import org.mmpp.amazon.ecs.response.model.OperationRequest;
import org.mmpp.amazon.ecs.response.model.ProductGroup;
import org.mmpp.amazon.ecs.response.model.Title;

public class AmazonInquisitorTest {

	@Test
	public void testSearch() throws RequestParameterException{
		AmazonInquisitor getter = new AmazonInquisitorImpl(new AbstractAccessService(){

			@Override
			protected InputStream getInputStream() throws IOException {
		        java.io.InputStream in = getClass().getClassLoader().getResourceAsStream("aws_result_sample_2012-06-28.xml");
				return in;
			}
			@Override public void disconnect() {}
			@Override public void connect(Request request) throws RequestParameterException, IOException {}
		});
		Request postRequest = new Request();
		postRequest.setItemRequest(new ItemLookupRequest());

		ItemSearchResponse itemSearchResponse = (ItemSearchResponse)getter.postRequest(postRequest);
		ItemSearchResult itemResult = itemSearchResponse.getItemResult();
		{
			org.mmpp.amazon.ecs.response.model.Request request = itemResult.getRequest();
			assertTrue(request.isValid());
			AbstractItemRequest itemRequest = request.getItemRequest();
			ItemSearchRequest itemSearchRequest = (ItemSearchRequest)itemRequest;
			assertEquals("神塚 ときお",itemSearchRequest.getAuthor());
			assertEquals(1,itemSearchRequest.getItemPage());
			assertEquals("Small",itemSearchRequest.getResponseGroup());
			assertEquals("Books",itemSearchRequest.getSearchIndex());
		}
		assertEquals(2,itemResult.getTotalPage());
		assertEquals(17,itemResult.getTotalResult());
		assertEquals("http://www.amazon.co.jp/gp/redirect.html?camp=2025&creative=5143&location=http%3A%2F%2Fwww.amazon.co.jp%2Fgp%2Fsearch%3Fkeywords%3D%25E7%25A5%259E%25E5%25A1%259A%2B%25E3%2581%25A8%25E3%2581%258D%25E3%2581%258A%26url%3Dsearch-alias%253Dbooks-single-index&linkCode=xm2&tag=AssociateTag-22&SubscriptionId=AWSAccessKeyId",itemResult.getMoreSearchResultsUrl());
		java.util.List<org.mmpp.amazon.ecs.response.model.Item> items = itemResult.getItems();
		assertEquals(10,items.size());
		{
			org.mmpp.amazon.ecs.response.model.Item item = items.get(0);
			assertEquals("406321026X",item.getASIN());
			assertEquals("http://www.amazon.co.jp/%E3%83%90%E3%82%A4%E3%82%AD%E3%83%83%E3%82%BA-1-%E3%82%A2%E3%83%95%E3%82%BF%E3%83%8C%E3%83%BC%E3%83%B3KC-%E7%A5%9E%E5%A1%9A-%E3%81%A8%E3%81%8D%E3%81%8A/dp/406321026X%3FSubscriptionId%3DAWSAccessKeyId%26tag%3DAssociateTag-22%26linkCode%3Dxm2%26camp%3D2025%26creative%3D165953%26creativeASIN%3D406321026X",item.getDetailPageURL());
			java.util.List<ItemLink> itemLinks = item.getItemLinks();
			assertEquals(4,itemLinks.size());
			{
				ItemLink itemLink = itemLinks.get(0);
				assertEquals("Add To Wishlist",itemLink.getDescription());
				assertEquals("http://www.amazon.co.jp/gp/registry/wishlist/add-item.html%3Fasin.0%3D406321026X%26SubscriptionId%3DAWSAccessKeyId%26tag%3DAssociateTag-22%26linkCode%3Dxm2%26camp%3D2025%26creative%3D5143%26creativeASIN%3D406321026X",itemLink.getURL());
			}
			java.util.List<ItemAttribute> itemAttributes = item.getItemAttributes();
			assertEquals(4,itemAttributes.size());
			{
				ItemAttribute itemAttribute = itemAttributes.get(0);
				assertTrue(itemAttribute instanceof Author);
				Author author = (Author)itemAttribute;
				assertEquals("神塚 ときお",author.getName());
			}
			{
				ItemAttribute itemAttribute = itemAttributes.get(1);
				assertTrue(itemAttribute instanceof Manufacturer);
				Manufacturer manufacturer = (Manufacturer)itemAttribute;
				assertEquals("講談社",manufacturer.getName());
			}
			{
				ItemAttribute itemAttribute = itemAttributes.get(2);
				assertTrue(itemAttribute instanceof ProductGroup);
				ProductGroup productGroup = (ProductGroup)itemAttribute;
				assertEquals("Book",productGroup.getName());
			}
			{
				ItemAttribute itemAttribute = itemAttributes.get(3);
				assertTrue(itemAttribute instanceof Title);
				Title title = (Title)itemAttribute;
				assertEquals("バイキッズ 1 (アフタヌーンKC)",title.getName());
			}
			
		}
		OperationRequest request = itemSearchResponse.getOperationRequest();
		java.util.Map<String,String> headers = request.getHTTPHeaders();
		assertEquals(1,headers.size());
		assertEquals("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:12.0) Gecko/20100101 Firefox/12.0",headers.get("UserAgent"));
		java.util.Map<String,String> args = request.getArguments();
		assertEquals(10,args.size());
		assertEquals("ItemSearch",args.get("Operation"));
		assertEquals("AWSECommerceService",args.get("Service"));
		assertEquals("1",args.get("ItemPage"));
		assertEquals("AssociateTag-22",args.get("AssociateTag"));
		assertEquals("Books",args.get("SearchIndex"));
		assertEquals("神塚 ときお",args.get("Author"));
		assertEquals("Signature=",args.get("Signature"));
		assertEquals("AWSAccessKeyId",args.get("AWSAccessKeyId"));
		assertEquals("2012-06-28T06:36:33Z",args.get("Timestamp"));
		assertEquals("カタカナ",args.get("__mk_ja_JP"));

		assertEquals("ce419a7c-e9fe-4b30-abcb-54f1873af99f",request.getRequestId());
		assertEquals("0.0391610000000000",request.getRequestProcessingTime());
		
	}
	
	@Test
	public void testIsAWSAccountNull(){
		Request request = new Request();
		request.setItemRequest(new ItemLookupRequest());
		TestableAmazonInquisitor hoge = new TestableAmazonInquisitor();
		assertFalse(hoge.isAmazonAccessAccount(request));
	}

	@Test
	public void testIsAWSAccountNull2(){
		Request request = new Request();
		request.setItemRequest(new ItemLookupRequest());
		request.getItemRequest().setAccessCertificate(new AccessCertificate());
		TestableAmazonInquisitor hoge = new TestableAmazonInquisitor();
		assertFalse(hoge.isAmazonAccessAccount(request));
	}
	@Test
	public void testIsAWSAccountNull3(){
		Request request = new Request();
		request.setItemRequest(new ItemLookupRequest());
		request.getItemRequest().setAccessCertificate(new AccessCertificate());
		request.getItemRequest().setAffiliateAccount(new AffiliateAccount());
		TestableAmazonInquisitor hoge = new TestableAmazonInquisitor();
		assertFalse(hoge.isAmazonAccessAccount(request));
	}

	@Test
	public void testIsAWSAccountNullAccessCertificate1(){
		Request request = new Request();
		request.setItemRequest(new ItemLookupRequest());
		AccessCertificate accessCertificate = new AccessCertificate();
		accessCertificate.setAccessKey("access");
		request.getItemRequest().setAccessCertificate(accessCertificate);
		AffiliateAccount affiliateAccount = new AffiliateAccount();
		request.getItemRequest().setAffiliateAccount(affiliateAccount);
		TestableAmazonInquisitor hoge = new TestableAmazonInquisitor();
		assertFalse(hoge.isAmazonAccessAccount(request));
	}
	@Test
	public void testIsAWSAccountNullAccessCertificate2(){
		Request request = new Request();
		request.setItemRequest(new ItemLookupRequest());
		AccessCertificate accessCertificate = new AccessCertificate();
		accessCertificate.setSecretKey("SecretKey");
		request.getItemRequest().setAccessCertificate(accessCertificate);
		AffiliateAccount affiliateAccount = new AffiliateAccount();
		request.getItemRequest().setAffiliateAccount(affiliateAccount);
		TestableAmazonInquisitor hoge = new TestableAmazonInquisitor();
		assertFalse(hoge.isAmazonAccessAccount(request));
	}
	@Test
	public void testIsAWSAccountTrue(){
		Request request = new Request();
		request.setItemRequest(new ItemLookupRequest());
		AccessCertificate accessCertificate = new AccessCertificate();
		accessCertificate.setAccessKey("access");
		accessCertificate.setSecretKey("SecretKey");
		request.getItemRequest().setAccessCertificate(accessCertificate);
		AffiliateAccount affiliateAccount = new AffiliateAccount();
		affiliateAccount.setTrackingID("tracking");
		request.getItemRequest().setAffiliateAccount(affiliateAccount);
		TestableAmazonInquisitor hoge = new TestableAmazonInquisitor();
		assertTrue(hoge.isAmazonAccessAccount(request));
	}
	@Test
	public void testStoreAWSAccount(){
		Request request = new Request();
		request.setItemRequest(new ItemLookupRequest());
		String accessKey, secretKey, trackingID;
		accessKey = "a";
		secretKey = "b";
		trackingID = "t";
		System.setProperty(AmazonInquisitor.ENVKEY_ACCESSKEY,accessKey);
		System.setProperty(AmazonInquisitor.ENVKEY_SECRETKEY,secretKey);		
		System.setProperty(AmazonInquisitor.ENVKEY_TRACKINGID,trackingID);		

		new TestableAmazonInquisitor().storeAmazonAccessAccountFromEnv(request);
		assertEquals(accessKey,request.getItemRequest().getAccessCertificate().getAccessKey());
		assertEquals(secretKey,request.getItemRequest().getAccessCertificate().getSecretKey());
		assertEquals(trackingID,request.getItemRequest().getAffiliateAccount().getTrackingID());
	}
	@Test
	public void testStoreAWSAccountFromPropetoes() throws IOException{
		String accessKey, secretKey, trackingID;
		accessKey = "a";
		secretKey = "b";
		trackingID = "t";

		java.io.File file = new java.io.File(AmazonInquisitor.AWS_CONFIG_FILE);
        java.io.FileOutputStream fileOutput = new java.io.FileOutputStream(file);
        fileOutput.write(AmazonInquisitor.ENVKEY_ACCESSKEY.getBytes());
        fileOutput.write("=".getBytes());
        fileOutput.write(accessKey.getBytes());
        fileOutput.write(System.getProperty("line.separator").getBytes());
        fileOutput.write(AmazonInquisitor.ENVKEY_SECRETKEY.getBytes());
        fileOutput.write("=".getBytes());
        fileOutput.write(secretKey.getBytes());
        fileOutput.write(System.getProperty("line.separator").getBytes());
        fileOutput.write(AmazonInquisitor.ENVKEY_TRACKINGID.getBytes());
        fileOutput.write("=".getBytes());
        fileOutput.write(trackingID.getBytes());
        fileOutput.write(System.getProperty("line.separator").getBytes());

		Request request = new Request();
		request.setItemRequest(new ItemLookupRequest());

		new TestableAmazonInquisitor().storeAmazonAccessAccountFromEnv(request);
		assertEquals(accessKey,request.getItemRequest().getAccessCertificate().getAccessKey());
		assertEquals(secretKey,request.getItemRequest().getAccessCertificate().getSecretKey());
		assertEquals(trackingID,request.getItemRequest().getAffiliateAccount().getTrackingID());

		file.delete();
	}

	@Test
	public void testStoreAWSAccountFromConstracter() throws IOException{
		Account account = new Account();
		account.setAccessKey("a");
		account.setSecretKey("b");
		account.setTrackingID("t");


		Request request = new Request();
		request.setItemRequest(new ItemLookupRequest());

		new TestableAmazonInquisitor(account).storeAmazonAccessAccountFromEnv(request);
		assertEquals(account.getAccessKey(),request.getItemRequest().getAccessCertificate().getAccessKey());
		assertEquals(account.getSecretKey(),request.getItemRequest().getAccessCertificate().getSecretKey());
		assertEquals(account.getTrackingID(),request.getItemRequest().getAffiliateAccount().getTrackingID());

	}
	private class TestableAmazonInquisitor extends AmazonInquisitorImpl{
		public TestableAmazonInquisitor() {
			super();
		}
		public TestableAmazonInquisitor(Account account) {
			super(account);
		}
		public boolean isAmazonAccessAccount(Request request){
			return super.isAmazonAccessAccount(request);
		}
		public void storeAmazonAccessAccountFromEnv(Request request){
			super.storeAmazonAccessAccountFromEnv(request);
		}
	}
}
