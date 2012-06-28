package org.mmpp.libs.aws;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AmazonItemPaser {

	/**
	 * 検索結果総ページ数を取得します
	 * @param doc Amazon結果ページ
	 * @return 検索結果総ページ数
	 */
	public int getTotalPage(Document doc) {
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
				return Integer.valueOf(elmTotalPage.getFirstChild().getNodeValue());
				// 全ページ数が現在表示中かそれ以上であるか？ //
				// それ以上の場合、次ページ送りフラグをON //
			}
		}
		return 1;
	}
	/**
	 * Amazon REST検索結果からAmazonアイテム情報一覧を生成します
	 * @param xmlDocument 検索結果ドキュメント
	 * @return Amazonアイテム一覧
	 * @throws AmazonWebServiceException
	 */
	public AmazonItem[] parse(org.w3c.dom.Document xmlDocument) throws AmazonWebServiceException{
		
		// ItemLookupResponseのエレメント取得
		// org.w3c.dom.Element elmItemLookupResponse = xmlDocument.getElementById("ItemLookupResponse");
		org.w3c.dom.Element elmItemLookupResponse = xmlDocument.getDocumentElement();

		// 検索結果件数取得
		org.w3c.dom.NodeList nlstRootItems = elmItemLookupResponse.getElementsByTagName("Items");
		org.w3c.dom.Element elmItems = (org.w3c.dom.Element)nlstRootItems.item(0);

		// 結果件数を取得 ItemLookupResponse : Items : Itemのカウント？
		org.w3c.dom.NodeList nlstItems = elmItems.getElementsByTagName("Item");

		// 検索結果数を格納する変数 
		int intResultCount = nlstItems.getLength();
		
		AmazonItem clsAmazonItems[] = new AmazonItem[intResultCount];
		for(int i = 0 ; i < intResultCount ; i ++ ){
			// itemで繰り返す
			System.out.println("  ==  "+i);

			// 格納変数の初期化
			clsAmazonItems[i] = new AmazonItem();

			org.w3c.dom.Element elmItem = ((org.w3c.dom.Element)nlstItems.item(i));
			String asin = parseASIN(elmItem);
			clsAmazonItems[i].ASIN  =  asin;
		
			
			org.w3c.dom.Element elmItemAttributes = (org.w3c.dom.Element)(elmItem.getElementsByTagName("ItemAttributes")).item(0);
			// 詳細情報を解析します
			parseItemAttribute(clsAmazonItems[i],elmItemAttributes);
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
					if(errorElement==null)
						continue;
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
	 * Amazonアイテム情報の付随情報を解析する
	 * @param amazonItem Amazonアイテム情報
	 * @param elmItemAttributes　付随情報
	 */
	public void parseItemAttribute(AmazonItem amazonItem,Element elmItemAttributes) {
		// タイトル : ItemLookupResponse : Items : Item : ItemAttributes : Title
		amazonItem.Title  = getElementTextContent(elmItemAttributes,"Title");

		// 著者 : ItemLookupResponse : Items : Item : ItemAttributes : Author
		amazonItem.Author  = getElementTextContent(elmItemAttributes,"Author");

		{
			// 著者複数対応 : 
			// 文字列だけを抽出する ...
			amazonItem.Authors = new java.util.ArrayList<String>();
			org.w3c.dom.NodeList subjectList = elmItemAttributes.getElementsByTagName("Author");
			for (int intNode = 0; intNode < subjectList.getLength(); intNode++) {
				amazonItem.Authors.add((String)subjectList.item(intNode).getFirstChild().getNodeValue());
			}

			
		}
		// 出版社 : ItemLookupResponse : Items : Item : ItemAttributes : Manufacturer
		amazonItem.Manufacturer  = getElementTextContent(elmItemAttributes,"Manufacturer");

		// 種別？ : ItemLookupResponse : Items : Item : ItemAttributes : ProductGroup
		amazonItem.ProductGroup  = getElementTextContent(elmItemAttributes,"ProductGroup");

		{
			// 作成者 : ItemLookupResponse : Items : Item : ItemAttributes : Creator

			// 文字列だけを抽出する ...
			amazonItem.Creators = new java.util.ArrayList<java.util.HashMap<String,String>>();
			org.w3c.dom.NodeList creatorList = elmItemAttributes.getElementsByTagName("Creator");
			for (int intNode = 0; intNode < creatorList.getLength(); intNode++) {
				org.w3c.dom.Node node = creatorList.item(intNode);
				java.util.HashMap<String,String> hmpCreator = new java.util.HashMap<String,String>();
				hmpCreator.put("name", (String)node.getFirstChild().getNodeValue());
				hmpCreator.put("role", (String)node.getAttributes().getNamedItem("Role").getNodeValue());
				
				amazonItem.Creators.add(hmpCreator);
			}
		}
		
	}
	/**
	 * Item中のASIN要素の値を抽出します
	 * <pre>
	 *  <Item>
	 *    <ASIN>value</ASIN>
	 *  </Item>
	 * </pre>
	 * ASIN : ItemLookupResponse : Items : Item : ASIN
	 * @param elementItem Itemエレメント
	 * @return ASIN値
	 */
	public String parseASIN(Element elementItem) {
			return getElementTextContent(elementItem,"ASIN");
	}
	/**
	 * 指定タグのタグ名で括っているテキストコンテンツを取得します
	 * @param element ルートエレメント
	 * @param tagName 配下のタグ名
	 * @return テキストコンテンツ (null = テキストコンテンツなし)
	 */
	public String getElementTextContent(Element element,String tagName) {
		org.w3c.dom.Element valueElement = (org.w3c.dom.Element)(element.getElementsByTagName(tagName)).item(0);
		if(valueElement==null)
			return null;
		return valueElement.getTextContent();
	}

}
