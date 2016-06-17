package cn.pengh.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

import cn.pengh.annotation.ExceptField;
import cn.pengh.library.Log;
import cn.pengh.util.CurrencyUtil;
import cn.pengh.util.DateUtil;
import cn.pengh.util.StringUtil;

public class ClazzHelper {
	public static final String REGX_GET_METHOD = "^(get|is)(\\w+)";
	public static final String REGX_GET_FIELD_TYPE = "(class\\s+?)?(java.lang.)?(java.util.(concurrent.atomic.)?)?";
	public static boolean isLog4j2 = true;
	private static <T>void thisOut(T obj){
		if (isLog4j2)
			Log.info(obj);
		else 
			System.out.println(obj);
	}
	
	public static <T>void printByOut(T obj){
		isLog4j2 = false;
		print(obj);
		isLog4j2 = true;
	}
	
	public static <T>void print(T obj){
		print(obj,false);
	}
	
	public static <T>void print(T obj, boolean isGson) {
		if (obj == null) {
			thisOut("################# print null object>>##########################");
			return;
		}
		if (obj instanceof Map) {
			printMap((Map<?,?>)obj);
			return;
		} else if (obj instanceof Collection) {
			printCollection((Collection<?>) obj);
			return;
		}
		//转成json
		if (isGson)
			thisOut("print as json: "+new Gson().toJson(obj,obj.getClass()));
			
		thisOut("-------------------- begin print <<"+obj.toString()+">>----------------------");
		
		printRecursive(obj, obj.getClass(),new HashSet<String>());
		
		thisOut("-------------------- end print -----------------------");
	}
	
	private static <T>void printRecursive(T obj,Class<?> clazz,HashSet<String> fields){
		printInternal(obj, clazz, fields);
		if (clazz.getSuperclass() != null && !clazz.getSuperclass().getName().equals("java.lang.Object")) {
			thisOut("===================== print super --> "+clazz.getSuperclass().getName());
			printRecursive(obj,clazz.getSuperclass(),fields);
		}
	}
	
	private static <T>void printInternal(T obj,Class<?> clazz,HashSet<String> fields){
		String methodName = null,fieldName = null,typeName = null;
		for(Method m : clazz.getDeclaredMethods()) {
			methodName = m.getName();
			if (!methodName.matches(REGX_GET_METHOD))
				continue;
			if (m.getParameterTypes() != null && m.getParameterTypes().length > 0)
				continue;
			fieldName = getFieldNameByMethod(methodName);			
			typeName = getFieldTypeByMethod(m);
			try {
				m.setAccessible(true);//thisOut(fields+fieldName);
				Object v = fields.contains(fieldName) ? "@override" : m.invoke(obj);
				pInternal(typeName, fieldName, v);
				fields.add(fieldName);
				new MFVT(m,v);
			} catch (Exception e) {
				//e.printStackTrace();
				thisOut("ERROR  method.invoke(obj), missing arguments: "+fieldName);
				pInternal(typeName, fieldName,null);
			} finally {
				m.setAccessible(false);
			}
		}//
	}
	
	public static <T>List<MFVT> MV(T obj){
		List<MFVT> mvs = new ArrayList<MFVT>();
		if (obj == null)
			return mvs;
		MVRecursive(obj, obj.getClass(),mvs,new HashSet<String>());
		return mvs;
	}
	
	private static <T>void MVRecursive(T obj,Class<?> clazz,List<MFVT> mvs,HashSet<String> fields){
		MVInternal(obj, clazz, mvs, fields);
		if (clazz.getSuperclass() != null && !clazz.getSuperclass().getName().equals("java.lang.Object")) {
			//thisOut("===================== for super --> "+clazz.getSuperclass().getName());
			MVRecursive(obj,clazz.getSuperclass(),mvs,fields);
		}
	}
	
	private static <T>void MVInternal(T obj,Class<?> clazz,List<MFVT> mvs,HashSet<String> fields){
		String methodName = null,fieldName = null;
		for(Method m : clazz.getDeclaredMethods()) {
			methodName = m.getName();
			if (!methodName.matches(REGX_GET_METHOD))
				continue;
			if (m.getParameterTypes() != null && m.getParameterTypes().length > 0)
				continue;
			fieldName = getFieldNameByMethod(methodName);
			try {
				m.setAccessible(true);//thisOut(fields+fieldName);
				Object v = "@override";
				if (!fields.contains(fieldName)) {
					v = m.invoke(obj);
					mvs.add(new MFVT(m,v));					
				}					
				fields.add(fieldName);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				m.setAccessible(false);
			}
		}//
	}
	
	
	public static class MFVT{
		private String fieldName;
		private String methodName;
		private Object value;
		private Type type;
				
		public MFVT(Method method,Object value) {
			this.methodName = method.getName();
			this.fieldName = getFieldNameByMethod(methodName);
			type = method.getGenericReturnType();
			this.value = value;
		}
		public String getMethodName() {
			return methodName;
		}
		public String getFieldName() {
			return fieldName;
		}
		public Object getValue() {
			return value;
		}
		public Type getType() {
			return type;
		}
		public Class<?> getTypeClass() {
			try {
				return Class.forName(type.toString().replaceAll("class", "").trim());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}		
		
		public String toShow() {
			return "[type] "+getTypeClass().getName()+
					" \t-->[key] "+fieldName+
					" \t-->[value] "+value;
		}
		
		
	}
	private static <T>void pInternal(String typeName,String fieldName,Object value){
		String str = "[type] "+typeName+
				" \t-->[key] "+fieldName+
				" \t-->[value] "+value;			
		thisOut(str);
	}
	
	/*private static <T>void pInternal(T obj,Field f){
		String str = "[type] "+f.getType().getName().replace("java.lang.", "").replace("java.util.", "")+
				" \t-->[key] "+f.getName()+
				" \t-->[value] "+getObjVal(obj,f);			
		thisOut(str);
	}*/
	
	//getFileName或者fileName
	public static String getFieldNameByMethod(String methodName){
		return StringUtil.toLowCaptureName(methodName.replaceAll(REGX_GET_METHOD, "$2"));
	}
	
	
	
	
	public static String getFieldTypeByMethod(Method method){
		return method.getGenericReturnType().toString().trim().replaceAll(REGX_GET_FIELD_TYPE, "");
	}
	
	//getFileName或者fileName
	public static String getFieldTypeByMethod(Class<?> clazz,String methodName){
		//这里只考虑get
		methodName = methodName.matches(REGX_GET_METHOD) ? methodName : "get"+StringUtil.toCaptureName(methodName);
		return getFieldTypeByMethod(getMethodByName(clazz, methodName));
	}
	
	public static Method getMethodByName(Class<?> clazz,String methodName){
		for (Method method : getAllMethods(clazz)) {
			if (methodName.equals(method.getName()))
				return method;
		}
		return null;
	}
	
	public static Field getFieldByName(Class<?> clazz,String fieldName){
		for (Field field : getAllFields(clazz)) {
			if (fieldName.equals(field.getName()))
				return field;
		}
		return null;
	}
	
	
	
	private static void printMap(Map<?,?> map) {
		for (Entry<?, ?> e : map.entrySet()) {
			thisOut("===========[map-key] "+e.getKey()+" \t-->[map-value] ===========start");
			print(e.getValue());
			thisOut("===========[map-key] "+e.getKey()+" \t-->[map-value] ===========end");
		}
	}
	private static void printCollection(Collection<?> list){
		for (Object obj : list) {
			thisOut("===========[collection-value] ===========start");
			print(obj);
			thisOut("===========[collection-value] ===========end");
		}
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
	
	public static <T> String getObjVal(T obj,String f) {
		return getObjVal(obj, getFieldByName(obj.getClass(), getFieldNameByMethod(f)));
	}
	
	public static <T> void setRecentUpdate(T obj,String usr) {
		try {		
			Class<?> clazz = obj.getClass();
			Method mut = getClassMethod(clazz,"setRecUpdTs");
			mut.invoke(obj, new Date());						
			
			Method murs = getClassMethod(clazz,"setRecUpdUsr");
			Method murg = getClassMethod(clazz,"getRecUpdUsr");
			if (murg.invoke(obj) == null || murg.invoke(obj).toString().equals("")) {				
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
	
	public static HashSet<Method> getAllMethods(Class<?> clazz){
		HashSet<Method> methods = new HashSet<Method>();
		getAllMethodsRecursive(clazz, methods);	
		return methods;
	}


	public static String getSelectStr(Class<?> clazz){
		return getSelectStr(clazz, "");
	}
	public static String getSelectStr(Class<?> clazz,String tblPre,String... except){
		StringBuilder sb = new StringBuilder();
		HashSet<String> fields = ClazzHelper.getAllFieldStrings(clazz);
		
		List<String> exceptList = (except == null || except.length == 0 ) ? new ArrayList<String>() : Arrays.asList(except);
		
		for (String f : fields) {
			if (exceptList.contains(f)) {
				continue;
			}
			sb.append(tblPre).append(StringUtil.toUnderlineCase(f)).append(",");
		}
		return sb.toString().replaceAll("^(.*),$", "$1");
	}
	public static HashSet<String> getAllFieldStrings(Class<?> clazz){
		HashSet<String> fields = new HashSet<String>();
		for (Field f : getAllFields(clazz)) {
			fields.add(f.getName());
		}	
		return fields;
	}
	
	public static HashSet<Field> getAllFields(Class<?> clazz){
		HashSet<Field> fields = new HashSet<Field>();
		getAllFieldsRecursive(clazz, fields);	
		return fields;
	}
	
	private static void getAllFieldsRecursive(Class<?> clazz,HashSet<Field> fields){
		//从这里找getDeclaredFields
		for (Field f : clazz.getDeclaredFields()) {
			// 如果是编译器引入的则跳过
			if (f.isSynthetic() || f.isEnumConstant())
				continue;
			// static则跳过
			if (Modifier.isStatic(f.getModifiers()))
				continue;
			//@cn.pengh.annotation.ExceptField
			if (f.getAnnotation(ExceptField.class) != null)
				continue;
			
			fields.add(f);
		}	
		//从这里找getMethod
		/*for (Method m : clazz.getDeclaredMethods()) {
			if (!m.getName().startsWith("get"))
				continue;
			fields.add(new Field);
		}*/
		
		Class<?> superclass = clazz.getSuperclass();
		if (superclass != null){
			getAllFieldsRecursive(superclass, fields);
		}	
	}
	
	private static void getAllMethodsRecursive(Class<?> clazz,HashSet<Method> methods){
		//从这里找getDeclaredFields
		for (Method m : clazz.getDeclaredMethods()) {
			// 如果是编译器引入的则跳过
			if (m.isSynthetic() )
				continue;
			// static则跳过
			if (Modifier.isStatic(m.getModifiers()))
				continue;
			//@cn.pengh.annotation.ExceptField
			if (m.getAnnotation(ExceptField.class) != null)
				continue;
			
			methods.add(m);
		}	
		
		Class<?> superclass = clazz.getSuperclass();
		if (superclass != null){
			getAllMethodsRecursive(superclass, methods);
		}	
	}
	
	public static String[] getNullFieldArr(Object obj){
		List<String> nullFields = getNullFields(obj);
		return nullFields.toArray(new String[nullFields.size()]);
	}
	
	public static List<String> getNullFields(Object obj){
		List<String> nullFields = new ArrayList<String>();
		getNullFieldsIntenal(obj, obj.getClass(), nullFields);
		return nullFields;
	}
	
	
	public static void getNullFieldsIntenal(Object obj, Class<?> clazz, List<String> nullFields){
		for (Field f : clazz.getDeclaredFields()) {
			try {
				f.setAccessible(true);
				if(f.get(obj) == null)
					nullFields.add(f.getName());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				f.setAccessible(false);
			}
		}
		
		Class<?> superclass = clazz.getSuperclass();
		if (superclass != null){
			getNullFieldsIntenal(obj, superclass, nullFields);
		}
	}
	
	public static void setBatchValue(Object obj,Class<?> clazz,Map<String,Object> map){
		List<String> list = new ArrayList<String>();
		for (Entry<String,Object> e : map.entrySet()) {
			list.add(e.getKey());
		}		
		setBatchValueRecursive(obj, clazz, list, map);
		
		for (String k : list) {
			try {
				if (getClazzFieldByNm(clazz, k) == null) {	
					Log.debug("Field == null,"+k);
					setStringFieldValue(k, (String)map.get(k), obj);
				} 		
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void setBatchValueRecursive(Object obj,Class<?> clazz,List<String> list,Map<String,Object> map){
		setBatchValueRecursiveIntenal(obj, clazz, list, map);		
		Class<?> superclass = clazz.getSuperclass();
		if (superclass != null){
			setBatchValueRecursive(obj, superclass, list, map);
		}
			
	}
	
	private static void setBatchValueRecursiveIntenal(Object obj,Class<?> clazz,List<String> list,Map<String,Object> map){
		for (Field f : clazz.getDeclaredFields()){
			String k = f.getName();
			if (!list.contains(k)){
				continue;
			}
				
			String t = f.getType().getName();
			try {
				if (f == null || "java.lang.String".equals(t)) {					
					setStringFieldValue(k, (String)map.get(k), obj);
				} else {
					setFieldValue(k, map.get(k), obj);
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static <T> void setStringFieldValue(String k,String v,T obj){
		String kf = k.substring(0,1).toUpperCase() + k.substring(1);
		try {
			Method m = obj.getClass().getMethod("set" + kf,String.class);
			m.invoke(obj, v);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static <T> void setFieldValue(String f,Object v,T obj){
		setFieldValue(getClazzFieldByNm(obj.getClass(), f), v, obj);
	}
	
	public static <T> void setFieldValue(Field f,Object v,T obj){
		if (f == null)
			return;
		String t = f.getType().getName();
		try {
			f.setAccessible(true);
			if ("java.lang.Byte".equals(t)){
				v = CurrencyUtil.convert(v).byteValue();
			} else if ("java.lang.Short".equals(t)){
				v = CurrencyUtil.convert(v).shortValue();
			} else if ("java.lang.Integer".equals(t)){
				v  = CurrencyUtil.convert(v).intValue();
			} else if ("java.lang.Long".equals(t)){
				v  = CurrencyUtil.convert(v).longValue();
			}
			f.set(obj, v);		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			f.setAccessible(false);
		}
	}
	
	
	public static void main(String[] args) {
		//Log.debug(getSelectStr(Person.class));
		//ClazzHelper.print(new Person("",8));
	}
}
