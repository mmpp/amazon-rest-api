	package org.mmpp.libs.aws;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * アマゾンからデータを抽出するクラス
 * --
 * 環境変数
 * org.mmpp.aws.id : アマゾンのアクセスＩＤ
 * @author wataru
 *
 */
public class AmazonGetter {
	public AmazonGetter(){
		super();
	}
	public AmazonGetter(String asid){
		this();
		System.setProperty("org.mmpp.aws.id",asid);
	}
	// 2009/01/15 固定から変更可能へ(環境変数:org.mmpp.aws.id)
	// public String ASID = "1TT32G33GM90M3JXVYG2";

	public String getASID(){
		return System.getProperty("org.mmpp.aws.id");
	}
	/**
	 * バーコードを用いたサービスurlを生成するメソッド
	 * @param barcode バーコード
	 * @return アクセスurl
	 */
	private String getAmazonItemUrlWithBarcode(String barcode){
		// アマゾンアクセスアカウントを取得 //
		String ASID = getASID();
		// アクセスアカウントが取得できたか？
		if(ASID==null)
			// 出来ない場合、nullを返却
			return null;
		// 当該urlを取得
		return  "http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&AWSAccessKeyId="+ASID+"&Operation=ItemLookup&IdType=ISBN&ItemId="+barcode+"&SearchIndex=Books";		
	}
	private String getAmazonItemUrlWithTitle(String title){
		// アマゾンアクセスアカウントを取得 //
		String ASID = getASID();
		// アクセスアカウントが取得できたか？
		if(ASID==null)
			// 出来ない場合、nullを返却
			return null;
		// 当該urlを取得
		String strUrl = "";
		try {
			strUrl =   "http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&AWSAccessKeyId="+ASID+"&Operation=ItemSearch&Title=";
			strUrl += java.net.URLEncoder.encode(title,"UTF-8");
			strUrl += "&SearchIndex=Books&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A";
		} catch (UnsupportedEncodingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		strUrl = strUrl.replaceAll(" ","+" );		return  strUrl;		
	}
	/**
	 * 1つのページに表示できる商品数は最大で10個
	 * @param author
	 * @return
	 */
	private String getAmazonItemUrlWithAuthor(String author){
		// アマゾンアクセスアカウントを取得 //
		String ASID = getASID();
		// アクセスアカウントが取得できたか？
		if(ASID==null)
			// 出来ない場合、nullを返却
			return null;
		// 当該urlを取得
		String strUrl ="";
		try {
			strUrl =   "http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&AWSAccessKeyId="+ASID+"&Operation=ItemSearch&Author=";
			strUrl += java.net.URLEncoder.encode(author,"UTF-8");
			strUrl += "&SearchIndex=Books&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A";
		} catch (UnsupportedEncodingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		strUrl = strUrl.replaceAll(" ","+" );
		return  strUrl;		
	}

	public AmazonItem[] getAmazonItemWithBarcode(String barcode) throws AmazonWebServiceException{
		return getAmazonItems(barcode,null,null);
	}
	public AmazonItem[] getAmazonItems(String barcode,String title,String author) throws AmazonWebServiceException{
		// ページ指定型の第一ページで取得 //
		return getAmazonItemsWithPages(barcode, title, author, 1);
		/**
		// アクセスurlを取得
		// 
		String strUrl;
		if(barcode!=null){
		 strUrl = getAmazonItemUrlWithBarcode(barcode);
		}else{
			if(title!=null){
				strUrl = getAmazonItemUrlWithTitle(title);
			}else{
				strUrl = getAmazonItemUrlWithAuthor(author);
			}
		}
		// urlが取得できたかの判断
		if(strUrl==null)
			// 出来ない場合
			return null;
		System.out.println(" get url : "+strUrl);
		// 返却変数初期化
		AmazonItem[] clsAmazonItems = null;
		
		try { 
			// http接続初期化
			java.net.URL urlAmazon = new java.net.URL(strUrl);
			
			java.net.HttpURLConnection connectWeb = (java.net.HttpURLConnection)urlAmazon.openConnection();
			connectWeb.setRequestMethod("GET");
			java.net.HttpURLConnection.setFollowRedirects(false);
			connectWeb.setInstanceFollowRedirects(false);
			
			
			// 接続
			connectWeb.connect();
			
			
//			// 読み込みフェーズ
//			java.io.BufferedReader br;
//			String fld;
//			try {
//			     br=new java.io.BufferedReader(new java.io.InputStreamReader(connectWeb.getInputStream()));
//			     while((fld=br.readLine())!=null){
//			          System.out.print(fld);
//			     }
//			} catch (java.io.IOException e) {
//			     e.printStackTrace();
//			}

			// XML解析フェーズ
			// 参考 : http://www.hellohiro.com/xmldom.htm
			//       http://java.sun.com/j2se/1.5.0/ja/docs/ja/api/javax/xml/parsers/DocumentBuilder.html
			try {
				javax.xml.parsers.DocumentBuilderFactory  docBFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
				javax.xml.parsers.DocumentBuilder builder = docBFactory.newDocumentBuilder();
				  // パースを実行してDocumentオブジェクトを取得
				// Webのインプットストリームからドキュメント生成...
				org.w3c.dom.Document doc = builder.parse(connectWeb.getInputStream());
				
				// ドキュメントを解析 ... 
				clsAmazonItems = this.parseAmazonItemFromXML(doc);
				
			} catch (ParserConfigurationException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			// 切断
			connectWeb.disconnect();
			
		} catch (MalformedURLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		//	結果を返却
		return clsAmazonItems;
		**/
	}
	/**
	 * ページを跨いだ検索実行
	 * @param barcode
	 * @param title
	 * @param author
	 * @param pageno 検索ページ番号
	 * @return
	 * @throws AmazonWebServiceException
	 */
	public AmazonItem[] getAmazonItemsWithPages(String barcode,String title,String author,int pageno) throws AmazonWebServiceException{
		
		// 次ページ送りを行うかの判断フラグ ( true:次ページ)
		boolean blnNextPage=false;
		// アクセスurlを取得
		// 
		String strUrl;
		if(barcode!=null){
		 strUrl = getAmazonItemUrlWithBarcode(barcode);
		}else{
			if(title!=null){
				strUrl = getAmazonItemUrlWithTitle(title);
			}else{
				strUrl = getAmazonItemUrlWithAuthor(author);
			}

			// ページ指定を追加 //
			strUrl+="&ItemPage="+pageno;
		}
		// urlが取得できたかの判断
		if(strUrl==null)
			// 出来ない場合
			return null;
		
		System.out.println(" get url : "+strUrl);
		// 返却変数初期化
		AmazonItem[] clsAmazonItems = null;
		
		try { 
			// http接続初期化
			java.net.URL urlAmazon = new java.net.URL(strUrl);
			
			java.net.HttpURLConnection connectWeb = (java.net.HttpURLConnection)urlAmazon.openConnection();
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
				  // パースを実行してDocumentオブジェクトを取得
				// Webのインプットストリームからドキュメント生成...
				org.w3c.dom.Document doc = builder.parse(connectWeb.getInputStream());
				
				// ドキュメントを解析 ... 
				clsAmazonItems = this.parseAmazonItemFromXML(doc);
				
				// 検索結果のページ情報を取得 ... //
				// Items : TotalPages
				{

					// 検索結果件数取得
					// Itemsのエレメントを取得 //
					org.w3c.dom.Element elmItems = (org.w3c.dom.Element)(doc.getDocumentElement().getElementsByTagName("Items").item(0));
					// TotalPagesエレメントを取得 //
					org.w3c.dom.Element elmTotalPage = (org.w3c.dom.Element)(elmItems.getElementsByTagName("TotalPages")).item(0);

					// TotalPagesが存在する可の判断 //
					// isbnではOperation=ItemLookupの為、エラーとなる //
					if(elmTotalPage!=null){
						// 全ページ数が現在表示中かそれ以上であるか？ //
						// それ以上の場合、次ページ送りフラグをON //
						blnNextPage = (Integer.valueOf(elmTotalPage.getFirstChild().getNodeValue()) > pageno);
					}
				}
			} catch (ParserConfigurationException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			// 切断
			connectWeb.disconnect();
			
		} catch (MalformedURLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		// 次ページ送りがON?
		if(blnNextPage){
			// 次ページの処理 //
			// ページ番号をカウントアップ //
			pageno++;
			AmazonItem[] nextPageAmazonItems = this.getAmazonItemsWithPages(barcode, title, author, pageno);

			// 結果を集計 //
			// 現在のアイテムを配列に格納 //
			java.util.ArrayList<AmazonItem> arrItems = new java.util.ArrayList<AmazonItem>(java.util.Arrays.asList(clsAmazonItems));
			// 次のページのアイテムも追加 //
			arrItems.addAll(java.util.Arrays.asList(nextPageAmazonItems));
			
			// 返却変数に再作成 //
			clsAmazonItems = arrItems.toArray(new AmazonItem[arrItems.size()]);
		}
		//	結果を返却
		return clsAmazonItems;
	}
	
	private AmazonItem[] parseAmazonItemFromXML(org.w3c.dom.Document xmlDocument) throws AmazonWebServiceException{
		
		// ItemLookupResponseのエレメント取得
		// org.w3c.dom.Element elmItemLookupResponse = xmlDocument.getElementById("ItemLookupResponse");
		org.w3c.dom.Element elmItemLookupResponse = xmlDocument.getDocumentElement();
		//System.out.println("->"+elmItemLookupResponse.getNodeName());

		// 検索結果件数取得
		org.w3c.dom.NodeList nlstRootItems = elmItemLookupResponse.getElementsByTagName("Items");
		//System.out.println("-> Items "+nlstRootItems.getLength());
		org.w3c.dom.Element elmItems = (org.w3c.dom.Element)nlstRootItems.item(0);
		//System.out.println("-> "+elmItems.getNodeName());

		// 結果件数を取得 ItemLookupResponse : Items : Itemのカウント？
		org.w3c.dom.NodeList nlstItems = elmItems.getElementsByTagName("Item");

		// 検索結果数を格納する変数 
		int intResultCount = nlstItems.getLength();
		//System.out.println(" get item count : " + intResultCount);
		
		AmazonItem clsAmazonItems[] = new AmazonItem[intResultCount];
		for(int i = 0 ; i < intResultCount ; i ++ ){
			// itemで繰り返す
			System.out.println("  ==  "+i);

			// 格納変数の初期化
			clsAmazonItems[i] = new AmazonItem();
			//System.out.println("  < " + nlstItems.item(i).getNodeName());

			org.w3c.dom.Element elmItem = ((org.w3c.dom.Element)nlstItems.item(i));
			{
				//ASIN : ItemLookupResponse : Items : Item : ASIN
				org.w3c.dom.Element elmASIN = (org.w3c.dom.Element)(elmItem.getElementsByTagName("ASIN")).item(0);
				clsAmazonItems[i].ASIN  = elmASIN.getFirstChild().getNodeValue();				
			}			

			org.w3c.dom.Element elmItemAttributes = (org.w3c.dom.Element)(elmItem.getElementsByTagName("ItemAttributes")).item(0);

			{
				// タイトル : ItemLookupResponse : Items : Item : ItemAttributes : Title
				org.w3c.dom.Element elmItemTitle = (org.w3c.dom.Element)(elmItemAttributes.getElementsByTagName("Title")).item(0);
				String strTitle = elmItemTitle.getFirstChild().getNodeValue();
				
				//System.out.println(" Title : "+strTitle);
				clsAmazonItems[i].Title  = strTitle;
			}			
			{
				// 著者 : ItemLookupResponse : Items : Item : ItemAttributes : Author
//				org.w3c.dom.Element elmItemAuthor = (org.w3c.dom.Element)(elmItemAttributes.getElementsByTagName("Author")).item(0);
//				String strAuthor = elmItemAuthor.getFirstChild().getNodeValue();

				String strAuthor = getItemAttributeValue(elmItemAttributes,"Author");
				//System.out.println(" Author : "+strAuthor);
				clsAmazonItems[i].Author  = strAuthor;
			}			
			{
				// 著者複数対応 : 
				// 文字列だけを抽出する ...
				clsAmazonItems[i].Authors = new java.util.ArrayList<String>();
				org.w3c.dom.NodeList subjectList = elmItemAttributes.getElementsByTagName("Author");
				for (int intNode = 0; intNode < subjectList.getLength(); intNode++) {
					clsAmazonItems[i].Authors.add((String)subjectList.item(intNode).getFirstChild().getNodeValue());
				}

				
			}
			{
				// 出版社 : ItemLookupResponse : Items : Item : ItemAttributes : Manufacturer
				org.w3c.dom.Element elmItemManufacturer = (org.w3c.dom.Element)(elmItemAttributes.getElementsByTagName("Manufacturer")).item(0);
				String strManufacturer = elmItemManufacturer.getFirstChild().getNodeValue();
				
				//System.out.println(" Manufacturer : "+strManufacturer);
				clsAmazonItems[i].Manufacturer  = strManufacturer;
			}			
			{
				// 種別？ : ItemLookupResponse : Items : Item : ItemAttributes : ProductGroup
				org.w3c.dom.Element elmItemProductGroup = (org.w3c.dom.Element)(elmItemAttributes.getElementsByTagName("ProductGroup")).item(0);
				String strProductGroup = elmItemProductGroup.getFirstChild().getNodeValue();
				
				//System.out.println(" ProductGroup : "+strProductGroup);
				clsAmazonItems[i].ProductGroup  = strProductGroup;
			}			
			{
				// 作成者 : ItemLookupResponse : Items : Item : ItemAttributes : Creator

				// 文字列だけを抽出する ...
				clsAmazonItems[i].Creators = new java.util.ArrayList<java.util.HashMap<String,String>>();
				org.w3c.dom.NodeList creatorList = elmItemAttributes.getElementsByTagName("Creator");
				for (int intNode = 0; intNode < creatorList.getLength(); intNode++) {
					org.w3c.dom.Node node = creatorList.item(intNode);
					java.util.HashMap<String,String> hmpCreator = new java.util.HashMap<String,String>();
					hmpCreator.put("name", (String)node.getFirstChild().getNodeValue());
					hmpCreator.put("role", (String)node.getAttributes().getNamedItem("Role").getNodeValue());
					
					clsAmazonItems[i].Creators.add(hmpCreator);
				}


			}
		}
		
		// エラー取得 //
		// Items : Errors
		{
			org.w3c.dom.NodeList errorsNodeList = elmItems.getElementsByTagName("Errors");

			if((org.w3c.dom.Element)errorsNodeList.item(0)!=null){
				//  Errors : Error 
				org.w3c.dom.NodeList errorNodeList = ((org.w3c.dom.Element)errorsNodeList.item(0)).getElementsByTagName("Error");

				// 検索結果数を格納する変数 
				int intErrorCount = errorNodeList.getLength();
	
				
				for(int i = 0 ; i < intErrorCount ; i ++ ){
					// 記述例 //
					/**
	<Code>AWS.InvalidParameterValue</Code>
	<Message>
	98784592181187は、ItemIdの値として無効です。値を変更してから、再度リクエストを実行してください。
	</Message>
					 */
					org.w3c.dom.Element errorElement = ((org.w3c.dom.Element)errorNodeList.item(i));
	
					org.w3c.dom.Element elmMessage = (org.w3c.dom.Element)(errorElement.getElementsByTagName("Message")).item(0);
					AmazonWebServiceException exeption = new AmazonWebServiceException(elmMessage.getFirstChild().getNodeValue());
					org.w3c.dom.Element elmCode = (org.w3c.dom.Element)(errorElement.getElementsByTagName("Code")).item(0);
	
					exeption.code = elmCode.getFirstChild().getNodeValue();

					throw exeption;
				}
			}
		}

		return clsAmazonItems;

	}
	
	/**
	 * アイテム欄の各情報を抽出するメソッド
	 * @param attributeElement 抽出元アイテムアトリビュートエレメント
	 * @param key 抽出キー
	 * @return 抽出内容
	 */
	private String getItemAttributeValue(org.w3c.dom.Element attributeElement,String key){
		org.w3c.dom.Element elmItemProductGroup = (org.w3c.dom.Element)(attributeElement.getElementsByTagName(key)).item(0);
		if(elmItemProductGroup==null)
			return null;
		String strAttibuteValue = elmItemProductGroup.getFirstChild().getNodeValue();
		

		return strAttibuteValue;
	}
	
	/**
	 * アイテム欄の属性情報を取得するメソッド
	 * 例 : <[key] [attributeKey]="[return]" >
	 * @param attributeElement
	 * @param key
	 * @param attributeKey
	 * @return
	 */
//	private String getItemAttributeValue(org.w3c.dom.Element attributeElement,String key,String attributeKey){
//		org.w3c.dom.Element elmItemProductGroup = (org.w3c.dom.Element)(attributeElement.getElementsByTagName(key)).item(0);
//		if(elmItemProductGroup==null)
//			return null;
//		String strAttibuteValue = elmItemProductGroup.getAttribute(attributeKey);
//		
//
//		return strAttibuteValue;
//	}

}
