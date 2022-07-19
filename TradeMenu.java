import java.io.*;	
public class TradeMenu extends Menu{
	
	private String stockID;
	
	public TradeMenu(String _stockID){
		stockID = _stockID;
	}
	

	@Override
	public void printMenu(){
		Stock tradeStock = StocksInfo.findStockByID(stockID);
		Screen.printStockPriceInfo(tradeStock.getStockID(),tradeStock.getCurrentPrice());
		Screen.printTradeMenu();
	}
	
	
	@Override
	public NavigationData performAction(String optionIndex) throws IOException{
		Stock tradeStock= StocksInfo.findStockByID(stockID);
		String[][] holdingList = HoldingInfo.getHoldingList();
		File recordFile = new File("trade-record.csv"); // Date,StockID,Price,#Shares,Direction
		File holdingFile = new File("shares-holding.csv"); // StockID,#Shares,AveragePrice,TotalProfit
		FileWriter outputFile;
		switch (optionIndex){
			case "1":
				if (StockTrader.getAccCash()<tradeStock.getCurrentPrice()){
					Screen.printInsufficientCash();
					return new NavigationData(ConstantFlags.NAV_Trade,stockID);
				}
				else{
					int max = (int)(StockTrader.getAccCash()/tradeStock.getCurrentPrice());
					int shares;
					do{
						Screen.printAmountOfTradeSharePrompt(max);
						shares = Integer.parseInt(Screen.keyboard.nextLine());
						if (shares>max)
							Screen.printInsufficientCash();
					} while (shares>max);
						
					outputFile = new FileWriter(recordFile, true);
					outputFile.write(StockTrader.getCurrentDate()+","+stockID+","+tradeStock.getCurrentPrice()+","+shares+",1\n");
					outputFile.close();
					
					outputFile = new FileWriter(holdingFile);
					if (holdingList==null){
						outputFile.write(stockID+","+shares+","+tradeStock.getCurrentPrice()+",0.0\n");
					}
					else if (!HoldingInfo.isHold(stockID)){
						outputFile.write(stockID);
						outputFile.write(",");
						outputFile.write(""+shares);
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
						int index = HoldingInfo.indexOfID(stockID);
						int totalShares = shares + Integer.parseInt(holdingList[index][1]);
						outputFile.write(","+totalShares);
						double avgPrice = StocksInfo.getAveragePrice(stockID,StockTrader.getCurrentDate());
						outputFile.write(","+avgPrice);
						outputFile.write(",");
						outputFile.write(holdingList[index][3]);
						outputFile.write("\n");
						for (int i=0;i<holdingList.length;i++){
							if (i!=index){
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
					HoldingInfo.loadHolding();
					StockTrader.loadAccInfo();
				}
				return new NavigationData(ConstantFlags.NAV_Trade,stockID);
			case "2":
				if (!HoldingInfo.isHold(stockID)){
					Screen.printInsufficientShare();
					return new NavigationData(ConstantFlags.NAV_Trade,stockID);
				}
				else{
					int sellIndex = HoldingInfo.indexOfID(stockID);
					int sellMax = Integer.parseInt(holdingList[sellIndex][1]);
					int sellShare;
					do {
						Screen.printAmountOfTradeSharePrompt(sellMax);
						sellShare = Integer.parseInt(Screen.keyboard.nextLine());
						if (sellShare>sellMax)
							Screen.printInsufficientShare();
					} while (sellShare>sellMax);
					
					outputFile = new FileWriter(recordFile, true);
					outputFile.write(StockTrader.getCurrentDate()+","+stockID+","+tradeStock.getCurrentPrice()+","+sellShare+",2\n");
					outputFile.close();
					
					outputFile = new FileWriter(holdingFile);
					double profit = StocksInfo.getStockProfit(stockID,StockTrader.getCurrentDate());
					int remainShare = sellMax-sellShare;
					if (remainShare!=0)
						outputFile.write(stockID+","+remainShare+","+holdingList[sellIndex][2]+","+ profit +"\n");
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
					StockTrader.loadAccInfo();
					HoldingInfo.loadHolding();
					
				}
				return new NavigationData(ConstantFlags.NAV_Trade,stockID);
			case "3":
				return new NavigationData(ConstantFlags.NAV_BACK,null); 
		}
		return null;
		
	}
	
}