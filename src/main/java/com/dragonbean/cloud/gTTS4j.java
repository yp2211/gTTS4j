package com.dragonbean.cloud;

import autovalue.shaded.org.apache.commons.lang.ArrayUtils;
import autovalue.shaded.org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 *
 * gTTS4j (Google Text to Speech): Java version of an interface to Google's Text to Speech API
 * Google TTS API supports two read speeds
 * (speed <= 0.3: slow; speed > 0.3: normal; default: 1)
 *
 * Github: https://github.com/yp2211/gTTS4j
 *
 */
public class gTTS4j {

    private String lang = "en";
    private String text = "";
    private double speed = Speed.SLOW;
    private List<String> text_parts;
    private Token token;

    public static class Speed {
        public static double SLOW = 0.3;
        public static double NORMAL = 1;
    }

    private final String GOOGLE_TTS_URL = "https://translate.google.com/translate_tts";
    private final int MAX_CHARS = 100; // Max characters the Google TTS API takes at a time

    HashMap<String, String> LANGUAGES_MAP;

    public  gTTS4j() {
        LANGUAGES_MAP = new HashMap();
        LANGUAGES_MAP.put("af", "Afrikaans");
        LANGUAGES_MAP.put("sq", "Albanian");
        LANGUAGES_MAP.put("ar" , "Arabic");
        LANGUAGES_MAP.put("hy" , "Armenian");
        LANGUAGES_MAP.put("bn" , "Bengali");
        LANGUAGES_MAP.put("ca" , "Catalan");
        LANGUAGES_MAP.put("zh" , "Chinese");
        LANGUAGES_MAP.put("zh-cn" , "Chinese (Mandarin/China)");
        LANGUAGES_MAP.put("zh-tw" , "Chinese (Mandarin/Taiwan)");
        LANGUAGES_MAP.put("zh-yue" , "Chinese (Cantonese)");
        LANGUAGES_MAP.put("hr" , "Croatian");
        LANGUAGES_MAP.put("cs" , "Czech");
        LANGUAGES_MAP.put("da" , "Danish");
        LANGUAGES_MAP.put("nl" , "Dutch");
        LANGUAGES_MAP.put("en" , "English");
        LANGUAGES_MAP.put("en-au" , "English (Australia)");
        LANGUAGES_MAP.put("en-uk" , "English (United Kingdom)");
        LANGUAGES_MAP.put("en-us" , "English (United States)");
        LANGUAGES_MAP.put("eo" , "Esperanto");
        LANGUAGES_MAP.put("fi" , "Finnish");
        LANGUAGES_MAP.put("fr" , "French");
        LANGUAGES_MAP.put("de" , "German");
        LANGUAGES_MAP.put("el" , "Greek");
        LANGUAGES_MAP.put("hi" , "Hindi");
        LANGUAGES_MAP.put("hu" , "Hungarian");
        LANGUAGES_MAP.put("is" , "Icelandic");
        LANGUAGES_MAP.put("id" , "Indonesian");
        LANGUAGES_MAP.put("it" , "Italian");
        LANGUAGES_MAP.put("ja" , "Japanese");
        LANGUAGES_MAP.put("km" , "Khmer (Cambodian)");
        LANGUAGES_MAP.put("ko" , "Korean");
        LANGUAGES_MAP.put("la" , "Latin");
        LANGUAGES_MAP.put("lv" , "Latvian");
        LANGUAGES_MAP.put("mk" , "Macedonian");
        LANGUAGES_MAP.put("no" , "Norwegian");
        LANGUAGES_MAP.put("pl" , "Polish");
        LANGUAGES_MAP.put("pt" , "Portuguese");
        LANGUAGES_MAP.put("ro" , "Romanian");
        LANGUAGES_MAP.put("ru" , "Russian");
        LANGUAGES_MAP.put("sr" , "Serbian");
        LANGUAGES_MAP.put("si" , "Sinhala");
        LANGUAGES_MAP.put("sk" , "Slovak");
        LANGUAGES_MAP.put("es" , "Spanish");
        LANGUAGES_MAP.put("es-es" , "Spanish (Spain)");
        LANGUAGES_MAP.put("es-us" , "Spanish (United States)");
        LANGUAGES_MAP.put("sw" , "Swahili");
        LANGUAGES_MAP.put("sv" , "Swedish");
        LANGUAGES_MAP.put("ta" , "Tamil");
        LANGUAGES_MAP.put("th" , "Thai");
        LANGUAGES_MAP.put("tr" , "Turkish");
        LANGUAGES_MAP.put("uk" , "Ukrainian");
        LANGUAGES_MAP.put("vi" , "Vietnamese");
        LANGUAGES_MAP.put("cy" , "Welsh");
    }

    public void init(String text, String lang, boolean slow, boolean dubug) throws Exception {
        if (!LANGUAGES_MAP.containsKey(lang.toLowerCase())) throw new Exception("Language not supported: " + lang);
        else this.lang = lang;

        if (text == null || text.trim() == "") throw new Exception("No text to speak");
        else this.text = text;

        if (slow) this.speed = Speed.SLOW;
        else this.speed = Speed.NORMAL;

        // # Split text in parts
        List<String> text_parts = new ArrayList<String>();
        if (this.text.length() <= this.MAX_CHARS) {
            text_parts.add(text);
        } else {
            text_parts = this._tokenize(text, this.MAX_CHARS);
        }

        // # Clean
        this.text_parts = new ArrayList<String>();
        for (int i = 0; i < text_parts.size(); i++) {
            String temp = strip(text_parts.get(i));
            if (!temp.equals("")) {
                this.text_parts.add(temp);
            }
        }
        this.text_parts = text_parts;

        // # Google Translate token
        this.token = new Token();
    }

    private String strip(String x) { return x.replace("\n", "").trim(); }

    public InputStream exec() {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        InputStream is = null;

        try {
            for (int idx = 0; idx < this.text_parts.size(); idx++) {
                String part = this.text_parts.get(idx);
                String url = GOOGLE_TTS_URL + "?"
                        + "ie=" + "UTF-8"
                        + "&" + "q=" + part
                        + "&" + "tl=" + this.lang
                        + "&" + "ttsspeed=" + this.speed
                        + "&" + "total=" + this.text_parts.size()
                        + "&" + "idx=" + idx
                        + "&" + "client=" + "tw-ob"
                        + "&" + "textlen=" + part.length()
                        + "&" + "tk=" + this.token.calculate_token(part, null)
                        ;
                httpClient = HttpClients.createDefault();
                HttpGet get = new HttpGet(GOOGLE_TTS_URL);
                try {
                    URI uri = new URI(url);
                    get.setURI(uri);

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                get.setHeader("Referer", "http://translate.google.com/");
                get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");

                response = httpClient.execute(get);

                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    is = httpEntity.getContent();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return is;
    }

    private CloseableHttpClient getHttpClient(){
        return HttpClients.createDefault();
    }

    private List<String> _tokenize(String text, int max_size) {
        // """ Tokenizer on basic punctuation """

        String punc = "¡!()[]¿?.,،;:—。、：？！\n";
        char[] punc_list = punc.toCharArray();
        String pattern = StringUtils.join(ArrayUtils.toObject(punc_list), '|');
        String[] parts = text.split(pattern);

        List<String> min_parts = new ArrayList<String>();

        for (String p : parts){
            min_parts.addAll(this._minimize(p, " ", max_size));
        }

        return min_parts;
    }

    private List<String> _minimize(String thestring, String delim, int max_size) {
        // """ Recursive function that splits `thestring` in chunks
        // of maximum `max_size` chars delimited by `delim`. Returns list. """
        int beginInx = 0;
        List<String> outStrings = new ArrayList<String>();
        if ((thestring.length()) > max_size) {
            int idx = thestring.lastIndexOf(delim, max_size);
            if (idx > 0) {
                outStrings.add(thestring.substring(beginInx, idx));

                List<String> temp = this._minimize(thestring.substring(idx),delim, max_size);
                for (String s : temp) outStrings.add(s);
            }
        } else {
            outStrings.add(thestring);
        }

        return outStrings;
    }

}
