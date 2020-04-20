class Main{
	
	public static void main(String[] args){
		
		FileServer fs = new FileServer("http://10.0.13.14:8081");
		Client client = new Client("http://10.0.13.14:8081");
		
		fs.addToNamingServer("10.0.13.5");
		
		fs.addToNamingServer("10.0.13.14");

		fs.joinNamingServer();

		fs.addToNamingServer("10.0.13.5");
		
		//fs.removeNode("10.0.13.5");
		if (client.getIP("someRandomImage.gif") && client.getIP("Ronny.txt") && client.getIP("testImage.JPEG") && client.getIP("sunnyVillage.tif") && client.getIP("bruno.json") && client.getIP("robert.xml")){

			System.out.println("IP adresses have been found");
	
		}
		else{
			System.err.println("IP was NOT found!");
		}
	
	}

}
