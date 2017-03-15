import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketManager implements Runnable{
    protected Socket socket = null;
    private String filename = "file.apk";
    
    public String getFilename() {
		return filename;
	}
	
    public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public SocketManager(Socket socket) {
        this.socket = socket;
    
    }
    public void run() {
    	retrieveFile();
    }
	private void retrieveFile() {
    	InputStream in = null;
        OutputStream out = null;
        try {
            in = socket.getInputStream();
        } catch (IOException ex) {
            System.out.println("Can't get socket input stream.");
            System.exit(0);
        }
 
        try {
            out = new FileOutputStream(filename);
        } catch (FileNotFoundException ex) {
            System.out.println("Output File not found.");
            System.exit(0);
        }
 
        try{
            byte[] bytes = new byte[16*1024];
            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
            out.close();
            in.close();
            socket.close();
            System.out.println("Finished communication with " + socket.getRemoteSocketAddress().toString());
        } catch (IOException e){
             System.out.println("Error during reception");
             System.exit(0);
        }
	}
}