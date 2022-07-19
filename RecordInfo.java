import java.util.Scanner;
import java.io.*;

public class RecordInfo{
	
	public static String[][] getOneStockRecord(String id, String start, String end)throws IOException{
		File recordFile = new File("trade-record.csv");
		// Date,StockID,Price,#Shares,Direction 1 =buy 2 =sell
		Scanner fileReader = new Scanner(recordFile);
		int numRecord = 0;
		while (fileReader.hasNext()){
			String[] dataArr = fileReader.nextLine().split(",");
			if ( Utilities.DateCompare(start, dataArr[0])<=0 && Utilities.DateCompare(end, dataArr[0])>=0 && dataArr[1].equals(id))
				numRecord++;
		}
		fileReader.close();
		
		fileReader = new Scanner(recordFile);
		String[][] recordArray = new String[numRecord][5];
		int index = 0;
		while (fileReader.hasNext()){
			String[] dataArr = fileReader.nextLine().split(","); // Date,StockID,Price,#Shares,Direction 1 =buy 2 =sell
			if ( Utilities.DateCompare(start, dataArr[0])<=0 && Utilities.DateCompare(end, dataArr[0])>=0 && dataArr[1].equals(id)){
				// date id direction price share
				recordArray[index++]=dataArr;
			}	
		}
		fileReader.close();
		return recordArray;
	}
	
	public static String[][] getStocksRecord(String start, String end)throws IOException{
		File recordFile = new File("trade-record.csv");
		// Date,StockID,Price,#Shares,Direction 1 =buy 2 =sell
		if (!recordFile.exists())
			return null;
		
		Scanner fileReader = new Scanner(recordFile);
		int numRecord = 0;
		while (fileReader.hasNext()){
			String[] dataArr = fileReader.nextLine().split(",");
			if ( Utilities.DateCompare(start, dataArr[0])<=0 && Utilities.DateCompare(end, dataArr[0])>=0 )
				numRecord++;
		}
		fileReader.close();
		
		fileReader = new Scanner(recordFile);
		String[][] recordArray = new String[numRecord][5];
		int index = 0;
		while (fileReader.hasNext()){
			String[] dataArr = fileReader.nextLine().split(","); // Date,StockID,Price,#Shares,Direction 1 =buy 2 =sell
			if ( Utilities.DateCompare(start, dataArr[0])<=0 && Utilities.DateCompare(end, dataArr[0])>=0 ){
				// date id direction price share
				recordArray[index++]=dataArr;
			}	
		}
		fileReader.close();
		return recordArray;
	}
	
	
	
	
	
}