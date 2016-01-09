package clwang.chunyu.me.testfragmenttabhost;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;

    //定义数组来存放按钮图片
    @DrawableRes
    private int mImages[] = {
            R.drawable.tab_counter,
            R.drawable.tab_assistant,
            R.drawable.tab_contest,
            R.drawable.tab_center
    };

    private String mFragmentTags[] = {
            "counter",
            "assistant",
            "contest",
            "center"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);

        for (int i = 0; i < mImages.length; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mFragmentTags[i]).setIndicator(getImageView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, FragmentTab.class, null);
            // 设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.pedo_actionbar_bkg);
        }
    }

    private View getImageView(int index) {
        View view = getLayoutInflater().inflate(R.layout.view_tab_indicator, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.tab_iv_image);
        imageView.setImageResource(mImages[index]);

        return view;
    }
}
