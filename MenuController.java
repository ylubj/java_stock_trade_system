import java.io.*;

public class MenuController {
	private static Menu currMenu;
	
	public static void startMenu() throws IOException {
		currMenu = new MainMenu();
		askMenuOption();
		
	}
	
	public static void askMenuOption() throws IOException {
		NavigationData data = null;
		do {
			currMenu.printMenu();
			Screen.printSelectOption();
			String optionIndex = Screen.keyboard.nextLine();
			data = currMenu.performAction(optionIndex);
			if (data!= null)
				navigate(data);
		} while (data!= null);
	}
	
	public static void navigate(NavigationData nd) throws IOException{
		switch (nd.getNavTo()){
			case ConstantFlags.NAV_BACK:
				Menu parentMenu = currMenu.getParentMenu();
				currMenu.setParentMenu(null);
				currMenu = parentMenu;
				break;
			case ConstantFlags.NAV_MAIN:
				Menu mainMenu = new MainMenu();
				mainMenu.setParentMenu(currMenu);
				currMenu = mainMenu;
				break;
			case ConstantFlags.NAV_Enquire:
				Menu enquiryMenu;
				if(nd.getStockID()==null)
				{
					Screen.printEnterStockId();
					String stockId = Screen.keyboard.nextLine();
					while (StocksInfo.findStockByID(stockId)==null){
						Screen.printInvalidStockId();
						Screen.printEnterStockId();
						stockId = Screen.keyboard.nextLine();
					}
					enquiryMenu = new EnquiryMenu(stockId);	
				}
				else
					enquiryMenu = new EnquiryMenu(nd.getStockID());
				enquiryMenu.setParentMenu(currMenu);
				currMenu = enquiryMenu;
				break;
			case ConstantFlags.NAV_Trade:
				Menu tradeMenu;
				if(nd.getStockID()==null)
				{
					Screen.printEnterStockId();
					String stockId = Screen.keyboard.nextLine();
					while (StocksInfo.findStockByID(stockId)==null){
						Screen.printInvalidStockId();
						Screen.printEnterStockId();
						stockId = Screen.keyboard.nextLine();
					}
					tradeMenu = new TradeMenu(stockId);	
				}
				else
					tradeMenu = new TradeMenu(nd.getStockID());
				tradeMenu.setParentMenu(currMenu);
				currMenu = tradeMenu;
				break;
			case ConstantFlags.NAV_Record:
				Menu myRecordMenu = new MyRecordMenu();
				myRecordMenu.setParentMenu(currMenu);
				currMenu = myRecordMenu;
				break; 
			case ConstantFlags.NAV_Order:
				Menu orderMenu;
				if(nd.getStockID()==null)
					orderMenu= new OrderMenu(nd.getStartDate(),nd.getEndDate());
				else
					orderMenu= new OrderMenu(nd.getStockID(),nd.getStartDate(),nd.getEndDate());
				orderMenu.setParentMenu(currMenu);
				currMenu = orderMenu;
				break; 
		}
	}
	
}