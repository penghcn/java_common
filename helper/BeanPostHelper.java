
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 
 * @author penghcn
 * @since  2014-8-13
 */
public class BeanPostHelper {
	@ExceptField
	public static final String _SEQUENCE_ = "@_SEQUENCE_@";	
	
	public static String toUpdateSet(Object obj,String... except) {
		Class<?> clazz = obj.getClass();
		String s = toUpdateSetInternal(obj,clazz,except) + getSuperRecursive(obj, clazz, except);
		s = s.endsWith(",") ? s.substring(0,s.length() - 2) : s;
		
		
		if (s == null || s.equals("")){
			LogWriter.error("生成update set语句为空");
		}
		return " set "+s;
	}
	
	private static String getSuperRecursive(Object obj,Class<?> clazz,String... except){
		String s = "";
		if (s.equals("") && clazz.getSuperclass() != null){
			String superStr = toUpdateSetInternal(obj,clazz.getSuperclass(),except);
			s += superStr == null || superStr.equals("") ? "" : superStr+",";
			s += getSuperRecursive(obj, clazz.getSuperclass(), except);
		} else {
			return s;//回归条件
		}
		return s;
	}
	
	private static String toUpdateSetInternal(Object obj,Class<?> clazz,String... except) {
		if (except == null || except.equals(""))
			except = new String[]{};
		
		StringBuilder sb = new StringBuilder();
		for (Field f : clazz.getDeclaredFields()) {
			String k = f.getName();
			
			//@ExceptField
			if (f.getAnnotation(ExceptField.class) != null)
				continue;
			// 如果是编译器引入的则跳过
			if (f.isSynthetic() || f.isEnumConstant())
				continue;
			if (Arrays.asList(except).contains(k))
				continue;
			
			f.setAccessible(true);
			try {
				Object v = f.get(obj);
				//空
				if (v == null)
					continue;
			} catch (Exception e) {
				// 
				e.printStackTrace();
				throw new IllegalArgumentException("解析update sql语句出错。");
			}
			f.setAccessible(false);
			
			// set ins_cd = :insCd
			sb.append(u2l(k)+" = :"+k+",");
			
		}
		
		return sb.toString();
	}
	
	public static String toInsertSql(Object obj,String... except) {
		Class<?> clazz = obj.getClass();
		List<String> ls = toInsertSqlInternal(obj,clazz,except);
		if (clazz.getSuperclass() != null) {
			List<String> superLs = toInsertSqlInternal(obj,clazz.getSuperclass(),except);
			ls.addAll(superLs);
		}
		
		//
		StringBuilder sbk = new StringBuilder();
		StringBuilder sbv = new StringBuilder();
		for (String key : ls) {
			if (key.endsWith(_SEQUENCE_)){
				key = u2l(key.replaceAll(_SEQUENCE_, ""));
				sbk.append(","+key);
				sbv.append(",next value for "+ key);
				continue;
			}
			sbk.append(","+u2l(key));
			sbv.append(",:"+key);
		}
		if(sbk.length() == 0)
			throw new IllegalArgumentException("解析insert sql语句出错。");
		return "("+sbk.toString().substring(1)+") values ("+sbv.toString().substring(1)+")";
	}
	
	public static List<String> toInsertSqlInternal(Object obj,Class<?> clazz,String... except) {
		if (except == null || except.equals(""))
			except = new String[]{};
		
		List<String> ls = new ArrayList<String>();
		
		for (Field f : clazz.getDeclaredFields()) {
			String k = f.getName();
			
			//@ExceptField
			if (f.getAnnotation(ExceptField.class) != null)
				continue;
			// 如果是编译器引入的则跳过
			if (f.isSynthetic() || f.isEnumConstant())
				continue;
			if (Arrays.asList(except).contains(k))
				continue;
			
			f.setAccessible(true);
			try {
				Object v = f.get(obj);
				//空
				if (v == null)
					continue;
				//sequence
				if (v.toString().equals(_SEQUENCE_)) {
					ls.add(k+_SEQUENCE_);
					continue;
				}
					
			} catch (Exception e) {
				// 
				e.printStackTrace();
				throw new IllegalArgumentException("解析insert sql语句出错。");
			}
			f.setAccessible(false);
			
			// insert into inf.ins_cd = :insCd
			ls.add(k);
			
		}
		
		return ls;
	}
	
	public static String toQuerySql(Object obj,String pre,String... except){
		if (except == null)
			except = new String[]{};
		
		pre = toTblPre(pre);
		
		StringBuilder sb = new StringBuilder();
		
		Class<? extends Object> c = obj.getClass();
		for (Field f : c.getDeclaredFields()) {
			String k = f.getName();
			
			//@ExceptField
			if (f.getAnnotation(ExceptField.class) != null)
				continue;
			// 如果是编译器引入的则跳过
			if (f.isSynthetic() || f.isEnumConstant())
				continue;
			if (Arrays.asList(except).contains(k))
				continue;
			
			f.setAccessible(true);
			try {
				Object v = f.get(obj);
				//空
				if (v == null || v.toString().equals(""))
					continue;
			} catch (Exception e) {
				// 
				e.printStackTrace();
			}
			f.setAccessible(false);
			
			// and inf.ins_cd = :insCd
			sb.append(" and "+pre+u2l(k) + "= :"+k);
			
		}
		return sb.toString();
	}
	
	public static String toTblPre(String pre) {
		if (pre != null && !pre.equals("") && !pre.endsWith("."))
			pre += ".";
		return pre;
	}
	
	public static String toDB2PageSql(String sql) {
		return "SELECT * FROM(" + sql +") AS TBS_LIMIT WHERE TBS_LIMIT.RN BETWEEN :start AND :end";
	}
	
	private static String u2l(String str) {
        StringBuffer sb = new StringBuffer();        
        for(int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if(Character.isUpperCase(ch)) {
            	sb.append("_"+ Character.toLowerCase(ch));
            }
            else {
            	sb.append(ch);
            }
        }
        return sb.toString();
	}
	
	
	
}
