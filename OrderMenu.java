import java.io.*;
public class OrderMenu extends Menu{
	
	private String[][] recordArray;
	private String stockID,startDate,endDate;
	
	public OrderMenu(String _startDate, String _endDate) throws IOException{
		startDate = _startDate;
		endDate = _endDate;
		stockID = null;
		recordArray = RecordInfo.getStocksRecord(startDate,endDate);
	}
	
	public OrderMenu(String id, String _startDate, String _endDate) throws IOException{
		startDate = _startDate;
		endDate = _endDate;
		stockID = id;
		recordArray = RecordInfo.getOneStockRecord(id,startDate,endDate);
	}

	@Override
	public void printMenu(){
		Screen.printOrderMenu();
	}
	
	private void printRecordArray(){
		if (recordArray==null)
			return ; 
		for (int i = 0 ;i<recordArray.length;i++){ 
				int type = Integer.parseInt(recordArray[i][4]);
				double price = Double.parseDouble(recordArray[i][2]);
				int shares = Integer.parseInt(recordArray[i][3]);
				Screen.printTradeRecord(recordArray[i][0],recordArray[i][1],type,price,shares,price*shares);
		}
	}
	
	@Override
	public NavigationData performAction(String optionIndex) throws IOException{
		int option = Integer.parseInt(optionIndex);
		switch (option){
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				Utilities.sortRecord(recordArray,0, recordArray.length-1, option);
				printRecordArray();
				return new NavigationData(ConstantFlags.NAV_Order,stockID,startDate,endDate);  
			case 6:
				return new NavigationData(ConstantFlags.NAV_BACK,null); 
		}
		return null;
	}
	
}