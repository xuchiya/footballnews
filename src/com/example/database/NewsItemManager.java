package com.example.database;

import java.util.ArrayList;
import java.util.List;

import com.xuchiya.model.NewsBean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NewsItemManager {
	int count = 0;
	private DBHelper dbHelper;

	public NewsItemManager(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void add(NewsBean item) {

		String sql = "insert into tb_football_newsItem (title,link,date,imgLink,content,newstype) values(?,?,?,?,?,?) ;";
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(
				sql,
				new Object[] { item.getTitle(), item.getLink(), item.getDate(),
						item.getImgLink(), item.getContent(),
						item.getNewsType() });

		// System.out.println("DB___add item" + count++);
		db.close();
	}

	public void add(List<NewsBean> newsItems) {

		for (NewsBean newsItem : newsItems) {
			add(newsItem);
		}
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor d = db
				.rawQuery(
						"select title,link,date,imgLink,content,newstype from tb_football_newsItem ",
						null);
		System.out.println("d.count=" + d.getCount());
		d.close();
		db.close();
	}

	public void deleteAll(int newsType) {
		String sql = "delete from tb_football_newsItem where newstype = ?";
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(sql, new Object[] { newsType });

		// System.out.println("DB___deleteAll items  ----------  " + newsType);
		db.close();
	}

	public List<NewsBean> getNewsFromDB(int newsType, int currentPage) {
		List<NewsBean> newsItems = new ArrayList<NewsBean>();

		int offset = 30 * (currentPage - 1);
		String sql = "select title,link,date,imgLink,content,newstype from tb_football_newsItem where newstype = ? limit ?,? ";
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		try {
			Cursor c = db.rawQuery(sql, new String[] { newsType + "",offset + "", 30+ "" });
            System.out.println("offset =" +offset);
            System.out.println("offset+30 ="+(offset + 30));
			NewsBean newsitem = null;

			while (c.moveToNext()) {
				newsitem = new NewsBean();

				String title = c.getString(c.getColumnIndex("title"));

				String date = c.getString(c.getColumnIndex("date"));

				String imgLink = c.getString(c.getColumnIndex("imgLink"));

				String link = c.getString(c.getColumnIndex("link"));

				String content = c.getString(c.getColumnIndex("content"));

				int newstype = c.getInt(c.getColumnIndex("newstype"));

				newsitem.setTitle(title);
				newsitem.setLink(link);
				newsitem.setImgLink(imgLink);
				newsitem.setDate(date);
				newsitem.setNewsType(newstype);
				newsitem.setContent(content);
				newsItems.add(newsitem);
			}
			c.close();

			// Cursor d =
			// db.rawQuery("select title,link,date,imgLink,content,newstype from tb_football_newsItem ",
			// null);
			// System.out.println("d.count="+d.getCount());
			// d.close();
			System.out.println("DB___getNewsFromDB  ----------  " + newsType
					+ "____" + currentPage);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			db.close();
		}
		System.out.println("DB___getNewsFromDB  ----------  "
				+ newsItems.size());
		return newsItems;
	}
}
