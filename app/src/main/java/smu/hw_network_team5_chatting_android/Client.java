package smu.hw_network_team5_chatting_android;




import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;

public class Client {

    private String serverMessage;
    public static  String SERVERIP ; // your computer IP
    // address
    public static final int SERVERPORT = 1004; // yujeong
    //public static final int SERVERPORT = 7000; // git
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    PrintWriter out;
    BufferedReader in;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages
     * received from server
     */
    public Client(OnMessageReceived listener) {
        mMessageListener = listener;

    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message
     *            text entered by client
     */
    public void sendMessage(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    public void stopClient() {
        mRun = false;
    }

    public void run() {

        mRun = true;

        try {

            // here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
            Log.e("serverAddr", serverAddr.toString());
            Log.e("TCP Client", "C: Connecting...");

            // create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVERPORT);
            Log.e("TCP Server IP", SERVERIP);
            try {

                // send the message to the server
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);

                Log.e("TCP Client", "C: Sent.");

                Log.e("TCP Client", "C: Done.");

                // receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));

                // in this while the client listens for the messages sent by the
                // server
                while (mRun) {
                    serverMessage = in.readLine();
                    Log.e("=============Client Get DATA: ", serverMessage);
                    // 여기에서 받는 부분
                    //byte[] Data = new byte[100];
                    //String btNAME = null; // 버튼이름
                    //String id = null;   // ID
                    //String chat = null;   // 채팅 내용
                    //String serverMember = null;  // server에서 보내준 member
                    //StringTokenizer st, st2;
                    // data에서 버튼이름, 아이디, 채팅내용, member로 한번 나눈후 member를 다시 나눠야하기 떄문에 stringtokenizer두개 선언

                    if (serverMessage != null && mMessageListener != null) {
                        // call the method messageReceived from MyActivity class
                        //String DATA = new String(Data, 0, Integer.parseInt(serverMessage), "UTF-8"); // 데이터를 받았을때 UTF-8로 디코딩한 문자열을 얻음
                        Log.e("TCP Client Get DATA: ", serverMessage);

                        mMessageListener.messageReceived(serverMessage);
                        //mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '"
                        + serverMessage + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                // the socket must be closed. It is not possible to reconnect to
                // this socket
                // after it is closed, which means a new socket instance has to
                // be created.
                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }

    // Declare the interface. The method messageReceived(String message) will
    // must be implemented in the MyActivity
    // class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}