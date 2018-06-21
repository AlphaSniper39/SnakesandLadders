package com.cs17b003.snakesandladders;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView[] place = new ImageView[100];
    ImageView[] playerIV = new ImageView[4];
    int[] playerId = new int[4];
    Button playAgain, back;
    int noOfPlayers;
    TableLayout board;
    ImageView dice;
    TextView[] posText = new TextView[4];
    TextView[] playerPos = new TextView[4];
    Random rng = new Random();
    int[] position = new int[4];
    boolean turn, end, flag;
    TextView playMsg;
    int player;
    int dieRoll;
    boolean[] win = new boolean[4];
    int complete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialise();


        ((Button)findViewById(R.id.play)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        dice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!end) && (turn))
                    roll();
            }
        });


        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
            }
        });
    }



    void initialise (){

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;

        player = 0;
        dieRoll = 0;
        complete = 0;

        turn = true;
        end = false;
        flag = true;

        playerId[0] = R.drawable.orange;
        playerId[1] = R.drawable.yellow;
        playerId[2] = R.drawable.black;
        playerId[3] = R.drawable.pink;

        posText[0] = (TextView)findViewById(R.id.first);
        posText[1] = (TextView)findViewById(R.id.second);
        posText[2] = (TextView)findViewById(R.id.third);
        posText[3] = (TextView)findViewById(R.id.fourth);

        playerPos[0] = (TextView)findViewById(R.id.position1);
        playerPos[1] = (TextView)findViewById(R.id.position2);
        playerPos[2] = (TextView)findViewById(R.id.position3);
        playerPos[3] = (TextView)findViewById(R.id.position4);

        playMsg = (TextView)findViewById(R.id.playMsg);
        playMsg.setVisibility(View.GONE);


        for (int j = 0; j < 4;j++) {
            posText[j].setVisibility(View.GONE);
            playerPos[j].setVisibility(View.GONE);
        }

        ((ImageView) findViewById(R.id.imageView)).setVisibility(View.GONE);

        board = (TableLayout) findViewById(R.id.board);
        board.getLayoutParams().height = width;
        board.setVisibility(View.GONE);

        int initResId = R.id.player1;

        for (int i = 0; i < 4; i++){
            playerIV[i] = (ImageView) findViewById(initResId++);
            playerIV[i].setVisibility(View.GONE);

            position[i] = 0;
        }

        for (int i = 0; i < 100; i++) {
            place[i] = board.findViewWithTag(Integer.toString(i+1));
            place[i].getLayoutParams().height = width/10;
            place[i].getLayoutParams().width = width/10;
        }

        dice = (ImageView)findViewById(R.id.dice);
        dice.setVisibility(View.GONE);

        for (int j = 0; j < 4; j++)
            win[j] = false;

        ((TextView)findViewById(R.id.gameOver)).setVisibility(View.GONE);

        (playAgain = (Button)findViewById(R.id.playAgain)).setVisibility(View.GONE);
        (back = (Button)findViewById(R.id.back)).setVisibility(View.GONE);
    }



    void play(){
        if (((RadioButton)findViewById(R.id.two)).isChecked())  noOfPlayers = 2;
        else if (((RadioButton)findViewById(R.id.three)).isChecked())  noOfPlayers = 3;
        else if (((RadioButton)findViewById(R.id.four)).isChecked())  noOfPlayers = 4;

        ((Button)findViewById(R.id.play)).setVisibility(View.GONE);
        ((RadioGroup) findViewById(R.id.playerGroup)).setVisibility(View.GONE);

        ((ImageView) findViewById(R.id.imageView)).setVisibility(View.VISIBLE);
        board.setVisibility(View.VISIBLE);

        for (int i = 0; i < noOfPlayers; i++){
            playerIV[i].setVisibility(View.VISIBLE);
        }

        dice.setVisibility(View.VISIBLE);
        playMsg.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);

        for (int j = 0; j < noOfPlayers; j++) {
            posText[j].setVisibility(View.VISIBLE);
            playerPos[j].setVisibility(View.VISIBLE);
        }

        posText[0].setBackgroundColor(Color.MAGENTA);
    }



    void roll(){

        int r = rng.nextInt(6)+1;

        turn = false;

        posText[player].setBackgroundColor(Color.TRANSPARENT);

        if (position[player]+r <= 100){

            if (position[player] > 0) {

                pieceInPrevPlace();
            }

            else
                playerIV[player].setImageResource(0);

            position[player] += r;
        }

        switch (r){
            case 1:
                dice.setImageResource(R.drawable.one);
                break;

            case 2:
                dice.setImageResource(R.drawable.two);
                break;

            case 3:
                dice.setImageResource(R.drawable.three);
                break;

            case 4:
                dice.setImageResource(R.drawable.four);
                break;

            case 5:
                dice.setImageResource(R.drawable.five);
                break;

            case 6:
                dice.setImageResource(R.drawable.six);
                break;

            default:
        }

        if (position[player] <= 100){
            place[position[player]-1].setImageResource(playerId[player]);
            playerPos[player].setText("" + position[player]);
        }

        playMsg.setText("");

        snakes();
        ladders();

        new CountDownTimer(1500,1000){
            @Override
            public void onFinish() {
                reInitDice();
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();
    }


    void pieceInPrevPlace() {

        int[][] reInit = {{1,2,3}, {2,3,0},{3,0,1},{0,1,2}};

        place[position[player]-1].setImageResource(0);

        for (int i: reInit[player]) {

            if(i < noOfPlayers){
                if (position[player] == position[i])
                    place[position[player]-1].setImageResource(playerId[i]);
            }
        }
    }


    void reInitDice(){
        if(!turn){
            dice.setImageResource(R.drawable.dice);
            turn = true;

            win[player] = (position[player] == 100);

            if(win[player]){
                complete++;
                posText[player].setBackgroundColor(Color.GREEN);
                playerPos[player].setBackgroundColor(Color.GREEN);
            }

            if (complete == (noOfPlayers-1)) {
                ((TextView) findViewById(R.id.gameOver)).setVisibility(View.VISIBLE);
                playAgain.setVisibility(View.VISIBLE);
                end = true;
            }

            while (win[player = (++player) % noOfPlayers]);
            playMsg.setText("Play");

            posText[player].setBackgroundColor(Color.MAGENTA);
        }
    }


    void playAgain () {
        player = 0;
        dieRoll = 0;
        complete = 0;

        turn = true;
        end = false;

        ((ImageView) findViewById(R.id.imageView)).setVisibility(View.GONE);
        board.setVisibility(View.GONE);

        dice.setVisibility(View.GONE);
        playMsg.setVisibility(View.GONE);

        ((TextView)findViewById(R.id.gameOver)).setVisibility(View.GONE);

        for (int j = 0; j < noOfPlayers; j++) {
            posText[j].setVisibility(View.GONE);
            playerPos[j].setVisibility(View.GONE);

            playerPos[j].setText("0");

            posText[j].setBackgroundColor(Color.TRANSPARENT);
            playerPos[j].setBackgroundColor(Color.TRANSPARENT);
        }

        playAgain.setVisibility(View.GONE);
        back.setVisibility(View.GONE);

        ((Button)findViewById(R.id.play)).setVisibility(View.VISIBLE);
        ((RadioGroup) findViewById(R.id.playerGroup)).setVisibility(View.VISIBLE);

        for (int i = 0; i < noOfPlayers; i++){
            if (position[i] > 0){
                place[position[i]-1].setImageResource(0);
                playerIV[i].setImageResource(playerId[i]);
                position[i] = 0;
            }

            playerIV[i].setVisibility(View.GONE);

            win[i] = false;
        }

        dice.setImageResource(R.drawable.dice);
    }


    void snakes(){
        switch (position[player]){
            case 16:

                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 6;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 46:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 25;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 49:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 11;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 62:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 19;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 64:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 60;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 74:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 53;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 89:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 68;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 92:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 88;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 95:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 75;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 99:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 80;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();

            default:
                flag = false;
                break;
        }
    }

    void ladders() {
        switch (position[player]) {
            case 2:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 38;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 7:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 14;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 8:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 31;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 15:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 26;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 21:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 42;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 28:
                new CountDownTimer(500, 1000){
                @Override
                public void onFinish() {
                    place[position[player]-1].setImageResource(0);
                    position[player] = 84;
                    place[position[player]-1].setImageResource(playerId[player]);
                    playerPos[player].setText("" + position[player]);
                    flag = false;
                }

                @Override
                public void onTick(long millisUntilFinished) {

                }
            }.start();
                break;

            case 36:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 44;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 51:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 67;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 71:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 91;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 78:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 98;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            case 87:
                new CountDownTimer(500, 1000){
                    @Override
                    public void onFinish() {
                        place[position[player]-1].setImageResource(0);
                        position[player] = 94;
                        place[position[player]-1].setImageResource(playerId[player]);
                        playerPos[player].setText("" + position[player]);
                        flag = false;
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
                break;

            default:
                flag = false;
                break;
        }
    }
}