package com.example.devquest;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

public final class PasswordToggle {

    private PasswordToggle() {

    }

    @SuppressLint("ClickableViewAccessibility")
    public static void attach(EditText editText) {

        setPasswordVisible(editText, false);

        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() != MotionEvent.ACTION_UP) return false;

            Drawable eye = editText.getCompoundDrawablesRelative()[2];
            if (eye == null) return false;

            boolean rtl = editText.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
            boolean touchedEye;
            if (rtl) {
                int eyeEnd = editText.getPaddingLeft() + eye.getIntrinsicWidth();
                touchedEye = event.getX() <= eyeEnd;
            } else {
                int eyeStart = editText.getWidth() - editText.getPaddingRight() - eye.getIntrinsicWidth();
                touchedEye = event.getX() >= eyeStart;
            }
            if (!touchedEye) return false;

            boolean isHidden = editText.getTransformationMethod() instanceof PasswordTransformationMethod;
            setPasswordVisible(editText, isHidden);

            v.performClick();
            return true;
        });
    }

    private static void setPasswordVisible(EditText editText, boolean visible) {
        int cursor = Math.max(editText.getSelectionEnd(), 0);

        editText.setTransformationMethod(visible
                ? HideReturnsTransformationMethod.getInstance()
                : PasswordTransformationMethod.getInstance());
        editText.setSelection(cursor);

        Drawable start = editText.getCompoundDrawablesRelative()[0];
        Drawable eye = ContextCompat.getDrawable(editText.getContext(),
                visible ? R.drawable.dq_ic_eye_off : R.drawable.dq_ic_eye);
        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(start, null, eye, null);
    }
}