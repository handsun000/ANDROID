package com.example.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.news.databinding.ActivityMainBinding;
import com.example.news.databinding.RowNewsBinding;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList list;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        queue = Volley.newRequestQueue(this);
        getNews();
        //1. 화면이 로딩 -> 뉴스 정보를 받아온다.
        //2. 정보 -> 어댑터 넘겨온다.
        //3. 어댑터 -> 셋팅
    }

    public void getNews() {

        // Instantiate the RequestQueue.
        String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=c6ab276010b44a33bb2bdeaa5c7a20c0";



        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObj = new JSONObject(response);

                            JSONArray array = jsonObj.getJSONArray("articles");

                            //response ->> NewsData Class 분류
                            List<NewsData> news = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);

                                Log.d("NEWS", obj.toString());

                                NewsData newsData = new NewsData();
                                newsData.setTitle(obj.getString("title"));
                                newsData.setUrlToImage(obj.getString("urlToImage"));
                                newsData.setContent(obj.getString("content"));

                                news.add(newsData);
                            }

                            binding.recyclerView.setAdapter(new MyAdapter(news, MainActivity.this));

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("User-Agent", "Mozilla/5.0");
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<NewsData> list;

        public MyAdapter(List<NewsData> list, Context context) {
            this.list = list;
            Fresco.initialize(context);
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            RowNewsBinding binding = RowNewsBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
            return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {

            NewsData news = list.get(position);

            Uri uri = Uri.parse(news.getUrlToImage());

            viewHolder.binding.TextViewTitle.setText(news.getTitle());
            String content = news.getContent();
            if (content != null && content.length() > 0) {
                viewHolder.binding.TextViewContent.setText(content);
            }
            else {
                viewHolder.binding.TextViewContent.setText("-");
            }
            viewHolder.binding.TextViewContent.setText(news.getContent());
            viewHolder.binding.ImageViewTitle.setImageURI("");




        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        RowNewsBinding binding;

        public MyViewHolder(RowNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}