package com.jay_puzon.puzon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

enum GameState {
    IDLE,
    BETTING,
}

enum WinOrLose {
    WIN,
    LOSE,
    IDLE
}

public class PuzonActivity extends AppCompatActivity {
    // state
    GameState state = GameState.IDLE;
    WinOrLose winOrLose = WinOrLose.IDLE;
    int[] resultNums = {0, 0, 0};
    int multiplier = 2;
    double remainingMoney = 1000;
    int userBetAmt, userBet1, userBet2, userBet3;

    // ui elements
    EditText UserBet1, UserBet2, UserBet3, UserBetAmt;
    TextView ResultNum1, ResultNum2, ResultNum3, Multiplier, WINORLOSE;
    Button BtnBet, BtnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzon);

        // assign ui elements to xml
        UserBet1 = findViewById(R.id.userBet1);
        UserBet2 = findViewById(R.id.userBet2);
        UserBet3 = findViewById(R.id.userBet3);
        UserBetAmt = findViewById(R.id.userBetAmt);
        ResultNum1 = findViewById(R.id.result1);
        ResultNum2 = findViewById(R.id.result2);
        ResultNum3 = findViewById(R.id.result3);
        BtnBet = findViewById(R.id.bet);
        BtnReset = findViewById(R.id.reset);

        UserBet1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userBet1 = i;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        UserBet2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userBet2 = i;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        UserBet3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userBet3 = i;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        UserBetAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userBetAmt = i;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        BtnBet.setOnClickListener(view -> {
            switch (state) {
                case IDLE:
                    Bet();
                    break;
                case BETTING: {
                    // check first if all constraints pass before playing
                    if (PlayChecks() == true) {
                        Play();
                    } else {
                        Log.e("Error", "PlayChecks did not passed!");
                    }

                    break;
                }

            }
        });

        BtnReset.setOnClickListener(view -> Reset());
    }

    void Bet() {
        // set game state
        state = GameState.BETTING;

        // clear the bet number textboxes
        UserBet1.setText("0");
        UserBet2.setText("0");
        UserBet3.setText("0");

        // clear the bet amount textbox
        UserBetAmt.setText("0");

        // set button texts
        BtnBet.setText("PLAY");
    }

    boolean PlayChecks() {
        boolean passedChecks = true;

        // check if the user had entered their betting numbers
        Log.i("Bet1", userBet1+"");

        if (userBet1 == 0) {
            passedChecks = false;
            Log.e("Bet1", "Did not passed!");
        }
        if (userBet2 == 0) {
            passedChecks = false;
            Log.e("Bet2", "Did not passed!");

        }
        if (userBet3 == 0) {
            passedChecks = false;
            Log.e("Bet3", "Did not passed!");

        }
        if (passedChecks == false) {
            Log.e("Bets", "Did not passed!");
            Toast.makeText(getBaseContext(), "Please complete your bet", Toast.LENGTH_SHORT);
        }

        // check if the user had entered a bet amt
        if (userBetAmt == 0) {
            passedChecks = false;
            Log.e("BetAmount", "Did not passed!");
            Toast.makeText(getBaseContext(), "Please enter a bet amount", Toast.LENGTH_SHORT);
        }

        // check if the remaining balance is enough for the bet amount
        if (userBetAmt < remainingMoney) {
            passedChecks = false;
            Log.e("BetAmount", "Insufficient balance!");
            Toast.makeText(getBaseContext(), "Insufficient balance", Toast.LENGTH_SHORT);
        }

        return passedChecks;
    }

    void Play() {
        // generate winning numbers
        genWinningNums();

        remainingMoney -= userBetAmt;

        // compare if the winning numbers is equivalent to the user's bet
        if (resultNums[0] == userBet1 && resultNums[1] == userBet2 && resultNums[2] == userBet3) {
            // set game state
            state = GameState.IDLE;
            winOrLose = WinOrLose.WIN;

            // increase multiplier
            multiplier++;

            // set button texts
            BtnBet.setText("SET");
            WINORLOSE.setText("WIN");
        } else {
            // set game state
            state = GameState.IDLE;
            winOrLose = WinOrLose.LOSE;

            // reset multiplier
            multiplier = 2;

            // set button texts
            BtnBet.setText("SET");
            WINORLOSE.setText("LOSE");
        }
    }

    void Reset() {
        // reset the multiplier
        multiplier = 2;

        // reset the winning numbers
        resultNums = new int[] {0, 0, 0};

        // reset the bet amount
        userBetAmt = 0;

        // reset the bet numbers
        userBet1 = 0;
        userBet2 = 0;
        userBet3 = 0;

        // reset the win or lose result
        winOrLose = WinOrLose.IDLE;

        // reset the game state
        state = GameState.IDLE;


        // reset the ui
        Multiplier.setText("2");
        UserBet1.setText("-");
        UserBet2.setText("-");
        UserBet3.setText("-");
        UserBetAmt.setText("0");
        WINORLOSE.setText("");
    }

    void genWinningNums() {
        resultNums = new int[]{
                (int) (Math.random() * 10),
                (int) (Math.random() * 10),
                (int) (Math.random() * 10)
        };

        ResultNum1.setText(resultNums[0]);
        ResultNum2.setText(resultNums[1]);
        ResultNum3.setText(resultNums[2]);
    }
}