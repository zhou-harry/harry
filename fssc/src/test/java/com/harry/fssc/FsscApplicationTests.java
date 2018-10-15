package com.harry.fssc;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.harry.fssc.service.SequenceService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FsscApplicationTests {

	@Autowired
	private SequenceService service;
	
	@Test
	public void contextLoads() {
		System.out.println("---------------");
		String nextVal = service.getNextVal("SEQ_ROLEID");
		System.out.println(nextVal);
	}

	
}
