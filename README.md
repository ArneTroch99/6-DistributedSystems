# 6-DistributedSystems-NamingServer
Naming Server of the group project for 6-Distributes Systems for group Real Life Save Icons

Run the Naming Server in IDE or package with Maven and run the JAR. 

The Naming Server runs standalone, waiting for incoming node communication. Nodes are stored with their ID's (created with a hashing algorithm) in a map.
The Server responds to HTTP requests for node IP based on ID or for node ID based on node IP.
