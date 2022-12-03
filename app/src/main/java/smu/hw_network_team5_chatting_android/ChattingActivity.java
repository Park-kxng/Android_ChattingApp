package smu.hw_network_team5_chatting_android;

import static smu.hw_network_team5_chatting_android.MainActivity.userName;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity {
    private ListView mList;
    private ArrayList<String> arrayList;
    private MyCustomAdapter mAdapter;
    //public static MyCustomAdapter mAdapter;
    private Client mClient;
    // 로그인 이름 입력 부분
    private View dialogView;
    EditText editTextLoginName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        
        // 네트워크 연결 위해 필요 부분 ▼
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        

        arrayList = new ArrayList<String>();

        final EditText editText = (EditText) findViewById(R.id.editText);
        Button send = (Button)findViewById(R.id.send_button);

        //relate the listView from java to the one created in xml
        mList = (ListView)findViewById(R.id.list);
        mAdapter = new MyCustomAdapter(this, arrayList);
        mList.setAdapter(mAdapter);

        // connect to the server
        new connectTask().execute("");
        // 연결 후 다이얼로그 입력 받기
        //loginDialogShow();
        /*
        Button enterButton = findViewById(R.id.buttonEnter);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //sends the message to the server
                if (mClient != null) {
                    mClient.sendMessage("확인$$"+userName+"$$");
                }

                //refresh the list
                mAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });
        */
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = editText.getText().toString();
                //add the text in the arrayList
                //arrayList.add("c: " + message);

                //sends the message to the server
                if (mClient != null) {
                    mClient.sendMessage(message);
                }

                //refresh the list
                mAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

    }

    public class connectTask extends AsyncTask<String,String,Client> {

        @Override
        protected Client doInBackground(String... message) {
            Log.e("=============doInBackground: ", " doInBackground 들어옴");
            //we create a Client object and
            mClient = new Client(new Client.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    Log.e("============ publishProgress: ", message);
                    publishProgress(message);
                }
            });
            mClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.e("============ onProgressUpdate: ", values[0]);
            //in the arrayList we add the messaged received from server
            arrayList.add(values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            mAdapter.notifyDataSetChanged();
        }
    }
    
    /////////
    public void loginDialogShow(){
        // 로그인 이름만으로 하는 부분에서 다이얼로그로 입력 받기
        dialogView =View.inflate(getApplicationContext(), R.layout.dialog, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(getApplicationContext());
        dlg.setTitle("사용자 정보 입력");
        dlg.setView(dialogView); // 대화상자에 뷰를 세팅
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 확인 눌렀을 때 
                editTextLoginName = dialogView.findViewById(R.id.editTextLoginName);
                String loginName= editTextLoginName.getText().toString();
                if (loginName != null){
                    String message = "확인$$" + loginName + "$$";
                    //add the text in the arrayList
                    //arrayList.add("c: " + message);

                    //sends the message to the server
                    if (mClient != null) {
                        mClient.sendMessage(message);
                    }

                    //refresh the list
                    mAdapter.notifyDataSetChanged();
                }else{
                    Toast myToast = Toast.makeText(getApplicationContext(),"이름을 제대로 입력해주세요", Toast.LENGTH_SHORT);
                    myToast.show();
                }
            }
        });
        //무조건 입력해야해서 추소 버튼은 없음
        dlg.show();

    }
}