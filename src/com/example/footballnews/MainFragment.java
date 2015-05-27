package com.example.footballnews;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.NetUtil.NetUtil;
import com.example.database.NewsItemManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuchiya.model.NewsBean;
import com.xuchiya.spider.newsItemSpider;

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements
		IXListViewRefreshListener, IXListViewLoadMore {
	private static final int PULL_TO_REFRESH = 0;
	private static final int LOAD_MORE = 1;
	private long refreshDate = 0;
	private ImageView[] dots;
	private ViewPager advertisement;
	NewsItemManager mManager;

	DisplayImageOptions mOptions = new DisplayImageOptions.Builder()

	.showImageForEmptyUri(R.drawable.fail).showImageOnFail(R.drawable.fail)
			.resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisk(true)
			.build();

	private XListView xlistview;
	private int newsType;

	private int currentPage = 1;
	private List<NewsBean> data = new ArrayList<NewsBean>();
	private List<View> addata = new ArrayList<View>();
	private newsitemAdapter mAdapter = null;
	private mAdAdapter madvertisementAdapter = null;

	public MainFragment(int newsType) {
		this.newsType = newsType;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate--------------");
		super.onCreate(savedInstanceState);
		mAdapter = new newsitemAdapter(getActivity(), data);
		madvertisementAdapter = new mAdAdapter(addata);
		mManager = new NewsItemManager(getActivity());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		System.out.println("onCreateView--------------");
		View view = inflater.inflate(R.layout.tab_item_fragment_main, null);
		xlistview = (XListView) view.findViewById(R.id.xlistview);
		advertisement = (ViewPager) view.findViewById(R.id.advertisement);
		dots = new ImageView[3];
		dots[0] = (ImageView) view.findViewById(R.id.lv1);
		dots[1] = (ImageView) view.findViewById(R.id.lv2);
		dots[2] = (ImageView) view.findViewById(R.id.lv3);
		advertisement.setOffscreenPageLimit(1);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		System.out.println("onActivityCreated--------------");
		super.onActivityCreated(savedInstanceState);
		/**
		 * 初始化
		 */
		System.out.println("onActivityCreated" + newsType);

		// System.out.println("mAdapter======" + mAdapter.toString());
		if (((MainActivity) getActivity()).getCache_activity()
				.getCache_By_NewsType(newsType) == null) {

			// System.out.println("onActivityCreated" + newsType +
			// "-----------1");
			advertisement.setAdapter(madvertisementAdapter);
			xlistview.setAdapter(mAdapter);
			xlistview.setPullRefreshEnable(this);
			xlistview.setPullLoadEnable(this);
			// mXListView.NotRefreshAtBegin();
			/**
			 * 进来时直接刷新
			 */

			xlistview.startRefresh();

		} else {
			// System.out.println("onActivityCreated" + newsType +
			// "-----------2");
			List<NewsBean> temp = ((MainActivity) getActivity())
					.getCache_activity().getCache_By_NewsType(newsType);
			mAdapter.setData(temp);
			xlistview.setAdapter(mAdapter);
			List<View> tempView = new ArrayList<View>();
			for (int i = 0; i < 3; i++) {
				LayoutInflater in = LayoutInflater.from(getActivity());
				View viewOne = in.inflate(R.layout.one, null);
				ImageLoader.getInstance().displayImage(
						temp.get(i).getImgLink(),
						(ImageView) viewOne.findViewById(R.id.image_one),
						mOptions);

				tempView.add(viewOne);
			}
			madvertisementAdapter.setData(tempView);
			advertisement.setAdapter(madvertisementAdapter);
			madvertisementAdapter.notifyDataSetChanged();
			xlistview.setPullRefreshEnable(this);
			xlistview.setPullLoadEnable(this);
			xlistview.NotRefreshAtBegin();
			mAdapter.notifyDataSetChanged();
		}

		xlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {

				NewsBean temp = (NewsBean) mAdapter.getItem(index - 1);
				String url = temp.getLink();
				Intent intent = new Intent();
				intent.putExtra("url", url);
				intent.setClass(getActivity(), Detailed_Info_Activity.class);
				startActivity(intent);
			}
		});
		advertisement.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

				// TODO Auto-generated method stub
				for (int i = 0; i < 3; i++) {
					if (arg0 == i) {
						dots[i].setImageResource(R.drawable.login_point_selected);
					} else {
						dots[i].setImageResource(R.drawable.login_point);
					}
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	Handler myhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			List<NewsBean> getdata = (List<NewsBean>) msg.getData().get("data");
			// System.out.println("SOS-----------getdata.size()="+getdata.size());
			List<View> views = new ArrayList<View>();
			// System.out.println("SOS-----------mAdapter....="+mAdapter.getCount());
			switch (msg.what) {
			case PULL_TO_REFRESH:
				if (getdata.size() != 0) {

					for (int i = 0; i < 3; i++) {
						LayoutInflater in = LayoutInflater.from(getActivity());
						View viewOne = in.inflate(R.layout.one, null);
						ImageLoader.getInstance()
								.displayImage(
										getdata.get(i).getImgLink(),
										(ImageView) viewOne
												.findViewById(R.id.image_one),
										mOptions);
						views.add(viewOne);
					}
				}

				madvertisementAdapter.setData(views);
				madvertisementAdapter.notifyDataSetChanged();

				mAdapter.setData(getdata);
				mAdapter.notifyDataSetChanged();
				xlistview.stopRefresh();
				break;
			case LOAD_MORE:

				mAdapter.addAll(getdata);

				mAdapter.notifyDataSetChanged();
				xlistview.stopLoadMore();
				break;
			case 3:
				xlistview.stopRefresh();
				break;
			}

		}

	};

	@Override
	public void onRefresh() {
		System.out.println("onRefresh--------------");
		if (NetUtil.checkNet(getActivity())) {
			new Thread() {
				public void run() {
					long curDate = System.currentTimeMillis();

					// System.out.println("refreshDate=" + refreshDate);
					if ((curDate - refreshDate) > 1000 * 60 * 10) {
						// System.out.println("onrefresh---------" + "开始刷新");
						if (((MainActivity) getActivity()).getCache_activity()
								.getCache_By_NewsType(newsType) != null) {
							((MainActivity) getActivity()).getCache_activity()
									.clear(newsType);
						}
						// System.out.println("刷新到这一步");
						refreshDate = curDate;
						currentPage = 1;
						// String res = DownLoadHtml.download(URLUtils
						// .generateUrl(newsType, currentPage));
						newsItemSpider spider = new newsItemSpider();
						List<NewsBean> lists = spider.getNews(newsType,
								currentPage);
						((MainActivity) getActivity()).getCache_activity()
								.setCache(newsType, lists);
						mManager.deleteAll(newsType);
						mManager.add(lists);
						Message msg = Message.obtain();
						Bundle b = new Bundle();
						b.putSerializable("data", (Serializable) lists);
						msg.what = PULL_TO_REFRESH;
						msg.setData(b);
						myhandler.sendMessage(msg);
					} else {
						// System.out.println("onrefresh---------" +
						// "时间太短，不用刷新");
						// xlistview.stopRefresh();
						try {
							Thread.currentThread().sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						myhandler.sendEmptyMessage(3);
					}

				}
			}.start();

		} else {
			new AlertDialog.Builder(getActivity()).setMessage("当前网络不可用")
					.setPositiveButton("确定", null).show();

			new Thread() {
				public void run() {
					currentPage = 1;
					List<NewsBean> lists = mManager.getNewsFromDB(newsType,
							currentPage);
					System.out.println("DB__currentPage=" + currentPage
							+ "newsType=" + newsType);

					if (lists.size() != 0) {
						((MainActivity) getActivity()).getCache_activity()
								.setCache(newsType, lists);
					}
					Message msg = Message.obtain();
					Bundle b = new Bundle();
					b.putSerializable("data", (Serializable) lists);
					msg.what = PULL_TO_REFRESH;
					msg.setData(b);
					myhandler.sendMessage(msg);
				}
			}.start();
		}

	}

	@Override
	public void onLoadMore() {
		System.out.println("SOSssss-----------mAdapter....="
				+ mAdapter.getCount());
		if (NetUtil.checkNet(getActivity())) {
			new Thread() {
				public void run() {
					currentPage++;

					newsItemSpider spider = new newsItemSpider();
					List<NewsBean> lists = spider
							.getNews(newsType, currentPage);
					System.out.println("SOS-----------currentPage= "
							+ currentPage + "lists.size()=" + lists.size());

					mManager.add(lists);
					Message msg = Message.obtain();
					Bundle b = new Bundle();
					b.putSerializable("data", (Serializable) lists);
					msg.what = LOAD_MORE;
					msg.setData(b);
					myhandler.sendMessage(msg);
				}
			}.start();
		} else {
			new Thread() {
				public void run() {

					currentPage++;
					List<NewsBean> lists = mManager.getNewsFromDB(newsType,
							currentPage);
					System.out.println("SOS-----------getdata.lists.size="
							+ lists.size());
					Message msg = Message.obtain();
					Bundle b = new Bundle();
					b.putSerializable("data", (Serializable) lists);
					msg.what = LOAD_MORE;
					msg.setData(b);
					myhandler.sendMessage(msg);

				};
			}.start();

		}
	}

	class newsitemAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		List<NewsBean> mLists = new ArrayList<NewsBean>();

		public void setData(List<NewsBean> list) {
			mLists = list;
		}

		// 使用开源框架 Universal—Image—Loader
		public void addAll(List<NewsBean> list) {
			System.out.println("SOS--------before---addAll  mLists.size()="
					+ mLists.size());
			mLists.addAll(list);
			System.out.println("SOS--------after---addAll  mLists.size()="
					+ mLists.size());
		}

		public newsitemAdapter(Context context, List<NewsBean> mLists) {
			this.mInflater = LayoutInflater.from(context);
			this.mLists = mLists;
		}

		@Override
		public int getCount() {
			return mLists.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mLists.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = mInflater
						.inflate(R.layout.xlistview_item, parent, false);
				holder = new ViewHolder();
				holder.title = (TextView) view.findViewById(R.id.id_title);
				holder.date = (TextView) view.findViewById(R.id.id_date);
				holder.content = (TextView) view.findViewById(R.id.id_content);
				holder.image = (ImageView) view.findViewById(R.id.id_image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.title.setText(mLists.get(position).getTitle());
			holder.content.setText(mLists.get(position).getContent());
			holder.date.setText(mLists.get(position).getDate());

			ImageLoader.getInstance().displayImage(
					mLists.get(position).getImgLink(), holder.image, mOptions);

			return view;

		}

		private class ViewHolder {
			public TextView title;
			public TextView content;
			public TextView date;
			public ImageView image;
		}
	}

	class mAdAdapter extends PagerAdapter {
		private List<View> mLists = new ArrayList<View>();

		public mAdAdapter(List<View> mLists) {
			super();
			this.mLists = mLists;
		};

		public void setData(List<View> lists) {
			mLists = lists;
		}

		@Override
		public int getCount() {

			return mLists.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(mLists.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(mLists.get(position));
			return mLists.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return (arg0 == arg1);
		}

	}

}
