package com.nicodelee.beautyarticle.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nicodelee.view.LoadingDialog;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BaseFragment extends Fragment {
	private LayoutInflater inflater;
	private View contentView;
	private Context context;
	private ViewGroup container;
    public LoadingDialog loadingDialog;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
	}

//	@Override
//	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		this.inflater = inflater;
//		this.container = container;
//		onCreateView(savedInstanceState);
//        loadingDialog = new LoadingDialog(getActivity());
//		if (contentView == null)
//			return super.onCreateView(inflater, container, savedInstanceState);
//		return contentView;
//	}

	protected void onCreateView(Bundle savedInstanceState) {

	}


	public Context getApplicationContext() {
		return context;
	}

	public void setContentView(int layoutResID) {
		setContentView((ViewGroup) inflater.inflate(layoutResID, container, false));
	}

	public void setContentView(View view) {
		contentView = view;
	}

	public View getContentView() {
		return contentView;
	}

    public SwingBottomInAnimationAdapter setBottomInAnimation(AbsListView absListView,BaseAdapter baseAdapter){
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(baseAdapter);
        swingBottomInAnimationAdapter.setAbsListView(absListView);
        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(300);
        return  swingBottomInAnimationAdapter;
    }

	public View findViewById(int id) {
		if (contentView != null)
			return contentView.findViewById(id);
		return null;
	}

	// http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public APP getApp() {
        return (APP) getActivity().getApplication();
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
//
//    public void showInfo(String message) {
//        Crouton.makeText(getActivity(), message, Style.INFO).show();
//    }
//
//    public void showErro(String message) {
//        Crouton.makeText(getActivity(), message, Style.ALERT).show();
//    }
//
//    public void showConfirm(String message) {
//        Crouton.makeText(getActivity(), message, Style.CONFIRM).show();
//    }

    public void skipIntent(Class clz, HashMap<String, Object> map,
                           boolean isFinish) {
        Intent intent = new Intent(getActivity(), clz);
        if (map != null) {
            Iterator it = map.entrySet().iterator();

            while (it.hasNext()) {

                Map.Entry entry = (Map.Entry) it.next();

                String key = (String) entry.getKey();

                Serializable value = (Serializable) entry.getValue();

                intent.putExtra(key, value);
            }
        }
        startActivity(intent);
        if (isFinish)
            getActivity().finish();
    }

    public void skipIntent(Class clz, HashMap<String, Object> map, int code) {
        Intent intent = new Intent(getActivity(), clz);
        if (map != null) {
            Iterator it = map.entrySet().iterator();

            while (it.hasNext()) {

                Map.Entry entry = (Map.Entry) it.next();

                String key = (String) entry.getKey();

                Serializable value = (Serializable) entry.getValue();

                intent.putExtra(key, value);
            }
        }
        startActivityForResult(intent, code);
    }

    public void skipIntent(Class clz, int code, boolean isFinish) {
        Intent intent = new Intent(getActivity(), clz);
        startActivityForResult(intent, code);
        if (isFinish)
            getActivity().finish();
    }

    public void skipIntent(Class clz, boolean isFinish) {
        Intent intent = new Intent(getActivity(), clz);
        startActivity(intent);
        if (isFinish)
            getActivity().finish();
    }


    public Object getExtra(String name) {
        return getActivity().getIntent().getSerializableExtra(name);
    }
}
