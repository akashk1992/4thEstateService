package com.example.akash.fourthestatesample;

import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by akash on 8/4/15.
 */
public class FeedReader {
static private RssFeed feed;
   public RssFeed readFeed(final URL url) {
       new RssAsyncTask().execute(url);
       return feed;
   }
       class RssAsyncTask extends AsyncTask {
           ArrayList<RssItem> rssItems;

           @Override
           protected Object doInBackground(Object[] params) {
               try {
                   URL url=(URL)params[0];
                   feed = RssReader.read(url);
               } catch (MalformedURLException e) {
                   e.printStackTrace();
               } catch (SAXException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
//               rssItems = feed.getRssItems();

               return null;
           }

           @Override
           protected void onPostExecute(Object o) {
           }
       }

   }



