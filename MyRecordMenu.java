import java.io.*;
public class MyRecordMenu extends Menu{
	
	private int numHolding = 0;

	@Override
	public void printMenu(){
		Screen.printAccInfo(StockTrader.getAccCash(), StockTrader.getOverallProfit());
		String[][] holdingList = HoldingInfo.getHoldingList();
		if (holdingList!=null){
			numHolding = holdingList.length;
			for (int i = 0; i<holdingList.length;i++){
				if (Integer.parseInt(holdingList[i][1])>0)
					Screen.printShareHoldingOption(i+1, holdingList[i][0],Integer.parseInt(holdingList[i][1]), Double.parseDouble(holdingList[i][2]), Double.parseDouble(holdingList[i][3]));
			}
		}
		Screen.printEnquiryTradingRecordsInPeriodOption(numHolding+1);
		Screen.printBackOption(numHolding+2);
	}
	
	
	@Override
	public NavigationData performAction(String optionIndex) throws IOException{
		int option = Integer.parseInt(optionIndex);
		String[][] holdingList = HoldingInfo.getHoldingList();
		String startDate,endDate,recordArray[][];
		boolean inValidEndDate;
		if (option==numHolding+1){
			inValidEndDate = false;
			do{
				Screen.printStartDatePrompt();
				startDate = Screen.keyboard.nextLine();
				Screen.printEndDatePrompt();
				endDate = Screen.keyboard.nextLine();
				inValidEndDate = Utilities.DateCompare(startDate,endDate)==1;
				if (inValidEndDate)
					Screen.printInvalidAutoTradeEndDatePrompt();
			} while(inValidEndDate);
			recordArray = RecordInfo.getStocksRecord(startDate,endDate);
			if (recordArray==null)
				return new NavigationData(ConstantFlags.NAV_Order,null,startDate,endDate);
			Utilities.sortRecord(recordArray,0, recordArray.length-1, 1);
			for (int i = 0 ;i<recordArray.length;i++){ 
				int type = Integer.parseInt(recordArray[i][4]);
				double price = Double.parseDouble(recordArray[i][2]);
				int shares = Integer.parseInt(recordArray[i][3]);
				Screen.printTradeRecord(recordArray[i][0],recordArray[i][1],type,price,shares,price*shares);
			}
			return new NavigationData(ConstantFlags.NAV_Order,null,startDate,endDate);
		}
		else if (option==numHolding+2)
			return new NavigationData(ConstantFlags.NAV_BACK,null);
		else{
			inValidEndDate = false;
			do{
				Screen.printStartDatePrompt();
				startDate = Screen.keyboard.nextLine();
				Screen.printEndDatePrompt();
				endDate = Screen.keyboard.nextLine();
				inValidEndDate = Utilities.DateCompare(startDate,endDate)==1;
				if (inValidEndDate)
					Screen.printInvalidAutoTradeEndDatePrompt();
			} while(inValidEndDate);
			recordArray = RecordInfo.getOneStockRecord(holdingList[option-1][0],startDate,endDate);
			Utilities.sortRecord(recordArray,0, recordArray.length-1, 1);
			for (int i = 0 ;i<recordArray.length;i++){ 
				int type = Integer.parseInt(recordArray[i][4]);
				double price = Double.parseDouble(recordArray[i][2]);
				int shares = Integer.parseInt(recordArray[i][3]);
				Screen.printTradeRecord(recordArray[i][0],recordArray[i][1],type,price,shares,price*shares);
			}
			return new NavigationData(ConstantFlags.NAV_Order,holdingList[option-1][0],startDate,endDate);
		}
	}
	
}