package com.jmw.volley.toolbox;

import com.android.volley.Cache;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.protocol.HTTP;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dks0280703 on 10/29/14.
 *
 * We need to create a fake cache entry for our image
 */
public class JMWHeaderParser {

    public static Cache.Entry parseCacheHeaders(String path, byte[] data){

        // Make the date
        long now = System.currentTimeMillis();
        // Get the headers
        Map<String, String>headers = makeHeaders(now, path, data);

        long serverDate = 0;
        long serverExpires = 0;
        long softExpire = 0;
        long maxAge = 0;
        boolean hasCacheControl = false;

        String serverEtag = null;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = parseDateAsEpoch(headerValue);
        }

        headerValue = headers.get("Cache-Control");
        if (headerValue != null) {
            hasCacheControl = true;
            String[] tokens = headerValue.split(",");
            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i].trim();
                if (token.equals("no-cache") || token.equals("no-store")) {
                    return null;
                } else if (token.startsWith("max-age=")) {
                    try {
                        maxAge = Long.parseLong(token.substring(8));
                    } catch (Exception e) {
                    }
                } else if (token.equals("must-revalidate") || token.equals("proxy-revalidate")) {
                    maxAge = 0;
                }
            }
        }

        headerValue = headers.get("Expires");
        if (headerValue != null) {
            serverExpires = parseDateAsEpoch(headerValue);
        }

        serverEtag = headers.get("ETag");

        // Cache-Control takes precedence over an Expires header, even if both exist and Expires
        // is more restrictive.
        if (hasCacheControl) {
            softExpire = now + maxAge * 1000;
        } else if (serverDate > 0 && serverExpires >= serverDate) {
            // Default semantic for Expire header in HTTP specification is softExpire.
            softExpire = now + (serverExpires - serverDate);
        }

        Cache.Entry entry = new Cache.Entry();
        entry.data = data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = entry.softTtl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;

        return entry;

    }


    private static Map<String, String> makeHeaders(long now, String path, byte[] data){


        String eTag = makeETag(path, now);

        String time = String.valueOf(now);

        String contentLength = ""+data.length;



        // Now get the current date and expriation date
        String date = new String();
        String expDate = new String();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
            date = sdf.format(new Date());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(date));
            calendar.add(Calendar.DATE, 1);

            expDate = sdf.format(calendar.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String recievedTime = ""+System.currentTimeMillis();
        int r = (int)(Math.random()*100+1);
        String sentTime = recievedTime+r;



        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Alternate-Protocol", "443:quic,p=0.01");
        headers.put("Cache-Control", "public, max-age="+time+", no-transform");
        headers.put("Content-Disposition=inline;filename", path);
        headers.put("Content-Length", contentLength);
        headers.put("Date", date);
        headers.put("ETag", eTag);
        headers.put("Expires", expDate);
        headers.put("Server", "fife");
        headers.put("X-Android-Received-Millis", recievedTime);
        headers.put("X-Android-Response-Source", "NETWORK200");
        headers.put("X-Android-Sent-Millis", sentTime);
        headers.put("X-Content-Type-Options", "nosniff");
        headers.put("X-XSS-Protection", "1;mode=block");

        return headers;
    }


    /**
     * Make a fake eTag for Assets
     * @param path the path to the asset file
     * @param date the current date
     * @return an Etag
     */
    private static String makeETag(String path, long date){

        String tag = path+date;
        String hashTag = hashKeyForDisk(tag);

        if(hashTag.length()>32){
            try {
                String newTag = hashTag.substring(0, 32);
                return newTag;
            }catch (Exception e){}
        }

        return hashTag;
    }


    /**
     * A hashing method that changes a string (like a URL) into a hash suitable for using as a
     * disk filename.
     */
    private static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }


    /**
     * A method that changes bytes to a HexString
     * @param bytes
     * @return
     */
    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * Parse date in RFC1123 format, and return its value as epoch
     */
    public static long parseDateAsEpoch(String dateStr) {
        try {
            // Parse date in RFC1123 format if this header contains one
            return DateUtils.parseDate(dateStr).getTime();
        } catch (DateParseException e) {
            // Date in invalid format, fallback to 0
            return 0;
        }
    }

    /**
     * Returns the charset specified in the Content-Type of this header,
     * or the HTTP default (ISO-8859-1) if none can be found.
     */
    public static String parseCharset(Map<String, String> headers) {
        String contentType = headers.get(HTTP.CONTENT_TYPE);
        if (contentType != null) {
            String[] params = contentType.split(";");
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("charset")) {
                        return pair[1];
                    }
                }
            }
        }
        return HTTP.DEFAULT_CONTENT_CHARSET;
    }


}
