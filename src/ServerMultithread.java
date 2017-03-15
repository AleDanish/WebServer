import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class ServerMultithread implements Runnable {
	protected int serverPort;
	protected ServerSocket serverSocket = null;
	protected boolean hasStopped = false;
	protected Thread movingThread = null;

	public ServerMultithread(int port) {
		this.serverPort = port;
	}

	public void run() {
		synchronized (this) {
			this.movingThread = Thread.currentThread();
		}
		opnSvrSocket();
		while (!hasStopped()) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				System.out.println("Connection arrived from " + socket.getRemoteSocketAddress().toString());
			} catch (IOException e) {
				if (hasStopped()) {
					System.out.println("Server has Stopped...Please check");
					return;
				}
				throw new RuntimeException("Client cannot be connected - Error", e);
			}
			new Thread(new SocketManager(socket)).start();
		}
		System.out.println("Server has Stopped...Please check");
	}

	private synchronized boolean hasStopped() {
		return this.hasStopped;
	}

	public synchronized void stop() {
		hasStopped = true;
		try {
			serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Server can not be closed - Please check error", e);
		}
	}

	private void opnSvrSocket() {
		try {
			System.out.println("The server is listening on port " + serverPort + "...");
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Not able to open the port" + serverPort, e);
		}
	}

}