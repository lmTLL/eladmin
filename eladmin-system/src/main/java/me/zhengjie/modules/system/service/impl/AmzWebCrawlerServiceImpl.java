package me.zhengjie.modules.system.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.micrometer.core.instrument.util.StringUtils;
import me.zhengjie.modules.system.domain.*;
import me.zhengjie.modules.system.rest.WechatController;
import me.zhengjie.modules.system.service.AmzWebCrawlerService;
import me.zhengjie.modules.system.service.AsinInfoService;
import me.zhengjie.modules.system.service.FollowDetailsService;
import me.zhengjie.modules.system.service.FollowOthersService;
import me.zhengjie.modules.system.service.dto.AsinInfoDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

//import org.springframework.data.redis.core.StringRedisTemplate;
@Service
public class AmzWebCrawlerServiceImpl implements AmzWebCrawlerService {
    //@Autowired
    //private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private AsinInfoService asinInfoService;
    @Autowired
    private FollowDetailsService followDetailsService;
    @Autowired
    private FollowOthersService followOthersService;

    private static final Logger log = LoggerFactory.getLogger("adminLogger");
    public static String  content_openid="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
    private static List<String> list=new ArrayList<>();
    public void getAmzProductValueTask(List<AsinInfo> asinInfos) throws IOException, InterruptedException {
        log.info("--------开始爬取Amz数据---------");
        Map<String,Object> map=new HashMap<>();
        map.put("US","https://www.amazon.com/gp/offer-listing/");
        map.put("UK","https://www.amazon.co.uk/gp/offer-listing/");
        map.put("DE","https://www.amazon.de/gp/offer-listing/");
        map.put("JP","https://www.amazon.co.jp/gp/offer-listing/");
        map.put("FR","https://www.amazon.fr/gp/offer-listing/");
        map.put("ES","https://www.amazon.es/gp/offer-listing/");
        map.put("IT","https://www.amazon.it/gp/offer-listing/");
        map.put("AU","https://www.amazon.com.au/gp/offer-listing/");
        map.put("CA","https://www.amazon.ca/gp/offer-listing/");
        map.put("MX","https://www.amazon.com.mx/gp/offer-listing/");
        map.put("IN","https://www.amazon.in/gp/offer-listing/");
        int num =asinInfos.size();
        for (AsinInfo asinInfo : asinInfos) {
            String productUrl = map.get(asinInfo.getSite())+asinInfo.getAsin();
            log.info(productUrl);
            log.info((num--)+"");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (asinInfo.getSite().equals("JP.")){
                        asinInfoService.updateCount(asinInfo.getCount()-1,asinInfo.getId());
                        System.out.println("延时一次");
                    }else {
                        asinInfoService.updateCount(asinInfo.getStartCount(),asinInfo.getId());
                        try {
                            //监控条件：asin启动跟卖监控
                            if (asinInfo.getFollowListen().equals("Yes")){
                                getAmzValueByUrl(productUrl, asinInfo.getAsin(), asinInfo.getExcludeShop(), "127.0.0.1", 24000, asinInfo.getSite(),asinInfo.getId());
                            }//监控条件：asin启动五点，标题，价格监控
                            if (asinInfo.getFivepointListen().equals("Yes")|| asinInfo.getTitleListen().equals("Yes")||asinInfo.getPriceListen().equals("Yes")){
                                String url = productUrl.replaceFirst("/gp/offer-listing","/dp");
                                getAmzValueByUrl1(url, asinInfo.getAsin(), asinInfo.getExcludeShop(), "127.0.0.1", 24000, asinInfo.getSite(),asinInfo.getId());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            Thread.currentThread().sleep(3000);
        }
        for (String s : list) {
            log.info("失败URL："+s);
        }
    }
    private static int mathRandom(int length) {
        return Long.valueOf(Math.round(Math.random() * length)).intValue();

    }
    @Override
    public String getPageAsXml(String productUrl, String Ip, Integer port,Integer asinId,String site) throws MalformedURLException {

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        URL url = new URL(productUrl);
        String host = url.getHost();
        HtmlPage  page = null;
        try {
            WebRequest request = new WebRequest(new URL(productUrl));
            List<String> agents = userAgents();
            int randomNumer = mathRandom(agents.size() - 1);
            String agent = agents.get(randomNumer);
            request.setAdditionalHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
            request.setAdditionalHeader("accept-encoding", "accept-encoding");
            request.setAdditionalHeader("accept-language", "q=0.8,en-US;q=0.5,en;q="+Math.random());
            request.setAdditionalHeader("cache-control", "no-cache");
            request.setAdditionalHeader("user-agent", agent);
            request.setAdditionalHeader("Origin", host);
            request.setAdditionalHeader("upgrade-insecure-requests", "1");
            request.setAdditionalHeader("referer", productUrl);
            request.setAdditionalHeader("Cookie",siteCookie(site) );
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getCookieManager().setCookiesEnabled(true);//设置cookie是否可用
            webClient.setAjaxController(new NicelyResynchronizingAjaxController()); // ajax设置
            webClient.getOptions().setPrintContentOnFailingStatusCode(false);
            webClient.getOptions().setJavaScriptEnabled(false);              // 启用JS解释器，默认为true
            webClient.getOptions().setCssEnabled(false);                    // 禁用css支持
            webClient.getOptions().setThrowExceptionOnScriptError(false);   // js运行错误时，是否抛出异常
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setRedirectEnabled(true); //启动客户端重定向
            webClient.getOptions().setTimeout(100000);// 设置连接超时时间
            webClient.getOptions().setWebSocketMaxBinaryMessageBufferSize(8192);
            webClient.getOptions().setWebSocketMaxBinaryMessageSize(8192);
            ProxyConfig proxyConfig = webClient.getOptions().getProxyConfig();
            proxyConfig.setProxyHost(Ip);
            proxyConfig.setProxyPort(port);
            try {
                page= webClient.getPage(request);
            }catch (SocketTimeoutException e){
                log.info("读取超时，重新读取");
                Thread.currentThread().sleep(10000);
                try {
                    page= webClient.getPage(request);
                }catch (SocketTimeoutException e1){
                    log.info("尝试多次，无效");
                    list.add(productUrl);
                }
            }
            webClient.waitForBackgroundJavaScript(30 * 1000);               // 等待js后台执行30秒
            int statusCode = page.getWebResponse().getStatusCode();
            if (statusCode == 404 || statusCode == 503 || statusCode == 502) {
                if (statusCode != 404) {
                    //503 502网关错误的情况重试一次
                    page = webClient.getPage(request);
                }
                log.error("Status Code " + statusCode + ",URL " + productUrl);
                if (statusCode == 404){
                    AsinInfoDTO byAsin = asinInfoService.findById(asinId);
                    Token token = asinInfoService.getToken();
                    String accessToken = token.getToken();
                    String messageUrl=content_openid.replace("ACCESS_TOKEN", accessToken);
                    TestMessage testMessage=new TestMessage();
                    testMessage.setMsgtype("text");
                    testMessage.setTouser(byAsin.getOpenId());
                    Map<String,Object> map=new HashMap<>();
                    map.put("content","该asin："+byAsin.getAsin()+"找不到页面，请登录一点点助手修改！");
                    testMessage.setText(map);
                    String jsonTestMessage = JSONObject.toJSONString(testMessage);
                    Map<String, Object> map1 = WechatController.httpClientPost(messageUrl, jsonTestMessage);
                }
            }
        } catch (ClassCastException | InterruptedException e) {
            e.printStackTrace();
            log.error("ClassCastException" + productUrl);
            //TODO 记录哪个asin失败
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FailingHttpStatusCodeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webClient.getCurrentWindow().getJobManager().removeAllJobs();
            System.gc();
        }
        if (page == null) {
            return null;
        }
        String pageAsXml = page.asXml();
        return pageAsXml;
    }
    private  void getAmzValueByUrl(String productUrl, String asin, String station,String Ip,Integer port,String site,Integer asinId) throws IOException {
        String pageAsXml = getPageAsXml(productUrl,Ip,port,asinId,site);
        if (pageAsXml == null || StringUtils.isBlank(pageAsXml)) {
            log.error("-------没有获取到url数据-------");
            return;
        }
        log.info("--------获取到html数据-------长度:" + pageAsXml.length());
        try {
            Document doc = Jsoup.parse(pageAsXml);
            String title = doc.title();
            if (("Amazon.com Page Not Found").equals(title)
                    || ("Seite wurde nicht gefunden").equals(title)
                    || ("Page introuvable").equals(title)
                    || ("Impossibile trovare la pagina").equals(title)
                    || ("Documento no encontrado").equals(title)) {
                //TODO 日志
                log.error("-------Page Not Found，跳过-------");
                return;
            }
                Elements select = doc.getElementsByClass("a-row a-spacing-mini olpOffer");
                List<FollowDetails> list=new ArrayList<>();
                for (Element element : select) {
                    FollowDetails followDetails=new FollowDetails();
                    followDetails.setAsin(asin);
                    followDetails.setSite(site);
                    log.info("成功！");
                    Elements select1 = element.getElementsByClass("a-spacing-none olpSellerName");
                    System.out.println(select1.select("a").text());
                    Elements sellerClass = element.getElementsByClass("a-size-medium a-text-bold");
                    Elements a1 = sellerClass.select("a");
                    if ("".equals(select1.select("a").text())) {
                        //log.info("没有seller！");
                        followDetails.setSeller("");
                    } else {
                        System.out.println("seller:" + a1.toString().substring(a1.toString().lastIndexOf("=") + 1, a1.toString().lastIndexOf("\"")));
                        followDetails.setSeller(a1.toString().substring(a1.toString().lastIndexOf("=") + 1, a1.toString().lastIndexOf("\"")));
                    }
                    if ("".equals(select1.select("a").text())) {
                        //log.info("店铺：Amazon_WareHouse");
                        followDetails.setShopName("Amazon_WareHouse");
                    } else {
                        //log.info("店铺：" + select1.select("a").text());
                        followDetails.setShopName(select1.select("a").text());
                    }
                    //log.info("价格：" + element.getElementsByClass("a-size-large a-color-price olpOfferPrice a-text-bold").text());
                    followDetails.setFollowPrice(element.getElementsByClass("a-size-large a-color-price olpOfferPrice a-text-bold").text());
                    Elements select2 = element.getElementsByClass("a-popover-trigger a-declarative olpFbaPopoverTrigger");
                    String followType = null;
                    if ("".equals(select2.text())) {
                        //log.info("跟卖类型：FBM");
                        followType = "FBM";
                    } else {
                        if ("".equals(select1.select("a").text())) {
                            //log.info("跟卖类型：VC");
                            followType = "VC";
                        } else {
                            //log.info("跟卖类型：FBA");
                            followType = "FBA";
                        }
                    }
                    followDetails.setFollowType(followType);
                    followDetails.setStartDate(new Timestamp(System.currentTimeMillis()));
                    if(!"Amazon_WareHouse".equals(followDetails.getShopName())){
                        list.add(followDetails);
                    }
                    //list.add(followDetails);
                }
            if (select.size()>0){
                List<FollowDetails> byAsin1 = followDetailsService.getByAsin(asin);
                for (FollowDetails followDetails : list) {
                    Boolean as=true;
                    for (FollowDetails details : byAsin1) {
                        if (followDetails.getShopName().equals(details.getShopName())|| followDetails.getShopName().equals("Amazon_WareHouse")){
                            //followDetailsDao.updateDate(new Timestamp(new Date().getTime()),details.getId());
                            as=false;
                        }
                    }
                    String[] split = station.split(",");
                    for (String s : split) {
                        if (s.equals(followDetails.getShopName())){
                            System.out.println("排除该店铺："+s);
                            as=false;
                        }
                    }
                    log.info("shopName:"+followDetails.getShopName());
                    log.info("as:"+as);
                    if (as){
                        AsinInfoDTO byAsin = asinInfoService.findById(asinId);
                        Date date = new Date();
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Token token = asinInfoService.getToken();
                        String accessToken = token.getToken();
                        JSONObject json=new JSONObject();
                        JSONObject text=new JSONObject();
                        JSONObject keyword1=new JSONObject();
                        JSONObject keyword2=new JSONObject();
                        JSONObject keyword3=new JSONObject();
                        JSONObject keyword4=new JSONObject();
                        JSONObject first=new JSONObject();
                        JSONObject remark=new JSONObject();
                        json.put("touser",byAsin.getOpenId());
                        json.put("template_id","lGhu5QnatCrxpNOCfC2-lLovCnk2Y4_KD4zS3uUP3yM");
                        String replace = followDetails.getShopName().replace(" ", "");
                        String replace1 = replace.replace("&", "");
                        json.put("url", "");
                        first.put("value","您好，您有新的跟卖，请联系我们处理(13823783145)");
                        String titles="";
                        if (byAsin.getTitle()!=null){
                            if (byAsin.getTitle().length()>10){
                                titles=byAsin.getTitle().substring(0,10)+"...";
                            }else {
                                titles=byAsin.getTitle();
                            }
                        }
                        keyword1.put("value",titles);
                        keyword2.put("value","一点点助手" );
                        keyword3.put("value",simpleDateFormat.format(date));
                        keyword4.put("value","无" );
                        remark.put("value", "跟卖asin："+followDetails.getAsin()+"\n站点:"+site+"\n跟卖店铺名："+followDetails.getShopName()
                                +"\n跟卖售价："+followDetails.getFollowPrice()+"\n跟卖类型："+followDetails.getFollowType()+"\n点击生成Excel数据");
                        text.put("keyword1", keyword1);
                        text.put("keyword2", keyword2);
                        text.put("keyword3", keyword3);
                        text.put("keyword4", keyword4);
                        text.put("first", first);
                        text.put("remark",remark);
                        json.put("data", text);
                        log.info("开始发送信息");
                        Map<String, Object> map1 = WechatController.httpClientPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessToken, json.toJSONString());
                        System.out.println( map1.get("errcode")+":"+asinId);
                        if ("43004.0".equals(map1.get("errcode"))){
                            asinInfoService.delete(byAsin.getId());
                        }
                        if (!"VC".equals(followDetails.getFollowType())){
                            followDetailsService.create(followDetails);
                        }
                        as=true;
                    }
                }
                for (FollowDetails followDetails : byAsin1) {
                    Boolean as=true;
                    for (FollowDetails details : list) {
                        if (followDetails.getShopName().equals(details.getShopName())||"Amazon_WareHouse".equals(followDetails.getShopName())){
                            as=false;
                        }
                    }
                    String[] split = station.split(",");
                    for (String s : split) {
                        if (s.equals(followDetails.getShopName())){
                            System.out.println("排除该店铺："+s);
                            as=false;
                        }
                    }
                    if (as){
                        AsinInfoDTO byAsin = asinInfoService.findById(asinId);
                        Date date = new Date();
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Token token = asinInfoService.getToken();
                        String accessToken = token.getToken();
                        JSONObject json=new JSONObject();
                        JSONObject text=new JSONObject();
                        JSONObject keyword1=new JSONObject();
                        JSONObject keyword2=new JSONObject();
                        JSONObject keyword3=new JSONObject();
                        JSONObject keyword4=new JSONObject();
                        JSONObject first=new JSONObject();
                        JSONObject remark=new JSONObject();
                        json.put("touser",byAsin.getOpenId());
                        json.put("template_id","lGhu5QnatCrxpNOCfC2-lLovCnk2Y4_KD4zS3uUP3yM");
                        String replace = followDetails.getShopName().replace(" ", "");
                        json.put("url", "");
                        first.put("value","恭喜您，跟卖您的人已经走了~");
                        String titles="";
                        if (byAsin.getTitle()!=null){
                            if (byAsin.getTitle().length()>10){
                                titles=byAsin.getTitle().substring(0,10)+"...";
                            }else {
                                titles=byAsin.getTitle();
                            }
                        }
                        keyword1.put("value",titles);
                        keyword2.put("value","一点点助手" );
                        keyword3.put("value",simpleDateFormat.format(date));
                        keyword4.put("value","无" );
                        remark.put("value", "跟卖asin："+followDetails.getAsin()+"\n站点:"+site+"\n跟卖店铺名："+followDetails.getShopName()
                                +"\n跟卖售价："+followDetails.getFollowPrice()+"\n跟卖类型："+followDetails.getFollowType());
                        text.put("keyword1", keyword1);
                        text.put("keyword2", keyword2);
                        text.put("keyword3", keyword3);
                        text.put("keyword4", keyword4);
                        text.put("first", first);
                        text.put("remark",remark);
                        json.put("data", text);
                        log.info("开始发送信息");
                        Map<String, Object> map1 = WechatController.httpClientPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessToken, json.toJSONString());
                        System.out.println( map1.get("errcode")+":"+asinId);
                        if ("43004.0".equals(map1.get("errcode"))){
                             asinInfoService.delete(asinId);
                        }
                        followDetailsService.delete(followDetails.getId());
                        as=true;
                        //followDetailsDao.save(followDetails);
                    }
                }
            }else {
                System.out.println("select<0:"+productUrl);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  void getAmzValueByUrl1(String productUrl, String asin, String station,String Ip,Integer port,String site,Integer asinId) throws IOException {
        String pageAsXml = getPageAsXml(productUrl,Ip,port,asinId,site);
        if (pageAsXml == null || StringUtils.isBlank(pageAsXml)) {
            log.error("-------没有获取到url数据-------");
            return;
        }
        log.info("--------获取到html数据-------长度:" + pageAsXml.length());
        try {
            Document doc = Jsoup.parse(pageAsXml);
            String title = doc.title();
            if (("Amazon.com Page Not Found").equals(title)
                    || ("Seite wurde nicht gefunden").equals(title)
                    || ("Page introuvable").equals(title)
                    || ("Impossibile trovare la pagina").equals(title)
                    || ("Documento no encontrado").equals(title)) {
                //TODO 日志
                log.error("-------Page Not Found，跳过-------");
                return;
            }
            FollowOthers followOtherss = new FollowOthers();
            followOtherss.setAsin(asin);
            try {
                Element titleStr = doc.getElementById("productTitle");
                followOtherss.setTitle(titleStr.html());
            } catch (Exception e) {
                System.out.println("找不到标题------asin为：" + asin);
            }
            try {
                Element priceStr = doc.getElementById("priceblock_ourprice");
                followOtherss.setPrice(priceStr.html().replaceAll("&nbsp;"," "));
            } catch (Exception e) {
                System.out.println("找不到价格------asin为：" + asin);
            }
            String pointSelector = "#feature-bullets > ul > li:nth-child(%d) > span";
            StringBuffer five = new StringBuffer();
            try {
                for (int j=1;j<=10;j++){//#fbExpandableSectionContent > ul > li:nth-child(1) > span
                    String ss =doc.selectFirst(pointSelector.replace("%d",j+"")).html();
                    if(ss!=null){
                        five.append(" • "+ss);
                        five.append("@!@");
                    }
                }
            }catch (Exception e){
                System.out.println("五点获取");
            }
            five.delete(five.length()-3,five.length()-1);
            five.deleteCharAt(five.length() - 1);
            System.out.println("!@!@!@!@!@!@"+five.toString());
            followOtherss.setFivePoints(five.toString());
            Date date = new Date();
            followOtherss.setStartDate(new Timestamp(System.currentTimeMillis()));

            List<FollowOthers> byAsin2 = followOthersService.getByAsin(asin);
            System.out.println(byAsin2);
            Boolean ass1=true;
            Boolean ass2=true;
            for (FollowOthers others : byAsin2) {
                if( followOtherss.getPrice().equals(others.getPrice()) && followOtherss.getTitle().equals(others.getTitle())){
                    System.out.println("标题，价格两个都相同");
                    ass1=false;
                }
                //将五点切割匹配
                String[] webstr = followOtherss.getFivePoints().split("@!@");
                String[] sqlstr = others.getFivePoints().split("@!@");
                List<String> strList = Arrays.asList(sqlstr);
                List<String> list = new ArrayList<>(strList);
                if(webstr.length==sqlstr.length){//&nbsp;
                    for (int j = 0 ;j <= webstr.length-1 ;j++){
                        if(list.contains(webstr[j])){
                            try {
                                list.remove(webstr[j]);
                            }catch (Exception e){
                                System.out.println("删除错误。");
                                System.out.println(e);
                            }
                        }
                    }
                    if(list.size() == 0) {
                        System.out.println("五点相同");
                        ass2=false;
                    }
                }
            }
            if(ass1||ass2){
                System.out.println("======================updating=========================");
                try {
                    followOthersService.create(followOtherss);
                }catch (Exception e){
                    System.out.println("======================un_update=========================");
                    System.out.println(e);
                }
            }

        }catch (Exception e){

        }
    }


    private static List<String> userAgents()
    {
        List<String> agents = new ArrayList<>();
        agents.add(
                "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; QQBrowser/7.0.3698.400)");
        agents.add("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)");
        agents.add(
                "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 SE 2.X MetaSr 1.0");
        agents.add(
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.133 Safari/534.16");
        agents.add(
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
        agents.add("Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
        agents.add("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
        agents.add(
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER");
        agents.add(
                "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; LBBROWSER) ");
        agents.add("Mozilla/5.0 (Windows NT 5.1; U; en; rv:1.8.1) Gecko/20061208 Firefox/2.0.0 Opera 9.50");
        agents.add("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; en) Opera 9.50");
        agents.add(
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2");
        agents.add(
                "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1");
        agents.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;");
        agents.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)");
        agents.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)");
        agents.add("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
        agents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1");
        agents.add("Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1");
        agents.add("Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11");
        agents.add(
                "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        agents.add(
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        agents.add("Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11");
        agents.add(
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
        agents.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Maxthon 2.0)");
        agents.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)");
        agents.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
        agents.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; The World)");
        agents.add(
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)");
        agents.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)");
        agents.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Avant Browser)");
        agents.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
        agents.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
        return agents;
    }



    private static String siteCookie(String site) {
        Map<String,String> map=new HashMap<>();
        String[] strings={"JP","DE","UK","FR","ES","IT","CA"};
        map.put("US","session-id-time=2082787201l; x-wl-uid=1MjxFp2+iZG7kzeCvi7ImTjVvvS5lyFwkTV25i7PzGSJkefHpAtUyCfjStqBOUoY1Zbz+DAz1v5FHPEcV22kwEeqnR0OhYNv4kS/VGrXYd3SSZQzsxHouxHJqzeYlb92L+eUMsIGUpE4=; amznacsleftnav-bd45b3e8-773f-4d8d-be99-105a8f967e19=1; ubid-main=134-1563934-9331205; session-id=132-8115169-7067158; at-main=Atza|IwEBILq8Q6CgEi_A1RSGRVSdjSyrbvRlEiUo7giVXdxzZ8nD9_Pr1HoovT-sB--iiXKVIq7X3VvPTyOWHvUHBMH2QClWIrgGidYnwiQN9eQGwUDbk4E5MqNQuBW973HmhntirX6RQXH-DV-ekehzXIGhTbuX4rTRH_twSuaunArD8EGG3T-kdwpJRhYd2XpUmpyjsC8GGlWoOTlbZSh8s56ADY1QKJDYCfU8Wv6k9aFLpgwt3W6AGwjiLHlcrQJT9JXyazy7OJASQkhWrbUpm5r6hA9hDcCzT8dn7OFvP6fawgazVocq3A4nWxUGjBHPWSvwQa6byZcsCqYLluMvhFcG_3VKXm0G9TxX1Kl4FPmEY0GaADA64jtC1G0E8PwwAUgEplmLHh3lRZIe_rpjjeu_-4KA; sess-at-main=\"bEdYmqnWuaN7Ucf5pPjiF6FoEJzS8Gvuwol8mO4KCjg=\"; sst-main=Sst1|PQF7tRDxXgSnmHZZnpk86VmmC0rBQUOGnjDP2sjTwSFV2dAgRkLVsyZAlF81569pH2DkrONSY1j0GLFVQBfAkgah3rOWvqeKhxaicPrtQUKWaa0SR0bpW8xj1jGjpr91tyLhhFc-i4VBU7j6eHjhW8A51mWec41NIn0_xC6Eu-WQv7YLkgTIZqW0jHJKRnB5BZ0SsRJjTs15k1Jem28GKhfjORYL_ElXEvbcvyJ85Ob6zaX0EUWWd93wrnQPdSa4GZdWcVARNZMX-amFsYlCYJlJRD2NFfBwEXmh2W7Wwlsl7UQGFy10llmjFBZX8tuIslPsWdVLIW1OzMcZGXDZsU2rWg; lc-main=en_US; i18n-prefs=USD; x-main=\"kgE7pRfmoZ8qhJYLV@X7RRWM?9lqJJbyrQIZqfl0G5GA7L?Rn8tAEpK6uYnv@63w\"; s_nr=1566469491509-New; s_vnum=1998469491510%26vn%3D1; s_dslv=1566469491512; session-token=lvteMbPpWBlNS2E4Bgmuqkg0R1pj7/HMrdS6z0+eK1w/Hyq8THIaht1ozgejzuvAGoBIXwF1K0A8i8wVbF3yOXPvZllFNUp2Pi0+XxKy3bvrYYe7D2jdSPf20AayRYKKFViN7QwngD2pVwa4qjVo8+Ugss50cnBR7rVXS6GaX2lXf2jNoJ1hmUJ3G7sOD8jkH2Fs439+k7d8tOD+z1gLLWtiQjP0hUlZSiLWAbM9oU267ywCclZ8Bt2tMX703Di18g01jvFYIWzw2wzgSuO5+g==; csm-hit=tb:2EYZ8G4D4ADHVEC5HWQH+s-2EYZ8G4D4ADHVEC5HWQH|1566547579171&t:1566547579172&adb:adblk_no");
        map.put("JP","session-id=358-0826627-8894910; i18n-prefs=JPY; ubid-acbjp=358-6982471-5299054; x-wl-uid=17M8/LOQoaX7S4YBAMEf/Zv1Es7D8F9JrQo8/S8cpDy/0Z3FsbJkQ1DS7SxLUAh4lL1xXNj838u4=; session-token=PKajR1Ox9bXunxAM4dYCNHYAKFzNWqODAMNGK2Ss2fNTyc+DjlP/n1RkkqZTziQwuiJljIabU0kdx5ZduSyIhGHM/BTWeYK+Zf9IscGI6j0N8pDtWlWw/fhASVs7gJ0jZ4aGX8sLUFvUi1drFWwfwY/ftc08BnQYlS22xdH0gFTUHfdO5jlNZcj/Fc14CW5F; session-id-time=2082726001l; csm-hit=tb:SP4WXKYC4NAD7QJPF9G3+s-SP4WXKYC4NAD7QJPF9G3|1566552410303&t:1566552410313&adb:adblk_no");
        map.put("DE","x-wl-uid=1BtiPv0bEm6iyG40XbGpxHdDDF1bJrKica4J23BtBMuVxDhIFIFwx27TMdY5ttNvLbdI49ypjSxw=; session-id=258-7798837-4228757; ubid-acbde=262-2727595-8583111; session-token=\"VvjogsZ5cwKPjZcPTD+KcxtWIXK2qsih9Ba4yBXtqV2/UrJJaQbfSf9BdERG70x6ALubsswmhP26dMorikB29iRFbBZ0bj1MnP6TVpHf03gtnVf8fuh2HpwQN57ki66yA5bMKL/DOWeiGpCH/KZ0CvmmUfeAUp0LFHh/d0IO6VcR/0iPLz9Ku26HIGMBLvzJflZGHGA1S7vAmKrVQAjNHsJCljLO8qor3lrmmYrZqWZP021kbsXYNih/3FOQhfiagWqC/tBuFZc=\"; i18n-prefs=EUR; session-id-time=2082754801l; csm-hit=tb:s-BCPFXDDCJ9WFSXT6ZM3N|1567129564543&t:1567129565280&adb:adblk_no");
        map.put("UK","x-wl-uid=16eqQqEw4ltvCdhkuJ/AZk75ee4FAqJJJeUc9OuQvI5F53+g3Cf3sb3oXQdPCJfEXlCIR6mz1JHo=; session-id=261-3951163-2342938; ubid-acbuk=260-6463418-9975204; session-token=\"nH7pfSL57N94juNf+LIfeZ/IvwezA62Qt78d6SmI5vyBgXADkv8Gs2fmZVDRVySdQDiQ5EvM6izIf4vnpsi2Y8vIiauP+Quu7YGFHCM3GtsoQN7/btPai/KtGapLDhkw7PlUvhXgqbph7CbEw/dwyw0dnuV06fIOJgMh6TgfRXplWOvDHdUlGhRbvgFmzRPOKVaA5lumFPq3c8eSbztcgSo+q2+4lNZpeVb08O5tXRGKuHt9N4rBiZH4PJP1Sh1/+xL48ck9mxg=\"; i18n-prefs=GBP; csm-hit=tb:EFEQY86CS0A8AH8P3FPG+s-FD56SP5AXNAVW7HGJFJX|1567129751571&t:1567129751571&adb:adblk_no; session-id-time=2082758401l");
        map.put("FR","x-wl-uid=1swpQA2O665IghktBrAdCZO9pETMsxxjMqNwWj+DBABWsGXexuir5Xe9nRuSvc3UqNgyxfYmZvsk=; session-id=261-1998890-6981547; ubid-acbfr=262-8247920-9994003; session-token=zG5sFvn7snov+uSYRf3unZWmRepHC7zLBJeKQnzBVMc761mWsQteToL6lsp7mZFRGqBfF5ZExYh5k49Lof79MbtWHelcgMYUIfYXXB5hb1/82VA4x+NiGRddfwhpN9Cwv7r9HC+OMbClXa6MU6EEmBAjdJ1ISDnv+FS2GxeOoqkOfxjJy9n+3gNvknsDhF+GfQ9oADeV5DAUpYABRS26LAXCgx4j091pciUFQpcZvmMxgA9udRtZa0TxuvSE7noR; i18n-prefs=EUR; csm-hit=tb:78BWB4N1VGH33W6XTRDF+s-WARJNADW7NSN4NHT7V5X|1567129924924&t:1567129924924&adb:adblk_no; session-id-time=2082754801l");
        map.put("ES","x-wl-uid=1mp0XKm9xcGjGvXer9p89joBXF8WquwtsjJIZREdLfdkS2X9Fu60khHGbnnxUrq5M73y3xfb32oM=; session-id=258-8150425-8732519; ubid-acbes=257-3404964-0170315; session-token=\"SNhiqR5LBDOYBC7nIMrUHpiPBzvUo1HqivrLbAhTfyfLOyVcmEt78FdI1Dv9pAmhoppyUu9jw8I+5xvRPd3xtqZiVOXrIsyHaS4PO/6QKDNxZhbPSKjOkwco3BTm36cr6GBCuQuB1xalR3+3ggiKmi0BCq5p1DPkldXEEVdze8nASciFB1pqaux6C/OJxpw6fR3CjTPgwVXbAO4LCTtUro/sAM2rOA2s8bo8EaZI6lkeqRGqfyB6sA==\"; i18n-prefs=EUR; session-id-time=2082758401l; csm-hit=tb:6BFSMFFXBWNX2J6PSGPH+s-55CJMDSF8CCF07A2JHY0|1567130033834&t:1567130033834&adb:adblk_no");
        map.put("IT","x-wl-uid=1sWeFWdyIwicTFaQS8f/klJ6qzyzVWWNfzNL6sw0dnao1MiD1+tBRIhxFOfI+n65PS+EGU7Hle2Q=; session-id=261-7147576-8877716; ubid-acbit=262-1585867-9254856; session-token=\"29v7304frICnJqcnphhzQmlUjK3AhrDQQU8SiLNkF+lWPHhtgqNOOmTaE9L3cIg5MvJny6MmmhQ18hpZZeuzAYT6xrGc3+/JptX9tda+h333yp5sxPjPpPcd6d0XGwBndnGtYpae45xLNlDnPFzs/QfopYBj3NNjSFOmeI2x0/2QYIgXYir+eaYpe5+VT5E6MNME30xjEKPRJFn/FS6jVJTi1KftOZ6ZqphRl1Nq/fl9tJXs5BsgcQ==\"; i18n-prefs=EUR; session-id-time=2082758401l; csm-hit=tb:8RDE4XFJ5D9X6VW6HT2D+s-QHMJ3GMT0HH622EP9DJB|1567130114376&t:1567130114377&adb:adblk_no");
        map.put("CA","session-id=130-9470168-6375937; session-id-time=2082787201l; i18n-prefs=CAD; ubid-acbca=131-8610881-3279254; x-wl-uid=1SasR3JKVz8Ing1hK1nYeaYwxRCrqGIMk8emZi1dfOUvjwn/6bRSxlUcakrzkfOEWbgJuqCsJP50=; session-token=KM5QbIFZxwqTEjYRwx0DooYlq7wf0QoXDJ9QOFj7FkPZ9rO+sKEnN7+Nd/frHMTJTVN0ZDQsaXDjEvrdXkl56D28JRmyVOU294lkir8TrxFE08rQQXPq/n27CcAidlb1ZhHQuWlg1/cYJ+QAW0BZffw4lnMJNWGx4srwucwpWdDSF2KQQIL5+BgSgIHkrCryrcsG7hzhKo4Eih5ScxEBlZbAmMozEJ3ndtuYympUXjN0O7gqK52w8EEQlr73ZEZ9; csm-hit=tb:10B41YP0N97BX0QPXKMV+s-7DZE23V97NMWK5KAZ8RM|1566553801484&t:1566553801484&adb:adblk_no");
        return map.get(site);
    }
}
