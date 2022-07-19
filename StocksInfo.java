import java.util.Scanner;
import java.io.*;

public class StocksInfo {
	
	private static Stock[] stockList;
	private static int listedNum;
	private static int ETFnum;
	private static String[] tradeDate;
	
	public static void loadStocks(String currentDate) throws IOException{
		File stockMeta = new File("stock-meta.csv");
		File dailyPrice = new File("daily-price.csv");
		File performance = new File("performance.csv");
		
		listedNum = countNumStocks(stockMeta,1);
		ETFnum = countNumStocks(stockMeta,2);
		
		stockList = new Stock[listedNum+ETFnum];
		
		for (int i=0; i<listedNum;i++)
			stockList[i] = new ListedCompany();
		for (int i= listedNum;i<(listedNum+ETFnum);i++)
			stockList[i] = new ETF();
		
		loadStockId(stockMeta);
		loadStockPrice(dailyPrice,currentDate);
		 
		loadETFinfo(stockMeta);
		loadListedInfo(performance,currentDate);
		
		
	}
	
	public static Stock[] getStockList(){
		return stockList;
	}
	
	/**
		@param file "stock-meta.csv"
		@param type 1 = Listed company, 2 = ETF
	*/
	private static int countNumStocks(File file, int type) throws IOException{  
		Scanner fileReader = new Scanner(file);
		int count = 0;
		while (fileReader.hasNext()){
			String line = fileReader.nextLine();
			String[] dataArr = line.split(","); // dataArr[0] stockID; dataArr[1] type
			if (Integer.parseInt(dataArr[1]) == type)
				count++;
		}
		fileReader.close();
		return count;
	}
	
	/**
		@param file "stock-meta.csv"
	*/
	private static void loadStockId(File file) throws IOException{
		Scanner fileReader = new Scanner(file);
		int listedIndex = 0;
		int ETFindex = 0;
		
		while (fileReader.hasNext()){
			String line = fileReader.nextLine();
			String[] dataArr = line.split(","); // dataArr[0] stockID; dataArr[1] type
			int type = Integer.parseInt(dataArr[1]);
			if (type ==1)
				stockList[listedIndex++].setStockID(dataArr[0]);
			else if (type == 2){
				stockList[listedNum+ETFindex].setStockID(dataArr[0]);
				ETFindex++;
			}
		}
		fileReader.close();
	}
	
	
	
	/**
		@param file "daily-price.csv"
		@param currentDate input from StockTraderDemo.java
	*/
	private static void loadStockPrice(File file, String currentDate) throws IOException{
		Scanner fileReader = new Scanner(file);
		while (fileReader.hasNext()){
            String line = fileReader.nextLine();
            String[] dataArr = line.split(","); // [0] stockID [1] date [2] close
			
			for (int i = 0;i<stockList.length;i++){
				if (dataArr[0].equals(stockList[i].getStockID()) && dataArr[1].equals(currentDate))
					stockList[i].setCurrentPrice(Double.parseDouble(dataArr[2]));
			}
        }
		fileReader.close();
	}
	
	/**
		@param file "performance.csv"
	*/
	private static void loadListedInfo(File file, String currentDate) throws IOException{
		Scanner fileReader = new Scanner(file);
		String[] stockLastDate = new String[stockList.length];
		for(int i=0;i<stockLastDate.length;i++)
			stockLastDate[i] = "00/00/0000";
		
		while (fileReader.hasNext()){
			String line = fileReader.nextLine();
            String[] dataArr = line.split(",") ;// [0] stockID, [1] date, [2] profit, [3] NAV, [4] dividend
			for (int i=0; i<listedNum;i++){
				if (dataArr[0].equals(stockList[i].getStockID()) && Utilities.DateCompare(currentDate,dataArr[1])>=0 && Utilities.DateCompare(stockLastDate[i],dataArr[1])<0){
					stockList[i].setProfit(Double.parseDouble(dataArr[2]));
					stockList[i].setNAV(Double.parseDouble(dataArr[2]));
					stockList[i].setDividend(Double.parseDouble(dataArr[3]));
					stockLastDate[i] = dataArr[1];
				}
			}
		}
		fileReader.close();
	}
	
	
	
	/**
		@param file "stock-meta.csv"
	*/
	private static void loadETFinfo (File file) throws IOException {
		Scanner fileReader = new Scanner(file);
		int index = 0;
		while (fileReader.hasNext()){
			String line = fileReader.nextLine();
			String[] dataArr = line.split(","); // dataArr[0] stockID; dataArr[1] type
			if (Integer.parseInt(dataArr[1]) == 2){ // ETF
				// dataArr[2] to dataArr[dataArr.length-1] -> listOfStock
				setListOfStockOfSingleETF(findStockByID(dataArr[0]), dataArr);
			}
		}
		fileReader.close();
	}
	
	public static Stock findStockByID(String id){
		for (int i = 0; i<stockList.length;i++)
			if (id.equals(stockList[i].getStockID()))
				return stockList[i];
		return null;
	}
	
	private static void setListOfStockOfSingleETF (Stock stock, String[] meta){
		Stock[] listOfStockID = new ListedCompany[meta.length-2];
		for (int i=2;i<meta.length;i++){
			listOfStockID[i-2] = findStockByID(meta[i]);
		}
		stock.setListOfStockID(listOfStockID);
	}
	
	public static void tradeDate() throws IOException{
		File file = new File("daily-price.csv");
		Scanner fileReader = new Scanner(file);
		int count = 0;
		while (fileReader.hasNext()){
			fileReader.nextLine();
			count++;
		}
		fileReader.close();

		String[] dateDuplicated = new String[count];
		fileReader = new Scanner(file);
		int index = 0;
		while (fileReader.hasNext()){
			String line = fileReader.nextLine();
			dateDuplicated[index] = line.split(",")[1];
			index++;
		}
		fileReader.close();
		
		String[] dateArray = new String[count];
		int numTradeDate = 0;
		for (int i=0;i<dateDuplicated.length;i++){
			if(!Utilities.isContain(Utilities.deepCopyStringArray(dateArray,0,numTradeDate),dateDuplicated[i])){
				dateArray[numTradeDate] = dateDuplicated[i];
				numTradeDate++;
			}
		}
		tradeDate = Utilities.deepCopyStringArray(dateArray,0,numTradeDate-1);
		Utilities.sortDate(tradeDate, 0, tradeDate.length-1);
	}
	
	public static String[] getTradeDate(){
		return tradeDate;
	}
	
	public static String[] getStockPrice(Stock stock) throws IOException{
		String[] stockPrice= new String[tradeDate.length];
		
		File file = new File("daily-price.csv");
		int numStockPrice = 0;
		Scanner fileReader = new Scanner(file);
		while (fileReader.hasNext()){
			String[] dataArr = fileReader.nextLine().split(",");// [0] stockID [1] date [2] close
			if (dataArr[0].equals(stock.getStockID()))
				stockPrice[numStockPrice++] = dataArr[1]+","+dataArr[2];
		}
		fileReader.close();
		return Utilities.deepCopyStringArray(stockPrice,0,numStockPrice-1);
	}
	
	public static double getStockPrice(String stockID,String Date) throws IOException{
		File file = new File("daily-price.csv");
		Scanner fileReader = new Scanner(file);
		while (fileReader.hasNext()){
			String[] dataArr = fileReader.nextLine().split(",");// [0] stockID [1] date [2] close
			if (dataArr[0].equals(stockID)&& Utilities.DateCompare(dataArr[1],Date)==0){
				fileReader.close();
				return Double.parseDouble(dataArr[2]);
			}
		}
		fileReader.close();
		return 0.0;
	}
	
	
	
	
	public static double getAveragePrice(String stockID,String date) throws IOException{
		File recordFile = new File("trade-record.csv");
		Scanner fileReader = new Scanner(recordFile);
		double numerator=0;
		int sumShares=0;
		while (fileReader.hasNext()){
			String[] dataArr = fileReader.nextLine().split(","); // 0-date 1-stockid 2-price 3-shares 4-direction(1=buy 2=sell)
			double price = Double.parseDouble(dataArr[2]);
			int shares = Integer.parseInt(dataArr[3]); 
			if (dataArr[4].equals("1") && dataArr[1].equals(stockID) && Utilities.DateCompare(dataArr[0], date)<=0){
				sumShares += shares;
				numerator += price*shares;
			}
		}
		fileReader.close();
		double averagePrice = numerator/((double)sumShares);
		return averagePrice;
	}
	
	
	public static double getTotalProfit(String date) throws IOException{
		File recordFile = new File("trade-record.csv");
		double totalprofit = 0.0;
		if (!recordFile.exists())
			return totalprofit;
		Scanner fileReader;
		for(int i=0;i<listedNum+ETFnum;i++){
			//amount holding, money sold, money spent
			double info[] = {0,0,0};
			fileReader = new Scanner(recordFile);
			while (fileReader.hasNext()){
				String line = fileReader.nextLine();
				String[] dataArr = line.split(",") ;// Date,StockID,Price,#Shares,Direction
				if(dataArr[1].equals(stockList[i].getStockID())){
					if(dataArr[4].equals("1")){
							info[0]+=Double.parseDouble(dataArr[3]);
							info[2]+=Double.parseDouble(dataArr[2])*Double.parseDouble(dataArr[3]);
					}
					else if(dataArr[4].equals("2")){
							info[0]-=Double.parseDouble(dataArr[3]);
							info[1]+=Double.parseDouble(dataArr[2])*Double.parseDouble(dataArr[3]);
					}
				}
			}
			totalprofit+=info[0]*stockList[i].getCurrentPrice()+info[1]-info[2];
			fileReader.close();
		}
		
		double dividend = 0;
		File performFile = new File("performance.csv");
		fileReader = new Scanner(performFile);
		while (fileReader.hasNext()){
				String line = fileReader.nextLine();
				String[] dataArr = line.split(",") ;//[0] stockID, [1] date, [2] profit, [3] NAV, [4] dividend, [5] payable date
				if(Utilities.DateCompare(date,dataArr[5])>=0){
					dividend+=getNumHoldingThatDay(dataArr[0],dataArr[5])*Double.parseDouble(dataArr[4]);
				}
		}
		fileReader.close();
		totalprofit+=dividend;
		return totalprofit;
		
	}
	
	public static int getNumHoldingThatDay(String stockId,String date) throws IOException{
		File recordFile = new File("trade-record.csv");
		int amount=0;
		if (!recordFile.exists())
			return amount;
		Scanner fileReader = new Scanner(recordFile);
		while (fileReader.hasNext()){
			String line = fileReader.nextLine();
			String[] dataArr = line.split(",") ;// Date,StockID,Price,#Shares,Direction
			if(dataArr[1].equals(stockId)&&(Utilities.DateCompare(dataArr[0],date)==-1)){
				if(dataArr[4].equals("1"))
					amount+=Integer.parseInt(dataArr[3]);
				else if(dataArr[4].equals("2"))
					amount-=Integer.parseInt(dataArr[3]);	
			}
		}
		fileReader.close();
		return amount;
	}
	
	public static double getStockProfit(String stockId, String date) throws IOException{
		File recordFile = new File("trade-record.csv");
		Double totalprofit = 0.0;
		if (!recordFile.exists())
			return totalprofit;
		//amount holding, money sold, money spent
		double info[] = {0,0,0};
		Scanner fileReader = new Scanner(recordFile);
		while (fileReader.hasNext()){
			String line = fileReader.nextLine();
			String[] dataArr = line.split(",") ;// Date,StockID,Price,#Shares,Direction
			if(dataArr[1].equals(stockId)){
				if(dataArr[4].equals("1")){
					info[0]+=Double.parseDouble(dataArr[3]);
					info[2]+=Double.parseDouble(dataArr[2])*Double.parseDouble(dataArr[3]);
				}
				else if(dataArr[4].equals("2")){
					info[0]-=Double.parseDouble(dataArr[3]);
					info[1]+=Double.parseDouble(dataArr[2])*Double.parseDouble(dataArr[3]);
				}
			}
		}
		totalprofit+=info[0]*findStockByID(stockId).getCurrentPrice()+info[1]-info[2];
		fileReader.close();
		
		//now we calculate dividend
		double dividend = 0;
		File performFile = new File("performance.csv");
		fileReader = new Scanner(performFile);
		while (fileReader.hasNext()){
				String line = fileReader.nextLine();
				String[] dataArr = line.split(",") ;//[0] stockID, [1] date, [2] profit, [3] NAV, [4] dividend, [5] payable date
				if(Utilities.DateCompare(date,dataArr[5])>=0&&(dataArr[0].equals(stockId))){
					dividend+=getNumHoldingThatDay(dataArr[0],dataArr[5])*Double.parseDouble(dataArr[4]);
				}
		}
		fileReader.close();
		totalprofit+=dividend;
		return totalprofit;
	}
	
	

}