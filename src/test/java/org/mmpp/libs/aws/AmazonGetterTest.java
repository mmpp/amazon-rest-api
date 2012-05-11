package org.mmpp.libs.aws;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mmpp.libs.aws.AmazonGetter;
import org.mmpp.libs.aws.AmazonItem;
import org.mmpp.libs.aws.AmazonWebServiceException;

public class AmazonGetterTest {

	@Test
	public void testGetAmazonItemWithBarcode() {
		
		AmazonGetter getter = new AmazonGetter("1TT32G33GM90M3JXVYG2");
		{
			try {
				AmazonItem[] items = getter.getAmazonItemWithBarcode("9784091831941");
				assertEquals("ISBN13 タイトル", items[0].Title,"バウ 4 (ビッグコミックス)");
				assertEquals("ISBN13 著者", items[0].Author,"テリー山本");
				assertEquals("ISBN13 著者配列", items[0].Authors.get(0),"テリー山本");
				assertEquals("ISBN13 出版社", items[0].Manufacturer,"小学館");
				assertEquals("ISBN13 ASIN", items[0].ASIN,"409183194X");
			} catch (AmazonWebServiceException e) {
				fail("例外 発生");
			}
		}
		{
		  try {
			AmazonItem[] items = getter.getAmazonItemWithBarcode("4091831915");
			  assertEquals("ISBN10 タイトル", items[0].Title,"バウ (1) (ビッグコミックス)");
			  assertEquals("ISBN10 著者", items[0].Author,"テリー山本");
			  assertEquals("ISBN10 著者配列", items[0].Authors.get(0),"テリー山本");
			  assertEquals("ISBN10 出版社", items[0].Manufacturer,"小学館");
			  assertEquals("ISBN10 ASIN", items[0].ASIN,"4091831915");
		} catch (AmazonWebServiceException e) {
		}
		}
	}
	@Test
	public void testGetAmazonItemWithBarcodeException() {
		
		AmazonGetter getter = new AmazonGetter("1TT32G33GM90M3JXVYG2");
		{
	
		try {
			 AmazonItem[] items = getter.getAmazonItemWithBarcode("98784592181187");
			 if(items!=null);
			fail("例外 実行");
		} catch (AmazonWebServiceException e) {
			assertEquals("例外 実行 Message: ", e.getMessage(),"98784592181187は、ItemIdの値として無効です。値を変更してから、再度リクエストを実行してください。");
			assertEquals("例外 実行 code : ", e.code,"AWS.InvalidParameterValue");

		}
		}
	}
	
	@Test
	/**
	 * 著者検索 ... 
	 */
	public void testGetAmazonItems() {
		AmazonGetter getter = new AmazonGetter("1TT32G33GM90M3JXVYG2");
		{
			try {
				AmazonItem[] items = getter.getAmazonItems(null, null, "斉藤里恵");
				assertEquals("著者検索 ", items.length,1);
			} catch (AmazonWebServiceException e) {
				fail("例外 実行");
			}
		}
		{
			try {
				AmazonItem[] items = getter.getAmazonItems(null, null, "神塚 ときお");
				assertEquals("著者検索 ページ跨ぎ ", items.length,17);
			} catch (AmazonWebServiceException e) {
				fail("例外 実行");
			}
		}
	}

	

}
