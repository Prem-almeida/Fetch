package com.example.fetch.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
//import java.sql.Date;
import java.util.Date;
import java.util.HashMap;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.sql.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fetch.exception.ResourceNotFound;
import com.example.fetch.model.ComImpl;
import com.example.fetch.model.Transaction;
import com.example.fetch.model.result;
import com.example.fetch.repository.Profile_Repo;

@RestController
@RequestMapping("/fetch/v1/")
public class trans_controller {	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	@Autowired
	private Profile_Repo profileRepository;

	// get all Transactions sorted @GetMapping("/all_transs")
	@GetMapping("/all_transs")
	public List<Transaction> getAllTransactionn() {
		
		List<Transaction> all_items = new ArrayList<Transaction>();
		all_items = profileRepository.findAll();

//uncomment the next 2 statement if you care for sorting the list while displaying 
//		Comparator<Transaction> com = new ComImpl();
//		Collections.sort(all_items, com);
		return all_items;
	}
	
	// get all Transactions no Zeros @GetMapping("/all_trans")
	@GetMapping("/all_trans")
	public List<Transaction> getAllTransaction() {
		
		List<Transaction> all_items = new ArrayList<Transaction>();
		all_items = profileRepository.findAll();

//uncomment the next 2 statement if you care for sorting the list while displaying 
//		Comparator<Transaction> com = new ComImpl();
//		Collections.sort(all_items, com);
		return Remove_zeros(all_items);
	}

	// get the total available points @GetMapping("/User_points")
	@GetMapping("/User_points")
	public int totalpoints() {
		return profileRepository.get_total();
	}

	// create Transaction @PostMapping("/add")
	@PostMapping("/add")
	public Transaction CreateTransaction(@RequestBody Transaction trans) {

		if (trans.getTimestamp() == null) {
			Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
			trans.setTimestamp(timestamp1);
		} else {

			trans.setTimestamp(trans.getTimestamp());

		}
		return profileRepository.save(trans);
//	    

	}

	// delete by i@DeleteMapping("remove_all/")
	@DeleteMapping("remove_all/")
	public List<Transaction> remove_e() {
		List<Transaction> todelete = profileRepository.findAll();
		List<Transaction> deleted = new ArrayList<Transaction>();
		
		for(int i=0;i<todelete.size();i++) {
			Transaction x = todelete.get(i);
			if(x.getPoints()==0)
				
			{
				deleted.add(x);
				System.out.println("Deleted: {"+x.getTransaction_no()+", "+x.getPayer()+","+x.getPoints()+", "+x.getTimestamp());
				profileRepository.delete(x);
			}
			
		}
				
		return deleted;
	}
	
	// Remove Empty @DeleteMapping("remove/{id}")
	@DeleteMapping("remove/{id}")
	public ResponseEntity<Map<String, Boolean>> remove(@PathVariable long id) {
			
			Transaction todelete = profileRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFound("The Item you are trying to remove does not exsist:" + id));

			profileRepository.delete(todelete);
			Map<String, Boolean> map = new HashMap<>();
			map.put("Deleted ID:" + id, Boolean.TRUE);
			return ResponseEntity.ok(map);
		}

	// redeem @PutMapping("/redeem")
	@PutMapping("/redeem/{point}")
	public List<result> redeem(@PathVariable int point) {

		
		System.out.println("Points to redeem: "+point);		
		
		// create a sub item to return
		List<result> sub = new ArrayList<result>();
		List<Transaction> all_items = new ArrayList<Transaction>();

		// get all the items and sort them using the comparator for remove
		all_items = getAllTransaction();
		Comparator<Transaction> com = new ComImpl();
		Collections.sort(all_items, com);

		//Store points to remove in a temp variable
		int temp_r_point = point;
		
		
		// check the total points for remove
		int total = profileRepository.get_total();
		System.out.println("\nPoints to redeem:" + point + " total:" + total + " items in db:" + (all_items.size()) + "\n\n");

		if (point > total) {

			result value = new result("Message:" + (point - total) + " are needed for reedemtion ", total);
			// update a message in sub which will be returned to the system
			sub.add(value);
			return sub;
		} else {

			// main Code if the we have enough points to remove

			// counter i to iterate through the transaction items
			int i = 0;
			int cur_point = 0;

			// main Loop
			while (temp_r_point != 0) {

				// save the current transaction in TR
				Transaction tr = all_items.get(i);

				// cur_points is TR.POINTS
				cur_point = tr.getPoints();
				System.out.println("Temp_points:" + temp_r_point + " current_points" + cur_point);
				// current transaction points lets say 300 is it greater than
				// what we want want to redeem lets 200==> update to 100
				// if yes then we will have a remainder and we need to update that item
				// accordingly
				// or else go to else part
				if (cur_point >= temp_r_point) {

					int remaineder = cur_point - temp_r_point; // 100=300-200
					System.out.println("removed " + (temp_r_point) + " from: " + tr.getPayer());

					if (remaineder == 0) {
						tr.setPoints(0);
						profileRepository.save(tr);
						// remove(tr.getTransaction_no());
					} else {
						tr.setPoints(remaineder);
						profileRepository.save(tr);
					}

					result value = new result(tr.getPayer(), (temp_r_point) * -1);
					System.out.println("\nbefore Payer:" + value.getPayer() + " points:" + value.getPoints());
					sub = update_result(value, sub);
					temp_r_point = 0;
					i++;

				} else {

					System.out.println("removed " + cur_point + " from:" + tr.getPayer());
					temp_r_point = temp_r_point - cur_point;
					System.out.println("\nbefore Payer:" + tr.getPayer() + " points:" + cur_point * -1);
					result value = new result(tr.getPayer(), cur_point * -1);
					sub = update_result(value, sub);
//                  remove(tr.getTransaction_no());
					tr.setPoints(0);
					profileRepository.save(tr);
					i++;
				}

			}

		}

		return sub;

	}

	//Update Result Method
	public List<result> update_result(result cur, List<result> rs) {
		System.out.println("\n-------Update_result-Start--------\n");
		if (rs.isEmpty()) {
			System.out.println("the result list is empty added "+cur.getPayer()+":"+cur.getPayer());
			rs.add(cur);
			System.out.println("\n-------Update_result-End----------\n");
			return rs;
		} else {

			boolean found = false;
			for (int i = 0; i < rs.size(); i++) {

				result rsi = rs.get(i);
				if (cur.getPayer().equals(rsi.getPayer())) {
					System.out.println("\n\nAlready Exsist" + cur.getPayer() + "=" + rsi.getPayer() + "\n\n");
					found = true;
					rsi.setPoints(rsi.getPoints() + cur.getPoints());
				}
			}

			if (found == false) {
				rs.add(cur);
				System.out.println("added to result list"+cur.getPayer()+":"+cur.getPayer());
				System.out.println("\n-------Update_result-End----------\n");
				return rs;
			} else {
				System.out.println("\n-------Update_result-End----------\n");
				return rs;
			}

		}

	}

	
	//will remove duplicate zeros
	public List<Transaction> Remove_zeros(List<Transaction> ts) {

		System.out.println("\n-------Remove-Start---" + ts.size() + "-----\n");
		List<Transaction> no_zero = new ArrayList<Transaction>();
		
		HashMap<String,Integer> track_payer = new HashMap<String,Integer>();
		int index=0;
		
		
		for (int i = 0; i < (ts.size()); i++) {

			Transaction tr = ts.get(i);

			System.out.print(tr.getTransaction_no());

			if (tr.getPoints() == 0) {

				
				if (track_payer.containsKey(tr.getPayer()) ) {
					System.out.println("-->Remove " + tr.getPayer() + ": " + tr.getPoints() + " is zero and already exsist\n");
				} else {
					
					no_zero.add(tr);
					System.out.println("Track Update new value: key:"+tr.getPayer()+" value:"+index);
					track_payer.put(tr.getPayer(),index);
					index++;
					System.out.println("-->Keep " + tr.getPayer() + ": " + tr.getPoints() + "\n");
				}
			} else {
				
				
				if (track_payer.containsKey(tr.getPayer())) {
					
					if(no_zero.get(track_payer.get(tr.getPayer())).getPoints()==0) {
						
						System.out.println("-->Null item update "+no_zero.get(track_payer.get(tr.getPayer())).getPayer());
						System.out.println("Remove old "+no_zero.get(track_payer.get(tr.getPayer())).getPayer()+" from no_zero "+track_payer.get(tr.getPayer())+"");
										
						int remove=track_payer.get( tr.getPayer() );
						System.out.println("Remove "+remove);
						no_zero.remove(remove);
						
						System.out.println("Track_payer update: for "+tr.getPayer()+" index old "+track_payer.get(tr.getPayer())+" index new "+index+"");
						track_payer.put(tr.getPayer(),index);
						System.out.println("-->Keep " + tr.getPayer() + ": " + tr.getPoints() + "\n");
						no_zero.add(tr);
						index++;
						
					}else {
						System.out.println("-->Keep " + tr.getPayer() + ": " + tr.getPoints() + "\n");
						System.out.println("Track Update new value: key:"+tr.getPayer()+" value:"+index);
						track_payer.put(tr.getPayer(),index);
						no_zero.add(tr);
						index++;
					}
					
					
				}else {
					
					no_zero.add(tr);
					System.out.println("Track Update new value: key:"+tr.getPayer()+" value:"+index);
					track_payer.put(tr.getPayer(),index);
					index++;

					System.out.println("-->Keep " + tr.getPayer() + ": " + tr.getPoints() + "\n");
				}
				
			}

		}
		System.out.println("-------Remove-END----------\n\n");
		return no_zero;
	}

	//test @GetMapping("/test")
	@GetMapping("/test")
	public void test() {
		
		System.out.println("test");
	
	}

}
