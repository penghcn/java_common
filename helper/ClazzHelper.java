
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ClazzHelper {
	
	public static <T>void print(T obj) {
		if (obj == null) {
			LogWriter.info("################# print null object>>##########################");
			return;
		}
			
		LogWriter.info("################# begin print <<"+obj.toString()+">>##########################");
		for (Field f : obj.getClass().getDeclaredFields()) {
			pInternal(obj, f);
		}
		printSuperRecursive(obj, obj.getClass());
		
		LogWriter.info("################# end print ############################");
	}
	
	private static <T>void printSuperRecursive(T obj,Class<?> clazz){
		if (clazz.getSuperclass() != null) {
			LogWriter.info("===================== print super =====================");
			for (Field f : clazz.getSuperclass().getDeclaredFields()) {
				pInternal(obj, f);
			}
			printSuperRecursive(obj,clazz.getSuperclass());
		} else {
			return;
		}
	}
	
	private static <T>void pInternal(T obj,Field f){
		String str = "[type] "+f.getType().getName().replace("java.lang.", "").replace("java.util.", "")+
				" \t-->[key] "+f.getName()+
				" \t-->[value] "+getObjVal(obj,f);			
		LogWriter.info(str);
	}
	/**
	 * 
	 * @param obj
	 * @param f
	 * @return
	 */
	public static <T> String getObjVal(T obj,Field f) {
		String v = "";
		try {		
			f.setAccessible(true);
			v = f.get(obj).toString();
			f.setAccessible(false);
		} catch (Exception e) {
			//e.printStackTrace();
			v = "null";
		}
		return v;
	}
	
	public static <T> void setRecentUpdate(T obj) {
		try {		
			Class<?> clazz = obj.getClass();
			Method mut = getClassMethod(clazz,"setRecUpdTs");
			mut.invoke(obj, new Date());						
			
			Method murs = getClassMethod(clazz,"setRecUpdUsr");
			Method murg = getClassMethod(clazz,"getRecUpdUsr");
			if (murg.invoke(obj) == null || murg.invoke(obj).toString().equals("")) {
				String usr = LoginThread.getLoginId();
				murs.invoke(obj, usr == null ? "cron system" : usr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//忽略
		}
	}
	
	public static<T> Method getClassMethod(Class <T> aClazz, String Name) {
		Method[] declaredMethods = aClazz.getDeclaredMethods();
		for (Method m : declaredMethods) {
			if (m.getName().equals(Name))
				return m;
			
		}
		Class<?> superclass = aClazz.getSuperclass();
		if (superclass != null)
			return getClassMethod(superclass, Name);
		return null;
	}
	
	
	public static Field getClazzFieldByNm(Class<?> clazz,String name) {
		Field f = null;
		try {
			f = clazz.getDeclaredField(name);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		if (f == null && clazz.getSuperclass() != null){
			f = getClazzFieldByNm(clazz.getSuperclass(),name);
		} else {
			return f;
		}
		return f;		
	}
	
	
	public static List<String> getValByStrArr(Object bean,String[] validate,String index){
		List<String> str = new LinkedList<String>();
		if (index != null)
			str.add(index);
		
		for (int i = 0; i < validate.length; i++) {
			String k = validate[i];//System.out.println("k:"+k);
			if (k.equals(""))
				continue;
			
			try {
				Field fg = getClazzFieldByNm(bean.getClass(), k);
				if (fg == null)
					continue;
				fg.setAccessible(true);
				
				if (fg.get(bean) == null)
					continue;
				
				String t = fg.getType().getName();
				String v = "";
				if (t.equals("java.lang.Double"))
					v = new BigDecimal(((Double)fg.get(bean))).divide(new BigDecimal(1.0),2,BigDecimal.ROUND_HALF_UP).toString();
				else if (t.equals("int"))
					v = Integer.toString((Integer)fg.get(bean));
				else if (t.equals("java.util.Date"))
					v = DateUtil.Date2Str((Date)fg.get(bean), DateUtil.LOCALE_FORMAT);
				else
					v = fg.get(bean).toString();
				//add...
				str.add(v);
				fg.setAccessible(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return str;
	}
	public static List<String> getValByStrExp(Object bean,String[] exp,String index){
		List<String> str = new LinkedList<String>();
		if (index != null)
			str.add(index);
		//要求bean的私有属性是按顺序书写的
		for (Field f : bean.getClass().getDeclaredFields()) {
			//
			String k = f.getName();System.out.println("k:"+k);
			String t = f.getType().getName();
			String v = "";
			if (!Arrays.asList(exp).contains(k)) {
				
				try {
					Field fg = bean.getClass().getDeclaredField(k);
					fg.setAccessible(true);
					
					if (fg.get(bean) == null) 
						continue;
						

					if (t.equals("java.lang.Double") || t.equals("double"))
						v = new BigDecimal(((Double)fg.get(bean))).divide(new BigDecimal(1.0),2,BigDecimal.ROUND_HALF_UP).toString();
					else if (t.equals("int") || t.equals("java.lang.Integer"))
						v = Integer.toString((Integer)fg.get(bean));
					else if (t.equals("long") || t.equals("java.lang.Long"))
						v = Long.toString((Long)fg.get(bean));
					else
						v = fg.get(bean).toString();
					//add...
					str.add(v);
					fg.setAccessible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		}
		return str;
	}
	
	public static List<String> getValByStrExpNotNull(Object bean,String[] exp,String index){
		List<String> str = new LinkedList<String>();
		if (index != null)
			str.add(index);
		//要求bean的私有属性是按顺序书写的
		for (Field f : bean.getClass().getDeclaredFields()) {
			//
			String k = f.getName();System.out.println("k:"+k);
			String t = f.getType().getName();
			String v = "";
			if (!Arrays.asList(exp).contains(k)) {
				
				try {
					Field fg = bean.getClass().getDeclaredField(k);
					fg.setAccessible(true);
					
					if (fg.get(bean) == null) {
						str.add("");
						fg.setAccessible(false);
						continue;
					}
						

					if (t.equals("java.lang.Double") || t.equals("double"))
						v = new BigDecimal(((Double)fg.get(bean))).divide(new BigDecimal(1.0),2,BigDecimal.ROUND_HALF_UP).toString();
					else if (t.equals("int") || t.equals("java.lang.Integer"))
						v = Integer.toString((Integer)fg.get(bean));
					else if (t.equals("long") || t.equals("java.lang.Long"))
						v = Long.toString((Long)fg.get(bean));
					else
						v = fg.get(bean).toString();
					//add...
					str.add(v);
					fg.setAccessible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		}
		return str;
	}
	
}
