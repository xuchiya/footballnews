package com.example.footballnews;

import java.util.ArrayList;
import java.util.List;

import com.xuchiya.model.NewsBean;
import com.xuchiya.util.Constant;

public class CacheData {

	private ArrayList<NewsBean> Cache_England;

	private ArrayList<NewsBean> Cache_Spain;

	private ArrayList<NewsBean> Cache_German;

	private ArrayList<NewsBean> Cache_Italy;

	private ArrayList<NewsBean> Cache_Europ;

	public ArrayList<NewsBean> getCache_By_NewsType(int Newstype) {
		switch (Newstype) {

		case Constant.NEWS_TYPE_ENGLAND:
			return Cache_England;

		case Constant.NEWS_TYPE_SPNAN:
			return Cache_Spain;

		case Constant.NEWS_TYPE_GERMAN:
			return Cache_German;

		case Constant.NEWS_TYPE_ITALY:
			return Cache_Italy;

		case Constant.NEWS_TYPE_EUORP:
			return Cache_Europ;

		}
		return null;

	}

	public void setCache(int newsType, List<NewsBean> list) {
		switch (newsType) {

		case Constant.NEWS_TYPE_ENGLAND:
			Cache_England = (ArrayList<NewsBean>) list;
			break;

		case Constant.NEWS_TYPE_SPNAN:
			Cache_Spain = (ArrayList<NewsBean>) list;
			break;

		case Constant.NEWS_TYPE_GERMAN:
			Cache_German = (ArrayList<NewsBean>) list;
			break;

		case Constant.NEWS_TYPE_ITALY:
			Cache_Italy = (ArrayList<NewsBean>) list;
			break;

		case Constant.NEWS_TYPE_EUORP:
			Cache_Europ = (ArrayList<NewsBean>) list;
			break;

		}

	}

	public void appendCache(int newsType, List<NewsBean> list) {
		switch (newsType) {

		case Constant.NEWS_TYPE_ENGLAND:
			Cache_England.addAll(list);
			break;

		case Constant.NEWS_TYPE_SPNAN:
			Cache_Spain.addAll(list);
			break;

		case Constant.NEWS_TYPE_GERMAN:
			Cache_German.addAll(list);
			break;

		case Constant.NEWS_TYPE_ITALY:
			Cache_Italy.addAll(list);
			break;

		case Constant.NEWS_TYPE_EUORP:
			Cache_Europ.addAll(list);
			break;
		}

	}

	public void clear(int newsType) {
		switch (newsType) {

		case Constant.NEWS_TYPE_ENGLAND:
			Cache_England.clear();
			break;

		case Constant.NEWS_TYPE_SPNAN:
			Cache_Spain.clear();
			break;

		case Constant.NEWS_TYPE_GERMAN:
			Cache_German.clear();
			break;

		case Constant.NEWS_TYPE_ITALY:
			Cache_Italy.clear();
			break;

		case Constant.NEWS_TYPE_EUORP:
			Cache_Europ.clear();
			break;
		}
	}

}
