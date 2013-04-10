package util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;

import objects.ICEtizen;

import org.junit.Test;

public class WorldStatesFetcherTest {

	
	
	@Test
	public void updateWorldStates() throws Exception {
		WorldStatesFetcher fetcher = new WorldStatesFetcher();
		fetcher.updateWorldStates();
		
		
		// the number of ICEtizens logged-in must be equal
		// to the number of elements in timestamp list
		assertTrue(fetcher.icetizens.size() == fetcher.timestamp.size());
		
		// list out all the icetizens
		Object[] usernameArray = fetcher.icetizens.keySet().toArray();
		System.out.println( fetcher.icetizens.keySet().toString());
		
		
		for(int k = 0 ; k < usernameArray.length; k++ ){
			
			ICEtizen user = fetcher.icetizens.get(usernameArray[k]);
			System.out.println(user.getUsername());
			
			if(user.type == 0){
				System.out.println("Type == 0 ");

			}else{
				System.out.println("Type == 1");
				System.out.println(user.getIcetizenLook().gidH);
			}

		}
	}

	
}