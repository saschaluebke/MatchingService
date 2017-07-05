package ontology;

import components.Language;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//Transliterated Title (TT) / Title (TI)
public class MedlineAPI {

        private String to = "de";
        private String from = "en";

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo(){
            return to;
        }

        public void setTo(String language){
            this.to = language;
        }

        public String init(){
            String response = "!! Translation Error !!";
            String url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/einfo.fcgi";
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

            //String data =  "{'to':'"+to+"','text':'"+word+"'}";
            OutputStreamWriter out=null;
            InputStreamReader isr=null;
            try {
                out = new OutputStreamWriter(conn.getOutputStream());
                //out.write(data);
                out.close();
                isr =   new InputStreamReader(conn.getInputStream());
            } catch (IOException e1) {

                e1.printStackTrace();
            }


            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            try {
                db = dbf.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            URL ur = null;
            try {
                ur = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                Document doc = db.parse(ur.openStream());
                System.out.println("Aus dem Doc genomen:");
                System.out.println(doc.getNodeName());
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

/*
            try (Scanner scanner = new Scanner(isr)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.print(responseBody);
                //String tempString = responseBody.substring(responseBody.indexOf("translationText")+17);
               // response = tempString.substring(1, tempString.indexOf("}")-1);


            } catch (Exception e) {
                e.printStackTrace();
            }
*/
            return response;
        }



    }




