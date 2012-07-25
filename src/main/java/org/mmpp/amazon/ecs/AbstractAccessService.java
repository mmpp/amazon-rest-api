package org.mmpp.amazon.ecs;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.mmpp.amazon.ecs.request.generator.RequestParameterException;
import org.mmpp.amazon.ecs.request.model.Request;
import org.mmpp.amazon.ecs.response.model.AbstractResponse;
import org.mmpp.amazon.ecs.response.parser.AmazonServiceResponseParser;
import org.mmpp.amazon.ecs.response.parser.AmazonServiceResponseParserImpl;
import org.xml.sax.SAXException;

public abstract class AbstractAccessService {
	/**
	 * ログ
	 */
	private java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().getName());

	protected abstract java.io.InputStream getInputStream() throws IOException;
	
	public abstract void disconnect();
	public abstract void connect(Request request) throws RequestParameterException, IOException;

	public AbstractResponse postRequest(Request request) throws IOException {
		// XML解析フェーズ
		// 参考 : http://www.hellohiro.com/xmldom.htm
		//       http://java.sun.com/j2se/1.5.0/ja/docs/ja/api/javax/xml/parsers/DocumentBuilder.html
		try {
			javax.xml.parsers.DocumentBuilderFactory  docBFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			javax.xml.parsers.DocumentBuilder builder = docBFactory.newDocumentBuilder();
			AmazonServiceResponseParser parser = new AmazonServiceResponseParserImpl();
			  // パースを実行してDocumentオブジェクトを取得
			// Webのインプットストリームからドキュメント生成...
			org.w3c.dom.Document document = builder.parse(getInputStream()); 
			return parser.parse(document);
			
		} catch (ParserConfigurationException e) {
			logger.log(java.util.logging.Level.WARNING,"検索結果のドキュメントが解析できませんでした",e);
		} catch (SAXException e) {
			logger.log(java.util.logging.Level.WARNING,"HTTPのドキュメントが解析できませんでした",e);
		}
		return null;
	}
}
