package com.oleg.hubal.creditcardedittext;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 17.02.2017.
 */

public class CCNumEditText extends LinearLayout {

    private static final String TAG = "CreditCardEditText";
    private static int mLayoutMargin;
    private static int mEditTextPadding;

    private List<EditText> mEditTextList;
    private int[] mTextPattern;

    private int mCurrentPosition = 0;
    private boolean isTextChanged = false;

    private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int position = (int) v.getTag();

            if (isPreviousFull(position)) {
                mCurrentPosition = position;
            } else {
                requestCurrentFocus();
            }
        }
    };

    private OnKeyListener mOnKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            return false;
        }
    };

    public CCNumEditText(Context context) {
        super(context);
        init();
    }

    public CCNumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CCNumEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mEditTextList = new ArrayList<>();
        setOrientation(HORIZONTAL);
        mLayoutMargin = dpToPx(3);
        mEditTextPadding = 0;
    }

    private boolean isPreviousFull(int position) {
        if (position == 0) {
            return false;
        } else {
            EditText previousEditText = mEditTextList.get(position - 1);
            int maxLength = mTextPattern[position - 1];
            String cardText = previousEditText.getText().toString();
            return cardText.length() == maxLength;
        }
    }

    public String getCardNumber() {
        String creditCardNumber = "";
        for (EditText editText : mEditTextList) {
            creditCardNumber += editText.getText().toString();
        }
        return creditCardNumber;
    }

    public void setCardNumber(String cardNumber) {
        clearNumber();

        int startPosition = 0;

        for (EditText editText : mEditTextList) {

            int endPosition = editText.getMaxEms() + startPosition;

            if (endPosition >= cardNumber.length()) {
                endPosition = cardNumber.length();
            }

            String actualNumber = cardNumber.substring(startPosition, endPosition);
            editText.setText(actualNumber);
            startPosition = endPosition;
//            editText.setSelection(actualNumber.length());

            if (startPosition == cardNumber.length()) {
                return;
            }
        }
    }

    public void clearNumber() {
        for (EditText editText : mEditTextList) {
            editText.setText("");
        }
    }

    public void setTextPattern(int[] pattern) {
        mTextPattern = pattern;
        for (int i = 0; i < pattern.length; i++) {
            createEditText(pattern[i], i);
        }

    }

    private void createEditText(int length, int position) {
        EditText editText = new EditText(getContext());
        editText.setTag(position);
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setBackgroundResource(R.drawable.edit_text_background);
        editText.setOnFocusChangeListener(mOnFocusChangeListener);
        editText.addTextChangedListener(new CreditCardTextWatcher(length));
        editText.setOnKeyListener(mOnKeyListener);

        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(inputFilters);

        LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) length);
        layoutParams.setMargins(mLayoutMargin, mLayoutMargin, mLayoutMargin, mLayoutMargin);
        editText.setPadding(mEditTextPadding, mEditTextPadding, mEditTextPadding, mEditTextPadding);
        editText.setLayoutParams(layoutParams);
        mEditTextList.add(editText);
        addView(editText);
    }

    private void onFocusNext() {
        if (mCurrentPosition < mEditTextList.size() - 1) {
            mCurrentPosition++;
            requestCurrentFocus();
        }
    }

    private void clearPreviousLastNumber() {
        if (mCurrentPosition > 0) {
            mCurrentPosition--;
            EditText mCurrentEditText = mEditTextList.get(mCurrentPosition);

            String text = mCurrentEditText.getText().toString();
            int substringLength = text.length() - 1;
            mCurrentEditText.setText(text.substring(0, substringLength));
            mCurrentEditText.setSelection(substringLength);

            requestCurrentFocus();
        }
    }

    private void requestCurrentFocus() {
        mEditTextList.get(mCurrentPosition).requestFocus();
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    class CreditCardTextWatcher implements TextWatcher {

        private int mLengthLimit;

        CreditCardTextWatcher(int lengthLimit) {
            mLengthLimit = lengthLimit;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
