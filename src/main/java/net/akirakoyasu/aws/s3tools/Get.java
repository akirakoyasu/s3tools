package net.akirakoyasu.aws.s3tools;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.CommandLine;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;

public class Get extends S3Command {

	protected Get(String cmdOptionsSyntax) {
		super(cmdOptionsSyntax, "Get S3 object");
	}

	public void command(S3URL s3url, File path) {
		// S3クライアントを作成
		AmazonS3Client client = new AmazonS3Client(credential);
		
		String key = s3url.getKey();
		// ディレクトリであれば、その配下にコピーする
		if (path.isDirectory()) {
			int pos = key.lastIndexOf("/");
			String name = key.substring(pos + 1);
			path = new File(path, name);
		}
		
		client.getObject(
				new GetObjectRequest(s3url.getBacket(), key),
				path);
		
		client.shutdown();
	}
	
	public static void main(String[] args) {
		Get cmd = new Get("<s3url> [<path>]");
		
		CommandLine cl = cmd.parse(args);
		if (cl == null) {
			return;
		}
		
		@SuppressWarnings("unchecked")
		List<String> argsRemaining = cl.getArgList();
		Iterator<String> i = argsRemaining.iterator();
		S3URL s3url = new S3URL(i.next());
		File path = new File(i.hasNext()? i.next(): "./");
		
		cmd.command(s3url, path);
	}
}
