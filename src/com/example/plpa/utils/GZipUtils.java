package com.example.plpa.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipUtils {
	public static byte[] compress(String string) throws IOException {
	    byte[] blockcopy = ByteBuffer
	        .allocate(4)
	        .order(java.nio.ByteOrder.LITTLE_ENDIAN)
	        .putInt(string.length())
	        .array();
	    ByteArrayOutputStream os = new ByteArrayOutputStream(string.length());
	    GZIPOutputStream gos = new GZIPOutputStream(os);
	    gos.write(string.getBytes());
	    gos.close();
	    os.close();
	    byte[] compressed = new byte[4 + os.toByteArray().length];
	    System.arraycopy(blockcopy, 0, compressed, 0, 4);
	    System.arraycopy(os.toByteArray(), 0, compressed, 4, os.toByteArray().length);
	    return compressed;
	}

	public static String decompress(byte[] compressed) throws IOException {
	    final int BUFFER_SIZE = 32;
	    ByteArrayInputStream is = new ByteArrayInputStream(compressed, 4, compressed.length - 4);
	    GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
	    StringBuilder string = new StringBuilder();
	    byte[] data = new byte[BUFFER_SIZE];
	    int bytesRead;
	    while ((bytesRead = gis.read(data)) != -1) {
	        string.append(new String(data, 0, bytesRead));
	    }
	    gis.close();
	    is.close();
	    return string.toString();
	}
}
