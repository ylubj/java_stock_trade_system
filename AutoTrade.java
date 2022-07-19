import java.io.*;
public class AutoTrade{
	private static String[] dateRange; 
	private static String endDate;
	
	public static void setAutoTradeDateRange(){
		String today = StockTrader.getCurrentDate();
		do{
			Screen.printEndDatePrompt();
			endDate = Screen.keyboard.nextLine();
			if (Utilities.DateCompare(endDate,today)<=0)
				Screen.printInvalidAutoTradeEndDatePrompt();
		} while (Utilities.DateCompare(endDate,today)<=0);
		
		String[] tradeDate = StocksInfo.getTradeDate();
		int end, start = Utilities.getStringArrayIndex(tradeDate,today);
		
		if (Utilities.isContain(tradeDate,endDate))
			end = Utilities.getStringArrayIndex(tradeDate,endDate);
		else{
			String lastDate ="00/00/0000";
			for (int i=0; i<tradeDate.length ;i++){
				if (Utilities.DateCompare(endDate,tradeDate[i])> 0 && Utilities.DateCompare(lastDate,tradeDate[i])<0)
					lastDate = tradeDate[i];
			}
			end = Utilities.getStringArrayIndex(tradeDate,lastDate);
		}
		
		dateRange = Utilities.deepCopyStringArray(tradeDate,start,end);
	}
	
	public static void launchAutoTrade() throws IOException{
		String stockID = "00700";
		Stock tradeStock;
		String[] tradeDate = StocksInfo.getTradeDate();
		double lastTradePrice=0.0,avgPrice ;
		int sellShare,sellIndex,buyShare,buyIndex,totalShares;
		File recordFile = new File("trade-record.csv"); // Date,StockID,Price,#Shares,Direction
		File holdingFile = new File("shares-holding.csv"); // StockID,#Shares,AveragePrice,TotalProfit
		FileWriter outputFile;
		String[][] holdingList;
		
		if (!StockTrader.getCurrentDate().equals(tradeDate[0]))
			lastTradePrice = StocksInfo.getStockPrice(stockID,tradeDate[Utilities.getStringArrayIndex(tradeDate,StockTrader.getCurrentDate())-1]);
		
		for (int j=0;j<dateRange.length;j++){
			StockTrader.setCurrentDate(dateRange[j]);
			StocksInfo.loadStocks(StockTrader.getCurrentDate());
			HoldingInfo.loadHolding();
			StockTrader.loadAccInfo();
			tradeStock = StocksInfo.findStockByID(stockID);
			holdingList = HoldingInfo.getHoldingList();
			StockTrader.loadAccInfo();
			//sell
			if (lastTradePrice!=0.0 && tradeStock.getCurrentPrice()<lastTradePrice && HoldingInfo.isHold(stockID)){
				sellIndex = HoldingInfo.indexOfID(stockID);
				sellShare = Integer.parseInt(holdingList[sellIndex][1]);
				Screen.printAutoTradeMsg(StockTrader.getCurrentDate(), stockID, 2, tradeStock.getCurrentPrice(), sellShare, tradeStock.getCurrentPrice()*sellShare);
				outputFile = new FileWriter(recordFile, true);
				outputFile.write(StockTrader.getCurrentDate()+","+stockID+","+tradeStock.getCurrentPrice()+","+sellShare+",2\n");
				outputFile.close();
				
				
				outputFile = new FileWriter(holdingFile);
				//double profit = StocksInfo.getStockProfit(stockID,StockTrader.getCurrentDate());
				//outputFile.write(stockID+","+0+","+holdingList[sellIndex][2]+","+ profit +"\n");
				for (int i=0;i<holdingList.length;i++){
					if(i!=sellIndex){
						outputFile.write(holdingList[i][0]);
						outputFile.write(",");
						outputFile.write(holdingList[i][1]);
						outputFile.write(",");
						outputFile.write(holdingList[i][2]);
						outputFile.write(",");
						outputFile.write(holdingList[i][3]);
						outputFile.write("\n");
					}
				}
				outputFile.close();
			}
			// buy
			else if (lastTradePrice!=0.0 &&  tradeStock.getCurrentPrice()>lastTradePrice && StockTrader.getAccCash()>=tradeStock.getCurrentPrice() ){
				buyShare = (int)(StockTrader.getAccCash()/tradeStock.getCurrentPrice());
				Screen.printAutoTradeMsg(StockTrader.getCurrentDate(), stockID, 1, tradeStock.getCurrentPrice(), buyShare, tradeStock.getCurrentPrice()*buyShare);
				
				outputFile = new FileWriter(recordFile, true);
				outputFile.write(StockTrader.getCurrentDate()+","+stockID+","+tradeStock.getCurrentPrice()+","+buyShare+",1\n");
				outputFile.close();
				
				outputFile = new FileWriter(holdingFile);
				
				if (holdingList==null){
					outputFile.write(stockID+","+buyShare+","+tradeStock.getCurrentPrice()+",0.0\n");
				}
				else if (!HoldingInfo.isHold(stockID)){
					outputFile.write(stockID);
					outputFile.write(",");
					outputFile.write(""+buyShare);
					outputFile.write(",");
					outputFile.write(""+StocksInfo.getAveragePrice(stockID,StockTrader.getCurrentDate()));
					outputFile.write(","+StocksInfo.getStockProfit(stockID,StockTrader.getCurrentDate())+"\n");
					for (int i=0;i<holdingList.length;i++){
						outputFile.write(holdingList[i][0]);
						outputFile.write(",");
						outputFile.write(holdingList[i][1]);
						outputFile.write(",");
						outputFile.write(holdingList[i][2]);
						outputFile.write(",");
						outputFile.write(holdingList[i][3]);
						outputFile.write("\n");
					}
				}
				else{
					outputFile.write(stockID);
					buyIndex = HoldingInfo.indexOfID(stockID);
					totalShares = buyShare + Integer.parseInt(holdingList[buyIndex][1]);
					outputFile.write(","+totalShares);
					avgPrice = StocksInfo.getAveragePrice(stockID,StockTrader.getCurrentDate());
					outputFile.write(","+avgPrice);
					outputFile.write(",");
					outputFile.write(holdingList[buyIndex][3]);
					outputFile.write("\n");
					
					for (int i=0;i<holdingList.length;i++){
						if (i!=buyIndex){
							outputFile.write(holdingList[i][0]);
							outputFile.write(",");
							outputFile.write(holdingList[i][1]);
							outputFile.write(",");
							outputFile.write(holdingList[i][2]);
							outputFile.write(",");
							outputFile.write(holdingList[i][3]);
							outputFile.write("\n");
						}
					}
				}
				outputFile.close();
			}
			lastTradePrice = tradeStock.getCurrentPrice();
		}
		
		
		boolean isTradeDate = Utilities.isContain(tradeDate,endDate);
		while (!isTradeDate){
			String lastDate ="00/00/0000";
			for (int i=0; i<tradeDate.length && !isTradeDate;i++){
				if (Utilities.DateCompare(endDate,tradeDate[i])== -1 && Utilities.DateCompare(endDate,lastDate)== 1){
					endDate = tradeDate[i];
					isTradeDate = true;
				}
				lastDate = tradeDate[i];
			}
		}
		StockTrader.setCurrentDate(endDate);
		StocksInfo.loadStocks(StockTrader.getCurrentDate());
		HoldingInfo.loadHolding();	
		StockTrader.loadAccInfo();
	}
	
}