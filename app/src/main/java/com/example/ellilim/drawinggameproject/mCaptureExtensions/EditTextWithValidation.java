package com.example.ellilim.drawinggameproject.mCaptureExtensions;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.example.ellilim.drawinggameproject.R;

import java.util.Objects;

//Validation already inside of the EditText, with icons being shown
public class EditTextWithValidation extends AppCompatEditText{

    int ammountOfLetters = 2;
    boolean firstTime = true;

    public EditTextWithValidation(Context context) {
        super(context);
        validation();
    }

    public EditTextWithValidation(Context context, AttributeSet attrs) {
        super(context, attrs);
        validation();
    }

    public EditTextWithValidation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        validation();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        validation();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        validation();
    }

    //Change the ammount of letters we need for correct validation
    public void setAmmountOfLetters(int ammount){
        ammountOfLetters = ammount;
    }

    //check which one we need to show
    public void validation(){
        if(firstTime) {
            this.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Accent), PorterDuff.Mode.SRC_ATOP);
            this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            firstTime = false;
        } else if(Objects.requireNonNull(this.getText()).toString().trim().length() < ammountOfLetters) {
            this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_24dp, 0);
        } else {
            this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_circle_24dp, 0);
        }
    }
}
