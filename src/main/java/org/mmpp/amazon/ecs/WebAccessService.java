package org.mmpp.amazon.ecs;

import java.io.IOException;
import java.io.InputStream;

import org.mmpp.amazon.ecs.request.generator.RequestParameterException;
import org.mmpp.amazon.ecs.request.generator.URLGenerator;
import org.mmpp.amazon.ecs.request.model.Request;
import org.mmpp.amazon.ecs.response.model.AbstractResponse;

public class WebAccessService extends AbstractAccessService{
	/**
	 * ログ
	 */
	private java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().getName());

	java.net.HttpURLConnection connectWeb = null;

	@Override
	public InputStream getInputStream() throws IOException {
		return connectWeb.getInputStream();
	}

	@Override
	public void disconnect() {
		if(connectWeb!=null){
			// 切断
			connectWeb.disconnect();
		}		
	}

	@Override
	public void connect(Request request) throws RequestParameterException, IOException {

		// アクセスurlを取得
		String url = new URLGenerator().parse(request);
		
		logger.info(" Access url : "+url);
		
		AbstractResponse itemResponse=null;
		
		java.net.HttpURLConnection connectWeb = null;
		
		// http接続初期化
		java.net.URL urlAmazon = new java.net.URL(url);
		
		connectWeb = (java.net.HttpURLConnection)urlAmazon.openConnection();
		connectWeb.setRequestMethod("GET");
		java.net.HttpURLConnection.setFollowRedirects(false);
		connectWeb.setInstanceFollowRedirects(false);
		
		
		// 接続
		connectWeb.connect();

	}

	
}
