package com.example.fetch.model;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(name="profile")
public class Transaction {
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long transaction_no;
	
	@Column(name ="payer")
	private String payer;
	
	@Column(name ="points")
	private int points;
	
	@Column(name="time_stamp")
	private java.sql.Timestamp time_stamp;
	

	public Transaction() {     }
	
	
	
	public Transaction(String payer, int points, java.sql.Timestamp time_stamp) 
	{
		super();
		this.payer = payer;
		this.points = points;		
		this.time_stamp = time_stamp;
	}

	
	public long getTransaction_no() {
		return transaction_no;
	}

	public void setTransaction_no(long transaction_no) {
		this.transaction_no = transaction_no;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public Timestamp parseTimestamp(String timestamp) {
	    try {
	    	System.out.println("\nParse Timestamp Unformated "+timestamp);
	        System.out.println("Parse Timestamp formated "+format.parse(timestamp).getTime());
	    	return new Timestamp(format.parse(timestamp).getTime());
	        
	    } catch (ParseException e) {
	        throw new IllegalArgumentException(e);
	    }
	}
	
	
	public java.sql.Timestamp getTimestamp() {
		return time_stamp;
	}

	public void setTimestamp(java.sql.Timestamp time_stamp) {
		this.time_stamp = time_stamp;
	}
	


	

	  

	
}
	

