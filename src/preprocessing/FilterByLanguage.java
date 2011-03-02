package preprocessing;

import java.io.File;

import util.Utilities;


public class FilterByLanguage {

	public static final String inputDirectory=System.getProperty("user.home")+"/wrk/datasets/ckcc/groo001";
	public static final String outputDirectory=System.getProperty("user.home")+"/wrk/datasets/ckcc/filtered/";
	public static final String condition="meta type=\"language\" value=\"nl";
	
	public static void main(String[] args) throws Exception {
		File[] fileList= Utilities.listDirContents(new File(inputDirectory), ".xml");
		for (int i=0;i<fileList.length;i++){
			System.out.println(fileList[i].getAbsolutePath());
			String text=Utilities.readTextFile(fileList[i]);
			if(text.contains(condition)){
				Utilities.writeObject(text, outputDirectory+fileList[i].getName());
			}
		}

	}

}
