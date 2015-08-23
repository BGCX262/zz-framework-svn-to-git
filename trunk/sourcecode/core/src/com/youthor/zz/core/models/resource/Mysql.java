package com.youthor.zz.core.models.resource;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.youthor.zz.common.ZzContext;
import com.youthor.zz.common.m.db.adapter.DBAdapter;
import com.youthor.zz.common.xml.XmlElement;

public class Mysql extends DBAdapter {

    private DataSource ds = null;
    private boolean initFlag = false;
    private XmlElement config = null;

    public void init(XmlElement dbConfig){
        try{
            this.config = dbConfig;
            this.ds = this.getDataSource();
            this.initFlag = true;
        }
        catch(Exception e){
            throw new IllegalMonitorStateException("can not init mysql dbdapter:" + e.getMessage());
        }
    }

    public void beginTransaction() {
        this.checkStatus();
        try{
            this.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        }
        catch(Exception e){
            throw new IllegalAccessError(e.getMessage());
        }
    }

    public void commit() {
        this.checkStatus();
        try{
            this.getConnection().commit();
        }
        catch(Exception e){
            throw new IllegalAccessError(e.getMessage());
        }
    }

    public void rollBack() {
        this.checkStatus();
        try{
            this.getConnection().rollback();
        }
        catch(Exception e){
            throw new IllegalAccessError(e.getMessage());
        }
    }

   @Override
   public void finalize(){
       try{
           super.finalize();
           ZzContext zzContext = ZzContext.getContext();
           Connection connection = (Connection)zzContext.getObject("zz_connection");
           if(connection != null && (!connection.isClosed())){
               connection.close();
           }
           if(ds != null){
               ((ComboPooledDataSource)ds).close();
           }
       }
       catch(Throwable e){
           e.printStackTrace();
       }
   }

   protected void checkStatus(){
       if(!initFlag){
           throw new IllegalMonitorStateException("the adapter is not inited!");
       }
   }

   protected DataSource getDataSource() throws Exception {
       String connType = this.config.getSubNodeValue("conn_type");
       if("jndi".equalsIgnoreCase(connType)){
           String jndiName = this.config.getSubNodeValue("jndi_name");
           Context jndiContext = new InitialContext();
           return (DataSource)jndiContext.lookup(jndiName);   
       }
       if("datasource".equalsIgnoreCase(connType)){
          return this.getComboPooledDataSource();
       }
       return null;
   }

   public Connection getConnection() {
       this.checkStatus();
       try{
           ZzContext zzContext = ZzContext.getContext();
           Connection connection = (Connection)zzContext.getObject("zz_connection");
           if(connection == null || connection.isClosed()){
               if(this.ds != null){
                   connection = this.ds.getConnection();
               }
               else{
                   Class.forName("com.mysql.jdbc.Driver"); 
                   String userName = this.config.getSubNodeValue("username");
                   String password = this.config.getSubNodeValue("password");
                   String url = (String) this.config.getSubNodeValue("url");
                   connection =  DriverManager.getConnection(url,userName,password); 
               }
               zzContext.addObject("zz_connection", connection);
           }
           return connection;
           
       }
       catch(Exception e){
           throw new IllegalMonitorStateException("ds err:" + e.getMessage());
       }
   }
   
   //refer http://baike.baidu.com/view/920062.htm
   protected DataSource getComboPooledDataSource() throws Exception{
       String userName = this.config.getSubNodeValue("username");
       String password = this.config.getSubNodeValue("password");
       String url = this.config.getSubNodeValue("url");
       
       int maxPoolSize = this.getIntValueByObj(this.config.getSubNodeValue("max_pool_size"), 00);
       int minPoolSize = this.getIntValueByObj(this.config.getSubNodeValue("min_pool_size"), 10);
       int maxIdleTime = this.getIntValueByObj(this.config.getSubNodeValue("max_idle_time"), 30);
       int initialPoolSize = this.getIntValueByObj(this.config.getSubNodeValue("initial_pool_size"), 20);
       int acquireIncrement = this.getIntValueByObj(this.config.getSubNodeValue("acquire_increment"), 2);
       int acquireRetryAttempts = this.getIntValueByObj(this.config.getSubNodeValue("acquire_retry_attempts"), 30);

       ComboPooledDataSource cpds = new ComboPooledDataSource();
       cpds.setDriverClass("com.mysql.jdbc.Driver");
       cpds.setJdbcUrl(url);
       cpds.setUser(userName);
       cpds.setPassword(password);
       
       cpds.setMaxPoolSize(maxPoolSize);
       cpds.setMinPoolSize(minPoolSize);
       cpds.setMaxIdleTime(maxIdleTime);
       cpds.setInitialPoolSize(initialPoolSize);
       cpds.setAcquireIncrement(acquireIncrement);
       cpds.setAcquireRetryAttempts(acquireRetryAttempts);
       cpds.setTestConnectionOnCheckin(false);
       cpds.setTestConnectionOnCheckout(false);

       return cpds;
   }

   protected int getIntValueByObj(Object obj, int defaultValue){
       if(obj == null){
           return defaultValue;
       }
       try{
           int ret = Integer.parseInt(obj.toString());
           return ret;
       }
       catch(Exception e){
           e.printStackTrace();
           return defaultValue;
       }
   }
}