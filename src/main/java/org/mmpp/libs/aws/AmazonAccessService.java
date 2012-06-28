package org.mmpp.libs.aws;

/**
 * Amazonアクセスサービスインタフェイス
 * @author wataru
 *
 */
public interface AmazonAccessService {
	/**
	 * 検索条件を指定してAmazonアイテム情報を取得します
	 * @param barcode バーコード (単独指定)
	 * @param title タイトル名
	 * @param author 著者名
	 * @return Amazonアイテム一覧
	 * @throws AmazonWebServiceException
	 */
	public AmazonItem[] getAmazonItems(String barcode,String title,String author) throws AmazonWebServiceException;
	
	/**
	 * ページを跨いだ検索実行
	 * @param barcode バーコード (単独指定)
	 * @param title タイトル名
	 * @param author 著者名
	 * @param pageno 検索ページ番号
	 * @return Amazonアイテム一覧
	 * @throws AmazonWebServiceException
	 */
	public AmazonItem[] getAmazonItemsWithPages(String barcode,String title,String author,int pageno) throws AmazonWebServiceException;


}
