package com.songxinjing.goodfind.httpclient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.songxinjing.goodfind.constant.Constant;
import com.songxinjing.goodfind.domain.OrderInfo;
import com.songxinjing.goodfind.domain.ProdPrice;
import com.songxinjing.goodfind.exception.ConnectionCloseException;
import com.songxinjing.goodfind.util.CommUtils;

public class GFHttpClient {

	protected static final Logger logger = LoggerFactory.getLogger(GFHttpClient.class);

	private static CloseableHttpClient httpclient;

	private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000)
			.setConnectionRequestTimeout(10000).build();

	private static String jSessionId;

	private static String dseSessionId;

	private static String userId = "*FF*8A8F*19*DFd*23*EA2*9F*3EE*C1*01kG*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00";

	private static BigDecimal bd1_999 = new BigDecimal("1.999");
	private static BigDecimal bd2_001 = new BigDecimal("2.001");

	static {
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(100);
		connManager.setDefaultMaxPerRoute(10);
		httpclient = HttpClients.custom().setConnectionManager(connManager).build();
	}

	public static void setjSessionId(String jSessionId) {
		GFHttpClient.jSessionId = jSessionId;
	}

	public static void setDseSessionId(String dseSessionId) {
		GFHttpClient.dseSessionId = dseSessionId;
	}

	public static CloseableHttpResponse httpGet(String url, Header[] headers) throws IOException {
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		httpGet.setProtocolVersion(HttpVersion.HTTP_1_1);
		httpGet.setHeaders(headers);
		return httpclient.execute(httpGet);
	}

	public static CloseableHttpResponse httpPost(String url, Header[] headers, List<NameValuePair> nvps)
			throws IOException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		httpPost.setProtocolVersion(HttpVersion.HTTP_1_1);
		httpPost.setHeaders(headers);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		return httpclient.execute(httpPost);
	}

	public static List<Header> getGFHeaders() {
		List<Header> list = new ArrayList<Header>();
		list.add(new BasicHeader("Host", "etrade.gf.com.cn"));
		list.add(new BasicHeader("Connection", "keep-alive"));
		list.add(new BasicHeader("Accept", "*/*"));
		list.add(new BasicHeader("Origin", "https://etrade.gf.com.cn"));
		list.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
		list.add(new BasicHeader("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36"));
		list.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
		list.add(new BasicHeader("Referer", "https://trade.gf.com.cn/workbench/index.jsp"));
		list.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		list.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"));
		return list;
	}

	/**
	 * 寻找套利
	 * 
	 * @param longProd
	 * @param shortProd
	 * @throws IOException
	 * @throws ConnectionCloseException
	 */
	public static void findArbitrage(ProdPrice longProd, ProdPrice shortProd)
			throws IOException, ConnectionCloseException {

		BigDecimal bdLongSell = new BigDecimal(longProd.getSalePrice1());
		BigDecimal bdShortSell = new BigDecimal(shortProd.getSalePrice1());
		BigDecimal buyArbi = bdLongSell.add(bdShortSell);

		if (buyArbi.compareTo(bd1_999) <= 0) {
			int num = longProd.getSaleAmount1() <= shortProd.getSaleAmount1() ? longProd.getSaleAmount1()
					: shortProd.getSaleAmount1();
			num = num / Constant.DEAL_RATIO + 1;
			num = num <= Constant.MAX_DEAL_NUM ? num : Constant.MAX_DEAL_NUM;

			doubleOrder(longProd, shortProd, true, num);
			logger.info("[" + longProd.getProdCode() + "]Arbi!!!===Long:" + longProd.getSalePrice1() + "["
					+ longProd.getSaleAmount1() + "], Short:" + shortProd.getSalePrice1() + "["
					+ shortProd.getSaleAmount1() + "], buyArbi=" + buyArbi + "[" + num + "]");
			CommUtils.sleep(5000);
			dealOneSide();
			return;
		}

		BigDecimal bdLongBuy = new BigDecimal(longProd.getBuyPrice1());
		BigDecimal bdShortBuy = new BigDecimal(shortProd.getBuyPrice1());
		BigDecimal sellArbi = bdLongBuy.add(bdShortBuy);

		if (sellArbi.compareTo(bd2_001) >= 0) {
			int num = longProd.getBuyAmount1() <= shortProd.getBuyAmount1() ? longProd.getBuyAmount1()
					: shortProd.getBuyAmount1();
			num = num / Constant.DEAL_RATIO + 1;
			num = num <= Constant.MAX_DEAL_NUM ? num : Constant.MAX_DEAL_NUM;
			doubleOrder(longProd, shortProd, false, num);
			logger.info("[" + longProd.getProdCode() + "]Arbi!!!===Long:" + longProd.getBuyPrice1() + "["
					+ longProd.getBuyAmount1() + "], Short:" + shortProd.getBuyPrice1() + "["
					+ shortProd.getBuyAmount1() + "], sellArbi=" + sellArbi + "[" + num + "]");
			CommUtils.sleep(5000);
			dealOneSide();
			return;
		}

		logger.info("[" + longProd.getProdCode() + "] " + sellArbi + " - " + buyArbi);

	}

	/**
	 * 获取价格
	 * 
	 * @param fundCode
	 * @return
	 * @throws IOException
	 * @throws ConnectionCloseException
	 */
	public static ProdPrice getProdPrice(String fundCode) throws IOException, ConnectionCloseException {
		String url = "https://etrade.gf.com.cn/entry";
		List<Header> list = getGFHeaders();
		list.add(new BasicHeader("Cookie",
				"name=value; JSESSIONID=" + jSessionId + "; dse_sessionId=" + dseSessionId + "; userId=" + userId));
		Header[] headers = list.toArray(new Header[list.size()]);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("classname", "com.gf.etrade.control.NXBUF2Control"));
		nvps.add(new BasicNameValuePair("method", "nxbQueryPrice"));
		nvps.add(new BasicNameValuePair("fund_code", fundCode));
		nvps.add(new BasicNameValuePair("dse_sessionId", dseSessionId));
		CloseableHttpResponse response = httpPost(url, headers, nvps);
		String connState = response.getFirstHeader("Connection").getValue();
		logger.debug("获取价格，Connection状态：" + connState);
		if ("close".equals(connState)) {
			throw new ConnectionCloseException();
		}
		try {
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String body = EntityUtils.toString(entity);
					JSONObject json = JSONObject.parseObject(body);
					if (json != null) {
						JSONArray dataArray = json.getJSONArray("data");
						if (dataArray != null && dataArray.size() > 0) {
							JSONObject data = dataArray.getJSONObject(0);
							ProdPrice prodPrice = new ProdPrice();
							prodPrice.setProdCode(fundCode);
							prodPrice.setBuyPrice1(data.getString("buy_price1"));
							prodPrice.setSalePrice1(data.getString("sale_price1"));
							prodPrice.setBuyAmount1(data.getFloat("buy_amount1").intValue() / 1000);
							prodPrice.setSaleAmount1(data.getFloat("sale_amount1").intValue() / 1000);
							return prodPrice;
						}
					}
				}
			}
		} finally {
			response.close();
		}
		return null;
	}

	/**
	 * 双向订单
	 * 
	 * @param longProd
	 * @param shortProd
	 * @param buyOrsell
	 * @return
	 * @throws IOException
	 */
	public static void doubleOrder(ProdPrice longProd, ProdPrice shortProd, boolean isBuy, int num) throws IOException {
		String url = "https://etrade.gf.com.cn/entry";
		List<Header> list = getGFHeaders();
		list.add(new BasicHeader("Cookie",
				"name=value; JSESSIONID=" + jSessionId + "; dse_sessionId=" + dseSessionId + "; userId=" + userId));
		Header[] headers = list.toArray(new Header[list.size()]);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("classname", "com.gf.etrade.control.NXBUF2Control"));
		nvps.add(new BasicNameValuePair("method", "nxbdoubleentrust"));
		nvps.add(new BasicNameValuePair("dse_sessionId", dseSessionId));
		nvps.add(new BasicNameValuePair("fund_code", longProd.getProdCode()));
		nvps.add(new BasicNameValuePair("entrust_amount", String.valueOf(num * 1000)));
		nvps.add(new BasicNameValuePair("fund_code_1", shortProd.getProdCode()));
		nvps.add(new BasicNameValuePair("entrust_amount_1", String.valueOf(num * 1000)));
		if (isBuy) {
			nvps.add(new BasicNameValuePair("entrust_price", longProd.getSalePrice1()));
			nvps.add(new BasicNameValuePair("entrust_price_1", shortProd.getSalePrice1()));
			nvps.add(new BasicNameValuePair("entrust_bs", "1"));
		} else {
			nvps.add(new BasicNameValuePair("entrust_price", longProd.getBuyPrice1()));
			nvps.add(new BasicNameValuePair("entrust_price_1", shortProd.getBuyPrice1()));
			nvps.add(new BasicNameValuePair("entrust_bs", "2"));
		}
		httpPost(url, headers, nvps);
	}

	/**
	 * 处理单边成交
	 * 
	 * @throws IOException
	 * @throws ConnectionCloseException
	 */
	public static void dealOneSide() throws IOException, ConnectionCloseException {
		List<OrderInfo> infos = orderCheck();
		while (infos.size() > 0) {
			for (OrderInfo info : infos) {
				boolean isCancel = cancelOrder(info.getEntrustNo());
				if (isCancel) {
					logger.info("成功撤销订单：" + info.getEntrustNo());
					ProdPrice prodPrice = getProdPrice(info.getProdCode());
					if (prodPrice != null) {
						if (info.isBuy()) {
							info.setOrderPrice(prodPrice.getSalePrice1());
						} else {
							info.setOrderPrice(prodPrice.getBuyPrice1());
						}
						logger.info("发起单边弥补订单");
						singleOrder(info);
					}
				}
			}
			CommUtils.sleep(5000);
			infos = orderCheck();
		}
	}

	/**
	 * 获取未成交订单
	 * 
	 * @param isBuy
	 * @return
	 * @throws IOException
	 * @throws ConnectionCloseException
	 */
	public static List<OrderInfo> orderCheck() throws IOException, ConnectionCloseException {
		String url = "https://etrade.gf.com.cn/entry";
		List<Header> list = getGFHeaders();
		list.add(new BasicHeader("Cookie",
				"name=value; JSESSIONID=" + jSessionId + "; dse_sessionId=" + dseSessionId + "; userId=" + userId));
		Header[] headers = list.toArray(new Header[list.size()]);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("classname", "com.gf.etrade.control.NXBUF2Control"));
		nvps.add(new BasicNameValuePair("method", "nxbQueryEntrust"));
		nvps.add(new BasicNameValuePair("query_type", "1"));
		nvps.add(new BasicNameValuePair("query_mode", "1"));
		nvps.add(new BasicNameValuePair("prodta_no", "98"));
		nvps.add(new BasicNameValuePair("entrust_no", "0"));
		nvps.add(new BasicNameValuePair("fund_code", ""));
		nvps.add(new BasicNameValuePair("start_date", "0"));
		nvps.add(new BasicNameValuePair("end_date", "0"));
		nvps.add(new BasicNameValuePair("position_str", "0"));
		nvps.add(new BasicNameValuePair("limit", "10"));
		nvps.add(new BasicNameValuePair("dse_sessionId", dseSessionId));
		nvps.add(new BasicNameValuePair("start", "0"));
		List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
		CloseableHttpResponse response = httpPost(url, headers, nvps);
		String connState = response.getFirstHeader("Connection").getValue();
		logger.debug("获取未成交订单，Connection状态：" + connState);
		if ("close".equals(connState)) {
			throw new ConnectionCloseException();
		}
		try {
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String body = EntityUtils.toString(entity);
					JSONObject json = JSONObject.parseObject(body);
					if (json != null) {
						int notDeal = Integer.parseInt(json.getString("total"));
						logger.info("未成交订单数量：" + notDeal);
						if (notDeal > 0) {
							JSONArray dataArray = json.getJSONArray("data");
							for (int i = 0; i < dataArray.size(); i++) {
								JSONObject data = dataArray.getJSONObject(i);
								OrderInfo info = new OrderInfo();
								info.setProdCode(data.getString("prod_code"));
								info.setEntrustNo(data.getString("entrust_no"));
								String buyOrSell = data.getString("prod_prop_dict").trim();
								if ("多空杠杆买单".equals(buyOrSell)) {
									info.setBuy(true);
								} else {
									info.setBuy(false);
								}
								info.setOrderPrice(data.getString("entrust_price"));
								info.setOrderNum(data.getFloat("entrust_amount").intValue());
								orderInfos.add(info);
							}
						}
					}
				}
			}
		} finally {
			response.close();
		}
		return orderInfos;
	}

	/**
	 * 撤销订单
	 * 
	 * @param
	 * @return
	 * @throws IOException
	 * @throws ConnectionCloseException
	 */
	public static boolean cancelOrder(String entrustNo) throws IOException, ConnectionCloseException {
		String url = "https://etrade.gf.com.cn/entry";
		List<Header> list = getGFHeaders();
		list.add(new BasicHeader("Cookie",
				"name=value; JSESSIONID=" + jSessionId + "; dse_sessionId=" + dseSessionId + "; userId=" + userId));
		Header[] headers = list.toArray(new Header[list.size()]);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("classname", "com.gf.etrade.control.NXBUF2Control"));
		nvps.add(new BasicNameValuePair("method", "nxbentrustcancel"));
		nvps.add(new BasicNameValuePair("dse_sessionId", dseSessionId));
		nvps.add(new BasicNameValuePair("entrust_no", entrustNo));
		CloseableHttpResponse response = httpPost(url, headers, nvps);
		String connState = response.getFirstHeader("Connection").getValue();
		logger.debug("撤销订单，Connection状态：" + connState);
		if ("close".equals(connState)) {
			throw new ConnectionCloseException();
		}
		try {
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String body = EntityUtils.toString(entity);
					JSONObject json = JSONObject.parseObject(body);
					if (json != null) {
						logger.info("撤销订单...应答：" + json.getBoolean("success"));
						return json.getBoolean("success");
					}
				}
			}
		} finally {
			response.close();
		}
		return false;
	}

	/**
	 * 单向订单
	 * 
	 * @param OrderInfo
	 * @param buyOrsell
	 * @return
	 * @throws IOException
	 */
	public static void singleOrder(OrderInfo info) throws IOException {
		String url = "https://etrade.gf.com.cn/entry";
		List<Header> list = getGFHeaders();
		list.add(new BasicHeader("Cookie",
				"name=value; JSESSIONID=" + jSessionId + "; dse_sessionId=" + dseSessionId + "; userId=" + userId));
		Header[] headers = list.toArray(new Header[list.size()]);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("classname", "com.gf.etrade.control.NXBUF2Control"));
		nvps.add(new BasicNameValuePair("method", "nxbentrust"));
		nvps.add(new BasicNameValuePair("dse_sessionId", dseSessionId));
		nvps.add(new BasicNameValuePair("fund_code", info.getProdCode()));
		nvps.add(new BasicNameValuePair("entrust_amount", String.valueOf(info.getOrderNum())));
		nvps.add(new BasicNameValuePair("entrust_price", info.getOrderPrice()));
		if (info.isBuy()) {
			nvps.add(new BasicNameValuePair("entrust_bs", "1"));
		} else {
			nvps.add(new BasicNameValuePair("entrust_bs", "2"));
		}
		nvps.add(new BasicNameValuePair("auto_deal", "true"));
		httpPost(url, headers, nvps);
	}
}
