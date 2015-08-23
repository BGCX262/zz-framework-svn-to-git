package com.youthor.zz.common.m.db.adapter;

import java.sql.Connection;

import com.youthor.zz.common.xml.XmlElement;

abstract public class DBAdapter {

	
	public abstract void init(XmlElement dbConfig);

	public abstract void beginTransaction();

	public abstract void commit();

	public abstract void rollBack();

	public abstract Connection getConnection();

}
