package com.study.inovel.util;

import android.util.Log;
import android.widget.Toast;

import com.study.inovel.app.App;
import com.study.inovel.bean.Book;
import com.study.inovel.bean.CacheBook;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dnw on 2017/3/31.
 */

public class HtmlParserUtil {

    public static String getNovelLink(String url)
    {
        Connection conn= Jsoup.connect(url);
        try {
            //获取到整个网页
            Document doc=conn.timeout(10000).get();
            //获取检索到的小说列表的第一个
            Element li1=doc.select("div.wrap").select("div.result-wrap").select("div.main-content-wrap").select("div.book-img-text").select("ul").select("li").get(0);
            //从第一个小说中，检索书籍详情链接，也可以在此获取更新状态，但是有时不准
            Element node2=li1.select("div.book-img-box").select("a[href]").first();
            return node2.attr("href");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
    public static Book getInfo(String url)
    {
        Connection conn= Jsoup.connect(url);
        Log.d("test","inok");
        try {
            //获取到整个网页
            Book book=new Book();
            Document doc=conn.timeout(10000).get();
            //获取图片地址
            book.imgUrl=doc.select("div.wrap").select("div.result-wrap").select("div.main-content-wrap").select("div.book-img-text").select("ul").select("li").get(0).select("div.book-img-box")
                    .select("a[href]").first().getElementsByTag("img").attr("src");
            //获取书名
            book.bookName=doc.select("div.wrap").select("div.result-wrap").select("div.main-content-wrap").select("div.book-img-text").select("ul").select("li").get(0).select("div.book-mid-info").first().getElementsByTag("h4").select("cite.red-kw").text();
            //获取作者
            book.author=doc.select("div.wrap").select("div.result-wrap").select("div.main-content-wrap").select("div.book-img-text").select("ul").select("li").get(0).select("div.book-mid-info").select("p.author").select("a.name").text();
            //获取简介
            book.info=doc.select("div.wrap").select("div.result-wrap").select("div.main-content-wrap").select("div.book-img-text").select("ul").select("li").get(0).select("div.book-mid-info").select("p.intro").text();
            //获取更新状态
            book.updateTitle=doc.select("div.wrap").select("div.result-wrap").select("div.main-content-wrap").select("div.book-img-text").select("ul").select("li").get(0).select("div.book-mid-info").select("p.update").select("a[href]").text();
            book.updateTime=doc.select("div.wrap").select("div.result-wrap").select("div.main-content-wrap").select("div.book-img-text").select("ul").select("li").get(0)
                    .select("div.book-mid-info").select("p.update").select("span").text();
            return book;
        }catch(Exception e)
        {
            //Toast.makeText(App.getContext(),"网络连接差，请重试...",Toast.LENGTH_SHORT).show();
        }

        return null;
    }
    public static Book getUpdateInfo(String url)
    {
        Connection conn= Jsoup.connect(url);
        try {
            //获取到整个网页
            Book book=new Book();
            Document doc=conn.timeout(10000).get();
            //获取图片地址
            book.imgUrl=doc.select("div.book-detail-wrap").select("div.book-information").select("div.book-img").first().select("a.J-getJumpUrl").first().getElementsByTag("img").attr("src");
            //获取书名
            book.bookName=doc.select("div.book-detail-wrap").select("div.book-information").select("div.book-info").first().select("h1").select("em").text();
            //获取作者
            book.author=doc.select("div.book-detail-wrap").select("div.book-information").select("div.book-info").first().select("h1").select("a").text();
            //获取简介
            String info=doc.select("div.wrap").select("div.book-detail-wrap").select("div.book-content-wrap").select("div.left-wrap")
                    .select("div.book-info-detail").select("div.book-intro").select("p").text();
            Pattern pattern=Pattern.compile("\\s+");//匹配一或多个空格
            Matcher matcher=pattern.matcher(info);
            book.info=matcher.replaceAll("");
            //book.info=info.substring(info.lastIndexOf(" "),10);
            //获取更新状态
            book.updateTitle=doc.select("div.wrap").select("div.book-detail-wrap").select("div.book-content-wrap").select("div.left-wrap")
                    .select("div.book-info-detail").select("div.book-state").select("ul").select("li.update").select("div.detail").select("p.cf").select("a.blue").attr("title");

            book.updateTime=doc.select("div.wrap").select("div.book-detail-wrap").select("div.book-content-wrap").select("div.left-wrap")
                    .select("div.book-info-detail").select("div.book-state").select("ul").select("li.update").select("div.detail").select("p.cf").select("em.time").text();
            return book;
        }catch(Exception e)
        {
            //Toast.makeText(App.getContext(),"网络连接差，请重试...",Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    public static CacheBook getCacheUpdateInfo(String url)
    {
        Connection conn= Jsoup.connect(url);
        try {
            //获取到整个网页
            CacheBook book=new CacheBook();
            Document doc=conn.timeout(10000).get();
            //获取书名
            book.cacheBookName=doc.select("div.book-detail-wrap").select("div.book-information").select("div.book-info").first().select("h1").select("em").text();
            //获取作者
            book.cacheAuthor=doc.select("div.book-detail-wrap").select("div.book-information").select("div.book-info").first().select("h1").select("a").text();
            //获取更新状态
            book.cacheUpdateTitle=doc.select("div.wrap").select("div.book-detail-wrap").select("div.book-content-wrap").select("div.left-wrap")
                    .select("div.book-info-detail").select("div.book-state").select("ul").select("li.update").select("div.detail").select("p.cf").select("a.blue").attr("title");
            book.cacheUpdateTime=doc.select("div.wrap").select("div.book-detail-wrap").select("div.book-content-wrap").select("div.left-wrap")
                    .select("div.book-info-detail").select("div.book-state").select("ul").select("li.update").select("div.detail").select("p.cf").select("em.time").text();
            return book;
        }catch(Exception e)
        {
            //Toast.makeText(App.getContext(),"网络连接差，请重试...",Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    public static String getNovelIsExist(String url)
    {
        Connection conn= Jsoup.connect(url);
        try {
            //获取到整个网页
            Document doc=conn.timeout(10000).get();
            //获取检索到的小说列表的第一个
            Element li1=doc.select("div.wrap").select("div.result-wrap").select("div.main-content-wrap").select("div.book-img-text").select("ul").select("li").get(0);
            //从第一个小说中，检索书籍详情链接，也可以在此获取更新状态，但是有时不准
            Element node2=li1.select("div.book-img-box").select("a[href]").first();
            return node2.select("cite").text();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
