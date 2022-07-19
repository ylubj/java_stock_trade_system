public class ETF extends Stock{
    
    private Stock[] listOfStockID;
    @Override
	public void setListOfStockID (Stock[] _listOfStockID){
		listOfStockID = _listOfStockID;
	}
	
	@Override
	public Stock[] getListOfStockID(){
		return listOfStockID;
	}
}