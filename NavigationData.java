public class NavigationData{
	private int navTo;
	private String stockID;
	private String startDate;
	private String endDate;
	
	public NavigationData(int n, String s){
		navTo = n;
		stockID = s;
		startDate= null;
		endDate = null;
	}
	
	public NavigationData(int n, String s, String start, String end){
		navTo = n;
		stockID = s;
		startDate= start;
		endDate = end;
	}
	
	
	public int getNavTo(){
		return navTo;
	}
	
	public String getStockID(){
		return stockID;
	}
	
	public String getStartDate(){
		return startDate;
	}
	
	public String getEndDate(){
		return endDate;
	}
	
	
}