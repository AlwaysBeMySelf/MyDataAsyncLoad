package com.example.dataasyncload;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.example.adapter.MyAdapter;
import com.example.domain.Contact;
import com.example.service.ContactService;

public class MainActivity extends Activity {

	private ListView listview;
	File cache;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			listview.setAdapter(new MyAdapter(MainActivity.this,(List<Contact>)msg.obj, R.layout.list_item,cache));
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listview = (ListView) findViewById(R.id.ls);
		cache = new File(Environment.getExternalStorageDirectory(), "cache");
		if (!cache.exists()) {
			cache.mkdirs();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<Contact> data = ContactService.getContact();
					handler.sendMessage(handler.obtainMessage(22,data));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		if (cache.exists()) {
			for (File file : cache.listFiles()) {
				file.delete();
			}
			cache.delete();
		}
		super.onDestroy();
	}
	
}
