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
				assertEquals("ISBN13 �^�C�g��", items[0].Title,"�o�E 4 (�r�b�O�R�~�b�N�X)");
				assertEquals("ISBN13 ����", items[0].Author,"�e���[�R�{");
				assertEquals("ISBN13 ���Ҕz��", items[0].Authors.get(0),"�e���[�R�{");
				assertEquals("ISBN13 �o�Ŏ�", items[0].Manufacturer,"���w��");
				assertEquals("ISBN13 ASIN", items[0].ASIN,"409183194X");
			} catch (AmazonWebServiceException e) {
				fail("��O ����");
			}
		}
		{
		  try {
			AmazonItem[] items = getter.getAmazonItemWithBarcode("4091831915");
			  assertEquals("ISBN10 �^�C�g��", items[0].Title,"�o�E (1) (�r�b�O�R�~�b�N�X)");
			  assertEquals("ISBN10 ����", items[0].Author,"�e���[�R�{");
			  assertEquals("ISBN10 ���Ҕz��", items[0].Authors.get(0),"�e���[�R�{");
			  assertEquals("ISBN10 �o�Ŏ�", items[0].Manufacturer,"���w��");
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
			fail("��O ���s");
		} catch (AmazonWebServiceException e) {
			assertEquals("��O ���s Message: ", e.getMessage(),"98784592181187�́AItemId�̒l�Ƃ��Ė����ł��B�l��ύX���Ă���A�ēx���N�G�X�g�����s���Ă��������B");
			assertEquals("��O ���s code : ", e.code,"AWS.InvalidParameterValue");

		}
		}
	}
	
	@Test
	/**
	 * ���Ҍ��� ... 
	 */
	public void testGetAmazonItems() {
		AmazonGetter getter = new AmazonGetter("1TT32G33GM90M3JXVYG2");
		{
			try {
				AmazonItem[] items = getter.getAmazonItems(null, null, "�ē����b");
				assertEquals("���Ҍ��� ", items.length,1);
			} catch (AmazonWebServiceException e) {
				fail("��O ���s");
			}
		}
		{
			try {
				AmazonItem[] items = getter.getAmazonItems(null, null, "�_�� �Ƃ���");
				assertEquals("���Ҍ��� �y�[�W�ׂ� ", items.length,17);
			} catch (AmazonWebServiceException e) {
				fail("��O ���s");
			}
		}
	}

	

}
