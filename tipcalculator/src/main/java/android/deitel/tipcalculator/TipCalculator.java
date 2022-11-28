package android.deitel.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

//main Activity class for Tip Calculator
public class TipCalculator extends AppCompatActivity {

    //constants used when saving/restoring state
    private static final String BILL_TOTAL = "BILL_TOTAL";
    private static final String CUSTOM_PERCENT = "CUSTOM_PERCENT";

    private double currentBillTotal; // bill amount entered by user
    private int currentCustomPercent; // tip % set wuth the SeekBar
    private EditText tip10EditText; // displays 10% tip
    private EditText total10EditText; // displays total with 10% tip
    private EditText tip15EditText; // displays 15% tip
    private EditText total15EditText; // displays total with 15% tip
    private EditText tip20EditText; // displays 20% tip
    private EditText total20EditText; // displays total with 20% tip
    private EditText billEditText; // accepts user input for bill total
    private TextView customTipTextView; // displays custom tip percentage
    private EditText tipCustomEditText; // displays custom tip amount
    private EditText totalCustomEditText; // displays total with custom tip

    // Called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState); // call superclass's version
        setContentView(R.layout.activity_main); // inflate the GUI

        if (savedInstanceState == null){
            currentBillTotal = 0.0;
            currentCustomPercent = 18;
        } else {
            currentBillTotal = savedInstanceState.getDouble(BILL_TOTAL);
            currentCustomPercent = savedInstanceState.getInt(CUSTOM_PERCENT);
        }

        tip10EditText = (EditText) findViewById(R.id.tip10PlainText);
        tip15EditText = (EditText) findViewById(R.id.tip15PlainText);
        tip20EditText = (EditText) findViewById(R.id.tip20PlainText);
        total10EditText = (EditText) findViewById(R.id.total10PlainText);
        total15EditText = (EditText) findViewById(R.id.total15PlainText);
        total20EditText = (EditText) findViewById(R.id.total20PlainText);

        customTipTextView = (TextView) findViewById(R.id.customTipTextView);
        tipCustomEditText = (EditText) findViewById(R.id.tipCustomPlainText);
        totalCustomEditText = (EditText) findViewById(R.id.totalCustomPlainText);
        billEditText = (EditText) findViewById(R.id.billPlainText);

        billEditText.addTextChangedListener(billEditTextWatcher);

        SeekBar customSeekBar = (SeekBar) findViewById(R.id.customSeekBar);
        customSeekBar.setOnSeekBarChangeListener(customSeekBarListener);
    }

    private void updateStandard(){
        double tenPercentTip = currentBillTotal * .1;
        double tenPercentTotal = currentBillTotal + tenPercentTip;
        tip10EditText.setText(String.format("%.02f", tenPercentTip));
        total10EditText.setText(String.format("%.02f", tenPercentTotal));

        double fifteenPercentTip = currentBillTotal * .15;
        double fifteenPercentTotal = currentBillTotal + fifteenPercentTip;
        tip15EditText.setText(String.format("%.02f", fifteenPercentTip));
        total15EditText.setText(String.format("%.02f", fifteenPercentTotal));

        double twentyPercentTip = currentBillTotal * .2;
        double twentyPercentTotal = currentBillTotal + twentyPercentTip;
        tip20EditText.setText(String.format("%.02f", twentyPercentTip));
        total20EditText.setText(String.format("%.02f", twentyPercentTotal));
    }

    private void updateCustom(){
        customTipTextView.setText(currentCustomPercent + "%");
        double customTipAmount = currentBillTotal * currentCustomPercent * .01;
        double customTotalAmount = currentBillTotal + customTipAmount;
        tipCustomEditText.setText(String.format("%.02f", customTipAmount));
        totalCustomEditText.setText(String.format("%.02f", customTotalAmount));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putDouble(BILL_TOTAL, currentBillTotal);
        outState.putInt(CUSTOM_PERCENT, currentCustomPercent);
    }

    private OnSeekBarChangeListener customSeekBarListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            currentCustomPercent = seekBar.getProgress();
            updateCustom();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private TextWatcher billEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                currentBillTotal = Double.parseDouble(s.toString());
            } catch (NumberFormatException e){
                currentBillTotal = 0.0;
            }

            updateStandard();
            updateCustom();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}