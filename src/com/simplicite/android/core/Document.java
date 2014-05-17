/*
 * Simplicite(R) for Google Android(R)
 * http://www.simplicite.fr
 */
package com.simplicite.android.core;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;

/**
 * <p>Document</p>
 */
public class Document implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String MIME_JPG = "image/jpeg";
	public final static String MIME_PNG = "image/png";
	public final static String MIME_GIF = "image/gif";

	/**
	 * <p>Document ID</p>
	 */
	public String id;
	/**
	 * <p>Name</p>
	 */
	public String name;
	/**
	 * <p>Relative path</p>
	 */
	public String path;
	/**
	 * <p>MIME type</p>
	 */
	public String mime;
	/**
	 * <p>Size</p>
	 */
	public int size;
	/**
	 * <p>Content (base64 encoded)</p>
	 */
	public String content;

	/**
	 * <p>Get decoded content as byte array</p>
	 */
	public byte[] getData() {
		return Base64.decode(content, Base64.DEFAULT);
	}

	/**
	 * <p>Encode and set content from byte array</p>
	 * @param data Data
	 * @param mime Mime tyep
	 */
	public void setData(byte[] data, String mime) {
		content = Base64.encodeToString(data, Base64.DEFAULT);
		size = data.length;
		this.mime = mime;
	}

	/**
	 * <p>Encode and set content from bitmap</p>
	 * @param bitmap Bitmap
	 * @param format Format
	 */
	public void setData(Bitmap bitmap, CompressFormat format) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(format, 100, baos);
		setData(baos.toByteArray(), format.equals(CompressFormat.JPEG) ? MIME_JPG : MIME_PNG);
	}

	/**
	 * <p>Get content as bitmap</p>
	 */
	public Bitmap getBitmap() {
		byte[] data = getData();
		return data.length == 0 ? null : BitmapFactory.decodeByteArray(data, 0, data.length);
	}

	/**
	 * <p>PNG thumbnail (base64 encoded)</p>
	 */
	public String thumbnail;
	/**
	 * <p>Get decoded PNG thumbnail as byte array</p>
	 */
	public byte[] getThumbnailData() {
		return Base64.decode(thumbnail, Base64.DEFAULT);
	}
	/**
	 * <p>Get thumbnail content as bitmap</p>
	 */
	public Bitmap getThumbnailBitmap() {
		byte[] data = getThumbnailData();
		return data.length == 0 ? null : BitmapFactory.decodeByteArray(data, 0, data.length);
	}

	/**
	 * <p>Object name (only if the document is an object field value)</p>
	 */
	public String object;
	/**
	 * <p>Object row ID (only if the document is an object field value)</p>
	 */
	public String rowid;
	/**
	 * <p>Field name (only if the document is an object field value)</p>
	 */
	public String field;

	/**
	 * <p>String representation</p>
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("id:" + id + "\n");
		s.append("name:" + name + "\n");
		s.append("path:" + path + "\n");
		s.append("mime:" + mime + "\n");
		s.append("size:" + size + "\n");
		s.append("content:" + content + "\n");
		s.append("object:" + object + "\n");
		s.append("rowid:" + rowid + "\n");
		s.append("field:" + field + "\n");
		return s.toString();
	}

}