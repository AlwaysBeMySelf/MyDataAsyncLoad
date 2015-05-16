package com.example.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.R.integer;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import com.example.domain.Contact;
import com.example.utils.MD5;

public class ContactService {
	public static List<Contact> getContact() throws Exception {
		String path = "http://169.254.201.211:8080/web/list.xml";

		HttpURLConnection conn = (HttpURLConnection) new URL(path)
				.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			return parseXML(conn.getInputStream());
		}

		return null;
	}

	private static List<Contact> parseXML(InputStream inputStream)
			throws XmlPullParserException, IOException {
		List<Contact> contacts = new ArrayList<Contact>();
		Contact contact = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inputStream, "UTF-8");
		int type = parser.getEventType();
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("contact".equals(parser.getName())) {
					contact = new Contact();
					int id = Integer.parseInt(parser.getAttributeValue(0));
					contact.setId(id);
				}
				if ("name".equals(parser.getName())) {
					String name = parser.nextText();
					contact.setName(name);
				}
				if ("image".equals(parser.getName())) {
					String image = parser.getAttributeValue(0);
					contact.setPath(image);
				}
				break;
			case XmlPullParser.END_TAG:
				if ("contact".equals(parser.getName())) {
					contacts.add(contact);
					contact = null;
				}
				break;
			}
			type = parser.next();
		}
		return contacts;
	}
	
	public static Uri getImage(String path, File cacaheDir) throws IOException {
		File localFile = new File(cacaheDir,MD5.getMD5(path)+ path.substring(path.lastIndexOf(".")));
		if (localFile.exists()) {
			return Uri.fromFile(localFile);
		}
		else {
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				FileOutputStream stream = new FileOutputStream(localFile);
				InputStream inputStream = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int len;
				while ((len = inputStream.read(buffer)) != -1) {
					stream.write(buffer, 0, len);
				}
				inputStream.close();
				stream.close();
				return Uri.fromFile(localFile);
			}
		}
		return null;
		
	}

}
