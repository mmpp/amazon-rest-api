package org.mmpp.amazon.rest;

import org.mmpp.amazon.rest.request.generator.RequestParameterException;
import org.mmpp.amazon.rest.request.model.IdType;
import org.mmpp.amazon.rest.request.model.Request;
import org.mmpp.amazon.rest.request.model.SearchIndex;
import org.mmpp.amazon.rest.request.model.SearchType;
import org.mmpp.amazon.rest.response.model.AbstractResponse;


/**
 * Amazonの検索を行うサービスを提供します
 * @author wataru
 */
public interface AmazonInquisitor {
	/**
	 * 環境変数 アクセス証明書 アクセスキー
	 */
	public final static String ENVKEY_ACCESSKEY = "org.mmpp.aws.accesskey";
	/**
	 * 環境変数 アクセス証明書 シークレットアクセスキー
	 */
	public final static String ENVKEY_SECRETKEY = "org.mmpp.aws.secretkey";
	/**
	 * 環境変数 アフィリエイト トラッキングID
	 */
	public final static String ENVKEY_TRACKINGID = "org.mmpp.aws.trackingid";
	/**
	 * amazonアカウントファイル名
	 */
	public final static String AWS_CONFIG_FILE = "amazonaccount.properties";

	/**
	 * リクエストを送信します
	 * @param request リクエスト
	 * @return レスポンス
	 * @throws RequestParameterException
	 */
	public AbstractResponse postRequest(Request request) throws RequestParameterException;
	/**
	 * アイテム抽出します
	 * @param searchIdex 種別
	 * @param idType 抽出タイプ
	 * @param itemId 抽出値
	 * @return レスポンス
	 * @throws RequestParameterException
	 */
	public AbstractResponse lookup(SearchIndex searchIdex,IdType idType,String itemId) throws RequestParameterException;
	/**
	 * アイテムを検索します
	 * @param searchIdex 種別
	 * @param searchParameters 検索条件
	 * @return レスポンス
	 * @throws RequestParameterException
	 */
	public AbstractResponse search(SearchIndex searchIdex,java.util.Map<SearchType,String> searchParameters) throws RequestParameterException;
}
