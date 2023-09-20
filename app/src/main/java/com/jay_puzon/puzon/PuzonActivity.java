package com.jay_puzon.puzon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import javax.xml.transform.Result;

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
    EditText UserBetAmt, UserBet1, UserBet3, UserBet2;
    TextView ResultNum1, ResultNum2, ResultNum3, Multiplier, WINORLOSE, RemainingMoney;
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
        Multiplier = findViewById(R.id.multiplier);
        RemainingMoney = findViewById(R.id.remainingMoney);
        WINORLOSE = findViewById(R.id.win_or_lose);

        EditText[] UserBets = new EditText[] {
                findViewById(R.id.userBet1),
                findViewById(R.id.userBet2),
                findViewById(R.id.userBet3)
        };

        for (int j = 0; j < UserBets.length; j++) {
            final int k = j;
            UserBets[j].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.i("userBet"+(k+1), charSequence.toString());
                    if (charSequence.toString().equals("")) {
                        return;
                    }
                    switch (k) {
                        case 0: {
                            userBet1 = Integer.valueOf(charSequence.toString());
                        }; break;
                        case 1: {
                            userBet2 = Integer.valueOf(charSequence.toString());
                        }; break;
                        case 2: {
                            userBet3 = Integer.valueOf(charSequence.toString());
                        }; break;
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        UserBetAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("userBetAmt", charSequence.toString());
                if (charSequence.toString().equals("")) {
                    return;
                }
                userBetAmt = Integer.valueOf(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        BtnBet.setOnClickListener(view -> {
            switch (state) {
                case IDLE:
                    try {
                        Bet();
                    } catch (Exception e) {
                        Log.e("E", e.toString());
                    }
                    break;
                case BETTING: {
                    // check first if all constraints pass before playing
                    if (PlayChecks() == true) {
                        try {
                            Play();
                        } catch (Exception e) {
                            Log.e("E", e.toString());
                        }
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
        // but only clear them if the user had not entered any bet yet
        // otherwise, do not clear them

        if (!UserBet1.getText().equals("0") ||
                !UserBet1.getText().equals("-")) {
            if (userBet1 != 0) {
                UserBet1.setText("0");
            }
        }
        if (!UserBet2.getText().equals("0") ||
                !UserBet2.getText().equals("-")) {
            if (userBet2 != 0) {
                UserBet2.setText("0");
            }
        }
        if (!UserBet3.getText().equals("0") ||
                !UserBet3.getText().equals("-")) {
            if (userBet3 != 0) {
                UserBet3.setText("0");
            }
        }

        UserBet1.setEnabled(true);
        UserBet2.setEnabled(true);
        UserBet3.setEnabled(true);

        // clear the bet amount textbox
        UserBetAmt.setText("0");
        UserBetAmt.setEnabled(true);

        // set button texts
        BtnBet.setText("PLAY");
    }

    boolean PlayChecks() {
        boolean passedChecks = true;

        // check if the user had entered their betting numbers
        Log.i("Bet1", userBet1+"");
        Log.i("Bet2", userBet2+"");
        Log.i("Bet3", userBet3+"");

        if (userBet1 == 0 || userBet1 > 9) {
            passedChecks = false;
            Log.e("Bet1", "Did not passed!");
            UserBet1.setError("Numbers 1-9 only");
        }
        if (userBet2 == 0 || userBet2 > 9) {
            passedChecks = false;
            Log.e("Bet2", "Did not passed!");
            UserBet2.setError("Numbers 1-9 only");
        }
        if (userBet3 == 0 || userBet3 > 9) {
            passedChecks = false;
            Log.e("Bet3", "Did not passed!");
            UserBet3.setError("Numbers 1-9 only");
        }
        if (passedChecks == false) {
            Log.e("Bets", "Did not passed!");
            Toast.makeText(getBaseContext(), "Invalid bet. Enter numbers 1-9 only", Toast.LENGTH_SHORT).show();
            return passedChecks;
        }

        // check if the user had entered a bet amt
        if (userBetAmt <= 0) {
            passedChecks = false;
            Log.e("BetAmount", "Did not passed!");
            Toast.makeText(getBaseContext(), "Please enter a bet amount", Toast.LENGTH_SHORT).show();
            UserBetAmt.setError("Enter a bet amount");
            return passedChecks;
        }

        // check if the remaining balance is enough for the bet amount
        if (userBetAmt > remainingMoney) {
            passedChecks = false;
            Log.e("BetAmount", "Insufficient balance!");
            Toast.makeText(getBaseContext(), "Insufficient balance", Toast.LENGTH_SHORT).show();
            UserBet1.setError("Insufficient balance");
            return passedChecks;
        }

        return passedChecks;
    }

    void Play() {
        try {
            // generate winning numbers
            genWinningNums();

            // compare if the winning numbers is equivalent to the user's bet
            if (resultNums[0] == userBet1 && resultNums[1] == userBet2 && resultNums[2] == userBet3) {
                // set game state
                state = GameState.IDLE;
                winOrLose = WinOrLose.WIN;

                // increase money
                remainingMoney = remainingMoney + (userBetAmt * multiplier);

                // increase multiplier
                multiplier++;

                Log.i("Remaining Money", remainingMoney+"");

                // set ui
                Multiplier.setText(multiplier + "x");
                RemainingMoney.setText(remainingMoney+"");
                BtnBet.setText("SET");
                WINORLOSE.setText("WIN");
            } else {
                // set game state
                state = GameState.IDLE;
                winOrLose = WinOrLose.LOSE;

                // reset multiplier
                multiplier = 2;

                // decrease money
                remainingMoney = remainingMoney - userBetAmt;
                Log.i("Remaining Money", remainingMoney+"");

                // set ui
                Multiplier.setText("2x");
                RemainingMoney.setText(remainingMoney+"");
                BtnBet.setText("SET");
                WINORLOSE.setText("LOSE");
            }
        } catch (Exception e) {
            Log.e("Play Error: ", e.toString());
        }

        // disable the bet number textboxes
        UserBet1.setEnabled(false);
        UserBet2.setEnabled(false);
        UserBet3.setEnabled(false);
        UserBetAmt.setEnabled(false);

        // reset if the remaining money is 0
        if (remainingMoney == 0) {
            Reset();
        }
    }

    void Reset() {
         try {
             // reset the multiplier
             multiplier = 2;

             // reset the winning numbers
             resultNums = new int[]{0, 0, 0};

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

             // reset the money
             remainingMoney = 1000;

             // reset multiplier
             multiplier = 2;

             // reset the ui
             Multiplier.setText(multiplier+ "x");
             UserBet1.setText("0");
             UserBet1.setEnabled(false);
             UserBet2.setText("0");
             UserBet2.setEnabled(false);
             UserBet3.setText("0");
             UserBet3.setEnabled(false);
             UserBetAmt.setText("0");
             UserBetAmt.setEnabled(false);
             ResultNum1.setText("-");
             ResultNum2.setText("-");
             ResultNum3.setText("-");
             RemainingMoney.setText(remainingMoney+"");
             WINORLOSE.setText("-");
         } catch (Exception e) {
             Log.e("RESET Error", e.toString());
         }
    }

    void genWinningNums() {
        try {
            Random rd = new Random();
            // generate luck
            int luck = rd.nextInt(5);
            Log.i("Luck", luck+"");

            // check if luck is 4
            // then we must generate the winning numbers, same as the user bet
            if (luck == 4) {
                resultNums = new int[]{
                        userBet1,
                        userBet2,
                        userBet3
                };
            } else {
                // otherwise, generate random numbers
                resultNums = new int[]{
                        Numbers[rd.nextInt(Numbers.length-1)],
                        Numbers[rd.nextInt(Numbers.length-1)],
                        Numbers[rd.nextInt(Numbers.length-1)],
                };
            }

            ResultNum1.setText(resultNums[0]+"");
            ResultNum2.setText(resultNums[1]+"");
            ResultNum3.setText(resultNums[2]+"");
        } catch (Exception e) {
            Log.e("genWinningNums Error: ", e.toString());
        }
    }

    int[] Numbers = {
            1, 2, 3, 4, 5, 6, 7, 8, 9
    };
}