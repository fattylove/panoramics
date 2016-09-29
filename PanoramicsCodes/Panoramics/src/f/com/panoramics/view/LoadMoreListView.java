package f.com.panoramics.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.guxiu.panoramics.R;

/**
 * 
 * @author Fatty
 * 
 * Load more by ListView for Comment
 *
 */
public class LoadMoreListView extends ListView implements OnScrollListener {

	private OnScrollListener mOnScrollListener;
	private LayoutInflater mInflater;

	private View footerView;
	private RelativeLayout footerLayout;
	private ProgressBar mProgressBarLoadMore;
	private OnLoadMoreListener mOnLoadMoreListener;
	private boolean mIsLoadingMore = false;

	private boolean mCanLoadMore = true;
	private int mCurrentScrollState;
	
	public LoadMoreListView(Context context) {
		super(context);
		init(context);
	}

	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerView = (View) mInflater.inflate(R.layout.p_listview_load_more_footer, this, false);
		footerLayout = (RelativeLayout) footerView.findViewById(R.id.load_more_footer);
		mProgressBarLoadMore = (ProgressBar) footerView.findViewById(R.id.load_more_progressBar);
		addFooterView(footerView);
		super.setOnScrollListener(this);
	}

	@Override
	public void setOnScrollListener(AbsListView.OnScrollListener l) {
		mOnScrollListener = l;
	}
	
	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		mOnLoadMoreListener = onLoadMoreListener;
	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}
		if (mOnLoadMoreListener != null) {
			if (visibleItemCount == totalItemCount) {
				mProgressBarLoadMore.setVisibility(View.INVISIBLE);
				footerLayout.setVisibility(View.INVISIBLE);
				return;
			}
			boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
			if (!mIsLoadingMore && loadMore && mCurrentScrollState != SCROLL_STATE_IDLE) {
				if(!mCanLoadMore){
					return;
				}
				footerLayout.setVisibility(View.VISIBLE);
				mProgressBarLoadMore.setVisibility(View.VISIBLE);
				mIsLoadingMore = true;
				onLoadMore();
			}
		}
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mCurrentScrollState = scrollState;
		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	public void setCanLoadMore(boolean canLoadMore){
		mCanLoadMore = canLoadMore;
		footerLayout.setVisibility(View.INVISIBLE);
		mProgressBarLoadMore.setVisibility(View.INVISIBLE);
	}
	
	public void onLoadMore() {
		if (mOnLoadMoreListener != null) {
			mOnLoadMoreListener.onLoadMore();
		}
	}

	public void onLoadMoreComplete() {
		mIsLoadingMore = false;
		footerLayout.setVisibility(View.INVISIBLE);
		mProgressBarLoadMore.setVisibility(View.INVISIBLE);
	}

	public interface OnLoadMoreListener {
		public void onLoadMore();
	}

}