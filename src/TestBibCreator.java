import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
public class TestBibCreator
{
    static int count = 0;
    public static void main(String[] args) throws IOException
    {
        Scanner scanner = new Scanner(System.in);   
        System.out.println("Welcome to BibCreator!");
        
        // 1. Ensuring all .bib files are readable. 
        //    If file is empty or does not exist, an error message is displayed 
        //    and the application stops.
        for (int i = 1; i <=10; i++) 
        {           
            try
            {   
                FileInputStream fileName = new FileInputStream("Latex" + i + ".bib");
                Path latexFile = Paths.get("Latex" + i + ".bib");               
                long fileSize = 0;
                try
                {
                    fileSize = Files.size(latexFile);       
                } 
                catch (IOException e) 
                {
                    System.out.println(e.getMessage());
                }
                scanner = new Scanner(fileName);
                if(fileSize == 0)
                {
                    System.out.println("Error! File latex" + i + ".bib cannot be opened!\n"
                            + "This file is empty. Program will terminate now.\n");
                }
                scanner.close();
            }
            catch (FileNotFoundException e) 
            {
                scanner.close(); //To close any opened files
                System.out.println("Could not open input file Latex" + i + ".bib for reading\n"
                        + "Please check if file exists! Program will terminate after closing any opened files\n");
                System.exit(0);
            }
        }
        
        // 2. a-Verifying validity of .bib files for conversion.
        // 	  b-Creating IEEE, ACM, NJ files.
        //    c-Converting into IEEE, ACM, and NJ.
        try
        {
            processFilesForValidation();
        } 
        catch (FileInvalidException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
   
       
        
        scanner = new Scanner(System.in);
        int error=0;
        
        while(error!=2)
        {
	        System.out.print("\n\nPlease, enter the name of the file you wish to review or enter a wrong name twice to exit: ");       
	        String fileToReview = scanner.next();
	        File file = new File(fileToReview);
	        try 
	        {
		        FileReader fileReader = new FileReader(file);
		        int data = fileReader.read(); 
		        while(data != -1) 
		        {
		        System.out.print((char)data);
		        data = fileReader.read();
		        }
		        fileReader.close();
	        } 
	        catch (FileNotFoundException e) 
	        {
	        	error++;
	        	System.out.println("\nCould not open input file. File does not exist; possibly it could not be created!");
	        	if(error==1)
		        {
		        	System.out.println("However, you will be allowed another chance to enter another file name.");
		        }
		        if(error==2)
		        {
		        	System.out.println("\nSorry, I am unable to display your desired files! Program will exit");
		        	System.exit(0);
		        }
	        } 
	        
	        
        }
    
        scanner.close();
      
    }//end of main
    
    
    
    
    // ======= METHODS ========

    
    public static void processFilesForValidation() throws FileInvalidException, IOException
    {   
        Scanner[] sc = new Scanner[10];
        PrintWriter[] pw = new PrintWriter[10];
        File[] latFile = new File[10];
        File ieeeFiles = null;
        
        for (int i = 1; i <= sc.length; i++) 
        {   
            File latexFiles = new File("Latex" + i + ".bib");
            File acmFiles = null;
            File njFiles = null;
            String[] words = null;
            String[] field = new String[11];
            FileReader fr = null;
            count=0;
            try
            {
                fr = new FileReader(latexFiles);
            } 
            catch (FileNotFoundException e) 
            {
                e.printStackTrace();
            }
            BufferedReader br = new BufferedReader(fr);
            String str;
            field[0] = "author={},";
            field[1] = "journal={},";
            field[2] = "title={},";
            field[3] = "year={},";
            field[4] = "volume={},";
            field[5] = "number={},";
            field[6] = "pages={},";
            field[7] = "keywords={},";
            field[8] = "doi={},";
            field[9] = "ISSN={},";
            field[10] = "month={},";
            while((str=br.readLine()) != null)
            {
                words=str.split(" ");
                for(String word : words)
                {
                    for (int j = 0; j < field.length; j++) 
                    {
                        if(word.equals(field[j]))
                        {
                            count++;
                            if(count==1)
                            {
                                System.out.println("\nError: Detected Empty Field!");
                                System.out.println("============================");
                                System.out.println("\nProblem detected with input file: Latex"+i+".bib");
                                System.out.println("File is invalid: Field " + field[j] + " is Empty. Processing stopped at this point. Other empty fields may be present as well!");
                                ieeeFiles = new File("ieee" + i + ".json");
                                ieeeFiles.delete();
                                System.out.println("\nWARNING: ieee"+i+".json was deleted");
                                acmFiles = new File("acm" + i + ".json");
                                acmFiles.delete();
                                System.out.println("\nWARNING: acm"+i+".json was deleted");
                                njFiles = new File("nj" + i + ".json");
                                njFiles.delete();
                                System.out.println("\nWARNING: nj"+i+".json was deleted");
                            }
                        }   
                    }
                }               
            } // end of while loop
        } // end of for loop
        for (int i = 1; i < sc.length; i++) 
        {
            // Converting into IEEE format.
            // For the conversion into IEEE, ACM, and NJ format:
            // Inspired by lekside1's BibCreator solution on GitHub.
            // (https://github.com/lekside1/BibCreator/blob/master/BibCreator/src/filesCreator.java)
            // Array List for each field
        	
            ArrayList<String> ieeeAuthors = new ArrayList<String>();
            ArrayList<String> ieeeVolume = new ArrayList<String>();
            ArrayList<String> ieeeNumber = new ArrayList<String>();
            ArrayList<String> ieeeJournal = new ArrayList<String>();
            ArrayList<String> ieeePages = new ArrayList<String>();
            ArrayList<String> ieeeYear = new ArrayList<String>();
            ArrayList<String> ieeeTitle = new ArrayList<String>();
            ArrayList<String> ieeeMonth = new ArrayList<String>();
            try
            {
        		latFile[i] = new File("Latex"+i+".bib");
        		pw[i] = new PrintWriter(new FileOutputStream("ieee"+i+".json"));     
                sc[i] = new Scanner(new FileInputStream(latFile[i]));
            }
            catch(FileNotFoundException e)
            {
                System.out.println(e.getMessage());
            }
            
            // Fetching specific strings and storing them into corresponding array
            
            while(sc[i].hasNextLine())
            {
                String line = sc[i].nextLine();
                if((line.indexOf("author={")) >-1)
                {
                    ieeeAuthors.add((line));
                }
                if((line.indexOf("volume={")) >-1)
                {
                    ieeeVolume.add(line);
                }
                if((line.indexOf("number={")) >-1)
                {
                    ieeeNumber.add((line));
                }
                if((line.indexOf("journal={")) >-1)
                {
                    ieeeJournal.add((line));
                }
                if((line.indexOf("pages={")) >-1)
                {
                    ieeePages.add((line));
                }
                if((line.indexOf("year={")) >-1)
                {
                    ieeeYear.add((line));
                }
                if((line.indexOf("title={")) >-1)
                {
                    ieeeTitle.add((line));
                }
                if((line.indexOf("month={")) >-1)
                {
                    ieeeMonth.add((line));
                }
            }
            
            // Replacing parts of Latex files to conform with IEEE standards
            
            for(int j = 0; j < ieeeAuthors.size();  j++)
            {
                ieeeAuthors.set(j,ieeeAuthors.get(j).replace("author={",""));
                ieeeAuthors.set(j,ieeeAuthors.get(j).replace(" and", ","));
                ieeeAuthors.set(j,ieeeAuthors.get(j).replace("},", "."));
            }
            for(int j = 0; j < ieeeTitle.size(); j++)
            {
                ieeeTitle.set(j,ieeeTitle.get(j).replace("title={","\""));
                ieeeTitle.set(j,ieeeTitle.get(j).replace("},","\","));  
            }
            for(int j = 0; j < ieeeJournal.size(); j++)
            {
                ieeeJournal.set(j,ieeeJournal.get(j).replace("journal={",""));
                ieeeJournal.set(j,ieeeJournal.get(j).replace("},", ","));   
            }
            for(int j = 0; j < ieeeVolume.size(); j++)
            {
                ieeeVolume.set(j,ieeeVolume.get(j).replace("volume={","vol. "));
                ieeeVolume.set(j,ieeeVolume.get(j).replace("},", ",")); 
            }
            for(int j = 0; j < ieeeNumber.size(); j++)
            {
                ieeeNumber.set(j,ieeeNumber.get(j).replace("number={","no. "));
                ieeeNumber.set(j,ieeeNumber.get(j).replace("},", ",")); 
            }
            for(int j = 0; j < ieeePages.size(); j++)
            {
                ieeePages.set(j,ieeePages.get(j).replace("pages={","p. "));
                ieeePages.set(j,ieeePages.get(j).replace("},", ","));   
            }
            for(int j = 0; j < ieeeMonth.size(); j++)
            {
                ieeeMonth.set(j,ieeeMonth.get(j).replace("month={",""));
                ieeeMonth.set(j,ieeeMonth.get(j).replace("},", " "));   
            }
            for(int j = 0; j < ieeeYear.size(); j++)
            {
                ieeeYear.set(j,ieeeYear.get(j).replace("year={",""));
                ieeeYear.set(j,ieeeYear.get(j).replace("},", ".")); 
            }
            // printing onto the output files
            for(int j = 1; j <= ieeeAuthors.size()-1; j++)
            {           
    		 	pw[i].println(ieeeAuthors.get(j) + ieeeTitle.get(j) + ieeeJournal.get(j) + ieeeVolume.get(j) + ieeeNumber.get(j) + ieeePages.get(j) + ieeeMonth.get(j) + ieeeYear.get(j));
                pw[i].println();               
            }    
            pw[i].flush();
            pw[i].close();
    		File ieee2 = new File("ieee2.json");
    		ieee2.delete();
    		File ieee5 = new File("ieee5.json");
    		ieee5.delete();
    		File ieee7 = new File("ieee7.json");
    		ieee7.delete();
       
    		// Converting into ACM format.
            
            try
			{
            	latFile[i] = new File("Latex"+i+".bib");
            	pw[i] = new PrintWriter("acm"+i+".json");
            	sc[i] = new Scanner(new FileInputStream(latFile[i]));
			}

			catch(FileNotFoundException e)
			{
				System.out.println(e.getMessage());
			}
            
            ArrayList<String> acmMonth = new ArrayList<String>();
			ArrayList<String> acmAuthors = new ArrayList<String>();        
			ArrayList<String> acmVolume = new ArrayList<String>();
			ArrayList<String> acmNumber = new ArrayList<String>();
			ArrayList<String> acmJournal = new ArrayList<String>();
			ArrayList<String> acmPages = new ArrayList<String>();
			ArrayList<String> acmYear = new ArrayList<String>();
			ArrayList<String> acmTitle = new ArrayList<String>();
			ArrayList<String> acmDoi = new ArrayList<String>();
			
			while(sc[i].hasNextLine())
			{
				String lineInFile=sc[i].nextLine();

				if((lineInFile.indexOf("author={")) >-1)
				{
					acmAuthors.add((lineInFile));
				}
				if((lineInFile.indexOf("journal={")) >-1)
				{
					acmJournal.add(lineInFile);
				}
				if((lineInFile.indexOf("volume={")) >-1)
				{
					acmVolume.add((lineInFile));
				}
				if((lineInFile.indexOf("number={")) >-1)
				{
					acmNumber.add((lineInFile));
				}
				if((lineInFile.indexOf("pages={")) >-1)
				{
					acmPages.add((lineInFile));
				}
				if((lineInFile.indexOf("month={")) >-1)
				{
					acmMonth.add((lineInFile));
				}
				if((lineInFile.indexOf("year={")) >-1)
				{
					acmYear.add((lineInFile));
				}
				if((lineInFile.indexOf("title={")) >-1)
				{
					acmTitle.add((lineInFile));
				}
				if((lineInFile.indexOf("doi={")) >-1)
				{
					acmDoi.add((lineInFile));
				}
			}
			
			for(int j = 0; j < acmAuthors.size(); j++)
			{
				acmAuthors.set(j,acmAuthors.get(j).replace("author={", ""));

				if(acmAuthors.get(j).indexOf("and") >-1)
				{
					acmAuthors.set(j,acmAuthors.get(j).substring(0, acmAuthors.get(j).indexOf("and")));
					acmAuthors.set(j,acmAuthors.get(j) + "et al. ");
				}
				// if only 1 author
				else
					acmAuthors.set(j,acmAuthors.get(j).replace("},", "."));
			}
			for(int j = 0; j < acmYear.size(); j++)
			{
				acmYear.set(j,acmYear.get(j).replace("year={", ""));
				acmYear.set(j,acmYear.get(j).replace("},", "."));	
			}
			for(int j = 0; j < acmTitle.size(); j++)
			{
				acmTitle.set(j,acmTitle.get(j).replace("title={", ""));	
				acmTitle.set(j,acmTitle.get(j).replace("},", "."));	
			}
			for(int j = 0; j < acmJournal.size(); j++)
			{
				acmJournal.set(j,acmJournal.get(j).replace("journal={", ""));
				acmJournal.set(j,acmJournal.get(j).replace("},", "."));
			}
			for(int j = 0; j < acmVolume.size(); j++)
			{
				acmVolume.set(j,acmVolume.get(j).replace("volume={", ""));
				acmVolume.set(j,acmVolume.get(j).replace("},", ","));
			}
			for(int j = 0; j < acmNumber.size(); j++)
			{
				acmNumber.set(j,acmNumber.get(j).replace("number={", ""));
				acmNumber.set(j,acmNumber.get(j).replace("},", ""));
				acmNumber.set(j,acmNumber.get(j)+"("+acmYear.get(j).replace(".", "),"));
			}
			for(int j = 0; j < acmPages.size(); j++)
			{
				acmPages.set(j,acmPages.get(j).replace("pages={",""));
				acmPages.set(j,acmPages.get(j).replace("},","."));
			}
			for(int j = 0; j < acmDoi.size(); j++)
			{
				acmDoi.set(j,acmDoi.get(j).replace("doi={", "DOI:https://doi.org/"));
				acmDoi.set(j,acmDoi.get(j).replace("},", "."));	
			}

			for(int j = 0; j < acmJournal.size(); j++)
			{
				pw[i].println("["+(j+1)+"]"+"		"+acmAuthors.get(j) + acmYear.get(j) + acmTitle.get(j) + acmJournal.get(j) + acmVolume.get(j) + acmNumber.get(j) + acmPages.get(j) + acmDoi.get(j));
				pw[i].println();
			}
			pw[i].flush();
			pw[i].close();
    		File acm2 = new File("acm2.json");
    		acm2.delete();
    		File acm5 = new File("acm5.json");
    		acm5.delete();
    		File acm7 = new File("acm7.json");
    		acm7.delete();
			
			
			// Converting into NJ format.
			try
			{
				latFile[i] = new File("Latex"+i+".bib");
            	pw[i] = new PrintWriter("nj"+i+".json");
            	sc[i] = new Scanner(new FileInputStream(latFile[i]));				
			}
			catch(FileNotFoundException e)
			{
				System.out.println(e.getMessage());
			}

			// create ArrayList for each fields
			ArrayList<String> NJauthors = new ArrayList<String>();
			ArrayList<String> NJvolume = new ArrayList<String>();
			ArrayList<String> NJjournal = new ArrayList<String>();
			ArrayList<String> NJpages = new ArrayList<String>();
			ArrayList<String> NJyear = new ArrayList<String>();
			ArrayList<String> NJtitle = new ArrayList<String>();

			while(sc[i].hasNextLine())
			{
				String lineInFile = sc[i].nextLine();

				if((lineInFile.indexOf("author={")) >-1)
				{
					NJauthors.add((lineInFile));
				}
				if((lineInFile.indexOf("journal={")) >-1)
				{
					NJjournal.add(lineInFile);
				}
				if((lineInFile.indexOf("volume={")) >-1)
				{
					NJvolume.add((lineInFile));
				}
				if((lineInFile.indexOf("pages={")) >-1)
				{
					NJpages.add((lineInFile));
				}
				if((lineInFile.indexOf("year={")) >-1)
				{
					NJyear.add((lineInFile));
				}
				if((lineInFile.indexOf("title={")) >-1)
				{
					NJtitle.add((lineInFile));
				}
			}

			for(int j = 0; j < NJauthors.size(); j++)
			{
				NJauthors.set(j,NJauthors.get(j).replace("author={", ""));
				NJauthors.set(j,NJauthors.get(j).replace("},", "."));
				NJauthors.set(j,NJauthors.get(j).replace("and", "&"));
			}
			for(int j = 0; j < NJtitle.size(); j++)
			{
				NJtitle.set(j,NJtitle.get(j).replace("title={", ""));
				NJtitle.set(j,NJtitle.get(j).replace("},", "."));

			}
			for(int j = 0; j < NJjournal.size(); j++)
			{
				NJjournal.set(j,NJjournal.get(j).replace("journal={", ""));
				NJjournal.set(j,NJjournal.get(j).replace("},", "."));	
			}
			for(int j = 0; j < NJvolume.size(); j++)
			{
				NJvolume.set(j,NJvolume.get(j).replace("volume={", ""));
				NJvolume.set(j,NJvolume.get(j).replace("},", ","));	
			}
			for(int j = 0; j < NJyear.size(); j++)
			{
				NJyear.set(j,NJyear.get(j).replace("year={", ""));
				NJyear.set(j,NJyear.get(j).replace("},", ")."));	
			}

			for(int j = 0; j < NJpages.size(); j++)
			{
				NJpages.set(j,NJpages.get(j).replace("pages={",""));
				NJpages.set(j,NJpages.get(j).replace("}, ", ""));	
				NJpages.set(j,NJpages.get(j)+"("+NJyear.get(j));
			}

			for(int j = 0; j < NJauthors.size(); j++)
			{
				pw[i].println(NJauthors.get(j) + NJtitle.get(j) + NJjournal.get(j) + NJvolume.get(j) + NJpages.get(j));
				pw[i].println();
			}
			pw[i].flush();
			pw[i].close();
    		File nj2 = new File("nj2.json");
    		nj2.delete();
    		File nj5 = new File("nj5.json");
    		nj5.delete();
    		File nj7 = new File("nj7.json");
    		nj7.delete();
			sc[i].close();
			        
        } // end of conversion For-loop

    } // end of method processFilesForValidation()
}