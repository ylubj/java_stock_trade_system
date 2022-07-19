import java.io.*;
import java.util.Scanner;

public class HoldingInfo{
	private static String[][] holdingList;
	
	public static void loadHolding() throws IOException{
		File file = new File("shares-holding.csv");
		if (!file.exists()){
			holdingList = null;
			return ;
		}
		Scanner fileReader = new Scanner(file);
		int numHolding = 0;
		while (fileReader.hasNext()){
			fileReader.nextLine();
			numHolding++;
		}
		fileReader.close();
		if (numHolding==0){
			holdingList = null;
			return ;
		}
			
		
		fileReader = new Scanner(file);
		holdingList = new String[numHolding][4]; 
		int index = 0;
		while (fileReader.hasNext()) {
			holdingList[index] = fileReader.nextLine().split(","); //StockID,#Shares,AveragePrice,TotalProfit
			holdingList[index][3] = ""+StocksInfo.getStockProfit(holdingList[index][0],StockTrader.getCurrentDate());
			index++;
		}
		fileReader.close();
	}
	
	
	public static String[][] getHoldingList(){
		return holdingList;
	}
	
	public static boolean isHold(String id){
		if (holdingList == null)
			return false;
		for (int i =0; i<holdingList.length;i++){
			if (id.equals(holdingList[i][0]) && !holdingList[i][1].equals("0"))
				return true;
		}
		return false;
	}
	public static int indexOfID(String id){
		for (int i =0;i<holdingList.length;i++)
			if (holdingList[i][0].equals(id))
				return i;
		return 0;
	}
	
}