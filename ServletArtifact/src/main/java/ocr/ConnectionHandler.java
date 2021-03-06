package ocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.validator.UrlValidator;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import com.google.gson.Gson;

import ocr.data.ImageDataHolder;
import ocr.data.ParsedResult;
import ocr.data.PublicOcrDataHolder;


@MultipartConfig
public class ConnectionHandler extends HttpServlet implements Servlet {

	final static int EXIT_CODE_FAIL = -1;
	final static int EXIT_CODE_SUCCESS = 1;
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        /*String pageTitle = "My Page Title";
        request.setAttribute("title", pageTitle);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    	*/
    	
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    	try{
    		String jsonText;
    		String filename = "/tmp/" + UUID.randomUUID() + ".jpg";
    		
    		writeImage(request, filename);
    		
    		jsonText = getJsonFromPublicOCR(filename);
    		List<String> imageTextList = getText(jsonText);
    		LinkedList<String> outputList = removeBadText(imageTextList);
    		
    		int exit_code = EXIT_CODE_FAIL;
    		if(!outputList.isEmpty()){
    			exit_code = EXIT_CODE_SUCCESS;
    		}
    		
    		jsonText = buildJson(outputList, exit_code);
    		
    		
    		returnJsonMessage(jsonText, response);
    		
    	}catch(Exception e){
    		response.getWriter().write(e.getMessage());
    	}
    	
    }
    private void writeImage(HttpServletRequest request, String outputFilename){

    	try {
			for(Part part: request.getParts()){
				
				InputStream fileContent = part.getInputStream();
				OutputStream os = new FileOutputStream(outputFilename);
			     
			    byte[] buffer = new byte[1024];
			    int bytesRead;
			    //read from is to buffer
			    while((bytesRead = fileContent.read(buffer)) !=-1){
			        os.write(buffer, 0, bytesRead);
			    }
				fileContent.close();
			    os.flush();
			    os.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private String getJsonFromPublicOCR(String filename){
    	String ocrUrl = "https://api.ocr.space/Parse/Image";
    	String apiKey = "apikey";
    	String apiValue = "helloworld";
    	String langVar = "language";
    	String language = "eng";
    	String overlayVar = "isOverlayRequired";
    	String setOverlay = "true";
    	File image = new File(filename);
    	
    	try{
            //httpd variables
            SSLContextBuilder sslbuilder = new SSLContextBuilder();
            sslbuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslbuilder.build());
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

            HttpPost uploadFile = new HttpPost(ocrUrl);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody(apiKey, apiValue);
            builder.addTextBody(langVar, language);
            builder.addTextBody(overlayVar, setOverlay);
            builder.addBinaryBody("file", image, ContentType.APPLICATION_OCTET_STREAM, "file.jpg");
            HttpEntity multipart = builder.build();

            uploadFile.setEntity(multipart);

            CloseableHttpResponse response = httpClient.execute(uploadFile);
            HttpEntity responseEntity = response.getEntity();

            if(responseEntity.getContentLength() == 0){
                return null;
            }

            StringBuilder sb = new StringBuilder();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(responseEntity.getContent()), 65728);
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String jsonMessage = sb.toString();
            
            return jsonMessage;
        }
        catch (IOException e) { e.printStackTrace(); }
        catch (Exception e) { e.printStackTrace(); }
		return null;
    }
    
    private String buildJson(List<String> imageLinks, int exitCode){
    	ImageDataHolder holder = new ImageDataHolder();
    	
    	holder.setTextList(imageLinks);
    	holder.setExitCode(exitCode);
    	
    	Gson gson = new Gson();
    	return gson.toJson(holder);
    	
    }

    private LinkedList<String> removeBadText(List<String> textList){
    	LinkedList<String> outputList = new LinkedList<String>();
    	
    	for(int i = 0; i < textList.size();i++){
    		StringTokenizer st = new StringTokenizer(textList.get(i));
    		while(st.hasMoreTokens()){
    			String text = st.nextToken();
	    		if(URLChecker.isValid(text)){
	    			outputList.add(text);
	    		}
    		}
    	}
    	
    	return outputList;
    }
    
    private List<String> getText(String jsonMessage){
    	Gson gson = new Gson();
    	PublicOcrDataHolder holder = 
    			gson.fromJson(jsonMessage, PublicOcrDataHolder.class);
    	
    	LinkedList<String> textList = new LinkedList<String>();
    	Iterator<ParsedResult> resultIterator= holder.getParsedResults().iterator();
    	
    	while(resultIterator.hasNext()){
    		textList.add(resultIterator.next().getParsedText());
    	}
    	
    	return textList;
    }
    
    private void returnJsonMessage(String jsonMessage, HttpServletResponse response){
    	try {
			PrintWriter out = response.getWriter();
			
    		out.println(jsonMessage);
	    		
	    	out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
