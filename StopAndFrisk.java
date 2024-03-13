import java.util.ArrayList;

/**
 * The StopAndFrisk class represents stop-and-frisk data, provided by
 * the New York Police Department (NYPD), that is used to compare
 * during when the policy was put in place and after the policy ended.
 * 
 */
public class StopAndFrisk {

    /*
     * The ArrayList keeps track of years that are loaded from CSV data file.
     * Each SFYear corresponds to 1 year of SFRecords. 
     * Each SFRecord corresponds to one stop and frisk occurrence.
     */ 
    private ArrayList<SFYear> database; 

    /*
     * Constructor creates and initializes the @database array
     * 
     * DO NOT update nor remove this constructor
     */
    public StopAndFrisk () {
        database = new ArrayList<>();
    }

    /*
     * Getter method for the database.
     * *** DO NOT REMOVE nor update this method ****
     */
    public ArrayList<SFYear> getDatabase() {
        return database;
    }

    /**
     * This method reads the records information from an input csv file and populates 
     * the database.
     * 
     * Each stop and frisk record is a line in the input csv file.
     * 
     * 1. Open file utilizing StdIn.setFile(csvFile)
     * 2. While the input still contains lines:
     *    - Read a record line (see assignment description on how to do this)
     *    - Create an object of type SFRecord containing the record information
     *    - If the record's year has already is present in the database:
     *        - Add the SFRecord to the year's records
     *    - If the record's year is not present in the database:
     *        - Create a new SFYear 
     *        - Add the SFRecord to the new SFYear
     *        - Add the new SFYear to the database ArrayList
     * 
     * @param csvFile
     */
    public void readFile ( String csvFile ) {

        // DO NOT remove these two lines
        StdIn.setFile(csvFile); // Opens the file
        StdIn.readLine();       // Reads and discards the header line
        
        while (!StdIn.isEmpty()) {
        String[] recordEntries = StdIn.readLine().split(",");
        int year = Integer.parseInt(recordEntries[0]);
        String description = recordEntries[2];        
        String gender = recordEntries[52];        
        String race = recordEntries[66];        
        String location = recordEntries[71];
        Boolean arrested = recordEntries[13].equals("Y");
        Boolean frisked = recordEntries[16].equals("Y");
        SFRecord information = new SFRecord(description, arrested, frisked, gender, race, location);
        
        boolean yearispresent = false;
        for (int i = 0; i < database.size(); i++) {
            SFYear sfyear = database.get(i);
            if (year == sfyear.getcurrentYear()) {
            sfyear.addRecord(information);
            yearispresent = true;
            break;}
                }
        
            if (!yearispresent) {
            SFYear newsfyear = new SFYear(year);
            newsfyear.addRecord(information);
            database.add(newsfyear);
                }
            }
        }

    /**
     * This method returns the stop and frisk records of a given year where 
     * the people that was stopped was of the specified race.
     * 
     * @param year we are only interested in the records of year.
     * @param race we are only interested in the records of stops of people of race. 
     * @return an ArrayList containing all stop and frisk records for people of the 
     * parameters race and year.
     */

    public ArrayList<SFRecord> populationStopped ( int year, String race ) {

        ArrayList<SFRecord> sfrecords = new ArrayList<>();
        SFYear specificyear = getSFYear(year);
        ArrayList<SFRecord> recordsforyear = specificyear.getRecordsForYear();

            for (int i = 0; i < recordsforyear.size(); i++) {
                SFRecord record = recordsforyear.get(i);
                    if (race.equals(record.getRace())) {
                    sfrecords.add(record);
            }
            }
            return sfrecords; 
            }

    private SFYear getSFYear(int year) {
        for (int i = 0; i < database.size(); i++) {
            SFYear sfyear = database.get(i);
            if (year == sfyear.getcurrentYear()) {
                return sfyear;
                    }
                }
            return null;
            }
            

    /**
     * This method computes the percentage of records where the person was frisked and the
     * percentage of records where the person was arrested.
     * 
     * @param year we are only interested in the records of year.
     * @return the percent of the population that were frisked and the percent that
     *         were arrested.
     */
    public double[] friskedVSArrested ( int year ) {
        
        SFYear specificyear = getSFYear(year);

        ArrayList<SFRecord> records = specificyear.getRecordsForYear();
        int total = records.size();
        int frisked = 0;
        int arrested = 0;
    
        for (int i = 0; i < records.size(); i++) {
            SFRecord record = records.get(i);
            if (record.getFrisked()) {
                frisked++;
                }
    
            if (record.getArrested()) {
                arrested++;
                }
            }
    
        double friskedpercent = (frisked * 100.0) / total;
        double arrestedpercent = (arrested * 100.0) / total;
        double[] outputarray = new double [2];
        outputarray[0] = friskedpercent;
        outputarray[1] = arrestedpercent;
    
        return outputarray;
    }

    /**
     * This method keeps track of the fraction of Black females, Black males,
     * White females and White males that were stopped for any reason.
     * Drawing out the exact table helps visualize the gender bias.
     * 
     * @param year we are only interested in the records of year.
     * @return a 2D array of percent of number of White and Black females
     *         versus the number of White and Black males.
     */
    public double[][] genderBias ( int year ) {

        SFYear specificyear = getSFYear(year);

        ArrayList<SFRecord> records = specificyear.getRecordsForYear();
        int blacktotal = 0;
        int whitetotal = 0;
        int blackmale = 0;
        int whitemale = 0;
        int blackfemale = 0;
        int whitefemale = 0;

        for (int i = 0; i < records.size(); i++) {
            SFRecord record = records.get(i);
            String race = record.getRace();
            String gender = record.getGender();

            if ("B".equals(race)) {
                blacktotal++;
            } else if ("W".equals(race)) {
                whitetotal++;
            }

            if ("M".equals(gender)) {
                if ("B".equals(race)) {
                    blackmale++;
                } else if ("W".equals(race)) {
                    whitemale++;
                }
            } else if ("F".equals(gender)) {
                if ("B".equals(race)) {
                    blackfemale++;
                } else if ("W".equals(race)) {
                    whitefemale++;
                }
            }
        }

        double blackfemalepercent = (blackfemale / (double) blacktotal) * 0.5 * 100;
        double whitefemalepercent = (whitefemale / (double) whitetotal) * 0.5 * 100;
        double blackmalepercent = (blackmale / (double) blacktotal) * 0.5 * 100;
        double whitemalepercent = (whitemale / (double) whitetotal) * 0.5 * 100;
        double totalfemalepercent = whitefemalepercent + blackfemalepercent;
        double totalmalepercent = whitemalepercent + blackmalepercent;

        double[][] resultingarray = new double [2][3];
            resultingarray[0][0] = blackfemalepercent;
            resultingarray[0][1] = whitefemalepercent;
            resultingarray[0][2] = totalfemalepercent;
            resultingarray[1][0] = blackmalepercent;
            resultingarray[1][1] = whitemalepercent;
            resultingarray[1][2] = totalmalepercent;

        return resultingarray;
    }


    /**
     * This method checks to see if there has been increase or decrease 
     * in a certain crime from year 1 to year 2.
     * 
     * Expect year1 to preceed year2 or be equal.
     * 
     * @param crimeDescription
     * @param year1 first year to compare.
     * @param year2 second year to compare.
     * @return 
     */

    public double crimeIncrease ( String crimeDescription, int year1, int year2 ) {
        
    if (year1 > year2 ){
        return 0;
    }

    SFYear sfyear1 = getSFYear(year1);
    SFYear sfyear2 = getSFYear(year2);

    int numberofcrimesyear1 = countCrimeOccurrences(sfyear1.getRecordsForYear(), crimeDescription);
    int numberofcrimesyear2 = countCrimeOccurrences(sfyear2.getRecordsForYear(), crimeDescription);

    double percentyear1 = (numberofcrimesyear1 / (double) sfyear1.getRecordsForYear().size()) * 100;
    double percentyear2 = (numberofcrimesyear2 / (double) sfyear2.getRecordsForYear().size()) * 100;

    double percentchange = percentyear2 - percentyear1;

    return percentchange;
        }

    private int countCrimeOccurrences(ArrayList<SFRecord> records, String crimeDescription) {
    int count = 0;
    for (int i = 0; i < records.size(); i++) {
        SFRecord record = records.get(i);
        if (record.getDescription().indexOf(crimeDescription) != -1) {
            count++;
        }
    }
    return count;
    }

    /**
     * This method outputs the NYC borough where the most amount of stops 
     * occurred in a given year. This method will mainly analyze the five 
     * following boroughs in New York City: Brooklyn, Manhattan, Bronx, 
     * Queens, and Staten Island.
     * 
     * @param year we are only interested in the records of year.
     * @return the borough with the greatest number of stops
     */
    public String mostCommonBorough ( int year ) {

        SFYear specificyear = getSFYear(year);
        
        ArrayList<SFRecord> records = specificyear.getRecordsForYear();
        int[] counts = new int[5];
        String[] boroughs = {"Brooklyn", "Manhattan", "Bronx", "Queens", "Staten Island"};
        
        for (int i = 0; i < records.size(); i++) {
        SFRecord record = records.get(i);
        String location = record.getLocation();
               
        for (int j = 0; j < boroughs.length; j++) {
            if (location.equalsIgnoreCase(boroughs[j])) {
                counts[j]++;
                break; }
                    }
                }

        int maxindex = 0;
        for (int k = 1; k < counts.length; k++) {
                if (counts[k] > counts[maxindex]) {
                    maxindex = k;
                    }
                }
            
            return boroughs[maxindex];
            }
        
        }

