package me.zhengjie.modules.system.service;

import java.net.MalformedURLException;

public interface AmzWebCrawlerService {
    String getPageAsXml(String productUrl, String Ip, Integer port, Integer asinId, String site) throws MalformedURLException;

}
