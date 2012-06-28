package org.mmpp.libs.aws;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
/**
 * Amazonアクセスサービス実装クラス
 * @author wataru
 *
 */
public class AmazonAccessServiceImpl implements AmazonAccessService{
	/**
	 * Amazonアカウント情報
	 */
	private AmazonAccount _amazonAccount;
	/**
	 * デフォルトコンストラクタ
	 * @param amazonAccount アカウント情報
	 */
	public AmazonAccessServiceImpl(AmazonAccount amazonAccount) {
		super();
		_amazonAccount = amazonAccount;
	}
	public AmazonAccount getAmazonAccount(){
		return _amazonAccount;
	}

	@Override
	public AmazonItem[] getAmazonItems(String barcode,String title,String author) throws AmazonWebServiceException{
		// ページ指定型の第一ページで取得 //
		return getAmazonItemsWithPages(barcode, title, author, 1);
	}

	@Override
	public AmazonItem[] getAmazonItemsWithPages(String barcode,String title,String author,int pageno) throws AmazonWebServiceException{
		return getAmazonItemsWithPages(barcode,title,author,pageno,true);
	}
	
	public AmazonItem[] getAmazonItemsWithPages(String barcode,String title,String author,int pageno,boolean hasTotalPage) throws AmazonWebServiceException{
		
		// アクセスurlを取得
		String strUrl = AmazonUrlGenerator.createUrl(getAmazonAccount(),barcode,title,author,pageno);
		// urlが取得できたかの判断
		if(strUrl==null)
			// 出来ない場合
			return null;
		
		System.out.println(" Access url : "+strUrl);
		// amazonアイテム一覧格納変数
		AmazonItem[] clsAmazonItems = null;
		
		java.net.HttpURLConnection connectWeb = null;
		
		// 次のページが存在しているかの判断
		boolean haveNextPage = false;
		
		try { 
			// http接続初期化
			java.net.URL urlAmazon = new java.net.URL(strUrl);
			
			connectWeb = (java.net.HttpURLConnection)urlAmazon.openConnection();
			connectWeb.setRequestMethod("GET");
			java.net.HttpURLConnection.setFollowRedirects(false);
			connectWeb.setInstanceFollowRedirects(false);
			
			
			// 接続
			connectWeb.connect();
			
			
			// XML解析フェーズ
			// 参考 : http://www.hellohiro.com/xmldom.htm
			//       http://java.sun.com/j2se/1.5.0/ja/docs/ja/api/javax/xml/parsers/DocumentBuilder.html
			try {
				javax.xml.parsers.DocumentBuilderFactory  docBFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
				javax.xml.parsers.DocumentBuilder builder = docBFactory.newDocumentBuilder();
				AmazonItemPaser amazonItemPaser = new AmazonItemPaser();
				  // パースを実行してDocumentオブジェクトを取得
				// Webのインプットストリームからドキュメント生成...
				org.w3c.dom.Document doc = builder.parse(connectWeb.getInputStream());
				
				// ドキュメントを解析 ... 
				clsAmazonItems = amazonItemPaser.parse(doc);
				
				// 総ページ数を取得します
				int totalPage = amazonItemPaser.getTotalPage(doc);

				// 検索結果のページ情報を取得 ... //
				haveNextPage = (totalPage > pageno);

			} catch (ParserConfigurationException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			
		} catch (MalformedURLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}finally{
			if(connectWeb!=null){
				// 切断
				connectWeb.disconnect();
			}
		}
		// 次ページ送りがON?
		if(!haveNextPage){
			return clsAmazonItems;
		}
		
		// 次ページの処理 //
		// ページ番号をカウントアップ //
		pageno++;
		AmazonItem[] nextPageAmazonItems = this.getAmazonItemsWithPages(barcode, title, author, pageno,hasTotalPage);

		// 結果を集計 //
		// 現在のアイテムを配列に格納 //
		java.util.ArrayList<AmazonItem> arrItems = new java.util.ArrayList<AmazonItem>(java.util.Arrays.asList(clsAmazonItems));
		// 次のページのアイテムも追加 //
		arrItems.addAll(java.util.Arrays.asList(nextPageAmazonItems));
		
		// 返却変数に再作成 //
		return arrItems.toArray(new AmazonItem[arrItems.size()]);

	}


}
