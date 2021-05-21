/**************************************************************************************************
* This Activity will handle user authentication with OAuth2.0 for use of Google's YouTube API.
***************************************************************************************************/
package com.example.ytdl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccountActivity extends AppCompatActivity {

    //Class View Widgets
    Button BUTTON_Test1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //--Assign Widgets--//
        BUTTON_Test1 = findViewById(R.id.BUTTONTest1);
    }

    /**
     * View Accessible function to start the Main Activity of the Application
     * @param v = View Parameter for View Event Functions
     */
    public void openMainActivity(View v){
        Intent launch_acitvity = new Intent(this,MainActivity.class);
        startActivity(launch_acitvity);
    }

}
