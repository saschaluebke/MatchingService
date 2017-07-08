package translators;

import components.Language;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

/**
 * API from Transltr.org
 */
public class Transltr implements Translator{
	private String to = "de";
	private String from = "en";

	public String translate(String input){
		String output="";
		String[] tokens = tokenizer(input);
		for(int i=0;i<tokens.length;i++){
			String t = transform(tokens[i]);
			tokens[i] = t;
		}
		output = retokenizer(tokens);
		return output;
	}
	
	public String transform(String word){
		String response = "!! Translation Error !!";
		String url = "http://www.transltr.org/api/translate?text="+word+"&to="+to+"&from="+from;
		URL obj=null;

		try {
			obj = new URL(url);
		} catch (MalformedURLException e1) {

			e1.printStackTrace();
		}
		HttpURLConnection conn=null;
		try {
			conn = (HttpURLConnection) obj.openConnection();
		} catch (IOException e1) {

			e1.printStackTrace();
		}

		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		try {
			conn.setRequestMethod("GET");
		} catch (ProtocolException e1) {

			e1.printStackTrace();
		}

		String userpass = "user" + ":" + "pass";
		String basicAuth="";
		try {
			basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}
		conn.setRequestProperty ("Authorization", basicAuth);

		String data =  "{'to':'"+to+"','text':'"+word+"'}";
		OutputStreamWriter out=null;
		InputStreamReader isr=null;
		try {
			out = new OutputStreamWriter(conn.getOutputStream());
			out.write(data);
			out.close();
			isr =   new InputStreamReader(conn.getInputStream());
		} catch (IOException e1) {

			e1.printStackTrace();
		}

		try (Scanner scanner = new Scanner(isr)) {
			String responseBody = scanner.useDelimiter("\\A").next();
			String tempString = responseBody.substring(responseBody.indexOf("translationText")+17);
			response = tempString.substring(1, tempString.indexOf("}")-1);


		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	public List<Language> getLanguages(){
		Language de = new Language("de","German");
		Language en = new Language("en","English");
		Language it = new Language("it", "Italian");
		Language fin = new Language("fi",	"finnish");
		Language es = new Language("es","spanish");
		List<Language> languages = new ArrayList<>();
		languages.add(de);
		languages.add(en);
		languages.add(it);
		languages.add(fin);
		languages.add(es);
				/* All languages {"ar","bs","bg","ca","zh-CHS","zh-CHT","hr","cs","da","nl","en","et","fi","fr","de","el",
				"ht","he","hi","mww","hu","id","it","ja","sw","ko","lv","lt","no","fa","pl","pt","ro","ru","sr-Latn",
				"sl","es","sv","tr","uk" };
				*/
		return languages;
	}

	@Override
	public String translation(String input) {
		return translate(input);
	}

	@Override
	public void setFromLanguage(String language) {
		this.from = language;
	}

	@Override
	public void setToLanguage(String language) {
		this.to = language;
	}

	@Override
	public String getFromLanguage() {
		return from;
	}

	@Override
	public String getToLanguage() {
		return to;
	}

	public String[] tokenizer(String sentence){
		sentence = sentence.replace(".","");
		sentence = sentence.replace("-"," ");

		String[] array = sentence.split(" ");

		for (int i=0; i<array.length; i++){
			array[i].trim();
		}
		return array;
	}

	public String retokenizer(String[] tokens){
		String translation = "";
		StringBuilder builder = new StringBuilder();
		for(String s : tokens) {
			builder.append(s);
			builder.append(" ");
		}
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}
}


