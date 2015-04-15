package com.example.akash.fourthestatesample;

import android.content.Context;
import android.util.Log;

import com.firebase.client.Firebase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

/**
 * Created by akash on 10/4/15.
 */
public class Parser {

    private Document doc;

    public synchronized void parseArticle(String sourceName, String category, String url, String key, String pubDateTime) {
        switch (sourceName.toLowerCase()) {
            case "andhra wishesh":
                parseAndhraWishes(sourceName, category, url, key, pubDateTime);
                break;
            case "times of india":
                parseTimesOfIndia(sourceName, category, url, key, pubDateTime);
                break;
            case "bbc news":
                parseBBCNews(sourceName, category, url, key, pubDateTime);
                break;
            case "sify news":
                parseSifyNews(sourceName, category, url, key, pubDateTime);
                break;
            case "live mint news":
                parseLiveMintNews(sourceName, category, url, key, pubDateTime);
                break;
        }
    }

    public void parseAndhraWishes(String sourceName, String category, String url, String key, String pubDateTime) {
        try {
            Log.d("test", "parsing : " + sourceName);
            if (url != null && !url.isEmpty())
                doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element descriptionEle = doc.select("p[style],div.article-content").first();
        Element imageEle = doc.select("center img,div.article-content > center > img").first();
        Element titleEle = doc.select("h1,div.article-content > div > h1").first();
        String imageUrl = null;
        if (imageEle != null) {
            imageUrl = imageEle.absUrl("src");
        } else
            imageUrl = null;
        if (titleEle != null && !titleEle.text().isEmpty() && descriptionEle.text() != null&& !descriptionEle.text().isEmpty()) {
            String parms[] = new String[]
                    {sourceName, category, url, imageUrl, titleEle.text(), descriptionEle.text(), key,pubDateTime};
            postArticleInfo(parms);
        }
    }

    public void postArticleInfo(String[] parms) {
        Firebase ref = new Firebase(BackGroungService.BASE_URL).child("/article_info");
        Firebase ref1 = new Firebase(BackGroungService.BASE_URL).child("/article_urls");
        //  NounsCollector nounsCollector = new NounsCollector();
        Map<String, Object> articleInfo = new HashMap<String, Object>();
        articleInfo.put("source_name", parms[0]);
        articleInfo.put("category", parms[1]);
        articleInfo.put("article_url", parms[2]);
        articleInfo.put("image_url", parms[3]);
        articleInfo.put("title", parms[4]);
//        nounsCollector.parserAction(parms[4]);
//        String[] titleKeywords = nounsCollector.getNouns();
//        articleInfo.put("title_keywords", titleKeywords);
        articleInfo.put("body", parms[5]);
        //   nounsCollector.parserAction(parms[5]);
//        String[] bodyKeywords = nounsCollector.getNouns();
//        articleInfo.put("body_keywords", bodyKeywords);
        articleInfo.put("location", null);
        articleInfo.put("storified", false);
        articleInfo.put("preprocessed", false);
        articleInfo.put("processed", false);
        articleInfo.put("publish_date_time", parms[7]);
        ref.push().setValue(articleInfo);
        Log.d("test", "Title: " + parms[4]);
//        Log.d("test", "title nouns: " + titleKeywords);
        Log.d("test", "description: " + parms[5]);
//        Log.d("test", "body nouns: " + bodyKeywords);
       /* Log.d("test", "ImageSrc: " + imageSrc);
        Log.d("test", "source name: " + sourceName);
        Log.d("test", "category: " + category);
        Log.d("test", "article url: " + url);*/
        ref1.child(parms[6]).removeValue();
        Log.d("test", "key delated " + parms[6]);
    }

    private void parseTimesOfIndia(String sourceName, String category, String url, String key, String pubDateTime) {
        try {
            Log.d("test", "parsing : " + sourceName);
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("test", "parsing : " + sourceName);
        Element titleEle = doc.select("#s_content > div.flL.left_bdr > span.arttle > h1,span.arttle h1,div.article h2").first();
        Element imageEle = doc.select("#bellyad > div > div.flL_pos > img").first();
        Element descriptionEle = doc.select("#artext1 > div,#storydiv > div.section1 > div,div.article > div.content").first();
        String imageUrl = null;
        if (imageEle != null) {
            imageUrl = imageEle.absUrl("src");
        } else
            imageUrl = null;
        if (titleEle != null && !titleEle.text().isEmpty() && descriptionEle.text() != null&& !descriptionEle.text().isEmpty()) {

            String parms[] = new String[]
                    {sourceName, category, url, imageUrl, titleEle.text(), descriptionEle.text(), key};
            postArticleInfo(parms);
        }
    }

    private void parseBBCNews(String sourceName, String category, String url, String key, String pubDateTime) {
        Log.d("test", "parsing : " + sourceName);
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element titleEle = doc.select("div.story-body h1,h1.story-body__h1").first();
        Element imageEle = doc.select("figure.media-landscape.full-width.has-caption.lead img").first();
        Elements descriptionEle = doc.select("div.story-body__inner");

        String imageUrl = null;
        if (imageEle != null) {
            imageUrl = imageEle.absUrl("src");
        } else
            imageUrl = null;
        if (titleEle != null && !titleEle.text().isEmpty() && descriptionEle.text() != null&& !descriptionEle.text().isEmpty()) {
            String parms[] = new String[]
                    {sourceName, category, url, imageUrl, titleEle.text(), descriptionEle.text(), key,pubDateTime};
            postArticleInfo(parms);
        }


    }

    private void parseSifyNews(String sourceName, String category, String url, String key, String pubDateTime) {
        Log.d("test", "parsing : " + sourceName);
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element titleEle = doc.select("#artContent > div.fullstory-heading-wrapper > h1").first();
        Element imageEle = doc.select("#artContent > div:nth-child(7) > img").first();
        Element descriptionEle = doc.select("#contentDiv").first();
        String imageUrl = null;
        if (imageEle != null) {
            imageUrl = imageEle.absUrl("src");
        } else
            imageUrl = null;
        if (titleEle != null && !titleEle.text().isEmpty() && descriptionEle.text() != null&& !descriptionEle.text().isEmpty()) {
            String parms[] = new String[]
                    {sourceName, category, url, imageUrl, titleEle.text(), descriptionEle.text(), key,pubDateTime};
            postArticleInfo(parms);
        }


    }

    private void parseLiveMintNews(String sourceName, String category, String url, String key, String pubDateTime) {
        Log.d("test", "parsing : " + sourceName);
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element titleEle = doc.select("h1.sty_head_38").first();
        Element imageEle = doc.select("div.sty_main_pic_sml1 img").first();
        Elements descriptionEle = doc.select("div.text");
        String imageUrl = null;
        if (imageEle != null) {
            imageUrl = imageEle.absUrl("src");
        } else
            imageUrl = null;
        if (titleEle != null && !titleEle.text().isEmpty() && descriptionEle.text() != null&& !descriptionEle.text().isEmpty()) {
            String parms[] = new String[]
                    {sourceName, category, url, imageUrl, titleEle.text(), descriptionEle.text(), key,pubDateTime};
            postArticleInfo(parms);
        }


    }

}




