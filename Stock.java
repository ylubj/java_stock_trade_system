public class Stock{
    private String stockID;
    private double currentPrice;
    
    
    
    public void setStockID(String _stockID){
        stockID = _stockID;
    }
    
    
    public void setCurrentPrice(double _currentPrice) {
		currentPrice = _currentPrice;
    }
    
    public String getStockID(){
        return stockID;
    }
    
    public double getCurrentPrice(){
        return currentPrice;
    }
	
	public void setProfit(double _profit) {
    }
	    
    public void setNAV(double _NAV) {
    }
	    
    public void setDividend(double _dividend) {
    }
	
	public void setListOfStockID (Stock[] _listOfStockID){
	}
	
	public Stock[] getListOfStockID(){
		return null;
	}
	
	public double getProfit(){
        return 0.0;
    }
    
    public double getNAV(){
        return 0.0;
    }
    
    public double getDividend(){
        return 0.0;
    }
}