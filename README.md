# gTTS4j
gTTS4j (Google Text to Speech): Java version of an interface to Google's Text to Speech API. 

## Usage

### 1. Import `gTTS4j`
    import com.dragonbean.cloud.gTTS4j;

### 2. Create an instance
    gTTS4j gtts = new gTTS4j();
    gtts.init(text, "en", true, false);

###### _Parameters:_
*  `text` - String - Text to be spoken.
*  `lang` - String - [ISO 639-1 language code](#lang_list) (supported by the Google _Text to Speech_ API) to speak in.
*  `slow` - Boolean - Speak slowly. Default `False` (Note: only two speeds are provided by the API).

### 3. Get voice stream
    InputStream is = gtts.exec();

###### _Example:_
    
    InputStream is = null;
    String text = "Surprise!";
    gTTS4j gtts = new gTTS4j();
    try {
        gtts.init(text, "en", true, false);
        is = gtts.exec();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (is != null) is.close();
    }

## Supported Languages <a name="lang_list"></a>

  * 'af' : 'Afrikaans'
  * 'sq' : 'Albanian'
  * 'ar' : 'Arabic'
  * 'hy' : 'Armenian'
  * 'bn' : 'Bengali'
  * 'ca' : 'Catalan'
  * 'zh' : 'Chinese'
  * 'zh-cn' : 'Chinese (Mandarin/China)'
  * 'zh-tw' : 'Chinese (Mandarin/Taiwan)'
  * 'zh-yue' : 'Chinese (Cantonese)'
  * 'hr' : 'Croatian'
  * 'cs' : 'Czech'
  * 'da' : 'Danish'
  * 'nl' : 'Dutch'
  * 'en' : 'English'
  * 'en-au' : 'English (Australia)'
  * 'en-uk' : 'English (United Kingdom)'
  * 'en-us' : 'English (United States)'
  * 'eo' : 'Esperanto'
  * 'fi' : 'Finnish'
  * 'fr' : 'French'
  * 'de' : 'German'
  * 'el' : 'Greek'
  * 'hi' : 'Hindi'
  * 'hu' : 'Hungarian'
  * 'is' : 'Icelandic'
  * 'id' : 'Indonesian'
  * 'it' : 'Italian'
  * 'ja' : 'Japanese'
  * 'km' : 'Khmer (Cambodian)'
  * 'ko' : 'Korean'
  * 'la' : 'Latin'
  * 'lv' : 'Latvian'
  * 'mk' : 'Macedonian'
  * 'no' : 'Norwegian'
  * 'pl' : 'Polish'
  * 'pt' : 'Portuguese'
  * 'ro' : 'Romanian'
  * 'ru' : 'Russian'
  * 'sr' : 'Serbian'
  * 'si' : 'Sinhala'
  * 'sk' : 'Slovak'
  * 'es' : 'Spanish'
  * 'es-es' : 'Spanish (Spain)'
  * 'es-us' : 'Spanish (United States)'
  * 'sw' : 'Swahili'
  * 'sv' : 'Swedish'
  * 'ta' : 'Tamil'
  * 'th' : 'Thai'
  * 'tr' : 'Turkish'
  * 'uk' : 'Ukrainian'
  * 'vi' : 'Vietnamese'
  * 'cy' : 'Welsh'
    
## Contributing

1. _Fork_ [yp2211/gTTS4j](https://github.com/yp2211/gTTS4j) on GitHub and clone it locally
2. Make sure you write tests for new features or modify the existing ones if necessary
3. Open a new _Pull Request_ from your feature branch to the `master` branch.
4. Thank you!
