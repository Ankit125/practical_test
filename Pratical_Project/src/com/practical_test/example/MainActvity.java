package com.practical_test.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.practical_test.example.R;
import com.server.async.AsyncPost;
import com.server.async.OnResponse;
import com.server.async.response;

public class MainActvity extends ListActivity {

	private ArrayList<response> list_response;
	// The data to be displayed in the ListView

	private ProgressBar progressbar;
	List_Adapter adap;
	private Integer page = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loadmore);

		progressbar = (ProgressBar) findViewById(R.id.progressBar1);

		list_response = new ArrayList<response>();
		if (isNetworkAvailable(getApplicationContext())) {
			get_data();
		} else {
			Toast.makeText(getApplicationContext(), "No internet connection",
					Toast.LENGTH_LONG).show();
		}

		// set a listener to be invoked when the list reaches the end
		((LoadMoreListView) getListView())
				.setOnLoadMoreListener(new OnLoadMoreListener() {
					public void onLoadMore() {
						// Do the work to load more items at the end of list
						// here
						// new LoadDataTask().execute();
						if (isNetworkAvailable(getApplicationContext())) {
							get_data();
						} else {
							Toast.makeText(getApplicationContext(),
									"No internet connection", Toast.LENGTH_LONG)
									.show();
						}
					}
				});

	}

	public class List_Adapter extends BaseAdapter {
		private LayoutInflater inflater = null;

		public List_Adapter(Activity a, ArrayList<response> list_response) {
			inflater = (LayoutInflater) a.getSystemService(a
					.getApplicationContext().LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list_response.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list_response.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View vi = convertView;
			if (convertView == null) {
				vi = inflater.inflate(R.layout.sdsdsds, null);
			}
			TextView txt_id = (TextView) vi.findViewById(R.id.textView1);
			TextView txt_userid = (TextView) vi.findViewById(R.id.textView2);
			TextView txt_date = (TextView) vi.findViewById(R.id.textView3);
			TextView txt_msg = (TextView) vi.findViewById(R.id.textView4);

			txt_id.setText("" + list_response.get(position).getId());
			txt_userid.setText("" + list_response.get(position).getUserid());
			txt_date.setText("" + list_response.get(position).getDatetime());
			txt_msg.setText("" + list_response.get(position).getMsg());
			updateView(position - 3);
			return vi;
		}

	}

	public void get_data() {
		if (page == 1) {
			progressbar.setVisibility(View.VISIBLE);
		}
		// http://50.2.223.175/androidtest/getchathistory.php?page=1
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("page", "" + page++));

		AsyncPost asyncPost = new AsyncPost(
				"http://50.2.223.175/androidtest/getchathistory.php", params,
				new OnResponse() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub

						try {
							Log.i("System out", "response :" + response);
							JSONObject json = new JSONObject(response);
							JSONArray jArray = json.getJSONArray("chathistory");
							if (jArray.length() != 0) {
								for (int i = 0; i < jArray.length(); i++) {
									JSONObject json_data = jArray
											.getJSONObject(i);
									String id = json_data.getString("id");
									String datetime = json_data
											.getString("datetime");
									String userid = json_data
											.getString("userid");
									String msg = json_data.getString("msg");

									response res = new response();
									res.setId(id);
									res.setDatetime(datetime);
									res.setUserid(userid);
									res.setMsg(msg);
									list_response.add(res);
									runOnUiThread(new Runnable() {
										public void run() {
											adap = new List_Adapter(
													MainActvity.this,
													list_response);
											setListAdapter(adap);
											adap.notifyDataSetChanged();
											((LoadMoreListView) getListView())
													.onLoadMoreComplete();
											if (page != 2) {
												((LoadMoreListView) getListView()).setSelection(adap
														.getCount() - 1);
											}
											progressbar
													.setVisibility(View.INVISIBLE);
										}
									});
								}
							} else {
								runOnUiThread(new Runnable() {
									public void run() {
										progressbar
												.setVisibility(View.INVISIBLE);
										((LoadMoreListView) getListView())
												.onLoadMoreComplete();
										Toast.makeText(getApplicationContext(),
												"No more data found",
												Toast.LENGTH_LONG).show();
									}
								});
								Log.i("System out", "No more data");
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		asyncPost.execute();
	}

	public void updateView(int index) {
		View v = ((LoadMoreListView) getListView()).getChildAt(index
				- ((LoadMoreListView) getListView()).getFirstVisiblePosition());

		if (v == null)
			return;

		Log.i("System out", "Update item");
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}