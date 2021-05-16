package com.example.fetch.model;

import java.util.Comparator;
import java.util.Date;

public class ComImpl implements Comparator<Transaction>{

	@Override
	public int compare(Transaction o1, Transaction o2) {
		
		Date date1;
		date1 = o1.getTimestamp();
		Date date2;
		date2 = o2.getTimestamp(); 
		
//		System.out.println("SORTING "+i+" "+date1+"Compare to"+date2+" is "+date1.compareTo(date2));

		
		
		return date1.compareTo(date2);

	}

	
	
	
	
}
