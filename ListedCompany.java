public class ListedCompany extends Stock{
    
    private double profit;
    private double NAV;
    private double dividend;


	@Override 
    public void setProfit(double _profit) {
		profit = _profit;
    }
	@Override    
    public void setNAV(double _NAV) {
		NAV = _NAV;
    }
	@Override    
    public void setDividend(double _dividend) {
		dividend = _dividend;
    }
	    
	
    @Override
    public double getProfit(){
        return profit;
    }
    @Override
    public double getNAV(){
        return NAV;
    }
    @Override
    public double getDividend(){
        return dividend;
    }
    
    
}