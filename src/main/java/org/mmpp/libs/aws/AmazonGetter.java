package org.mmpp.libs.aws;

/**
 * Amazonの検索を行うサービスを提供します
 * @author wataru
 *
 */
public interface AmazonGetter {
	/**
	 * アマゾンアクセスのアカウント情報を取得します
	 * @return アマゾンアクセスアカウント情報
	 */
	public AmazonAccount getAmazonAccount();
	/**
	 * バーコードからアイテムを取得します
	 * @param barcode バーコード
	 * @return Amazonアイテム
	 * @throws AmazonWebServiceException
	 */
	public AmazonItem[] getAmazonItemWithBarcode(String barcode) throws AmazonWebServiceException ;
	/**
	 * 検索しアイテム一覧を取得します
	 * @param title タイトル ( null = 指定なし )
	 * @param author 著者情報 ( null = 指定なし )
	 * @return Amazonアイテム一覧
	 * @throws AmazonWebServiceException
	 */
	public AmazonItem[] getAmazonItems(String title,String author) throws AmazonWebServiceException;
	/**
	 * 検索しアイテム一覧を取得します
	 * @param title タイトル ( null = 指定なし )
	 * @param author 著者情報 ( null = 指定なし )
	 * @param pageno　取得ページ番号
	 * @return Amazonアイテム一覧
	 * @throws AmazonWebServiceException
	 */
	public AmazonItem[] getAmazonItemsWithPages(String title,String author,int pageno) throws AmazonWebServiceException;
	
}
