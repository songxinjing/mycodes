package com.songxinjing.goodfind.httpclient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.songxinjing.goodfind.domain.OrderInfo;
import com.songxinjing.goodfind.domain.ProdPrice;
import com.songxinjing.goodfind.util.CommUtils;

public class GFHttpClient {

	private static CloseableHttpClient httpclient = HttpClients.createDefault();

	private static String jSessionId;

	private static String dseSessionId;

	private static String userId = "*FF*8A8F*19*DFd*23*EA2*9F*3EE*C1*01kG*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00";

	private static BigDecimal bd1_998 = new BigDecimal("1.998");
	private static BigDecimal bd1_999 = new BigDecimal("1.999");
	private static BigDecimal bd2_001 = new BigDecimal("2.001");
	private static BigDecimal bd2_002 = new BigDecimal("2.002");

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

	public static ProdPrice getProdPrice(String fundCode) {

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

		try {
			CloseableHttpResponse response = GFHttpClient.httpPost(url, headers, nvps);
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String body = EntityUtils.toString(entity);
					JSONObject json = JSONObject.parseObject(body);
					if (json == null) {
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

	/**
	 * 单向订单
	 * 
	 * @param longProd
	 * @param shortProd
	 * @param buyOrsell
	 * @return
	 */
	public static void singleOrder(OrderInfo info) {

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

		try {
			// logger.debug("Begin Send...");
			GFHttpClient.httpPost(url, headers, nvps);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 双向订单
	 * 
	 * @param longProd
	 * @param shortProd
	 * @param buyOrsell
	 * @return
	 */
	public static void doubleOrder(ProdPrice longProd, ProdPrice shortProd, String buyOrsell) {

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
		nvps.add(new BasicNameValuePair("entrust_amount", "1000"));
		if ("1".equals(buyOrsell)) {
			nvps.add(new BasicNameValuePair("entrust_price", longProd.getSalePrice1()));
		} else {
			nvps.add(new BasicNameValuePair("entrust_price", longProd.getBuyPrice1()));
		}
		nvps.add(new BasicNameValuePair("fund_code_1", shortProd.getProdCode()));
		nvps.add(new BasicNameValuePair("entrust_amount_1", "1000"));
		if ("1".equals(buyOrsell)) {
			nvps.add(new BasicNameValuePair("entrust_price_1", shortProd.getSalePrice1()));
		} else {
			nvps.add(new BasicNameValuePair("entrust_price_1", shortProd.getBuyPrice1()));
		}
		nvps.add(new BasicNameValuePair("entrust_bs", buyOrsell));

		try {
			// logger.debug("Begin Send...");
			GFHttpClient.httpPost(url, headers, nvps);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取未成交订单
	 * 
	 * @param isBuy
	 * @return
	 */
	public static List<OrderInfo> orderCheck(boolean isBuy) {

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
		try {
			// logger.debug("Begin Send...");
			CloseableHttpResponse response = GFHttpClient.httpPost(url, headers, nvps);
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					String body = EntityUtils.toString(entity);
					JSONObject json = JSONObject.parseObject(body);
					if (json == null) {
						return null;
					}
					int notDeal = Integer.parseInt(json.getString("total"));
					if (notDeal > 0) {
						JSONArray dataArray = json.getJSONArray("data");
						for (int i = 0; i < dataArray.size(); i++) {
							JSONObject data = dataArray.getJSONObject(i);
							OrderInfo info = new OrderInfo();
							info.setProdCode(data.getString("prod_code"));
							info.setEntrustNo(data.getString("entrust_no"));
							info.setBuy(isBuy);
							info.setOrderPrice(data.getString("entrust_price"));
							info.setOrderNum(data.getFloat("entrust_amount").intValue());
							orderInfos.add(info);
						}
					}
					return orderInfos;
				}
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 撤销订单
	 * 
	 * @param
	 * @return
	 */
	public static boolean cancelOrder(String entrustNo) {

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

		try {
			// logger.debug("Begin Send...");
			CloseableHttpResponse response = GFHttpClient.httpPost(url, headers, nvps);
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					String body = EntityUtils.toString(entity);
					JSONObject json = JSONObject.parseObject(body);
					if (json == null) {
						return false;
					}
					return json.getBoolean("success");
				}
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean priceCheck(ProdPrice longProd, ProdPrice shortProd, boolean isBuy, BigDecimal price) {

		if (isBuy) {
			if (price.compareTo(bd1_999) == 0 && longProd.getSaleAmount1() >= 5 && shortProd.getSaleAmount1() >= 5) {
				if (longProd.getSaleAmount1() >= shortProd.getSaleAmount1()) {
					if ((longProd.getSaleAmount1() / shortProd.getSaleAmount1()) <= 10) {
						return true;
					}
				} else {
					if ((shortProd.getSaleAmount1() / longProd.getSaleAmount1()) <= 10) {
						return true;
					}
				}
			}
		} else {
			if (price.compareTo(bd2_001) == 0 && longProd.getBuyAmount1() >= 5 && shortProd.getBuyAmount1() >= 5) {
				if (longProd.getBuyAmount1() >= shortProd.getBuyAmount1()) {
					if ((longProd.getBuyAmount1() / shortProd.getBuyAmount1()) <= 10) {
						return true;
					}
				} else {
					if ((shortProd.getBuyAmount1() / longProd.getBuyAmount1()) <= 10) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void findArbitrage(ProdPrice longProd, ProdPrice shortProd) {

		BigDecimal bdLongSell = new BigDecimal(longProd.getSalePrice1());
		BigDecimal bdShortSell = new BigDecimal(shortProd.getSalePrice1());
		BigDecimal buyArbi = bdLongSell.add(bdShortSell);

		if (buyArbi.compareTo(bd1_998) <= 0 || priceCheck(longProd, shortProd, true, buyArbi)) {
			doubleOrder(longProd, shortProd, "1");
			Date now = new Date();
			System.out.println(CommUtils.format.format(now) + " [" + longProd.getProdCode() + "]Arbi!!!===Long:"
					+ longProd.getSalePrice1() + "[" + longProd.getSaleAmount1() + "], Short:"
					+ shortProd.getSalePrice1() + "[" + shortProd.getSaleAmount1() + "], buyArbi=" + buyArbi);
			CommUtils.sleep(5000);
			List<OrderInfo> infos = orderCheck(true);
			while (infos != null && infos.size() > 0) {
				System.out.println("未成交订单数量：" + infos.size());
				boolean findPrice = true;
				for (OrderInfo info : infos) {
					boolean isCancel = cancelOrder(info.getEntrustNo());
					if (isCancel) {
						ProdPrice prodPrice = getProdPrice(info.getProdCode());
						if (prodPrice == null) {
							findPrice = false;
							break;
						}
						if (info.isBuy()) {
							info.setOrderPrice(prodPrice.getSalePrice1());
						} else {
							info.setOrderPrice(prodPrice.getBuyPrice1());
						}
						singleOrder(info);
					}
				}
				if (!findPrice) {
					break;
				}
				CommUtils.sleep(5000);
				infos = orderCheck(true);
				System.out.println("未成交订单数量：" + infos.size());
			}
			return;
		}

		BigDecimal bdLongBuy = new BigDecimal(longProd.getBuyPrice1());
		BigDecimal bdShortBuy = new BigDecimal(shortProd.getBuyPrice1());
		BigDecimal sellArbi = bdLongBuy.add(bdShortBuy);

		if (sellArbi.compareTo(bd2_002) >= 0 || priceCheck(longProd, shortProd, false, sellArbi)) {
			doubleOrder(longProd, shortProd, "2");
			Date now = new Date();
			System.out.println(CommUtils.format.format(now) + " [" + longProd.getProdCode() + "]Arbi!!!===Long:"
					+ longProd.getBuyPrice1() + "[" + longProd.getBuyAmount1() + "], Short:" + shortProd.getBuyPrice1()
					+ "[" + shortProd.getBuyAmount1() + "], sellArbi=" + sellArbi);
			CommUtils.sleep(5000);
			List<OrderInfo> infos = orderCheck(false);
			while (infos != null && infos.size() > 0) {
				System.out.println("未成交订单数量：" + infos.size());
				boolean findPrice = true;
				for (OrderInfo info : infos) {
					boolean isCancel = cancelOrder(info.getEntrustNo());
					if (isCancel) {
						ProdPrice prodPrice = getProdPrice(info.getProdCode());
						if (prodPrice == null) {
							findPrice = false;
							break;
						}
						if (info.isBuy()) {
							info.setOrderPrice(prodPrice.getSalePrice1());
						} else {
							info.setOrderPrice(prodPrice.getBuyPrice1());
						}
						singleOrder(info);
					}
				}
				if (!findPrice) {
					break;
				}
				CommUtils.sleep(5000);
				infos = orderCheck(false);
				System.out.println("未成交订单数量：" + infos.size());
			}
			return;
		}

		Date now = new Date();
		System.out.println(
				CommUtils.format.format(now) + " [" + longProd.getProdCode() + "] " + sellArbi + " - " + buyArbi);

	}

}
