package com.example.csci350program2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GameOverActivity extends AppCompatActivity {

    public static final String G = "GAMEOVER";

    private TextView winnerview;
    private TextView winner;
    private Button againplay;

    private String winnertxt;
    private String losertxt;

    private EditText searchet;
    private Button searchbtn;
    private TextView playerlist;
    private TextView searchresults;
    private TextView topplayers;

    private int wins;
    private int losses;
    private boolean namefound;
    ConnectFourData db; //SQLite Database
    Cursor c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        //Sets Status Bar at top to Red
        Window neo = getWindow();
        neo.setStatusBarColor(Color.rgb(102,0,153)); //purple

        //Sets Title Text and Color
        setTitle("Connect Four by Julio Madrigal");
        Spannable text = new SpannableString(getTitle());
        text.setSpan(new ForegroundColorSpan(Color.rgb(255,153,0)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); //orange
        setTitle(text);

        //Sets Title Background Color
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c2833"))); //black

        Intent intent = getIntent(); //gets both winner and loser

        winnertxt = intent.getStringExtra(MainActivity.WINNERNAME); //winner's name
        losertxt = intent.getStringExtra(MainActivity.LOSERNAME); //loser's name

        winnerview = (TextView) findViewById(R.id.winnertext);
        winner = (TextView) findViewById(R.id.winnername); //where it displays the winner
        againplay = (Button) findViewById(R.id.playagain);
        againplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //////////////////////////////////////////////////////////////Optional: Go Back to Name Activity and send names, On Activity for Result NameActivity?
                Intent myIntent = new Intent(GameOverActivity.this, NameActivity.class);

               // myIntent.putExtra(NAME,username);
                //if(cpuok == 0) {
                    //myIntent.putExtra(NAME2, username2);
                //}
               // myIntent.putExtra(OKCPU, cpuok); //determines if a cpu is being used or not

                //myIntent.putExtra(COLOR,color); //using normal(1) or inverse(2) colors?

                startActivity(myIntent);  // FOR TO
            }
        });
        winner.setText(winnertxt);
        ///////////////////////////////////////////////////////////////////////////////////////////
        searchet = (EditText) findViewById(R.id.selecttext);
        searchbtn = (Button) findViewById(R.id.selectbutton);
        playerlist = (TextView) findViewById(R.id.playerlist);
        searchresults = (TextView) findViewById(R.id.searchresult);
        topplayers = (TextView) findViewById(R.id.topplayer);

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the EditText's Name and search it within the data base
                String nameentered = searchet.getText().toString();
                Cursor n = db.SearchName(nameentered);
                while (n.moveToNext()) {
                    Log.i(G, "Search Result: " + n.getString(1) + " Wins: " + n.getInt(2) + " Losses: " + n.getInt(3));
                    searchresults.setText(n.getString(1) + " Wins: " + n.getInt(2) + " Losses: " + n.getInt(3));
                }
            }
        });

        if(winnertxt.equals("DRAW")) //Draw, so no one is entered
        {
            winnerview.setText(View.INVISIBLE); //Doesn't say "The Winner is: "
        }
        else {
            db = new ConnectFourData(this);
            Log.i(G, "About to Insert into Database...");
            if (winnertxt.equals("CPU")) {
                //Do Nothing
            } else //Winner entered into DataBase
            {
                Log.i(G,"Updating Winner in Database...");
                namefound = false;
                c = db.SelectAll();
                while (c.moveToNext()) {
                    if(c.getString(1).equals(winnertxt))
                    {
                        Log.i(G,"Found Player! Wins: " + c.getInt(2));
                        wins = c.getInt(2)+1;
                        db.UpdateWins(winnertxt, wins);
                        Log.i(G,"Found Player! UpdatedWins: " + c.getInt(2));
                        namefound = true;
                        //break;
                    }
                }
                Log.i(G,"Finished Loop: " + namefound);
                if(namefound == false) { //New Data Entry for new Player
                    db.Insert(winnertxt, 1, 0);
                }
            }

            if (losertxt.equals("CPU")) {
                //Do Nothing
            } else { //Loser entered into Database
                Log.i(G,"Updating Loser in Database...");
                namefound = false;
                c = db.SelectAll();
                while (c.moveToNext()) {
                    if(c.getString(1).equals(losertxt))
                    {
                        Log.i(G,"Found Player! Losses: " + c.getInt(3));
                        losses = c.getInt(3)+1;
                        db.UpdateLosses(losertxt, losses);
                        Log.i(G,"Found Player! UpdatedLosses: " + c.getInt(3));
                        namefound = true;
                        //break;
                    }
                }
                if(namefound == false) { //New Data Entry for new player
                    db.Insert(losertxt, 0, 1);
                }
            }

            Cursor k;
            k = db.SortWins();
            int counter = 0;
            while (k.moveToNext() && counter != 5) {
                Log.i(G, "Ordered Wins: " + k.getString(0) + " Wins: " + k.getInt(1) + " Losses: " + k.getInt(2));
                topplayers.setText(topplayers.getText()+"\n"+k.getString(0)+" Wins: " + k.getInt(1) + " Losses: " + k.getInt(2));
                counter++;
            }

            c = db.SelectAll();

            while (c.moveToNext()) {
                Log.i(G, c.getString(1) + " Wins: " + c.getInt(2) + " Losses: " + c.getInt(3));
                playerlist.setText(playerlist.getText()+"\n"+c.getString(1)+" Wins: " + c.getInt(2) + " Losses: " + c.getInt(3));
            }
            //playerlist.setMovementMethod(new ScrollingMovementMethod());
        }//End if not a draw and enter into database
    }
}
