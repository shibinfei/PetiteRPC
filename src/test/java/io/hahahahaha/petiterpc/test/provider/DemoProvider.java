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

	@Override
	public void testVoid() {
		System.out.println("executing void function");
	}

	@Override
	public void testOverload(String s) {
		System.out.println("overload");
	}

	@Override
	public int testOverload(String s, int i) {
		System.out.println("overload2");
		return 0;
	}
	
	public long testOverload(String s, String i) {
		System.out.println("overload2");
		return 0;
	}

}
