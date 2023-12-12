package ads.quarterlyReport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;

public class Quarterly_Report {
	//This code generates algorithms for:
	//Task 1 - The total sales for each department per quarter i.e. “2nd Quarter totals: Electrical, £208,000”
	//Task 2 - The name of the best and worst performing department per quarter, with its respective monthly sales i.e. “2nd Quarter best: Kitchen, £65, 000, £67,000, £56,000” for the second quarter.
	//Task 4 - Given the total sales for each quarter, calculate the tax that needs to be paid at 17%.
	
	//init empty hashmaps for sample data and sales totals
	private HashMap<String, HashMap<String, Integer>> data;
	private HashMap<Integer, HashMap<String, Integer>> totalQuarterlySalesByDepartment;
	private HashMap<Integer, HashMap<String, ArrayList<Integer>>> bestAndWorst;
	
	//init primative variables for tax rate task
	private int quarter1TotalTax = 0;
	private int quarter2TotalTax = 0;
	private int quarter3TotalTax = 0;
	private int quarter4TotalTax = 0;
	private double taxRate = 0;

	//Init a constant array of months string allow comparison of index system for establishing the quarters
	private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	
	//main function executes the code
	public static void main(String[] args) {
		
		//initiate report instance
		Quarterly_Report report = new Quarterly_Report();
		
		// Initiate the sample data structure from hard coded data
		report.data = report.generateSampleTable();
		
		//Generate the sales for each quarter using the sample data (see below)
		report.setTotalQuarterlySales(report.data);
		
		//Generate the best and worst departments per quarter
		report.setBestAndWorst(report.data);
		
		
		
		//Set the tax rate
		report.setTaxRate(0.17);
		
		//Generate totals from tax rate and set
		report.setQuarter1Total();
		report.setQuarter2Total();
		report.setQuarter3Total();
		report.setQuarter4Total();
		
		//run a console app while loop for users to access totals
		boolean appRunning = true;
		while (appRunning) {
			
			//use a scanner to take user input
			Scanner scanner = new Scanner(System.in);
			System.out.println();
			System.out.println("Quarterly Report.");
			System.out.println("Enter your choice.\nq: Sales per Quarter \np: Performance by Quarter\ns: Sales Taxes per Quarter\ne: Exit");
			String choice = scanner.nextLine();
			
			//give users different reports based on their letter choice
			switch(choice) {
				case("q"):
					//to string methods loop over data structures and print them 
					report.quartersToString();
					break;
				case("p"):
					report.bestAndWorstToString();
					break;
				case("s"):
					report.salesTaxToString();
					break;
				case("e"):
					//if user enters e, program is terminated
					System.out.println("Exiting.");
					appRunning = false;
			}
		}
		
		
	} 
	
	//Sample Data Generator
	//This method generates a hash map structure that closely resembles the table in the brief.
	//The hash maps is made up of nested hash maps with String/Integer pairs.
	//Hash maps were chosen as a mapping solution because they provide easy processing
	//even when data is not available. In the sample data quarters 1 and 4 are missing,
	//which implies the data could have missing quarters and needs to work in that case.
	
	public static HashMap <String, HashMap<String, Integer>> generateSampleTable() {
		
		//generate a parent hash map to contain hash maps for each month
		HashMap <String, HashMap<String, Integer>> sampleData = new HashMap();
		
		//generate an empty hashmap of String/Integer pairs for each month
		HashMap <String, Integer> april = new HashMap();
		HashMap <String, Integer> may = new HashMap();
		HashMap <String, Integer> june = new HashMap();
		HashMap <String, Integer> july = new HashMap();
		HashMap <String, Integer> august = new HashMap();
		HashMap <String, Integer> september = new HashMap();
		
		//each individual month is populated by data from the brief, with String keys and Integer values.
		april.put("Electrical", 67);
		april.put("Kitchen", 65);
		april.put("Bathroom", 63);
		april.put("SoftFurnishings", 18);
		april.put("Accessories", 16);
		may.put("Electrical", 63);
		may.put("Kitchen", 67);
		may.put("Bathroom", 63);
		may.put("SoftFurnishings", 24);
		may.put("Accessories", 23);
		june.put("Electrical", 78);
		june.put("Kitchen", 56);
		june.put("Bathroom", 65);
		june.put("SoftFurnishings", 22);
		june.put("Accessories", 21);
		july.put("Electrical", 78);
		july.put("Kitchen", 45);
		july.put("Bathroom", 71);
		july.put("SoftFurnishings", 19);
		july.put("Accessories", 19);
		august.put("Electrical", 104);
		august.put("Kitchen", 56);
		august.put("Bathroom", 73);
		august.put("SoftFurnishings", 17);
		august.put("Accessories", 20);
		september.put("Electrical", 103);
		september.put("Kitchen", 72);
		september.put("Bathroom", 69);
		september.put("SoftFurnishings", 16);
		september.put("Accessories", 19);
		
		//once populated, the sampleData parent is populated by the monthly hash maps.
		//Keys are labeled in the same way as the column to the left of the table.
		sampleData.put("April", april);
		sampleData.put("May", may);
		sampleData.put("June", june);
		sampleData.put("July", july);
		sampleData.put("August", august);
		sampleData.put("September", september);
		
		return sampleData;
	}
	
	
	//Task 1 Methods
	//Normally I would organize by method category, but I've listed by task
	//to make it easier to read. Sample data generator is listed at the end.
	
	//A setter method that takes the data table as input
	public void setTotalQuarterlySales(HashMap<String, HashMap<String, Integer>> data) {
		
		//Generate a hash map to contain four quarterly hash maps that contain string/integer pairs
		HashMap <Integer, HashMap<String, Integer>> quarterlyTotals = this.generateNestedIntegerHashMap();
		
		//for every month of sales listed in the data table, run this code
		for (var salesMonth : data.entrySet()) {
		
			//set simple name key for repeated use
			String key = salesMonth.getKey();
			
			//input the sales for the quarter into the hash map, using get quarter number to convert the month into the target quarter
			inputSalesMonthToQuarter(salesMonth, quarterlyTotals, getQuarterNumber(key));
		}
		//set the field of the class to the hash map generated in the function
		this.totalQuarterlySalesByDepartment = quarterlyTotals;
	}
	
	//
	private HashMap <Integer, HashMap<String, Integer>> generateNestedIntegerHashMap () {
		//Generate a hash map structure to contain a hash map for each quarter
		HashMap <Integer, HashMap<String, Integer>> nestedIntegerHashmap = new HashMap<Integer, HashMap<String, Integer>>();
		
		//Declare empty hash maps for each quarter 
		HashMap<String, Integer> q1 = new HashMap<String, Integer>();
		HashMap<String, Integer> q2 = new HashMap<String, Integer>();
		HashMap<String, Integer> q3 = new HashMap<String, Integer>();
		HashMap<String, Integer> q4 = new HashMap<String, Integer>();
		
		//Add the hash maps to the parent hash map
		nestedIntegerHashmap.put(1, q1);
		nestedIntegerHashmap.put(2, q2);
		nestedIntegerHashmap.put(3, q3);
		nestedIntegerHashmap.put(4, q4);
		
		return nestedIntegerHashmap;
		
	}
	
	//quarter generator, enter a month and receive a quarter as an int
	private int getQuarterNumber(String month) {
		int quarter = 0;
		if(month == months[0] || month == months[1] || month == months[2]) {
			quarter = 1;
		}
		if(month == months[3] || month == months[4] || month == months[5]) {
			quarter = 2;
		}
		if(month == months[6] || month == months[7] || month == months[8]) {
			quarter = 3;
		}
		if(month == months[9] || month == months[10] || month == months[11]) {
			quarter = 4;
		}	
		return quarter;
	}
	
	//generates a department total for a quarter and adds the total to a target quarter
	//receives a target quarter, an entry of a map, a month of sales and the quarterly totals hash map
	private void inputSalesMonthToQuarter(Map.Entry <String, HashMap<String, Integer>> salesMonth, HashMap <Integer, HashMap<String, Integer>> quarterlyTotals, int quarter) {
		
		//create a set of entries (keys and values) from the value of the current month
		Set<Entry<String, Integer>> month = salesMonth.getValue().entrySet(); 
		
		//for every department (key/value pair) listed within the month
		for (var department : month) {
			
			//separate key and value into single variables
			String depName = department.getKey();
			Integer depTotal = department.getValue();
			
			//if quarterlyTotals contains the department already
			if (quarterlyTotals.get(quarter).containsKey(depName)) {
			
				//Update the total, adding the new value to the base department amount
				Integer baseTotal = quarterlyTotals.get(quarter).get(depName);
				quarterlyTotals.get(quarter).put(depName, (baseTotal + depTotal));	
		
			} else {
			//otherwise set an initial value for the department
				quarterlyTotals.get(quarter).put(depName, depTotal);	
			}
		}
	}
	
	//generate a series of print statements for the while loop Console app to use when
	//the user chooses the option
	public void quartersToString() {
		
		//for each quarter in the parent hash map
		for (var quarter : this.totalQuarterlySalesByDepartment.entrySet()) {
			String quarterTitle = "1st";
			
			//Set the name of the quarter dynamically using the Integer keys
			switch(quarter.getKey()) {
				case(1):
					quarterTitle = "1st";
					break;
				case(2):
					quarterTitle = "2nd";
					break;
				case(3):
					quarterTitle = "3rd";
					break;
				case(4):
					quarterTitle = "4th";
					break;
	
			}
			//If a quarter doesn't have data, it won't be printed
			if (!quarter.getValue().entrySet().isEmpty()) {
				
				//print the title 
				System.out.println(quarterTitle + " Quarter totals: ");
				
				//convert the quarter's hash map into a set and loop over each value, printing it
				for (Map.Entry<String,Integer> department : quarter.getValue().entrySet()) {
					System.out.println(department.getKey() + ": £" + department.getValue() + ",000");
				}
			}
			//make a line break
			System.out.println();
		
			
		}
	}
	
	//Task 2 Methods
	
	//Setter for the hash map containing the best and worst totals for each quarter
	public void setBestAndWorst(HashMap<String, HashMap<String, Integer>> data) {
	
		//Generate a hash map containing four empty hash maps with String/ArrayList of Integer pairs
		HashMap <Integer, HashMap<String, ArrayList<Integer>>> bestAndWorst = this.generateNestedArrayListHashmap();

		//Use the quarterly sales generated in task 1 as a comparison tool
		//generate a Set of entries to loop over
		for (var quarter : this.totalQuarterlySalesByDepartment.entrySet()) {
			
			//save the quarter number for later comparison
			Integer quarterName = quarter.getKey();
			
			//Declare empty variables for best and worst of the totals to occupy
			String bestDepName = "";
			String worstDepName = "";
			
			//Best department will increase over time, so initial is set to 0
			Integer bestDepTotal = 0;
			
			//Worst department will decrease over time, so initial is set to 99999
			Integer worstDepTotal = 99999;
			 
			//generate a set of entries from the nested hash map to loop over
			//each entry is a department with a String name and an Integer total for that quarter
			for (var department : quarter.getValue().entrySet()) {
				
				//if the department has a larger value than the current best department
				//overwrite both the name and the value
				if (department.getValue() > bestDepTotal) {
					bestDepName = department.getKey();
					bestDepTotal = department.getValue();
				}
				
				//if the department has a smaller value than the current worst department
				//overwrite both the name and the value
				if (department.getValue() < worstDepTotal) {
					worstDepName = department.getKey();
					worstDepTotal = department.getValue();
				}
			}
			
			//generate an empty hash map for a quarter
			HashMap<String, ArrayList<Integer>> performanceMap = new HashMap();
			
			//if there is no best department or worst department,
			//don't populate the map
			if (!bestDepName.isEmpty() || !worstDepName.isEmpty()) {
				
				//add an entry with an empty array list for the best and worst
				//departments generated above
				performanceMap.put(bestDepName, new ArrayList<Integer>());
				performanceMap.put(worstDepName, new ArrayList<Integer>());
		
			//So we're still in the quarterly totals loop. Here we run a nested loop 
			//over the months in the sample data, converting the hash map into a set once more.
			//We keep the current best and worst departments variables saved for comparison.
			for (var salesMonth : data.entrySet()) {
				
				//generate the quarter number for the month using the method from task 1
				//ready for comparison with the quarter set for best and worst
				String key = salesMonth.getKey();
				int currentDepQuarter = this.getQuarterNumber(key);

				//Make simple names for best and worst array for shorter code
				ArrayList bestArray = performanceMap.get(bestDepName);
				ArrayList worstArray = performanceMap.get(worstDepName);
				
				//Convert the sales month hash map into a set for a two level nested loop
				//Check if each department matches the best or worst departments
				for (var dep : salesMonth.getValue().entrySet()) {
				
					//if the key string matches the best department string
					//and the current quarter matches the quarter of best/worst
					//add the value to the array
					if (dep.getKey() == bestDepName && currentDepQuarter == quarterName) {
						bestArray.add(dep.getValue());
					}
					if (dep.getKey() == worstDepName && currentDepQuarter == quarterName) {
						performanceMap.get(worstDepName).add(dep.getValue());
					}
				}
				//If the results aren't empty, update the empty arrays in the performance map
				//with the arrays populated above
				if (!bestArray.isEmpty() || !worstArray.isEmpty()) {
						performanceMap.put(bestDepName, bestArray);
						performanceMap.put(worstDepName, worstArray);
				}
			}
			//add the quarter performance hash map to the parent hash map
			bestAndWorst.put(quarterName, performanceMap);
			}
			
			//set the field of the class to the hash map generated in the function
			this.bestAndWorst = bestAndWorst;
		}
	
		}
		

	public void bestAndWorstToString() {
		//generate set from best and worst hash map and loop over it
		for (var quarter : this.bestAndWorst.entrySet()) {
			
			//set the quarter name
			String quarterTitle = "1st";
			switch(quarter.getKey()) {
				case(1):
					quarterTitle = "1st";
					break;
				case(2):
					quarterTitle = "2nd";
					break;
				case(3):
					quarterTitle = "3rd";
					break;
				case(4):
					quarterTitle = "4th";
					break;
	
			}
			//if quarter isn't empty, print quarter	
			if (!quarter.getValue().entrySet().isEmpty()) {
				
				//Data structure is a set, so worst will always show first.
				//With this knowledge we can use a count to print 'worst'
				//alongside the first value to show, then iterate it
				//which guarantees best will show when the count is 2.
				int count = 1;
				
				//print the title
				System.out.println("\n" + quarterTitle + " Quarter best and worst: ");
				
				//make an entry set for the quarter and loop over it, printing the values dynamically
				for (Map.Entry<String,ArrayList<Integer>> department : quarter.getValue().entrySet()) {
					//if the count is 1, worst is guaranteed to print next
					if(count == 1) {
						System.out.print("Worst: ");
						count++;
					} else {
					//if the count is 2, best is guaranteed to print
						System.out.print("\nBest: ");
					}
			
					System.out.println(department.getKey());
					
					//loop over the values of the department and add pound signs and thousand during print
					for (var total : department.getValue()) {
						System.out.print("£" + total + ",000 ");
					}
					
				}
				//print line break
				System.out.println();
			}
			
		}
	}
	

	
	private HashMap<Integer, HashMap<String, ArrayList<Integer>>> generateNestedArrayListHashmap () {
		//generate a parent hash map to contain hash maps of String/ArrayList pairs
		HashMap <Integer, HashMap<String, ArrayList<Integer>>> nestedArrayListHashmap = new HashMap();
		
		//generate the empty hash maps to populate the parent
		HashMap<String, ArrayList<Integer>> q1 = new HashMap();
		HashMap<String, ArrayList<Integer>> q2 = new HashMap();
		HashMap<String, ArrayList<Integer>> q3 = new HashMap();
		HashMap<String, ArrayList<Integer>> q4 = new HashMap();
		
		//use put to populate
		nestedArrayListHashmap.put(1, q1);
		nestedArrayListHashmap.put(2, q2);
		nestedArrayListHashmap.put(3, q3);
		nestedArrayListHashmap.put(4, q4);
		
		return nestedArrayListHashmap;
		
	}
	
	//Task 3 Methods
	//setters and getters for the tax rate
	
	//Set a tax rate using a decimal representation of a percentage
	private void setTaxRate(double rate) {
		this.taxRate = rate;
	}
	
	public double getTaxRate() {
		return taxRate;
	}
	
	//setters and getters for the tax to pay for each quarter
	public Integer getQuarter1TotalTax() {
		return quarter1TotalTax;
	}

	public void setQuarter1Total() {
		//each quarter uses the generateQuarterTaxTotal method
		//to generate an amount and sets it to the instance
		this.quarter1TotalTax = generateQuarterTaxTotal(1);
	}

	public void setQuarter2Total() {
		
		this.quarter2TotalTax = generateQuarterTaxTotal(2);
	}
	public Integer getQuarter2Total() {
		return quarter2TotalTax;
	}
	
	public void setQuarter3Total() {
		this.quarter3TotalTax = generateQuarterTaxTotal(3);
	}
	
	public Integer getQuarter3Total() {
		return quarter3TotalTax;
	}

	public void setQuarter4Total() {
		
		this.quarter4TotalTax = generateQuarterTaxTotal(4);
	}
	public Integer getQuarter4Total() {
		return quarter4TotalTax;
	} 

	//Method takes a quarter and returns the tax for that quarter
	private int generateQuarterTaxTotal(Integer quarter) {
		//Create a variable from a quarter of the total sales for each department in task 1
		Map<String, Integer> quarterTotals = this.totalQuarterlySalesByDepartment.get(quarter);
		
		//Create a double for total
		double total = 0.0;
		//Loop over the totals for the quarter and add them to the total
		for (var dep : quarterTotals.entrySet()) {
			total += dep.getValue();
		}
		
		//Take the total, add the tax, times by 1000 and cast to an int
		int tax = (int) (total * this.taxRate * 1000);
		return tax;
	}

	
	private void salesTaxToString() {
		//print title
		System.out.println("Sales tax totals for tax rate " + taxRate * 100 + "%");
		 
		//if the quarter has no values, we do not list it here.
		if (getQuarter1TotalTax() > 0) {
			//use a get method to return the tax
			System.out.println("Quarter 1 Tax To Pay: £" + getQuarter1TotalTax());
		}
		if (getQuarter2Total() > 0) {
			System.out.println("Quarter 2 Tax To Pay: £" + getQuarter2Total());
		}
		if (getQuarter3Total() > 0) {
			System.out.println("Quarter 3 Tax To Pay: £" + getQuarter3Total());
		}
		if (getQuarter4Total() > 0) {
			System.out.println("Quarter 4 Tax To Pay: £" + getQuarter4Total());
		}
		System.out.println();
	}
	
}
