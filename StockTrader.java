/**
	Assuming no dividends
*/

import java.io.*;
import java.util.Scanner;
public class StockTrader{
	private static String currentDate;
	private static double accCash;
	private static double overallProfit;
	
	public static void loadAccInfo() throws IOException{
		File recordFile = new File("trade-record.csv");
		if (!recordFile.exists()){
			accCash = 1000000;
			overallProfit = 0;
			return;
		}
		Scanner fileReader = new Scanner(recordFile);
		accCash = 1000000;
		while (fileReader.hasNext()){
			String line = fileReader.nextLine();
			String[] dataArr = line.split(","); // 2-price 3-shares 4-direction(1=buy 2=sell)
			double price = Double.parseDouble(dataArr[2]);
			int shares = Integer.parseInt(dataArr[3]);
			if (dataArr[4].equals("2")){
				accCash += price*shares;
			}
			else
				accCash -= price*shares;
		}
		fileReader.close();
		//dividend added
		double dividend = 0;
		File performFile = new File("performance.csv");
		fileReader = new Scanner(performFile);
		while (fileReader.hasNext()){
			String line = fileReader.nextLine();
			String[] dataArr = line.split(",") ;//[0] stockID, [1] date, [2] profit, [3] NAV, [4] dividend, [5] payable date
			if(Utilities.DateCompare(currentDate,dataArr[5])>=0){
				dividend+=StocksInfo.getNumHoldingThatDay(dataArr[0],dataArr[5])*Double.parseDouble(dataArr[4]);
			}
		}
		fileReader.close();
		accCash+=dividend;
		
		overallProfit = StocksInfo.getTotalProfit(currentDate);
	}
	
	public static double getAccCash(){
		return accCash;
	}
	
	public static double getOverallProfit(){
		return overallProfit;
	}
	
	public static void setCurrentDate(String _currentDate){
		currentDate = _currentDate;
	}
	
	public static String getCurrentDate(){
		return currentDate;
	}

	public static void main(String[] args) throws IOException{
		/*
			Set today
			assume the date input is always valid
		*/
		StocksInfo.tradeDate();
		Screen.printDateInsertion();
		String currentDate = Screen.keyboard.nextLine();
		
		String[] tradeDate = StocksInfo.getTradeDate();
		boolean isTradeDate = Utilities.isContain(tradeDate,currentDate);
		while (!isTradeDate){
			String lastDate ="00/00/0000";
			for (int i=0; i<tradeDate.length && !isTradeDate;i++){
				if (Utilities.DateCompare(currentDate,tradeDate[i])== -1 && Utilities.DateCompare(currentDate,lastDate)== 1){
					currentDate = tradeDate[i];
					isTradeDate = true;
				}
				lastDate = tradeDate[i];
			}
		}
		setCurrentDate(currentDate);
		
		StocksInfo.loadStocks(getCurrentDate());
		HoldingInfo.loadHolding();
		loadAccInfo();
		
		MenuController.startMenu();
		
	}
}