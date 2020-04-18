class Main{
	
	public static void main(String[] args){
		
		FileServer fs = new FileServer("http://10.0.13.14");
		Client client = new Client("http://10.0.13.14");
		
		fs.addToNamingServer("10.0.13.5");
	
		if (client.getIP("Benny.txt")){
			System.out.println("IP has been found");
	
		}
		else{
			System.err.println("IP was NOT found!");
		}
	
	}

}
