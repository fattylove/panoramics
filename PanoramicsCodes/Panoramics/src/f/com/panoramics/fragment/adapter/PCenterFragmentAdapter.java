package f.com.panoramics.fragment.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;


/**
 * 
 * @author Fatty
 * 
 * FragmentPagerAdapter
 *
 */
public class PCenterFragmentAdapter extends FragmentPagerAdapter {

	ArrayList<Fragment> fragments;

	/**
	 * 
	 * @param fm
	 * @param list
	 */
	public PCenterFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
		super(fm);
		this.fragments = list;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return super.isViewFromObject(view, object);
	}
	
	
	
}
