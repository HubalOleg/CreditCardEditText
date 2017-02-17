package com.oleg.hubal.creditcardedittext;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 15.02.2017.
 */

public class CreditCardEditText extends LinearLayout {

    private static final String TAG = "CreditCardEditText";
    private static int mLayoutMargin;
    private static int mEditTextPadding;

    private List<EditText> mEditTextList;

    private int mCurrentPosition = 0;
    private boolean isClearPrevious = false;

    private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                requestCurrentFocus();
            }
        }
    };

    private OnKeyListener mOnKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            int length = ((EditText) v).getText().length();
            if (length == 0 && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                if (isClearPrevious) {
                    clearPreviousLastNumber();
                }
                isClearPrevious = true;
            }
            return false;
        }
    };

    public CreditCardEditText(Context context) {
        super(context);
        init();
    }

    public CreditCardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mEditTextList = new ArrayList<>();
        setOrientation(HORIZONTAL);
        mLayoutMargin = dpToPx(3);
        mEditTextPadding = 0;
    }

    public String getCardNumber() {
        String creditCardNumber = "";
        for (EditText editText : mEditTextList) {
            creditCardNumber += editText.getText().toString();
        }
        return creditCardNumber;
    }

    public void setCardNumber(String cardNumber) {
        int startPosition = 0;

        for (EditText editText : mEditTextList) {
            int endPosition = editText.getMaxEms() + startPosition;

            if (endPosition >= cardNumber.length()) {
                endPosition = cardNumber.length();
            }

            String actualNumber = cardNumber.substring(startPosition, endPosition);
            editText.setText(actualNumber);
            startPosition = endPosition;
            editText.setSelection(actualNumber.length());

            if (startPosition == cardNumber.length()) {
                return;
            }
        }
    }

    public void setTextPattern(int[] pattern) {
        for (int number : pattern) {
            addEditText(number);
        }
    }

    private void addEditText(int length) {
        EditText editText = new EditText(getContext());
        editText.setMaxEms(length);
        editText.setClickable(false);
        editText.setLongClickable(false);
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setBackgroundResource(R.drawable.edit_text_background);
        editText.setOnFocusChangeListener(mOnFocusChangeListener);
        editText.addTextChangedListener(new CreditCardTextWatcher(length));
        editText.setOnKeyListener(mOnKeyListener);
        editText.setPadding(mEditTextPadding, mEditTextPadding, mEditTextPadding, mEditTextPadding);
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(inputFilters);

        LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) length);
        layoutParams.setMargins(mLayoutMargin, mLayoutMargin, mLayoutMargin, mLayoutMargin);
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
            isClearPrevious = false;
            int length = s.length();

            if (length == mLengthLimit) {
                onFocusNext();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
