package net.akirakoyasu.aws.s3tools;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.CommandLine;

import com.amazonaws.services.s3.AmazonS3Client;

public class Put extends S3Command {

	protected Put(String cmdOptionsSyntax) {
		super(cmdOptionsSyntax, "Put file to S3");
	}

	public void command(File file, S3URL s3url) {
		// S3クライアントを作成
		AmazonS3Client client = new AmazonS3Client(credential);

		client.putObject(s3url.getBacket(), s3url.getKey(), file);

		client.shutdown();
	}
	
	public static void main(String[] args) {
		Put cmd = new Put("<file> <s3url>");

		CommandLine cl = cmd.parse(args);
		if (cl == null) {
			return;
		}
		
		@SuppressWarnings("unchecked")
		List<String> argsRemaining = (List<String>) cl.getArgList();
		Iterator<String> i = argsRemaining.iterator();
		File file = new File(i.next());
		S3URL s3url = new S3URL(i.next());

		cmd.command(file, s3url);
	}
}
