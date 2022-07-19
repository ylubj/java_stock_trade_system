import java.io.*;

public class MainMenu extends Menu{
	@Override
	public void printMenu(){
		Screen.printMainMenu(StockTrader.getCurrentDate());
	}
	
	@Override
	public NavigationData performAction(String optionIndex) throws IOException{
		boolean repeat;
		do{
			switch (optionIndex){
				case "1":
					return new NavigationData(ConstantFlags.NAV_Record,null);
				case "2":
					return new NavigationData(ConstantFlags.NAV_Enquire,null);
				case "3":
					return new NavigationData(ConstantFlags.NAV_Trade,null);
				case "4":
					AutoTrade.setAutoTradeDateRange();
					AutoTrade.launchAutoTrade();
					return new NavigationData(ConstantFlags.NAV_MAIN,null);
				case "5":
					String[] tradeDate = StocksInfo.getTradeDate();
					int index = Utilities.getStringArrayIndex(tradeDate,StockTrader.getCurrentDate());
					StockTrader.setCurrentDate(tradeDate[index+1]);
					StocksInfo.loadStocks(StockTrader.getCurrentDate());
					HoldingInfo.loadHolding();
					StockTrader.loadAccInfo();
					return new NavigationData(ConstantFlags.NAV_MAIN,null);
				case "6":
					System.exit(0);
				default:
					Screen.printInvalidMainMenuOption();
					Screen.printSelectOption();
					optionIndex = Screen.keyboard.nextLine();
					repeat=true;
					break;
			}
		} while (repeat);
		return null;
	}
}