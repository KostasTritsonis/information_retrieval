package util;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class CsvReader {
	private String path;
	
	public CsvReader(String path) {
		this.path = path;
		
	}

	public ArrayList<String[]> ReadCsv(){
		Scanner reader = null;
		String line = "";
		ArrayList<String[]> A = new ArrayList<>();
		
		try {
			reader = new Scanner(new FileReader(path));
			while(reader.hasNext()) {
					line = reader.nextLine();
					String[] row = line.split(",");
					A.add(row);
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		reader.close();

		return A;
	}

}
