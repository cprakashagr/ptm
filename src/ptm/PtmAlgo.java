package ptm;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PtmAlgo {

	
	private FileInputStream fis;
	private FileOutputStream fos;
	private File sofile;
	private File tFile;
	private File stFile;
	private String patternText;
	
	private String[] stopWordsList;
	private int newNum;
	private int originalLength=0;
	
	private long timePtm=0;
	private long timeOrig=0;

	public PtmAlgo() {
		
		
	}
	
	public PtmAlgo(String sourceFile, String stopFile, String pattern) {
		
		/*
		 * Steps:
		 * 1. Copy the File to the local folder.
		 * 2. Do Pre Processing; Use Delimiters.
		 * 3. Stop Words. {, }.
		 * 4. Stemming
		 * 5. Redundancy Removal
		 * 6. We have 3 important files: 
		 * 		resultFinal.dat		(for PTM Search)
		 * 		sourceFile.dat		(for original search)
		 * 		WithStop.dat		(for d-limit search)
		 */
		
		patternText = pattern;
		
		// Copying the Source File
		
		sofile = new File (sourceFile);
		stFile = new File(stopFile);
		
		tFile = new File ("SourceFile.dat");
		int data = 0;
		
		try {
			fis = new FileInputStream(sofile);
			fos = new FileOutputStream(tFile);
			
			while (data != -1) {
				
				data=fis.read();
				fos.write(data);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// Do Pre processing.
		
		countOriginal();
		fetchStopWords();		
		removeStopWords();
		stemSteps();
		removeRedundancy();
		
		// Searching Technique
		/*
		 * 1. Search resultFinal.dat
		 * 2. Search WithStop.dat if not found at previous search
		 * 3. Search sourceFile.dat 
		 */
		
		// Searching the FinalResult
		long diff1 = searchResult("resultFinal.dat");
		if (diff1 == 0) {
			diff1 = searchResult("WithStop.dat");
		}
		timePtm = diff1;
		System.out.println("Diff1: "+diff1);
		long diff2 = searchResult("sourceFile.dat");
		System.out.println("Diff2: "+diff2);
		timeOrig = diff2;
	}
	
	public long getTimeOrig() {
		
		return timeOrig;
	}
	
	public long getTimePtm() {
		
		return timePtm;
	}
	
	private long searchResult(String fName) {
		
		long l1=0;
		long l2=0;
		Scanner sc;
		String word;
		File f = new File(fName);
		
		try {
			sc = new Scanner(f);
			l1=System.nanoTime();
			System.out.println("L1: "+l1);
			while (sc.hasNext()) {
				word = sc.next();
				if (word.equals(patternText)) {
					l2 = System.nanoTime();
					System.out.println("L2: " + l2);
//					System.out.println("L2 : " + l2 + ". With Iterations: " + i);					
				}
			}
			
			if (l2 != 0) {
				sc.close();
				return l2-l1;
			}
			
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public void fetchStopWords() {
		int i = 0;
		
		Scanner sc;
		String word;
		try {
			sc = new Scanner(stFile);
			
			while (sc.hasNext()) {
				i++;
				sc.next();
			}
			
			sc = new Scanner(stFile);
			System.gc();
			
			stopWordsList = new String[i];
			i=0;
			while (sc.hasNext()) {
				
				word = sc.next();
				stopWordsList[i++] = word;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void removeStopWords() {
		
		String word;
		Scanner sc;
		DataOutputStream outFileStop;
		DataOutputStream outFile;
		try {
			sc = new Scanner(sofile);
			sc.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!(){}'\"]+"));
			
			outFileStop = new DataOutputStream(new FileOutputStream(new File("WithStop.dat")));
			outFile     = new DataOutputStream(new FileOutputStream(new File("WithoutStop.dat")));
			
			while (sc.hasNext()) {
				
				word = sc.next();
				if (isStopWord(word)) {
					
					outFileStop.writeChars(word+"\n");
				}
				else {
					
					outFile.writeChars(word+"\n");
				}
			}
			
			outFileStop.close();
			outFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isStopWord(String word) {
		
		int len = stopWordsList.length;
		int i=0;
		
		for (i=0;i<len;i++) {
			
			if (stopWordsList[i].equals(word)) {
				
				return true;
			}
		}
		
		return false;
	}
	
	public int getTimeWithoutPTM() {
		
		return originalLength;
	}

	public int getTimeWithPTM() {
		
		return newNum;
	}
	
	public void stemSteps() {
		char[] w = new char[501];
		Stemming s = new Stemming();
		try {
			FileInputStream in = new FileInputStream("WithoutStop.dat");
			File file = new File("result1.txt");
	 
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException ex) {

				}
			}
	 
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("result1.txt", true)));
				while(true) {  
					int ch = in.read();
					if (Character.isLetter((char) ch)) {
						int j = 0;
						while(true) {  
							ch = Character.toLowerCase((char) ch);
							w[j] = (char) ch;
							if (j < 500) j++;
							ch = in.read();
							if (!Character.isLetter((char) ch)) {
								for (int c = 0; c < j; c++) s.add(w[c]);
								s.stem(); {
									String u;
									u = s.toString();
	      
									try {
										out.println(u);
										out.close();
									} catch (Exception e) {
									
									}
	 	   
								}
								break;
							}
						}
					}
					if (ch < 0) break;
				}
			}
			catch (IOException e) {
			}
		}
		catch (FileNotFoundException e) {
		}
	}
	
	private void countOriginal() {
		
		Scanner sc;
		try {
			sc = new Scanner(sofile);
			
			while (sc.hasNext()) {
				originalLength++;
				sc.next();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void removeRedundancy() {
		
		int i=originalLength;
		PrintWriter print;
		File outFile = new File("resultFinal.dat");
		Scanner sc;
		String word;
		
		System.out.println(sofile);
		try {
			sc = new Scanner(sofile);
			while (sc.hasNext()) {
				word = sc.next();
				if (isRedundant(word)) {
					i--;
					System.out.println("Redun " + word);
				}
				else {
					System.out.println("Non Redun " + word);
					print = new PrintWriter(new FileOutputStream(outFile, true));
					print.println(word);
					print.close();
				}				
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		newNum = i;
	}
	
	@SuppressWarnings("resource")
	private boolean isRedundant(String word) {
		
		String prevWord;
		Scanner sc;
		File nFile = new File("resultFinal.dat");
		try {
			sc = new Scanner(nFile);
			while (sc.hasNext()) {
				prevWord = sc.next();
				if (prevWord.equals(word)) {
					return true;
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}
