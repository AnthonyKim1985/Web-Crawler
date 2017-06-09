package com.anthonykim.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Created by hyuk0 on 2017-06-09.
 */

public class CrawlerDriver {
    private static final String packageName = CrawlerDriver.class.getPackage().getName();
    private static final Logger logger = Logger.getLogger(packageName);

    static {
        //
        // TODO: Secure Socket Layer Certification
        //
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            final SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        final String BASE_URL = "https://search.naver.com/search.naver?where=post&sm=tab_jum&ie=utf8&query=";

        try (Scanner scanner = new Scanner(System.in)) {
            logger.info("크롤링 할 키워드를 입력하고 엔터키를 눌러 주세요.");
            final String keyword = scanner.nextLine();

            Document document = Jsoup.connect(String.format("%s%s", BASE_URL, keyword)).get();
            Elements elements = document.select("a.sh_blog_title._sp_each_url._sp_each_title");

            for (Element element : elements)
                System.out.println(String.format("블로그 타이틀: %s", element.attr("title")));
        }
    }
}