package com.kodonho.android.layoutbasic;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ConstraintLayout layout;
    TextView result,preview;
    View target;
    float targetX,targetY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.layout);
        target = findViewById(R.id.target);
        target.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                targetX=target.getX();
                targetY=target.getY();
            }
        });
        result = findViewById(R.id.result);
        preview = findViewById(R.id.preview);
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
        String temp = preview.getText().toString();
        // String 리소스를 코드에서 사용하기
        String PRE = getResources().getString(R.string.text_preview);
        if(PRE.equals(temp)) temp = "";
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
        runAnimation(v);
        preview.setText(temp);
    }

    public void runAnimation(View current){
        // 가상버튼 생성
        final Button dummy = new Button(this);
        // 레이아웃에 버튼을 추가하고
        layout.addView(dummy);
        // 버튼의 속성을 정의
        dummy.setWidth(current.getWidth());
        dummy.setHeight(current.getHeight());
        dummy.setBackgroundColor(Color.GRAY);
        dummy.setX(current.getX());
        dummy.setY(current.getY());
        dummy.setPivotX(50);
        dummy.setPivotY(50);

        Log.d("AniTest","targetX="+targetX+", targetY="+targetY);

        ObjectAnimator aniX = ObjectAnimator.ofFloat(dummy,
                View.X,
                targetX);
        ObjectAnimator aniY = ObjectAnimator.ofFloat(dummy,
                View.Y,
                targetY);
        ObjectAnimator aniR = ObjectAnimator.ofFloat(dummy,
                View.ROTATION,
                720);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(dummy,
                View.SCALE_X,
                0.2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(dummy,
                View.SCALE_Y,
                0.2f);
        AnimatorSet aniSet = new AnimatorSet();
        aniSet.setDuration(1000);
        aniSet.playTogether(aniX,aniY,aniR,scaleX,scaleY);
        // 애니메이션 완료시 처리를 위한 리스너
        aniSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }
            @Override
            public void onAnimationEnd(Animator animation) {
                // 애니매이션이 완료되면 버튼을 제거한다
                layout.removeView(dummy);
            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        aniSet.start();
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
