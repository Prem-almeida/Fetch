package com.example.fetch.model;

import java.util.List;

public class result {
	
	public result(String payer, int points) {
		super();
		this.payer = payer;
		this.points = points;
	}
	
	public result(int points) {
		super();
		this.points = points;
	}
	
	public result() {
		
	}

	private String payer;
	private int points;
	
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
	
	
	public List<result> update_result(result cur, List<result> rs) {
		//makes a list of result and updates them according to the points redreemed
		
		if(rs.isEmpty()) {
			rs.add(cur);
			return rs;
		}else {
			
			
			boolean found=false;
			for(int i=0;i<rs.size();i++) {
				
				result rsi= rs.get(i);
				if(cur.getPayer()==rsi.getPayer()) {
					found=true;
					rsi.setPoints(rsi.getPoints()+cur.getPoints());
				}
			}
			
			
			if(found==false) {
				rs.add(cur);
				return rs;
			}else {
				return rs;
			}
			
		}
		
		
	
		
	}

}
