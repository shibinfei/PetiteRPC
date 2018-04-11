package io.hahahahaha.petiterpc.test.provider;

import io.hahahahaha.petiterpc.common.Provider;

@Provider(threadCoreSize = 2, degradation = true)
public class DemoProvider implements DemoInterface {

	@Override
	public String fuck(String strf)  {
		try {
			Thread.sleep(2000);
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println(strf);
		return "fuck";
	}

}
