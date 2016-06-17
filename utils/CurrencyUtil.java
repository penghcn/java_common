package cn.pengh.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class CurrencyUtil {
	private static final BigDecimal BD0 = BigDecimal.ZERO;
	private static final BigDecimal BD1 = BigDecimal.ONE;
	private static final BigDecimal BD100 = new BigDecimal(100);
	private static final BigDecimal BD1_NEGATIVE = new BigDecimal(-1);
	private static final int SCALE = 2;
	private static final String CURRENCY_FORMAT = "#,#00.00#";
	
	//元转分
	public static BigDecimal yuan2fen(Object money) {
		return yuan2fen(money, SCALE);
	}
	
	public static BigDecimal yuan2fen(Object money,int SCALE) {
		BigDecimal bd = new BigDecimal(_getStrMoney(money));
		return bd.multiply(BD100).setScale(SCALE,BigDecimal.ROUND_HALF_UP);
	}
	//分转元
	public static BigDecimal fen2yuan(Object money,int SCALE) {
		BigDecimal bd = new BigDecimal(_getStrMoney(money));
		return bd.divide(BD100,SCALE,BigDecimal.ROUND_HALF_UP);
	}
	public static BigDecimal fen2yuan(Object money) {
		return fen2yuan(money, SCALE);
	}
	
	
	public static BigDecimal negative(Object money,int SCALE) {
		BigDecimal bd = new BigDecimal(_getStrMoney(money));
		return bd.setScale(SCALE,BigDecimal.ROUND_HALF_UP).multiply(BD1_NEGATIVE);
	}
	
	public static BigDecimal convert(Object money,int SCALE) {
		BigDecimal bd = new BigDecimal(_getStrMoney(money));
		return bd.setScale(SCALE,BigDecimal.ROUND_HALF_UP).multiply(BD1);
	}
	
	public static BigDecimal convert(Object money) {
		return convert(money, SCALE);
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
	public static String yuan2fenStr(Object money) {
		return yuan2fen(money).toString();
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
	public static <T> BigDecimal add(int SCALE,T...ds) {
    	BigDecimal bigD = BD0;
    	if (ds == null) 
    		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
		for (T t : ds) {
			if (t != null)
				bigD = bigD.add(new BigDecimal(_getStrMoney(t)));
		}
		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }
	public static BigDecimal add(int SCALE,double...ds) {
    	BigDecimal bigD = BD0;
		for (double d : ds) {
			bigD = bigD.add(new BigDecimal(Double.toString(d)));
		}
		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }
	public static BigDecimal add(int SCALE,float...ds) {
    	BigDecimal bigD = BD0;
		for (float d : ds) {
			bigD = bigD.add(new BigDecimal(Float.toString(d)));
		}
		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }
	public static BigDecimal add(int SCALE,int...ds) {
    	BigDecimal bigD = BD0;
		for (int d : ds) {
			bigD = bigD.add(new BigDecimal(d));
		}
		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }
	public static BigDecimal add(int SCALE,long...ds) {
    	BigDecimal bigD = BD0;
		for (long d : ds) {
			bigD = bigD.add(new BigDecimal(d));
		}
		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }
	public static BigDecimal add(int SCALE,short...ds) {
    	BigDecimal bigD = BD0;
		for (short d : ds) {
			bigD = bigD.add(new BigDecimal(d));
		}
		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }
	public static BigDecimal add(int SCALE,byte...ds) {
    	BigDecimal bigD = BD0;
		for (byte d : ds) {
			bigD = bigD.add(new BigDecimal(d));
		}
		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }
	public static <T>BigDecimal add(T...ds) {
    	return add(SCALE, ds);
    }	
	public static <T>BigDecimal add(double...ds) {
    	return add(SCALE, ds);
    }
	public static <T>BigDecimal add(float...ds) {
    	return add(SCALE, ds);
    }
	public static BigDecimal add(long...ds) {
    	return add(SCALE, ds);
    }
	public static BigDecimal add(int...ds) {
    	return add(SCALE,ds);
    }
	public static BigDecimal add(short...ds) {    	
		return add(SCALE,ds);
    }
	public static BigDecimal add(byte...ds) {    	
		return add(SCALE,ds);
    }
	public static <T> BigDecimal add(int SCALE,List<T> ls) {
		return add(SCALE,StringUtil.list2Array(ls));
	}
	public static <T> BigDecimal add(List<T> ls) {
		return add(SCALE,ls);
	}
		
	public static BigDecimal add(Object money,Object money2,int SCALE) {	
		return add(SCALE,new String[]{ _getStrMoney(money), _getStrMoney(money2)});
    }
	public static BigDecimal add(Object money,Object money2) {	
		return add(money, money2, SCALE);
    }

    //乘法	
	public static <T> BigDecimal multiply(int SCALE,T... ds) {
    	BigDecimal bigD = BD1;		
		for (T d : ds) {
			if (d != null)
				bigD = bigD.multiply(new BigDecimal(_getStrMoney(d)));
		}
		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }
	
	public static BigDecimal multiply(int SCALE,double... ds) {
    	BigDecimal bigD = BD1;	
		for (double d : ds) {
			bigD = bigD.multiply(new BigDecimal(Double.toString(d)));
		}
		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }
	public static BigDecimal multiply(int SCALE,float... ds) {
    	BigDecimal bigD = BD1;	
		for (float d : ds) {
			bigD = bigD.multiply(new BigDecimal(Float.toString(d)));
		}
		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }
	public static BigDecimal multiply(int SCALE,long... ds) {
    	BigDecimal bigD = BD1;	
		for (long d : ds) {
			bigD = bigD.multiply(new BigDecimal(Long.toString(d)));
		}
		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }
	public static BigDecimal multiply(int SCALE,int... ds) {
    	BigDecimal bigD = BD1;	
		for (long d : ds) {
			bigD = bigD.multiply(new BigDecimal(d));
		}
		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }
	public static BigDecimal multiply(int SCALE,short... ds) {
    	BigDecimal bigD = BD1;	
		for (short d : ds) {
			bigD = bigD.multiply(new BigDecimal(d));
		}
		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }
	public static BigDecimal multiply(int SCALE,byte... ds) {
    	BigDecimal bigD = BD1;	
		for (byte d : ds) {
			bigD = bigD.multiply(new BigDecimal(d));
		}
		return bigD.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }

	public static<T> BigDecimal multiply(T... ds) {
    	return multiply(SCALE, ds);
    }
	public static BigDecimal multiply(double... ds) {
    	return multiply(SCALE, ds);
    }
	public static BigDecimal multiply(float... ds) {
    	return multiply(SCALE, ds);
    }
	public static BigDecimal multiply(long... ds) {
    	return multiply(SCALE, ds);
    }
	public static BigDecimal multiply(int... ds) {
		return multiply(SCALE, ds);
    }
	public static BigDecimal multiply(short... ds) {
		return multiply(SCALE, ds);
    }
	public static BigDecimal multiply(byte... ds) {
		return multiply(SCALE, ds);
    }
	public static <T> BigDecimal multiply(int SCALE,List<T> ls) {
		return multiply(SCALE,StringUtil.list2Array(ls));
	}
	public static <T> BigDecimal multiply(List<T> ls) {
		return multiply(SCALE,ls);
	}
	public static BigDecimal multiply(Object money,Object money2,int SCALE) {	
		return multiply(SCALE,new String[]{ _getStrMoney(money), _getStrMoney(money2)});
    }
	public static BigDecimal multiply(Object money,Object money2) {	
		return multiply(money, money2, SCALE);
    }
	/**
	 * son/mum
	 * @param son
	 * @param mum
	 * @return
	 */
	public static BigDecimal divide(Object son,Object mum) {    	
		return divide(son, mum, SCALE);
    }
	public static BigDecimal divide(Object son,Object mum,int SCALE) {  
		String mumStr = _getStrMoney(mum);
		//分母为0
		if ("0".equals(mumStr))
			return BD0.divide(BD1, SCALE, BigDecimal.ROUND_HALF_UP);
		return new BigDecimal(_getStrMoney(son)).divide(new BigDecimal(mumStr), SCALE, BigDecimal.ROUND_HALF_UP);//2位小数
    }
	
	public static String rate(Object son,Object mum) { 		
		return rate(son, mum, SCALE);//2位小数
    }
	
	
	public static String rate(Object son,Object mum,int SCALE) { 
		BigDecimal sunBd = new BigDecimal(_getStrMoney(son)).multiply(BD100),mumBd = new BigDecimal(_getStrMoney(mum)) ;
		if (sunBd.intValue() == 0 || mumBd.intValue() == 0)
			return BD0.divide(BD1, SCALE, BigDecimal.ROUND_HALF_UP).toString();
		return sunBd.divide(mumBd, SCALE, BigDecimal.ROUND_HALF_UP).toString();
    }
	
	public static String format(Object money) {
		return format(money,CURRENCY_FORMAT);
	}
	public static String format(Object money,String formatReg) {
		DecimalFormat df = new DecimalFormat(formatReg);
		return df.format(new BigDecimal(_getStrMoney(money)).doubleValue()).toString().replaceAll("^0(\\d).(\\d+)$", "$1.$2");
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
}
