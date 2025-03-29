package org.httpserver.socket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.ServerSocket;


public  class SocketFactory {

    public ServerSocket createServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }
    public ServerSocket createServerSocket(int port, int backlog) throws IOException{
        return new ServerSocket(port, backlog);
    }
    public ServerSocket createServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException{
        return new ServerSocket(port, backlog, bindAddr);
    }
    public Socket createClientSocket(String host, int port) throws IOException {
        return new Socket(host, port);
    }
    public Socket createClientSocket(String host, int port, InetAddress localAddr, int localPort) throws IOException {
        return new Socket(host, port, localAddr, localPort);
    }

}