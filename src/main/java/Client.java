import java.net.*;
import java.io.*;

class Client{

	private HttpURLConnection con;
	private String address;


	public Client(String address){
		this.address = address;
	}

	private void openConnection(String request, String method){
		
		try{
			URL url = new URL(request);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(method);
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
		}catch(IOException e){e.printStackTrace();}
	}

	public Boolean getIP(String fileName){
		
		String request = address + "/fileLocation?filename="+fileName;
		
		StringBuffer content = new StringBuffer();		
		try{
			openConnection(request, "GET");

			BufferedReader in = new BufferedReader(
  			new InputStreamReader(con.getInputStream()));
			String inputLine;
		
			while ((inputLine = in.readLine()) != null) {
    				content.append(inputLine);
			}
		
			if (con.getResponseCode() != 200){
				System.out.println("Could not resolve filename");
				return false;
			}
			in.close();
			con.disconnect();
		
		}catch(IOException e){e.printStackTrace();}

		//Ping the node
		String IP = content.toString();
		
		try{
			InetAddress addr = InetAddress.getByName(IP); 
			
			if (addr.isReachable(5000)){
				return true;
			}
			return false;

		}catch(Exception e){e.printStackTrace();}
		
		return false;		
	}

}
