import java.net.*;
import java.io.*;

	
class FileServer{

	private HttpURLConnection con;
	private String address;

	public FileServer(String namingServerIP){
		this.address = namingServerIP;
	}

	
	private void openConnection(String request, String method) {

		try{
			URL url = new URL(request);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(method);
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
		}catch(IOException e){e.printStackTrace();}
	}

	private int httpQuery(String request, String method){
		
		openConnection(request, method);
                con.disconnect();

                int response = -1;
                try{
                        response = con.getResponseCode();
                }catch (IOException e){e.printStackTrace();}
		
		return response;
	}
	
	public int joinNamingServer(){
		
		String request = address + "/addNode";
		
		return httpQuery(request, "PUT");	
	}

	public int addToNamingServer(String nodeAddress){
	
		String request = address + "/addNode?name="+nodeAddress;
		
		return httpQuery(request, "PUT");
	}	
	
	public int removeNode(String nodeAddress){
		String request = address + "/deleteNode?name=" + nodeAddress;
		
		return httpQuery(request, "DELETE");
	}

	 	
	
}	
