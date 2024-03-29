package server.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Simple Web Server for learning purposes. Handles one client connection at a time and sends back a static HTML page as response.
 */
public class SimpleWebServer {
	ServerSocket s;

	/**
	 * Creates and returns server socket.
	 * 
	 * @param port
	 *            Server port.
	 * @return created server socket
	 * @throws Exception
	 *             Exception thrown, if socket cannot be created.
	 */
	protected ServerSocket getServerSocket(int port) throws Exception {
		return new ServerSocket(port);
	}

	/**
	 * Starts web server and handles web browser requests.
	 * 
	 * @param port
	 *            Server port(ex. 80, 8080)
	 * @throws Exception
	 *             Exception thrown, if server fails to start.
	 */
	public void runServer(int port) throws Exception {
		s = getServerSocket(port);

		while (true) {
			try {
				Socket serverSocket = s.accept();
				handleRequest(serverSocket);
			} catch (IOException e) {
				System.out.println("Failed to start server: " + e.getMessage());
				System.exit(0);
				return;
			}
		}
	}

	/**
	 * Handles web browser requests and returns a static web page to browser.
	 * 
	 * @param s
	 *            socket connection between server and web browser.
	 */
	public void handleRequest(Socket s) {
		BufferedReader is;     // inputStream from web browser
		PrintWriter os;        // outputStream to web browser
		String request;        // Request from web browser

		try {
			String webServerAddress = s.getInetAddress().toString();
			System.out.println("Accepted connection from " + webServerAddress);
			is = new BufferedReader(new InputStreamReader(s.getInputStream()));

			request = is.readLine();
			System.out.println("Server recieved request from client: " + request);

			os = new PrintWriter(s.getOutputStream(), true);
			os.println("HTTP/1.0 200");
			os.println("Content-type: text/html");
			os.println("Server-name: myserver");
			String response = "<html><head>" + "<title>Simpl Web Page</title></head>\n" + "<h1>Congratulations!!!</h1>\n" + "<h3>This page was returned by " + webServerAddress + "</h3>\n" + "<p>This is the first page hosted by your web server.\n</p>"
					+ "Visit <A HREF=\"http://www.techwiki.ordak.org\"> http://www.techwiki.ordak.org</A> for more sample codes.\n" + "</html>\n";
			os.println("Content-length: " + response.length());
			os.println("");
			os.println(response);
			os.flush();
			os.close();
			s.close();
		} catch (IOException e) {
			System.out.println("Failed to send response to client: " + e.getMessage());
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return;
	}

	public static void main(String[] args) {
		SimpleWebServer webServer = new SimpleWebServer();
		try {
			webServer.runServer(8080);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
