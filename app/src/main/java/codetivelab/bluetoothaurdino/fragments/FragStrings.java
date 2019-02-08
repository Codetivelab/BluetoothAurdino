package codetivelab.bluetoothaurdino.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import codetivelab.bluetoothaurdino.R;

public class FragStrings extends Fragment {
    OnNextBtnClickedListener mListener;
    public void setOnNextBtnClickedListener(OnNextBtnClickedListener mListener){
        this.mListener=mListener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_strings_itm, container, false);
        final Button nxtBtn=(Button)view.findViewById(R.id.frag_btn_nxt);
        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null){
                    mListener.onNxtBtnClicked();
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
    public interface OnNextBtnClickedListener{
        public void onNxtBtnClicked();
    }
}
