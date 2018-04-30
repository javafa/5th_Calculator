package com.kodonho.android.layoutbasic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        for(int i=0; i<10; i++){
            int id = getResources().getIdentifier("btn"+i, "id", getPackageName());
            findViewById(id).setOnClickListener(this);
        }
        findViewById(R.id.btnPlus).setOnClickListener(this);
        findViewById(R.id.btnMinus).setOnClickListener(this);
        findViewById(R.id.btnMultiply).setOnClickListener(this);
        findViewById(R.id.btnDivide).setOnClickListener(this);
        findViewById(R.id.btnCalc).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String temp = result.getText().toString();
        if("0".equals(temp)) temp = "";
        switch(v.getId()){
            case R.id.btn0: temp += "0"; break;
            case R.id.btn1: temp += "1"; break;
            case R.id.btn2: temp += "2"; break;
            case R.id.btn3: temp += "3"; break;
            case R.id.btn4: temp += "4"; break;
            case R.id.btn5: temp += "5"; break;
            case R.id.btn6: temp += "6"; break;
            case R.id.btn7: temp += "7"; break;
            case R.id.btn8: temp += "8"; break;
            case R.id.btn9: temp += "9"; break;
            case R.id.btnPlus: temp += "+"; break;
            case R.id.btnMinus: temp += "-"; break;
            case R.id.btnMultiply: temp += "*"; break;
            case R.id.btnDivide: temp += "/"; break;
            case R.id.btnCancel: temp = "0"; break;
            case R.id.btnCalc:
                temp = calc(temp);
                break;
        }
        result.setText(temp);
    }

    public String calc(String str){
        String result = "";
        // 코딩
        // 46 + 95 - 32 * 5
        // 1. 연산자와 숫자를 분리해서 처리
        String arr1[] = str.split(""); // split 함수에 공백을 넣으면 문자 하나 단위로 분리해준다
        //arr1[0] = "4";
        //arr1[1] = "6";
        //arr1[2] = "+";
        // 1.2 연산자와 숫자를 분리해서 arr2 List에 담는다
        List<String> arr2 = new ArrayList<>();
        String temp = "";
        for(String item : arr1){
            if(item.equals("+") || item.equals("-") || item.equals("*") || item.equals("/")){
                arr2.add(temp);
                temp = "";
                arr2.add(item);
            }else{
                temp = temp + item;
            }
        }
        arr2.add(temp); // 마지막에 더한 문자를 저장한다
        //              0   1   2  3   4  5  6
        // List arr2 = {46, +, 95, -, 32, *, 5}
        //                  141 , -, 32, *, 5
        //                       109 , * , 5
        //                            545
        // 반복문을 돌면서 *,/ 연산자를 먼저체크하고
        for(int i=0; i<arr2.size(); i++){
            String item = arr2.get(i);
            if(item.equals("*") || item.equals("/")){
                int prev = Integer.parseInt(arr2.get(i-1)); // * 전의 아이템을 꺼내서 숫자로 변환
                int next = Integer.parseInt(arr2.get(i+1)); // * 다음의 아이템을 꺼내서 숫자로 변환
                int calced = 0;
                if(item.equals("*"))
                    calced = prev * next;
                else
                    calced = prev / next;
                arr2.set(i, calced+""); // 앞뒤의 값을 연산한 결과값을 현재 연산자의 위치에 저장
                arr2.remove(i+1); // 앞뒤의 아이템을 삭제
                arr2.remove(i-1);
            }
        }
        // 반복문을 돌면서 +,- 연산자를 다음에 체크한다
        for(int i=0; i<arr2.size(); i++){
            String item = arr2.get(i);
            if(item.equals("+") || item.equals("-")){
                int prev = Integer.parseInt(arr2.get(i-1)); // * 전의 아이템을 꺼내서 숫자로 변환
                int next = Integer.parseInt(arr2.get(i+1)); // * 다음의 아이템을 꺼내서 숫자로 변환
                int calced = 0;
                if(item.equals("+"))
                    calced = prev + next;
                else
                    calced = prev - next;
                arr2.set(i, calced+""); // 앞뒤의 값을 연산한 결과값을 현재 연산자의 위치에 저장
                arr2.remove(i+1); // 앞뒤의 아이템을 삭제
                arr2.remove(i-1);
            }
        }

        // 결과값 저장 후 리턴
        result = arr2.get(0);
        return result;
    }
}
