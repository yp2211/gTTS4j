package com.dragonbean.cloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* Token (Google Translate Token)
* Generate the current token key and allows generation of tokens (tk) with it
* Java version of `token-script.js` itself from translate.google.com
*
* Author: Yang Peng
* Date: 2017/09/12
*/
public class Token {

    public String token_key;

    String SALT_1 = "+-a^+6";
    String SALT_2 = "+-3^+b+-f";


    public Token(){
        this.token_key = null;
    }

    public String calculate_token(String text, String seed) {
        if (seed == null) {
            seed = this._get_token_key();
        }

        String[] strings = seed.split("\\.");
        String first_seed = strings[0];
        String second_seed = strings[1];

        byte[] d = null;
        try {
            d = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            d = text.getBytes();
        }

        long a = Long.parseLong(first_seed);
        // TODO: Test
        for (byte value : d) {
            a += value;
            a = this._work_token(a, this.SALT_1);
        }
        a = this._work_token(a, this.SALT_2);

        a ^= Long.parseLong(second_seed);
        if (0 > a) {
            a = (a & 2147483647l) + 2147483648l;
        }

        a %= 0x1.e848p19;
        a = Long.parseLong(a + "");

        return a + "." + (a ^ Long.parseLong(first_seed));
    }

    private long _work_token(long a, String seed) {
        // TODO: Test
        for (int i = 0; i < seed.length() - 2; i+=3) {
            char c = seed.toCharArray()[i + 2];
            long d = (c >= 'a') ? ((int)c - 87) : Integer.parseInt(c + "");
            d = (seed.toCharArray()[i + 1] == '+') ? (this._rshift(a, d)) : (a << d);
            a = (seed.toCharArray()[i] == '+') ? (a + d & 4294967295l) : (a ^ d);
        }
        return a;
    }

    private long _rshift(long val, long n) {
        long l = (val >= 0) ? (val >> n) : (val + 0x100000000L) >> n;
        return l;
    }

    private String _get_token_key() {
        if (this.token_key != null) {
            return this.token_key;
        }

        int hours = (int) Math.floor(System.currentTimeMillis()/ 1000 / 3600);

        String urlNameString = "https://translate.google.com/";
        BufferedReader in = null;
        String result = "";
        String a = "";
        String b = "";
        try {
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
            // 建立实际的连接
            connection.connect();

            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            /* 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            */
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;

            //response = requests.get(urlNameString);
            //line = response.text.split('\n')[-1];
            while ((line = in.readLine()) != null) {
                int index = line.indexOf("TKK=");
                if (index > 0) {
                    // String tkk_expr = getGroup1(".*?(TKK=.*?;)W.*?", line.substring(index, 100));
                    String tkk_expr = line.substring(index, index + 100);
                    if (tkk_expr != null) {
                        a = getGroup1("a\\\\x3d(-?\\d+);", tkk_expr);
                        b = getGroup1("b\\\\x3d(-?\\d+);", tkk_expr);
                        break;
                    }
                }

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        result = hours + "." + (Long.parseLong(a) + Long.parseLong(b));
        this.token_key = result;
        return result;
    }

    private String getGroup1(String pattern, String text) {
        Pattern r = Pattern.compile(pattern);

        Matcher m = r.matcher(text);
        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }
}
