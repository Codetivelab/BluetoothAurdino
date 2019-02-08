package codetivelab.bluetoothaurdino.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import codetivelab.bluetoothaurdino.R;

public class FragInfo extends Fragment {
    ImageView imageView;
    Button nxtBtn;
    OnFinishBtnClickedListener mListener;
    public void setOnFinishBtnClickedListener(OnFinishBtnClickedListener mListener){
        this.mListener=mListener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_info_itm, container, false);
        imageView=(ImageView)view.findViewById(R.id.im);
        ObjectAnimator animator=ObjectAnimator.ofFloat(imageView,"translationY",-110f);
        animator.setDuration(2000);
        animator.start();

        nxtBtn=(Button)view.findViewById(R.id.frag_btn_fnsh);
        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null){
                    mListener.onFinishBtnClicked();
                }
            }
        });
        nxtBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==true){
                    nxtBtn.setBackgroundResource(R.color.colorBtnHover);;
                }else{
                    nxtBtn.setBackgroundResource(R.color.colorPrimaryDark);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ObjectAnimator animator=ObjectAnimator.ofFloat(imageView,"translationY",-110f);
        animator.setDuration(2000);
        animator.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        ObjectAnimator animator=ObjectAnimator.ofFloat(imageView,"translationY",0f);
        animator.setDuration(10);
        animator.start();

    }

    public void onDestroyView(){
        super.onDestroyView();
    }
    public interface OnFinishBtnClickedListener{
        public void onFinishBtnClicked();
    }
}