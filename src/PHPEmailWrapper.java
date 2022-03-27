import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PHPEmailWrapper {
    String body,email,subject;
    public PHPEmailWrapper(String body,String email){
        this.body = body;
        this.email = email;
        this.subject = "Message From Advanced Library Automation System";
    }
    public PHPEmailWrapper(){
    }
    public PHPEmailWrapper(String body,String email,String subject){
        this.body = body;
        this.email = email;
        this.subject = subject;
    }
    
    public String execute(){
        try {
            URL url = new URL("http://localhost/alas/email/handler.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            Map<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("body", body);
            params.put("subject", subject);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (postData.length() != 0) {
                    postData.append('&');
                }
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            connection.setDoOutput(true);


        try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
            writer.write(postDataBytes);
            writer.flush();
            writer.close();

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String line;
                content = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            //Print for debug
            //System.out.println(content.toString());
            return content.toString();
        } catch (Exception e){
            return "401";
        }finally {
            connection.disconnect();
        }
        }catch (Exception e){
            return "401";
        }
    }

    public String generateOTP(){
        int otp = new Random().nextInt(900000) + 100000;
        body = body + "<br> Your OTP is :- <b>" + otp + "</b>";
        String st = execute();
        if (st.contains("200")){
            return String.valueOf(otp);
        }else {
            return "402";
        }

    }
/*
    public static void main(String[] args) {
        PHPEmailWrapper a = new PHPEmailWrapper("This is a test email which carries your password. From ALAS","devjuneja43@gmail.com","This is a Optional Subject!!!");
        System.out.println(a.generateOTP());
    }*/
}
