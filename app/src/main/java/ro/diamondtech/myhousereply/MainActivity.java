package ro.diamondtech.myhousereply;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ro.diamontech.myhousereply.R;

/**
 * Created by user1 on 25/01/2018.
 */

public class MainActivity extends AppCompatActivity {

    //for Log
    private final String TAG = MainActivity.class.getSimpleName();

    //aici ajunge codul utilizator mCodeUser care va fi transferat in toate celalte pagini
    //inca nu este implementata procedura de stabilire a cod user si este doar direct introdusa
    //dar care sa fie egala cu code_user introdus in FakeDataUtils


    //Here reach mCodeUser user code which will be transferred in all activities
    //is not yet implemented the procedure for determining the user code and is only directly entered in @string.pref_code_user_default
    //but which is equal to the code entered in the FakeDataUtils_user (!)
    public String iniCodeUser ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the user code for all activities
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getResources().getString(R.string.pref_code_user_key), iniCodeUser);

        //button for check connection (not implemented yet....), login (not implemented yet....) and show the house and rooms status
        final Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context  context = MainActivity.this;
                //test if is Demo (only blank user and pass
                EditText editTextUser = (EditText) findViewById(R.id.editTextUser);
                EditText editTextPass = (EditText) findViewById(R.id.editTextPass);
                String txtuser = editTextUser.getText().toString();
                String txtpass = editTextPass.getText().toString();

                String msgDemo = getResources().getString(R.string.msg_err_login);
                   if (testlogin(txtuser,txtpass)) {
                       Class statusActivity = StatusActivity.class;
                       Intent intentToStartActivityStatus = new Intent(context, statusActivity);
                       startActivity(intentToStartActivityStatus);
                   } else {

                       Toast.makeText(context,msgDemo,Toast.LENGTH_SHORT).show();

                   }
            }
        }

        );

    }

    //Test user (not implemented yet....)
    private boolean testlogin(String user,String pass){
        //only demo
        if (user.equals("") & pass.equals("")) return true;
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_page,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //start settings (incomplete....)
        if (id == R.id.mmain_settings){
            Context context = this;
            Class settingsClass = SettingsMainActivity.class;
            Intent intentToStartSettingsActivity = new Intent(context,settingsClass);
            startActivity(intentToStartSettingsActivity,null);
        }

        return super.onOptionsItemSelected(item);
    }
}
