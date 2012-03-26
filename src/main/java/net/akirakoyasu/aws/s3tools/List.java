package net.akirakoyasu.aws.s3tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class List extends S3Command {

	protected List(String cmdOptionsSyntax) {
		super(cmdOptionsSyntax, "List S3 objects");
	}

	public void command(S3URL s3url,
			RequestGenerator generator, ListPriter printer) {
		// S3クライアントを作成
		AmazonS3Client client = new AmazonS3Client(credential);
		
		ListObjectsRequest request = generator.generate(s3url);
		ObjectListing objectListing = client.listObjects(request);
		printer.print(objectListing);
		while (objectListing.isTruncated()) {
			objectListing = client.listNextBatchOfObjects(objectListing);
			printer.print(objectListing);
		}
		
		client.shutdown();
	}
	
	public static void main(String[] args) {
		List cmd = new List("[-l] [-R] <s3url>");
		
		Options options = new Options();
		options.addOption("l", "long", false, "In long format");
		options.addOption("R", "recurse", false, "Recursively list");
		CommandLine cl = cmd.parse(options, args);
		if (cl == null) {
			return;
		}
		
		@SuppressWarnings("unchecked")
		java.util.List<String> argsRemaining = cl.getArgList();
		Iterator<String> i = argsRemaining.iterator();
		S3URL s3url = new S3URL(i.next());
		
		cmd.command(s3url,
				cl.hasOption("R")? recursiveGenerator: simpleGenerator,
				cl.hasOption("l")? detailListPrinter: simpleListPrinter);
	}
	
	private static interface RequestGenerator {
		public ListObjectsRequest generate(S3URL s3url);
	}
	
	private static final RequestGenerator simpleGenerator = new RequestGenerator(){
		@Override
		public ListObjectsRequest generate(S3URL s3url) {
			return new ListObjectsRequest()
				.withBucketName(s3url.getBacket())
				.withPrefix(s3url.getKey())
				.withDelimiter("/");
		}
	};
	
	private static final RequestGenerator recursiveGenerator = new RequestGenerator(){
		@Override
		public ListObjectsRequest generate(S3URL s3url) {
			return new ListObjectsRequest()
				.withBucketName(s3url.getBacket())
				.withPrefix(s3url.getKey());
		}
	};
	
	private static interface ListPriter {
		public void print(ObjectListing objectListing);
	}
	
	private static final ListPriter simpleListPrinter = new ListPriter(){
		@Override
		public void print(ObjectListing objectListing) {
			for (String commonPrefix : objectListing.getCommonPrefixes()) {
				System.out.println(commonPrefix);
			}
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
				System.out.println(objectSummary.getKey());
			}
		}
	};
	
	private static final ListPriter detailListPrinter = new ListPriter(){
		@Override
		public void print(ObjectListing objectListing) {
			String bucketName = objectListing.getBucketName();
			for (String commonPrefix : objectListing.getCommonPrefixes()) {
				S3URL s3url = new S3URL(bucketName, commonPrefix);
				System.out.println(s3url);
			}
			DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
				S3URL s3url = new S3URL(bucketName, objectSummary.getKey());
				System.out.printf("%s\t%s\t%s%n",
						s3url,
						f.format(objectSummary.getLastModified()),
						objectSummary.getSize());
			}
		}
	};
}
