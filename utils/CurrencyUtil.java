

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class CurrencyUtil {
	private static BigDecimal bd0 = new BigDecimal(0);
	private static BigDecimal bd1 = new BigDecimal(1);
	private static BigDecimal bd100 = new BigDecimal(100);
	private static final String CURRENCY_FORMAT = "#,#00.00#";
	
	//元转分
	public static BigDecimal yuan2fen(Object money) {
		BigDecimal bd = new BigDecimal(_getStrMoney(money));
		return bd.setScale(2,BigDecimal.ROUND_HALF_UP).multiply(bd100);
	}
	//分转元
	public static BigDecimal fen2yuan(Object money) {
		BigDecimal bd = new BigDecimal(_getStrMoney(money));
		return bd.divide(bd100,2,BigDecimal.ROUND_HALF_UP);
	}
	
	//元转分
	public static BigDecimal convert(Object money) {
		BigDecimal bd = new BigDecimal(_getStrMoney(money));
		return bd.setScale(2,BigDecimal.ROUND_HALF_UP).multiply(bd1);
	}
	
	private static String _getStrMoney(Object money) {
		String strMoney = "0";
		if (money != null) {
			strMoney = money.toString();
			if (strMoney.equals(""))
				strMoney = "0";
		}	
		return strMoney;
	}
	
	
	
	public static long yuan2fenLong(Object money) {
		return yuan2fen(money).longValue();
	}
	public static int yuan2fenInt(Object money) {
		return yuan2fen(money).intValue();
	}	
	public static String fen2yuanStr(Object money) {
		return fen2yuan(money).toString();
	}
	public static int fen2yuanInt(Object money) {
		return fen2yuan(money).intValue();
	}
	public static String fen2yuanWithFormat(Object money) {
		return format(fen2yuanStr(money));
	}
	public static Double convertDouble(Object money) {
		return convert(money).doubleValue();
	}
	public static String convertStr(Object money) {
		return convert(money).toString();
	}
	
	//加减法	
	public static BigDecimal add(Double...ds) {
    	BigDecimal bigD = bd0;
		for (int i = 0; i < ds.length; i++) {
			if (ds[i] != null)
				bigD = bigD.add(new BigDecimal(Double.toString(ds[i])));
		}
		//return bigD.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return bigD.setScale(2,BigDecimal.ROUND_HALF_UP);
    }
	public static BigDecimal add(String...ds) {
    	BigDecimal bigD = bd0;
		for (int i = 0; i < ds.length; i++) {
			if (ds[i] != null)
				bigD = bigD.add(new BigDecimal(ds[i]));
		}
		//return bigD.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return bigD.setScale(2,BigDecimal.ROUND_HALF_UP);
    }
	public static BigDecimal add(List<String> ls) {
		return add(ls.toArray(new String[ls.size()]));
	}
	public static BigDecimal addDouble(List<Double> ls) {
		return add(ls.toArray(new Double[ls.size()]));
	}
    //乘法
	public static BigDecimal multiply(Double... ds) {
    	BigDecimal bigD = bd1;
		for (int i = 0; i < ds.length; i++) {
			if (ds[i] != null)
				bigD = bigD.multiply(new BigDecimal(Double.toString(ds[i]))); 
		}
		//除法
		//new BigDecimal("100").divide(new BigDecimal("3"), 2, BigDecimal.ROUND_HALF_UP).doubleValue();//2位小数
		return bigD.setScale(2,BigDecimal.ROUND_HALF_UP);
    }
	public static BigDecimal multiply(String... ds) {
    	BigDecimal bigD = bd1;
		for (int i = 0; i < ds.length; i++) {
			if (ds[i] != null)
				bigD = bigD.multiply(new BigDecimal(ds[i])); 
		}
		//除法
		//new BigDecimal("100").divide(new BigDecimal("3"), 2, BigDecimal.ROUND_HALF_UP).doubleValue();//2位小数
		return bigD.setScale(2,BigDecimal.ROUND_HALF_UP);
    }
	
	public static BigDecimal divide(String mum,String son) {    	
		//除法
		return new BigDecimal(son).divide(new BigDecimal(mum), 2, BigDecimal.ROUND_HALF_UP);//2位小数
    }
	
	public static String format(double in) {
		DecimalFormat df = new DecimalFormat(CURRENCY_FORMAT);
		return df.format(in).toString();
	}
	public static String format(String in) {
		DecimalFormat df = new DecimalFormat(CURRENCY_FORMAT);
		return df.format(new BigDecimal(in).doubleValue()).toString();
	}
	public static String bytes2HexString(byte[] b) {  
	    String ret = "";  
	    for (int i = 0; i < b.length; i++) {  
	        String hex = Integer.toHexString(b[i] & 0xFF);  
	    if (hex.length() == 1) {  
	        hex = '0' + hex;  
	    }  
	     ret += hex.toUpperCase();  
	  }  
	  return ret;  
	}
	
	protected static void test2() {
		System.out.println(format("22232.9"));
		System.out.println(fen2yuanWithFormat("6049005"));
		System.out.println(yuan2fenLong("4343.657"));
		
		//注意对比
		System.out.println(new BigDecimal("4343.655").multiply(bd100).setScale(2, BigDecimal.ROUND_HALF_UP));
		System.out.println(new BigDecimal("4343.655").multiply(bd100).setScale(2, BigDecimal.ROUND_HALF_UP).longValue());
		System.out.println(new BigDecimal("4343.655").setScale(2, BigDecimal.ROUND_HALF_UP).multiply(bd100));
		System.out.println(new BigDecimal("4343.655").setScale(2, BigDecimal.ROUND_HALF_UP).multiply(bd100).longValue());
	}
	
	
	
	public static void main(String[] args) {
		//test2();
		Double r = 0.0;
		System.out.println(fen2yuanStr(r));
		//test22(r);
		System.out.println(convertStr(r));
		
	}
}
