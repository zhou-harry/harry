package com.harry.fssc.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	public byte[] getBytes(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[2048];
		int len = 0;
		while ((len = is.read(b, 0, 2048)) != -1) {
			baos.write(b, 0, len);
		}
		baos.flush();
		byte[] bytes = baos.toByteArray();
		return bytes;
	}
}
