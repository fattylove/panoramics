package f.com.panoramics.fragment;

import com.guxiu.panoramics.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author Fatty
 *
 */
public class FN_FollowingFragment extends Fragment {
	
	
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view  = inflater.inflate(R.layout.panoramics_fragment_fn_following_layout, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	};
}
