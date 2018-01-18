package io.hahahahaha.petiterpc.test.provider;

import io.hahahahaha.petiterpc.common.Provider;

@Provider
public class DemoProvider implements DemoInterface {

	@Override
	public String fuck(String strf) {
		System.out.println(strf);
		return "fuck";
	}

}
