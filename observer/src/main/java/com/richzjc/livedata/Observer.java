package com.richzjc.livedata;


public interface Observer {

	void update(int id, Object... args);
}