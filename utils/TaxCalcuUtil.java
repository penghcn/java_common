

import java.math.BigDecimal;


/**
 * 
 * @author penghcn
 * @created 2014年10月31日下午6:05:08
 */
public class TaxCalcuUtil {
  
    
    //加法
    private static double add(Double...ds) {
    	BigDecimal bigD = new BigDecimal("0");
		for (int i = 0; i < ds.length; i++) {
			if (ds[i] != null)
				bigD = bigD.add(new BigDecimal(Double.toString(ds[i])));
		}
		//return bigD.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return bigD.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    //乘法
    private static double mul(Double... ds) {
    	BigDecimal bigD = new BigDecimal("1");
		for (int i = 0; i < ds.length; i++) {
			if (ds[i] != null)
				bigD = bigD.multiply(new BigDecimal(Double.toString(ds[i]))); 
		}
		//除法
		//new BigDecimal("100").divide(new BigDecimal("3"), 2, BigDecimal.ROUND_HALF_UP).doubleValue();//2位小数
		return bigD.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * 个人所得税算法
     * @param preTaxWage
     * @return
     */
    public static long wageTaxCalFen(double preTaxWage) {
    	return MathUtil.multiply2Long(wageTaxCalYuan(MathUtil.divideFinal(preTaxWage,100)),100);
    }
    /**
     * 个人所得税算法
     * @param preTaxWage
     * @return 
     * @return tax
     */
    public static double wageTaxCalYuan(double preTaxWage) {
    	double BENCHMARK = -3500.0,
    			tail = 0,
    			factor = 0,
    			tax = 0,
    			gap = add(preTaxWage,BENCHMARK);
    	
    	//笨办法 ，，， if else if 
    	if (gap <= 0) {
    		//nothing
    	} else if (gap > 0 && gap <= 1500) {
    		factor = 0.03;
    	} else if (gap > 1500 && gap <= 4500) {
    		factor = 0.1;
    		tail = -105;
    	} else if (gap > 4500 && gap <= 9000) {
    		factor = 0.2;
    		tail = -555;
    	} else if (gap > 9000 && gap <= 35000) {
    		factor = 0.25;
    		tail = -1005;
    	} else if (gap > 35000 && gap <= 55000) {
    		factor = 0.3;
    		tail = -2755;
    	} else if (gap > 55000 && gap <= 80000) {
    		factor = 0.35;
    		tail = -5505;
    	} else if (gap > 80000) {
    		factor = 0.45;
    		tail = -13505;
    	}
    	tax = add(mul(gap,factor),tail);
    	//LogWriter.info("个人所得税：("+preTaxWage+BENCHMARK+")*" +factor + tail +" = "+ tax);
    	return tax;
    	
    }
    
    public static void main(String[] args) {
		System.out.println(wageTaxCalYuan(92400));
	}
    
   
}
