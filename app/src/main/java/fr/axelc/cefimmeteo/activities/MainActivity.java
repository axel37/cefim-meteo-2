package fr.axelc.cefimmeteo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.axelc.cefimmeteo.utils.Util;
import fr.axelc.cefimmeteo.R;


public class MainActivity extends AppCompatActivity {
    private FloatingActionButton mFloatingButtonFavorite;
    private EditText mEditTextMessage;

    private LinearLayout mLinearLayoutMain;
    private TextView mTextViewNoConnection;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mLinearLayoutMain = findViewById(R.id.linear_layout_current_city);
        mTextViewNoConnection = findViewById(R.id.text_view_no_connection);
        mFloatingButtonFavorite = findViewById(R.id.floating_action_button_favorite);
        mEditTextMessage = findViewById(R.id.edit_text_message);

        mFloatingButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FavoriteActivity.class);
                intent.putExtra(Util.KEY_MESSAGE, mEditTextMessage.getText().toString());
                startActivity(intent);
            }
        });

        if (Util.isActiveNetwork(mContext)) {
            Log.d("TAG", "Je suis connecté");
        } else {
            Log.d("TAG", "Je ne suis pas connecté");
            updateViewNoConnection();
        }
    }

    public void updateViewNoConnection() {
        mLinearLayoutMain.setVisibility(View.INVISIBLE);
        mFloatingButtonFavorite.setVisibility(View.INVISIBLE);
        mTextViewNoConnection.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}