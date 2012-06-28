package org.mmpp.libs.aws;

/**
 * Amazonの検索を行うサービスの実装クラス
 * @author wataru
 *
 */
public class AmazonGetterImpl implements AmazonGetter{
	/**
	 * Amazonのアカウント情報
	 */
	private AmazonAccount _amazonAccount = null;
	/**
	 * RESTアクセス処理本体の格納変数
	 */
	private AmazonAccessService _amazonAccessService = null;
	/**
	 * デフォルトコンストラクタ<br>
	 * デフォルトコンストラクタの利用は環境変数にてアカウント情報を指定する事
	 */
	public AmazonGetterImpl(){
		super();
	}
	/**
	 * コンストラクタ
	 * @param asid AmazonWebservice Account ID
	 * @param secid AmazonWebService Sercret Key
	 * @param affiliateid Affilate ID
	 */
	public AmazonGetterImpl(String asid,String secid,String affiliateid){
		this();
		this._amazonAccount = new AmazonAccount(asid,secid,affiliateid);
	}
	@Override
	public AmazonAccount getAmazonAccount(){
		if(_amazonAccount==null){
			this._amazonAccount = AmazonAccount.loadFromEnviroment();
		}
		return this._amazonAccount;
	}
	/**
	 * Amazonアクセスサービスを取得します
	 * @return Amazonアクセスサービス
	 */
	private AmazonAccessService getAmazonAccessService(){
		if(_amazonAccessService==null){
			_amazonAccessService = new AmazonAccessServiceImpl(getAmazonAccount());
		}
		return _amazonAccessService;
	}
	@Override
	public AmazonItem[] getAmazonItemWithBarcode(String barcode) throws AmazonWebServiceException{
		return getAmazonAccessService().getAmazonItemsWithPages(barcode,null,null,1);
	}
	@Override
	public AmazonItem[] getAmazonItems(String title,String author) throws AmazonWebServiceException{
		return getAmazonItemsWithPages(title,author,1);	
	}
	@Override
	public AmazonItem[] getAmazonItemsWithPages(String title,String author,int pageno) throws AmazonWebServiceException{
		return getAmazonAccessService().getAmazonItemsWithPages(null,title,author,pageno);	
	}

}
