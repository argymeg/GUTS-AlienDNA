/*
 * 2016 GUTS Hackathon entry. 
 * Challenge: Alien DNA
 * Team 12: Pair of Genes
 * Argyris Megalios, Robin Shaw, Alex Yang
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import FormatIO.*;

public class FinalClass {
	public static void main (String [] args)
	{
		Console con = new Console("GUTS Hackathon Alien DNA Challenge");
		String filechoice = "";
		int windowsize = 0;
		
		con.println("Enter your choice of chromosome (1k, 10k, 100k, 10m)");		
		String choice = con.readWord();
		
		if (choice.equals("1k"))
		{
			filechoice = "seqlist.txt";
			windowsize = 5;
		}
		else if (choice.equals("10k"))
		{
			filechoice = "seqlist2.txt";
			windowsize = 10;
		}
		else if (choice.equals("100k"))
		{
			filechoice = "seqlist3.txt";
			windowsize = 10;
		}
		else if (choice.equals("10m"))
		{
			con.println("Not quite there yet.");
			for(;;){}
		}
		else
		{
			con.println("Please learn to read first.");
			for(;;){}
		}

		FileIn seqListFile = new FileIn("Data/" + filechoice);

		//Read sequence filenames into ArrayList
		ArrayList<String> seqFileList = new ArrayList<String>();
		for (int i=0; i<=3; i++)
		{
			seqFileList.add(seqListFile.readLine());
		}

		//Read actual sequences into ArrayList
		ArrayList<ArrayList<String>> seqList = new ArrayList<ArrayList<String>>();
		for (int i=0; i<=3; i++)
		{
			seqList.add(readFile(seqFileList.get(i)));
		}


		//Find all beginnings
		ArrayList<String> startList = new ArrayList<String>();
		for (int i=0; i<=3; i++)
		{
			int seqfrags = seqList.get(i).size();
			for (int k=0; k<=seqfrags-1;k++)
			{
				if(seqList.get(i).get(k).length()>=windowsize)
				{
					startList.add(seqList.get(i).get(k).substring(0,windowsize));

				}
				else
				{}
			}
		}
		
		//Find beginning frequency
		HashSet<String> uniqueStarts = new HashSet<String>(startList);
		ArrayList <String> startFreqList = new ArrayList<String>();
		ArrayList <Integer> startFreqFreq = new ArrayList<Integer>();

		for (String key : uniqueStarts)
		{
			startFreqList.add(key);
			startFreqFreq.add(Collections.frequency(startList, key));			
		}
		
		//Find most frequent ("correct") beginning
		int keyOfMax = 0;
		int max = 0;
		for (int i=0; i<= startFreqList.size() -1; i++)
		{
			if (startFreqFreq.get(i)>(Integer) max)
			{
				max = startFreqFreq.get(i);
				keyOfMax = i;
			}
			else
			{

			}
		}
		System.out.println(startFreqFreq.get(keyOfMax));
		System.out.println(startFreqList.get(keyOfMax));

		//Find longest string that contains the correct beginning
		ArrayList<String> firstCandidates = new ArrayList<String>();
		for (int i=0; i<=3; i++)
		{
			int seqfrags = seqList.get(i).size();
			for (int k=0; k<=seqfrags-1;k++)
			{
				Pattern firstCandidate = Pattern.compile(startFreqList.get(keyOfMax) + ".+");
				Matcher matcher = firstCandidate.matcher(seqList.get(i).get(k));
				while (matcher.find())
				{
					firstCandidates.add(matcher.group());
				}
			}
		}

		//Puts the starting string into battle position
		int max2=0;
		String biggestFrag = "";
		for(int i=0; i<=firstCandidates.size()-1;i++)
		{
			if (firstCandidates.get(i).length()>max2)
			{
				max2 = firstCandidates.get(i).length();
				biggestFrag = firstCandidates.get(i);
			}
		}

		String reconstruction = biggestFrag;
		String dangling = "";
		String nextBiggestFrag = "" ;
		FileOut fout = new FileOut("Data/" + choice + "-output.txt");

		
		//Where the magic happens.
		findSequence:
		for (;;)
		{
			//Sets dangling end to end of what we have so far (reconstruction) or dies trying.
			try 
			{
				dangling = reconstruction.substring(reconstruction.length()-windowsize, reconstruction.length());
				System.err.println(dangling);
			}
			catch (StringIndexOutOfBoundsException e) {{System.err.println("That's all, folks!");break findSequence;}}

			//Finds all fragments that contain dangling sequence
			ArrayList<String> contCandidates = new ArrayList<String>();
			for (int i=0; i<=3; i++)
			{
				int seqfrags = seqList.get(i).size();
				for (int k=0; k<=seqfrags-1;k++)
				{
					Pattern contCandidate = Pattern.compile(".+" + dangling + ".+");
					Matcher matcher = contCandidate.matcher(seqList.get(i).get(k));
					while (matcher.find())
					{
						contCandidates.add(matcher.group());//System.err.println(matcher.group());
					}
				}
			}
			
			//Gets length of current continuation fragment 
			int max3=0;
			for(int i=0; i<=contCandidates.size()-1;i++)
			{
				if (contCandidates.get(i).length()>max3)
				{
					max3 = contCandidates.get(i).length();
					nextBiggestFrag = contCandidates.get(i);
				}
			}
			
			//Splices fragments together
			reconstruction = strconnect(reconstruction, nextBiggestFrag, reconstruction.length(), max3);
			fout.println(reconstruction);
		}
		fout.close();
	}

	private static ArrayList<String> readFile(String seq)
	{
		FileIn fin = new FileIn(seq);
		String gen = "";
		gen = fin.getLine();
		ArrayList<String> genomepieces = new ArrayList<String>();
		Pattern genomepiece = Pattern.compile("[ABCDEFG]+");
		Matcher matcher = genomepiece.matcher(gen);
		while (matcher.find())
		{
			genomepieces.add(matcher.group());
		}
		return genomepieces;
	}

	private static boolean compStrings (String string1, String string2)
	{
		return string1.equals(string2);
	}

	private static String strContain(String string1, String string2, int k, int strLength2)
	{
		String Str3;
		if(string1.contains(string2))
		{
			Str3 = string1;
		}
		else
		{
			Str3 = string1 + string2.substring(k+5, strLength2);
		}
		return Str3;
	}

	private static String strconnect(String Str1, String Str2, int strLength1, int strLength2)
	{
		boolean result;
		String Str3 = "";
		for(int i = 0; i<=strLength1-5;i++)
		{
			String subStr1 = Str1.substring(i,i+5);
			for (int k = 0; k <= strLength2-5; k++)
			{
				String subStr2 = Str2.substring(k, k+5);
				result = compStrings(subStr1,subStr2);
				if(result)
				{
					String possMatch1 = Str1.substring(i, strLength1);
					String possMatch2 = Str2.substring(k, strLength2);
					//The maximum length allowed is actually the smallest of the two strings' lengths!
					int maxLength = Math.min(possMatch1.length(), possMatch2.length());
					possMatch1 = possMatch1.substring(0, maxLength);
					possMatch2 = possMatch2.substring(0, maxLength);
					result = compStrings (possMatch1, possMatch2);
					if(result)
					{
						Str3 = strContain(Str1, Str2, k, strLength2);
					}
					else
					{
						result=false; 
					}
				}
			}

		}
		return Str3;
	}

	//Disused but interesting code starts here.


	/*	
	//Part of main
	//Find matches
	//Counts through "Sequence a"
	for (int i=0; i<=2; i++)
	{
		int seq1frags = seqList.get(i).size();
		//Counts through "Sequence b"
		for (int k=i+1; k<=3; k++)
		{
			int seq2frags = seqList.get(k).size();
			//Counts through fragments of sequence a
			for (int x=0; x <= seq1frags-1; x++)
			{
				//Counts through fragments of sequence b
				for (int y=0; y <= seq2frags-1; y++)
				{
					if (findMatch(seqList.get(i).get(x), seqList.get(k).get(y)))
						System.err.println(i + " " + x + "  " + k + " " + y);
				}
			}

		}
	}

	//findMatch helper method

	private static boolean findMatch(String Str1, String Str2)
	{

		//Console con = new Console();
		boolean result = false;
		int strLength1 = Str1.length();
		int strLength2 = Str2.length();

		matchSearch:
			for(int i = 0; i<=strLength1-5;i++)
			{
				String subStr1 = Str1.substring(i,i+5);
				for (int k = 0; k <= strLength2-5; k++)
				{
					String subStr2 = Str2.substring(k, k+5);
					result = compStrings(subStr1,subStr2);
					if(result)
					{
						String possMatch1 = Str1.substring(i, strLength1);
						String possMatch2 = Str2.substring(k, strLength2);
						//The maximum length allowed is actually the smallest of the two strings' lengths!
						int maxLength = Math.min(possMatch1.length(), possMatch2.length());
						possMatch1 = possMatch1.substring(0, maxLength);
						possMatch2 = possMatch2.substring(0, maxLength);
						result = compStrings (possMatch1, possMatch2);
						if(result)
						{
							//con.print("Yes!!!" + i + " " + k); 
							//System.out.println(maxLength);
							break matchSearch;
						}
						else
						{
							result=false; 
						}
					}
				}
			}
		return result;
	}	

	 */		
}
