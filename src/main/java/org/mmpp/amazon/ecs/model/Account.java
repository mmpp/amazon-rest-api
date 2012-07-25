package org.mmpp.amazon.ecs.model;

/**
 * Amazonアクセスアカウント
 * @author wataru
 */
public class Account {
	/** アクセス証明書 アクセスキー */
	public String _accessKey;
	/** アクセス証明書 シークレットアクセスキー */
	public String _secretKey;
	/** アフィリエイト トラッキングID */
	public String _trackingID;
	/**
	 * アクセスキーを取得する
	 * @return アクセスキー
	 */
	public String getAccessKey() {
		return _accessKey;
	}
	/**
	 * アクセスキーを格納します
	 * @param accessKey アクセスキー
	 */
	public void setAccessKey(String accessKey) {
		this._accessKey = accessKey;
	}
	/**
	 * シークレットアクセスキーを取得します
	 * @return シークレットアクセスキー
	 */
	public String getSecretKey() {
		return _secretKey;
	}
	/**
	 * シークレットアクセスキーを格納します
	 * @param secretKey シークレットアクセスキー
	 */
	public void setSecretKey(String secretKey) {
		this._secretKey = secretKey;
	}
	/**
	 * トラッキングIDを取得します
	 * @return トラッキングID
	 */
	public String getTrackingID() {
		return _trackingID;
	}
	/**
	 * トラッキングIDを格納します
	 * @param trackingID トラッキングID
	 */
	public void setTrackingID(String trackingID) {
		this._trackingID = trackingID;
	}

}
