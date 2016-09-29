package f.com.panoramics.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.guxiu.panoramics.R;


/**
 * 
 * @author Fatty
 * 
 * FloatHeaderListView
 *
 */
public class FloatHeaderListView extends ExpandableListView implements OnScrollListener {
	
	private OnScrollListener mOnScrollListener;
	private LayoutInflater mInflater;

	private View footerView;
	private RelativeLayout loadMoreFooterView;
	private ProgressBar loadMoreProgressBar;
	
	private OnLoadMoreListener mOnLoadMoreListener;
	
	private boolean mIsLoadingMore = false;
	private boolean mCanLoadMore = true;
	private int mCurrentScrollState;
	
	public FloatHeaderListView(Context context) {
		super(context);
		init(context);
	}

	public FloatHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FloatHeaderListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerView = (View) mInflater.inflate(R.layout.p_listview_group_child_load_more_footer, this, false);
		loadMoreFooterView = (RelativeLayout) footerView.findViewById(R.id.load_more_footer);
		loadMoreProgressBar = (ProgressBar) footerView.findViewById(R.id.load_more_progressBar);
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
			mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
		
		if(totalItemCount > 0){
			refreshHeader() ;
		}
		
		if (mOnLoadMoreListener != null) {
			if (visibleItemCount == totalItemCount) {
				loadMoreFooterView.setVisibility(View.GONE);
				loadMoreProgressBar.setVisibility(View.GONE);
				return;
			}
			boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
			if (!mIsLoadingMore && loadMore && mCurrentScrollState != SCROLL_STATE_IDLE) {
				if(!mCanLoadMore){
					return;
				}
				loadMoreFooterView.setVisibility(View.VISIBLE);
				loadMoreProgressBar.setVisibility(View.VISIBLE);
				mIsLoadingMore = true;
				onLoadMore();
			}
		}
	}

	/**
	 * 
	 */
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mCurrentScrollState = scrollState;
		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
	}
	
	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		super.setAdapter(adapter);
	}

	/**
	 * 
	 * @author Fatty             loadMore
	 *
	 */
	public interface OnLoadMoreListener {
		public void onLoadMore();
	}
	
	public void setCanLoadMore(boolean canLoadMore){
		mCanLoadMore = canLoadMore;
		loadMoreFooterView.setVisibility(View.GONE);
		loadMoreProgressBar.setVisibility(View.GONE);
	}
	
	public boolean isCanLoadMore(){
		return mCanLoadMore;
	}
	
	public void onLoadMore() {
		if (mOnLoadMoreListener != null) {
			mOnLoadMoreListener.onLoadMore();
		}
	}

	public void onLoadMoreComplete() {
		mIsLoadingMore = false;
		loadMoreFooterView.setVisibility(View.GONE);
		loadMoreProgressBar.setVisibility(View.GONE);
	}
	
	
	/**
	 ********************************         ViewHeader          *********************************           
	 */
	private View mHeaderView;
	private int mHeaderWidth;
	private int mHeaderHeight;
	private OnHeaderUpdateListener mHeaderUpdateListener;

	/**
	 * 
	 * @author Fatty
	 *
	 */
	public interface OnHeaderUpdateListener {
		public void setHeadViewContent(View headerView, int firstVisibleGroupPos);
	}
	
	public void setOnHeaderUpdateListener(OnHeaderUpdateListener listener) {
		mHeaderUpdateListener = listener;
	}
	
	public void setHeaderView(View view){
		this.mHeaderView = view;
	}
	
	protected void refreshHeader() {
		if (mHeaderView == null) {
			return;
		}
		int firstVisiblePos = getFirstVisiblePosition();
		int pos = firstVisiblePos + 1;
		int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
		int group = getPackedPositionGroup(getExpandableListPosition(pos));
		
		if(firstVisibleGroupPos < 0 ){
			mHeaderHeight = 0;
			return;
		}

		if (group == firstVisibleGroupPos + 1) {
			View view = getChildAt(1);
			if (view == null) {
				return;
			}
			if (view.getTop() <= mHeaderHeight) {
				int delta = mHeaderHeight - view.getTop();
				mHeaderView.layout(0, -delta, mHeaderWidth, mHeaderHeight - delta);
			} else {
				mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
			}
		} else {
			mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
		}

		if (mHeaderUpdateListener != null) {
			mHeaderUpdateListener.setHeadViewContent(mHeaderView, firstVisibleGroupPos);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mHeaderView == null) {
			return;
		}
		measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
		mHeaderWidth = mHeaderView.getMeasuredWidth();
		mHeaderHeight = mHeaderView.getMeasuredHeight();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (mHeaderView == null) {
			return;
		}
		int delta = mHeaderView.getTop();
		mHeaderView.layout(0, delta, mHeaderWidth, mHeaderHeight + delta);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mHeaderView != null) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}
}