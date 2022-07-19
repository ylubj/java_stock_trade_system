import java.io.*;
import java.util.Scanner;

public class Utilities{
    public static int DateCompare(String date, String comparedDate){
        int[] dateArray = transformStringArray(date.split("/"));
        int[] comparedArray = transformStringArray(comparedDate.split("/"));
        
        // date is before comparedDate
        if (dateArray[2]<comparedArray[2] || (dateArray[2]==comparedArray[2] && dateArray[1]<comparedArray[1])  || (dateArray[2]==comparedArray[2] && dateArray[1]== comparedArray[1] && dateArray[0]< comparedArray[0]) )
            return -1;
        else if (dateArray[0]== comparedArray[0] && dateArray[1]== comparedArray[1] && dateArray[2]== comparedArray[2]) // same date
            return 0;
        else
            return 1; // date is after comparedDate
    }
    
	public static int[] transformStringArray(String[] array){
		int[] dateArray = new int[array.length];
		for (int i=0; i<dateArray.length;i++){
			dateArray[i] = Integer.parseInt(array[i]);
		}
		return dateArray;
	}
	public static String[] deepCopyStringArray(String[] array, int start, int end){
		if (end < start)
			return null;
		String[] newArray = new String[end-start+1];
		int j=0;
		for (int i=start;i<end+1;i++)
			newArray[j++] = array[i];
		return newArray;
	}
	
	public static boolean isContain(String[] array, String target){
		for (int i=0;i<array.length;i++){
			if (target.equals(array[i]))
				return true;
		}
		return false;
	}
	
	public static int getStringArrayIndex(String[] array,String target){
		int index=0;
		for (int i=0;i<array.length;i++){
			if (target.equals(array[i]))
				index = i;
		}
		return index;
	}
	//-----------------
	public static void sortRecord(String[][] array,int start, int end, int type){
		if (start>=end)
			return ;
		int m = partitionRecord(array,start,end,type);
		sortRecord(array,start,m-1,type);
		sortRecord(array,m+1,end,type);
	}
	public static int partitionRecord(String[][] array, int start, int end,int type){
		String[] pivot = array[end];
		int numLarger = countLargerRecord(array, start, end-1,pivot,type);
		String[][] leftArray = new String[numLarger][5];
		String[][] rightArray = new String[end - start - numLarger][5];
		
		int lCount = 0;
		int rCount = 0;
		
		switch (type){ 
			case 1:
				for (int i = start; i <end;i++){
					if (DateCompare(array[i][type-1],pivot[type-1])== 1) 
						leftArray[lCount++] = array[i];
					else 
						rightArray[rCount++] = array[i];
				}
				break;
			case 2: // order by price
				for (int i = start; i <end;i++){
					if (Double.parseDouble(array[i][type])>Double.parseDouble(pivot[type]))   
						leftArray[lCount++] = array[i];
					else 
						rightArray[rCount++] = array[i];
				}
				break;
			case 3: // order by shares
				for (int i = start; i <end;i++){
					if (Integer.parseInt(array[i][type])>Integer.parseInt(pivot[type]))   
						leftArray[lCount++] = array[i];
					else 
						rightArray[rCount++] = array[i];
				}
				break;
			case 4: // order by amount
				for (int i = start; i <end;i++){
					if ( Integer.parseInt(array[i][3])* Double.parseDouble(array[i][2]) >  Integer.parseInt(pivot[3])*Double.parseDouble(pivot[2]) )   
						leftArray[lCount++] = array[i];
					else 
						rightArray[rCount++] = array[i];
				}
				break;
			case 5: // order by String id 1
				for (int i = start; i <end;i++){
					if (array[i][type-4].compareTo(pivot[type-4]) >0)  
						leftArray[lCount++] = array[i];
					else 
						rightArray[rCount++] = array[i];
				}
				break;
		}
		
		for (int i = start;i<start+numLarger;i++)
			array[i] = leftArray[i-start];
		
		array[start+numLarger]=pivot;
		
		for (int i = 0; i<rightArray.length;i++){
			array[i+start+numLarger+1] = rightArray[i];
		}
		
		return start+numLarger;
		
	}
	// array: 0Date,2Price,3#Shares,Direction 1 =buy 2 =sell
	public static int countLargerRecord(String[][] array, int start, int end, String[] target,int type){
		int count =0;
		switch (type){ 
			case 1: // order by date
				for (int i = start; i <=end;i++){
					if (DateCompare(array[i][type-1],target[type-1])== 1) 
						count++;
				}
				return count;
			case 2: // order by price
				for (int i = start; i <=end;i++){
					if (Double.parseDouble(array[i][type])>Double.parseDouble(target[type]))   
						count++;
				}
				return count;
			case 3: // order by shares
				for (int i = start; i <=end;i++){
					if (Integer.parseInt(array[i][type])>Integer.parseInt(target[type]))   
						count++;
				}
				return count;
			case 4: // order by amount
				for (int i = start; i <=end;i++){
					if ( Integer.parseInt(array[i][3])* Double.parseDouble(array[i][2]) >   Integer.parseInt(target[3])*Double.parseDouble(target[2]) )   
						count++;
				}
				return count;
			case 5: // order by String id 1
				for (int i = start; i <=end;i++){
					if (array[i][type-4].compareTo(target[type-4]) >0)  
						count++;
				}
				return count;
		}
		return count;
	}
	
	
	
	
	//---------------------
	public static void sortDate(String[] array, int start, int end){
		if(start >=end)
			return;
		int m = partition(array,start,end);
		sortDate(array,start,m-1);
		sortDate(array,m+1,end);
	}
	
	public static int partition(String[] array, int start, int end){
		String pivot = array[end];
		int numSmaller = countSmaller(array, start, end-1,pivot);
		String[] leftArray = new String[numSmaller];
		String[] rightArray = new String[end - start - numSmaller];
		
		
		int lCount = 0;
		int rCount = 0;
		for (int i = start;i<end;i++){ // i<end means: not include pivot
			if (DateCompare(pivot, array[i])== 1){
				leftArray[lCount] = array[i];
				lCount++;
			}
			else{
				rightArray[rCount] = array[i];
				rCount++;
			}
		}
		
		for (int i = start;i<start+numSmaller;i++)
			array[i] = leftArray[i-start];
		
		array[start+numSmaller]=pivot;
		
		for (int i = 0; i<rightArray.length;i++){
			array[i+start+numSmaller+1] = rightArray[i];
		}
		
		return start+numSmaller;
  	}
	
	
	public static int countSmaller(String[] array, int start, int end, String target){
		int count =0;
		for (int i = start; i <=end;i++){
			if (DateCompare(target, array[i])== 1)   
				count++;
		}
		return count;
	}
	
	// ------------------
	

	
}

