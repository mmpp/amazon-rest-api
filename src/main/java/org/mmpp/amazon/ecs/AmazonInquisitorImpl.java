package org.mmpp.amazon.ecs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Map;

import org.mmpp.amazon.ecs.model.Account;
import org.mmpp.amazon.ecs.request.generator.RequestParameterException;
import org.mmpp.amazon.ecs.request.model.AccessCertificate;
import org.mmpp.amazon.ecs.request.model.AffiliateAccount;
import org.mmpp.amazon.ecs.request.model.IdType;
import org.mmpp.amazon.ecs.request.model.ItemLookupRequest;
import org.mmpp.amazon.ecs.request.model.ItemSearchRequest;
import org.mmpp.amazon.ecs.request.model.Request;
import org.mmpp.amazon.ecs.request.model.SearchIndex;
import org.mmpp.amazon.ecs.request.model.SearchType;
import org.mmpp.amazon.ecs.response.model.AbstractResponse;

/**
 * Amazonの検索を行うサービスの実装クラス
 * @author wataru
 */
public class AmazonInquisitorImpl implements AmazonInquisitor{
	/**
	 * ログ
	 */
	private java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().getName());

	/**
	 * アマゾンへのアクセスアカウント
	 */
	private Account _account = null;
	/**
	 * デフォルトコンストラクタ<br>
	 * デフォルトコンストラクタの利用は環境変数にてアカウント情報を指定する事
	 */
	public AmazonInquisitorImpl(){
		super();
	}
	/**
	 * コンストラクタ
	 * @param account アカウント情報
	 */
	public AmazonInquisitorImpl(Account account){
		this();
		_account = account;
	}
	/**
	 * 結果をラップするコンストラクタ
	 * @param accessService　アクセスサービス
	 */
	public AmazonInquisitorImpl(AbstractAccessService accessService) {
		this();
		_accessService = accessService;
	}
	/**
	 * アカウント情報
	 * @return
	 */
	public Account getAccount(){
		if(_account==null){
			// 環境変数
			_account = loadAccountEnvironment();
			if(_account==null){
				// 設定ファイル
				_account = loadAccountProperties();
			}
		}
		return _account;
	}
	@Override
	public AbstractResponse postRequest(Request request) throws RequestParameterException {
		if(!isAmazonAccessAccount(request)){
			storeAmazonAccessAccountFromEnv(request);
		}
		return post(request);
	}
	
	/**
	 * リクエストに環境変数に登録されている
	 * アカウント情報を登録します
	 * @param request リクエスト
	 */
	protected void storeAmazonAccessAccountFromEnv(Request request) {
		Account account = getAccount();
		if(account==null)
			return;
		AccessCertificate accessCertificate = new AccessCertificate();
		accessCertificate.setAccessKey(account.getAccessKey());
		accessCertificate.setSecretKey(account.getSecretKey());
		AffiliateAccount affiliateAccount = new AffiliateAccount();
		affiliateAccount.setTrackingID(account.getTrackingID());
		request.getItemRequest().setAccessCertificate(accessCertificate);
		request.getItemRequest().setAffiliateAccount(affiliateAccount);
	}
	/**
	 * リクエストにアカウント情報が登録されているかの判断
	 * @param request リクエスト
	 * @return true : アカウント情報が格納されている
	 */
	protected boolean isAmazonAccessAccount(Request request) {
		if(request.getItemRequest().getAccessCertificate()==null)
			return false;
		if(request.getItemRequest().getAffiliateAccount()==null)
			return false;
		if(request.getItemRequest().getAccessCertificate().getAccessKey()==null)
			return false;
		if(request.getItemRequest().getAccessCertificate().getSecretKey()==null)
			return false;
		if(request.getItemRequest().getAffiliateAccount().getTrackingID()==null)
			return false;
		
		return true;
	}
	@Override
	public AbstractResponse lookup(SearchIndex searchIdex, IdType idType,String itemId) throws RequestParameterException {
		Request request = new Request();
		
		ItemLookupRequest itemLookupRequest = new ItemLookupRequest();
		
		itemLookupRequest.setIdType(IdType.ISBN);
		itemLookupRequest.setItemId(itemId);
		itemLookupRequest.setSearchIndex(SearchIndex.Books);

		request.setItemRequest(itemLookupRequest);
		return this.postRequest(request);
	}
	@Override
	public AbstractResponse search(SearchIndex searchIdex,Map<SearchType, String> searchParameters) throws RequestParameterException {
		Request request = new Request();
		
		ItemSearchRequest itemSearchRequest = new ItemSearchRequest();

		itemSearchRequest.setSearchIndex(searchIdex);
		itemSearchRequest.getSearchParameters().putAll(searchParameters);

		request.setItemRequest(itemSearchRequest);
		return this.postRequest(request);
	}
	/**
	 * 環境変数からアクセスアカウントを読み込みます
	 * @return アクセスアカウント (null : 不足あり)
	 */
	private Account loadAccountEnvironment() {
		return loadAccountProperties(System.getProperties());
	}
	/**
	 * 設定ファイルからアクセスアカウント情報を取得します
	 * @return アクセスアカウント (null : 不足あり)
	 */
	private Account loadAccountProperties() {
        java.io.InputStream in = getClass().getClassLoader().getResourceAsStream(AWS_CONFIG_FILE);
        if(in==null)
        	return null;
        java.util.Properties properties = new java.util.Properties();  
        try {
        	properties.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return loadAccountProperties(properties);
	}
	/**
	 * アクセスアカウントを読み込みます
	 * @param properties プロパティ
	 * @return アクセスアカウント
	 */
	private Account loadAccountProperties(java.util.Properties properties) {
		String accessKey, secretKey, trackingID;
		accessKey = properties.getProperty(ENVKEY_ACCESSKEY);
		secretKey = properties.getProperty(ENVKEY_SECRETKEY);		
		trackingID = properties.getProperty(ENVKEY_TRACKINGID);
		
		// どれかが不足していたらNull
		if(accessKey==null)
			return null;
		if(secretKey==null)
			return null;
		if(trackingID==null)
			return null;
		
		Account account = new Account();
		account.setAccessKey(accessKey);
		account.setSecretKey(secretKey);
		account.setTrackingID(trackingID);
		return account;
	}

	private AbstractAccessService _accessService = null;

	public AbstractAccessService getAccessService(){
		if(_accessService==null){
			_accessService = new WebAccessService();
		}
		return _accessService;
	}
	public AbstractResponse post(Request request) throws RequestParameterException {
		try { 
			// 接続します
			getAccessService().connect(request);
			// 
			return getAccessService().postRequest(request);
		} catch (MalformedURLException e) {
			logger.log(java.util.logging.Level.WARNING,"Amazon検索用のURL記述に誤りがあります",e);
		} catch (ProtocolException e) {
			logger.log(java.util.logging.Level.WARNING,"プロトコルが不明です",e);
		} catch (IOException e) {
			logger.log(java.util.logging.Level.WARNING,"入出力に例外が発生しました",e);
		}finally{
			if(getAccessService()!=null){
				// 切断
				getAccessService().disconnect();
			}
		}
		return null;
	}

}
