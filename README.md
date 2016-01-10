# TestFragmentTabHost
FragmentTabHost的使用方法和注意事项.

FragmentTabHost作为Android4.0版本的控件, 已经被项目广泛使用, 5.0版本又推出TabLayout+ViewPager显示多页. 我来讲解如何使用FragmentTabHost.

Github[下载地址](https://github.com/SpikeKing/TestFragmentTabHost)

![TabHost](http://img.blog.csdn.net/20160110080020199)

主要包括:
(1) 自定义Tab的图片资源和去掉分割线.
(2) 缓存Fragment的布局, 减少填充.

> 在切换页面时, 控件会调用Fragment的onCreateView, 重新创建页面.
> 通过缓存页面, 可以增强性能. 

## 1. 布局
FragmentTabHost是原生控件, 并不需要添加其他的maven库. 
包括标签组Tabs和页面TabContainer, 标签组固定大小, 页面填充.
```
<?xml version="1.0" encoding="utf-8"?>
<android.support.v4.app.FragmentTabHost
    android:id="@android:id/tabhost"
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <TabWidget
            android:id="@android:id/tabs"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:layout_gravity="bottom"/>

        <FrameLayout
            android:id="@android:id/tabcontent"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1"/>

    </LinearLayout>

</android.support.v4.app.FragmentTabHost>
```

> 注意控件的id必须是Android提供的标准id, 即"@android:id".

Fragment布局, 包含一行文字提示.
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:tools="http://schemas.android.com/tools"
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              android:orientation="vertical">

    <TextView
        android:id="@+id/tab_tv_text"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:textSize="40sp"
        tools:text="Test"/>

</LinearLayout>
```

Tab布局, 包含一个图片控件.
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <ImageView
        android:id="@+id/tab_iv_image"
        android:padding="12dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:contentDescription="@null"
        tools:src="@drawable/tab_assistant"/>

</LinearLayout>
```

## 2. 主页
setup设置页面组合, 却掉分割线etDividerDrawable(null), 设置Tab.
使用自定义的图片资源, newTabSpec设置Fragment的Tag标签.
```
/**
 * 主页, 切换Tab标签显示不同页面.
 *
 * @author C.L.Wang
 */
public class MainActivity extends AppCompatActivity {

    @Bind(android.R.id.tabhost) FragmentTabHost mTabHost;

    // 图片
    @DrawableRes
    private int mImages[] = {
            R.drawable.tab_counter,
            R.drawable.tab_assistant,
            R.drawable.tab_contest,
            R.drawable.tab_center
    };

    // 标题
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

        ButterKnife.bind(this);

        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null); // 去掉分割线

        for (int i = 0; i < mImages.length; i++) {
            // Tab按钮添加文字和图片
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mFragmentTags[i]).setIndicator(getImageView(i));
            // 添加Fragment
            mTabHost.addTab(tabSpec, FragmentTab.class, null);
            // 设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.pedo_actionbar_bkg);
        }
    }

    // 获得图片资源
    private View getImageView(int index) {
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.view_tab_indicator, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_iv_image);
        imageView.setImageResource(mImages[index]);
        return view;
    }
}
```

## 3. 切换页
显示不同Tag标签. 缓存页面, 注意关联前, 删除父控件关联. 页面显示Tag信息. 
```
/**
 * Tab的Fragment
 * <p/>
 * Created by wangchenlong on 15/12/28.
 */
public class FragmentTab extends Fragment {

    @Bind(R.id.tab_tv_text) TextView mTvText;

    private View mViewContent; // 缓存视图内容

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mViewContent == null) {
            mViewContent = inflater.inflate(R.layout.fragment_tab, container, false);
        }

        // 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
        ViewGroup parent = (ViewGroup) mViewContent.getParent();
        if (parent != null) {
            parent.removeView(mViewContent);
        }

        ButterKnife.bind(this, mViewContent);
        return mViewContent;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 显示Fragment的Tag信息
        mTvText.setText(String.valueOf("Page: " + getTag()));
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
```

![动画](http://img.blog.csdn.net/20160110080053082)

OK, That's all! 熟练使用控件很重要. Enjoy It!
