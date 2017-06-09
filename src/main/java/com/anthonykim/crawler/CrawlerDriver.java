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
 * Created by Anthony Jinhyuk Kim on 2017-06-09.
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
            logger.info("Enter the keyword below to crawling.");
            final String keyword = scanner.nextLine();

            Document document = Jsoup.connect(String.format("%s%s", BASE_URL, keyword)).get();
            Elements elements = document.select("a.sh_blog_title._sp_each_url._sp_each_title");

            for (Element element : elements)
                System.out.println(String.format("Blog Title: %s", element.attr("title")));
        }
    }
}