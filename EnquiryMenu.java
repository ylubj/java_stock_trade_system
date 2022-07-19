import java.io.*;

public class EnquiryMenu extends Menu{
	
	private String stockID;
	private int numListOfStockID = 0;
	
	public EnquiryMenu(String _stockID){
		stockID = _stockID;
	}
	
	@Override
	public void printMenu(){
		Stock enquireStock = StocksInfo.findStockByID(stockID);
		if (enquireStock.getListOfStockID()!=null){
			Screen.printStockPriceInfo(enquireStock.getStockID(),enquireStock.getCurrentPrice());
			Stock[] listOfStock = enquireStock.getListOfStockID();
			numListOfStockID = listOfStock.length;
			for (int i=0;i<numListOfStockID;i++){
				Screen.printStockOption(i+1,listOfStock[i].getStockID());
			}
		}
		else {
			Screen.printListedCompanyInfo(enquireStock.getStockID(), enquireStock.getCurrentPrice(), enquireStock.getProfit(), enquireStock.getNAV(), enquireStock.getDividend());
		}
		Screen.printTradeOption(numListOfStockID+1);
		Screen.printPriceInDateRangeOption(numListOfStockID+2);
		Screen.printBackOption(numListOfStockID+3);
	}
	
	@Override
	public NavigationData performAction(String optionIndex) throws IOException{
		int option = Integer.parseInt(optionIndex);
		if (option == numListOfStockID+1)
			return new NavigationData(ConstantFlags.NAV_Trade,stockID);
		else if (option == numListOfStockID+2){
			boolean inValidEndDate = false;
			String startDate,endDate;
			do{
				Screen.printStartDatePrompt();
				startDate = Screen.keyboard.nextLine();
				Screen.printEndDatePrompt();
				endDate = Screen.keyboard.nextLine();
				inValidEndDate = Utilities.DateCompare(startDate,endDate)==1;
				if (inValidEndDate){
					Screen.printInvalidDatePrompt();
				}
			} while(inValidEndDate);
			
			if (Utilities.DateCompare(endDate,StockTrader.getCurrentDate())==1) // endDate after current date
				endDate = StockTrader.getCurrentDate();
			
			String[] stockPrice = StocksInfo.getStockPrice(StocksInfo.findStockByID(stockID));//
			boolean isNoPrice=true;
			for (int i =0;i<stockPrice.length;i++){
				String[] dataArr = stockPrice[i].split(","); // date,price  start date end
				if ( Utilities.DateCompare(startDate, dataArr[0])<=0 && Utilities.DateCompare(endDate, dataArr[0])>=0 ){
					isNoPrice = false;
					Screen.printPrice(dataArr[0],Double.parseDouble(dataArr[1]));
				}
			}
			if (isNoPrice)
				Screen.printNoPriceWithinRange();
			return new NavigationData(ConstantFlags.NAV_Enquire,stockID);
		}
		else if (option == numListOfStockID+3)
			return new NavigationData(ConstantFlags.NAV_BACK,null);

		return new NavigationData(ConstantFlags.NAV_Enquire, StocksInfo.findStockByID(stockID).getListOfStockID()[option-1].getStockID());
	}
}