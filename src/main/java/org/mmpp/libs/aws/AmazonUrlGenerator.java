package org.mmpp.libs.aws;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AmazonUrlGenerator {
	/**
	 * URL作成します
	 * @param account Amazonアカウント情報
	 * @param barcode バーコード
	 * @param title タイトル
	 * @param author 著者名
	 * @param pageno ページ番号
	 * @return アクセスURL
	 */
	public static String createUrl(AmazonAccount account,String barcode,String title,String author,int pageno){
		java.util.Map<String, String> map =  new java.util.HashMap<String, String>();
		map.put("Service","AWSECommerceService");
		map.put("SearchIndex","Books");
		map.put("AssociateTag",account.getAffiliateID());
		if(barcode!=null){
			map.put("Operation","ItemLookup");
			map.put("IdType","ISBN");
			map.put("ItemId",barcode);
			return  castUrlWithParameters(account,map);
		}
		map.put("Operation","ItemSearch");
		if(title!=null)
			map.put("Title",title);
		if(author!=null)
			map.put("Author",author);
		map.put("ItemPage",String.valueOf(pageno));
		map.put("__mk_ja_JP","カタカナ");
		return  castUrlWithParameters(account,map);
	}

	/**
	 * AmazonへのアクセスURLを生成します
	 * @param account Amazonアカウント情報
	 * @param params 引き渡しパラメタ
	 * @return アクセスURL (null:例外)
	 */
	private static String castUrlWithParameters(AmazonAccount account,java.util.Map<String,String> params){
		// アマゾンアクセスアカウントを取得 //
		// アクセスアカウントが取得できたか？
		if(account==null)
			// 出来ない場合、nullを返却
			return null;

		SignedRequestsHelper helper = new SignedRequestsHelper(account);
		try {
			return helper.sign(params);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
