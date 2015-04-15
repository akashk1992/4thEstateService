package com.example.akash.fourthestatesample;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import com.firebase.client.*;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class BackGroungService extends Service {
    private RssFeed feed = null;

    public static String BASE_URL = "https://4thestateapp.firebaseio.com";

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                feedCrawl();
                stopSelf();
            }
        }).start();

        return Service.START_STICKY;
    }

    private void articleCrawl(final boolean isExist) {
        final Firebase ref = new Firebase(BASE_URL).child("/article_urls");
        Long time = new Long(new Date().getTime());
        Log.d("test12", "article crawling:");
        final Query q1 = ref.orderByChild("pick_time_stamp").endAt(time).limitToFirst(1);
        q1.addListenerForSingleValueEvent(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(DataSnapshot snapshot) {
                                                  Iterable<DataSnapshot> list = snapshot.getChildren();
                                                  for (DataSnapshot dataSnapshot : list) {
                                                      Log.d("test12", "key" + dataSnapshot.getKey());
                                                      Log.d("test12", "value" + dataSnapshot.getValue());
                                                      Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                                      String key = dataSnapshot.getKey();
                                                      new RssAsyncTask().execute(map.get("url"), isExist, map.get("source_name"), map.get("category"), dataSnapshot.getKey(), map.get("publish_date_time"));
                                                      // ref.child(key).child("pick_time_stamp").setValue(new Date().getTime() + 10 * 60000);
                                                  }

                                              }

                                              @Override
                                              public void onCancelled(FirebaseError firebaseError) {
                                              }
                                          }
        );
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
    }

    public void postData(RssFeed rssFeed, String sourceName, String category, String key) {
        Firebase ref = new Firebase(BASE_URL).child("/article_urls");
        Firebase rssRef = new Firebase(BASE_URL).child("/rssfeed");
        for (RssItem rssItem : rssFeed.getRssItems()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("url", rssItem.getLink());
            map.put("category", category);
            map.put("source_name", sourceName);
            map.put("pick_time_stamp", new Date().getTime());
            map.put("publish_date_time", rssItem.getPubDate().toString());
//            ref.push().setValue(map);

            StringBuffer title=new StringBuffer();
            StringTokenizer stringTokenizer = new StringTokenizer(rssItem.getTitle(), ".#$[],");
            while (stringTokenizer.hasMoreTokens()) {
                title.append(stringTokenizer.nextToken());
            }
            Date lastDate=new Date(new Date().getTime()-(12*60*60*1000));
            Log.d("test12","pubdate"+rssItem.getPubDate()+"   currentdate "+lastDate);
            if (lastDate.before(rssItem.getPubDate())) {
                Log.d("test12","uploded inside"+title);
                    ref.child(new String(title)).setValue(map);
            }
        }
        if (rssFeed != null) {
//            Log.d("test12", "record: " + rssRef.child(key).getKey() + " passed key:" + key + "    ref " + rssRef.child(key).child("pick_time_stamp").getKey());
            rssRef.child(key).child("pick_time_stamp").setValue(new Date().getTime() + (180 * 60000));
//            Log.d(new Date().getTime() + (20 * 60000) + "  test12", "next picked time: " + rssRef.child(key).getKey());
        }
    }

    public void feedCrawl() {
        final Firebase ref = new Firebase(BASE_URL).child("/rssfeed");
        Long time = new Long(new Date().getTime());
        final Query q1 = ref.orderByChild("pick_time_stamp").endAt(time).limitToFirst(1);
        q1.addListenerForSingleValueEvent(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(DataSnapshot snapshot) {
                                                  Iterable<DataSnapshot> list = snapshot.getChildren();
                                                  boolean isExist = false;
                                                  Log.d("test12", "no of feeds " + snapshot.getChildrenCount());
                                                  for (DataSnapshot dataSnapshot : list) {
                                                      Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                                      isExist = true;
                                                      String key = dataSnapshot.getKey();
                                                      Log.d("test12", " key in feed " + key);
                                                      String url = (String) map.get("feed_url");
                                                      try {
                                                          ref.child(key).child("pick_time_stamp").setValue(new Date().getTime() + (10 * 60000));
                                                          Log.d(new Date().getTime() + (10 * 60000) + " test12", "update time");
                                                          new RssAsyncTask().execute(url, isExist, map.get("source_name"), map.get("category"), key).get();
                                                      } catch (InterruptedException e) {
                                                          e.printStackTrace();
                                                      } catch (ExecutionException e) {
                                                          e.printStackTrace();
                                                      }

                                                  }
                                                  if (!isExist) {
                                                      articleCrawl(isExist);
                                                  }

                                              }

                                              @Override
                                              public void onCancelled(FirebaseError firebaseError) {
                                              }
                                          }

        );


    }


    class RssAsyncTask extends AsyncTask {
        ArrayList<RssItem> rssItems;

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Parser parser = new Parser();
                String url = (String) params[0];
                boolean isFeedCrawl = (boolean) params[1];
                String sourceName = (String) params[2];
                String category = (String) params[3];
                String key = (String) params[4];

                URL url1 = new URL(url);
                Log.d("test12", "url in bacground " + url);
                Log.d("test12", "is feeds are there " + isFeedCrawl);
                if (isFeedCrawl) {
                    postData(RssReader.read(url1), sourceName, category, key);
                } else {
                    String pubDateTime = (String) params[5];
//                    Long pubDateTime=(Long)params[5];
                    parser.parseArticle(sourceName, category, url, key, pubDateTime);
                }
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

