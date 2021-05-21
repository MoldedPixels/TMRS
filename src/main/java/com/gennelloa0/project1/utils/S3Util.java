package com.gennelloa0.project1.utils;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.net.URL;
import java.util.Date;

public class S3Util {
	public static final Region region = Region.US_EAST_2;
	public static final String BUCKET_NAME = "2103trms";

	private static Logger log = LogManager.getLogger(S3Util.class);
	private static S3Util instance = null;
	private S3Client s3 = null;

	private S3Util() {
		s3 = S3Client.builder().region(region).build();
	}

	public static synchronized S3Util getInstance() {
		if (instance == null) {
			instance = new S3Util();
		}
		return instance;
	}

	public void uploadToBucket(String key, byte[] file) {
		log.trace("Uploading file as " + key);
		s3.putObject(PutObjectRequest.builder().bucket(BUCKET_NAME).key(key).build(), RequestBody.fromBytes(file));
		log.trace("Upload Complete");
	}

	public InputStream getObject(String key) {
		log.trace("Retrieving Data from S3: " + key);
		InputStream s = s3.getObject(GetObjectRequest.builder().bucket(BUCKET_NAME).key(key).build());
		return s;
	}

	public String generateURL(String key) {
		Regions clientRegion = Regions.US_EAST_2;
		try {
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion)
					.withCredentials(new ProfileCredentialsProvider()).build();

			// Set the presigned URL to expire after one hour.
			Date expiration = new Date();
			long expTimeMillis = expiration.getTime();
			expTimeMillis += 1000 * 60 * 60;
			expiration.setTime(expTimeMillis);

			// Generate the presigned URL.
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(BUCKET_NAME, key)
					.withMethod(HttpMethod.GET).withExpiration(expiration);
			URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

			return url.toString();
		} catch (AmazonServiceException e) {
			// The call was transmitted successfully, but Amazon S3 couldn't process
			// it, so it returned an error response.
			e.printStackTrace();
		} catch (SdkClientException e) {
			// Amazon S3 couldn't be contacted for a response, or the client
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
		}
		return "FAILED";
	}
}