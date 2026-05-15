package cz.vsb.fei.donkeykong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.ServerSocket;

@SpringBootApplication
public class DonkeykongDbApplication {

	public static void main(String[] args) {
		if (System.getProperty("server.port") == null && System.getenv("SERVER_PORT") == null) {
			System.setProperty("server.port", String.valueOf(findPort(8080)));
		}
		SpringApplication.run(DonkeykongDbApplication.class, args);
	}

	private static int findPort(int port) {
		while (true) {
			try (ServerSocket ignored = new ServerSocket(port)) {
				return port;
			} catch (IOException e) {
				port++;
			}
		}
	}

}
