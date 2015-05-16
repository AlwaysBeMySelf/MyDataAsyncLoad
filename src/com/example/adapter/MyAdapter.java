package com.example.adapter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dataasyncload.R;
import com.example.domain.Contact;
import com.example.service.ContactService;

public class MyAdapter extends BaseAdapter {
	List<Contact> data;
	int listItem;
	File cache;
	LayoutInflater inflater;
	Context context;
	public MyAdapter(Context context, List<Contact> data, int listItem,
			File cache) {

		this.context = context;
		this.data = data;
		this.listItem = listItem;
		this.cache = cache;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		TextView textView;
		if (convertView == null) {
			convertView = inflater.inflate(listItem, null);
			imageView = (ImageView) convertView.findViewById(R.id.image);
			textView = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(new DataWrapper(imageView, textView));
		} else {
			DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
			imageView = dataWrapper.imageView;
			textView = dataWrapper.textView;
		}
		
		Contact contact = data.get(position);
		textView.setText(contact.getName());
		asyncImageLoad(imageView, contact.getPath());
		return convertView;
	}

	private void asyncImageLoad(final ImageView imageView, final String path) {
		AsyncImageTask imageTask = new AsyncImageTask(imageView);
		imageTask.execute(path);
	}

	private final class AsyncImageTask extends AsyncTask<String, Integer, Uri> {
		ImageView imageView;

		public AsyncImageTask(ImageView imageView) {
			this.imageView = imageView;
		}

		@Override
		protected Uri doInBackground(String... params) {
				try {
					return ContactService.getImage(params[0], cache);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			return null;
		}

		@Override
		protected void onPostExecute(Uri result) {
			if (result != null && imageView != null)
				imageView.setImageURI(result);
			super.onPostExecute(result);
		}

	}

	/*private void asyncImageLoad(final ImageView imageView, final String path) {

		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				Uri uri = (Uri) msg.obj;
				if (imageView != null && uri != null) {
					imageView.setImageURI(uri);
				}
			}
		};

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					Uri image = ContactService.getImage(path, cache);
					handler.sendMessage(handler.obtainMessage(11, image));
				} catch (IOException e) { // TODO Auto-generated catch block
											// e.printStackTrace();
				}
			}
		};
		new Thread(runnable).start();
	}*/

	private final class DataWrapper {
		public ImageView imageView;
		public TextView textView;

		public DataWrapper(ImageView imageView, TextView textView) {
			this.imageView = imageView;
			this.textView = textView;
		}
	}

}
