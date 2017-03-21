package com.songxinjing.goodfind.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.songxinjing.goodfind.domain.ProdPrice;

public class GFHttpClient {

	protected static final Logger logger = LoggerFactory.getLogger(GFHttpClient.class);

	private static CloseableHttpClient httpclient = HttpClients.createDefault();

	private static String jSessionId;

	private static String dseSessionId;

	private static String userId = "*FF*8A8F*19*DFd*23*EA2*9F*3EE*C1*01kG*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00";

	public static void setjSessionId(String jSessionId) {
		GFHttpClient.jSessionId = jSessionId;
	}

	public static void setDseSessionId(String dseSessionId) {
		GFHttpClient.dseSessionId = dseSessionId;
	}

	public static CloseableHttpResponse httpGet(String url, Header[] headers)
			throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(url);
		httpGet.setProtocolVersion(HttpVersion.HTTP_1_1);
		httpGet.setHeaders(headers);
		return httpclient.execute(httpGet);
	}

	public static CloseableHttpResponse httpPost(String url, Header[] headers, List<NameValuePair> nvps)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setProtocolVersion(HttpVersion.HTTP_1_1);
		httpPost.setHeaders(headers);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		return httpclient.execute(httpPost);
	}

	public static List<Header> getGFHeaders() {
		List<Header> list = new ArrayList<Header>();
		list.add(new BasicHeader("Host", "trade.gf.com.cn"));
		list.add(new BasicHeader("Connection", "keep-alive"));
		list.add(new BasicHeader("Accept", "*/*"));
		list.add(new BasicHeader("Origin", "https://trade.gf.com.cn"));
		list.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
		list.add(new BasicHeader("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36"));
		list.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
		list.add(new BasicHeader("Referer", "https://trade.gf.com.cn/workbench/index.jsp"));
		list.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		list.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"));
		return list;
	}

	public static ProdPrice getProdPrice(String fundCode) {

		String url = "https://trade.gf.com.cn/entry";
		List<Header> list = getGFHeaders();
		list.add(new BasicHeader("Cookie",
				"name=value; JSESSIONID=" + jSessionId + "; dse_sessionId=" + dseSessionId + "; userId=" + userId));
		Header[] headers = list.toArray(new Header[list.size()]);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("classname", "com.gf.etrade.control.NXBUF2Control"));
		nvps.add(new BasicNameValuePair("method", "nxbQueryPrice"));
		nvps.add(new BasicNameValuePair("fund_code", fundCode));
		nvps.add(new BasicNameValuePair("dse_sessionId", dseSessionId));

		try {
			// logger.debug("Begin Send...");
			CloseableHttpResponse response = GFHttpClient.httpPost(url, headers, nvps);
			Header setCookieHeader = response.getFirstHeader("Set-Cookie");
			if (setCookieHeader != null) {
				// logger.error(setCookieHeader.getName() + " : " +
				// setCookieHeader.getValue());
				return null;
			}
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					String body = EntityUtils.toString(entity);
					JSONObject json = JSONObject.parseObject(body);
					if (json == null) {
						logger.debug("body: " + body);
						return null;
					}
					JSONArray dataArray = json.getJSONArray("data");
					if (dataArray != null && dataArray.size() > 0) {
						JSONObject data = dataArray.getJSONObject(0);
						ProdPrice prodPrice = new ProdPrice();
						prodPrice.setProdCode(fundCode);
						prodPrice.setBuyPrice1(data.getString("buy_price1"));
						prodPrice.setSalePrice1(data.getString("sale_price1"));
						prodPrice.setBuyAmount1(data.getFloat("buy_amount1").intValue() / 1000);
						prodPrice.setSaleAmount1(data.getFloat("sale_amount1").intValue() / 1000);
						// logger.debug(prodPrice.toString());
						return prodPrice;
					}
				}
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> doubleOrder(ProdPrice longProd, ProdPrice shortProd) {

		String url = "https://trade.gf.com.cn/entry";
		List<Header> list = getGFHeaders();
		list.add(new BasicHeader("Cookie",
				"name=value; JSESSIONID=" + jSessionId + "; dse_sessionId=" + dseSessionId + "; userId=" + userId));
		Header[] headers = list.toArray(new Header[list.size()]);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("classname", "com.gf.etrade.control.NXBUF2Control"));
		nvps.add(new BasicNameValuePair("method", "nxbdoubleentrust"));
		nvps.add(new BasicNameValuePair("dse_sessionId", dseSessionId));
		nvps.add(new BasicNameValuePair("fund_code", longProd.getProdCode()));
		nvps.add(new BasicNameValuePair("entrust_amount", "1000"));
		nvps.add(new BasicNameValuePair("entrust_price", longProd.getSalePrice1()));
		nvps.add(new BasicNameValuePair("fund_code_1", shortProd.getProdCode()));
		nvps.add(new BasicNameValuePair("entrust_amount_1", "1000"));
		nvps.add(new BasicNameValuePair("entrust_price_1", shortProd.getSalePrice1()));
		nvps.add(new BasicNameValuePair("entrust_bs", "1"));

		try {
			// logger.debug("Begin Send...");
			CloseableHttpResponse response = GFHttpClient.httpPost(url, headers, nvps);
			Header setCookieHeader = response.getFirstHeader("Set-Cookie");
			if (setCookieHeader != null) {
				// logger.error(setCookieHeader.getName() + " : " +
				// setCookieHeader.getValue());
				return null;
			}
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					String body = EntityUtils.toString(entity);
					JSONObject json = JSONObject.parseObject(body);
					if (json == null) {
						logger.debug("body: " + body);
						return null;
					}
					String success = json.getString("success");
					if ("true".equals(success)) {
						JSONArray dataArray = json.getJSONArray("data");
						if (dataArray != null && dataArray.size() > 0) {
							JSONObject data = dataArray.getJSONObject(0);
							List<String> entrustList = new ArrayList<String>();
							entrustList.add(data.getString("entrust_no"));
							logger.info("entrust_no: " + data.getString("entrust_no"));
							entrustList.add(data.getString("entrust_no_1"));
							logger.info("entrust_no_1: " + data.getString("entrust_no_1"));
							return entrustList;
						}
					}
				}
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void findArbitrage(ProdPrice longProd, ProdPrice shortProd) {

		Float fBuyArbi = (Float.parseFloat(longProd.getSalePrice1()) + Float.parseFloat(shortProd.getSalePrice1()))
				* 1000;

		int buyArbi = fBuyArbi.intValue();

		logger.info("\n[" + longProd.getProdCode() + "]longSell:" + longProd.getSalePrice1() + ", shortSell:"
				+ shortProd.getSalePrice1() + ", buyArbi:" + buyArbi);

		if (buyArbi <= 1998) {
			logger.error("\n[" + longProd.getProdCode() + "]Arbi!!!===Long:" + longProd.getSalePrice1() + "["
					+ longProd.getSaleAmount1() + "], Short:" + shortProd.getSalePrice1() + "["
					+ shortProd.getSaleAmount1() + "], buyArbi=" + buyArbi);

			doubleOrder(longProd, shortProd);
			return;
		}

		Float fSellArbi = (Float.parseFloat(longProd.getBuyPrice1()) + Float.parseFloat(shortProd.getBuyPrice1()))
				* 1000;

		int sellArbi = fSellArbi.intValue();

		logger.info("\n[" + longProd.getProdCode() + "]longBuy:" + longProd.getBuyPrice1() + ", shortBuy:"
				+ shortProd.getBuyPrice1() + ", sellArbi:" + sellArbi);

		if (sellArbi >= 2002) {
			logger.error("\n[" + longProd.getProdCode() + "]Arbi!!!===Long:" + longProd.getBuyPrice1() + "["
					+ longProd.getBuyAmount1() + "], Short:" + shortProd.getBuyPrice1() + "["
					+ shortProd.getBuyAmount1() + "], sellArbi=" + sellArbi);
			return;
		}

	}

}
