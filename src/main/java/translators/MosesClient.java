package translators;


import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.URL;
import java.util.HashMap;


public class MosesClient implements Translator {
    private String fromLanguage;
    private String toLanguage;
    private  XmlRpcClientConfigImpl config;
    private XmlRpcClient client;

    /**
     *
     * @param path Normal is "http://localhost:8080/RPC2"
     */
    public MosesClient(String path){
        try {
            // Create an instance of XmlRpcClient
            config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(path));
            client = new XmlRpcClient();
            client.setConfig(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Moses normal output is something like this: Hypotonie|UNK|UNK|UNK has something to tun.|UNK|UNK|UNK with blood [1111111]
    //We want: "Hypotonie has somethin to tun. with blood"
    public String translation(String input){
        input = pureTranslation(input);
        String[] container = input.split("\\|");
        String output = "";
        int count=2;
        for(String s : container){
            if(s.contains("UNK")){

            }else{
                if (count%2==0){
                    output = output + s;
                }
                count++;
            }
            System.out.println(output);
        }
        output = output.replaceAll("  "," ");
        return output;
    }

    @Override
    public void setFromLanguage(String language) {
        this.fromLanguage = language;
    }

    @Override
    public void setToLanguage(String language) {
        this.toLanguage = language;
    }

    @Override
    public String getFromLanguage() {
        return fromLanguage;
    }

    @Override
    public String getToLanguage() {
        return toLanguage;
    }

    public String pureTranslation(String input){
        String textTranslation = " - No Translation - ";

        // The XML-RPC data type used by mosesserver is <struct>. In Java, this data type can be represented using HashMap.
        HashMap<String,String> mosesParams = new HashMap<String,String>();
        String textToTranslate = new String(input);
        mosesParams.put("text", textToTranslate);
        mosesParams.put("align", "true");
        mosesParams.put("report-all-factors", "true");
        // The XmlRpcClient.execute method doesn't accept Hashmap (pParams). It's either Object[] or List.
        Object[] params = new Object[] { null };
        params[0] = mosesParams;
        // Invoke the remote method "translate". The result is an Object, convert it to a HashMap.
        HashMap result = null;
        try {
            result = (HashMap)client.execute("translate", params);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
        // Print the returned results
        textTranslation = (String)result.get("text");

        /**
         * Moses gibt noch ein paar Informationen über die Zusammensetzung des Übersetzen Textes
         */

        /*
        if (result.get("align") != null){
                Object[] aligns = (Object[])result.get("align");
                System.out.println("Phrase alignments : [Source Start:Source End][Target Start]");
                for ( Object element : aligns) {
                    HashMap align = (HashMap)element;
                    System.out.println("["+align.get("src-start")+":"+align.get("src-end")+"]["+align.get("tgt-start")+"]");
                }
            }
         */



        return textTranslation;
    }
/*
    public String test(String text){
        String textTranslation = "Hallo";
        try {
            // Create an instance of XmlRpcClient
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://localhost:8080/RPC2"));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            // The XML-RPC data type used by mosesserver is <struct>. In Java, this data type can be represented using HashMap.
            HashMap<String,String> mosesParams = new HashMap<String,String>();
            String textToTranslate = new String(text);
            mosesParams.put("text", textToTranslate);
            mosesParams.put("align", "true");
            mosesParams.put("report-all-factors", "true");
            // The XmlRpcClient.execute method doesn't accept Hashmap (pParams). It's either Object[] or List.
            Object[] params = new Object[] { null };
            params[0] = mosesParams;
            // Invoke the remote method "translate". The result is an Object, convert it to a HashMap.
            HashMap result = (HashMap)client.execute("translate", params);
            // Print the returned results
            textTranslation = (String)result.get("text");
            System.out.println("Input : "+textToTranslate);
            System.out.println("Translation : "+textTranslation);
            if (result.get("align") != null){
                Object[] aligns = (Object[])result.get("align");
                System.out.println("Phrase alignments : [Source Start:Source End][Target Start]");
                for ( Object element : aligns) {
                    HashMap align = (HashMap)element;
                    System.out.println("["+align.get("src-start")+":"+align.get("src-end")+"]["+align.get("tgt-start")+"]");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return textTranslation;
    }
*/
}
