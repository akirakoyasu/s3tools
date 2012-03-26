package net.akirakoyasu.aws.s3tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class S3URLTest {

	@Test
	public void testToString() {
		S3URL s3url = new S3URL("s3://backet/key");
		assertEquals("s3://backet/key", s3url.toString());
	}
	
	@Test
	public void testToString2() {
		S3URL s3url = new S3URL("backet/key");
		assertEquals("s3://backet/key", s3url.toString());
	}
	
	@Test
	public void testToString3() {
		S3URL s3url = new S3URL("s3://backet");
		assertEquals("s3://backet/", s3url.toString());
	}
	
	@Test
	public void testToString4() {
		S3URL s3url = new S3URL("backet");
		assertEquals("s3://backet/", s3url.toString());
	}
	
	@Test
	public void testToString5() {
		S3URL s3url = new S3URL("backet", "key");
		assertEquals("s3://backet/key", s3url.toString());
	}

}
