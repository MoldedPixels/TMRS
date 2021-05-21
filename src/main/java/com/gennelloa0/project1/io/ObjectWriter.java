package com.gennelloa0.project1.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ObjectWriter {
	
	private static final Logger log = LogManager.getLogger(ObjectWriter.class);
	
	public Object readObjectFromFile(String filename) {
		log.trace("reading object from file");
		Object objects = null;
		try (ObjectInputStream o = new ObjectInputStream(new FileInputStream(filename));) {
			objects = o.readObject();
			return objects;
		} catch (Exception e) {
			log.error(e.getMessage());
			for(StackTraceElement s : e.getStackTrace()) {
				log.warn(s);
			}
		}
		return null;
	}
	public void writeObjectToFile(String filename, Object objects) {
		log.trace("saving object to file");
		Serializable s = null;
		try{
			s = (Serializable) objects;
		} catch(Exception e) {
			throw new ClassCastException("Object is not Serializable.");
		}
		try (FileOutputStream f = new FileOutputStream(new File(filename));
				ObjectOutputStream o = new ObjectOutputStream(f);) {
			o.writeObject(s);
		} catch (Exception e) {
			log.error(e.getMessage());
			for(StackTraceElement stack : e.getStackTrace()) {
				log.warn(stack);
			}
		}
	}
}
