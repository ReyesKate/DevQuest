package com.example.devquest.Activities;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.devquest.Models.QuestionBank;
import com.example.devquest.Models.QuestionModel;
import com.example.devquest.R;
import com.example.devquest.UserRepository;
import com.example.devquest.databinding.ActivityQuestionBinding;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    private static final int QUESTIONS_PER_QUIZ = 15;
    private static final int MAX_HINTS = 3;

    ArrayList<QuestionModel> list = new ArrayList<>();
    private int count = 0;
    private int position = 0;
    private int score = 0;
    private boolean typedMode = false;
    private boolean fillMode = false;
    private int hintsLeft = MAX_HINTS;
    private boolean hintUsed = false;
    private long startTime;
    private Dialog timeoutDialog;
    private Dialog hintDialog;
    private String language = "C#";
    private String setName = "Multiple Choices";
    CountDownTimer timer;
    ActivityQuestionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        startTime = System.currentTimeMillis();

        setName = getIntent().getStringExtra("set");
        if (setName == null) {
            setName = "Multiple Choices";
        }

        language = getIntent().getStringExtra("language");
        if (language == null) {
            language = "C#";
        }

        String type;
        if (setName.equals("Fill in the Blank")) {
            typedMode = true;
            fillMode = true;
            type = "fill";
        } else if (setName.equals("Identification")) {
            typedMode = true;
            type = "identification";
        } else {
            type = "mc";
        }

        list = QuestionBank.load(language, type, QUESTIONS_PER_QUIZ);

        resetTimer();
        timer.start();

        if (typedMode) {
            count = 4;
            binding.optionContainer.setVisibility(View.GONE);
            binding.btnSubmit.setVisibility(View.VISIBLE);
            if (fillMode) {
                binding.blankInput.setVisibility(View.VISIBLE);
            } else {
                binding.answerInput.setVisibility(View.VISIBLE);
            }
        }

        for (int i = 0; i < 4; i++) {
            binding.optionContainer.getChildAt(i)
                    .setOnClickListener(v -> checkAnswer((Button) v));
        }

        binding.btnSubmit.setOnClickListener(v -> checkTypedAnswer());

        binding.answerInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkTypedAnswer();
                return true;
            }
            return false;
        });

        binding.blankInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkTypedAnswer();
                return true;
            }
            return false;
        });

        playAnimation(binding.question, 0, list.get(position).getQuestion());

        binding.btnBack.setOnClickListener(v -> {
            if (timer != null) {
                timer.cancel();
            }
            finish();
        });

        binding.btnShare.setOnClickListener(v -> binding.btnNext.performClick());

        binding.btnNext.setOnClickListener(v -> goToNextQuestion());

        binding.hintContainer.setOnClickListener(v -> useHint());
        updateHintButton();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if (timeoutDialog != null && timeoutDialog.isShowing()) {
            timeoutDialog.dismiss();
        }
        if (hintDialog != null && hintDialog.isShowing()) {
            hintDialog.dismiss();
        }
    }

    private void resetTimer() {

        timer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished /
                        1000));
            }

            @Override
            public void onFinish() {

                showTimeoutDialog();

            }
        };

    }

    private void showTimeoutDialog() {

        if (isFinishing() || isDestroyed()) {
            return;
        }

        if (hintDialog != null && hintDialog.isShowing()) {
            hintDialog.dismiss();
        }

        if (typedMode) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            }
        }

        timeoutDialog = new Dialog(QuestionActivity.this);
        timeoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timeoutDialog.setContentView(R.layout.timeout_dialog);
        timeoutDialog.setCancelable(false);

        Window window = timeoutDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        timeoutDialog.findViewById(R.id.btnNextQuestion).setOnClickListener(v -> {
            timeoutDialog.dismiss();
            goToNextQuestion();
        });

        timeoutDialog.show();

    }

    private void updateHintButton() {

        binding.tvHintCount.setText(getString(R.string.fraction_format, hintsLeft, MAX_HINTS));

        boolean canUse = hintsLeft > 0 && !hintUsed && !binding.btnNext.isEnabled();
        binding.hintContainer.setEnabled(canUse);
        binding.hintContainer.setAlpha(canUse ? 1f : 0.5f);
    }

    private void useHint() {

        if (hintsLeft <= 0 || hintUsed || binding.btnNext.isEnabled()) return;

        String hint = QuestionBank.getHint(list.get(position).getQuestion());
        if (hint == null || hint.isEmpty()) {
            Toast.makeText(this, R.string.no_hint, Toast.LENGTH_SHORT).show();
            return;
        }

        hintsLeft--;
        hintUsed = true;
        updateHintButton();
        showHintDialog(hint);
    }

    private void showHintDialog(String hint) {

        if (isFinishing() || isDestroyed()) {
            return;
        }

        if (typedMode) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            }
        }

        hintDialog = new Dialog(QuestionActivity.this);
        hintDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        hintDialog.setContentView(R.layout.hint_dialog);
        hintDialog.setCancelable(true);

        Window window = hintDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        ((TextView) hintDialog.findViewById(R.id.tvHintMessage)).setText(hint);
        ((TextView) hintDialog.findViewById(R.id.tvHintLeft)).setText(
                getResources().getQuantityString(R.plurals.hints_left, hintsLeft, hintsLeft));

        hintDialog.findViewById(R.id.btnHintOk).setOnClickListener(v -> hintDialog.dismiss());

        hintDialog.show();
    }

    private void goToNextQuestion() {

        if (timer != null) {
            timer.cancel();
        }

        if (typedMode) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            }
        }

        binding.btnNext.setEnabled(false);
        binding.btnNext.setAlpha((float) 0.3);
        enableOption(true);
        hintUsed = false;
        updateHintButton();
        position++;

        if (position == list.size()) {
            UserRepository.saveResult(language, setName, score, list.size());

            Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("total", list.size());
            intent.putExtra("time", System.currentTimeMillis() - startTime);
            startActivity(intent);
            finish();
            return;
        }

        timer.start();

        if (typedMode) {
            count = 4;
            resetTypedInput();
        } else {
            count = 0;
        }

        playAnimation(binding.question, 0, list.get(position).getQuestion());

    }

    private void playAnimation(View view, int value, String data) {

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(@NonNull Animator animation) {
                        if (value == 0 && count < 4) {
                            String option = "";

                            if (count == 0) option = list.get(position).getOptionA();
                            else if (count == 1) option = list.get(position).getOptionB();
                            else if (count == 2) option = list.get(position).getOptionC();
                            else if (count == 3) option = list.get(position).getOptionD();

                            playAnimation(binding.optionContainer.getChildAt(count), 0, option);
                            count++;
                        }
                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animation) {
                        if (value == 0) {
                            if (fillMode && view == binding.question) {

                                ((TextView) view).setText(data.replace("__", blankSpaces()));
                                waitForLayoutThenPlaceBlank((TextView) view);
                            } else {
                                ((TextView) view).setText(data);
                            }
                            binding.totalQuestion.setText(
                                    getString(R.string.fraction_format, position + 1, list.size()));
                            view.setTag(data);
                            playAnimation(view, 1, data);
                        }
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animation) { }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animation) { }
                });
    }

    private void enableOption(boolean enable) {

        for (int i = 0; i < 4; i++) {
            binding.optionContainer.getChildAt(i).setEnabled(enable);

            if (enable) {
                binding.optionContainer.getChildAt(i).setBackgroundResource(R.drawable.btn_opt);
            }
        }

    }

    private void checkAnswer(Button selectedOption) {

        if (timer != null) {
            timer.cancel();
        }

        binding.btnNext.setEnabled(true);
        binding.btnNext.setAlpha(1);
        updateHintButton();

        String correct = list.get(position).getCorrectAnswer();

        if (selectedOption.getText().toString().equals(correct)) {
            score++;
            selectedOption.setBackgroundResource(R.drawable.right_answ);
        } else {
            selectedOption.setBackgroundResource(R.drawable.wrong_answ);

            Button correctOption = binding.optionContainer.findViewWithTag(correct);
            if (correctOption != null) {
                correctOption.setBackgroundResource(R.drawable.right_answ);
            }
        }

        enableOption(false);
    }

    private EditText activeInput() {
        return fillMode ? binding.blankInput : binding.answerInput;
    }

    private String blankSpaces() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            sb.append('\u2007');
        }
        return sb.toString();
    }

    private void waitForLayoutThenPlaceBlank(final TextView tv) {

        tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                placeBlank(tv);
            }
        });

    }

    private void placeBlank(TextView tv) {

        Layout layout = tv.getLayout();
        if (layout == null) {
            return;
        }

        String text = tv.getText().toString();
        int start = text.indexOf('\u2007');
        int end = text.lastIndexOf('\u2007') + 1;

        if (start < 0) {
            return;
        }

        int line = layout.getLineForOffset(start);
        float x1 = layout.getPrimaryHorizontal(start);
        float x2 = layout.getPrimaryHorizontal(end);
        int lineTop = layout.getLineTop(line);
        int lineBottom = layout.getLineBottom(line);

        int width = Math.max((int) (x2 - x1), 1);
        int height = lineBottom - lineTop;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.gravity = Gravity.TOP | Gravity.START;
        params.leftMargin = tv.getLeft() + tv.getPaddingLeft() + (int) x1;
        params.topMargin = tv.getTop() + tv.getPaddingTop() + lineTop;

        binding.blankInput.setLayoutParams(params);
        binding.blankInput.animate().alpha(1).setStartDelay(650).setDuration(300).start();

    }

    private void checkTypedAnswer() {

        EditText input = activeInput();
        String answer = input.getText().toString().trim();

        if (answer.isEmpty()) {
            if (fillMode) {
                binding.answerFeedback.setText(R.string.type_answer_in_blank);
                binding.answerFeedback.setVisibility(View.VISIBLE);
            } else {
                input.setError(getString(R.string.type_answer_first));
            }
            return;
        }

        if (timer != null) {
            timer.cancel();
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        }

        String correct = list.get(position).getCorrectAnswer();
        boolean isCorrect = false;

        for (String accepted : correct.split("\\|")) {
            if (answer.equalsIgnoreCase(accepted.trim())) {
                isCorrect = true;
                break;
            }
        }

        if (isCorrect) {
            score++;
            if (fillMode) {
                binding.blankInput.setBackgroundResource(R.drawable.blank_correct);
                binding.blankInput.setTextColor(Color.parseColor("#34C77B"));
                binding.answerFeedback.setVisibility(View.GONE);
            } else {
                binding.answerInput.setBackgroundResource(R.drawable.right_answ);
            }
        } else {
            if (fillMode) {
                binding.blankInput.setBackgroundResource(R.drawable.blank_wrong);
                binding.blankInput.setTextColor(Color.parseColor("#FF6B6B"));
            } else {
                binding.answerInput.setBackgroundResource(R.drawable.wrong_answ);
            }
            binding.answerFeedback.setText(
                    getString(R.string.correct_answer_format, correct.replace("|", " / ")));
            binding.answerFeedback.setVisibility(View.VISIBLE);
        }

        input.setEnabled(false);
        binding.btnSubmit.setEnabled(false);
        binding.btnSubmit.setAlpha((float) 0.5);

        binding.btnNext.setEnabled(true);
        binding.btnNext.setAlpha(1);
        updateHintButton();
    }

    private void resetTypedInput() {

        binding.answerInput.setText("");
        binding.answerInput.setError(null);
        binding.answerInput.setEnabled(true);
        binding.answerInput.setBackgroundResource(R.drawable.btn_opt);

        binding.blankInput.setText("");
        binding.blankInput.setEnabled(true);
        binding.blankInput.setAlpha(0);
        binding.blankInput.setBackgroundResource(R.drawable.blank_underline);
        binding.blankInput.setTextColor(Color.WHITE);

        binding.btnSubmit.setEnabled(true);
        binding.btnSubmit.setAlpha(1);
        binding.answerFeedback.setVisibility(View.GONE);

    }
}