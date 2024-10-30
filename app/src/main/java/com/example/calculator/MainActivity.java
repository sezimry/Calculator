package com.example.calculator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvExpression, tvResult;
    MaterialButton btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnAC, btnDot;
    MaterialButton btnC, btnOpenBrackets, btnCloseBrackets, btnDiv, btnMul, btnSub, btnAdd, btnEqual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //устанавливаем Insets для корректного отображения на экранах с вырезами
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //инициализация текстовых полей
        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);

        //инициализация кнопок
        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btnDot = findViewById(R.id.btnDot);
        btnAC = findViewById(R.id.btnAC);
        btnC = findViewById(R.id.btnC);
        btnAdd = findViewById(R.id.btnAdd);
        btnSub = findViewById(R.id.btnSub);
        btnDiv = findViewById(R.id.btnDiv);
        btnMul = findViewById(R.id.btnMul);
        btnOpenBrackets = findViewById(R.id.btnOpenBrackets);
        btnCloseBrackets = findViewById(R.id.btnCloseBrackets);
        btnEqual = findViewById(R.id.btnEqual);

        //установка слушателей для кнопок
        initButton(btn0);
        initButton(btn1);
        initButton(btn2);
        initButton(btn3);
        initButton(btn4);
        initButton(btn5);
        initButton(btn6);
        initButton(btn7);
        initButton(btn8);
        initButton(btn9);
        initButton(btnDot);
        initButton(btnAC);
        initButton(btnC);
        initButton(btnAdd);
        initButton(btnSub);
        initButton(btnDiv);
        initButton(btnMul);
        initButton(btnOpenBrackets);
        initButton(btnCloseBrackets);
        initButton(btnEqual);

        //устанавливаем стартовое значение в tvExpression
        tvExpression.setText("");
    }

    //инициализация кнопок и установка слушателя событий
    void initButton(MaterialButton button) {
        button.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String btnText = button.getText().toString();
        String data = tvExpression.getText().toString();

        //очистка всех данных
        if (btnText.equals("AC")) {
            tvExpression.setText("");
            tvResult.setText("");
            return;
        }

        // Удаление последнего символа
        if (btnText.equals("C")) {
            if (data.length() != 0 && !data.equals("0")) {
                data = data.substring(0, data.length() - 1);
            }
            tvExpression.setText(data);
            return;
        }

        //вычисление результата по нажатию "="
        if (btnText.equals("=")) {
            String result = tvResult.getText().toString();
            if (!result.equals("")) {
                tvExpression.setText(result);
            }
            return;
        }

        //добавление текста кнопки к выражению
        data += btnText;
        tvExpression.setText(data);

        //вычисление результата
        String finalResult = evaluateExpression(data);
        if (!finalResult.equals("Error")) {
            tvResult.setText(finalResult);
        } else {
            tvResult.setText("");
        }
        Log.i("result", finalResult);
    }

    //оценка математического выражения с помощью Rhino
    private String evaluateExpression(String expression) {
        Context rhino = Context.enter();
        rhino.setOptimizationLevel(-1); //отключаем оптимизацию для мобильных устройств
        try {
            Scriptable scope = rhino.initStandardObjects();
            String result = rhino.evaluateString(scope, expression, "JavaScript", 1, null).toString();

            //форматирование результата
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            return decimalFormat.format(Double.parseDouble(result));
        } catch (Exception e) {
            return "Error";
        } finally {
            Context.exit();
        }
    }
}