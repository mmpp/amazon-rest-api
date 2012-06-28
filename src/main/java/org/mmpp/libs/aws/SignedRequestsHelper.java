package org.mmpp.libs.aws;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

	public class SignedRequestsHelper {
		private static final String UTF8_CHARSET = "UTF-8";
		private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
		private static final String REQUEST_URI = "/onca/xml";
		private static final String REQUEST_METHOD = "GET";

		private String endpoint = "ecs.amazonaws.jp";

		private Mac _mac = null;
		
		private AmazonAccount _amazonAccount ;
		
		public SignedRequestsHelper(AmazonAccount amazonAccount) {
			super();
			setAmazonAccount(amazonAccount);
		}
		private void setAmazonAccount(AmazonAccount amazonAccount) {
			this._amazonAccount = amazonAccount;
		}
		public AmazonAccount getAmazonAccount(){
			return _amazonAccount;
		}
		private Mac initMac() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException{
			Mac mac;
			byte[] secretyKeyBytes = getAmazonAccount().getSecretKey().getBytes(UTF8_CHARSET);
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretyKeyBytes, HMAC_SHA256_ALGORITHM);
			mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
			mac.init(secretKeySpec);
			return mac;
		}
		private Mac getMac() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException{
			if(_mac==null){
				_mac = initMac();
			}
			return _mac;
		}

		public String sign(Map<String, String> params) throws InvalidKeyException, IllegalStateException, NoSuchAlgorithmException {
			params.put("AWSAccessKeyId", getAmazonAccount().getAwsID());
			params.put("Timestamp", getTimestamp());

			SortedMap<String, String> sortedParamMap = new TreeMap<String, String>(params);
			String canonicalQS = canonicalize(sortedParamMap);
			String toSign =
				REQUEST_METHOD + "\n"
				+ endpoint + "\n"
				+ REQUEST_URI + "\n"
				+ canonicalQS;

			String hmac = hmac(toSign);
			String sig = percentEncodeRfc3986(hmac);
			String url = "http://" + endpoint + REQUEST_URI + "?" +
								canonicalQS + "&Signature=" + sig;

			return url;
		}

		private String hmac(String stringToSign) throws InvalidKeyException, IllegalStateException, NoSuchAlgorithmException {
			String signature = null;
			byte[] data;
			byte[] rawHmac;
			try {
				data = stringToSign.getBytes(UTF8_CHARSET);
				rawHmac = getMac().doFinal(data);
				Base64 encoder = new Base64();
				signature = new String(encoder.encode(rawHmac));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(UTF8_CHARSET + " is unsupported!", e);
			}
			return signature;
		}

		/**
		 * 現在時刻文字列を取得します
		 * @return 現在時刻文字列
		 */
		private String getTimestamp() {
			String timestamp = null;
			Calendar cal = Calendar.getInstance();
			DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			dfm.setTimeZone(TimeZone.getTimeZone("GMT"));
			timestamp = dfm.format(cal.getTime());
			return timestamp;
		}

		/**
		 * マップを文字列に変換します
		 * @param map マップクラス
		 * @return 文字列
		 */
		public static String canonicalize(SortedMap<String, String> map){
			if (map.isEmpty()) {
				return "";
			}

			StringBuffer buffer = new StringBuffer();
			for (Map.Entry<String, String> kvpair:map.entrySet()) {
				buffer.append("&");
				buffer.append(percentEncodeRfc3986(kvpair.getKey()));
				buffer.append("=");
				buffer.append(percentEncodeRfc3986(kvpair.getValue()));
			}
			return buffer.toString().substring(1);
		}

		/**
		 * URLをアクセス可能なエンコードします
		 * @param url 変換前URL
		 * @return エスケープ後URL
		 */
		public static String percentEncodeRfc3986(String url) {
			try {
				return java.net.URLEncoder.encode(url, UTF8_CHARSET)
					.replace("+", "%20")
					.replace("*", "%2A")
					.replace("%7E", "~");
			} catch (UnsupportedEncodingException e) {
			}
			return url;
		}

	}
