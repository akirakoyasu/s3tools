package net.akirakoyasu.aws.s3tools;

public class S3URL {
	private static final String protocol = "s3";
	private String backet;
	private String key;
	
	public S3URL(String spec) {
		String parse = spec;
		
		String protocolString = protocol + "://";
		if (parse.startsWith(protocolString)) {
			parse = parse.substring(protocolString.length());
		}
		
		int pos = parse.indexOf("/");
		if (pos < 0) {
			backet = parse;
			key = "";
		} else {
			backet = parse.substring(0, pos);
			key = parse.substring(pos + 1);
		}
	}
	
	public S3URL(String backet, String key) {
		this.backet = backet;
		this.key = key;
	}
	
	public static String getProtocol() {
		return protocol;
	}
	
	public String getBacket() {
		return backet;
	}

	public void setBacket(String backet) {
		this.backet = backet;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	@Override
	public String toString() {
		return protocol + "://" + backet + "/" + key;
	}
}
