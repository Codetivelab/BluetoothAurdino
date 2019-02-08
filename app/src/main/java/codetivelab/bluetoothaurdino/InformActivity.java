        package codetivelab.bluetoothaurdino;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import codetivelab.bluetoothaurdino.adapters.FragmentPagerAdaptr;
import codetivelab.bluetoothaurdino.fragments.FragInfo;
import codetivelab.bluetoothaurdino.fragments.FragStrings;
import codetivelab.bluetoothaurdino.helper.ShrdPrfrncsHlpr;
import codetivelab.bluetoothaurdino.widgets.CustomViewPager;

public class InformActivity extends AppCompatActivity {
    FragInfo mFragInfo;
    FragStrings mFragStrings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);
        final CustomViewPager mPager=(CustomViewPager)findViewById(R.id.info_pager);
        mFragInfo=new FragInfo();
        mFragStrings=new FragStrings();
        List<Fragment> mList=new ArrayList<Fragment>();
        mList.add(mFragStrings);
        mList.add(mFragInfo);
        FragmentPagerAdaptr mAdapter=new FragmentPagerAdaptr(mList,getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mFragInfo.setOnFinishBtnClickedListener(new FragInfo.OnFinishBtnClickedListener() {
            @Override
            public void onFinishBtnClicked() {
                Toast.makeText(InformActivity.this,"Finishing",Toast.LENGTH_LONG).show();
                ShrdPrfrncsHlpr mHelper=new ShrdPrfrncsHlpr(getApplicationContext());
                mHelper.saveTrue();
                startActivity(new Intent(InformActivity.this,MainActivity.class));
                finish();
            }
        });

        mFragStrings.setOnNextBtnClickedListener(new FragStrings.OnNextBtnClickedListener() {
            @Override
            public void onNxtBtnClicked() {
                mPager.setCurrentItem(1);
            }
        });

    }
}
