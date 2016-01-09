package clwang.chunyu.me.testfragmenttabhost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Tab的Fragment
 * <p/>
 * Created by wangchenlong on 15/12/28.
 */
public class FragmentTab extends Fragment {

    private static final String TAG = "DEBUG-WCL: " + FragmentTab.class.getSimpleName();

    private static final String ARG_SELECTION_TAG = "fragment_tab.arg_selection_tag";

    @Bind(R.id.tab_tv_text) TextView mTvText;

    private View mViewContent; // 视图内容

    public FragmentTab() {
    }

    public static FragmentTab newInstance(String page) {
        FragmentTab tab = new FragmentTab();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SELECTION_TAG, page);
        tab.setArguments(bundle);
        return tab;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: " + getTag());

        if (mViewContent == null) {
            Log.e(TAG, "onCreateView-inflate: " + getTag());
            mViewContent = inflater.inflate(R.layout.fragment_tab, container, false);
        }

        // 缓存的View需要判断是否已经被加过parent, 如果有parent需要从parent删除,
        // 要不然会发生这个View已经有parent的错误.
        ViewGroup parent = (ViewGroup) mViewContent.getParent();
        if (parent != null) {
            parent.removeView(mViewContent);
        }

        Log.e(TAG, "onCreateView-old: " + getTag());
        ButterKnife.bind(this, mViewContent);
        return mViewContent;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onViewCreated: " + getTag());
        super.onViewCreated(view, savedInstanceState);
        mTvText.setText(String.valueOf("页面: " + getTag()));
    }

    @Override public void onDestroyView() {
        Log.e(TAG, "onDestroyView: " + getTag());
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
