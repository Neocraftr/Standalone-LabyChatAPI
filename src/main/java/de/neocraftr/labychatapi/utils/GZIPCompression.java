package de.neocraftr.labychatapi.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPCompression {

    public static byte[] compress(byte[] input) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(input);
            gzip.close();
            byte[] compressed = bos.toByteArray();
            bos.close();
            return compressed;
        } catch (IOException err) {
            err.printStackTrace();
            return input;
        }
    }

    public static byte[] decompress(byte[] input) {
        try {
            byte[] buffer = new byte[1024];
            ByteArrayInputStream bis = new ByteArrayInputStream(input);
            GZIPInputStream gis = new GZIPInputStream(bis);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int len;
            while((len = gis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            gis.close();
            out.close();
            return out.toByteArray();
        } catch (IOException err) {
            err.printStackTrace();
            return input;
        }
    }
}
