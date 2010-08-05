package com.phonegap;

import java.io.*;
import java.net.*;
import java.util.*;

public class CallbackServer implements Runnable {

	Thread serverThread;
	LinkedList<Response> responseQueue;
	boolean active = true;
	int port = 8080;
	
	
	
	CallbackServer()
	{
		responseQueue = new LinkedList<Response>();
		serverThread = new Thread(this);
		serverThread.start();
	}
	
	public int getPort()
	{
		return port;
	}
	
	public void addResponse(Response r)
	{
		responseQueue.add(r);
	}
	
	public void stopServer()
	{
		active = false;
	}
	
	public void startServer()
	{
		try {
			String request;
			ServerSocket waitSocket = new ServerSocket(port);
			
			while(active)
			{
				Socket connection = waitSocket.accept();
				BufferedReader xhrReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				DataOutputStream output = new DataOutputStream(connection.getOutputStream());
				String hostname = connection.getInetAddress().getHostAddress();
				request = xhrReader.readLine();
				//if(request.contains("GET") && hostname.contains("localhost"))
				if(request.contains("GET"))
				{
					if(responseQueue.isEmpty())
					{
						output.writeBytes("{ \"data\" : 0 }");
					}
					else
					{
						Response rsp = responseQueue.poll();
						output.writeBytes(rsp.getJson());
					}
				}
				
				output.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		startServer();
	}		
	
}
